/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.client;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;

public abstract class SkyProvider {
    public SkyProvider() {
    }

    @SideOnly(Side.CLIENT)
    public abstract void render(float f, ClientWorld arg, Minecraft minecraft);
}
