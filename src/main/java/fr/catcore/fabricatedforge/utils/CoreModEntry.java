package fr.catcore.fabricatedforge.utils;

import net.fabricmc.loader.impl.metadata.LoaderModMetadata;

import java.io.File;

public class CoreModEntry extends ModEntry{
    private static int num = 0;

    protected CoreModEntry(String modName, File file, File original) {
        super(modName, "coremod" + (num++), file, original);
    }

    @Override
    LoaderModMetadata createModMetadata() {
        return null;
    }

    @Override
    String getType() {
        return "Core Mod";
    }
}
