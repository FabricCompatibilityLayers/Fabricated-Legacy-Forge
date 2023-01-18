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

import com.google.common.base.Throwables;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedBytes;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.network.Connection;
import net.minecraft.network.listener.PacketListener;

import java.util.Arrays;
import java.util.logging.Level;

public abstract class FMLPacket {
    private FMLPacket.Type type;

    public static byte[] makePacket(FMLPacket.Type type, Object... data) {
        byte[] packetData = type.make().generatePacket(data);
        return Bytes.concat(new byte[][]{{UnsignedBytes.checkedCast((long)type.ordinal())}, packetData});
    }

    public static FMLPacket readPacket(byte[] payload) {
        int type = UnsignedBytes.toInt(payload[0]);
        return FMLPacket.Type.values()[type].make().consumePacket(Arrays.copyOfRange(payload, 1, payload.length));
    }

    public FMLPacket(FMLPacket.Type type) {
        this.type = type;
    }

    public abstract byte[] generatePacket(Object... objects);

    public abstract FMLPacket consumePacket(byte[] bs);

    public abstract void execute(Connection arg, FMLNetworkHandler fMLNetworkHandler, PacketListener arg2, String string);

    static enum Type {
        MOD_LIST_REQUEST(ModListRequestPacket.class),
        MOD_LIST_RESPONSE(ModListResponsePacket.class),
        MOD_IDENTIFIERS(ModIdentifiersPacket.class),
        MOD_MISSING(ModMissingPacket.class),
        GUIOPEN(OpenGuiPacket.class),
        ENTITYSPAWN(EntitySpawnPacket.class),
        ENTITYSPAWNADJUSTMENT(EntitySpawnAdjustmentPacket.class);

        private Class<? extends FMLPacket> packetType;

        private Type(Class<? extends FMLPacket> clazz) {
            this.packetType = clazz;
        }

        FMLPacket make() {
            try {
                return (FMLPacket)this.packetType.newInstance();
            } catch (Exception var2) {
                Throwables.propagateIfPossible(var2);
                FMLLog.log(Level.SEVERE, var2, "A bizarre critical error occured during packet encoding", new Object[0]);
                throw new FMLNetworkException(var2);
            }
        }
    }
}
