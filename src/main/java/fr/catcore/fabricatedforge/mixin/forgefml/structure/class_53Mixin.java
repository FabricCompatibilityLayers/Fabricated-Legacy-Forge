package fr.catcore.fabricatedforge.mixin.forgefml.structure;

import net.minecraft.block.Block;
import net.minecraft.structure.class_50;
import net.minecraft.structure.class_53;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.terraingen.BiomeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(class_53.class)
public class class_53Mixin {
    @Shadow protected class_50 field_102;

    /**
     * @author forge
     * @reason hooks
     */
    @Overwrite
    protected int method_114(int par1, int par2) {
        BiomeEvent.GetVillageBlockID event = new BiomeEvent.GetVillageBlockID(this.field_102.getBiome(), par1, par2);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        if (event.getResult() == Event.Result.DENY) {
            return event.replacement;
        } else {
            if (this.field_102.field_93) {
                if (par1 == Block.LOG.id) {
                    return Block.SANDSTONE.id;
                }

                if (par1 == Block.STONE_BRICKS.id) {
                    return Block.SANDSTONE.id;
                }

                if (par1 == Block.PLANKS.id) {
                    return Block.SANDSTONE.id;
                }

                if (par1 == Block.WOODEN_STAIRS.id) {
                    return Block.SANDSTONE_STAIRS.id;
                }

                if (par1 == Block.STONE_STAIRS.id) {
                    return Block.SANDSTONE_STAIRS.id;
                }

                if (par1 == Block.GRAVEL_BLOCK.id) {
                    return Block.SANDSTONE.id;
                }
            }

            return par1;
        }
    }

    /**
     * @author forge
     * @reason hooks
     */
    @Overwrite
    protected int method_115(int par1, int par2) {
        BiomeEvent.GetVillageBlockMeta event = new BiomeEvent.GetVillageBlockMeta(this.field_102.getBiome(), par1, par2);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        if (event.getResult() == Event.Result.DENY) {
            return event.replacement;
        } else {
            if (this.field_102.field_93) {
                if (par1 == Block.LOG.id) {
                    return 0;
                }

                if (par1 == Block.STONE_BRICKS.id) {
                    return 0;
                }

                if (par1 == Block.PLANKS.id) {
                    return 2;
                }
            }

            return par2;
        }
    }
}
