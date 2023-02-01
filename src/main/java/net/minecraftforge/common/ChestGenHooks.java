/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

import fr.catcore.fabricatedforge.mixininterface.IWeightedRandomChestContent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.*;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.util.collection.Weighting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class ChestGenHooks {
    public static final String MINESHAFT_CORRIDOR = "mineshaftCorridor";
    public static final String PYRAMID_DESERT_CHEST = "pyramidDesertyChest";
    public static final String PYRAMID_JUNGLE_CHEST = "pyramidJungleChest";
    public static final String PYRAMID_JUNGLE_DISPENSER = "pyramidJungleDispenser";
    public static final String STRONGHOLD_CORRIDOR = "strongholdCorridor";
    public static final String STRONGHOLD_LIBRARY = "strongholdLibrary";
    public static final String STRONGHOLD_CROSSING = "strongholdCrossing";
    public static final String VILLAGE_BLACKSMITH = "villageBlacksmith";
    public static final String BONUS_CHEST = "bonusChest";
    public static final String DUNGEON_CHEST = "dungeonChest";
    private static final HashMap<String, ChestGenHooks> chestInfo = new HashMap();
    private static boolean hasInit = false;
    private String category;
    private int countMin = 0;
    private int countMax = 0;
    ArrayList<WeightedRandomChestContent> contents = new ArrayList();

    private static void init() {
        if (!hasInit) {
            addInfo("mineshaftCorridor", MineshaftPieces.field_4910, 3, 7);
            addInfo("pyramidDesertyChest", class_5.field_6, 2, 7);
            addInfo("pyramidJungleChest", class_6.field_11, 2, 7);
            addInfo("pyramidJungleDispenser", class_6.field_12, 2, 2);
            addInfo("strongholdCorridor", class_15.field_27, 2, 4);
            addInfo("strongholdLibrary", class_19.field_38, 1, 5);
            addInfo("strongholdCrossing", class_24.field_48, 1, 5);
            addInfo("villageBlacksmith", class_49.field_89, 3, 9);
            addInfo("bonusChest", ServerWorld.field_2817, 10, 10);
            ItemStack book = new ItemStack(Item.ENCHANTED_BOOK, 1, 0);
            WeightedRandomChestContent tmp = new WeightedRandomChestContent(book, 1, 1, 1);
            getInfo("mineshaftCorridor").addItem(tmp);
            getInfo("pyramidDesertyChest").addItem(tmp);
            getInfo("pyramidJungleChest").addItem(tmp);
            getInfo("strongholdCorridor").addItem(tmp);
            getInfo("strongholdLibrary").addItem(new WeightedRandomChestContent(book, 1, 5, 2));
            getInfo("strongholdCrossing").addItem(tmp);
            ChestGenHooks d = new ChestGenHooks("dungeonChest");
            d.countMin = 8;
            d.countMax = 8;
            chestInfo.put("dungeonChest", d);
            addDungeonLoot(d, new ItemStack(Item.SADDLE), 100, 1, 1);
            addDungeonLoot(d, new ItemStack(Item.IRON_INGOT), 100, 1, 4);
            addDungeonLoot(d, new ItemStack(Item.BREAD), 100, 1, 1);
            addDungeonLoot(d, new ItemStack(Item.WHEAT), 100, 1, 4);
            addDungeonLoot(d, new ItemStack(Item.GUNPOWDER), 100, 1, 4);
            addDungeonLoot(d, new ItemStack(Item.STRING), 100, 1, 4);
            addDungeonLoot(d, new ItemStack(Item.BUCKET), 100, 1, 1);
            addDungeonLoot(d, new ItemStack(Item.GOLDEN_APPLE), 1, 1, 1);
            addDungeonLoot(d, new ItemStack(Item.REDSTONE), 40, 1, 4);
            addDungeonLoot(d, new ItemStack(Item.RECORD_13), 5, 1, 1);
            addDungeonLoot(d, new ItemStack(Item.RECORD_CAT), 5, 1, 1);
            addDungeonLoot(d, new ItemStack(Item.DYES, 1, 3), 100, 1, 1);
            addDungeonLoot(d, book, 100, 1, 1);
        }
    }

    static void addDungeonLoot(ChestGenHooks dungeon, ItemStack item, int weight, int min, int max) {
        dungeon.addItem(new WeightedRandomChestContent(item, min, max, weight));
    }

    private static void addInfo(String category, WeightedRandomChestContent[] items, int min, int max) {
        chestInfo.put(category, new ChestGenHooks(category, items, min, max));
    }

    public static ChestGenHooks getInfo(String category) {
        if (!chestInfo.containsKey(category)) {
            chestInfo.put(category, new ChestGenHooks(category));
        }

        return (ChestGenHooks)chestInfo.get(category);
    }

    public static ItemStack[] generateStacks(Random rand, ItemStack source, int min, int max) {
        int count = min + rand.nextInt(max - min + 1);
        ItemStack[] ret;
        if (source.getItem() == null) {
            ret = new ItemStack[0];
        } else if (count > source.getItem().getMaxCount()) {
            ret = new ItemStack[count];

            for(int x = 0; x < count; ++x) {
                ret[x] = source.copy();
                ret[x].count = 1;
            }
        } else {
            ret = new ItemStack[1];
            ret[0] = source.copy();
            ret[0].count = count;
        }

        return ret;
    }

    public static WeightedRandomChestContent[] getItems(String category, Random rnd) {
        return getInfo(category).getItems(rnd);
    }

    public static int getCount(String category, Random rand) {
        return getInfo(category).getCount(rand);
    }

    public static void addItem(String category, WeightedRandomChestContent item) {
        getInfo(category).addItem(item);
    }

    public static void removeItem(String category, ItemStack item) {
        getInfo(category).removeItem(item);
    }

    public static ItemStack getOneItem(String category, Random rand) {
        return getInfo(category).getOneItem(rand);
    }

    public ChestGenHooks(String category) {
        this.category = category;
    }

    public ChestGenHooks(String category, WeightedRandomChestContent[] items, int min, int max) {
        this(category);

        for(WeightedRandomChestContent item : items) {
            this.contents.add(item);
        }

        this.countMin = min;
        this.countMax = max;
    }

    public void addItem(WeightedRandomChestContent item) {
        this.contents.add(item);
    }

    public void removeItem(ItemStack item) {
        Iterator<WeightedRandomChestContent> itr = this.contents.iterator();

        while(itr.hasNext()) {
            WeightedRandomChestContent cont = (WeightedRandomChestContent)itr.next();
            if (item.equalsIgnoreNbt(cont.content) || item.getData() == -1 && item.id == cont.content.id) {
                itr.remove();
            }
        }
    }

    public WeightedRandomChestContent[] getItems(Random rnd) {
        ArrayList<WeightedRandomChestContent> ret = new ArrayList();

        for(WeightedRandomChestContent orig : this.contents) {
            Item item = orig.content.getItem();
            if (item != null) {
                WeightedRandomChestContent n = item.getChestGenBase(this, rnd, orig);
                if (n != null) {
                    ret.add(n);
                }
            }
        }

        return (WeightedRandomChestContent[])ret.toArray(new WeightedRandomChestContent[ret.size()]);
    }

    public int getCount(Random rand) {
        return this.countMin < this.countMax ? this.countMin + rand.nextInt(this.countMax - this.countMin) : this.countMin;
    }

    public ItemStack getOneItem(Random rand) {
        WeightedRandomChestContent[] items = this.getItems(rand);
        WeightedRandomChestContent item = (WeightedRandomChestContent) Weighting.getRandom(rand, items);
        ItemStack[] stacks = generateStacks(rand, item.content, item.min, item.max);
        return stacks.length > 0 ? stacks[0] : null;
    }

    public int getMin() {
        return this.countMin;
    }

    public int getMax() {
        return this.countMax;
    }

    public void setMin(int value) {
        this.countMin = value;
    }

    public void setMax(int value) {
        this.countMax = value;
    }

    static {
        init();
    }
}
