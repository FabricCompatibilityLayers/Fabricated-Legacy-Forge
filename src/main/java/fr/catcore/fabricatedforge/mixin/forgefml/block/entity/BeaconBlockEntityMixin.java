package fr.catcore.fabricatedforge.mixin.forgefml.block.entity;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin extends BlockEntity implements Inventory {

    @Shadow private boolean hasValidBase;

    @Shadow private int levels;

    /**
     * @author forge
     * @reason yes
     */
    @Overwrite
    private void method_4203() {
        if (!this.world.isAboveHighestBlock(this.x, this.y + 1, this.z)) {
            this.hasValidBase = false;
            this.levels = 0;
        } else {
            this.hasValidBase = true;
            this.levels = 0;

            for(int var1 = 1; var1 <= 4; this.levels = var1++) {
                int var2 = this.y - var1;
                if (var2 < 1) {
                    break;
                }

                boolean var3 = true;

                for(int var4 = this.x - var1; var4 <= this.x + var1 && var3; ++var4) {
                    for(int var5 = this.z - var1; var5 <= this.z + var1; ++var5) {
                        int var6 = this.world.getBlock(var4, var2, var5);
                        Block block = Block.BLOCKS[var6];
                        if (block == null || !((IBlock)block).isBeaconBase(this.world, var4, var2, var5, this.x, this.y, this.z)) {
                            var3 = false;
                            break;
                        }
                    }
                }

                if (!var3) {
                    break;
                }
            }

            if (this.levels == 0) {
                this.hasValidBase = false;
            }
        }
    }
}
