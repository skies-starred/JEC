package foo.starred.jec.modules.impl.sounds

import foo.starred.jec.annotations.Load
import foo.starred.jec.config.categories.SoundsCategory
import foo.starred.jec.events.EntityEvent
import foo.starred.jec.events.SoundPlayEvent
import foo.starred.jec.events.core.runWhen
import foo.starred.jec.modules.Module
import net.minecraft.world.entity.LivingEntity

@Load
object KillSounds : Module(SoundsCategory.killSounds) {
    init {
        on<EntityEvent.Death> {
            if (entity !is LivingEntity) return@on
            SoundsCategory.killSound.pk(entity.x, entity.y, entity.z)
        }

        on<SoundPlayEvent> {
            if (!location.path.endsWith(".death")) return@on
            cancel()
        }.runWhen(SoundsCategory.killSoundsCancel)
    }
}