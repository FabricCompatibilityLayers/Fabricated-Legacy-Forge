package fr.catcore.fabricatedforge.mixin.forgefml.structure;

import net.minecraft.block.Block;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.class_1254;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(class_1254.class)
public abstract class class_1254Mixin extends StructurePiece {
    @Shadow private int field_4914;

    @Shadow @Final private boolean field_4912;

    @Shadow private boolean field_4913;

    @Shadow @Final private boolean field_4911;

    protected class_1254Mixin(int chainLength) {
        super(chainLength);
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
            int var8 = this.field_4914 * 5 - 1;
            this.method_62(par1World, par3StructureBoundingBox, 0, 0, 0, 2, 1, var8, 0, 0, false);
            this.method_66(par1World, par3StructureBoundingBox, par2Random, 0.8F, 0, 2, 0, 2, 2, var8, 0, 0, false);
            if (this.field_4912) {
                this.method_66(par1World, par3StructureBoundingBox, par2Random, 0.6F, 0, 0, 0, 2, 1, var8, Block.COBWEB.id, 0, false);
            }

            for(int var9 = 0; var9 < this.field_4914; ++var9) {
                int var10 = 2 + var9 * 5;
                this.method_62(par1World, par3StructureBoundingBox, 0, 0, var10, 0, 1, var10, Block.WOODEN_FENCE.id, 0, false);
                this.method_62(par1World, par3StructureBoundingBox, 2, 0, var10, 2, 1, var10, Block.WOODEN_FENCE.id, 0, false);
                if (par2Random.nextInt(4) == 0) {
                    this.method_62(par1World, par3StructureBoundingBox, 0, 2, var10, 0, 2, var10, Block.PLANKS.id, 0, false);
                    this.method_62(par1World, par3StructureBoundingBox, 2, 2, var10, 2, 2, var10, Block.PLANKS.id, 0, false);
                } else {
                    this.method_62(par1World, par3StructureBoundingBox, 0, 2, var10, 2, 2, var10, Block.PLANKS.id, 0, false);
                }

                this.method_65(par1World, par3StructureBoundingBox, par2Random, 0.1F, 0, 2, var10 - 1, Block.COBWEB.id, 0);
                this.method_65(par1World, par3StructureBoundingBox, par2Random, 0.1F, 2, 2, var10 - 1, Block.COBWEB.id, 0);
                this.method_65(par1World, par3StructureBoundingBox, par2Random, 0.1F, 0, 2, var10 + 1, Block.COBWEB.id, 0);
                this.method_65(par1World, par3StructureBoundingBox, par2Random, 0.1F, 2, 2, var10 + 1, Block.COBWEB.id, 0);
                this.method_65(par1World, par3StructureBoundingBox, par2Random, 0.05F, 0, 2, var10 - 2, Block.COBWEB.id, 0);
                this.method_65(par1World, par3StructureBoundingBox, par2Random, 0.05F, 2, 2, var10 - 2, Block.COBWEB.id, 0);
                this.method_65(par1World, par3StructureBoundingBox, par2Random, 0.05F, 0, 2, var10 + 2, Block.COBWEB.id, 0);
                this.method_65(par1World, par3StructureBoundingBox, par2Random, 0.05F, 2, 2, var10 + 2, Block.COBWEB.id, 0);
                this.method_65(par1World, par3StructureBoundingBox, par2Random, 0.05F, 1, 2, var10 - 1, Block.TORCH.id, 0);
                this.method_65(par1World, par3StructureBoundingBox, par2Random, 0.05F, 1, 2, var10 + 1, Block.TORCH.id, 0);
                ChestGenHooks info = ChestGenHooks.getInfo("mineshaftCorridor");
                if (par2Random.nextInt(100) == 0) {
                    this.method_69(par1World, par3StructureBoundingBox, par2Random, 2, 0, var10 - 1, info.getItems(par2Random), info.getCount(par2Random));
                }

                if (par2Random.nextInt(100) == 0) {
                    this.method_69(par1World, par3StructureBoundingBox, par2Random, 0, 0, var10 + 1, info.getItems(par2Random), info.getCount(par2Random));
                }

                if (this.field_4912 && !this.field_4913) {
                    int var11 = this.applyYTransform(0);
                    int var12 = var10 - 1 + par2Random.nextInt(3);
                    int var13 = this.applyXTransform(1, var12);
                    var12 = this.applyZTransform(1, var12);
                    if (par3StructureBoundingBox.intersects(var13, var11, var12)) {
                        this.field_4913 = true;
                        par1World.method_3690(var13, var11, var12, Block.SPAWNER.id);
                        MobSpawnerBlockEntity var14 = (MobSpawnerBlockEntity)par1World.getBlockEntity(var13, var11, var12);
                        if (var14 != null) {
                            var14.method_527("CaveSpider");
                        }
                    }
                }
            }

            for(int var121 = 0; var121 <= 2; ++var121) {
                for(int var10 = 0; var10 <= var8; ++var10) {
                    int var11 = this.method_57(par1World, var121, -1, var10, par3StructureBoundingBox);
                    if (var11 == 0) {
                        this.method_56(par1World, Block.PLANKS.id, 0, var121, -1, var10, par3StructureBoundingBox);
                    }
                }
            }

            if (this.field_4911) {
                for(int var131 = 0; var131 <= var8; ++var131) {
                    int var10 = this.method_57(par1World, 1, -1, var131, par3StructureBoundingBox);
                    if (var10 > 0 && Block.field_493[var10]) {
                        this.method_65(
                                par1World, par3StructureBoundingBox, par2Random, 0.7F, 1, 0, var131, Block.RAIL_BLOCK.id, this.method_75(Block.RAIL_BLOCK.id, 0)
                        );
                    }
                }
            }

            return true;
        }
    }
}
