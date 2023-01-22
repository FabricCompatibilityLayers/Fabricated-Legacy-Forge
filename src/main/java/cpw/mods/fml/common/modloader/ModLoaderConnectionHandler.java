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

import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Connection;
import net.minecraft.network.PendingConnection;
import net.minecraft.network.class_690;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.server.MinecraftServer;

public class ModLoaderConnectionHandler implements IConnectionHandler {
    private BaseModProxy mod;

    public ModLoaderConnectionHandler(BaseModProxy mod) {
        this.mod = mod;
    }

    public void playerLoggedIn(Player player, PacketListener netHandler, Connection manager) {
        this.mod.onClientLogin((PlayerEntity)player);
    }

    public String connectionReceived(PendingConnection netHandler, Connection manager) {
        return null;
    }

    public void connectionOpened(PacketListener netClientHandler, String server, int port, Connection manager) {
        ModLoaderHelper.sidedHelper.clientConnectionOpened(netClientHandler, manager, this.mod);
    }

    public void connectionClosed(Connection manager) {
        if (ModLoaderHelper.sidedHelper == null || !ModLoaderHelper.sidedHelper.clientConnectionClosed(manager, this.mod)) {
            this.mod.serverDisconnect();
            this.mod.onClientLogout(manager);
        }
    }

    public void clientLoggedIn(PacketListener nh, Connection manager, class_690 login) {
        this.mod.serverConnect(nh);
    }

    public void connectionOpened(PacketListener netClientHandler, MinecraftServer server, Connection manager) {
        ModLoaderHelper.sidedHelper.clientConnectionOpened(netClientHandler, manager, this.mod);
    }
}
