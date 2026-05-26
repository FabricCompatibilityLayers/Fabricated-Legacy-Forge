package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.FMLEntityPlayerExtension;
import net.minecraft.src.Block;

public interface EntityPlayerExtension extends EntityLivingExtension, FMLEntityPlayerExtension {
    float getCurrentPlayerStrVsBlock(Block par1Block, int meta);
}
