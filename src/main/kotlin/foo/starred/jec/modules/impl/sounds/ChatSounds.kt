package foo.starred.jec.modules.impl.sounds

import foo.starred.jec.annotations.Load
import foo.starred.jec.config.categories.SoundsCategory
import foo.starred.jec.events.MessageEvent
import foo.starred.jec.modules.Module

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