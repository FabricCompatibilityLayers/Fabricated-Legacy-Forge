/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.common.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import cpw.mods.fml.common.*;
import fr.catcore.fabricatedforge.mixin.forgefml.block.entity.BlockEntityAccessor;
import fr.catcore.fabricatedforge.mixininterface.ILevelGeneratorType;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeDispatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.level.LevelGeneratorType;

import java.util.*;
import java.util.logging.Level;

public class GameRegistry {
    private static Multimap<ModContainer, BlockProxy> blockRegistry = ArrayListMultimap.create();
    private static Set<IWorldGenerator> worldGenerators = Sets.newHashSet();
    private static List<IFuelHandler> fuelHandlers = Lists.newArrayList();
    private static List<ICraftingHandler> craftingHandlers = Lists.newArrayList();
    private static List<IPickupNotifier> pickupHandlers = Lists.newArrayList();
    private static List<IPlayerTracker> playerTrackers = Lists.newArrayList();

    public GameRegistry() {
    }

    public static void registerWorldGenerator(IWorldGenerator generator) {
        worldGenerators.add(generator);
    }

    public static void generateWorld(int chunkX, int chunkZ, World world, ChunkProvider chunkGenerator, ChunkProvider chunkProvider) {
        long worldSeed = world.getSeed();
        Random fmlRandom = new Random(worldSeed);
        long xSeed = fmlRandom.nextLong() >> 3;
        long zSeed = fmlRandom.nextLong() >> 3;
        fmlRandom.setSeed(xSeed * (long)chunkX + zSeed * (long)chunkZ ^ worldSeed);

        for(IWorldGenerator generator : worldGenerators) {
            generator.generate(fmlRandom, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
        }
    }

    @Deprecated
    public static void registerDispenserHandler(IDispenserHandler handler) {
    }

    @Deprecated
    public static void registerDispenserHandler(IDispenseHandler handler) {
    }

    @Deprecated
    public static int tryDispense(
            World world, int x, int y, int z, int xVelocity, int zVelocity, ItemStack item, Random random, double entX, double entY, double entZ
    ) {
        return -1;
    }

    public static Object buildBlock(ModContainer container, Class<?> type, Mod.Block annotation) throws Exception {
        Object o = type.getConstructor(Integer.TYPE).newInstance(findSpareBlockId());
        registerBlock((net.minecraft.block.Block)o);
        return o;
    }

    private static int findSpareBlockId() {
        return BlockTracker.nextBlockId();
    }

    public static void registerItem(Item item, String name) {
        registerItem(item, name, null);
    }

    public static void registerItem(Item item, String name, String modId) {
        GameData.setName(item, name, modId);
    }

    @Deprecated
    public static void registerBlock(net.minecraft.block.Block block) {
        registerBlock(block, BlockItem.class);
    }

    public static void registerBlock(net.minecraft.block.Block block, String name) {
        registerBlock(block, BlockItem.class, name);
    }

    @Deprecated
    public static void registerBlock(net.minecraft.block.Block block, Class<? extends BlockItem> itemclass) {
        registerBlock(block, itemclass, null);
    }

    public static void registerBlock(net.minecraft.block.Block block, Class<? extends BlockItem> itemclass, String name) {
        registerBlock(block, itemclass, name, null);
    }

    public static void registerBlock(net.minecraft.block.Block block, Class<? extends BlockItem> itemclass, String name, String modId) {
        if (Loader.instance().isInState(LoaderState.CONSTRUCTING)) {
            FMLLog.warning(
                    "The mod %s is attempting to register a block whilst it it being constructed. This is bad modding practice - please use a proper mod lifecycle event.",
                    new Object[]{Loader.instance().activeModContainer()}
            );
        }

        try {
            assert block != null : "registerBlock: block cannot be null";

            assert itemclass != null : "registerBlock: itemclass cannot be null";

            int blockItemId = block.id - 256;
            Item i = (Item)itemclass.getConstructor(Integer.TYPE).newInstance(blockItemId);
            registerItem(i, name, modId);
        } catch (Exception var6) {
            FMLLog.log(Level.SEVERE, var6, "Caught an exception during block registration", new Object[0]);
            throw new LoaderException(var6);
        }

        blockRegistry.put(Loader.instance().activeModContainer(), (BlockProxy)block);
    }

    public static void addRecipe(ItemStack output, Object... params) {
        addShapedRecipe(output, params);
    }

    public static RecipeType addShapedRecipe(ItemStack output, Object... params) {
        return RecipeDispatcher.getInstance().registerShapedRecipe(output, params);
    }

    public static void addShapelessRecipe(ItemStack output, Object... params) {
        RecipeDispatcher.getInstance().registerShapelessRecipe(output, params);
    }

    public static void addRecipe(RecipeType recipe) {
        RecipeDispatcher.getInstance().getAllRecipes().add(recipe);
    }

    public static void addSmelting(int input, ItemStack output, float xp) {
        SmeltingRecipeRegistry.getInstance().method_3488(input, output, xp);
    }

    public static void registerTileEntity(Class<? extends BlockEntity> tileEntityClass, String id) {
        BlockEntity.registerBlockEntity(tileEntityClass, id);
    }

    public static void registerTileEntityWithAlternatives(Class<? extends BlockEntity> tileEntityClass, String id, String... alternatives) {
        BlockEntity.registerBlockEntity(tileEntityClass, id);
        Map<String, Class> teMappings = BlockEntityAccessor.getStringClassMap();

        for(String s : alternatives) {
            if (!teMappings.containsKey(s)) {
                teMappings.put(s, tileEntityClass);
            }
        }
    }

    public static void addBiome(Biome biome) {
        ((ILevelGeneratorType)LevelGeneratorType.DEFAULT).addNewBiome(biome);
    }

    public static void removeBiome(Biome biome) {
        ((ILevelGeneratorType)LevelGeneratorType.DEFAULT).removeBiome(biome);
    }

    public static void registerFuelHandler(IFuelHandler handler) {
        fuelHandlers.add(handler);
    }

    public static int getFuelValue(ItemStack itemStack) {
        int fuelValue = 0;

        for(IFuelHandler handler : fuelHandlers) {
            fuelValue = Math.max(fuelValue, handler.getBurnTime(itemStack));
        }

        return fuelValue;
    }

    public static void registerCraftingHandler(ICraftingHandler handler) {
        craftingHandlers.add(handler);
    }

    public static void onItemCrafted(PlayerEntity player, ItemStack item, Inventory craftMatrix) {
        for(ICraftingHandler handler : craftingHandlers) {
            handler.onCrafting(player, item, craftMatrix);
        }
    }

    public static void onItemSmelted(PlayerEntity player, ItemStack item) {
        for(ICraftingHandler handler : craftingHandlers) {
            handler.onSmelting(player, item);
        }
    }

    public static void registerPickupHandler(IPickupNotifier handler) {
        pickupHandlers.add(handler);
    }

    public static void onPickupNotification(PlayerEntity player, ItemEntity item) {
        for(IPickupNotifier notify : pickupHandlers) {
            notify.notifyPickup(item, player);
        }
    }

    public static void registerPlayerTracker(IPlayerTracker tracker) {
        playerTrackers.add(tracker);
    }

    public static void onPlayerLogin(PlayerEntity player) {
        for(IPlayerTracker tracker : playerTrackers) {
            tracker.onPlayerLogin(player);
        }
    }

    public static void onPlayerLogout(PlayerEntity player) {
        for(IPlayerTracker tracker : playerTrackers) {
            tracker.onPlayerLogout(player);
        }
    }

    public static void onPlayerChangedDimension(PlayerEntity player) {
        for(IPlayerTracker tracker : playerTrackers) {
            tracker.onPlayerChangedDimension(player);
        }
    }

    public static void onPlayerRespawn(PlayerEntity player) {
        for(IPlayerTracker tracker : playerTrackers) {
            tracker.onPlayerRespawn(player);
        }
    }
}
