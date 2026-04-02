package xyz.aerii.jec.modules.impl.chat

import xyz.aerii.jec.annotations.Load
import xyz.aerii.jec.config.categories.ChatCategory
import xyz.aerii.jec.events.MessageEvent
import xyz.aerii.jec.modules.Module
import xyz.aerii.jec.utils.client
import xyz.aerii.jec.utils.message

@Load
object AutoMeow : Module(ChatCategory.autoMeow) {
    val r = Regex("\\b${Regex.escape(client.user.name)}\\b")

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

            if (r.containsMatchIn(s)) return@on
            if (!ChatCategory.autoMeowKeywords.any { if (a) it in s0 else it == s0 }) return@on

            ChatCategory.autoMeowResponses.random().message()
        }
    }
}