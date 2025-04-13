package fr.catcore.fabricatedforge.mixin.forgefml.structure;

import net.minecraft.block.Block;
import net.minecraft.structure.class_5;
import net.minecraft.structure.class_8;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(class_5.class)
public class class_5Mixin extends class_8 {

    @Shadow private boolean[] field_5;

    protected class_5Mixin(Random random, int i, int j, int k, int l, int m, int n) {
        super(random, i, j, k, l, m, n);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean generate(World par1World, Random par2Random, BlockBox par3StructureBoundingBox) {
        this.method_62(par1World, par3StructureBoundingBox, 0, -4, 0, this.field_14 - 1, 0, this.field_16 - 1, Block.SANDSTONE.id, Block.SANDSTONE.id, false);

        int var4;
        for(var4 = 1; var4 <= 9; ++var4) {
            this.method_62(par1World, par3StructureBoundingBox, var4, var4, var4, this.field_14 - 1 - var4, var4, this.field_16 - 1 - var4, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
            this.method_62(par1World, par3StructureBoundingBox, var4 + 1, var4, var4 + 1, this.field_14 - 2 - var4, var4, this.field_16 - 2 - var4, 0, 0, false);
        }

        int var5;
        for(var4 = 0; var4 < this.field_14; ++var4) {
            for(var5 = 0; var5 < this.field_16; ++var5) {
                this.method_72(par1World, Block.SANDSTONE.id, 0, var4, -5, var5, par3StructureBoundingBox);
            }
        }

        var4 = this.method_75(Block.SANDSTONE_STAIRS.id, 3);
        var5 = this.method_75(Block.SANDSTONE_STAIRS.id, 2);
        int var6 = this.method_75(Block.SANDSTONE_STAIRS.id, 0);
        int var7 = this.method_75(Block.SANDSTONE_STAIRS.id, 1);
        byte var8 = 1;
        byte var9 = 11;
        this.method_62(par1World, par3StructureBoundingBox, 0, 0, 0, 4, 9, 4, Block.SANDSTONE.id, 0, false);
        this.method_62(par1World, par3StructureBoundingBox, 1, 10, 1, 3, 10, 3, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, var4, 2, 10, 0, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, var5, 2, 10, 4, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, var6, 0, 10, 2, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, var7, 4, 10, 2, par3StructureBoundingBox);
        this.method_62(par1World, par3StructureBoundingBox, this.field_14 - 5, 0, 0, this.field_14 - 1, 9, 4, Block.SANDSTONE.id, 0, false);
        this.method_62(par1World, par3StructureBoundingBox, this.field_14 - 4, 10, 1, this.field_14 - 2, 10, 3, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, var4, this.field_14 - 3, 10, 0, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, var5, this.field_14 - 3, 10, 4, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, var6, this.field_14 - 5, 10, 2, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, var7, this.field_14 - 1, 10, 2, par3StructureBoundingBox);
        this.method_62(par1World, par3StructureBoundingBox, 8, 0, 0, 12, 4, 4, Block.SANDSTONE.id, 0, false);
        this.method_62(par1World, par3StructureBoundingBox, 9, 1, 0, 11, 3, 4, 0, 0, false);
        this.method_56(par1World, Block.SANDSTONE.id, 2, 9, 1, 1, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE.id, 2, 9, 2, 1, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE.id, 2, 9, 3, 1, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE.id, 2, 10, 3, 1, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE.id, 2, 11, 3, 1, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE.id, 2, 11, 2, 1, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE.id, 2, 11, 1, 1, par3StructureBoundingBox);
        this.method_62(par1World, par3StructureBoundingBox, 4, 1, 1, 8, 3, 3, Block.SANDSTONE.id, 0, false);
        this.method_62(par1World, par3StructureBoundingBox, 4, 1, 2, 8, 2, 2, 0, 0, false);
        this.method_62(par1World, par3StructureBoundingBox, 12, 1, 1, 16, 3, 3, Block.SANDSTONE.id, 0, false);
        this.method_62(par1World, par3StructureBoundingBox, 12, 1, 2, 16, 2, 2, 0, 0, false);
        this.method_62(par1World, par3StructureBoundingBox, 5, 4, 5, this.field_14 - 6, 4, this.field_16 - 6, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 9, 4, 9, 11, 4, 11, 0, 0, false);
        this.method_61(par1World, par3StructureBoundingBox, 8, 1, 8, 8, 3, 8, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
        this.method_61(par1World, par3StructureBoundingBox, 12, 1, 8, 12, 3, 8, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
        this.method_61(par1World, par3StructureBoundingBox, 8, 1, 12, 8, 3, 12, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
        this.method_61(par1World, par3StructureBoundingBox, 12, 1, 12, 12, 3, 12, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
        this.method_62(par1World, par3StructureBoundingBox, 1, 1, 5, 4, 4, 11, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
        this.method_62(par1World, par3StructureBoundingBox, this.field_14 - 5, 1, 5, this.field_14 - 2, 4, 11, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 6, 7, 9, 6, 7, 11, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
        this.method_62(par1World, par3StructureBoundingBox, this.field_14 - 7, 7, 9, this.field_14 - 7, 7, 11, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
        this.method_61(par1World, par3StructureBoundingBox, 5, 5, 9, 5, 7, 11, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
        this.method_61(par1World, par3StructureBoundingBox, this.field_14 - 6, 5, 9, this.field_14 - 6, 7, 11, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
        this.method_56(par1World, 0, 0, 5, 5, 10, par3StructureBoundingBox);
        this.method_56(par1World, 0, 0, 5, 6, 10, par3StructureBoundingBox);
        this.method_56(par1World, 0, 0, 6, 6, 10, par3StructureBoundingBox);
        this.method_56(par1World, 0, 0, this.field_14 - 6, 5, 10, par3StructureBoundingBox);
        this.method_56(par1World, 0, 0, this.field_14 - 6, 6, 10, par3StructureBoundingBox);
        this.method_56(par1World, 0, 0, this.field_14 - 7, 6, 10, par3StructureBoundingBox);
        this.method_62(par1World, par3StructureBoundingBox, 2, 4, 4, 2, 6, 4, 0, 0, false);
        this.method_62(par1World, par3StructureBoundingBox, this.field_14 - 3, 4, 4, this.field_14 - 3, 6, 4, 0, 0, false);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, var4, 2, 4, 5, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, var4, 2, 3, 4, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, var4, this.field_14 - 3, 4, 5, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, var4, this.field_14 - 3, 3, 4, par3StructureBoundingBox);
        this.method_62(par1World, par3StructureBoundingBox, 1, 1, 3, 2, 2, 3, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
        this.method_62(par1World, par3StructureBoundingBox, this.field_14 - 3, 1, 3, this.field_14 - 2, 2, 3, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, 0, 1, 1, 2, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, 0, this.field_14 - 2, 1, 2, par3StructureBoundingBox);
        this.method_56(par1World, Block.STONE_SLAB.id, 1, 1, 2, 2, par3StructureBoundingBox);
        this.method_56(par1World, Block.STONE_SLAB.id, 1, this.field_14 - 2, 2, 2, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, var7, 2, 1, 2, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE_STAIRS.id, var6, this.field_14 - 3, 1, 2, par3StructureBoundingBox);
        this.method_62(par1World, par3StructureBoundingBox, 4, 3, 5, 4, 3, 18, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
        this.method_62(par1World, par3StructureBoundingBox, this.field_14 - 5, 3, 5, this.field_14 - 5, 3, 17, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 3, 1, 5, 4, 2, 16, 0, 0, false);
        this.method_62(par1World, par3StructureBoundingBox, this.field_14 - 6, 1, 5, this.field_14 - 5, 2, 16, 0, 0, false);

        int var10;
        for(var10 = 5; var10 <= 17; var10 += 2) {
            this.method_56(par1World, Block.SANDSTONE.id, 2, 4, 1, var10, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 1, 4, 2, var10, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, this.field_14 - 5, 1, var10, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 1, this.field_14 - 5, 2, var10, par3StructureBoundingBox);
        }

        this.method_56(par1World, Block.WOOL.id, var8, 10, 0, 7, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOOL.id, var8, 10, 0, 8, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOOL.id, var8, 9, 0, 9, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOOL.id, var8, 11, 0, 9, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOOL.id, var8, 8, 0, 10, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOOL.id, var8, 12, 0, 10, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOOL.id, var8, 7, 0, 10, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOOL.id, var8, 13, 0, 10, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOOL.id, var8, 9, 0, 11, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOOL.id, var8, 11, 0, 11, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOOL.id, var8, 10, 0, 12, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOOL.id, var8, 10, 0, 13, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOOL.id, var9, 10, 0, 10, par3StructureBoundingBox);

        for(var10 = 0; var10 <= this.field_14 - 1; var10 += this.field_14 - 1) {
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10, 2, 1, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10, 2, 2, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10, 2, 3, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10, 3, 1, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10, 3, 2, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10, 3, 3, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10, 4, 1, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 1, var10, 4, 2, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10, 4, 3, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10, 5, 1, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10, 5, 2, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10, 5, 3, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10, 6, 1, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 1, var10, 6, 2, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10, 6, 3, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10, 7, 1, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10, 7, 2, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10, 7, 3, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10, 8, 1, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10, 8, 2, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10, 8, 3, par3StructureBoundingBox);
        }

        for(var10 = 2; var10 <= this.field_14 - 3; var10 += this.field_14 - 3 - 2) {
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10 - 1, 2, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10, 2, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10 + 1, 2, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10 - 1, 3, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10, 3, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10 + 1, 3, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10 - 1, 4, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 1, var10, 4, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10 + 1, 4, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10 - 1, 5, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10, 5, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10 + 1, 5, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10 - 1, 6, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 1, var10, 6, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10 + 1, 6, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10 - 1, 7, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10, 7, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.WOOL.id, var8, var10 + 1, 7, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10 - 1, 8, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10, 8, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.SANDSTONE.id, 2, var10 + 1, 8, 0, par3StructureBoundingBox);
        }

        this.method_61(par1World, par3StructureBoundingBox, 8, 4, 0, 12, 6, 0, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
        this.method_56(par1World, 0, 0, 8, 6, 0, par3StructureBoundingBox);
        this.method_56(par1World, 0, 0, 12, 6, 0, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOOL.id, var8, 9, 5, 0, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE.id, 1, 10, 5, 0, par3StructureBoundingBox);
        this.method_56(par1World, Block.WOOL.id, var8, 11, 5, 0, par3StructureBoundingBox);
        this.method_61(par1World, par3StructureBoundingBox, 8, -14, 8, 12, -11, 12, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
        this.method_61(par1World, par3StructureBoundingBox, 8, -10, 8, 12, -10, 12, Block.SANDSTONE.id, 1, Block.SANDSTONE.id, 1, false);
        this.method_61(par1World, par3StructureBoundingBox, 8, -9, 8, 12, -9, 12, Block.SANDSTONE.id, 2, Block.SANDSTONE.id, 2, false);
        this.method_62(par1World, par3StructureBoundingBox, 8, -8, 8, 12, -1, 12, Block.SANDSTONE.id, Block.SANDSTONE.id, false);
        this.method_62(par1World, par3StructureBoundingBox, 9, -11, 9, 11, -1, 11, 0, 0, false);
        this.method_56(par1World, Block.STONE_PRESSURE_PLATE.id, 0, 10, -11, 10, par3StructureBoundingBox);
        this.method_62(par1World, par3StructureBoundingBox, 9, -13, 9, 11, -13, 11, Block.TNT.id, 0, false);
        this.method_56(par1World, 0, 0, 8, -11, 10, par3StructureBoundingBox);
        this.method_56(par1World, 0, 0, 8, -10, 10, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE.id, 1, 7, -10, 10, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE.id, 2, 7, -11, 10, par3StructureBoundingBox);
        this.method_56(par1World, 0, 0, 12, -11, 10, par3StructureBoundingBox);
        this.method_56(par1World, 0, 0, 12, -10, 10, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE.id, 1, 13, -10, 10, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE.id, 2, 13, -11, 10, par3StructureBoundingBox);
        this.method_56(par1World, 0, 0, 10, -11, 8, par3StructureBoundingBox);
        this.method_56(par1World, 0, 0, 10, -10, 8, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE.id, 1, 10, -10, 7, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE.id, 2, 10, -11, 7, par3StructureBoundingBox);
        this.method_56(par1World, 0, 0, 10, -11, 12, par3StructureBoundingBox);
        this.method_56(par1World, 0, 0, 10, -10, 12, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE.id, 1, 10, -10, 13, par3StructureBoundingBox);
        this.method_56(par1World, Block.SANDSTONE.id, 2, 10, -11, 13, par3StructureBoundingBox);

        for(var10 = 0; var10 < 4; ++var10) {
            if (!this.field_5[var10]) {
                int var11 = Axis.OFFSET_X[var10] * 2;
                int var12 = Axis.OFFSET_Z[var10] * 2;
                this.field_5[var10] = this.method_69(par1World, par3StructureBoundingBox, par2Random, 10 + var11, -11, 10 + var12, ChestGenHooks.getItems("pyramidDesertyChest"), ChestGenHooks.getCount("pyramidDesertyChest", par2Random));
            }
        }

        return true;
    }
}
