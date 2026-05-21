package io.github.fabriccompatibilitylayers.fabricatedforge.extension.client;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.Tessellator;

public interface RenderGlobalExtension {
    void drawBlockDamageTexture(Tessellator par1Tessellator, EntityLiving par2EntityLiving, float par3);
}