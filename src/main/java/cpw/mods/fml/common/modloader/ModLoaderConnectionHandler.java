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
        if (!ModLoaderHelper.sidedHelper.clientConnectionClosed(manager, this.mod)) {
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
