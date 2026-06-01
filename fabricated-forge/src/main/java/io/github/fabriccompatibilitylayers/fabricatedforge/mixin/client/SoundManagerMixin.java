/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fr.catcore.cursedmixinextensions.annotations.Public;
import net.minecraft.src.SoundManager;
import net.minecraft.src.SoundPool;
import net.minecraft.src.SoundPoolEntry;
import net.minecraftforge.client.ModCompatibilityClient;
import net.minecraftforge.client.event.sound.*;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulscode.sound.SoundSystem;

@Mixin(SoundManager.class)
public class SoundManagerMixin {

    // Pattern M (@Public): Mixin can't declare new public statics directly;
    // @Public promotes this to public visibility at load time.
    @Public
    private static int MUSIC_INTERVAL = 12000;

    // Pattern F (@ModifyConstant): replaces the hardcoded 12000 in the field
    // initializer (compiled into <init>) so MUSIC_INTERVAL is used instead.
    // Logic delta: 1:1 translation — result is the same value by default,
    // but now the constant is configurable via the public field.
    @ModifyConstant(method = "<init>()V", constant = @Constant(intValue = 12000))
    private int forge$musicInterval(int constant) {
        return MUSIC_INTERVAL;
    }

    // Pattern A (@Inject at RETURN): fires both hooks unconditionally at method exit —
    // simpler than @WrapMethod since we don't need to suppress or wrap the original.
    @Inject(method = "loadSoundSettings", at = @At("RETURN"))
    private void forge$soundLoad(CallbackInfo ci) {
        ModCompatibilityClient.audioModLoad((SoundManager)(Object) this);
        MinecraftForge.EVENT_BUS.post(new SoundLoadEvent((SoundManager)(Object) this));
    }

    // Pattern A (@Inject at NEW): injects before the SoundSystem constructor call so
    // both hooks fire after all codecs are registered but before the system initialises —
    // identical ordering to the patch, without needing to touch the constructor return value.
    @Definition(id = "SoundSystem", type = SoundSystem.class)
    @Expression("new SoundSystem()")
    @Inject(
            method = "tryToSetLibraryAndCodecs",
            at = @At(value = "MIXINEXTRAS:EXPRESSION")
    )
    private void forge$soundSetup(CallbackInfo ci) {
        ModCompatibilityClient.audioModAddCodecs();
        MinecraftForge.EVENT_BUS.post(new SoundSetupEvent((SoundManager)(Object) this));
    }

    // Pattern E (@WrapOperation on INVOKE): intercepts getRandomSound() and pipes its result
    // through both hooks before returning — @WrapOperation's return value directly replaces
    // the call's result in bytecode, so var1 sees the event-filtered entry with no LocalRef needed.
    @WrapOperation(
        method = "playRandomMusicIfReady",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/SoundPool;getRandomSound()Lnet/minecraft/src/SoundPoolEntry;")
    )
    private SoundPoolEntry forge$pickBackgroundMusic(SoundPool pool, Operation<SoundPoolEntry> original) {
        SoundPoolEntry var1 = original.call(pool);
        var1 = ModCompatibilityClient.audioModPickBackgroundMusic((SoundManager)(Object) this, var1);
        var1 = SoundEvent.getResult(new PlayBackgroundMusicEvent((SoundManager)(Object) this, var1));
        return var1;
    }

    // Pattern F (@ModifyConstant): both occurrences of 12000 in playRandomMusicIfReady
    // (rand.nextInt(12000) and + 12000) are replaced by a single handler returning MUSIC_INTERVAL.
    @ModifyConstant(method = "playRandomMusicIfReady", constant = @Constant(intValue = 12000))
    private int forge$musicIntervalPlay(int constant) {
        return MUSIC_INTERVAL;
    }

    // Pattern E (@WrapOperation on INVOKE): intercepts getRandomSoundFromSoundPool in
    // playStreaming and pipes the result through PlayStreamingEvent — the wrapped return
    // value is what var8 is assigned, so the subsequent null check sees the event result.
    // No @Slice needed: playStreaming has only one getRandomSoundFromSoundPool call.
    @WrapOperation(
        method = "playStreaming",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/SoundPool;getRandomSoundFromSoundPool(Ljava/lang/String;)Lnet/minecraft/src/SoundPoolEntry;")
    )
    private SoundPoolEntry forge$pickStreamingSound(SoundPool pool, String name, Operation<SoundPoolEntry> original,
                                                    @Local(argsOnly = true, ordinal = 0) float par2,
                                                    @Local(argsOnly = true, ordinal = 1) float par3,
                                                    @Local(argsOnly = true, ordinal = 2) float par4) {
        SoundPoolEntry var8 = original.call(pool, name);
        return SoundEvent.getResult(new PlayStreamingEvent((SoundManager)(Object) this, var8, name, par2, par3, par4));
    }

