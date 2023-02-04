package fr.catcore.fabricatedforge.mixin.forgefml.util;

import fr.catcore.fabricatedforge.mixininterface.IWeightedRandomChestContent;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.util.collection.Weighting;
import net.minecraftforge.common.ChestGenHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(WeightedRandomChestContent.class)
public abstract class WeightedRandomChestContentMixin implements IWeightedRandomChestContent {
    @Shadow public ItemStack content;

    @Shadow public int min;

    @Shadow public int max;

    /**
     * @author forge
     * @reason hooks
     */
    @Overwrite
    public static void method_2379(
            Random par0Random, WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, ChestBlockEntity par2TileEntityChest, int par3
    ) {
        for(int var4 = 0; var4 < par3; ++var4) {
            IWeightedRandomChestContent var5 = (WeightedRandomChestContent)Weighting.getRandom(par0Random, par1ArrayOfWeightedRandomChestContent);
            ItemStack[] stacks = var5.generateChestContent(par0Random, par2TileEntityChest);

            for(ItemStack item : stacks) {
                par2TileEntityChest.setInvStack(par0Random.nextInt(par2TileEntityChest.getInvSize()), item);
            }
        }
    }

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    public static void generateLoot(
            Random par0Random, WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, DispenserBlockEntity par2TileEntityDispenser, int par3
    ) {
        for(int var4 = 0; var4 < par3; ++var4) {
            IWeightedRandomChestContent var5 = (WeightedRandomChestContent)Weighting.getRandom(par0Random, par1ArrayOfWeightedRandomChestContent);
            ItemStack[] stacks = var5.generateChestContent(par0Random, par2TileEntityDispenser);

            for(ItemStack item : stacks) {
                par2TileEntityDispenser.setInvStack(par0Random.nextInt(par2TileEntityDispenser.getInvSize()), item);
            }
        }
    }

    @Override
    public ItemStack[] generateChestContent(Random random, Inventory newInventory) {
        return ChestGenHooks.generateStacks(random, this.content, this.min, this.max);
    }
}
