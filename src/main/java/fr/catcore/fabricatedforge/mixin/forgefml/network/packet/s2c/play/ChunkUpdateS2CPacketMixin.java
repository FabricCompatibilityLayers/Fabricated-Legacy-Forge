package fr.catcore.fabricatedforge.mixin.forgefml.network.packet.s2c.play;

import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.ChunkUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
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

    /**
     * @author Minecraft Forge
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

            int var3;
            for (var3 = 0; var3 < 16; ++var3) {
                var2 += this.field_2481 >> var3 & 1;
            }

            var3 = 12288 * var2;
            int msb = 0;

            for (int x = 0; x < 16; ++x) {
                msb += this.field_2482 >> x & 1;
            }

            var3 += 2048 * msb;
            if (this.field_2483) {
                var3 += 256;
            }

            this.field_2485 = new byte[var3];
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
            e.printStackTrace();
        }
    }
}
