package net.minecraftforge.client;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;

public abstract class SkyProvider {
    public SkyProvider() {
    }

    @SideOnly(Side.CLIENT)
    @Environment(EnvType.CLIENT)
    public abstract void render(float f, ClientWorld arg, Minecraft minecraft);
}
