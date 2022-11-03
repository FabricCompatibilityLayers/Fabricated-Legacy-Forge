package cpw.mods.fml.common.network;

import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;

public interface ITinyPacketHandler {
    void handle(PacketListener arg, MapUpdateS2CPacket arg2);
}
