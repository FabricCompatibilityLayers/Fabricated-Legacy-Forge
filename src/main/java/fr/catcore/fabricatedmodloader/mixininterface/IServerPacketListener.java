package fr.catcore.fabricatedmodloader.mixininterface;

import net.minecraft.entity.player.ServerPlayerEntity;

public interface IServerPacketListener {
    ServerPlayerEntity getPlayer();
}
