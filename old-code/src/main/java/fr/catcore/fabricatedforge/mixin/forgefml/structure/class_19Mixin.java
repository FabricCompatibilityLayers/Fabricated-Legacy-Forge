package fr.catcore.fabricatedforge.mixin.forgefml.structure;

import net.minecraft.block.Block;
import net.minecraft.structure.StrongholdPieces;
import net.minecraft.structure.class_19;
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

@Mixin(class_19.class)
public abstract class class_19Mixin extends class_31 {
    @Shadow @Final private boolean field_39;

    @Shadow @Final protected class_32 field_37;

    protected class_19Mixin(int i) {
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
            byte var4 = 11;
            if (!this.field_39) {
                var4 = 6;
            }

            this.method_64(par1World, par3StructureBoundingBox, 0, 0, 0, 13, var4 - 1, 14, true, par2Random, StrongholdPiecesAccessor.getField_25());
            this.method_41(par1World, par2Random, par3StructureBoundingBox, this.field_37, 4, 1, 0);
            this.method_66(par1World, par3StructureBoundingBox, par2Random, 0.07F, 2, 1, 1, 11, 4, 13, Block.COBWEB.id, Block.COBWEB.id, false);

            int var7;
            for(var7 = 1; var7 <= 13; ++var7) {
                if ((var7 - 1) % 4 == 0) {
                    this.method_62(par1World, par3StructureBoundingBox, 1, 1, var7, 1, 4, var7, Block.PLANKS.id, Block.PLANKS.id, false);
                    this.method_62(par1World, par3StructureBoundingBox, 12, 1, var7, 12, 4, var7, Block.PLANKS.id, Block.PLANKS.id, false);
                    this.method_56(par1World, Block.TORCH.id, 0, 2, 3, var7, par3StructureBoundingBox);
                    this.method_56(par1World, Block.TORCH.id, 0, 11, 3, var7, par3StructureBoundingBox);
                    if (this.field_39) {
                        this.method_62(par1World, par3StructureBoundingBox, 1, 6, var7, 1, 9, var7, Block.PLANKS.id, Block.PLANKS.id, false);
                        this.method_62(par1World, par3StructureBoundingBox, 12, 6, var7, 12, 9, var7, Block.PLANKS.id, Block.PLANKS.id, false);
                    }
                } else {
                    this.method_62(par1World, par3StructureBoundingBox, 1, 1, var7, 1, 4, var7, Block.BOOKSHELF.id, Block.BOOKSHELF.id, false);
                    this.method_62(par1World, par3StructureBoundingBox, 12, 1, var7, 12, 4, var7, Block.BOOKSHELF.id, Block.BOOKSHELF.id, false);
                    if (this.field_39) {
                        this.method_62(par1World, par3StructureBoundingBox, 1, 6, var7, 1, 9, var7, Block.BOOKSHELF.id, Block.BOOKSHELF.id, false);
                        this.method_62(par1World, par3StructureBoundingBox, 12, 6, var7, 12, 9, var7, Block.BOOKSHELF.id, Block.BOOKSHELF.id, false);
                    }
                }
            }

            for(var7 = 3; var7 < 12; var7 += 2) {
                this.method_62(par1World, par3StructureBoundingBox, 3, 1, var7, 4, 3, var7, Block.BOOKSHELF.id, Block.BOOKSHELF.id, false);
                this.method_62(par1World, par3StructureBoundingBox, 6, 1, var7, 7, 3, var7, Block.BOOKSHELF.id, Block.BOOKSHELF.id, false);
                this.method_62(par1World, par3StructureBoundingBox, 9, 1, var7, 10, 3, var7, Block.BOOKSHELF.id, Block.BOOKSHELF.id, false);
            }

            if (this.field_39) {
                this.method_62(par1World, par3StructureBoundingBox, 1, 5, 1, 3, 5, 13, Block.PLANKS.id, Block.PLANKS.id, false);
                this.method_62(par1World, par3StructureBoundingBox, 10, 5, 1, 12, 5, 13, Block.PLANKS.id, Block.PLANKS.id, false);
                this.method_62(par1World, par3StructureBoundingBox, 4, 5, 1, 9, 5, 2, Block.PLANKS.id, Block.PLANKS.id, false);
                this.method_62(par1World, par3StructureBoundingBox, 4, 5, 12, 9, 5, 13, Block.PLANKS.id, Block.PLANKS.id, false);
                this.method_56(par1World, Block.PLANKS.id, 0, 9, 5, 11, par3StructureBoundingBox);
                this.method_56(par1World, Block.PLANKS.id, 0, 8, 5, 11, par3StructureBoundingBox);
                this.method_56(par1World, Block.PLANKS.id, 0, 9, 5, 10, par3StructureBoundingBox);
                this.method_62(par1World, par3StructureBoundingBox, 3, 6, 2, 3, 6, 12, Block.WOODEN_FENCE.id, Block.WOODEN_FENCE.id, false);
                this.method_62(par1World, par3StructureBoundingBox, 10, 6, 2, 10, 6, 10, Block.WOODEN_FENCE.id, Block.WOODEN_FENCE.id, false);
                this.method_62(par1World, par3StructureBoundingBox, 4, 6, 2, 9, 6, 2, Block.WOODEN_FENCE.id, Block.WOODEN_FENCE.id, false);
                this.method_62(par1World, par3StructureBoundingBox, 4, 6, 12, 8, 6, 12, Block.WOODEN_FENCE.id, Block.WOODEN_FENCE.id, false);
                this.method_56(par1World, Block.WOODEN_FENCE.id, 0, 9, 6, 11, par3StructureBoundingBox);
                this.method_56(par1World, Block.WOODEN_FENCE.id, 0, 8, 6, 11, par3StructureBoundingBox);
                this.method_56(par1World, Block.WOODEN_FENCE.id, 0, 9, 6, 10, par3StructureBoundingBox);
                var7 = this.method_75(Block.LADDER_BLOCK.id, 3);
                this.method_56(par1World, Block.LADDER_BLOCK.id, var7, 10, 1, 13, par3StructureBoundingBox);
                this.method_56(par1World, Block.LADDER_BLOCK.id, var7, 10, 2, 13, par3StructureBoundingBox);
                this.method_56(par1World, Block.LADDER_BLOCK.id, var7, 10, 3, 13, par3StructureBoundingBox);
                this.method_56(par1World, Block.LADDER_BLOCK.id, var7, 10, 4, 13, par3StructureBoundingBox);
                this.method_56(par1World, Block.LADDER_BLOCK.id, var7, 10, 5, 13, par3StructureBoundingBox);
                this.method_56(par1World, Block.LADDER_BLOCK.id, var7, 10, 6, 13, par3StructureBoundingBox);
                this.method_56(par1World, Block.LADDER_BLOCK.id, var7, 10, 7, 13, par3StructureBoundingBox);
                byte var8 = 7;
                byte var9 = 7;
                this.method_56(par1World, Block.WOODEN_FENCE.id, 0, var8 - 1, 9, var9, par3StructureBoundingBox);
                this.method_56(par1World, Block.WOODEN_FENCE.id, 0, var8, 9, var9, par3StructureBoundingBox);
                this.method_56(par1World, Block.WOODEN_FENCE.id, 0, var8 - 1, 8, var9, par3StructureBoundingBox);
                this.method_56(par1World, Block.WOODEN_FENCE.id, 0, var8, 8, var9, par3StructureBoundingBox);
                this.method_56(par1World, Block.WOODEN_FENCE.id, 0, var8 - 1, 7, var9, par3StructureBoundingBox);
                this.method_56(par1World, Block.WOODEN_FENCE.id, 0, var8, 7, var9, par3StructureBoundingBox);
                this.method_56(par1World, Block.WOODEN_FENCE.id, 0, var8 - 2, 7, var9, par3StructureBoundingBox);
                this.method_56(par1World, Block.WOODEN_FENCE.id, 0, var8 + 1, 7, var9, par3StructureBoundingBox);
                this.method_56(par1World, Block.WOODEN_FENCE.id, 0, var8 - 1, 7, var9 - 1, par3StructureBoundingBox);
                this.method_56(par1World, Block.WOODEN_FENCE.id, 0, var8 - 1, 7, var9 + 1, par3StructureBoundingBox);
                this.method_56(par1World, Block.WOODEN_FENCE.id, 0, var8, 7, var9 - 1, par3StructureBoundingBox);
                this.method_56(par1World, Block.WOODEN_FENCE.id, 0, var8, 7, var9 + 1, par3StructureBoundingBox);
                this.method_56(par1World, Block.TORCH.id, 0, var8 - 2, 8, var9, par3StructureBoundingBox);
                this.method_56(par1World, Block.TORCH.id, 0, var8 + 1, 8, var9, par3StructureBoundingBox);
                this.method_56(par1World, Block.TORCH.id, 0, var8 - 1, 8, var9 - 1, par3StructureBoundingBox);
                this.method_56(par1World, Block.TORCH.id, 0, var8 - 1, 8, var9 + 1, par3StructureBoundingBox);
                this.method_56(par1World, Block.TORCH.id, 0, var8, 8, var9 - 1, par3StructureBoundingBox);
                this.method_56(par1World, Block.TORCH.id, 0, var8, 8, var9 + 1, par3StructureBoundingBox);
            }

            ChestGenHooks info = ChestGenHooks.getInfo("strongholdLibrary");
            this.method_69(par1World, par3StructureBoundingBox, par2Random, 3, 3, 5, info.getItems(), info.getCount(par2Random));
            if (this.field_39) {
                this.method_56(par1World, 0, 0, 12, 9, 1, par3StructureBoundingBox);
                this.method_69(par1World, par3StructureBoundingBox, par2Random, 12, 8, 1, info.getItems(), info.getCount(par2Random));
            }

            return true;
        }
    }
}
