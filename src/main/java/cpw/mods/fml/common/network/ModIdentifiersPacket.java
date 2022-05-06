package cpw.mods.fml.common.network;

import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.network.Connection;
import net.minecraft.network.listener.PacketListener;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class ModIdentifiersPacket extends FMLPacket {
    private Map<String, Integer> modIds = Maps.newHashMap();

    public ModIdentifiersPacket() {
        super(Type.MOD_IDENTIFIERS);
    }

    public byte[] generatePacket(Object... data) {
        ByteArrayDataOutput dat = ByteStreams.newDataOutput();
        Collection<NetworkModHandler> networkMods = FMLNetworkHandler.instance().getNetworkIdMap().values();
        dat.writeInt(networkMods.size());
        Iterator i$ = networkMods.iterator();

        while(i$.hasNext()) {
            NetworkModHandler handler = (NetworkModHandler)i$.next();
            dat.writeUTF(handler.getContainer().getModId());
            dat.writeInt(handler.getNetworkId());
        }

        return dat.toByteArray();
    }

    public FMLPacket consumePacket(byte[] data) {
        ByteArrayDataInput dat = ByteStreams.newDataInput(data);
        int listSize = dat.readInt();

        for(int i = 0; i < listSize; ++i) {
            String modId = dat.readUTF();
            int networkId = dat.readInt();
            this.modIds.put(modId, networkId);
        }

        return this;
    }

    public void execute(Connection network, FMLNetworkHandler handler, PacketListener netHandler, String userName) {
        Iterator i$ = this.modIds.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<String, Integer> idEntry = (Map.Entry)i$.next();
            handler.bindNetworkId((String)idEntry.getKey(), (Integer)idEntry.getValue());
        }

    }
}
