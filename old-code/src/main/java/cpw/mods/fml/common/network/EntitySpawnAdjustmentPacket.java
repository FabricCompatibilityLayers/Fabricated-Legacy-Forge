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

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.listener.PacketListener;

public class EntitySpawnAdjustmentPacket extends FMLPacket {
    public int entityId;
    public int serverX;
    public int serverY;
    public int serverZ;

    public EntitySpawnAdjustmentPacket() {
        super(Type.ENTITYSPAWNADJUSTMENT);
    }

    public byte[] generatePacket(Object... data) {
        ByteArrayDataOutput dat = ByteStreams.newDataOutput();
        dat.writeInt((Integer)data[0]);
        dat.writeInt((Integer)data[1]);
        dat.writeInt((Integer)data[2]);
        dat.writeInt((Integer)data[3]);
        return dat.toByteArray();
    }

    public FMLPacket consumePacket(byte[] data) {
        ByteArrayDataInput dat = ByteStreams.newDataInput(data);
        this.entityId = dat.readInt();
        this.serverX = dat.readInt();
        this.serverY = dat.readInt();
        this.serverZ = dat.readInt();
        return this;
    }

    public void execute(Connection network, FMLNetworkHandler handler, PacketListener netHandler, String userName) {
        FMLCommonHandler.instance().adjustEntityLocationOnClient(this);
    }
}
