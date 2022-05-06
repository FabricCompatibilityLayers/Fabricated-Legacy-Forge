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
