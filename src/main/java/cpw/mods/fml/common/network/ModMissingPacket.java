package cpw.mods.fml.common.network;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionRange;
import net.minecraft.network.Connection;
import net.minecraft.network.listener.PacketListener;

import java.util.Iterator;
import java.util.List;

public class ModMissingPacket extends FMLPacket {
    private List<ModMissingPacket.ModData> missing;
    private List<ModMissingPacket.ModData> badVersion;

    public ModMissingPacket() {
        super(Type.MOD_MISSING);
    }

    public byte[] generatePacket(Object... data) {
        ByteArrayDataOutput dat = ByteStreams.newDataOutput();
        List<String> missing = (List)data[0];
        List<String> badVersion = (List)data[1];
        dat.writeInt(missing.size());
        Iterator i$ = missing.iterator();

        String bad;
        ModContainer mc;
        while(i$.hasNext()) {
            bad = (String)i$.next();
            mc = (ModContainer) Loader.instance().getIndexedModList().get(bad);
            dat.writeUTF(bad);
            dat.writeUTF(mc.getVersion());
        }

        dat.writeInt(badVersion.size());
        i$ = badVersion.iterator();

        while(i$.hasNext()) {
            bad = (String)i$.next();
            mc = (ModContainer) Loader.instance().getIndexedModList().get(bad);
            dat.writeUTF(bad);
            dat.writeUTF(mc.getVersion());
        }

        return dat.toByteArray();
    }

    public FMLPacket consumePacket(byte[] data) {
        ByteArrayDataInput dat = ByteStreams.newDataInput(data);
        int missingLen = dat.readInt();
        this.missing = Lists.newArrayListWithCapacity(missingLen);

        int badVerLength;
        for(badVerLength = 0; badVerLength < missingLen; ++badVerLength) {
            ModMissingPacket.ModData md = new ModMissingPacket.ModData();
            md.modId = dat.readUTF();
            md.modVersion = dat.readUTF();
            this.missing.add(md);
        }

        badVerLength = dat.readInt();
        this.badVersion = Lists.newArrayListWithCapacity(badVerLength);

        for(int i = 0; i < badVerLength; ++i) {
            ModMissingPacket.ModData md = new ModMissingPacket.ModData();
            md.modId = dat.readUTF();
            md.modVersion = dat.readUTF();
            this.badVersion.add(md);
        }

        return this;
    }

    public void execute(Connection network, FMLNetworkHandler handler, PacketListener netHandler, String userName) {
        FMLCommonHandler.instance().getSidedDelegate().displayMissingMods(this);
    }

    public List<ArtifactVersion> getModList() {
        ImmutableList.Builder<ArtifactVersion> builder = ImmutableList.builder();
        Iterator i$ = this.missing.iterator();

        ModMissingPacket.ModData md;
        while(i$.hasNext()) {
            md = (ModMissingPacket.ModData)i$.next();
            builder.add(new DefaultArtifactVersion(md.modId, VersionRange.createFromVersion(md.modVersion, (ArtifactVersion)null)));
        }

        i$ = this.badVersion.iterator();

        while(i$.hasNext()) {
            md = (ModMissingPacket.ModData)i$.next();
            builder.add(new DefaultArtifactVersion(md.modId, VersionRange.createFromVersion(md.modVersion, (ArtifactVersion)null)));
        }

        return builder.build();
    }

    private static class ModData {
        String modId;
        String modVersion;

        private ModData() {
        }
    }
}
