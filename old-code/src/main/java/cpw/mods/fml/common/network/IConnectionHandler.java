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
package cpw.mods.fml.common.network;

import net.minecraft.network.Connection;
import net.minecraft.network.PendingConnection;
import net.minecraft.network.class_690;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.server.MinecraftServer;

public interface IConnectionHandler {
    void playerLoggedIn(Player player, PacketListener arg, Connection arg2);

    String connectionReceived(PendingConnection arg, Connection arg2);

    void connectionOpened(PacketListener arg, String string, int i, Connection arg2);

    void connectionOpened(PacketListener arg, MinecraftServer minecraftServer, Connection arg2);

    void connectionClosed(Connection arg);

    void clientLoggedIn(PacketListener arg, Connection arg2, class_690 arg3);
}
