package fr.catcore.fabricatedforge.mixin.forgefml.network.packet.s2c.play;

import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.class_687;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
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

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void readFrom(DataInputStream par1DataInputStream) {
        try {
            short var2 = par1DataInputStream.readShort();
            this.field_2536 = par1DataInputStream.readInt();
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
            } catch (DataFormatException var13) {
                throw new IOException("Bad compressed data format");
            } finally {
                var4.end();
            }

            int var5 = 0;

            for (int var6 = 0; var6 < var2; ++var6) {
                this.field_2532[var6] = par1DataInputStream.readInt();
                this.field_2533[var6] = par1DataInputStream.readInt();
                this.field_2530[var6] = par1DataInputStream.readShort();
                this.field_2531[var6] = par1DataInputStream.readShort();
                int var7 = 0;

                int var8;
                for (var8 = 0; var8 < 16; ++var8) {
                    var7 += this.field_2530[var6] >> var8 & 1;
                }

                int msb = 0;

                for (int x = 0; x < 16; ++x) {
                    msb += this.field_2531[var6] >> x & 1;
                }

                var8 = 10240 * var7 + 2048 * msb + 256;
                this.field_2535[var6] = new byte[var8];
                System.arraycopy(var3, var5, this.field_2535[var6], 0, var8);
                var5 += var8;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
