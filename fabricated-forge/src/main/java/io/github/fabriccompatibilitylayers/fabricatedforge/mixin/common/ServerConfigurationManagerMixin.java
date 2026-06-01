/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldProviderExtension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import net.minecraftforge.common.DimensionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerConfigurationManager.class)
public abstract class ServerConfigurationManagerMixin {
    @Shadow @Final private MinecraftServer mcServer;

    @Shadow public abstract void func_72375_a(EntityPlayerMP par1EntityPlayerMP, WorldServer par2WorldServer);

    @Shadow public abstract void updateTimeAndWeatherForPlayer(EntityPlayerMP par1EntityPlayerMP, WorldServer par2WorldServer);

    @Shadow public abstract void syncPlayerInventory(EntityPlayerMP par1EntityPlayerMP);

    @Inject(method = "respawnPlayer", at = @At("HEAD"))
    private void forge$FixRespawnDimension(EntityPlayerMP par1EntityPlayerMP, int par2, boolean par3, CallbackInfoReturnable<EntityPlayerMP> cir,
                                           @Local(ordinal = 0, argsOnly = true) LocalIntRef par2Ref) {
        World world = mcServer.worldServerForDimension(par2);
        if (world == null || !world.provider.canRespawnHere())
        {
            par2Ref.set(0);
        }
    }

    @Inject(method = "respawnPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/src/EntityPlayerMP;entityId:I", ordinal = 0))
    private void forge$updateDimensionId(EntityPlayerMP par1EntityPlayerMP, int par2, boolean par3, CallbackInfoReturnable<EntityPlayerMP> cir,
                                         @Local(ordinal = 1) EntityPlayerMP var6) {
        var6.dimension = par2;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int par2)
    {
        transferPlayerToDimension(par1EntityPlayerMP, par2, new Teleporter());
    }

    public void transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int par2, Teleporter teleporter) {
        int var3 = par1EntityPlayerMP.dimension;
        WorldServer var4 = this.mcServer.worldServerForDimension(par1EntityPlayerMP.dimension);
        par1EntityPlayerMP.dimension = par2;
        WorldServer var5 = this.mcServer.worldServerForDimension(par1EntityPlayerMP.dimension);
        par1EntityPlayerMP.playerNetServerHandler
                .sendPacketToPlayer(
                        new Packet9Respawn(
                                par1EntityPlayerMP.dimension,
                                (byte)par1EntityPlayerMP.worldObj.difficultySetting,
                                var5.getWorldInfo().getTerrainType(),
                                var5.getHeight(),
                                par1EntityPlayerMP.theItemInWorldManager.getGameType()
                        )
                );
        var4.removeEntity(par1EntityPlayerMP);
        par1EntityPlayerMP.isDead = false;

        WorldProvider pOld = DimensionManager.getProvider(var3);
        WorldProvider pNew = DimensionManager.getProvider(par2);
        double moveFactor = ((WorldProviderExtension) pOld).getMovementFactor() / ((WorldProviderExtension) pNew).getMovementFactor();
        double var6 = par1EntityPlayerMP.posX * moveFactor;
        double var8 = par1EntityPlayerMP.posZ * moveFactor;

        if (par1EntityPlayerMP.dimension == 1) {
            ChunkCoordinates var12 = var5.getEntrancePortalLocation();
            var6 = var12.posX;
            par1EntityPlayerMP.posY = var12.posY;
            var8 = var12.posZ;
            par1EntityPlayerMP.setLocationAndAngles(var6, par1EntityPlayerMP.posY, var8, 90.0F, 0.0F);
            if (par1EntityPlayerMP.isEntityAlive()) {
                var4.updateEntityWithOptionalForce(par1EntityPlayerMP, false);
            }
        }

        if (var3 != 1) {
            var6 = MathHelper.clamp_int((int)var6, -29999872, 29999872);
            var8 = MathHelper.clamp_int((int)var8, -29999872, 29999872);
            if (par1EntityPlayerMP.isEntityAlive()) {
                var5.spawnEntityInWorld(par1EntityPlayerMP);
                par1EntityPlayerMP.setLocationAndAngles(var6, par1EntityPlayerMP.posY, var8, par1EntityPlayerMP.rotationYaw, par1EntityPlayerMP.rotationPitch);
                var5.updateEntityWithOptionalForce(par1EntityPlayerMP, false);
                teleporter.placeInPortal(var5, par1EntityPlayerMP);
            }
        }

        par1EntityPlayerMP.setWorld(var5);
        this.func_72375_a(par1EntityPlayerMP, var4);
        par1EntityPlayerMP.playerNetServerHandler
                .setPlayerLocation(
                        par1EntityPlayerMP.posX, par1EntityPlayerMP.posY, par1EntityPlayerMP.posZ, par1EntityPlayerMP.rotationYaw, par1EntityPlayerMP.rotationPitch
                );
        par1EntityPlayerMP.theItemInWorldManager.setWorld(var5);
        this.updateTimeAndWeatherForPlayer(par1EntityPlayerMP, var5);
        this.syncPlayerInventory(par1EntityPlayerMP);

        for (Object var13Obj : par1EntityPlayerMP.getActivePotionEffects()) {
            PotionEffect var13 = ((PotionEffect) var13Obj);
            par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(par1EntityPlayerMP.entityId, var13));
        }
    }
}
