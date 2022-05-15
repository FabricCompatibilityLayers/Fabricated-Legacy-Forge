package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

public interface IPacketListener {
    public abstract void handleVanilla250Packet(CustomPayloadC2SPacket arg);

    public abstract PlayerEntity getPlayer();
}