    // Pattern A (@Inject at INVOKE): @Inject(value = "INVOKE") fires before the targeted
    // call by default — no shift needed. Captures var7 (the source name, ordinal 1 among
    // Strings since par1Str is ordinal 0) and the positional args via @Local.
    @Inject(
        method = "playStreaming",
        at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;play(Ljava/lang/String;)V")
    )
    private void forge$postStreamingSourceEvent(CallbackInfo ci,
                                                @Local(ordinal = 1) String var7,
                                                @Local(argsOnly = true, ordinal = 0) float par2,
                                                @Local(argsOnly = true, ordinal = 1) float par3,
                                                @Local(argsOnly = true, ordinal = 2) float par4) {
        MinecraftForge.EVENT_BUS.post(new PlayStreamingSourceEvent((SoundManager)(Object) this, var7, par2, par3, par4));
    }

    // Pattern E (@WrapOperation on INVOKE): same pattern as playStreaming — intercepts
    // getRandomSoundFromSoundPool in playSound and pipes the result through PlaySoundEvent.
    // playSound takes all six positional+volume+pitch args, so five floats are captured.
    @WrapOperation(
        method = "playSound",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/SoundPool;getRandomSoundFromSoundPool(Ljava/lang/String;)Lnet/minecraft/src/SoundPoolEntry;")
    )
    private SoundPoolEntry forge$pickSound(SoundPool pool, String name, Operation<SoundPoolEntry> original,
                                           @Local(argsOnly = true, ordinal = 0) float par2,
                                           @Local(argsOnly = true, ordinal = 1) float par3,
                                           @Local(argsOnly = true, ordinal = 2) float par4,
                                           @Local(argsOnly = true, ordinal = 3) float par5,
                                           @Local(argsOnly = true, ordinal = 4) float par6) {
        SoundPoolEntry var7 = original.call(pool, name);
        return SoundEvent.getResult(new PlaySoundEvent((SoundManager)(Object) this, var7, name, par2, par3, par4, par5, par6));
    }

    // Pattern A (@Inject at INVOKE): fires before sndSystem.play(var8) by default —
    // captures var8 (ordinal 1 among Strings: par1Str is ordinal 0) and xyz position args.
    @Inject(
        method = "playSound",
        at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;play(Ljava/lang/String;)V")
    )
    private void forge$postSoundSourceEvent(CallbackInfo ci,
                                            @Local(ordinal = 1) String var8,
                                            @Local(argsOnly = true, ordinal = 0) float par2,
                                            @Local(argsOnly = true, ordinal = 1) float par3,
                                            @Local(argsOnly = true, ordinal = 2) float par4) {
        MinecraftForge.EVENT_BUS.post(new PlaySoundSourceEvent((SoundManager)(Object) this, var8, par2, par3, par4));
    }

    // Pattern E (@WrapOperation on INVOKE): same pattern as playStreaming/playSound —
    // intercepts getRandomSoundFromSoundPool in playSoundFX and pipes through PlaySoundEffectEvent.
    // playSoundFX has no position args; only volume (par2) and pitch (par3) are captured.
    @WrapOperation(
        method = "playSoundFX",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/SoundPool;getRandomSoundFromSoundPool(Ljava/lang/String;)Lnet/minecraft/src/SoundPoolEntry;")
    )
    private SoundPoolEntry forge$pickSoundEffect(SoundPool pool, String name, Operation<SoundPoolEntry> original,
                                                 @Local(argsOnly = true, ordinal = 0) float par2,
                                                 @Local(argsOnly = true, ordinal = 1) float par3) {
        SoundPoolEntry var4 = original.call(pool, name);
        return SoundEvent.getResult(new PlaySoundEffectEvent((SoundManager)(Object) this, var4, name, par2, par3));
    }

    // Pattern A (@Inject at INVOKE): fires before sndSystem.play(var5) by default —
    // captures var5 (ordinal 1 among Strings: par1Str is ordinal 0).
    // PlaySoundEffectSourceEvent takes only manager + name, no position args needed.
    @Inject(
        method = "playSoundFX",
        at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;play(Ljava/lang/String;)V")
    )
    private void forge$postSoundEffectSourceEvent(CallbackInfo ci,
                                                  @Local(ordinal = 1) String var5) {
        MinecraftForge.EVENT_BUS.post(new PlaySoundEffectSourceEvent((SoundManager)(Object) this, var5));
    }
}