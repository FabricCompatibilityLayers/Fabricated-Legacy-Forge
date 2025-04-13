/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
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
