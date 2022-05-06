package cpw.mods.fml.common.modloader;

import cpw.mods.fml.common.network.IChatListener;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.play.ChatMessage_S2CPacket;
import net.minecraft.server.ServerPacketListener;

public class ModLoaderChatListener implements IChatListener {
    private BaseModProxy mod;

    public ModLoaderChatListener(BaseModProxy mod) {
        this.mod = mod;
    }

    public ChatMessage_S2CPacket serverChat(PacketListener handler, ChatMessage_S2CPacket message) {
        this.mod.serverChat((ServerPacketListener)handler, message.message);
        return message;
    }

    public ChatMessage_S2CPacket clientChat(PacketListener handler, ChatMessage_S2CPacket message) {
        this.mod.clientChat(message.message);
        return message;
    }
}
