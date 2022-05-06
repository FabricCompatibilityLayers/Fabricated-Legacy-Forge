package fr.catcore.fabricatedforge.utils;

import com.google.common.collect.Maps;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.impl.ModContainerImpl;
import net.fabricmc.loader.impl.discovery.ModCandidate;
import net.fabricmc.loader.impl.game.GameProvider;
import net.fabricmc.loader.impl.util.log.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FakeModManager {

    private static Method createBuiltin;

    private static final List<MLModEntry> MODS = new ArrayList<>();

    private static boolean loaded = false;
    private static boolean loading = false;

    public static void init() {
        if (!loaded && !loading) {
            loading = true;
            try {
                createBuiltin = ModCandidate.class.getDeclaredMethod("createBuiltin", GameProvider.BuiltinMod.class);
                createBuiltin.setAccessible(true);

                MLModDiscoverer.init();
                loading = false;
                loaded = true;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected static void addModEntry(MLModEntry modEntry) {
        try {
            ModCandidate candidate = (ModCandidate) createBuiltin.invoke(null, new GameProvider.BuiltinMod(Collections.emptyList(), new MLModMetadata(modEntry.modId, modEntry.modName)));
            ModContainer container = new ModContainerImpl(candidate);

            FabricLoader loader = FabricLoader.getInstance();

            getModList(loader).add(container);
            getModMap(loader).put(modEntry.modId, container);

            MODS.add(modEntry);
            Log.info(Constants.LOG_CATEGORY, "Added ModLoader mod " + modEntry.modName + " to mod list.");
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<MLModEntry> getMods() {
        return MODS;
    }

    /**
     * Get mod map from Fabric loader with reflections.
     * If fails it will return empty map.
     *
     * @param loader {@link FabricLoader} loader instance.
     * @return {@link Map} of {@link String} key and {@link ModContainer} value.
     */
    @SuppressWarnings("unchecked")
    private static Map<String, ModContainer> getModMap(FabricLoader loader) {
        try {
            Field field = loader.getClass().getDeclaredField("modMap");
            field.setAccessible(true);
            return (Map<String, ModContainer>) field.get(loader);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return Maps.newHashMap();
        }
    }

    /**
     * Get mod list from Fabric loader with reflections.
     * If fails it will return empty list.
     *
     * @param loader {@link FabricLoader} loader instance.
     * @return {@link List} of {@link ModContainer}
     */
    @SuppressWarnings("unchecked")
    private static List<ModContainer> getModList(FabricLoader loader) {
        try {
            Field field = loader.getClass().getDeclaredField("mods");
            field.setAccessible(true);
            return (List<ModContainer>) field.get(loader);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }
}
