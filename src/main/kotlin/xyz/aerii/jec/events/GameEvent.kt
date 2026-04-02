package xyz.aerii.jec.events

import xyz.aerii.jec.events.core.Event

sealed class GameEvent {
    data object Start : Event()

    data object Stop : Event()
}