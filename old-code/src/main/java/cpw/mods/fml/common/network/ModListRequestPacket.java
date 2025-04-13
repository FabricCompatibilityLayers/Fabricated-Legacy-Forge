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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.network.Connection;
import net.minecraft.network.listener.PacketListener;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cpw.mods.fml.common.network.FMLPacket.Type.MOD_LIST_RESPONSE;

public class ModListRequestPacket extends FMLPacket {
    private List<String> sentModList;
    private byte compatibilityLevel;

    public ModListRequestPacket() {
        super(Type.MOD_LIST_REQUEST);
    }

    public byte[] generatePacket(Object... data) {
        ByteArrayDataOutput dat = ByteStreams.newDataOutput();
        Set<ModContainer> activeMods = FMLNetworkHandler.instance().getNetworkModList();
        dat.writeInt(activeMods.size());
        for (ModContainer mc : activeMods)
        {
            dat.writeUTF(mc.getModId());
        }
        dat.writeByte(FMLNetworkHandler.getCompatibilityLevel());
        return dat.toByteArray();
    }

    public FMLPacket consumePacket(byte[] data) {
        this.sentModList = Lists.newArrayList();
        ByteArrayDataInput in = ByteStreams.newDataInput(data);
        int listSize = in.readInt();

        for(int i = 0; i < listSize; ++i) {
            this.sentModList.add(in.readUTF());
        }

        try {
            this.compatibilityLevel = in.readByte();
        } catch (IllegalStateException var5) {
            FMLLog.fine("No compatibility byte found - the server is too old", new Object[0]);
        }

        return this;
    }

    public void execute(Connection mgr, FMLNetworkHandler handler, PacketListener netHandler, String userName) {
        List<String> missingMods = Lists.newArrayList();
        Map<String,String> modVersions = Maps.newHashMap();
        Map<String, ModContainer> indexedModList = Maps.newHashMap(Loader.instance().getIndexedModList());

        for (String m : sentModList)
        {
            ModContainer mc = indexedModList.get(m);
            if (mc == null)
            {
                missingMods.add(m);
                continue;
            }
            indexedModList.remove(m);
            modVersions.put(m, mc.getVersion());
        }

        if (indexedModList.size()>0)
        {
            for (Map.Entry<String, ModContainer> e : indexedModList.entrySet())
            {
                if (e.getValue().isNetworkMod())
                {
                    NetworkModHandler missingHandler = FMLNetworkHandler.instance().findNetworkModHandler(e.getValue());
                    if (missingHandler.requiresServerSide())
                    {
                        // TODO : what should we do if a mod is marked "serverSideRequired"? Stop the connection?
                        FMLLog.warning("The mod %s was not found on the server you connected to, but requested that the server side be present", e.getKey());
                    }
                }
            }
        }

        FMLLog.fine("The server has compatibility level %d", compatibilityLevel);
        FMLCommonHandler.instance().getSidedDelegate().setClientCompatibilityLevel(compatibilityLevel);

        mgr.send(PacketDispatcher.getPacket("FML", FMLPacket.makePacket(MOD_LIST_RESPONSE, modVersions, missingMods)));
    }
}
