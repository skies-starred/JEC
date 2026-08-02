package foo.starred.jec.mixin.mixins;

import foo.starred.jec.events.EntityEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setPose(Lnet/minecraft/world/entity/Pose;)V"))
    private void jec$die(DamageSource damageSource, CallbackInfo ci) {
        new EntityEvent.Death((Entity) (Object) this).post();
    }
}
