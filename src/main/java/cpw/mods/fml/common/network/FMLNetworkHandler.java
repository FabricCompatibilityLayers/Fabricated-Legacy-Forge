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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.catcore.fabricatedforge.mixininterface.IPacketListener;
import fr.catcore.fabricatedforge.mixininterface.IPendingConnection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.Connection;
import net.minecraft.network.Packet;
import net.minecraft.network.PendingConnection;
import net.minecraft.network.class_690;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.ServerPacketListener;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.level.LevelGeneratorType;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.util.*;

public class FMLNetworkHandler {
    private static final int FML_HASH = Hashing.murmur3_32().hashString("FML").asInt();
    private static final int PROTOCOL_VERSION = 1;
    private static final FMLNetworkHandler INSTANCE = new FMLNetworkHandler();
    static final int LOGIN_RECEIVED = 1;
    static final int CONNECTION_VALID = 2;
    static final int FML_OUT_OF_DATE = -1;
    static final int MISSING_MODS_OR_VERSIONS = -2;
    private Map<PendingConnection, Integer> loginStates = Maps.newHashMap();
    private Map<ModContainer, NetworkModHandler> networkModHandlers = Maps.newHashMap();
    private Map<Integer, NetworkModHandler> networkIdLookup = Maps.newHashMap();

    public FMLNetworkHandler() {
    }

    public static void handlePacket250Packet(CustomPayloadC2SPacket packet, Connection network, PacketListener handler) {
        String target = packet.channel;
        if (target.startsWith("MC|")) {
            ((IPacketListener)handler).handleVanilla250Packet(packet);
        }

        if (target.equals("FML")) {
            instance().handleFMLPacket(packet, network, handler);
        } else {
            NetworkRegistry.instance().handleCustomPacket(packet, network, handler);
        }

    }

    public static void onConnectionEstablishedToServer(PacketListener clientHandler, Connection manager, class_690 login) {
        NetworkRegistry.instance().clientLoggedIn(clientHandler, manager, login);
    }

    private void handleFMLPacket(CustomPayloadC2SPacket packet, Connection network, PacketListener netHandler) {
        FMLPacket pkt = FMLPacket.readPacket(packet.field_2455);
        String userName = "";
        if (netHandler instanceof PendingConnection) {
            userName = ((PendingConnection)netHandler).username;
        } else {
            PlayerEntity pl = ((IPacketListener)netHandler).getPlayer();
            if (pl != null) {
                userName = pl.getUsername();
            }
        }

        pkt.execute(network, this, netHandler, userName);
    }

    public static void onConnectionReceivedFromClient(PendingConnection netLoginHandler, MinecraftServer server, SocketAddress address, String userName) {
        instance().handleClientConnection(netLoginHandler, server, address, userName);
    }

    private void handleClientConnection(PendingConnection netLoginHandler, MinecraftServer server, SocketAddress address, String userName) {
        if (!this.loginStates.containsKey(netLoginHandler)) {
            if (this.handleVanillaLoginKick(netLoginHandler, server, address, userName)) {
                FMLLog.fine("Connection from %s rejected - no FML packet received from client", userName);
                ((IPendingConnection)netLoginHandler).completeConnection("You don't have FML installed, you cannot connect to this server");
            } else {
                FMLLog.fine("Connection from %s was closed by vanilla minecraft", userName);
            }
        } else {
            switch ((Integer)this.loginStates.get(netLoginHandler)) {
                case -2:
                    ((IPendingConnection)netLoginHandler).completeConnection("The server requires mods that are absent or out of date on your client");
                    this.loginStates.remove(netLoginHandler);
                    break;
                case -1:
                    ((IPendingConnection)netLoginHandler).completeConnection("Your client is not running a new enough version of FML to connect to this server");
                    this.loginStates.remove(netLoginHandler);
                    break;
                case 0:
                default:
                    ((IPendingConnection)netLoginHandler).completeConnection("There was a problem during FML negotiation");
                    this.loginStates.remove(netLoginHandler);
                    break;
                case 1:
                    String modKick = NetworkRegistry.instance().connectionReceived(netLoginHandler, netLoginHandler.connection);
                    if (modKick != null) {
                        ((IPendingConnection)netLoginHandler).completeConnection(modKick);
                        this.loginStates.remove(netLoginHandler);
                        return;
                    }

                    if (!this.handleVanillaLoginKick(netLoginHandler, server, address, userName)) {
                        this.loginStates.remove(netLoginHandler);
                        return;
                    }

                    ((IPendingConnection)netLoginHandler).method_2189_fabric(false);
                    netLoginHandler.connection.send(this.getModListRequestPacket());
                    this.loginStates.put(netLoginHandler, 2);
                    break;
                case 2:
                    ((IPendingConnection)netLoginHandler).completeConnection((String)null);
                    this.loginStates.remove(netLoginHandler);
            }

        }
    }

