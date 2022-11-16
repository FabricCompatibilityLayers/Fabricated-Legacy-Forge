package fr.catcore.fabricatedforge.forged;

import net.minecraft.world.gen.feature.OreFeature;

public class OreFeatureForged extends OreFeature {
    public OreFeatureForged(int i, int j) {
        super(i, j);
    }

    public OreFeatureForged(int id, int meta, int number) {
        this(id, number);
        this.setMinableBlockMeta(meta);
    }
}
