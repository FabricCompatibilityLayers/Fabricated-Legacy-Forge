package fr.catcore.fabricatedforge.mixin.forgefml.structure;

import net.minecraft.block.Block;
import net.minecraft.structure.class_49;
import net.minecraft.structure.class_50;
import net.minecraft.structure.class_53;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(class_49.class)
public abstract class class_49Mixin extends class_53 {
    @Shadow private int field_90;

    @Shadow private boolean field_91;

    protected class_49Mixin(class_50 arg, int i) {
        super(arg, i);
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public boolean generate(World par1World, Random par2Random, BlockBox par3StructureBoundingBox) {
        if (this.field_90 < 0) {
            this.field_90 = this.method_113(par1World, par3StructureBoundingBox);
            if (this.field_90 < 0) {
                return true;
            }

            this.boundingBox.move(0, this.field_90 - this.boundingBox.maxY + 6 - 1, 0);
        }

        this.method_62(par1World, par3StructureBoundingBox, 0, 1, 0, 9, 4, 6, 0, 0, false);
        this.method_62(par1World, par3StructureBoundingBox, 0, 0, 0, 9, 0, 6, Block.STONE_BRICKS.id, Block.STONE_BRICKS.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 0, 4, 0, 9, 4, 6, Block.STONE_BRICKS.id, Block.STONE_BRICKS.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 0, 5, 0, 9, 5, 6, Block.STONE_SLAB.id, Block.STONE_SLAB.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 1, 5, 1, 8, 5, 5, 0, 0, false);
        this.method_62(par1World, par3StructureBoundingBox, 1, 1, 0, 2, 3, 0, Block.PLANKS.id, Block.PLANKS.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 0, 1, 0, 0, 4, 0, Block.LOG.id, Block.LOG.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 3, 1, 0, 3, 4, 0, Block.LOG.id, Block.LOG.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 0, 1, 6, 0, 4, 6, Block.LOG.id, Block.LOG.id, false);
        this.method_56(par1World, Block.PLANKS.id, 0, 3, 3, 1, par3StructureBoundingBox);
        this.method_62(par1World, par3StructureBoundingBox, 3, 1, 2, 3, 3, 2, Block.PLANKS.id, Block.PLANKS.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 4, 1, 3, 5, 3, 3, Block.PLANKS.id, Block.PLANKS.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 0, 1, 1, 0, 3, 5, Block.PLANKS.id, Block.PLANKS.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 1, 1, 6, 5, 3, 6, Block.PLANKS.id, Block.PLANKS.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 5, 1, 0, 5, 3, 0, Block.WOODEN_FENCE.id, Block.WOODEN_FENCE.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 9, 1, 0, 9, 3, 0, Block.WOODEN_FENCE.id, Block.WOODEN_FENCE.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 6, 1, 4, 9, 4, 6, Block.STONE_BRICKS.id, Block.STONE_BRICKS.id, false);
        this.method_56(par1World, Block.field_336.id, 0, 7, 1, 5, par3StructureBoundingBox);
        this.method_56(par1World, Block.field_336.id, 0, 8, 1, 5, par3StructureBoundingBox);
        this.method_56(par1World, Block.IRON_BARS.id, 0, 9, 2, 5, par3StructureBoundingBox);
        this.method_56(par1World, Block.IRON_BARS.id, 0, 9, 2, 4, par3StructureBoundingBox);
        this.method_62(par1World, par3StructureBoundingBox, 7, 2, 4, 8, 2, 5, 0, 0, false);
        this.method_56(par1World, Block.STONE_BRICKS.id, 0, 6, 1, 3, par3StructureBoundingBox);
        this.method_56(par1World, Block.FURNACE.id, 0, 6, 2, 3, par3StructureBoundingBox);
        this.method_56(par1World, Block.FURNACE.id, 0, 6, 3, 3, par3StructureBoundingBox);
        this.method_56(par1World, Block.DOUBLE_STONE_SLAB.id, 0, 8, 1, 1, par3StructureBoundingBox);
        this.method_56(par1World, Block.GLASS_PANE.id, 0, 0, 2, 2, par3StructureBoundingBox);
        this.method_56(par1World, Block.GLASS_PANE.id, 0, 0, 2, 4, par3StructureBoundingBox);
        this.method_56(par1World, Block.GLASS_PANE.id, 0, 2, 2, 6, par3StructureBoundingBox);
        this.method_56(par1World, Block.GLASS_PANE.id, 0, 4, 2, 6, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOODEN_FENCE.id, 0, 2, 1, 4, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOODEN_PRESSURE_PLATE.id, 0, 2, 2, 4, par3StructureBoundingBox);
        this.method_56(par1World, Block.PLANKS.id, 0, 1, 1, 5, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOODEN_STAIRS.id, this.method_75(Block.WOODEN_STAIRS.id, 3), 2, 1, 5, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOODEN_STAIRS.id, this.method_75(Block.WOODEN_STAIRS.id, 1), 1, 1, 4, par3StructureBoundingBox);
        int var4;
        int var5;
        if (!this.field_91) {
            var4 = this.applyYTransform(1);
            var5 = this.applyXTransform(5, 5);
            int var6 = this.applyZTransform(5, 5);
            if (par3StructureBoundingBox.intersects(var5, var4, var6)) {
                this.field_91 = true;
                this.method_69(par1World, par3StructureBoundingBox, par2Random, 5, 1, 5, ChestGenHooks.getItems("villageBlacksmith"), ChestGenHooks.getCount("villageBlacksmith", par2Random));
            }
        }

        for(var4 = 6; var4 <= 8; ++var4) {
            if (this.method_57(par1World, var4, 0, -1, par3StructureBoundingBox) == 0 && this.method_57(par1World, var4, -1, -1, par3StructureBoundingBox) != 0) {
                this.method_56(par1World, Block.STONE_STAIRS.id, this.method_75(Block.STONE_STAIRS.id, 3), var4, 0, -1, par3StructureBoundingBox);
            }
        }

        for(var4 = 0; var4 < 7; ++var4) {
            for(var5 = 0; var5 < 10; ++var5) {
                this.clearBlocksUpwards(par1World, var5, 6, var4, par3StructureBoundingBox);
                this.method_72(par1World, Block.STONE_BRICKS.id, 0, var5, -1, var4, par3StructureBoundingBox);
            }
        }

        this.method_109(par1World, par3StructureBoundingBox, 7, 1, 1, 1);
        return true;
    }
}
