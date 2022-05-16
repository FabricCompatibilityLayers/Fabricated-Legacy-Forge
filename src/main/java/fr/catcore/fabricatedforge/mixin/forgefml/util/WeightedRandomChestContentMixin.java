package fr.catcore.fabricatedforge.mixin.forgefml.util;

import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.util.collection.Weighting;
import net.minecraftforge.common.ChestGenHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(WeightedRandomChestContent.class)
public class WeightedRandomChestContentMixin {

    @Unique
    public ItemStack itemStack;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void fmlCtr(int par1, int par2, int par3, int par4, int par5, CallbackInfo ci) {
        this.itemStack = new ItemStack(par1, 1, par2);
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public static void method_2379(Random par0Random, WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, ChestBlockEntity par2TileEntityChest, int par3) {
        for(int var4 = 0; var4 < par3; ++var4) {
            WeightedRandomChestContent var5 = (WeightedRandomChestContent) Weighting.getRandom(par0Random, par1ArrayOfWeightedRandomChestContent);
            ItemStack[] stacks = ChestGenHooks.generateStacks(par0Random, var5.itemStack, var5.minCount, var5.minCount);
            ItemStack[] arr$ = stacks;
            int len$ = stacks.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                ItemStack item = arr$[i$];
                par2TileEntityChest.setInvStack(par0Random.nextInt(par2TileEntityChest.getInvSize()), item);
            }
        }

    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public static void generateLoot(Random par0Random, WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, DispenserBlockEntity par2TileEntityDispenser, int par3) {
        for(int var4 = 0; var4 < par3; ++var4) {
            WeightedRandomChestContent var5 = (WeightedRandomChestContent)Weighting.getRandom(par0Random, par1ArrayOfWeightedRandomChestContent);
            ItemStack[] stacks = ChestGenHooks.generateStacks(par0Random, var5.itemStack, var5.minCount, var5.minCount);

            for (ItemStack item : stacks) {
                par2TileEntityDispenser.setInvStack(par0Random.nextInt(par2TileEntityDispenser.getInvSize()), item);
            }
        }

    }
}
