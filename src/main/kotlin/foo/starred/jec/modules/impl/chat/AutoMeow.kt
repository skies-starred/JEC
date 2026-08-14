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

    private val lockedChats = mutableMapOf<String, Long>()

    init {
        on<MessageEvent.Chat.Receive> {
            if (stripped.isEmpty()) return@on
            if (ChatCategory.autoMeowKeywords.isEmpty()) return@on
            if (ChatCategory.autoMeowResponses.isEmpty()) return@on

            val c = stripped.indexOf(": ")
            if (c == -1) return@on

            val s = stripped.substring(0, c).takeIf { it.isNotBlank() } ?: return@on
            val s0 = stripped.substring(c + 2).takeIf { it.isNotBlank() }?.lowercase() ?: return@on
            val a = ChatCategory.autoMeowLooseCheck

            val isKeyword = ChatCategory.autoMeowKeywords.any { if (a) it in s0 else it == s0 }
            val isResponse = ChatCategory.autoMeowResponses.any { if (a) it.lowercase() in s0 else it.lowercase() == s0 }

            val chatKey = when {
                s.startsWith("To ") -> s.substring(3).trim()
                s.startsWith("From ") -> s.substring(5).trim()
                else -> set.firstOrNull { s.startsWith(it.first) }?.first ?: "All"
            }

            val now = System.currentTimeMillis()
            val lockedTime = lockedChats[chatKey]

            if (lockedTime != null && now - lockedTime > 3000L) {
                lockedChats.remove(chatKey)
            }

            if (!isKeyword && !isResponse) {
                lockedChats.remove(chatKey)
                return@on
            }

            if (lockedChats.containsKey(chatKey)) {
                lockedChats[chatKey] = now
                return@on
            }

            lockedChats[chatKey] = now

            if (s.startsWith("To ") || r.containsMatchIn(s) || !isKeyword) return@on

            s.fn(ChatCategory.autoMeowResponses.random()).message()
        }
    }

    fun String.fn(response: String): String {
        for ((k, v) in set) if (startsWith(k)) return "$v $response"
        return "/ac $response"
    }
}