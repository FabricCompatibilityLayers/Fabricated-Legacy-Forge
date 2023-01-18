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

import cpw.mods.fml.common.DuplicateModsFoundException;
import cpw.mods.fml.common.ModContainer;
import fr.catcore.fabricatedforge.forged.FatalErrorScreenForged;
import net.minecraft.client.gui.screen.FatalErrorScreen;

import java.io.File;
import java.util.Map;

public class GuiDupesFound extends FatalErrorScreenForged {
    private DuplicateModsFoundException dupes;

    public GuiDupesFound(DuplicateModsFoundException dupes) {
        this.dupes = dupes;
    }

    public void init() {
        super.init();
    }

    public void render(int par1, int par2, float par3) {
        this.renderBackground();
        int offset = Math.max(85 - this.dupes.dupes.size() * 10, 10);
        this.drawCenteredString(this.textRenderer, "Forge Mod Loader has found a problem with your minecraft installation", this.width / 2, offset, 16777215);
        offset += 10;
        this.drawCenteredString(this.textRenderer, "You have mod sources that are duplicate within your system", this.width / 2, offset, 16777215);
        offset += 10;
        this.drawCenteredString(this.textRenderer, "Mod Id : File name", this.width / 2, offset, 16777215);
        offset += 5;

        for(Map.Entry<ModContainer, File> mc : this.dupes.dupes.entries()) {
            offset += 10;
            this.drawCenteredString(
                    this.textRenderer,
                    String.format("%s : %s", ((ModContainer)mc.getKey()).getModId(), ((File)mc.getValue()).getName()),
                    this.width / 2,
                    offset,
                    15658734
            );
        }
    }
}
