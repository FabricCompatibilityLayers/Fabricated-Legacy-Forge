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

import java.util.Arrays;
import java.util.logging.Level;

import net.minecraft.src.NetHandler;
import net.minecraft.src.NetworkManager;

import com.google.common.base.Throwables;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedBytes;

import cpw.mods.fml.common.FMLLog;

public abstract class FMLPacket
{
    enum Type
    {
        /**
         * Opening salutation from the server to the client -> request all mods from the client
         */
        MOD_LIST_REQUEST(ModListRequestPacket.class),
        /**
         * The client responds with the list of mods and versions it has. This is verified by the server.
         */
        MOD_LIST_RESPONSE(ModListResponsePacket.class),
        /**
         * At which point the server tells the client the mod identifiers for this session.
         */
        MOD_IDENTIFIERS(ModIdentifiersPacket.class),
        /**
         * Or, if there is missing stuff, the server tells the client what's missing and drops the connection.
         */
        MOD_MISSING(ModMissingPacket.class),
        /**
         * Open a GUI on the client from the server
         */
        GUIOPEN(OpenGuiPacket.class),
        /**
         * Spawn an entity on the client from the server
         */
        ENTITYSPAWN(EntitySpawnPacket.class),
        /**
         * Fixes entity location data after spawning
         */
        ENTITYSPAWNADJUSTMENT(EntitySpawnAdjustmentPacket.class);
        

        private Class<? extends FMLPacket> packetType;

        private Type(Class<? extends FMLPacket> clazz)
        {
            this.packetType = clazz;
        }

        FMLPacket make()
        {
            try
            {
                return this.packetType.newInstance();
            }
            catch (Exception e)
            {
                Throwables.propagateIfPossible(e);
                FMLLog.log(Level.SEVERE, e, "A bizarre critical error occured during packet encoding");
                throw new FMLNetworkException(e);
            }
        }
    }

    private Type type;

    public static byte[] makePacket(Type type, Object... data)
    {
        byte[] packetData = type.make().generatePacket(data);
        return Bytes.concat(new byte[] { UnsignedBytes.checkedCast(type.ordinal()) }, packetData );
    }

    public static FMLPacket readPacket(byte[] payload)
    {
        int type = UnsignedBytes.toInt(payload[0]);
        return Type.values()[type].make().consumePacket(Arrays.copyOfRange(payload, 1, payload.length));
    }

    public FMLPacket(Type type)
    {
        this.type = type;
    }

    public abstract byte[] generatePacket(Object... data);

    public abstract FMLPacket consumePacket(byte[] data);

    public abstract void execute(NetworkManager network, FMLNetworkHandler handler, NetHandler netHandler, String userName);
    {
        // TODO Auto-generated method stub

    }
}
