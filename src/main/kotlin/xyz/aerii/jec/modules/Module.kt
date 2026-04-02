package xyz.aerii.jec.modules

import net.minecraft.network.protocol.Packet
import xyz.aerii.jec.events.PacketEvent
import xyz.aerii.jec.events.core.Event
import xyz.aerii.jec.events.core.runWhen
import xyz.aerii.jec.handlers.React

open class Module(
    val react: React<Boolean>
) {
    var enabled: Boolean = false
        private set

    init {
        enabled = react.value
        react.onChange { enabled = it }
    }

    protected inline fun <reified T : Event> on(
        priority: Int = 0,
        noinline handler: T.() -> Unit
    ) = xyz.aerii.jec.events.core.on<T>(priority, handler).runWhen(react)

    protected inline fun <reified E : PacketEvent, reified P : Packet<*>> on(
        priority: Int = 0,
        noinline handler: P.(E) -> Unit
    ) = xyz.aerii.jec.events.core.on<E, P>(priority, handler).runWhen(react)
}