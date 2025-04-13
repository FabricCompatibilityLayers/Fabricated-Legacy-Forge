package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.entity.PortalTeleporter;
import net.minecraft.entity.player.ServerPlayerEntity;

public interface IPlayerManager {
    void transferPlayerToDimension(ServerPlayerEntity par1EntityPlayerMP, int par2, PortalTeleporter teleporter);
}
