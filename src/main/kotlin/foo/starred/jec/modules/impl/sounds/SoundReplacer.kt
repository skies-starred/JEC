package foo.starred.jec.modules.impl.sounds

import foo.starred.jec.annotations.Load
import foo.starred.jec.config.categories.SoundsCategory
import foo.starred.jec.config.other.CatSounds
import foo.starred.jec.events.LocationEvent
import foo.starred.jec.events.SoundPlayEvent
import foo.starred.jec.modules.Module
import foo.starred.snowbird.utils.play
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent
import java.util.concurrent.ConcurrentHashMap

@Load
object SoundReplacer : Module(SoundsCategory.replaceSounds) {
    var map: ConcurrentHashMap<Identifier, Sound> = ConcurrentHashMap()

    val random: SoundEvent
        get() = CatSounds.all.random().ins ?: CatSounds.purreow

    val float: Float
        get() = (1..10).random() / 10f

    init {
        on<SoundPlayEvent> {
            if (location.path.startsWith("entity.cat")) return@on

            cancel()

            if (SoundsCategory.replacedSound != CatSounds.RANDOM) return@on SoundsCategory.replacedSound.pr(x, y, z)
            if (SoundsCategory.randomiseEvery) return@on random.play(x, y, z, float, float)

            map.getOrPut(location) { Sound(random) }.play(x, y, z)
        }

        on<LocationEvent.Server.Connect> {
            if (SoundsCategory.randomiseWorld) map.clear()
        }
    }

    data class Sound(val sound: SoundEvent) {
        val volume: Float = float
        val pitch: Float = float

        fun play(x: Double, y: Double, z: Double) {
            sound.play(x, y, z, volume, pitch)
        }
    }
}