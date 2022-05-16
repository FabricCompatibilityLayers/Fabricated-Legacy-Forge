package fr.catcore.fabricatedforge.mixin.forgefml.structure;

import net.minecraft.block.Block;
import net.minecraft.block.LeverBlock;
import net.minecraft.structure.class_6;
import net.minecraft.structure.class_7;
import net.minecraft.structure.class_8;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(class_6.class)
public abstract class class_6Mixin extends class_8 {
    @Shadow private static class_7 field_13;

    @Shadow private boolean field_9;

    @Shadow private boolean field_10;

    @Shadow private boolean field_7;

    @Shadow private boolean field_8;

    protected class_6Mixin(Random random, int i, int j, int k, int l, int m, int n) {
        super(random, i, j, k, l, m, n);
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public boolean generate(World par1World, Random par2Random, BlockBox par3StructureBoundingBox) {
        if (!this.method_16(par1World, par3StructureBoundingBox, 0)) {
            return false;
        } else {
            int var4 = this.method_75(Block.STONE_STAIRS.id, 3);
            int var5 = this.method_75(Block.STONE_STAIRS.id, 2);
            int var6 = this.method_75(Block.STONE_STAIRS.id, 0);
            int var7 = this.method_75(Block.STONE_STAIRS.id, 1);
            this.method_64(par1World, par3StructureBoundingBox, 0, -4, 0, this.field_14 - 1, 0, this.field_16 - 1, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 2, 1, 2, 9, 2, 2, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 2, 1, 12, 9, 2, 12, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 2, 1, 3, 2, 2, 11, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 9, 1, 3, 9, 2, 11, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 1, 3, 1, 10, 6, 1, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 1, 3, 13, 10, 6, 13, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 1, 3, 2, 1, 6, 12, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 10, 3, 2, 10, 6, 12, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 2, 3, 2, 9, 3, 12, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 2, 6, 2, 9, 6, 12, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 3, 7, 3, 8, 7, 11, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 4, 8, 4, 7, 8, 10, false, par2Random, field_13);
            this.setAir(par1World, par3StructureBoundingBox, 3, 1, 3, 8, 2, 11);
            this.setAir(par1World, par3StructureBoundingBox, 4, 3, 6, 7, 3, 9);
            this.setAir(par1World, par3StructureBoundingBox, 2, 4, 2, 9, 5, 12);
            this.setAir(par1World, par3StructureBoundingBox, 4, 6, 5, 7, 6, 9);
            this.setAir(par1World, par3StructureBoundingBox, 5, 7, 6, 6, 7, 8);
            this.setAir(par1World, par3StructureBoundingBox, 5, 1, 2, 6, 2, 2);
            this.setAir(par1World, par3StructureBoundingBox, 5, 2, 12, 6, 2, 12);
            this.setAir(par1World, par3StructureBoundingBox, 5, 5, 1, 6, 5, 1);
            this.setAir(par1World, par3StructureBoundingBox, 5, 5, 13, 6, 5, 13);
            this.method_56(par1World, 0, 0, 1, 5, 5, par3StructureBoundingBox);
            this.method_56(par1World, 0, 0, 10, 5, 5, par3StructureBoundingBox);
            this.method_56(par1World, 0, 0, 1, 5, 9, par3StructureBoundingBox);
            this.method_56(par1World, 0, 0, 10, 5, 9, par3StructureBoundingBox);

            int var8;
            for(var8 = 0; var8 <= 14; var8 += 14) {
                this.method_64(par1World, par3StructureBoundingBox, 2, 4, var8, 2, 5, var8, false, par2Random, field_13);
                this.method_64(par1World, par3StructureBoundingBox, 4, 4, var8, 4, 5, var8, false, par2Random, field_13);
                this.method_64(par1World, par3StructureBoundingBox, 7, 4, var8, 7, 5, var8, false, par2Random, field_13);
                this.method_64(par1World, par3StructureBoundingBox, 9, 4, var8, 9, 5, var8, false, par2Random, field_13);
            }

            this.method_64(par1World, par3StructureBoundingBox, 5, 6, 0, 6, 6, 0, false, par2Random, field_13);

            for(var8 = 0; var8 <= 11; var8 += 11) {
                for(int var9 = 2; var9 <= 12; var9 += 2) {
                    this.method_64(par1World, par3StructureBoundingBox, var8, 4, var9, var8, 5, var9, false, par2Random, field_13);
                }

                this.method_64(par1World, par3StructureBoundingBox, var8, 6, 5, var8, 6, 5, false, par2Random, field_13);
                this.method_64(par1World, par3StructureBoundingBox, var8, 6, 9, var8, 6, 9, false, par2Random, field_13);
            }

            this.method_64(par1World, par3StructureBoundingBox, 2, 7, 2, 2, 9, 2, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 9, 7, 2, 9, 9, 2, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 2, 7, 12, 2, 9, 12, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 9, 7, 12, 9, 9, 12, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 4, 9, 4, 4, 9, 4, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 7, 9, 4, 7, 9, 4, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 4, 9, 10, 4, 9, 10, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 7, 9, 10, 7, 9, 10, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 5, 9, 7, 6, 9, 7, false, par2Random, field_13);
            this.method_56(par1World, Block.STONE_STAIRS.id, var4, 5, 9, 6, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_STAIRS.id, var4, 6, 9, 6, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_STAIRS.id, var5, 5, 9, 8, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_STAIRS.id, var5, 6, 9, 8, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_STAIRS.id, var4, 4, 0, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_STAIRS.id, var4, 5, 0, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_STAIRS.id, var4, 6, 0, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_STAIRS.id, var4, 7, 0, 0, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_STAIRS.id, var4, 4, 1, 8, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_STAIRS.id, var4, 4, 2, 9, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_STAIRS.id, var4, 4, 3, 10, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_STAIRS.id, var4, 7, 1, 8, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_STAIRS.id, var4, 7, 2, 9, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_STAIRS.id, var4, 7, 3, 10, par3StructureBoundingBox);
            this.method_64(par1World, par3StructureBoundingBox, 4, 1, 9, 4, 1, 9, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 7, 1, 9, 7, 1, 9, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 4, 1, 10, 7, 2, 10, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 5, 4, 5, 6, 4, 5, false, par2Random, field_13);
            this.method_56(par1World, Block.STONE_STAIRS.id, var6, 4, 4, 5, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_STAIRS.id, var7, 7, 4, 5, par3StructureBoundingBox);

            for(var8 = 0; var8 < 4; ++var8) {
                this.method_56(par1World, Block.STONE_STAIRS.id, var5, 5, -var8, 6 + var8, par3StructureBoundingBox);
                this.method_56(par1World, Block.STONE_STAIRS.id, var5, 6, -var8, 6 + var8, par3StructureBoundingBox);
                this.setAir(par1World, par3StructureBoundingBox, 5, -var8, 7 + var8, 6, -var8, 9 + var8);
            }

            this.setAir(par1World, par3StructureBoundingBox, 1, -3, 12, 10, -1, 13);
            this.setAir(par1World, par3StructureBoundingBox, 1, -3, 1, 3, -1, 13);
            this.setAir(par1World, par3StructureBoundingBox, 1, -3, 1, 9, -1, 5);

            for(var8 = 1; var8 <= 13; var8 += 2) {
                this.method_64(par1World, par3StructureBoundingBox, 1, -3, var8, 1, -2, var8, false, par2Random, field_13);
            }

            for(var8 = 2; var8 <= 12; var8 += 2) {
                this.method_64(par1World, par3StructureBoundingBox, 1, -1, var8, 3, -1, var8, false, par2Random, field_13);
            }

            this.method_64(par1World, par3StructureBoundingBox, 2, -2, 1, 5, -2, 1, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 7, -2, 1, 9, -2, 1, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 6, -3, 1, 6, -3, 1, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 6, -1, 1, 6, -1, 1, false, par2Random, field_13);
            this.method_56(par1World, Block.TRIPWIRE_HOOK.id, this.method_75(Block.TRIPWIRE_HOOK.id, 3) | 4, 1, -3, 8, par3StructureBoundingBox);
            this.method_56(par1World, Block.TRIPWIRE_HOOK.id, this.method_75(Block.TRIPWIRE_HOOK.id, 1) | 4, 4, -3, 8, par3StructureBoundingBox);
            this.method_56(par1World, Block.TRIPWIRE.id, 4, 2, -3, 8, par3StructureBoundingBox);
            this.method_56(par1World, Block.TRIPWIRE.id, 4, 3, -3, 8, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_408.id, 0, 5, -3, 7, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_408.id, 0, 5, -3, 6, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_408.id, 0, 5, -3, 5, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_408.id, 0, 5, -3, 4, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_408.id, 0, 5, -3, 3, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_408.id, 0, 5, -3, 2, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_408.id, 0, 5, -3, 1, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_408.id, 0, 4, -3, 1, par3StructureBoundingBox);
            this.method_56(par1World, Block.MOSSY_COBBLESTONE.id, 0, 3, -3, 1, par3StructureBoundingBox);
            ChestGenHooks dispenser = ChestGenHooks.getInfo("pyramidJungleDispenser");
            ChestGenHooks chest = ChestGenHooks.getInfo("pyramidJungleChest");
            if (!this.field_9) {
                this.field_9 = this.method_68(par1World, par3StructureBoundingBox, par2Random, 3, -2, 1, 2, dispenser.getItems(), dispenser.getCount(par2Random));
            }

            this.method_56(par1World, Block.VINE.id, 15, 3, -2, 2, par3StructureBoundingBox);
            this.method_56(par1World, Block.TRIPWIRE_HOOK.id, this.method_75(Block.TRIPWIRE_HOOK.id, 2) | 4, 7, -3, 1, par3StructureBoundingBox);
            this.method_56(par1World, Block.TRIPWIRE_HOOK.id, this.method_75(Block.TRIPWIRE_HOOK.id, 0) | 4, 7, -3, 5, par3StructureBoundingBox);
            this.method_56(par1World, Block.TRIPWIRE.id, 4, 7, -3, 2, par3StructureBoundingBox);
            this.method_56(par1World, Block.TRIPWIRE.id, 4, 7, -3, 3, par3StructureBoundingBox);
            this.method_56(par1World, Block.TRIPWIRE.id, 4, 7, -3, 4, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_408.id, 0, 8, -3, 6, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_408.id, 0, 9, -3, 6, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_408.id, 0, 9, -3, 5, par3StructureBoundingBox);
            this.method_56(par1World, Block.MOSSY_COBBLESTONE.id, 0, 9, -3, 4, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_408.id, 0, 9, -2, 4, par3StructureBoundingBox);
            if (!this.field_10) {
                this.field_10 = this.method_68(par1World, par3StructureBoundingBox, par2Random, 9, -2, 3, 4, dispenser.getItems(), dispenser.getCount(par2Random));
            }

            this.method_56(par1World, Block.VINE.id, 15, 8, -1, 3, par3StructureBoundingBox);
            this.method_56(par1World, Block.VINE.id, 15, 8, -2, 3, par3StructureBoundingBox);
            if (!this.field_7) {
                this.field_7 = this.method_69(par1World, par3StructureBoundingBox, par2Random, 8, -3, 3, chest.getItems(), chest.getCount(par2Random));
            }

            this.method_56(par1World, Block.MOSSY_COBBLESTONE.id, 0, 9, -3, 2, par3StructureBoundingBox);
            this.method_56(par1World, Block.MOSSY_COBBLESTONE.id, 0, 8, -3, 1, par3StructureBoundingBox);
            this.method_56(par1World, Block.MOSSY_COBBLESTONE.id, 0, 4, -3, 5, par3StructureBoundingBox);
            this.method_56(par1World, Block.MOSSY_COBBLESTONE.id, 0, 5, -2, 5, par3StructureBoundingBox);
            this.method_56(par1World, Block.MOSSY_COBBLESTONE.id, 0, 5, -1, 5, par3StructureBoundingBox);
            this.method_56(par1World, Block.MOSSY_COBBLESTONE.id, 0, 6, -3, 5, par3StructureBoundingBox);
            this.method_56(par1World, Block.MOSSY_COBBLESTONE.id, 0, 7, -2, 5, par3StructureBoundingBox);
            this.method_56(par1World, Block.MOSSY_COBBLESTONE.id, 0, 7, -1, 5, par3StructureBoundingBox);
            this.method_56(par1World, Block.MOSSY_COBBLESTONE.id, 0, 8, -3, 5, par3StructureBoundingBox);
            this.method_64(par1World, par3StructureBoundingBox, 9, -1, 1, 9, -1, 5, false, par2Random, field_13);
            this.setAir(par1World, par3StructureBoundingBox, 8, -3, 8, 10, -1, 10);
            this.method_56(par1World, Block.STONE_BRICK.id, 3, 8, -2, 11, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_BRICK.id, 3, 9, -2, 11, par3StructureBoundingBox);
            this.method_56(par1World, Block.STONE_BRICK.id, 3, 10, -2, 11, par3StructureBoundingBox);
            this.method_56(par1World, Block.LEVER.id, LeverBlock.method_327(this.method_75(Block.LEVER.id, 2)), 8, -2, 12, par3StructureBoundingBox);
            this.method_56(par1World, Block.LEVER.id, LeverBlock.method_327(this.method_75(Block.LEVER.id, 2)), 9, -2, 12, par3StructureBoundingBox);
            this.method_56(par1World, Block.LEVER.id, LeverBlock.method_327(this.method_75(Block.LEVER.id, 2)), 10, -2, 12, par3StructureBoundingBox);
            this.method_64(par1World, par3StructureBoundingBox, 8, -3, 8, 8, -3, 10, false, par2Random, field_13);
            this.method_64(par1World, par3StructureBoundingBox, 10, -3, 8, 10, -3, 10, false, par2Random, field_13);
            this.method_56(par1World, Block.MOSSY_COBBLESTONE.id, 0, 10, -2, 9, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_408.id, 0, 8, -2, 9, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_408.id, 0, 8, -2, 10, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_408.id, 0, 10, -1, 9, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_355.id, 1, 9, -2, 8, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_355.id, this.method_75(Block.field_355.id, 4), 10, -2, 8, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_355.id, this.method_75(Block.field_355.id, 4), 10, -1, 8, par3StructureBoundingBox);
            this.method_56(par1World, Block.field_447.id, this.method_75(Block.field_447.id, 2), 10, -2, 10, par3StructureBoundingBox);
            if (!this.field_8) {
                this.field_8 = this.method_69(par1World, par3StructureBoundingBox, par2Random, 9, -3, 10, chest.getItems(), chest.getCount(par2Random));
            }

            return true;
        }
    }
}
