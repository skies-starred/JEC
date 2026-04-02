package xyz.aerii.jec.mixin.mixins;

import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xyz.aerii.jec.accessors.CatRenderStateAccessor;

@Mixin(CatRenderState.class)
public class CatRenderStateMixin implements CatRenderStateAccessor {
    @Unique
    Player jec$player;

    @Override
    public @Nullable Player jec$getPlayer() {
        return jec$player;
    }

    @Override
    public void jec$setPlayer(@NotNull Player player) {
        jec$player = player;
    }
}