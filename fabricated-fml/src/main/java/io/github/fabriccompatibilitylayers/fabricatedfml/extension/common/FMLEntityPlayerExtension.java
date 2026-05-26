package io.github.fabriccompatibilitylayers.fabricatedfml.extension.common;

import net.minecraft.src.World;

public interface FMLEntityPlayerExtension {
    void openGui(Object mod, int modGuiId, World world, int x, int y, int z);
}
