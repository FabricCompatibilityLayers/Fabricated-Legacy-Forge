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
        Iterator i$ = this.trades.iterator();

        while(i$.hasNext()) {
            TradeEntry ent = (TradeEntry)i$.next();
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
