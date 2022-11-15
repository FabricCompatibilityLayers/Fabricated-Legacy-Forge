package fr.catcore.fabricatedforge.mixin.forgefml.server;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkBlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkUpdateS2CPacket;
import net.minecraft.server.PlayerWorldManager;
import net.minecraft.server.class_793;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(class_793.class)
public abstract class class_793Mixin {
    @Shadow
    private int field_2799;

    @Shadow @Final
    private ChunkPos field_2797;

    @Shadow private short[] field_2798;

    @Shadow @Final
    PlayerWorldManager field_2795;

    @Shadow private int field_2800;

    @Shadow public abstract void method_2120(Packet packet);

    @Shadow protected abstract void method_2119(BlockEntity blockEntity);

    /**
     * @author Minecraft Forge
     * @reason meow
     */
    @Overwrite
    public void method_2117() {
        if (this.field_2799 != 0) {
            if (this.field_2799 == 1) {
                int var1 = this.field_2797.x * 16 + (this.field_2798[0] >> 12 & 15);
                int var2 = this.field_2798[0] & 255;
                int var3 = this.field_2797.z * 16 + (this.field_2798[0] >> 8 & 15);
                this.method_2120(new BlockUpdateS2CPacket(var1, var2, var3, this.field_2795.getWorld()));
                if (this.field_2795.getWorld().hasBlockEntity(var1, var2, var3)) {
                    this.method_2119(this.field_2795.getWorld().getBlockEntity(var1, var2, var3));
                }
            } else if (this.field_2799 != 64) {
                this.method_2120(
                        new ChunkBlockUpdateS2CPacket(
                                this.field_2797.x, this.field_2797.z, this.field_2798, this.field_2799, this.field_2795.getWorld()
                        )
                );

                for(int var1 = 0; var1 < this.field_2799; ++var1) {
                    int var2 = this.field_2797.x * 16 + (this.field_2798[var1] >> 12 & 15);
                    int var3 = this.field_2798[var1] & 255;
                    int var4 = this.field_2797.z * 16 + (this.field_2798[var1] >> 8 & 15);
                    if (this.field_2795.getWorld().hasBlockEntity(var2, var3, var4)) {
                        this.method_2119(this.field_2795.getWorld().getBlockEntity(var2, var3, var4));
                    }
                }
            } else {
                int var1 = this.field_2797.x * 16;
                int var2 = this.field_2797.z * 16;
                this.method_2120(
                        new ChunkUpdateS2CPacket(
                                this.field_2795.getWorld().getChunk(this.field_2797.x, this.field_2797.z), false, this.field_2800
                        )
                );

                for(int var3 = 0; var3 < 16; ++var3) {
                    if ((this.field_2800 & 1 << var3) != 0) {
                        int var4 = var3 << 4;

                        for(BlockEntity var7 : (List<BlockEntity>)this.field_2795.getWorld().method_2134(var1, var4, var2, var1 + 15, var4 + 16, var2 + 15)) {
                            this.method_2119(var7);
                        }
                    }
                }
            }

            this.field_2799 = 0;
            this.field_2800 = 0;
        }
    }
}
