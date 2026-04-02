package xyz.aerii.jec.utils

import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent

@JvmOverloads
fun SoundEvent.play(volume: Float = 1f, pitch: Float = 1f) {
    client.player?.playSound(this, volume, pitch)
}

fun String.sound(): SoundEvent? {
    val p = ResourceLocation.tryParse(this) ?: return null
    return SoundEvent.createVariableRangeEvent(p)
}