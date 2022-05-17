package fr.catcore.fabricatedforge.mixin.forgefml.world.chunk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkCache.class)
public abstract class ChunkCacheMixin implements WorldView {

    @Shadow private int minX;

    @Shadow private int minZ;

    @Shadow private Chunk[][] chunks;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public BlockEntity method_3781(int par1, int par2, int par3) {
        int var4 = (par1 >> 4) - this.minX;
        int var5 = (par3 >> 4) - this.minZ;
        if (var4 >= 0 && var4 < this.chunks.length && var5 >= 0 && var5 < this.chunks[var4].length) {
            Chunk var6 = this.chunks[var4][var5];
            return var6 == null ? null : var6.method_3912(par1 & 15, par2, par3 & 15);
        } else {
            return null;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public int getBlockData(int par1, int par2, int par3) {
        if (par2 < 0) {
            return 0;
        } else if (par2 >= 256) {
            return 0;
        } else {
            int var4 = (par1 >> 4) - this.minX;
            int var5 = (par3 >> 4) - this.minZ;
            if (var4 >= 0 && var4 < this.chunks.length && var5 >= 0 && var5 < this.chunks[var4].length) {
                Chunk var6 = this.chunks[var4][var5];
                return var6 == null ? 0 : var6.getBlockData(par1 & 15, par2, par3 & 15);
            } else {
                return 0;
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public int method_3771() {
        return 256;
    }
}
