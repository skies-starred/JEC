package xyz.aerii.jec.events

import net.minecraft.network.chat.Component
import xyz.aerii.jec.events.core.CancellableEvent
import xyz.aerii.jec.events.core.Event
import xyz.aerii.library.utils.stripped

sealed class MessageEvent {
    sealed class Chat {
        data class Intercept(val message: Component) : CancellableEvent() {
            val stripped = message.stripped()
        }

        data class Receive(val message: Component) : Event() {
            val stripped = message.stripped()
        }

        data class Send(var message: String) : Event()

        data class Command(var message: String) : Event()
    }
}