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

import cpw.mods.fml.common.network.ModMissingPacket;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.util.Language;

import java.util.Iterator;

public class GuiModsMissingForServer extends Screen {
    private ModMissingPacket modsMissing;

    public GuiModsMissingForServer(ModMissingPacket modsMissing) {
        this.modsMissing = modsMissing;
    }

    public void init() {
        Language translations = Language.getInstance();
        this.buttons.add(new OptionButtonWidget(1, this.width / 2 - 75, this.height - 38, translations.translate("gui.done")));
    }

    protected void buttonClicked(ButtonWidget par1GuiButton) {
        if (par1GuiButton.active && par1GuiButton.id == 1) {
            FMLClientHandler.instance().getClient().openScreen(null);
        }
    }

    public void render(int par1, int par2, float par3) {
        this.renderBackground();
        int offset = Math.max(85 - this.modsMissing.getModList().size() * 10, 10);
        this.drawCenteredString(this.textRenderer, "Forge Mod Loader could not connect to this server", this.width / 2, offset, 16777215);
        offset += 10;
        this.drawCenteredString(this.textRenderer, "The mods and versions listed below could not be found", this.width / 2, offset, 16777215);
        offset += 10;
        this.drawCenteredString(this.textRenderer, "They are required to play on this server", this.width / 2, offset, 16777215);
        offset += 5;

        for(ArtifactVersion v : this.modsMissing.getModList()) {
            offset += 10;
            this.drawCenteredString(this.textRenderer, String.format("%s : %s", v.getLabel(), v.getRangeString()), this.width / 2, offset, 15658734);
        }

        super.render(par1, par2, par3);
    }
}
