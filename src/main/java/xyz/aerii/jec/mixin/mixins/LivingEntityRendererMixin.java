package xyz.aerii.jec.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state/*? >= 26.1 {*//*.level*//*? }*/.CameraRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aerii.jec.accessors.CatRenderStateAccessor;
import xyz.aerii.jec.accessors.EntityRenderStateAccessor;
import xyz.aerii.jec.accessors.PlayerAccessor;
import xyz.aerii.jec.config.categories.RenderCategory;
import xyz.aerii.jec.modules.impl.render.CatModels;

import static xyz.aerii.jec.utils.ClientUtilsKt.getClient;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @SuppressWarnings({"unchecked", "rawtypes"})
    //~ if >= 26.1 'CameraRenderState;)V' -> 'level/CameraRenderState;)V'
    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("HEAD"), cancellable = true)
    private void jec$submit(LivingEntityRenderState a, PoseStack b, SubmitNodeCollector c, CameraRenderState d, CallbackInfo ci) {
        if (!CatModels.INSTANCE.getEnabled()) return;
        if (!(a instanceof AvatarRenderState e)) return;

        final var f = ((EntityRenderStateAccessor) a).jec$getEntity();
        if (!(f instanceof Player g)) return;
        if (!CatModels.fn3(g)) return;

        final var h = getClient().getEntityRenderDispatcher().renderers.get(EntityType.CAT);
        final var i = h != null ? h.createRenderState() : null;

        if (!(i instanceof CatRenderState j)) return;

        ((CatRenderStateAccessor) j).jec$setPlayer(g);

        CatModels.getB().extractRenderState(g, j, getClient().getDeltaTracker().getGameTimeDeltaPartialTick(true));
        j.isSitting = e.isCrouching;
        j.entityType = EntityType.CAT;

        if (j.nameTagAttachment != null) j.nameTagAttachment = j.nameTagAttachment.subtract(0.0, 1.2, 0.0);

        final PlayerAccessor k = ((PlayerAccessor) g);
        j.collarColor = k.jec$collar();

        boolean baby = k.jec$baby();
        ResourceLocation var = k.jec$variant();

        //? >= 26.1 {
        /*if (baby) {
            final ResourceLocation var2 = k.jec$variant2();
            if (var2 != null) var = var2;
            else baby = false;
        }
        *///? }

        j.isBaby = baby;
        if (var != null) j.texture = var;

        ((EntityRenderer) h).submit(j, b, c, d);
        ci.cancel();
    }

    @Inject(method = "scale", at = @At("HEAD"))
    private void jec$scale(LivingEntityRenderState a, PoseStack b, CallbackInfo ci) {
        if (!(a instanceof CatRenderState)) return;
        if (!CatModels.INSTANCE.getEnabled()) return;
        if (!RenderCategory.INSTANCE.getCatScale()) return;

        final Player c = ((CatRenderStateAccessor) a).jec$getPlayer();
        if (c == null) return;

        final boolean d = ((PlayerAccessor) c).jec$scale();
        if (!d) return;

        final float x = RenderCategory.INSTANCE.getCatScaleX();
        final float y = RenderCategory.INSTANCE.getCatScaleY();
        final float z = RenderCategory.INSTANCE.getCatScaleZ();

        b.scale(x, y, z);
        if (RenderCategory.INSTANCE.getCatScaleNametag()) if (a.nameTagAttachment != null) a.nameTagAttachment = a.nameTagAttachment.multiply(1.0, y, 1.0);
    }
}