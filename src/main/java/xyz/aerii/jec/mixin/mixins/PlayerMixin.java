package xyz.aerii.jec.mixin.mixins;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xyz.aerii.jec.accessors.PlayerAccessor;
import xyz.aerii.jec.modules.impl.render.CatModels;

//? >= 26.1
//import xyz.aerii.jec.config.other.CatVariant;

@Mixin(Player.class)
public class PlayerMixin implements PlayerAccessor {
    @Unique
    @Nullable ResourceLocation variant;

    @Unique
    @Nullable ResourceLocation variant2;

    @Unique
    @Nullable DyeColor collar;

    @Unique
    int baby = -1;

    @Unique
    int scale = -1;

    @Override
    public ResourceLocation jec$variant() {
        if (variant == null) {
            variant = CatModels.fn0(self());
            //? >= 26.1
            //variant2 = baby();
        }

        return variant;
    }

    @Override
    public ResourceLocation jec$variant2() {
        return variant2;
    }

    @Override
    public DyeColor jec$collar() {
        if (collar == null) collar = CatModels.fn2();
        return collar;
    }

    @Override
    public boolean jec$baby() {
        if (baby == -1) {
            if (CatModels.fn1(self())) baby = 1;
            else baby = 0;
        }

        return baby == 1;
    }

    @Override
    public boolean jec$scale() {
        if (scale == -1) {
            if (CatModels.fn5(self())) scale = 1;
            else scale = 0;
        }

        return scale == 1;
    }

    @Unique
    private Player self() {
        return (Player) (Object) this;
    }

    //? >= 26.1 {
    /*@Unique
    @Nullable
    private ResourceLocation baby() {
        final CatVariant var = CatVariant.getEntries().stream().filter(v -> v.getResourceLocation() == variant).findFirst().orElse(null);
        if (var == null) return null;

        return var.getResourceLocationBaby();
    }
    *///? }
}