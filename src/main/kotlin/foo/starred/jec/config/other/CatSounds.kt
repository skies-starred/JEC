package foo.starred.jec.config.other

import foo.starred.jec.config.categories.SoundsCategory
import foo.starred.snowbird.utils.play
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent

enum class CatSounds(val ins: SoundEvent?) {
    RANDOM(null),

    AMBIENT(SoundEvent.createVariableRangeEvent(Identifier.withDefaultNamespace("entity.cat.ambient"))),
    EAT(SoundEvent.createVariableRangeEvent(Identifier.withDefaultNamespace("entity.cat.eat"))),
    HISS(SoundEvent.createVariableRangeEvent(Identifier.withDefaultNamespace("entity.cat.hiss"))),
    HURT(SoundEvent.createVariableRangeEvent(Identifier.withDefaultNamespace("entity.cat.hurt"))),
    PURR(SoundEvent.createVariableRangeEvent(Identifier.withDefaultNamespace("entity.cat.purr"))),
    PURREOW(SoundEvent.createVariableRangeEvent(Identifier.withDefaultNamespace("entity.cat.purreow"))),
    STRAY(SoundEvent.createVariableRangeEvent(Identifier.withDefaultNamespace("entity.cat.stray_ambient")));

    fun pc() {
        play(SoundsCategory.chatSoundVolume, SoundsCategory.chatSoundPitch)
    }

    fun pk(x: Double, y: Double, z: Double) {
        if (x == 0.0 && y == 0.0 && z == 0.0) return play(SoundsCategory.killSoundVolume, SoundsCategory.killSoundPitch)
        play0(x, y, z, SoundsCategory.killSoundVolume, SoundsCategory.killSoundPitch)
    }

    fun pr(x: Double, y: Double, z: Double) {
        if (x == 0.0 && y == 0.0 && z == 0.0) return play(SoundsCategory.replacedSoundVolume, SoundsCategory.replacedSoundPitch)
        play0(x, y, z, SoundsCategory.replacedSoundVolume, SoundsCategory.replacedSoundPitch)
    }

    private fun play(volume: Int, pitch: Int) {
        (ins ?: purreow).play(volume / 10f, pitch / 10f)
    }

    private fun play0(x: Double, y: Double, z: Double, volume: Int, pitch: Int) {
        (ins ?: purreow).play(x, y, z, volume / 10f, pitch / 10f)
    }

    override fun toString(): String {
        return name.lowercase().replaceFirstChar { it.uppercase() }
    }

    companion object {
        val purreow: SoundEvent = SoundEvent.createVariableRangeEvent(Identifier.withDefaultNamespace("entity.cat.purreow"))
        val all: List<CatSounds> = entries.filter { it != RANDOM }
    }
}