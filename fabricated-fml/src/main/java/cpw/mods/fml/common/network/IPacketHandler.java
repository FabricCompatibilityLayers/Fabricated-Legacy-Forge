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
package cpw.mods.fml.common.network;

import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;

public interface IPacketHandler
{
    /**
     * Recieve a packet from one of the registered channels for this packet handler
     * @param manager The network manager this packet arrived from
     * @param packet The packet itself
     * @param player A dummy interface representing the player - it can be cast into a real player instance if needed
     */
    public void onPacketData(NetworkManager manager, Packet250CustomPayload packet, Player player);
}
