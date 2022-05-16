package fr.catcore.fabricatedforge.mixininterface;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface IWorldView {
    @Environment(EnvType.CLIENT)
    int getMaxBuildHeight();
}
