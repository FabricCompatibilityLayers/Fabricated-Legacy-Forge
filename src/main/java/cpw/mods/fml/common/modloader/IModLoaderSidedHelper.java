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

import cpw.mods.fml.common.network.EntitySpawnPacket;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Connection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

public interface IModLoaderSidedHelper {
    void finishModLoading(ModLoaderModContainer modLoaderModContainer);

    Object getClientGui(BaseModProxy baseModProxy, PlayerEntity arg, int i, int j, int k, int l);

    Entity spawnEntity(BaseModProxy baseModProxy, EntitySpawnPacket entitySpawnPacket, EntityRegistry.EntityRegistration entityRegistration);

    void sendClientPacket(BaseModProxy baseModProxy, CustomPayloadC2SPacket arg);

    void clientConnectionOpened(PacketListener arg, Connection arg2, BaseModProxy baseModProxy);

    boolean clientConnectionClosed(Connection arg, BaseModProxy baseModProxy);
}
