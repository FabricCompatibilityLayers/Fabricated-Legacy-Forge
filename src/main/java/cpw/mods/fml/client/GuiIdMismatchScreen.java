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

import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import cpw.mods.fml.common.registry.ItemData;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Language;

import java.util.List;
import java.util.Map;

public class GuiIdMismatchScreen extends ConfirmScreen {
    private List<String> missingIds = Lists.newArrayList();
    private List<String> mismatchedIds = Lists.newArrayList();
    private boolean allowContinue;

    public GuiIdMismatchScreen(MapDifference<Integer, ItemData> idDifferences, boolean allowContinue) {
        super(null, "ID mismatch", "Should I continue?", 1);
        this.field_1077 = this;

        for(Map.Entry<Integer, ItemData> entry : idDifferences.entriesOnlyOnLeft().entrySet()) {
            this.missingIds
                    .add(
                            String.format(
                                    "ID %d (ModID: %s, type %s) is missing",
                                    ((ItemData)entry.getValue()).getItemId(),
                                    ((ItemData)entry.getValue()).getModId(),
                                    ((ItemData)entry.getValue()).getItemType()
                            )
                    );
        }

        for(Map.Entry<Integer, MapDifference.ValueDifference<ItemData>> entry : idDifferences.entriesDiffering().entrySet()) {
            ItemData world = (ItemData)((MapDifference.ValueDifference)entry.getValue()).leftValue();
            ItemData game = (ItemData)((MapDifference.ValueDifference)entry.getValue()).rightValue();
            this.mismatchedIds
                    .add(
                            String.format(
                                    "ID %d is mismatched. World: (ModID: %s, type %s, ordinal %d) Game (ModID: %s, type %s, ordinal %d)",
                                    world.getItemId(),
                                    world.getModId(),
                                    world.getItemType(),
                                    world.getOrdinal(),
                                    game.getModId(),
                                    game.getItemType(),
                                    game.getOrdinal()
                            )
                    );
        }

        this.allowContinue = allowContinue;
    }

    public void confirmResult(boolean choice, int par2) {
        FMLClientHandler.instance().callbackIdDifferenceResponse(choice);
    }

    public void render(int par1, int par2, float par3) {
        this.renderBackground();
        if (!this.allowContinue && this.buttons.size() == 2) {
            this.buttons.remove(0);
        }

        int offset = Math.max(85 - this.missingIds.size() * 10 + this.mismatchedIds.size() * 30, 10);
        this.drawCenteredString(this.textRenderer, "Forge Mod Loader has found world ID mismatches", this.width / 2, offset, 16777215);
        offset += 10;

        for(String s : this.missingIds) {
            this.drawCenteredString(this.textRenderer, s, this.width / 2, offset, 15658734);
            offset += 10;
        }

        for(String s : this.mismatchedIds) {
            this.drawCenteredString(this.textRenderer, s, this.width / 2, offset, 15658734);
            offset += 10;
        }

        offset += 10;
        if (this.allowContinue) {
            this.drawCenteredString(this.textRenderer, "Do you wish to continue loading?", this.width / 2, offset, 16777215);
            offset += 10;
        } else {
            this.drawCenteredString(this.textRenderer, "You cannot connect to this server", this.width / 2, offset, 16777215);
            offset += 10;
        }

        for(int var4 = 0; var4 < this.buttons.size(); ++var4) {
            ButtonWidget var5 = (ButtonWidget)this.buttons.get(var4);
            var5.y = Math.min(offset + 10, this.height - 20);
            if (!this.allowContinue) {
                var5.x = this.width / 2 - 75;
                var5.message = Language.getInstance().translate("gui.done");
            }

            var5.method_891(this.field_1229, par1, par2);
        }
    }
}
