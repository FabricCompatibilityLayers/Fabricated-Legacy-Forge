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

import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;

public class GameData {
    private static Map<Integer, ItemData> idMap = Maps.newHashMap();
    private static CountDownLatch serverValidationLatch;
    private static CountDownLatch clientValidationLatch;
    private static MapDifference<Integer, ItemData> difference;
    private static boolean shouldContinue = true;
    private static boolean isSaveValid = true;
    private static Map<String, String> ignoredMods;

    public GameData() {
    }

    private static boolean isModIgnoredForIdValidation(String modId) {
        if (ignoredMods == null) {
            File f = new File(Loader.instance().getConfigDir(), "fmlIDChecking.properties");
            if (f.exists()) {
                Properties p = new Properties();

                try {
                    p.load(new FileInputStream(f));
                    ignoredMods = Maps.fromProperties(p);
                    if (ignoredMods.size() > 0) {
                        FMLLog.log("fml.ItemTracker", Level.WARNING, "Using non-empty ignored mods configuration file %s", new Object[]{ignoredMods.keySet()});
                    }
                } catch (Exception var4) {
                    Throwables.propagateIfPossible(var4);
                    FMLLog.log("fml.ItemTracker", Level.SEVERE, var4, "Failed to read ignored ID checker mods properties file", new Object[0]);
                    ignoredMods = ImmutableMap.of();
                }
            } else {
                ignoredMods = ImmutableMap.of();
            }
        }

        return ignoredMods.containsKey(modId);
    }

    public static void newItemAdded(Item item) {
        ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null) {
            mc = Loader.instance().getMinecraftModContainer();
            if (Loader.instance().hasReachedState(LoaderState.AVAILABLE)) {
                FMLLog.severe(
                        "It appears something has tried to allocate an Item outside of the initialization phase of Minecraft, this could be very bad for your network connectivity.",
                        new Object[0]
                );
            }
        }

        String itemType = item.getClass().getName();
        ItemData itemData = new ItemData(item, mc);
        if (idMap.containsKey(item.id)) {
            ItemData id = (ItemData)idMap.get(item.id);
            FMLLog.log(
                    "fml.ItemTracker",
                    Level.INFO,
                    "The mod %s is overwriting existing item at %d (%s from %s) with %s",
                    new Object[]{mc.getModId(), id.getItemId(), id.getItemType(), id.getModId(), itemType}
            );
        }

        idMap.put(item.id, itemData);
        if (!"Minecraft".equals(mc.getModId())) {
            FMLLog.log("fml.ItemTracker", Level.FINE, "Adding item %s(%d) owned by %s", new Object[]{item.getClass().getName(), item.id, mc.getModId()});
        }
    }

    public static void validateWorldSave(Set<ItemData> worldSaveItems) {
        isSaveValid = true;
        shouldContinue = true;
        if (worldSaveItems == null) {
            serverValidationLatch.countDown();

            try {
                clientValidationLatch.await();
            } catch (InterruptedException var6) {
            }
        } else {
            Function<? super ItemData, Integer> idMapFunction = new Function<ItemData, Integer>() {
                public Integer apply(ItemData input) {
                    return input.getItemId();
                }
            };
            Map<Integer, ItemData> worldMap = Maps.uniqueIndex(worldSaveItems, idMapFunction);
            difference = Maps.difference(worldMap, idMap);
            FMLLog.log("fml.ItemTracker", Level.FINE, "The difference set is %s", new Object[]{difference});
            if (difference.entriesDiffering().isEmpty() && difference.entriesOnlyOnLeft().isEmpty()) {
                isSaveValid = true;
                serverValidationLatch.countDown();
            } else {
                FMLLog.log("fml.ItemTracker", Level.SEVERE, "FML has detected item discrepancies", new Object[0]);
                FMLLog.log("fml.ItemTracker", Level.SEVERE, "Missing items : %s", new Object[]{difference.entriesOnlyOnLeft()});
                FMLLog.log("fml.ItemTracker", Level.SEVERE, "Mismatched items : %s", new Object[]{difference.entriesDiffering()});
                boolean foundNonIgnored = false;

                for(ItemData diff : difference.entriesOnlyOnLeft().values()) {
                    if (!isModIgnoredForIdValidation(diff.getModId())) {
                        foundNonIgnored = true;
                    }
                }

                for(MapDifference.ValueDifference<ItemData> diff : difference.entriesDiffering().values()) {
                    if (!isModIgnoredForIdValidation(((ItemData)diff.leftValue()).getModId())
                            && !isModIgnoredForIdValidation(((ItemData)diff.rightValue()).getModId())) {
                        foundNonIgnored = true;
                    }
                }

                if (!foundNonIgnored) {
                    FMLLog.log(
                            "fml.ItemTracker",
                            Level.SEVERE,
                            "FML is ignoring these ID discrepancies because of configuration. YOUR GAME WILL NOW PROBABLY CRASH. HOPEFULLY YOU WON'T HAVE CORRUPTED YOUR WORLD. BLAME %s",
                            new Object[]{ignoredMods.keySet()}
                    );
                }

                isSaveValid = !foundNonIgnored;
                serverValidationLatch.countDown();
            }

            try {
                clientValidationLatch.await();
                if (!shouldContinue) {
                    throw new RuntimeException("This server instance is going to stop abnormally because of a fatal ID mismatch");
                }
            } catch (InterruptedException var7) {
            }
        }
    }

    public static void writeItemData(NbtList itemList) {
        for(ItemData dat : idMap.values()) {
            itemList.method_1217(dat.toNBT());
        }
    }

    public static void initializeServerGate(int gateCount) {
        serverValidationLatch = new CountDownLatch(gateCount - 1);
        clientValidationLatch = new CountDownLatch(gateCount - 1);
    }

    public static MapDifference<Integer, ItemData> gateWorldLoadingForValidation() {
        try {
            serverValidationLatch.await();
            if (!isSaveValid) {
                return difference;
            }
        } catch (InterruptedException var1) {
        }

        difference = null;
        return null;
    }

    public static void releaseGate(boolean carryOn) {
        shouldContinue = carryOn;
        clientValidationLatch.countDown();
    }

    public static Set<ItemData> buildWorldItemData(NbtList modList) {
        Set<ItemData> worldSaveItems = Sets.newHashSet();

        for(int i = 0; i < modList.size(); ++i) {
            NbtCompound mod = (NbtCompound)modList.method_1218(i);
            ItemData dat = new ItemData(mod);
            worldSaveItems.add(dat);
        }

        return worldSaveItems;
    }

    static void setName(Item item, String name, String modId) {
        int id = item.id;
        ItemData itemData = (ItemData)idMap.get(id);
        itemData.setName(name, modId);
    }
}
