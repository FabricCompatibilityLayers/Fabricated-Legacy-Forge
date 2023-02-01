package net.minecraftforge.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;

public abstract class IRenderHandler {
    public IRenderHandler() {
    }

    @SideOnly(Side.CLIENT)
    public abstract void render(float f, ClientWorld arg, Minecraft minecraft);
}
