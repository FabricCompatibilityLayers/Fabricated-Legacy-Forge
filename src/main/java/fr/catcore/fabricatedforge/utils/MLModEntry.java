package fr.catcore.fabricatedforge.utils;

import net.fabricmc.loader.impl.metadata.LoaderModMetadata;

import java.io.File;

public class MLModEntry extends ModEntry {
    public final String initClass;

    protected MLModEntry(String modName, String modId, String initClass, File file, File original) {
        super(modName, modId, file, original);
        this.initClass = initClass;
    }

    @Override
    LoaderModMetadata createModMetadata() {
        return new MLModMetadata(modId, modName);
    }

    @Override
    String getType() {
        return "ModLoader";
    }
}
