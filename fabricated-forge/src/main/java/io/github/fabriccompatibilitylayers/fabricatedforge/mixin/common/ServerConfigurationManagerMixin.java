/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ServerConfigurationManagerExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldProviderExtension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(ServerConfigurationManager.class)
public abstract class ServerConfigurationManagerMixin implements ServerConfigurationManagerExtension {
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

    @Override
    public void transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int par2, Teleporter teleporter) {
        int var3 = par1EntityPlayerMP.dimension;
        WorldServer var4 = this.mcServer.worldServerForDimension(par1EntityPlayerMP.dimension);
        par1EntityPlayerMP.dimension = par2;
        WorldServer var5 = this.mcServer.worldServerForDimension(par1EntityPlayerMP.dimension);
        par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(par1EntityPlayerMP.dimension, (byte)par1EntityPlayerMP.worldObj.difficultySetting, var5.getWorldInfo().getTerrainType(), var5.getHeight(), par1EntityPlayerMP.theItemInWorldManager.getGameType()));
        var4.removeEntity(par1EntityPlayerMP);
        par1EntityPlayerMP.isDead = false;
        this.func_82448_a(par1EntityPlayerMP, var3, var4, var5, teleporter);
        this.func_72375_a(par1EntityPlayerMP, var4);
        par1EntityPlayerMP.playerNetServerHandler.setPlayerLocation(par1EntityPlayerMP.posX, par1EntityPlayerMP.posY, par1EntityPlayerMP.posZ, par1EntityPlayerMP.rotationYaw, par1EntityPlayerMP.rotationPitch);
        par1EntityPlayerMP.theItemInWorldManager.setWorld(var5);
        this.updateTimeAndWeatherForPlayer(par1EntityPlayerMP, var5);
        this.syncPlayerInventory(par1EntityPlayerMP);

        for(PotionEffect var7 : (Collection<PotionEffect>) par1EntityPlayerMP.getActivePotionEffects()) {
            par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(par1EntityPlayerMP.entityId, var7));
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void func_82448_a(Entity par1Entity, int par2, WorldServer par3WorldServer, WorldServer par4WorldServer) {
        func_82448_a(par1Entity, par2, par3WorldServer, par4WorldServer, new Teleporter());
    }

    @Override
    public void func_82448_a(Entity par1Entity, int par2, WorldServer par3WorldServer, WorldServer par4WorldServer, Teleporter teleporter)
    {
        WorldProvider pOld = par3WorldServer.provider;
        WorldProvider pNew = par4WorldServer.provider;
        double moveFactor = ((WorldProviderExtension) pOld).getMovementFactor() / ((WorldProviderExtension) pNew).getMovementFactor();
        double var5 = par1Entity.posX * moveFactor;
        double var7 = par1Entity.posZ * moveFactor;

        double var11 = par1Entity.posX;
        double var13 = par1Entity.posY;
        double var15 = par1Entity.posZ;
        float var17 = par1Entity.rotationYaw;

        if (par1Entity.dimension == 1) {
            ChunkCoordinates var18;
            if (par2 == 1) {
                var18 = par4WorldServer.getSpawnPoint();
            } else {
                var18 = par4WorldServer.getEntrancePortalLocation();
            }

            var5 = (double)var18.posX;
            par1Entity.posY = (double)var18.posY;
            var7 = (double)var18.posZ;
            par1Entity.setLocationAndAngles(var5, par1Entity.posY, var7, 90.0F, 0.0F);
            if (par1Entity.isEntityAlive()) {
                par3WorldServer.updateEntityWithOptionalForce(par1Entity, false);
            }
        }

        if (par2 != 1) {
            var5 = (double)MathHelper.clamp_int((int)var5, -29999872, 29999872);
            var7 = (double)MathHelper.clamp_int((int)var7, -29999872, 29999872);
            if (par1Entity.isEntityAlive()) {
                par4WorldServer.spawnEntityInWorld(par1Entity);
                par1Entity.setLocationAndAngles(var5, par1Entity.posY, var7, par1Entity.rotationYaw, par1Entity.rotationPitch);
                par4WorldServer.updateEntityWithOptionalForce(par1Entity, false);
                teleporter.placeInPortal(par4WorldServer, par1Entity, var11, var13, var15, var17);
            }
        }

        par1Entity.setWorld(par4WorldServer);
    }
}