    private boolean handleVanillaLoginKick(PendingConnection netLoginHandler, MinecraftServer server, SocketAddress address, String userName) {
        PlayerManager playerList = server.getPlayerManager();
        String kickReason = playerList.checkCanJoin(address, userName);
        if (kickReason != null) {
            ((IPendingConnection)netLoginHandler).completeConnection(kickReason);
        }

        return kickReason == null;
    }

    public static void handleLoginPacketOnServer(PendingConnection handler, class_690 login) {
        if (login.entityId == FML_HASH) {
            if (login.dimension == 1) {
                FMLLog.finest("Received valid FML login packet from %s", handler.connection.getAddress());
                instance().loginStates.put(handler, 1);
            } else {
                FMLLog.finest("Received incorrect FML (%x) login packet from %s", login.dimension, handler.connection.getAddress());
                instance().loginStates.put(handler, -1);
            }
        } else {
            FMLLog.fine("Received invalid login packet (%x, %x) from %s", login.entityId, login.dimension, handler.connection.getAddress());
        }

    }

    static void setHandlerState(PendingConnection handler, int state) {
        instance().loginStates.put(handler, state);
    }

    public static FMLNetworkHandler instance() {
        return INSTANCE;
    }

    public static class_690 getFMLFakeLoginPacket() {
        FMLCommonHandler.instance().getSidedDelegate().setClientCompatibilityLevel((byte)0);
        class_690 fake = new class_690();
        fake.entityId = FML_HASH;
        fake.dimension = 1;
        fake.gameMode = GameMode.NOT_SET;
        fake.levelType = LevelGeneratorType.TYPES[0];
        return fake;
    }

    public CustomPayloadC2SPacket getModListRequestPacket() {
        return PacketDispatcher.getPacket("FML", FMLPacket.makePacket(FMLPacket.Type.MOD_LIST_REQUEST));
    }

    public void registerNetworkMod(NetworkModHandler handler) {
        this.networkModHandlers.put(handler.getContainer(), handler);
        this.networkIdLookup.put(handler.getNetworkId(), handler);
    }

    public boolean registerNetworkMod(ModContainer container, Class<?> networkModClass, ASMDataTable asmData) {
        NetworkModHandler handler = new NetworkModHandler(container, networkModClass, asmData);
        if (handler.isNetworkMod()) {
            this.registerNetworkMod(handler);
        }

        return handler.isNetworkMod();
    }

    public NetworkModHandler findNetworkModHandler(Object mc) {
        if (mc instanceof ModContainer) {
            return (NetworkModHandler)this.networkModHandlers.get(mc);
        } else {
            return mc instanceof Integer ? (NetworkModHandler)this.networkIdLookup.get(mc) : (NetworkModHandler)this.networkModHandlers.get(FMLCommonHandler.instance().findContainerFor(mc));
        }
    }

    public Set<ModContainer> getNetworkModList() {
        return this.networkModHandlers.keySet();
    }

    public static void handlePlayerLogin(ServerPlayerEntity player, ServerPacketListener netHandler, Connection manager) {
        NetworkRegistry.instance().playerLoggedIn(player, netHandler, manager);
        GameRegistry.onPlayerLogin(player);
    }

    public Map<Integer, NetworkModHandler> getNetworkIdMap() {
        return this.networkIdLookup;
    }

