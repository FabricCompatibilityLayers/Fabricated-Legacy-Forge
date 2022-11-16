package fr.catcore.fabricatedforge.mixin.forgefml.structure;

import net.minecraft.block.Block;
import net.minecraft.structure.StrongholdPieces;
import net.minecraft.structure.class_15;
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

@Mixin(class_15.class)
public abstract class class_15Mixin extends class_31 {
    @Shadow @Final private class_32 field_28;

    @Shadow private boolean field_29;

    protected class_15Mixin(int i) {
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
            this.method_64(par1World, par3StructureBoundingBox, 0, 0, 0, 4, 4, 6, true, par2Random, StrongholdPiecesAccessor.getField_25());
            this.method_41(par1World, par2Random, par3StructureBoundingBox, this.field_28, 1, 1, 0);
            this.method_41(par1World, par2Random, par3StructureBoundingBox, class_32.OPENING, 1, 1, 6);
            this.method_62(par1World, par3StructureBoundingBox, 3, 1, 2, 3, 1, 4, Block.STONE_BRICK.id, Block.STONE_BRICK.id, false);
            this.method_56(par1World, Block.STONE_SLAB.id, 5, 3, 1, 1, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_SLAB.id, 5, 3, 1, 5, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_SLAB.id, 5, 3, 2, 2, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_SLAB.id, 5, 3, 2, 4, par3StructureBoundingBox);

            for(int var4 = 2; var4 <= 4; ++var4) {
                this.method_56(par1World, Block.STONE_SLAB.id, 5, 2, 1, var4, par3StructureBoundingBox);
            }

            if (!this.field_29) {
                int var7 = this.applyYTransform(2);
                int var5 = this.applyXTransform(3, 3);
                int var6 = this.applyZTransform(3, 3);
                if (par3StructureBoundingBox.intersects(var5, var7, var6)) {
                    this.field_29 = true;
                    this.method_69(
                            par1World,
                            par3StructureBoundingBox,
                            par2Random,
                            3,
                            2,
                            3,
                            ChestGenHooks.getItems("strongholdCorridor"),
                            ChestGenHooks.getCount("strongholdCorridor", par2Random)
                    );
                }
            }

            return true;
        }
    }
}
