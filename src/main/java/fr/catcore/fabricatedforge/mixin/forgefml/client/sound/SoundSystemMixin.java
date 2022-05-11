package fr.catcore.fabricatedforge.mixin.forgefml.client.sound;

import fr.catcore.fabricatedforge.mixininterface.ISoundSystem;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundLoader;
import net.minecraft.client.sound.SoundSystem;
import net.minecraftforge.client.ModCompatibilityClient;
import net.minecraftforge.client.event.sound.*;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(SoundSystem.class)
public class SoundSystemMixin implements ISoundSystem {

    @Shadow private static boolean started;
    @Shadow private GameOptions options;
    @Shadow public static paulscode.sound.SoundSystem soundSystem;
    @Shadow private int field_2267;
    @Shadow public SoundLoader musicLoader;
    @Shadow private Random rnd;
    @Shadow public SoundLoader bgmusicLoader;
    @Shadow public SoundLoader soundsLoader;
    @Shadow private int field_2263;

    // This should be public.
    private static int MUSIC_INTERVAL = 12000;

    @ModifyArg(method = "<init>", index = 0, at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", remap = false))
    private int ctr(int i) {
        return MUSIC_INTERVAL;
    }

    @Inject(method = "method_1709", at = @At("RETURN"))
    private void audioModLoad(GameOptions par1, CallbackInfo ci) {
        ModCompatibilityClient.audioModLoad((SoundSystem)(Object) this);
        MinecraftForge.EVENT_BUS.post(new SoundLoadEvent((SoundSystem)(Object) this));
    }

    @Inject(method = "startSoundSystem", at = @At(value = "NEW", remap = false, target = "Lpaulscode/sound/SoundSystem;<init>()V"))
    private void audioModLoadCodecs(CallbackInfo ci) {
        ModCompatibilityClient.audioModAddCodecs();
        MinecraftForge.EVENT_BUS.post(new SoundSetupEvent((SoundSystem)(Object) this));
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void method_1717() {
        if (started && this.options.musicVolume != 0.0F && !soundSystem.playing("BgMusic") && !soundSystem.playing("streaming")) {
            if (this.field_2267 > 0) {
                --this.field_2267;
                return;
            }

            Sound var1 = this.musicLoader.getSoundRandom();
            var1 = ModCompatibilityClient.audioModPickBackgroundMusic((SoundSystem)(Object) this, var1);
            var1 = SoundEvent.getResult(new PlayBackgroundMusicEvent((SoundSystem)(Object) this, var1));
            if (var1 != null) {
                this.field_2267 = this.rnd.nextInt(MUSIC_INTERVAL) + MUSIC_INTERVAL;
                soundSystem.backgroundMusic("BgMusic", var1.field_2258, var1.field_2257, false);
                soundSystem.setVolume("BgMusic", this.options.musicVolume);
                soundSystem.play("BgMusic");
            }
        }

    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void playBackgroundMusic(String par1Str, float par2, float par3, float par4, float par5, float par6) {
        if (started && (this.options.soundVolume != 0.0F || par1Str == null)) {
            String var7 = "streaming";
            if (soundSystem.playing("streaming")) {
                soundSystem.stop("streaming");
            }

            if (par1Str != null) {
                Sound var8 = this.bgmusicLoader.getSound(par1Str);
                var8 = SoundEvent.getResult(new PlayStreamingEvent((SoundSystem)(Object) this, var8, par1Str, par2, par3, par4));
                if (var8 != null && par5 > 0.0F) {
                    if (soundSystem.playing("BgMusic")) {
                        soundSystem.stop("BgMusic");
                    }

                    float var9 = 16.0F;
                    soundSystem.newStreamingSource(true, var7, var8.field_2258, var8.field_2257, false, par2, par3, par4, 2, var9 * 4.0F);
                    soundSystem.setVolume(var7, 0.5F * this.options.soundVolume);
                    MinecraftForge.EVENT_BUS.post(new PlayStreamingSourceEvent((SoundSystem)(Object) this, var7, par2, par3, par4));
                    soundSystem.play(var7);
                }
            }
        }

    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void playSound(String par1Str, float par2, float par3, float par4, float par5, float par6) {
        if (started && this.options.soundVolume != 0.0F) {
            Sound var7 = this.soundsLoader.getSound(par1Str);
            var7 = SoundEvent.getResult(new PlaySoundEvent((SoundSystem)(Object) this, var7, par1Str, par2, par3, par4, par5, par6));
            if (var7 != null && par5 > 0.0F) {
                this.field_2263 = (this.field_2263 + 1) % 256;
                String var8 = "sound_" + this.field_2263;
                float var9 = 16.0F;
                if (par5 > 1.0F) {
                    var9 *= par5;
                }

                soundSystem.newSource(par5 > 1.0F, var8, var7.field_2258, var7.field_2257, false, par2, par3, par4, 2, var9);
                soundSystem.setPitch(var8, par6);
                if (par5 > 1.0F) {
                    par5 = 1.0F;
                }

                soundSystem.setVolume(var8, par5 * this.options.soundVolume);
                MinecraftForge.EVENT_BUS.post(new PlaySoundSourceEvent((SoundSystem)(Object) this, var8, par2, par3, par4));
                soundSystem.play(var8);
            }
        }

    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void playSound(String par1Str, float par2, float par3) {
        if (started && this.options.soundVolume != 0.0F) {
            Sound var4 = this.soundsLoader.getSound(par1Str);
            var4 = SoundEvent.getResult(new PlaySoundEffectEvent((SoundSystem)(Object) this, var4, par1Str, par2, par3));
            if (var4 != null) {
                this.field_2263 = (this.field_2263 + 1) % 256;
                String var5 = "sound_" + this.field_2263;
                soundSystem.newSource(false, var5, var4.field_2258, var4.field_2257, false, 0.0F, 0.0F, 0.0F, 0, 0.0F);
                if (par2 > 1.0F) {
                    par2 = 1.0F;
                }

                par2 *= 0.25F;
                soundSystem.setPitch(var5, par3);
                soundSystem.setVolume(var5, par2 * this.options.soundVolume);
                MinecraftForge.EVENT_BUS.post(new PlaySoundEffectSourceEvent((SoundSystem)(Object) this, var5));
                soundSystem.play(var5);
            }
        }

    }

    @Override
    public int getMUSIC_INTERVAL() {
        return MUSIC_INTERVAL;
    }

    @Override
    public void setMUSIC_INTERVAL(int interval) {
        MUSIC_INTERVAL = interval;
    }
}
