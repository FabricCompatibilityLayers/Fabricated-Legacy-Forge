package cpw.mods.fml.common;

import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;

public class MinecraftDummyContainer extends DummyModContainer {
    private VersionRange staticRange;

    public MinecraftDummyContainer(String actualMCVersion) {
        super(new ModMetadata());
        this.getMetadata().modId = "Minecraft";
        this.getMetadata().name = "Minecraft";
        this.getMetadata().version = actualMCVersion;
        this.staticRange = VersionParser.parseRange("[" + actualMCVersion + "]");
    }

    public VersionRange getStaticVersionRange() {
        return this.staticRange;
    }
}
