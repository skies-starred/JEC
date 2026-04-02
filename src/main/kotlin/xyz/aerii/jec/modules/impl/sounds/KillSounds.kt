package xyz.aerii.jec.modules.impl.sounds

import net.minecraft.world.entity.LivingEntity
import xyz.aerii.jec.annotations.Load
import xyz.aerii.jec.config.categories.SoundsCategory
import xyz.aerii.jec.events.EntityEvent
import xyz.aerii.jec.events.SoundPlayEvent
import xyz.aerii.jec.events.core.runWhen
import xyz.aerii.jec.modules.Module

@Load
object KillSounds : Module(SoundsCategory.killSounds) {
    init {
        on<EntityEvent.Death> {
            if (entity !is LivingEntity) return@on
            SoundsCategory.killSound.pk()
        }

        on<SoundPlayEvent> {
            if (!location.path.endsWith(".death")) return@on
            cancel()
        }.runWhen(SoundsCategory.killSoundsCancel)
    }
}