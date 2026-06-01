/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {
    @Shadow public Entity field_73132_a;

    @Shadow public int field_73128_d;

    @Shadow public int field_73129_e;

    @Shadow public int field_73126_f;

    @Inject(method = "func_73117_b", at = @At(value = "FIELD", target = "Lnet/minecraft/src/EntityTrackerEntry;field_73143_t:Z"))
    private void fml$makeEntitySpawnAdjustment(EntityPlayerMP p_73117_1_, CallbackInfo ci) {
        int posX = MathHelper.func_76128_c(this.field_73132_a.field_70165_t * 32.0D);
        int posY = MathHelper.func_76128_c(this.field_73132_a.field_70163_u * 32.0D);
        int posZ = MathHelper.func_76128_c(this.field_73132_a.field_70161_v * 32.0D);
        if (posX != this.field_73128_d || posY != this.field_73129_e || posZ != this.field_73126_f)
        {
            FMLNetworkHandler.makeEntitySpawnAdjustment(this.field_73132_a.field_70157_k, p_73117_1_, this.field_73128_d, this.field_73129_e, this.field_73126_f);
        }
    }

    @Inject(method = "func_73124_b", at = @At(value = "FIELD", target = "Lnet/minecraft/src/EntityTrackerEntry;field_73132_a:Lnet/minecraft/src/Entity;", ordinal = 0), cancellable = true)
    private void fml$getEntitySpawningPacket(CallbackInfoReturnable<Packet> cir) {
        Packet pkt = FMLNetworkHandler.getEntitySpawningPacket(this.field_73132_a);

        if (pkt != null)
        {
            cir.setReturnValue(pkt);
        }
    }
}
