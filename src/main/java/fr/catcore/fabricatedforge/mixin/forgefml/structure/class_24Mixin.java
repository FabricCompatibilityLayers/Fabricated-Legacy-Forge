package fr.catcore.fabricatedforge.mixin.forgefml.structure;

import net.minecraft.block.Block;
import net.minecraft.structure.StrongholdPieces;
import net.minecraft.structure.class_24;
import net.minecraft.structure.class_31;
import net.minecraft.util.class_32;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(class_24.class)
public abstract class class_24Mixin extends class_31 {
    @Shadow @Final protected class_32 field_46;

    @Shadow @Final protected int field_47;

    protected class_24Mixin(int i) {
        super(i);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean generate(World par1World, Random par2Random, BlockBox par3StructureBoundingBox) {
        if (this.isTouchingLiquid(par1World, par3StructureBoundingBox)) {
            return false;
        } else {
            this.method_64(par1World, par3StructureBoundingBox, 0, 0, 0, 10, 6, 10, true, par2Random, StrongholdPiecesAccessor.getField_25());
            this.method_41(par1World, par2Random, par3StructureBoundingBox, this.field_46, 4, 1, 0);
            this.method_62(par1World, par3StructureBoundingBox, 4, 1, 10, 6, 3, 10, 0, 0, false);
            this.method_62(par1World, par3StructureBoundingBox, 0, 1, 4, 0, 3, 6, 0, 0, false);
            this.method_62(par1World, par3StructureBoundingBox, 10, 1, 4, 10, 3, 6, 0, 0, false);
            switch(this.field_47) {
                case 0:
                    this.method_56(par1World, Block.STONE_BRICK.id, 0, 5, 1, 5, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_BRICK.id, 0, 5, 2, 5, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_BRICK.id, 0, 5, 3, 5, par3StructureBoundingBox);
                    this.method_56(par1World, Block.TORCH.id, 0, 4, 3, 5, par3StructureBoundingBox);
                    this.method_56(par1World, Block.TORCH.id, 0, 6, 3, 5, par3StructureBoundingBox);
                    this.method_56(par1World, Block.TORCH.id, 0, 5, 3, 4, par3StructureBoundingBox);
                    this.method_56(par1World, Block.TORCH.id, 0, 5, 3, 6, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_SLAB.id, 0, 4, 1, 4, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_SLAB.id, 0, 4, 1, 5, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_SLAB.id, 0, 4, 1, 6, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_SLAB.id, 0, 6, 1, 4, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_SLAB.id, 0, 6, 1, 5, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_SLAB.id, 0, 6, 1, 6, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_SLAB.id, 0, 5, 1, 4, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_SLAB.id, 0, 5, 1, 6, par3StructureBoundingBox);
                    break;
                case 1:
                    for(int var4 = 0; var4 < 5; ++var4) {
                        this.method_56(par1World, Block.STONE_BRICK.id, 0, 3, 1, 3 + var4, par3StructureBoundingBox);
                        this.method_56(par1World, Block.STONE_BRICK.id, 0, 7, 1, 3 + var4, par3StructureBoundingBox);
                        this.method_56(par1World, Block.STONE_BRICK.id, 0, 3 + var4, 1, 3, par3StructureBoundingBox);
                        this.method_56(par1World, Block.STONE_BRICK.id, 0, 3 + var4, 1, 7, par3StructureBoundingBox);
                    }

                    this.method_56(par1World, Block.STONE_BRICK.id, 0, 5, 1, 5, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_BRICK.id, 0, 5, 2, 5, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_BRICK.id, 0, 5, 3, 5, par3StructureBoundingBox);
                    this.method_56(par1World, Block.field_334.id, 0, 5, 4, 5, par3StructureBoundingBox);
                    break;
                case 2:
                    for(int var4 = 1; var4 <= 9; ++var4) {
                        this.method_56(par1World, Block.STONE_BRICKS.id, 0, 1, 3, var4, par3StructureBoundingBox);
                        this.method_56(par1World, Block.STONE_BRICKS.id, 0, 9, 3, var4, par3StructureBoundingBox);
                    }

                    for(int var5 = 1; var5 <= 9; ++var5) {
                        this.method_56(par1World, Block.STONE_BRICKS.id, 0, var5, 3, 1, par3StructureBoundingBox);
                        this.method_56(par1World, Block.STONE_BRICKS.id, 0, var5, 3, 9, par3StructureBoundingBox);
                    }

                    this.method_56(par1World, Block.STONE_BRICKS.id, 0, 5, 1, 4, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_BRICKS.id, 0, 5, 1, 6, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_BRICKS.id, 0, 5, 3, 4, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_BRICKS.id, 0, 5, 3, 6, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_BRICKS.id, 0, 4, 1, 5, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_BRICKS.id, 0, 6, 1, 5, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_BRICKS.id, 0, 4, 3, 5, par3StructureBoundingBox);
                    this.method_56(par1World, Block.STONE_BRICKS.id, 0, 6, 3, 5, par3StructureBoundingBox);

                    for(int var6 = 1; var6 <= 3; ++var6) {
                        this.method_56(par1World, Block.STONE_BRICKS.id, 0, 4, var6, 4, par3StructureBoundingBox);
                        this.method_56(par1World, Block.STONE_BRICKS.id, 0, 6, var6, 4, par3StructureBoundingBox);
                        this.method_56(par1World, Block.STONE_BRICKS.id, 0, 4, var6, 6, par3StructureBoundingBox);
                        this.method_56(par1World, Block.STONE_BRICKS.id, 0, 6, var6, 6, par3StructureBoundingBox);
                    }

                    this.method_56(par1World, Block.TORCH.id, 0, 5, 3, 5, par3StructureBoundingBox);

                    for(int var7 = 2; var7 <= 8; ++var7) {
                        this.method_56(par1World, Block.PLANKS.id, 0, 2, 3, var7, par3StructureBoundingBox);
                        this.method_56(par1World, Block.PLANKS.id, 0, 3, 3, var7, par3StructureBoundingBox);
                        if (var7 <= 3 || var7 >= 7) {
                            this.method_56(par1World, Block.PLANKS.id, 0, 4, 3, var7, par3StructureBoundingBox);
                            this.method_56(par1World, Block.PLANKS.id, 0, 5, 3, var7, par3StructureBoundingBox);
                            this.method_56(par1World, Block.PLANKS.id, 0, 6, 3, var7, par3StructureBoundingBox);
                        }

                        this.method_56(par1World, Block.PLANKS.id, 0, 7, 3, var7, par3StructureBoundingBox);
                        this.method_56(par1World, Block.PLANKS.id, 0, 8, 3, var7, par3StructureBoundingBox);
                    }

                    this.method_56(par1World, Block.LADDER_BLOCK.id, this.method_75(Block.LADDER_BLOCK.id, 4), 9, 1, 3, par3StructureBoundingBox);
                    this.method_56(par1World, Block.LADDER_BLOCK.id, this.method_75(Block.LADDER_BLOCK.id, 4), 9, 2, 3, par3StructureBoundingBox);
                    this.method_56(par1World, Block.LADDER_BLOCK.id, this.method_75(Block.LADDER_BLOCK.id, 4), 9, 3, 3, par3StructureBoundingBox);
                    this.method_69(
                            par1World,
                            par3StructureBoundingBox,
                            par2Random,
                            3,
                            4,
                            8,
                            ChestGenHooks.getItems("strongholdCrossing"),
                            ChestGenHooks.getCount("strongholdCrossing", par2Random)
                    );
            }

            return true;
        }
    }
}
