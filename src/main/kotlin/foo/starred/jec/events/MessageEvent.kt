package foo.starred.jec.events

import foo.starred.jec.events.core.CancellableEvent
import foo.starred.jec.events.core.Event
import foo.starred.snowbird.utils.stripped
import net.minecraft.network.chat.Component

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