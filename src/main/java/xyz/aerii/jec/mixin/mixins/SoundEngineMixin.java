package xyz.aerii.jec.mixin.mixins;

import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aerii.jec.events.SoundPlayEvent;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {
    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    private void jec$play(SoundInstance sound, CallbackInfoReturnable<SoundEngine.PlayResult> cir) {
        if (sound == null) return;
        if (!(sound instanceof AbstractSoundInstance)) return;

        //~ if >= 1.21.11 '.getLocation()' -> '.getIdentifier()'
        if (new SoundPlayEvent(sound.getLocation(), sound.getX(), sound.getY(), sound.getZ()).post()) cir.setReturnValue(SoundEngine.PlayResult.NOT_STARTED);
    }
}