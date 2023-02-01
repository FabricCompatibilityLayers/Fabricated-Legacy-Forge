package fr.catcore.fabricatedforge.mixin.forgefml.network.packet.s2c.play;

import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.class_687;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Mixin(class_687.class)
public abstract class class_687Mixin extends Packet {

    @Shadow private int field_2536;

    @Shadow private int[] field_2532;

    @Shadow private int[] field_2533;

    @Shadow public int[] field_2530;

    @Shadow public int[] field_2531;

    @Shadow private byte[][] field_2535;

    @Shadow private static byte[] field_2537;

    @Shadow private byte[] field_2534;
    @Shadow private boolean field_5275;
    private int maxLen = 0;
    private Semaphore deflateGate;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"))
    private void ctr$deflate(List par1, CallbackInfo ci) {
        this.deflateGate = new Semaphore(1);
        this.maxLen = this.field_2534.length;
    }

    private void deflate() {
        byte[] data = new byte[this.maxLen];
        int offset = 0;

        for(int x = 0; x < this.field_2535.length; ++x) {
            System.arraycopy(this.field_2535[x], 0, data, offset, this.field_2535[x].length);
            offset += this.field_2535[x].length;
        }

        Deflater var11 = new Deflater(-1);

        try {
            var11.setInput(data, 0, this.maxLen);
            var11.finish();
            byte[] deflated = new byte[this.maxLen];
            this.field_2536 = var11.deflate(deflated);
            this.field_2534 = deflated;
        } finally {
            var11.end();
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void readFrom(DataInputStream par1DataInputStream) {
        try {
            short var2 = par1DataInputStream.readShort();
            this.field_2536 = par1DataInputStream.readInt();
            this.field_5275 = par1DataInputStream.readBoolean();
            this.field_2532 = new int[var2];
            this.field_2533 = new int[var2];
            this.field_2530 = new int[var2];
            this.field_2531 = new int[var2];
            this.field_2535 = new byte[var2][];
            if (field_2537.length < this.field_2536) {
                field_2537 = new byte[this.field_2536];
            }

            par1DataInputStream.readFully(field_2537, 0, this.field_2536);
            byte[] var3 = new byte[196864 * var2];
            Inflater var4 = new Inflater();
            var4.setInput(field_2537, 0, this.field_2536);

            try {
                var4.inflate(var3);
            } catch (DataFormatException var12) {
                throw new IOException("Bad compressed data format");
            } finally {
                var4.end();
            }

            int var5 = 0;

            for(int var6 = 0; var6 < var2; ++var6) {
                this.field_2532[var6] = par1DataInputStream.readInt();
                this.field_2533[var6] = par1DataInputStream.readInt();
                this.field_2530[var6] = par1DataInputStream.readShort();
                this.field_2531[var6] = par1DataInputStream.readShort();
                int var7 = 0;
                int var8 = 0;

                for(int var9 = 0; var9 < 16; ++var9) {
                    var7 += this.field_2530[var6] >> var9 & 1;
                    var8 += this.field_2531[var6] >> var9 & 1;
                }

                int var14 = 8192 * var7 + 256;
                var14 += 2048 * var8;
                if (this.field_5275) {
                    var14 += 2048 * var7;
                }

                this.field_2535[var6] = new byte[var14];
                System.arraycopy(var3, var5, this.field_2535[var6], 0, var14);
                var5 += var14;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author forge
     * @reason deflate
     */
    @Overwrite
    public void writeTo(DataOutputStream par1DataOutputStream) {
        try {
            if (this.field_2534 == null) {
                this.deflateGate.acquireUninterruptibly();
                if (this.field_2534 == null) {
                    this.deflate();
                }

                this.deflateGate.release();
            }

            par1DataOutputStream.writeShort(this.field_2532.length);
            par1DataOutputStream.writeInt(this.field_2536);
            par1DataOutputStream.writeBoolean(this.field_5275);
            par1DataOutputStream.write(this.field_2534, 0, this.field_2536);

            for(int var2 = 0; var2 < this.field_2532.length; ++var2) {
                par1DataOutputStream.writeInt(this.field_2532[var2]);
                par1DataOutputStream.writeInt(this.field_2533[var2]);
                par1DataOutputStream.writeShort((short)(this.field_2530[var2] & 65535));
                par1DataOutputStream.writeShort((short)(this.field_2531[var2] & 65535));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
