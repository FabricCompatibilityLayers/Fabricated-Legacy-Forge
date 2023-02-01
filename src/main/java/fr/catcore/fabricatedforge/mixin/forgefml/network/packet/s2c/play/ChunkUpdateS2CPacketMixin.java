package fr.catcore.fabricatedforge.mixin.forgefml.network.packet.s2c.play;

import net.minecraft.network.Packet;
import net.minecraft.network.class_673;
import net.minecraft.network.packet.s2c.play.ChunkUpdateS2CPacket;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkNibbleArray;
import net.minecraft.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Mixin(ChunkUpdateS2CPacket.class)
public abstract class ChunkUpdateS2CPacketMixin extends Packet {

    @Shadow public int field_2479;

    @Shadow public int field_2480;

    @Shadow public boolean field_2483;

    @Shadow public int field_2481;

    @Shadow public int field_2482;

    @Shadow private int field_2486;

    @Shadow private static byte[] field_2487;

    @Shadow private byte[] field_2485;

    @Shadow private byte[] field_2484;
    private Semaphore deflateGate;

    @Inject(method = "<init>(Lnet/minecraft/world/chunk/Chunk;ZI)V", at = @At("RETURN"))
    private void ctr$deflate(Chunk bl, boolean i, int par3, CallbackInfo ci) {
        this.deflateGate = new Semaphore(1);
    }

    private void deflate() {
        Deflater var5 = new Deflater(-1);

        try {
            var5.setInput(this.field_2485, 0, this.field_2485.length);
            var5.finish();
            byte[] deflated = new byte[this.field_2485.length];
            this.field_2486 = var5.deflate(deflated);
            this.field_2484 = deflated;
        } finally {
            var5.end();
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void readFrom(DataInputStream par1DataInputStream) {
        try {
            this.field_2479 = par1DataInputStream.readInt();
            this.field_2480 = par1DataInputStream.readInt();
            this.field_2483 = par1DataInputStream.readBoolean();
            this.field_2481 = par1DataInputStream.readShort();
            this.field_2482 = par1DataInputStream.readShort();
            this.field_2486 = par1DataInputStream.readInt();
            if (field_2487.length < this.field_2486) {
                field_2487 = new byte[this.field_2486];
            }

            par1DataInputStream.readFully(field_2487, 0, this.field_2486);
            int var2 = 0;
            int msb = 0;

            for(int var3 = 0; var3 < 16; ++var3) {
                var2 += this.field_2481 >> var3 & 1;
                msb += this.field_2482 >> var3 & 1;
            }

            int var12 = 12288 * var2;
            var12 += 2048 * msb;
            if (this.field_2483) {
                var12 += 256;
            }

            this.field_2485 = new byte[var12];
            Inflater var4 = new Inflater();
            var4.setInput(field_2487, 0, this.field_2486);

            try {
                var4.inflate(this.field_2485);
            } catch (DataFormatException var10) {
                throw new IOException("Bad compressed data format");
            } finally {
                var4.end();
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
            if (this.field_2484 == null) {
                this.deflateGate.acquireUninterruptibly();
                if (this.field_2484 == null) {
                    this.deflate();
                }

                this.deflateGate.release();
            }

            par1DataOutputStream.writeInt(this.field_2479);
            par1DataOutputStream.writeInt(this.field_2480);
            par1DataOutputStream.writeBoolean(this.field_2483);
            par1DataOutputStream.writeShort((short)(this.field_2481 & 65535));
            par1DataOutputStream.writeShort((short)(this.field_2482 & 65535));
            par1DataOutputStream.writeInt(this.field_2486);
            par1DataOutputStream.write(this.field_2484, 0, this.field_2486);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public static class_673 method_1813(Chunk par0Chunk, boolean par1, int par2) {
        int var3 = 0;
        ChunkSection[] var4 = par0Chunk.getBlockStorage();
        int var5 = 0;
        class_673 var6 = new class_673();
        byte[] var7 = field_2487;
        if (par1) {
            par0Chunk.field_4738 = true;
        }

        for(int var8 = 0; var8 < var4.length; ++var8) {
            if (var4[var8] != null && (!par1 || !var4[var8].isEmpty()) && (par2 & 1 << var8) != 0) {
                var6.field_2489 |= 1 << var8;
                if (var4[var8].method_3944() != null) {
                    var6.field_2490 |= 1 << var8;
                    ++var5;
                }
            }
        }

        for(int var111 = 0; var111 < var4.length; ++var111) {
            if (var4[var111] != null && (!par1 || !var4[var111].isEmpty()) && (par2 & 1 << var111) != 0) {
                byte[] var9 = var4[var111].getBlocks();
                System.arraycopy(var9, 0, var7, var3, var9.length);
                var3 += var9.length;
            }
        }

        for(int var12 = 0; var12 < var4.length; ++var12) {
            if (var4[var12] != null && (!par1 || !var4[var12].isEmpty()) && (par2 & 1 << var12) != 0) {
                ChunkNibbleArray var10 = var4[var12].getBlockData();
                System.arraycopy(var10.bytes, 0, var7, var3, var10.bytes.length);
                var3 += var10.bytes.length;
            }
        }

        for(int var13 = 0; var13 < var4.length; ++var13) {
            if (var4[var13] != null && (!par1 || !var4[var13].isEmpty()) && (par2 & 1 << var13) != 0) {
                ChunkNibbleArray var10 = var4[var13].getBlockLight();
                System.arraycopy(var10.bytes, 0, var7, var3, var10.bytes.length);
                var3 += var10.bytes.length;
            }
        }

        if (!par0Chunk.world.dimension.isNether) {
            for(int var14 = 0; var14 < var4.length; ++var14) {
                if (var4[var14] != null && (!par1 || !var4[var14].isEmpty()) && (par2 & 1 << var14) != 0) {
                    ChunkNibbleArray var10 = var4[var14].getSkyLight();
                    System.arraycopy(var10.bytes, 0, var7, var3, var10.bytes.length);
                    var3 += var10.bytes.length;
                }
            }
        }

        if (var5 > 0) {
            for(int var15 = 0; var15 < var4.length; ++var15) {
                if (var4[var15] != null && (!par1 || !var4[var15].isEmpty()) && var4[var15].method_3944() != null && (par2 & 1 << var15) != 0) {
                    ChunkNibbleArray var10 = var4[var15].method_3944();
                    System.arraycopy(var10.bytes, 0, var7, var3, var10.bytes.length);
                    var3 += var10.bytes.length;
                }
            }
        }

        if (par1) {
            byte[] var11 = par0Chunk.getBiomeArray();
            System.arraycopy(var11, 0, var7, var3, var11.length);
            var3 += var11.length;
        }

        var6.field_2488 = new byte[var3];
        System.arraycopy(var7, 0, var6.field_2488, 0, var3);
        return var6;
    }
}
