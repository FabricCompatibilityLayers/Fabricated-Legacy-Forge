/*
 * The FML Forge Mod Loader suite. Copyright (C) 2012 cpw
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

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetLoginHandler;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet1Login;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

public class ModLoaderConnectionHandler implements IConnectionHandler
{
    private BaseModProxy mod;

    public ModLoaderConnectionHandler(BaseModProxy mod)
    {
        this.mod = mod;
    }

    @Override
    public void playerLoggedIn(Player player, NetHandler netHandler, NetworkManager manager)
    {
        mod.onClientLogin((EntityPlayer)player);
    }

    @Override
    public String connectionReceived(NetLoginHandler netHandler, NetworkManager manager)
    {
        return null;
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, String server, int port, NetworkManager manager)
    {
        ModLoaderHelper.sidedHelper.clientConnectionOpened(netClientHandler, manager, mod);
    }

    @Override
    public void connectionClosed(NetworkManager manager)
    {
        if (!ModLoaderHelper.sidedHelper.clientConnectionClosed(manager, mod))
        {
            mod.serverDisconnect();
            mod.onClientLogout(manager);
        }
    }

    @Override
    public void clientLoggedIn(NetHandler nh, NetworkManager manager, Packet1Login login)
    {
        mod.serverConnect(nh);
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, NetworkManager manager)
    {
        ModLoaderHelper.sidedHelper.clientConnectionOpened(netClientHandler, manager, mod);
    }

}
