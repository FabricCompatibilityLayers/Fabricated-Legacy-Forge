package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.MapUpdate_S2CPacket;

public interface Iclass_469 {
    public void fmlPacket131Callback(MapUpdate_S2CPacket par1Packet131MapData);

    public void handleVanilla250Packet(CustomPayloadC2SPacket par1Packet250CustomPayload);

    public PlayerEntity getPlayer();
}
