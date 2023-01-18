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

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.client.render.Tessellator;

import java.util.ArrayList;

public class GuiSlotModList extends GuiScrollingList {
    private GuiModList parent;
    private ArrayList<ModContainer> mods;

    public GuiSlotModList(GuiModList parent, ArrayList<ModContainer> mods, int listWidth) {
        super(parent.getMinecraftInstance(), listWidth, parent.height, 32, parent.height - 65 + 4, 10, 35);
        this.parent = parent;
        this.mods = mods;
    }

    protected int getSize() {
        return this.mods.size();
    }

    protected void elementClicked(int var1, boolean var2) {
        this.parent.selectModIndex(var1);
    }

    protected boolean isSelected(int var1) {
        return this.parent.modIndexSelected(var1);
    }

    protected void drawBackground() {
        this.parent.renderBackground();
    }

    protected int getContentHeight() {
        return this.getSize() * 35 + 1;
    }

    protected void drawSlot(int listIndex, int var2, int var3, int var4, Tessellator var5) {
        ModContainer mc = (ModContainer)this.mods.get(listIndex);
        if (Loader.instance().getModState(mc) == LoaderState.ModState.DISABLED) {
            this.parent
                    .getFontRenderer()
                    .method_4247(this.parent.getFontRenderer().trimToWidth(mc.getName(), this.listWidth - 10), this.left + 3, var3 + 2, 16720418);
            this.parent
                    .getFontRenderer()
                    .method_4247(this.parent.getFontRenderer().trimToWidth(mc.getDisplayVersion(), this.listWidth - 10), this.left + 3, var3 + 12, 16720418);
            this.parent
                    .getFontRenderer()
                    .method_4247(this.parent.getFontRenderer().trimToWidth("DISABLED", this.listWidth - 10), this.left + 3, var3 + 22, 16720418);
        } else {
            this.parent
                    .getFontRenderer()
                    .method_4247(this.parent.getFontRenderer().trimToWidth(mc.getName(), this.listWidth - 10), this.left + 3, var3 + 2, 16777215);
            this.parent
                    .getFontRenderer()
                    .method_4247(this.parent.getFontRenderer().trimToWidth(mc.getDisplayVersion(), this.listWidth - 10), this.left + 3, var3 + 12, 13421772);
            this.parent
                    .getFontRenderer()
                    .method_4247(
                            this.parent
                                    .getFontRenderer()
                                    .trimToWidth(mc.getMetadata() != null ? mc.getMetadata().getChildModCountString() : "Metadata not found", this.listWidth - 10),
                            this.left + 3,
                            var3 + 22,
                            13421772
                    );
        }
    }
}
