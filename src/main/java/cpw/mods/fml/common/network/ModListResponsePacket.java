package cpw.mods.fml.common.network;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.network.Connection;
import net.minecraft.network.PendingConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
        Iterator i$ = modVersions.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<String, String> version = (Map.Entry)i$.next();
            dat.writeUTF((String)version.getKey());
            dat.writeUTF((String)version.getValue());
        }

        dat.writeInt(missingMods.size());
        i$ = missingMods.iterator();

        while(i$.hasNext()) {
            String missing = (String)i$.next();
            dat.writeUTF(missing);
        }

        return dat.toByteArray();
    }

    public FMLPacket consumePacket(byte[] data) {
        ByteArrayDataInput dat = ByteStreams.newDataInput(data);
        int versionListSize = dat.readInt();
        this.modVersions = Maps.newHashMapWithExpectedSize(versionListSize);

        int missingModSize;
        for(missingModSize = 0; missingModSize < versionListSize; ++missingModSize) {
            String modName = dat.readUTF();
            String modVersion = dat.readUTF();
            this.modVersions.put(modName, modVersion);
        }

        missingModSize = dat.readInt();
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
        Iterator i$ = this.missingMods.iterator();

        ModContainer mc;
        NetworkModHandler networkMod;
        while(i$.hasNext()) {
            String m = (String)i$.next();
            mc = (ModContainer)indexedModList.get(m);
            networkMod = handler.findNetworkModHandler(mc);
            if (networkMod.requiresClientSide()) {
                missingClientMods.add(m);
            }
        }

        i$ = this.modVersions.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<String, String> modVersion = (Map.Entry)i$.next();
            mc = (ModContainer)indexedModList.get(modVersion.getKey());
            networkMod = handler.findNetworkModHandler(mc);
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
        } else {
            pkt.field_2455 = FMLPacket.makePacket(Type.MOD_MISSING, new Object[]{missingClientMods, versionIncorrectMods});
            Logger.getLogger("Minecraft").info(String.format("User %s connection failed: missing %s, bad versions %s", userName, missingClientMods, versionIncorrectMods));
            FMLLog.info("User %s connection failed: missing %s, bad versions %s", new Object[]{userName, missingClientMods, versionIncorrectMods});
            FMLNetworkHandler.setHandlerState((PendingConnection)netHandler, -2);
        }

        pkt.field_2454 = pkt.field_2455.length;
        network.send(pkt);
        PendingConnection.method_2189((PendingConnection)netHandler, true);
    }
}
