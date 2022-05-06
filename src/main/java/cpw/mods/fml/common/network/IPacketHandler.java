package cpw.mods.fml.common.network;

import net.minecraft.network.Connection;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

public interface IPacketHandler {
    void onPacketData(Connection arg, CustomPayloadC2SPacket arg2, Player player);
}
