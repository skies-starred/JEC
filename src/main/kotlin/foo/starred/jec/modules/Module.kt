package foo.starred.jec.modules

import foo.starred.jec.events.PacketEvent
import foo.starred.jec.events.core.Event
import foo.starred.jec.events.core.runWhen
import foo.starred.snowbird.api.data.Observable
import net.minecraft.network.protocol.Packet

open class Module(
    val observable: Observable<Boolean>
) {
    var enabled: Boolean = false
        private set

    init {
        enabled = observable.value
        observable.onChange { enabled = it }
    }

    protected inline fun <reified T : Event> on(
        priority: Int = 0,
        noinline handler: T.() -> Unit
    ) = foo.starred.jec.events.core.on<T>(priority, handler).runWhen(observable)

    protected inline fun <reified E : PacketEvent, reified P : Packet<*>> on(
        priority: Int = 0,
        noinline handler: P.(E) -> Unit
    ) = foo.starred.jec.events.core.on<E, P>(priority, handler).runWhen(observable)
}
