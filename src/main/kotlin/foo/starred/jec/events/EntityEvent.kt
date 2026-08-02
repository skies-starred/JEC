package foo.starred.jec.events

import foo.starred.jec.events.core.Event
import net.minecraft.world.entity.Entity

sealed class EntityEvent {
    data class Load(
        val entity: Entity
    ) : Event()

    data class Unload(
        val entity: Entity
    ) : Event()

    data class Death(
        val entity: Entity
    ) : Event()
}