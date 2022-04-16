package fr.catcore.fabricatedmodloader.utils;

import java.io.File;

public class MLModEntry {
    public final String modName;
    public final String modId;
    public final String initClass;
    public final File file;
    public final File original;

    protected MLModEntry(String modName, String modId, String initClass, File file, File original) {
        this.modName = modName;
        this.modId = modId;
        this.initClass = initClass;
        this.file = file;
        this.original = original;
    }
}
