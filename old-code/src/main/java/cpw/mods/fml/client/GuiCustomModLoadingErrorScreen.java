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

import fr.catcore.fabricatedforge.forged.FatalErrorScreenForged;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public class GuiCustomModLoadingErrorScreen extends FatalErrorScreenForged {
    private CustomModLoadingErrorDisplayException customException;

    public GuiCustomModLoadingErrorScreen(CustomModLoadingErrorDisplayException customException) {
        this.customException = customException;
    }

    public void init() {
        super.init();
        this.customException.initGui(this, this.textRenderer);
    }

    public void render(int par1, int par2, float par3) {
        this.renderBackground();
        this.customException.drawScreen(this, this.textRenderer, par1, par2, par3);
    }
}
