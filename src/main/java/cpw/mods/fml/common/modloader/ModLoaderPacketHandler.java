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

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.Connection;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

public class ModLoaderPacketHandler implements IPacketHandler {
    private BaseModProxy mod;

    public ModLoaderPacketHandler(BaseModProxy mod) {
        this.mod = mod;
    }

    public void onPacketData(Connection manager, CustomPayloadC2SPacket packet, Player player) {
        if (player instanceof ServerPlayerEntity) {
            this.mod.serverCustomPayload(((ServerPlayerEntity)player).field_2823, packet);
        } else {
            ModLoaderHelper.sidedHelper.sendClientPacket(this.mod, packet);
        }
    }
}
