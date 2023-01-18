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

import com.google.common.collect.Lists;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.TradeEntry;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.village.TraderOfferList;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ModLoaderVillageTradeHandler implements VillagerRegistry.IVillageTradeHandler {
    private List<TradeEntry> trades = Lists.newArrayList();

    public ModLoaderVillageTradeHandler() {
    }

    public void manipulateTradesForVillager(VillagerEntity villager, TraderOfferList recipeList, Random random) {
        for(TradeEntry ent : this.trades) {
            if (ent.buying) {
                VillagerRegistry.addEmeraldBuyRecipe(villager, recipeList, random, Item.ITEMS[ent.id], ent.chance, ent.min, ent.max);
            } else {
                VillagerRegistry.addEmeraldSellRecipe(villager, recipeList, random, Item.ITEMS[ent.id], ent.chance, ent.min, ent.max);
            }
        }
    }

    public void addTrade(TradeEntry entry) {
        this.trades.add(entry);
    }
}
