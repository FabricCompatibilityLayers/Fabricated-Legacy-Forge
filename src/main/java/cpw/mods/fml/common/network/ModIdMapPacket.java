package cpw.mods.fml.common.network;

import com.google.common.collect.MapDifference;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedBytes;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Connection;
import net.minecraft.network.listener.PacketListener;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;

public class ModIdMapPacket extends FMLPacket {
    private byte[][] partials;

    public ModIdMapPacket() {
        super(Type.MOD_IDMAP);
    }

    public byte[] generatePacket(Object... data) {
        NbtList completeList = (NbtList)data[0];
        NbtCompound wrap = new NbtCompound();
        wrap.put("List", completeList);

        try {
            return NbtIo.writeCompressed(wrap);
        } catch (Exception var5) {
            FMLLog.log(Level.SEVERE, var5, "A critical error writing the id map", new Object[0]);
            throw new FMLNetworkException(var5);
        }
    }

    public FMLPacket consumePacket(byte[] data) {
        ByteArrayDataInput bdi = ByteStreams.newDataInput(data);
        int chunkIdx = UnsignedBytes.toInt(bdi.readByte());
        int chunkTotal = UnsignedBytes.toInt(bdi.readByte());
        int chunkLength = bdi.readInt();
        if (this.partials == null) {
            this.partials = new byte[chunkTotal][];
        }

        this.partials[chunkIdx] = new byte[chunkLength];
        bdi.readFully(this.partials[chunkIdx]);

        for(int i = 0; i < this.partials.length; ++i) {
            if (this.partials[i] == null) {
                return null;
            }
        }

        return this;
    }

    public void execute(Connection network, FMLNetworkHandler handler, PacketListener netHandler, String userName) {
        byte[] allData = Bytes.concat(this.partials);
        GameData.initializeServerGate(1);

        try {
            NbtCompound serverList = NbtIo.readCompressed(allData);
            NbtList list = serverList.getList("List");
            Set<ItemData> itemData = GameData.buildWorldItemData(list);
            GameData.validateWorldSave(itemData);
            MapDifference<Integer, ItemData> serverDifference = GameData.gateWorldLoadingForValidation();
            if (serverDifference != null) {
                FMLCommonHandler.instance().disconnectIDMismatch(serverDifference, netHandler, network);
            }
        } catch (Exception var10) {
        }
    }
}
