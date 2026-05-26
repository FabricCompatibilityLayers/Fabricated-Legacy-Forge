package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import net.minecraft.src.Packet;
import net.minecraft.src.Packet56MapChunks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.beans.Expression;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

@Mixin(Packet56MapChunks.class)
public abstract class Packet56MapChunksMixin extends Packet {
    @Shadow private byte[][] field_73584_f;

    @Shadow public int[] field_73588_b;

    @Shadow public int[] field_73590_a;

    @Shadow private int[] field_73586_d;

    @Shadow private int[] field_73589_c;

    @Shadow private static byte[] field_73591_h;

    @Shadow private int field_73585_g;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void readPacketData(DataInputStream par1DataInputStream) {
        try {
            short var2 = par1DataInputStream.readShort();
            this.field_73585_g = par1DataInputStream.readInt();
            this.field_73589_c = new int[var2];
            this.field_73586_d = new int[var2];
            this.field_73590_a = new int[var2];
            this.field_73588_b = new int[var2];
            this.field_73584_f = new byte[var2][];
            if (field_73591_h.length < this.field_73585_g) {
                field_73591_h = new byte[this.field_73585_g];
            }

            par1DataInputStream.readFully(field_73591_h, 0, this.field_73585_g);
            byte[] var3 = new byte[196864 * var2];
            Inflater var4 = new Inflater();
            var4.setInput(field_73591_h, 0, this.field_73585_g);

            try {
                var4.inflate(var3);
            } catch (DataFormatException var11) {
                throw new IOException("Bad compressed data format");
            } finally {
                var4.end();
            }

            int var5 = 0;

            for (int var6 = 0; var6 < var2; var6++) {
                this.field_73589_c[var6] = par1DataInputStream.readInt();
                this.field_73586_d[var6] = par1DataInputStream.readInt();
                this.field_73590_a[var6] = par1DataInputStream.readShort();
                this.field_73588_b[var6] = par1DataInputStream.readShort();
                int var7 = 0;

                for (int var8 = 0; var8 < 16; var8++) {
                    var7 += this.field_73590_a[var6] >> var8 & 1;
                }

                int msb = 0; //BugFix: MC does not read the MSB array from the packet properly, causing issues for servers that use blocks > 256
                for (int x = 0; x < 16; x++) {
                    msb += (field_73588_b[var6] >> x) & 1;
                }

                int var13 = 2048 * 5 * var7 + (2048 * msb) + 256;
                this.field_73584_f[var6] = new byte[var13];
                System.arraycopy(var3, var5, this.field_73584_f[var6], 0, var13);
                var5 += var13;
            }
        } catch (IOException var12) {
            throw new RuntimeException(var12);
        }
    }
}
