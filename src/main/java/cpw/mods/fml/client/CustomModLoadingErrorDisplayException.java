/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
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
import fr.catcore.fabricatedforge.forged.FatalErrorScreenForged;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.FatalErrorScreen;

@SideOnly(Side.CLIENT)
public abstract class CustomModLoadingErrorDisplayException extends RuntimeException implements IFMLHandledException {
    public CustomModLoadingErrorDisplayException() {
    }

    public abstract void initGui(FatalErrorScreenForged arg, TextRenderer arg2);

    public abstract void drawScreen(FatalErrorScreenForged arg, TextRenderer arg2, int i, int j, float f);
}
