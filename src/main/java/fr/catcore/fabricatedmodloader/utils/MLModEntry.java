package fr.catcore.fabricatedmodloader.utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MLModEntry {
    public final String modName;
    public final String modId;
    public final String initClass;
    public final File file;
    public final File original;
    public final Map<SoundType, List<String>> sounds;

    protected MLModEntry(String modName, String modId, String initClass, File file, File original, Map<SoundType, List<String>> sounds) {
        this.modName = modName;
        this.modId = modId;
        this.initClass = initClass;
        this.file = file;
        this.original = original;
        this.sounds = sounds;
    }

    protected MLModEntry(String modName, String modId, String initClass, File file, File original) {
        this.modName = modName;
        this.modId = modId;
        this.initClass = initClass;
        this.file = file;
        this.original = original;
        this.sounds = new HashMap<>();
    }

    public enum SoundType {
        sound,
        streaming,
        music
    }
}
