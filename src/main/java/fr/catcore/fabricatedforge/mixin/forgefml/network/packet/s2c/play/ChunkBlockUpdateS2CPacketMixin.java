package fr.catcore.fabricatedforge.mixin.forgefml.network.packet.s2c.play;

import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.ChunkBlockUpdateS2CPacket;
import net.minecraftforge.common.ForgeDummyContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;

@Mixin(ChunkBlockUpdateS2CPacket.class)
public abstract class ChunkBlockUpdateS2CPacketMixin extends Packet {
    @Shadow public int x;

    @Shadow public int z;

    @Shadow public int field_2413;

    @Shadow public byte[] field_2412;

    @ModifyConstant(method = "<init>(II[SILnet/minecraft/world/World;)V", constant = @Constant(intValue = 64))
    private int useForgeConstant(int constant) {
        return ForgeDummyContainer.clumpingThreshold;
    }

    @Redirect(method = "<init>(II[SILnet/minecraft/world/World;)V", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"))
    private void dontSpamLog(PrintStream instance, String x) {
        if (!x.startsWith("ChunkTilesUpdatePacket compress ")) {
            instance.println(x);
        }
    }

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    public void readFrom(DataInputStream par1DataInputStream) {
        try {
            this.x = par1DataInputStream.readInt();
            this.z = par1DataInputStream.readInt();
            this.field_2413 = par1DataInputStream.readShort() & '\uffff';
            int var2 = par1DataInputStream.readInt();
            if (var2 > 0) {
                this.field_2412 = new byte[var2];
                par1DataInputStream.readFully(this.field_2412);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    public void writeTo(DataOutputStream par1DataOutputStream) {
        try {
            par1DataOutputStream.writeInt(this.x);
            par1DataOutputStream.writeInt(this.z);
            par1DataOutputStream.writeShort((short) this.field_2413);
            if (this.field_2412 != null) {
                par1DataOutputStream.writeInt(this.field_2412.length);
                par1DataOutputStream.write(this.field_2412);
            } else {
                par1DataOutputStream.writeInt(0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
