package cpw.mods.fml.common;

import com.google.common.collect.SetMultimap;

import java.io.File;

public class DuplicateModsFoundException extends LoaderException {
    public SetMultimap<ModContainer, File> dupes;

    public DuplicateModsFoundException(SetMultimap<ModContainer, File> dupes) {
        this.dupes = dupes;
    }
}
