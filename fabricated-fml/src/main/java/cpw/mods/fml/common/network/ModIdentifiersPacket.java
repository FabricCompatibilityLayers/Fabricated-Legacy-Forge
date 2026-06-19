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

import static cpw.mods.fml.common.network.FMLPacket.Type.MOD_IDENTIFIERS;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.src.NetHandler;
import net.minecraft.src.INetworkManager;

import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class ModIdentifiersPacket extends FMLPacket
{

    private Map<String, Integer> modIds = Maps.newHashMap();

    public ModIdentifiersPacket()
    {
        super(MOD_IDENTIFIERS);
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        ByteArrayDataOutput dat = ByteStreams.newDataOutput();
        Collection<NetworkModHandler >networkMods = FMLNetworkHandler.instance().getNetworkIdMap().values();

        dat.writeInt(networkMods.size());
        for (NetworkModHandler handler : networkMods)
        {
            dat.writeUTF(handler.getContainer().getModId());
            dat.writeInt(handler.getNetworkId());
        }

        // TODO send the other id maps as well
        return dat.toByteArray();
    }

    @Override
    public FMLPacket consumePacket(byte[] data)
    {
        ByteArrayDataInput dat = ByteStreams.newDataInput(data);
        int listSize = dat.readInt();
        for (int i = 0; i < listSize; i++)
        {
            String modId = dat.readUTF();
            int networkId = dat.readInt();
            modIds.put(modId, networkId);
        }
        return this;
    }

    @Override
    public void execute(INetworkManager network, FMLNetworkHandler handler, NetHandler netHandler, String userName)
    {
        for (Entry<String,Integer> idEntry : modIds.entrySet())
        {
            handler.bindNetworkId(idEntry.getKey(), idEntry.getValue());
        }
        // TODO other id maps
    }
}
