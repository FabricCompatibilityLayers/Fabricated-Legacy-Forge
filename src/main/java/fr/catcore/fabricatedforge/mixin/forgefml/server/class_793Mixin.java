package fr.catcore.fabricatedforge.mixin.forgefml.server;

import fr.catcore.cursedmixinextensions.annotations.Public;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkBlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkUpdateS2CPacket;
import net.minecraft.server.PlayerWorldManager;
import net.minecraft.server.class_793;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.ForgeDummyContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkWatchEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;
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

    @Shadow @Final public List<ServerPlayerEntity> field_2796;

    @Public
    private static int clumpingThreshold;

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    public void method_2124(ServerPlayerEntity par1EntityPlayerMP) {
        if (this.field_2796.contains(par1EntityPlayerMP)) {
            par1EntityPlayerMP.field_2823
                    .sendPacket(new ChunkUpdateS2CPacket(this.field_2795.getWorld().getChunk(this.field_2797.x, this.field_2797.z), true, 0));
            this.field_2796.remove(par1EntityPlayerMP);
            par1EntityPlayerMP.loadedChunks.remove(this.field_2797);
            MinecraftForge.EVENT_BUS.post(new ChunkWatchEvent.UnWatch(this.field_2797, par1EntityPlayerMP));
            if (this.field_2796.isEmpty()) {
                long var2 = (long)this.field_2797.x + 2147483647L | (long)this.field_2797.z + 2147483647L << 32;
                ((PlayerWorldManagerAccessor) this.field_2795).getPlayerInstancesById().remove(var2);
                if (this.field_2799 > 0) {
                    ((PlayerWorldManagerAccessor) this.field_2795).getField_2792().remove(this);
                }

                this.field_2795.getWorld().chunkCache.scheduleUnload(this.field_2797.x, this.field_2797.z);
            }
        }
    }

    /**
     * @author forge
     * @reason idk
     */
    @Overwrite
    public void method_2118(int par1, int par2, int par3) {
        if (this.field_2799 == 0) {
            ((PlayerWorldManagerAccessor) this.field_2795).getField_2792().add((class_793)(Object) this);
        }

        this.field_2800 |= 1 << (par2 >> 4);
        short var4 = (short)(par1 << 12 | par3 << 8 | par2);

        for(int var5 = 0; var5 < this.field_2799; ++var5) {
            if (this.field_2798[var5] == var4) {
                return;
            }
        }

        if (this.field_2799 == this.field_2798.length) {
            this.field_2798 = Arrays.copyOf(this.field_2798, this.field_2798.length << 1);
        }

        this.field_2798[this.field_2799++] = var4;
    }

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
            } else {
                if (this.field_2799 >= ForgeDummyContainer.clumpingThreshold) {
                    int var1 = this.field_2797.x * 16;
                    int var2 = this.field_2797.z * 16;
                    this.method_2120(
                            new ChunkUpdateS2CPacket(
                                    this.field_2795.getWorld().getChunk(this.field_2797.x, this.field_2797.z), false, this.field_2800
                            )
                    );
                } else {
                    this.method_2120(
                            new ChunkBlockUpdateS2CPacket(
                                    this.field_2797.x, this.field_2797.z, this.field_2798, this.field_2799, this.field_2795.getWorld()
                            )
                    );
                }

                for(int var1 = 0; var1 < this.field_2799; ++var1) {
                    int var2 = this.field_2797.x * 16 + (this.field_2798[var1] >> 12 & 15);
                    int var3 = this.field_2798[var1] & 255;
                    int var4 = this.field_2797.z * 16 + (this.field_2798[var1] >> 8 & 15);
                    if (this.field_2795.getWorld().hasBlockEntity(var2, var3, var4)) {
                        this.method_2119(this.field_2795.getWorld().getBlockEntity(var2, var3, var4));
                    }
                }
            }

            this.field_2799 = 0;
            this.field_2800 = 0;
        }
    }
}
