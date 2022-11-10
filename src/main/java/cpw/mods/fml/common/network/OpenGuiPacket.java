package cpw.mods.fml.common.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.catcore.fabricatedforge.mixininterface.IPacketListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Connection;
import net.minecraft.network.listener.PacketListener;

public class OpenGuiPacket extends FMLPacket {
    private int windowId;
    private int networkId;
    private int modGuiId;
    private int x;
    private int y;
    private int z;

    public OpenGuiPacket() {
        super(Type.GUIOPEN);
    }

    public byte[] generatePacket(Object... data) {
        ByteArrayDataOutput dat = ByteStreams.newDataOutput();
        dat.writeInt((Integer) data[0]);
        dat.writeInt((Integer) data[1]);
        dat.writeInt((Integer) data[2]);
        dat.writeInt((Integer) data[3]);
        dat.writeInt((Integer) data[4]);
        dat.writeInt((Integer) data[5]);
        return dat.toByteArray();
    }

    public FMLPacket consumePacket(byte[] data) {
        ByteArrayDataInput dat = ByteStreams.newDataInput(data);
        this.windowId = dat.readInt();
        this.networkId = dat.readInt();
        this.modGuiId = dat.readInt();
        this.x = dat.readInt();
        this.y = dat.readInt();
        this.z = dat.readInt();
        return this;
    }

    public void execute(Connection network, FMLNetworkHandler handler, PacketListener netHandler, String userName) {
        PlayerEntity player = ((IPacketListener)netHandler).getPlayer();
        player.openGui(this.networkId, this.modGuiId, player.world, this.x, this.y, this.z);
        player.openScreenHandler.syncId = this.windowId;
    }
}
