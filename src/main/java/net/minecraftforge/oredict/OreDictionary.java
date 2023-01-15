package net.minecraftforge.oredict;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeDispatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipeType;
import net.minecraft.recipe.ShapelessRecipeType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;

import java.util.*;

public class OreDictionary {
    private static int maxID = 0;
    private static HashMap<String, Integer> oreIDs = new HashMap();
    private static HashMap<Integer, ArrayList<ItemStack>> oreStacks = new HashMap();

    public OreDictionary() {
    }

    public static void initVanillaEntries() {
        registerOre("logWood", new ItemStack(Block.LOG, 1, -1));
        registerOre("plankWood", new ItemStack(Block.PLANKS, 1, -1));
        registerOre("slabWood", new ItemStack(Block.WOODEN_SLAB, 1, -1));
        registerOre("stairWood", Block.WOODEN_STAIRS);
        registerOre("stairWood", Block.BIRCH_STAIRS);
        registerOre("stairWood", Block.JUNGLE_STAIRS);
        registerOre("stairWood", Block.SPRUCE_STAIRS);
        registerOre("stickWood", Item.STICK);
        Map<ItemStack, String> replacements = new HashMap();
        replacements.put(new ItemStack(Block.PLANKS, 1, -1), "plankWood");
        replacements.put(new ItemStack(Item.STICK), "stickWood");
        String[] dyes = new String[]{
                "dyeBlack",
                "dyeRed",
                "dyeGreen",
                "dyeBrown",
                "dyeBlue",
                "dyePurple",
                "dyeCyan",
                "dyeLightGray",
                "dyeGray",
                "dyePink",
                "dyeLime",
                "dyeYellow",
                "dyeLightBlue",
                "dyeMagenta",
                "dyeOrange",
                "dyeWhite"
        };

        for(int i = 0; i < 16; ++i) {
            ItemStack dye = new ItemStack(Item.DYES, 1, i);
            registerOre(dyes[i], dye);
            replacements.put(dye, dyes[i]);
        }

        ItemStack[] replaceStacks = (ItemStack[])replacements.keySet().toArray(new ItemStack[0]);
        ItemStack[] exclusions = new ItemStack[]{new ItemStack(Block.LAPIS_BLOCK), new ItemStack(Item.COOKIE)};
        List recipes = RecipeDispatcher.getInstance().getAllRecipes();
        List<RecipeType> recipesToRemove = new ArrayList();
        List<RecipeType> recipesToAdd = new ArrayList();

        for(Object obj : recipes) {
            if (obj instanceof ShapedRecipeType) {
                ShapedRecipeType recipe = (ShapedRecipeType)obj;
                ItemStack output = recipe.getOutput();
                if ((output == null || !containsMatch(false, exclusions, output)) && containsMatch(true, recipe.ingredients, replaceStacks)) {
                    recipesToRemove.add(recipe);
                    recipesToAdd.add(new ShapedOreRecipe(recipe, replacements));
                }
            } else if (obj instanceof ShapelessRecipeType) {
                ShapelessRecipeType recipe = (ShapelessRecipeType)obj;
                ItemStack output = recipe.getOutput();
                if ((output == null || !containsMatch(false, exclusions, output))
                        && containsMatch(true, (ItemStack[])recipe.stacks.toArray(new ItemStack[0]), replaceStacks)) {
                    recipesToRemove.add((RecipeType)obj);
                    RecipeType newRecipe = new ShapelessOreRecipe(recipe, replacements);
                    recipesToAdd.add(newRecipe);
                }
            }
        }

        recipes.removeAll(recipesToRemove);
        recipes.addAll(recipesToAdd);
        System.out.println(recipesToRemove.size() + " " + recipesToAdd.size());
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

    private static boolean containsMatch(boolean strict, ItemStack[] inputs, ItemStack... targets) {
        for(ItemStack input : inputs) {
            for(ItemStack target : targets) {
                if (itemMatches(target, input, strict)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean itemMatches(ItemStack target, ItemStack input, boolean strict) {
        if ((input != null || target == null) && (input == null || target != null)) {
            return target.id == input.id && (target.getData() == -1 && !strict || target.getData() == input.getData());
        } else {
            return false;
        }
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
