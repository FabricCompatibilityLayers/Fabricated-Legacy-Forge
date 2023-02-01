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
package cpw.mods.fml.common;

import com.google.common.collect.MapDifference;
import cpw.mods.fml.common.network.EntitySpawnAdjustmentPacket;
import cpw.mods.fml.common.network.EntitySpawnPacket;
import cpw.mods.fml.common.network.ModMissingPacket;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.ItemData;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;
import net.minecraft.network.Connection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public interface IFMLSidedHandler {
    List<String> getAdditionalBrandingInformation();

    Side getSide();

    void haltGame(String string, Throwable throwable);

    void showGuiScreen(Object object);

    Entity spawnEntityIntoClientWorld(EntityRegistry.EntityRegistration entityRegistration, EntitySpawnPacket entitySpawnPacket);

    void adjustEntityLocationOnClient(EntitySpawnAdjustmentPacket entitySpawnAdjustmentPacket);

    void beginServerLoading(MinecraftServer minecraftServer);

    void finishServerLoading();

    MinecraftServer getServer();

    void sendPacket(Packet arg);

    void displayMissingMods(ModMissingPacket modMissingPacket);

    void handleTinyPacket(PacketListener arg, MapUpdateS2CPacket arg2);

    void setClientCompatibilityLevel(byte b);

    byte getClientCompatibilityLevel();

    boolean shouldServerShouldBeKilledQuietly();

    void disconnectIDMismatch(MapDifference<Integer, ItemData> mapDifference, PacketListener arg, Connection arg2);
}
