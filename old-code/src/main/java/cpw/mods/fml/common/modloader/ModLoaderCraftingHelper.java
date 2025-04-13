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
package cpw.mods.fml.common.modloader;

import cpw.mods.fml.common.ICraftingHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class ModLoaderCraftingHelper implements ICraftingHandler {
    private BaseModProxy mod;

    public ModLoaderCraftingHelper(BaseModProxy mod) {
        this.mod = mod;
    }

    public void onCrafting(PlayerEntity player, ItemStack item, Inventory craftMatrix) {
        this.mod.takenFromCrafting(player, item, craftMatrix);
    }

    public void onSmelting(PlayerEntity player, ItemStack item) {
        this.mod.takenFromFurnace(player, item);
    }
}
