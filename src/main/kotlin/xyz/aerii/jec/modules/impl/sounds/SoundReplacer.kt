package xyz.aerii.jec.modules.impl.sounds

import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import xyz.aerii.jec.annotations.Load
import xyz.aerii.jec.config.categories.SoundsCategory
import xyz.aerii.jec.config.other.CatSounds
import xyz.aerii.jec.events.LocationEvent
import xyz.aerii.jec.events.SoundPlayEvent
import xyz.aerii.jec.modules.Module
import xyz.aerii.jec.utils.play
import java.util.concurrent.ConcurrentHashMap

@Load
object SoundReplacer : Module(SoundsCategory.replaceSounds) {
    var map: ConcurrentHashMap<ResourceLocation, Sound> = ConcurrentHashMap()

    val random: SoundEvent
        get() = CatSounds.all.random().ins ?: CatSounds.purreow

    val float: Float
        get() = (1..10).random() / 10f

    init {
        on<SoundPlayEvent> {
            if (location.path.startsWith("entity.cat")) return@on

            cancel()

            if (SoundsCategory.replacedSound != CatSounds.RANDOM) return@on SoundsCategory.replacedSound.pr()
            if (SoundsCategory.randomiseEvery) return@on random.play(float, float)

            map.getOrPut(location) { Sound(random) }.play()
        }

        on<LocationEvent.Server.Connect> {
            if (SoundsCategory.randomiseWorld) map.clear()
        }
    }

    data class Sound(val sound: SoundEvent) {
        val volume: Float = float
        val pitch: Float = float

        fun play() {
            sound.play(volume, pitch)
        }
    }
}