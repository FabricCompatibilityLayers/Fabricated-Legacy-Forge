/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.util.collection.Weight;
import net.minecraft.util.collection.Weighting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class DungeonHooks {
    private static ArrayList<DungeonHooks.DungeonMob> dungeonMobs = new ArrayList();

    public DungeonHooks() {
    }

    public static float addDungeonMob(String name, int rarity) {
        if (rarity <= 0) {
            throw new IllegalArgumentException("Rarity must be greater then zero");
        } else {
            for(DungeonHooks.DungeonMob mob : dungeonMobs) {
                if (name.equals(mob.type)) {
                    return (float)(mob.weight += rarity);
                }
            }

            dungeonMobs.add(new DungeonHooks.DungeonMob(rarity, name));
            return (float)rarity;
        }
    }

    public static int removeDungeonMob(String name) {
        for(DungeonHooks.DungeonMob mob : dungeonMobs) {
            if (name.equals(mob.type)) {
                dungeonMobs.remove(mob);
                return mob.weight;
            }
        }

        return 0;
    }

    public static String getRandomDungeonMob(Random rand) {
        DungeonHooks.DungeonMob mob = (DungeonHooks.DungeonMob)Weighting.getRandom(rand, dungeonMobs);
        return mob == null ? "" : mob.type;
    }

    @Deprecated
    public static void setDungeonLootTries(int number) {
        ChestGenHooks.getInfo("dungeonChest").setMax(number);
        ChestGenHooks.getInfo("dungeonChest").setMin(number);
    }

    @Deprecated
    public static int getDungeonLootTries() {
        return ChestGenHooks.getInfo("dungeonChest").getMax();
    }

    @Deprecated
    public void addDungeonLoot(DungeonHooks.DungeonLoot loot) {
        ChestGenHooks.getInfo("dungeonChest").addItem(loot);
    }

    @Deprecated
    public boolean removeDungeonLoot(DungeonHooks.DungeonLoot loot) {
        return ChestGenHooks.getInfo("dungeonChest").contents.remove(loot);
    }

    @Deprecated
    public static void addDungeonLoot(ItemStack item, int rarity) {
        addDungeonLoot(item, rarity, 1, 1);
    }

    @Deprecated
    public static float addDungeonLoot(ItemStack item, int rarity, int minCount, int maxCount) {
        ChestGenHooks.addDungeonLoot(ChestGenHooks.getInfo("dungeonChest"), item, rarity, minCount, maxCount);
        return (float)rarity;
    }

    @Deprecated
    public static void removeDungeonLoot(ItemStack item) {
        ChestGenHooks.removeItem("dungeonChest", item);
    }

    @Deprecated
    public static void removeDungeonLoot(ItemStack item, int minCount, int maxCount) {
        ChestGenHooks.removeItem("dungeonChest", item);
    }

    @Deprecated
    public static ItemStack getRandomDungeonLoot(Random rand) {
        return ChestGenHooks.getOneItem("dungeonChest", rand);
    }

    static {
        addDungeonMob("Skeleton", 100);
        addDungeonMob("Zombie", 200);
        addDungeonMob("Spider", 100);
    }

    @Deprecated
    public static class DungeonLoot extends WeightedRandomChestContent {
        @Deprecated
        public DungeonLoot(int weight, ItemStack item, int min, int max) {
            super(item, weight, min, max);
        }

        @Deprecated
        public ItemStack generateStack(Random rand) {
            int min = this.min;
            int max = this.max;
            ItemStack ret = this.content.copy();
            ret.count = min + rand.nextInt(max - min + 1);
            return ret;
        }

        protected final ItemStack[] generateChestContent(Random random, Inventory newInventory) {
            FMLLog.warning("Some mod is still using DungeonHooks.DungonLoot, tell them to stop! %s", new Object[]{this});
            return new ItemStack[]{this.generateStack(random)};
        }

        public boolean equals(ItemStack item, int min, int max) {
            int minCount = this.min;
            int maxCount = this.max;
            return min == minCount && max == maxCount && item.equalsIgnoreNbt(this.content) && ItemStack.equalsIgnoreDamage(item, this.content);
        }

        public boolean equals(ItemStack item) {
            return item.equalsIgnoreNbt(this.content) && ItemStack.equalsIgnoreDamage(item, this.content);
        }
    }

    public static class DungeonMob extends Weight {
        public String type;

        public DungeonMob(int weight, String type) {
            super(weight);
            this.type = type;
        }

        public boolean equals(Object target) {
            return target instanceof DungeonHooks.DungeonMob && this.type.equals(((DungeonHooks.DungeonMob)target).type);
        }
    }
}
