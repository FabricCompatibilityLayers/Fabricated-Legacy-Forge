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
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import fr.catcore.fabricatedforge.mixininterface.IPendingConnection;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Connection;
import net.minecraft.network.PendingConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static cpw.mods.fml.common.network.FMLPacket.Type.MOD_IDENTIFIERS;
import static cpw.mods.fml.common.network.FMLPacket.Type.MOD_MISSING;

public class ModListResponsePacket extends FMLPacket {
    private Map<String, String> modVersions;
    private List<String> missingMods;

    public ModListResponsePacket() {
        super(Type.MOD_LIST_RESPONSE);
    }

    public byte[] generatePacket(Object... data) {
        Map<String, String> modVersions = (Map)data[0];
        List<String> missingMods = (List)data[1];
        ByteArrayDataOutput dat = ByteStreams.newDataOutput();
        dat.writeInt(modVersions.size());

        for(Map.Entry<String, String> version : modVersions.entrySet()) {
            dat.writeUTF((String)version.getKey());
            dat.writeUTF((String)version.getValue());
        }

        dat.writeInt(missingMods.size());

        for(String missing : missingMods) {
            dat.writeUTF(missing);
        }

        return dat.toByteArray();
    }

    public FMLPacket consumePacket(byte[] data) {
        ByteArrayDataInput dat = ByteStreams.newDataInput(data);
        int versionListSize = dat.readInt();
        this.modVersions = Maps.newHashMapWithExpectedSize(versionListSize);

        for(int i = 0; i < versionListSize; ++i) {
            String modName = dat.readUTF();
            String modVersion = dat.readUTF();
            this.modVersions.put(modName, modVersion);
        }

        int missingModSize = dat.readInt();
        this.missingMods = Lists.newArrayListWithExpectedSize(missingModSize);

        for(int i = 0; i < missingModSize; ++i) {
            this.missingMods.add(dat.readUTF());
        }

        return this;
    }

    public void execute(Connection network, FMLNetworkHandler handler, PacketListener netHandler, String userName) {
        Map<String, ModContainer> indexedModList = Maps.newHashMap(Loader.instance().getIndexedModList());
        List<String> missingClientMods = Lists.newArrayList();
        List<String> versionIncorrectMods = Lists.newArrayList();

        for(String m : this.missingMods) {
            ModContainer mc = (ModContainer)indexedModList.get(m);
            NetworkModHandler networkMod = handler.findNetworkModHandler(mc);
            if (networkMod.requiresClientSide()) {
                missingClientMods.add(m);
            }
        }

        for(Map.Entry<String, String> modVersion : this.modVersions.entrySet()) {
            ModContainer mc = (ModContainer)indexedModList.get(modVersion.getKey());
            NetworkModHandler networkMod = handler.findNetworkModHandler(mc);
            if (!networkMod.acceptVersion((String)modVersion.getValue())) {
                versionIncorrectMods.add(modVersion.getKey());
            }
        }

        CustomPayloadC2SPacket pkt = new CustomPayloadC2SPacket();
        pkt.channel = "FML";
        if (missingClientMods.size() <= 0 && versionIncorrectMods.size() <= 0) {
            pkt.field_2455 = FMLPacket.makePacket(Type.MOD_IDENTIFIERS, new Object[]{netHandler});
            Logger.getLogger("Minecraft").info(String.format("User %s connecting with mods %s", userName, this.modVersions.keySet()));
            FMLLog.info("User %s connecting with mods %s", new Object[]{userName, this.modVersions.keySet()});
            pkt.field_2454 = pkt.field_2455.length;
            network.send(pkt);
            NbtList itemList = new NbtList();
            GameData.writeItemData(itemList);
            byte[][] registryPackets = FMLPacket.makePacketSet(Type.MOD_IDMAP, new Object[]{itemList});

            for(int i = 0; i < registryPackets.length; ++i) {
                network.send(PacketDispatcher.getPacket("FML", registryPackets[i]));
            }
        } else {
            pkt.field_2455 = FMLPacket.makePacket(Type.MOD_MISSING, new Object[]{missingClientMods, versionIncorrectMods});
            Logger.getLogger("Minecraft")
                    .info(String.format("User %s connection failed: missing %s, bad versions %s", userName, missingClientMods, versionIncorrectMods));
            FMLLog.info("User %s connection failed: missing %s, bad versions %s", new Object[]{userName, missingClientMods, versionIncorrectMods});
            FMLNetworkHandler.setHandlerState((PendingConnection)netHandler, -2);
            pkt.field_2454 = pkt.field_2455.length;
            network.send(pkt);
        }

        ((IPendingConnection) netHandler).method_2189_fabric(true);
    }
}
