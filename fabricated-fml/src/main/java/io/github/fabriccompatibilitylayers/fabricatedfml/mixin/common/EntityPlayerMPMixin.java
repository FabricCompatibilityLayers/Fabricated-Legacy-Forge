/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerMP.class)
public abstract class EntityPlayerMPMixin extends EntityPlayer {
    @Shadow public MinecraftServer field_71133_b;

    public EntityPlayerMPMixin(World p_i3564_1_) {
        super(p_i3564_1_);
    }

    @Inject(method = "func_70645_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/InventoryPlayer;func_70436_m()V"))
    private void fml$extraLogging(DamageSource p_70645_1_, CallbackInfo ci) {
        this.field_71133_b.func_71203_ab().field_72406_a.info(p_70645_1_.func_76360_b(this));
    }
}
