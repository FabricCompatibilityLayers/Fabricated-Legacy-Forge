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
package cpw.mods.fml.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.MapDifference;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.network.EntitySpawnAdjustmentPacket;
import cpw.mods.fml.common.network.EntitySpawnPacket;
import cpw.mods.fml.common.network.ModMissingPacket;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.network.Connection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.List;

public class FMLServerHandler implements IFMLSidedHandler {
    private static final FMLServerHandler INSTANCE = new FMLServerHandler();
    private MinecraftServer server;

    private FMLServerHandler() {
        FMLCommonHandler.instance().beginLoading(this);
    }

    public void beginServerLoading(MinecraftServer minecraftServer) {
        this.server = minecraftServer;
        ObfuscationReflectionHelper.detectObfuscation(World.class);
        Loader.instance().loadMods();
    }

    public void finishServerLoading() {
        Loader.instance().initializeMods();
        LanguageRegistry.reloadLanguageTable();
        GameData.initializeServerGate(1);
    }

    public void haltGame(String message, Throwable exception) {
        throw new RuntimeException(message, exception);
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    public static FMLServerHandler instance() {
        return INSTANCE;
    }

    public List<String> getAdditionalBrandingInformation() {
        return ImmutableList.of();
    }

    public Side getSide() {
        return Side.SERVER;
    }

    public void showGuiScreen(Object clientGuiElement) {
    }

    public Entity spawnEntityIntoClientWorld(EntityRegistry.EntityRegistration er, EntitySpawnPacket packet) {
        return null;
    }

    public void adjustEntityLocationOnClient(EntitySpawnAdjustmentPacket entitySpawnAdjustmentPacket) {
    }

    public void sendPacket(Packet packet) {
        throw new RuntimeException("You cannot send a bare packet without a target on the server!");
    }

    public void displayMissingMods(ModMissingPacket modMissingPacket) {
    }

    public void handleTinyPacket(PacketListener handler, MapUpdateS2CPacket mapData) {
    }

    public void setClientCompatibilityLevel(byte compatibilityLevel) {
    }

    public byte getClientCompatibilityLevel() {
        return 0;
    }

    public boolean shouldServerShouldBeKilledQuietly() {
        return false;
    }

    public void disconnectIDMismatch(MapDifference<Integer, ItemData> s, PacketListener handler, Connection mgr) {
    }
}
