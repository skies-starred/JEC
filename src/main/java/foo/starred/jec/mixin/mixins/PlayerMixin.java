package foo.starred.jec.mixin.mixins;

import foo.starred.jec.accessors.PlayerAccessor;
import foo.starred.jec.modules.impl.render.CatModels;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//? >= 26.1
import foo.starred.jec.config.other.CatVariant;

@Mixin(Player.class)
public class PlayerMixin implements PlayerAccessor {
    @Unique
    @Nullable Identifier variant;

    @Unique
    @Nullable Identifier variant2;

    @Unique
    @Nullable DyeColor collar;

    @Unique
    int baby = -1;

    @Unique
    int scale = -1;

    @Override
    public Identifier jec$variant() {
        if (variant == null) {
            variant = CatModels.fn0(self());
            //? >= 26.1
            variant2 = baby();
        }

        return variant;
    }

    @Override
    public Identifier jec$variant2() {
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
    @Unique
    @Nullable
    private Identifier baby() {
        final CatVariant var = CatVariant.getEntries().stream().filter(v -> v.getIdentifier() == variant).findFirst().orElse(null);
        if (var == null) return null;

        return var.getIdentifierBaby();
    }
    //? }
}