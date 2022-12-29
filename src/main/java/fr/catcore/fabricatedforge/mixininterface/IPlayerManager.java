package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.entity.Entity;
import net.minecraft.entity.PortalTeleporter;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public interface IPlayerManager {
    void transferPlayerToDimension(ServerPlayerEntity par1EntityPlayerMP, int par2, PortalTeleporter teleporter);

    void transferEntityToWorld(Entity par1Entity, int par2, ServerWorld par3WorldServer, ServerWorld par4WorldServer, PortalTeleporter teleporter);
}
