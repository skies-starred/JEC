package foo.starred.jec.events

import foo.starred.jec.events.core.Event

sealed class GameEvent : Event() {
    data object Start : GameEvent()

    data object Stop : GameEvent()
}