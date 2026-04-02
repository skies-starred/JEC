package xyz.aerii.jec.modules.impl.sounds

import xyz.aerii.jec.annotations.Load
import xyz.aerii.jec.config.categories.SoundsCategory
import xyz.aerii.jec.events.MessageEvent
import xyz.aerii.jec.modules.Module

@Load
object ChatSounds : Module(SoundsCategory.chatSounds) {
    init {
        on<MessageEvent.Chat.Receive> {
            if (SoundsCategory.chatKeywords.isEmpty()) return@on

            val s = stripped.substringAfter(": ", "").takeIf { it.isNotBlank() }?.lowercase() ?: return@on
            if (SoundsCategory.chatKeywords.any { it in s }) SoundsCategory.chatSound.pc()
        }
    }
}