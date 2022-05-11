package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.mob.MobEntity;

public interface IWorldRenderer {

    public void drawBlockDamageTexture(Tessellator par1Tessellator, MobEntity par2EntityPlayer, float par3);
}
