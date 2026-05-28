package xyz.aerii.jec.mixin.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.player.PlayerSkin;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aerii.jec.modules.impl.render.CatCapes;

import java.util.Objects;

import static xyz.aerii.library.api.ClientKt.getClient;

@Mixin(PlayerInfo.class)
public class PlayerInfoMixin {
    @Unique
    @Nullable
    private PlayerSkin jec$cached0 = null;

    @Unique
    @Nullable
    private ClientAsset.Texture jec$cached1 = null;

    @Final
    @Shadow
    private GameProfile profile;

    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    private void jec$getSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        if (!CatCapes.INSTANCE.getEnabled()) return;
        if (!Objects.equals(profile.name(), getClient().getUser().getName())) return;

        final PlayerSkin a = cir.getReturnValue();
        if (a == null) return;

        if (!Objects.equals(jec$cached1, a.body())) {
            jec$cached0 = null;
            jec$cached1 = a.body();
        }

        if (jec$cached0 != null) {
            cir.setReturnValue(jec$cached0);
            return;
        }

        final CatCapes.Cape c = CatCapes.INSTANCE.getMap().get(profile.name());
        if (c == null) return;

        final PlayerSkin e = new PlayerSkin(a.body(), c.getTexture(), a.elytra(), a.model(), a.secure());
        if (e.equals(a)) return;

        jec$cached0 = e;
        cir.setReturnValue(e);
    }
}