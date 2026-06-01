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

import net.minecraft.src.NetHandler;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet3Chat;
import cpw.mods.fml.common.network.IChatListener;

public class ModLoaderChatListener implements IChatListener
{

    private BaseModProxy mod;

    public ModLoaderChatListener(BaseModProxy mod)
    {
        this.mod = mod;
    }

    @Override
    public Packet3Chat serverChat(NetHandler handler, Packet3Chat message)
    {
        mod.serverChat((NetServerHandler)handler, message.field_73476_b);
        return message;
    }

    @Override
    public Packet3Chat clientChat(NetHandler handler, Packet3Chat message)
    {
        mod.clientChat(message.field_73476_b);
        return message;
    }

}
