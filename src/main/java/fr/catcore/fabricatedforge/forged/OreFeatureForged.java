package fr.catcore.fabricatedforge.forged;

import fr.catcore.fabricatedforge.mixininterface.IOreFeature;
import net.minecraft.world.gen.feature.OreFeature;

public class OreFeatureForged extends OreFeature {
    public OreFeatureForged(int i, int j) {
        super(i, j);
    }

    public OreFeatureForged(int id, int meta, int number) {
        this(id, number);
        ((IOreFeature)this).setMinableBlockMeta(meta);
    }
}
