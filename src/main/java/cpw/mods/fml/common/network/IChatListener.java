package cpw.mods.fml.common.network;

import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.play.ChatMessage_S2CPacket;

public interface IChatListener {
    ChatMessage_S2CPacket serverChat(PacketListener arg, ChatMessage_S2CPacket arg2);

    ChatMessage_S2CPacket clientChat(PacketListener arg, ChatMessage_S2CPacket arg2);
}
