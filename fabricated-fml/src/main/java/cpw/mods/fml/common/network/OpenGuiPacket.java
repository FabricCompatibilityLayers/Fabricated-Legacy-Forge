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

import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.FMLEntityPlayerExtension;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.NetHandlerExtension;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetworkManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class OpenGuiPacket extends FMLPacket
{
    private int windowId;
    private int networkId;
    private int modGuiId;
    private int x;
    private int y;
    private int z;
    
    public OpenGuiPacket()
    {
        super(Type.GUIOPEN);
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        ByteArrayDataOutput dat = ByteStreams.newDataOutput();
        dat.writeInt((Integer) data[0]); // windowId
        dat.writeInt((Integer) data[1]); // networkId
        dat.writeInt((Integer) data[2]); // modGuiId
        dat.writeInt((Integer) data[3]); // x
        dat.writeInt((Integer) data[4]); // y
        dat.writeInt((Integer) data[5]); // z
        return dat.toByteArray();
    }

    @Override
    public FMLPacket consumePacket(byte[] data)
    {
        ByteArrayDataInput dat = ByteStreams.newDataInput(data);
        windowId = dat.readInt();
        networkId = dat.readInt();
        modGuiId = dat.readInt();
        x = dat.readInt();
        y = dat.readInt();
        z = dat.readInt();
        return this;
    }

    @Override
    public void execute(NetworkManager network, FMLNetworkHandler handler, NetHandler netHandler, String userName)
    {
        EntityPlayer player = ((NetHandlerExtension) netHandler).getPlayer();
        ((FMLEntityPlayerExtension) player).openGui(networkId, modGuiId, player.field_70170_p, x, y, z);
        player.field_71070_bA.field_75152_c = windowId;
    }

}