    public void bindNetworkId(String key, Integer value) {
        Map<String, ModContainer> mods = Loader.instance().getIndexedModList();
        NetworkModHandler handler = this.findNetworkModHandler(mods.get(key));
        if (handler != null) {
            handler.setNetworkId(value);
            this.networkIdLookup.put(value, handler);
        }

    }

    public static void onClientConnectionToRemoteServer(PacketListener netClientHandler, String server, int port, Connection networkManager) {
        NetworkRegistry.instance().connectionOpened(netClientHandler, server, port, networkManager);
    }

    public static void onClientConnectionToIntegratedServer(PacketListener netClientHandler, MinecraftServer server, Connection networkManager) {
        NetworkRegistry.instance().connectionOpened(netClientHandler, server, networkManager);
    }

    public static void onConnectionClosed(Connection manager, PlayerEntity player) {
        NetworkRegistry.instance().connectionClosed(manager, player);
    }

    public static void openGui(PlayerEntity player, Object mod, int modGuiId, World world, int x, int y, int z) {
        ModContainer mc = FMLCommonHandler.instance().findContainerFor(mod);
        if (mc == null) {
            NetworkModHandler nmh = instance().findNetworkModHandler(mod);
            if (nmh == null) {
                FMLLog.warning("A mod tried to open a gui on the server without being a NetworkMod");
                return;
            }

            mc = nmh.getContainer();
        }

        if (player instanceof ServerPlayerEntity) {
            NetworkRegistry.instance().openRemoteGui(mc, (ServerPlayerEntity)player, modGuiId, world, x, y, z);
        } else {
            NetworkRegistry.instance().openLocalGui(mc, player, modGuiId, world, x, y, z);
        }

    }

    public static Packet getEntitySpawningPacket(Entity entity) {
        EntityRegistry.EntityRegistration er = EntityRegistry.instance().lookupModSpawn(entity.getClass(), false);
        if (er == null) {
            return null;
        } else {
            return er.usesVanillaSpawning() ? null : PacketDispatcher.getPacket("FML", FMLPacket.makePacket(FMLPacket.Type.ENTITYSPAWN, er, entity, instance().findNetworkModHandler(er.getContainer())));
        }
    }

    public static void makeEntitySpawnAdjustment(int entityId, ServerPlayerEntity player, int serverX, int serverY, int serverZ) {
        CustomPayloadC2SPacket pkt = PacketDispatcher.getPacket("FML", FMLPacket.makePacket(FMLPacket.Type.ENTITYSPAWNADJUSTMENT, entityId, serverX, serverY, serverZ));
        player.field_2823.sendPacket(pkt);
    }

    public static InetAddress computeLocalHost() throws IOException {
        InetAddress add = null;
        List<InetAddress> addresses = Lists.newArrayList();
        InetAddress localHost = InetAddress.getLocalHost();
        for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces()))
        {
            if (!ni.isLoopback() && ni.isUp())
            {
                addresses.addAll(Collections.list(ni.getInetAddresses()));
                if (addresses.contains(localHost))
                {
                    add = localHost;
                    break;
                }
            }
        }
        if (add == null && !addresses.isEmpty())
        {
            for (InetAddress addr: addresses)
            {
                if (addr.getAddress().length == 4)
                {
                    add = addr;
                    break;
                }
            }
        }
        if (add == null)
        {
            add = localHost;
        }
        return add;
    }

    public static ChatMessageS2CPacket handleChatMessage(PacketListener handler, ChatMessageS2CPacket chat) {
        return NetworkRegistry.instance().handleChat(handler, chat);
    }

    public static void handlePacket131Packet(PacketListener handler, MapUpdateS2CPacket mapData) {
        if (!(handler instanceof ServerPacketListener) && mapData.item == Item.MAP.id) {
            FMLCommonHandler.instance().handleTinyPacket(handler, mapData);
        } else {
            NetworkRegistry.instance().handleTinyPacket(handler, mapData);
        }

    }

    public static int getCompatibilityLevel() {
        return 1;
    }

    public static boolean vanillaLoginPacketCompatibility() {
        return FMLCommonHandler.instance().getSidedDelegate().getClientCompatibilityLevel() == 0;
    }
}
