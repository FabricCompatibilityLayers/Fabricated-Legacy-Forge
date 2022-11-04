package cpw.mods.fml.common.modloader;

import cpw.mods.fml.common.network.IChatListener;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.server.ServerPacketListener;

public class ModLoaderChatListener implements IChatListener {
    private BaseModProxy mod;

    public ModLoaderChatListener(BaseModProxy mod) {
        this.mod = mod;
    }

    public ChatMessageS2CPacket serverChat(PacketListener handler, ChatMessageS2CPacket message) {
        this.mod.serverChat((ServerPacketListener)handler, message.message);
        return message;
    }

    public ChatMessageS2CPacket clientChat(PacketListener handler, ChatMessageS2CPacket message) {
        this.mod.clientChat(message.message);
        return message;
    }
}
