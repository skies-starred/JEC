package foo.starred.jec.modules.impl.chat

import foo.starred.jec.annotations.Load
import foo.starred.jec.config.categories.ChatCategory
import foo.starred.jec.events.MessageEvent
import foo.starred.jec.modules.Module
import foo.starred.snowbird.api.message
import foo.starred.snowbird.api.name

@Load
object AutoMeow : Module(ChatCategory.autoMeow) {
    private val set = setOf("Guild >" to "/gc", "Party >" to "/pc", "Officer >" to "/oc", "Co-op >" to "/cc", "From " to "/r")
    private val r = Regex("\\b${Regex.escape(name)}\\b")

    private var last: String? = null

    init {
        on<MessageEvent.Chat.Receive> {
            if (stripped.isEmpty()) return@on
            if (ChatCategory.autoMeowKeywords.isEmpty()) return@on
            if (ChatCategory.autoMeowResponses.isEmpty()) return@on
            if (stripped.startsWith("To ")) return@on

            val c = stripped.indexOf(": ")
            if (c == -1) return@on

            val s = stripped.substring(0, c).takeIf { it.isNotBlank() } ?: return@on
            val s0 = stripped.substring(c + 2).takeIf { it.isNotBlank() }?.lowercase() ?: return@on
            val a = ChatCategory.autoMeowLooseCheck

            if (r.containsMatchIn(s)) return@on
            if (!ChatCategory.autoMeowKeywords.any { if (a) it in s0 else it == s0 }) return@on ::last.set(null)
            if (last == s) return@on

            last = s
            s.fn(ChatCategory.autoMeowResponses.random()).message()
        }
    }

    fun String.fn(response: String): String {
        for ((k, v) in set) if (startsWith(k)) return "$v $response"
        return "/ac $response"
    }
}