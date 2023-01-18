/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.oredict;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OreDictionary {
    private static int maxID = 0;
    private static HashMap<String, Integer> oreIDs = new HashMap();
    private static HashMap<Integer, ArrayList<ItemStack>> oreStacks = new HashMap();

    public OreDictionary() {
    }

    public static void initVanillaEntries() {
        registerOre("woodLog", new ItemStack(Block.LOG, 1, 0));
        registerOre("woodLog", new ItemStack(Block.LOG, 1, 1));
        registerOre("woodLog", new ItemStack(Block.LOG, 1, 2));
        registerOre("woodLog", new ItemStack(Block.LOG, 1, 3));
        registerOre("woodPlank", new ItemStack(Block.PLANKS, 1, 0));
        registerOre("woodPlank", new ItemStack(Block.PLANKS, 1, 1));
        registerOre("woodPlank", new ItemStack(Block.PLANKS, 1, 2));
        registerOre("woodPlank", new ItemStack(Block.PLANKS, 1, 3));
        registerOre("woodSlab", new ItemStack(Block.WOODEN_SLAB, 1, 0));
        registerOre("woodSlab", new ItemStack(Block.WOODEN_SLAB, 1, 1));
        registerOre("woodSlab", new ItemStack(Block.WOODEN_SLAB, 1, 2));
        registerOre("woodSlab", new ItemStack(Block.WOODEN_SLAB, 1, 3));
        registerOre("woodStair", Block.WOODEN_STAIRS);
        registerOre("woodStair", Block.BIRCH_STAIRS);
        registerOre("woodStair", Block.JUNGLE_STAIRS);
        registerOre("woodStair", Block.SPRUCE_STAIRS);
        registerOre("dyeBlack", new ItemStack(Item.DYES, 1, 0));
        registerOre("dyeRed", new ItemStack(Item.DYES, 1, 1));
        registerOre("dyeGreen", new ItemStack(Item.DYES, 1, 2));
        registerOre("dyeBrown", new ItemStack(Item.DYES, 1, 3));
        registerOre("dyeBlue", new ItemStack(Item.DYES, 1, 4));
        registerOre("dyePurple", new ItemStack(Item.DYES, 1, 5));
        registerOre("dyeCyan", new ItemStack(Item.DYES, 1, 6));
        registerOre("dyeLightGrey", new ItemStack(Item.DYES, 1, 7));
        registerOre("dyeGrey", new ItemStack(Item.DYES, 1, 8));
        registerOre("dyePink", new ItemStack(Item.DYES, 1, 9));
        registerOre("dyeLime", new ItemStack(Item.DYES, 1, 10));
        registerOre("dyeYellow", new ItemStack(Item.DYES, 1, 11));
        registerOre("dyeLightBlue", new ItemStack(Item.DYES, 1, 12));
        registerOre("dyeMagenta", new ItemStack(Item.DYES, 1, 13));
        registerOre("dyeOrange", new ItemStack(Item.DYES, 1, 14));
        registerOre("dyeWhite", new ItemStack(Item.DYES, 1, 15));
    }

    public static int getOreID(String name) {
        Integer val = (Integer)oreIDs.get(name);
        if (val == null) {
            val = maxID++;
            oreIDs.put(name, val);
            oreStacks.put(val, new ArrayList());
        }

        return val;
    }

    public static String getOreName(int id) {
        for(Map.Entry<String, Integer> entry : oreIDs.entrySet()) {
            if (id == entry.getValue()) {
                return (String)entry.getKey();
            }
        }

        return "Unknown";
    }

    public static ArrayList<ItemStack> getOres(String name) {
        return getOres(getOreID(name));
    }

    public static String[] getOreNames() {
        return (String[])oreIDs.keySet().toArray(new String[0]);
    }

    public static ArrayList<ItemStack> getOres(Integer id) {
        ArrayList<ItemStack> val = (ArrayList)oreStacks.get(id);
        if (val == null) {
            val = new ArrayList();
            oreStacks.put(id, val);
        }

        return val;
    }

    public static void registerOre(String name, Item ore) {
        registerOre(name, new ItemStack(ore));
    }

    public static void registerOre(String name, Block ore) {
        registerOre(name, new ItemStack(ore));
    }

    public static void registerOre(String name, ItemStack ore) {
        registerOre(name, getOreID(name), ore);
    }

    public static void registerOre(int id, Item ore) {
        registerOre(id, new ItemStack(ore));
    }

    public static void registerOre(int id, Block ore) {
        registerOre(id, new ItemStack(ore));
    }

    public static void registerOre(int id, ItemStack ore) {
        registerOre(getOreName(id), id, ore);
    }

    private static void registerOre(String name, int id, ItemStack ore) {
        ArrayList<ItemStack> ores = getOres(id);
        ore = ore.copy();
        ores.add(ore);
        MinecraftForge.EVENT_BUS.post(new OreDictionary.OreRegisterEvent(name, ore));
    }

    static {
        initVanillaEntries();
    }

    public static class OreRegisterEvent extends Event {
        public final String Name;
        public final ItemStack Ore;

        public OreRegisterEvent(String name, ItemStack ore) {
            this.Name = name;
            this.Ore = ore;
        }
    }
}
