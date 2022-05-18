package fr.catcore.fabricatedforge.utils;

import net.fabricmc.loader.impl.metadata.LoaderModMetadata;

import java.io.File;

public abstract class ModEntry {
    public final String modName;
    public final String modId;

    public final File file;
    public final File original;

    protected ModEntry(String modName, String modId, File file, File original) {
        this.modName = modName;
        this.modId = modId;
        this.file = file;
        this.original = original;
    }

    abstract LoaderModMetadata createModMetadata();
}
