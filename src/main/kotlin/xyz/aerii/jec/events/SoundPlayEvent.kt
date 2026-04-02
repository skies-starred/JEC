package xyz.aerii.jec.events

import net.minecraft.resources.ResourceLocation
import xyz.aerii.jec.events.core.CancellableEvent

data class SoundPlayEvent(val location: ResourceLocation) : CancellableEvent()