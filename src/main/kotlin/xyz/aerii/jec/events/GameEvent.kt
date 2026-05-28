package xyz.aerii.jec.events

import xyz.aerii.jec.events.core.Event

sealed class GameEvent : Event() {
    data object Start : GameEvent()

    data object Stop : GameEvent()
}