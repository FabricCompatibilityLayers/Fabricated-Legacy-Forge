package net.minecraftforge.common;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.EventBus;

import java.util.Arrays;
import java.util.List;

public class MinecraftForge {
    public static final EventBus EVENT_BUS = new EventBus();
    public static boolean SPAWNER_ALLOW_ON_INVERTED = false;
    private static final ForgeInternalHandler INTERNAL_HANDLER = new ForgeInternalHandler();

    public MinecraftForge() {
    }

    public static void addGrassPlant(Block block, int metadata, int weight) {
        ForgeHooks.grassList.add(new ForgeHooks.GrassEntry(block, metadata, weight));
    }

    public static void addGrassSeed(ItemStack seed, int weight) {
        ForgeHooks.seedList.add(new ForgeHooks.SeedEntry(seed, weight));
    }

    public static void setToolClass(Item tool, String toolClass, int harvestLevel) {
        ForgeHooks.toolClasses.put(tool, Arrays.asList(toolClass, harvestLevel));
    }

    public static void setBlockHarvestLevel(Block block, int metadata, String toolClass, int harvestLevel) {
        List key = Arrays.asList(block, metadata, toolClass);
        ForgeHooks.toolHarvestLevels.put(key, harvestLevel);
        ForgeHooks.toolEffectiveness.add(key);
    }

    public static void removeBlockEffectiveness(Block block, int metadata, String toolClass) {
        List key = Arrays.asList(block, metadata, toolClass);
        ForgeHooks.toolEffectiveness.remove(key);
    }

    public static void setBlockHarvestLevel(Block block, String toolClass, int harvestLevel) {
        for(int metadata = 0; metadata < 16; ++metadata) {
            List key = Arrays.asList(block, metadata, toolClass);
            ForgeHooks.toolHarvestLevels.put(key, harvestLevel);
            ForgeHooks.toolEffectiveness.add(key);
        }
    }

    public static int getBlockHarvestLevel(Block block, int metadata, String toolClass) {
        ForgeHooks.initTools();
        List key = Arrays.asList(block, metadata, toolClass);
        Integer harvestLevel = (Integer)ForgeHooks.toolHarvestLevels.get(key);
        return harvestLevel == null ? -1 : harvestLevel;
    }

    public static void removeBlockEffectiveness(Block block, String toolClass) {
        for(int metadata = 0; metadata < 16; ++metadata) {
            List key = Arrays.asList(block, metadata, toolClass);
            ForgeHooks.toolEffectiveness.remove(key);
        }
    }

    public static void initialize() {
        System.out.printf("MinecraftForge v%s Initialized\n", ForgeVersion.getVersion());
        FMLLog.info("MinecraftForge v%s Initialized", new Object[]{ForgeVersion.getVersion()});
        Block filler = new Block(0, Material.AIR);
        Block.BLOCKS[0] = null;
        Block.field_493[0] = false;
        Block.field_494[0] = 0;

        for(int x = 256; x < 4096; ++x) {
            if (Item.ITEMS[x] != null) {
                Block.BLOCKS[x] = filler;
            }
        }

        boolean[] temp = new boolean[4096];

        for(int x = 0; x < EndermanEntity.field_3919.length; ++x) {
            temp[x] = EndermanEntity.field_3919[x];
        }

        EndermanEntity.field_3919 = temp;
        EVENT_BUS.register(INTERNAL_HANDLER);
    }

    public static String getBrandingVersion() {
        return "Minecraft Forge " + ForgeVersion.getVersion();
    }
}
