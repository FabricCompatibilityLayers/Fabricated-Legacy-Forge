package cpw.mods.fml.common.network;

import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.play.MapUpdate_S2CPacket;

public interface ITinyPacketHandler {
    void handle(PacketListener arg, MapUpdate_S2CPacket arg2);
}
