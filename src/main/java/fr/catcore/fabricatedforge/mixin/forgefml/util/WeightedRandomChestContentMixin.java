package fr.catcore.fabricatedforge.mixin.forgefml.util;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.util.collection.Weighting;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DungeonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(WeightedRandomChestContent.class)
public abstract class WeightedRandomChestContentMixin {
    /**
     * @author forge
     * @reason hooks
     */
    @Overwrite
    public static void method_2379(
            Random par0Random, WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, ChestBlockEntity par2TileEntityChest, int par3
    ) {
        for(int var4 = 0; var4 < par3; ++var4) {
            WeightedRandomChestContent var5 = (WeightedRandomChestContent) Weighting.getRandom(par0Random, par1ArrayOfWeightedRandomChestContent);
            if (var5 instanceof DungeonHooks.DungeonLoot) {
                DungeonHooks.DungeonLoot loot = (DungeonHooks.DungeonLoot)var5;
                par2TileEntityChest.setInvStack(par0Random.nextInt(par2TileEntityChest.getInvSize()), loot.generateStack(par0Random));
                FMLLog.warning("Some mod is still using DungeonHooks.DungonLoot, tell them to stop! %s", new Object[]{loot});
            } else {
                ItemStack[] stacks = ChestGenHooks.generateStacks(par0Random, var5.content, var5.min, var5.max);

                for(ItemStack item : stacks) {
                    par2TileEntityChest.setInvStack(par0Random.nextInt(par2TileEntityChest.getInvSize()), item);
                }
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
            WeightedRandomChestContent var5 = (WeightedRandomChestContent)Weighting.getRandom(par0Random, par1ArrayOfWeightedRandomChestContent);
            ItemStack[] stacks = ChestGenHooks.generateStacks(par0Random, var5.content, var5.min, var5.max);

            for(ItemStack item : stacks) {
                par2TileEntityDispenser.setInvStack(par0Random.nextInt(par2TileEntityDispenser.getInvSize()), item);
            }
        }
    }
}
