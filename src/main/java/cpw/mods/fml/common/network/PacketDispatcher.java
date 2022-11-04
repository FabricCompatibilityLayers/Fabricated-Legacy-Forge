package cpw.mods.fml.common.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;

public class PacketDispatcher {
    public PacketDispatcher() {
    }

    public static CustomPayloadC2SPacket getPacket(String type, byte[] data) {
        return new CustomPayloadC2SPacket(type, data);
    }

    public static void sendPacketToServer(Packet packet) {
        FMLCommonHandler.instance().getSidedDelegate().sendPacket(packet);
    }

    public static void sendPacketToPlayer(Packet packet, Player player) {
        if (player instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity)player).field_2823.sendPacket(packet);
        }

    }

    public static void sendPacketToAllAround(double X, double Y, double Z, double range, int dimensionId, Packet packet) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server != null) {
            server.getPlayerManager().sendToAround(X, Y, Z, range, dimensionId, packet);
        } else {
            FMLLog.fine("Attempt to send packet to all around without a server instance available", new Object[0]);
        }

    }

    public static void sendPacketToAllInDimension(Packet packet, int dimId) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server != null) {
            server.getPlayerManager().sendToDimension(packet, dimId);
        } else {
            FMLLog.fine("Attempt to send packet to all in dimension without a server instance available", new Object[0]);
        }

    }

    public static void sendPacketToAllPlayers(Packet packet) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server != null) {
            server.getPlayerManager().sendToAll(packet);
        } else {
            FMLLog.fine("Attempt to send packet to all in dimension without a server instance available", new Object[0]);
        }

    }

    public static MapUpdateS2CPacket getTinyPacket(Object mod, short tag, byte[] data) {
        NetworkModHandler nmh = FMLNetworkHandler.instance().findNetworkModHandler(mod);
        return new MapUpdateS2CPacket((short)nmh.getNetworkId(), tag, data);
    }
}
