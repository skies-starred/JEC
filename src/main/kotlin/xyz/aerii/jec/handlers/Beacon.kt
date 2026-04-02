@file:Suppress("Unused")

package xyz.aerii.jec.handlers

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import xyz.aerii.jec.JEC
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URI
import java.util.zip.GZIPInputStream

object Beacon {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val gson = Gson()

    @JvmStatic
    @JvmOverloads
    fun get(url: String, log: Boolean = true, block: RequestBuilder.() -> Unit = {}) =
        RequestBuilder(url, "GET", log).apply(block).execute()

    @JvmStatic
    @JvmOverloads
    fun post(url: String, log: Boolean = true, block: RequestBuilder.() -> Unit = {}) =
        RequestBuilder(url, "POST", log).apply(block).execute()

    @JvmStatic
    fun Int.isRetryableError(): Boolean =
        this in listOf(408, 429, 500, 502, 503, 504)

    @JvmStatic
    @JvmOverloads
    suspend fun <T> retry(
        maxRetries: Int = 3,
        initialDelay: Long = 2000,
        maxDelay: Long = 30000,
        backoffMultiplier: Double = 2.0,
        shouldRetry: (Exception) -> Boolean = { true },
        operation: suspend () -> T
    ): T = withContext(Dispatchers.IO) {
        var currentDelay = initialDelay

        repeat(maxRetries) { attempt ->
            try {
                return@withContext operation()
            } catch (e: Exception) {
                if (attempt == maxRetries - 1 || !shouldRetry(e)) throw e
                JEC.LOGGER.warn("Retrying operation (attempt ${attempt + 1}/$maxRetries) due to ${e::class.simpleName}: ${e.message}")
                delay(currentDelay.coerceAtMost(maxDelay))
                currentDelay = (currentDelay * backoffMultiplier).toLong()
            }
        }

        throw IllegalStateException("Retry exhausted")
    }

    class RequestBuilder(private val url: String, private val method: String, private val log: Boolean) {
        val headers = mutableMapOf("User-Agent" to "Mozilla/5.0 (Athen)")
        var body: String? = null
        var onSuccess: (String) -> Unit = {}
        var onError: (Exception) -> Unit = {}
        var connectTimeout = 15_000
        var readTimeout = 45_000
        var maxRetries = 3
        var retryDelay = 2000L

        fun header(key: String, value: String) = apply {
            headers[key] = value
        }

        fun headers(vararg pairs: Pair<String, String>) = apply {
            headers.putAll(pairs)
        }

        fun headers(map: Map<String, String>) = apply {
            headers.putAll(map)
        }

        fun body(data: Any) = apply {
            body = data as? String ?: gson.toJson(data)
            header("Content-Type", "application/json")
        }

        fun json(data: Any) = apply {
            body(data)
        }

        fun timeout(connect: Int = 15_000, read: Int = 45_000) = apply {
            connectTimeout = connect
            readTimeout = read
        }

        fun retries(max: Int, delay: Long = 2000L) = apply {
            maxRetries = max
            retryDelay = delay
        }

        @JvmName($$"method$onSuccess")
        fun onSuccess(block: (String) -> Unit) = apply {
            onSuccess = block
        }

        @JvmName($$"method$onError")
        fun onError(block: (Exception) -> Unit) = apply {
            onError = block
        }

        inline fun <reified T> onSuccess(crossinline block: (T) -> Unit) = apply {
            onSuccess = { response ->
                val type = object : TypeToken<T>() {}.type
                val data: T = gson.fromJson(response, type)
                block(data)
            }
        }

        fun onJsonSuccess(block: (JsonObject) -> Unit) = apply {
            onSuccess = { response ->
                val json = JsonParser.parseString(response).asJsonObject
                block(json)
            }
        }

        fun execute() {
            scope.launch {
                if (log) JEC.LOGGER.info("Sent $method request to $url")

                runCatching {
                    retry(
                        maxRetries = maxRetries,
                        initialDelay = retryDelay,
                        shouldRetry = { it is SocketTimeoutException || (it is HttpException && it.statusCode.isRetryableError()) }
                    ) {
                        performRequest()
                    }
                }.onFailure {
                    JEC.LOGGER.error("Request $method $url failed", it)
                    onError(it as? Exception ?: Exception(it))
                }
            }
        }

        private suspend fun performRequest(): String = withContext(Dispatchers.IO) {
            val connection = createConnection()

            try {
                if (body != null && method in listOf("POST", "PUT", "PATCH")) {
                    connection.doOutput = true
                    connection.outputStream.use { it.write(body!!.toByteArray()) }
                }

                val statusCode = connection.responseCode

                if (statusCode in 200..299) {
                    val inp = connection.inputStream
                    val response = (if ("gzip=true" in url) GZIPInputStream(inp) else inp).bufferedReader().use { it.readText() }
                    if (log) JEC.LOGGER.info("Success in $method for $url → $statusCode (${response.length} bytes)")
                    onSuccess(response)
                    response
                } else {
                    JEC.LOGGER.warn("Error in $method for $url → $statusCode")
                    val error = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "HTTP $statusCode"
                    throw HttpException(error, statusCode)
                }
            } finally {
                connection.disconnect()
            }
        }

        private fun createConnection(): HttpURLConnection {
            return URI(url).toURL().openConnection().apply {
                if ("gzip=true" in this@RequestBuilder.url) header("Accept-Encoding", "gzip")
                setRequestProperty("Accept", "*/*")
                for ((k, v) in headers) setRequestProperty(k, v)
                this.connectTimeout = this@RequestBuilder.connectTimeout
                this.readTimeout = this@RequestBuilder.readTimeout
                (this as HttpURLConnection).requestMethod = method
            } as HttpURLConnection
        }
    }

    data class HttpException(override val message: String, val statusCode: Int) : Exception(message)
}