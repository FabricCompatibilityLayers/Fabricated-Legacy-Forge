package cpw.mods.fml.common.network;

import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;

public interface IChatListener {
    ChatMessageS2CPacket serverChat(PacketListener arg, ChatMessageS2CPacket arg2);

    ChatMessageS2CPacket clientChat(PacketListener arg, ChatMessageS2CPacket arg2);
}
