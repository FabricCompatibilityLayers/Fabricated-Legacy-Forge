/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.level.LevelProperties;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

public class ForgeDummyContainer extends DummyModContainer implements WorldAccessContainer {
    public static int clumpingThreshold = 64;

    public ForgeDummyContainer() {
        super(new ModMetadata());
        ModMetadata meta = this.getMetadata();
        meta.modId = "Forge";
        meta.name = "Minecraft Forge";
        meta.version = ForgeVersion.getVersion();
        meta.credits = "Made possible with help from many people";
        meta.authorList = Arrays.asList("LexManos", "Eloraam", "Spacetoad");
        meta.description = "Minecraft Forge is a common open source API allowing a broad range of mods to work cooperatively together. It allows many mods to be created without them editing the main Minecraft code.";
        meta.url = "http://MinecraftForge.net";
        meta.updateUrl = "http://MinecraftForge.net/forum/index.php/topic,5.0.html";
        meta.screenshots = new String[0];
        meta.logoFile = "/forge_logo.png";
        Configuration config = new Configuration(new File(Loader.instance().getConfigDir(), "forge.cfg"));
        if (!config.isChild) {
            config.load();
            Property enableGlobalCfg = config.get("general", "enableGlobalConfig", false);
            if (enableGlobalCfg.getBoolean(false)) {
                Configuration.enableGlobalConfig();
            }
        }

        Property clumpingThresholdProperty = config.get("general", "clumpingThreshold", 64);
        clumpingThresholdProperty.comment = "Controls the number threshold at which Packet51 is preferred over Packet52, default and minimum 64, maximum 1024";
        clumpingThreshold = clumpingThresholdProperty.getInt(64);
        if (clumpingThreshold > 1024 || clumpingThreshold < 64) {
            clumpingThreshold = 64;
            clumpingThresholdProperty.value = "64";
        }

        config.save();
    }

    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent evt) {
        ForgeChunkManager.captureConfig(evt.getModConfigurationDirectory());
    }

    @Subscribe
    public void postInit(FMLPostInitializationEvent evt) {
        ForgeChunkManager.loadConfiguration();
    }

    public NbtCompound getDataForWriting(WorldSaveHandler handler, LevelProperties info) {
        NbtCompound forgeData = new NbtCompound();
        NbtCompound dimData = DimensionManager.saveDimensionDataMap();
        forgeData.put("DimensionData", dimData);
        return forgeData;
    }

    public void readData(WorldSaveHandler handler, LevelProperties info, Map<String, NbtElement> propertyMap, NbtCompound tag) {
        if (tag.contains("DimensionData")) {
            DimensionManager.loadDimensionDataMap(tag.contains("DimensionData") ? tag.getCompound("DimensionData") : null);
        }
    }
}
