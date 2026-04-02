package xyz.aerii.jec.modules.impl.chat

import xyz.aerii.jec.annotations.Load
import xyz.aerii.jec.config.categories.ChatCategory
import xyz.aerii.jec.events.MessageEvent
import xyz.aerii.jec.modules.Module

@Load
object CatifyMessages : Module(ChatCategory.catifyMessages) {
    private val r = Regex("^/(gc|pc|ac|oc|message|tell|r|reply|msg|w|say)\\s\\S+.*$")

    init {
        on<MessageEvent.Chat.Send> {
            message = fn(message)
        }

        on<MessageEvent.Chat.Command> {
            if (!r.matches(message)) return@on
            message = fn(message)
        }
    }

    private fun fn(s: String): String {
        if (ChatCategory.catifyWords.isEmpty()) return s

        val p = s.split(" ").takeIf { it.size > 1 } ?: return s
        val a = p[0].startsWith("/")
        if (a && !ChatCategory.affectCommands) return s

        val l = p.lastIndex
        val result = buildString {
            for ((i, w) in p.withIndex()) {
                val b = i == l

                append(w)
                if (a && i < 2) continue

                if ((0..99).random() >= ChatCategory.catifyChance) {
                    if (!b) append(" ")
                    continue
                }

                val r = (0..99).random()
                val count = if (r < 50) 1 else if (r < 85) 2 else 2 + (0..1).random()

                repeat(count) {
                    append(" ")
                    append(ChatCategory.catifyWords.random())
                }

                if (!b) append(" ")
            }
        }

        return result
    }
}