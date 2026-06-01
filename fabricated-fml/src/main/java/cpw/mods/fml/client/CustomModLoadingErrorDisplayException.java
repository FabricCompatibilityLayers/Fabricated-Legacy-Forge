/*
 * The FML Forge Mod Loader suite. Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.client;

import cpw.mods.fml.common.IFMLHandledException;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import io.github.fabriccompatibilitylayers.fabricatedfml.forged.ForgedGuiErrorScreen;
import net.minecraft.src.FontRenderer;

/**
 * If a mod throws this exception during loading, it will be called back to render
 * the error screen through the methods below. This error will not be cleared, and will
 * not allow the game to carry on, but might be useful if your mod wishes to report
 * a fatal configuration error in a pretty way.
 *
 * Throw this through a proxy. It won't work on the dedicated server environment.
 * @author cpw
 *
 */
@SideOnly(Side.CLIENT)
public abstract class CustomModLoadingErrorDisplayException extends RuntimeException implements IFMLHandledException
{
    /**
     * Called after the GUI is inited by the parent code. You can do extra stuff here, maybe?
     *
     * @param errorScreen The error screen we're painting
     * @param fontRenderer A font renderer for you
     */
    public abstract void initGui(ForgedGuiErrorScreen errorScreen, FontRenderer fontRenderer);

    /**
     * Draw your error to the screen.
     *
     * <br/><em>Warning: Minecraft is in a deep error state.</em> <strong>All</strong> it can do is stop.
     * Do not try and do anything involving complex user interaction here.
     *
     * @param errorScreen The error screen to draw to
     * @param fontRenderer A font renderer for you
     * @param mouseRelX Mouse X
     * @param mouseRelY Mouse Y
     * @param tickTime tick time
     */
    public abstract void drawScreen(ForgedGuiErrorScreen errorScreen, FontRenderer fontRenderer, int mouseRelX, int mouseRelY, float tickTime);
}
