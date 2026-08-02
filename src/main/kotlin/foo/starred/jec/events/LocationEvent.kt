package foo.starred.jec.events

import foo.starred.jec.events.core.Event

sealed class LocationEvent {
    sealed class Server {
        data object Connect : Event()

        data object Disconnect : Event()
    }
}