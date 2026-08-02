package foo.starred.jec.events

import foo.starred.jec.events.core.CancellableEvent
import net.minecraft.resources.Identifier

data class SoundPlayEvent(
    val location: Identifier,
    val x: Double,
    val y: Double,
    val z: Double
) : CancellableEvent()