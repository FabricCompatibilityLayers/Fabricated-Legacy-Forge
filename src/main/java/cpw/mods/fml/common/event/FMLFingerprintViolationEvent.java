package cpw.mods.fml.common.event;

import com.google.common.collect.ImmutableSet;

import java.io.File;
import java.util.Set;

public class FMLFingerprintViolationEvent extends FMLEvent {
    public final boolean isDirectory;
    public final Set<String> fingerprints;
    public final File source;
    public final String expectedFingerprint;

    public FMLFingerprintViolationEvent(boolean isDirectory, File source, ImmutableSet<String> fingerprints, String expectedFingerprint) {
        this.isDirectory = isDirectory;
        this.source = source;
        this.fingerprints = fingerprints;
        this.expectedFingerprint = expectedFingerprint;
    }
}
