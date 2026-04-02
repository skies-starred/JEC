package xyz.aerii.jec.mixin.mixins;

import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aerii.jec.accessors.EntityRenderStateAccessor;
import xyz.aerii.jec.modules.impl.render.CatModels;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {
    @Inject(method = "getRenderOffset*", at = @At("HEAD"), cancellable = true)
    private void jec$getRenderOffset(AvatarRenderState state, CallbackInfoReturnable<Vec3> cir) {
        if (!CatModels.INSTANCE.getEnabled()) return;

        var entity = ((EntityRenderStateAccessor) state).jec$getEntity();
        if (entity instanceof Player p && CatModels.fn3(p)) cir.setReturnValue(Vec3.ZERO);
    }
}