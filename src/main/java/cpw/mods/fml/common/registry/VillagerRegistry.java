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
package cpw.mods.fml.common.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.structure.class_44;
import net.minecraft.structure.class_50;
import net.minecraft.util.Pair;
import net.minecraft.village.TraderOfferList;

import java.util.*;

public class VillagerRegistry {
    private static final VillagerRegistry INSTANCE = new VillagerRegistry();
    private Multimap<Integer, VillagerRegistry.IVillageTradeHandler> tradeHandlers = ArrayListMultimap.create();
    private Map<Class<?>, VillagerRegistry.IVillageCreationHandler> villageCreationHandlers = Maps.newHashMap();
    private Map<Integer, String> newVillagers = Maps.newHashMap();
    private List<Integer> newVillagerIds = Lists.newArrayList();

    public VillagerRegistry() {
    }

    public static VillagerRegistry instance() {
        return INSTANCE;
    }

    public void registerVillagerType(int villagerId, String villagerSkin) {
        if (this.newVillagers.containsKey(villagerId)) {
            FMLLog.severe("Attempt to register duplicate villager id %d", new Object[]{villagerId});
            throw new RuntimeException();
        } else {
            this.newVillagers.put(villagerId, villagerSkin);
            this.newVillagerIds.add(villagerId);
        }
    }

    public void registerVillageCreationHandler(VillagerRegistry.IVillageCreationHandler handler) {
        this.villageCreationHandlers.put(handler.getComponentClass(), handler);
    }

    public void registerVillageTradeHandler(int villagerId, VillagerRegistry.IVillageTradeHandler handler) {
        this.tradeHandlers.put(villagerId, handler);
    }

    public static String getVillagerSkin(int villagerType, String defaultSkin) {
        return instance().newVillagers.containsKey(villagerType) ? (String)instance().newVillagers.get(villagerType) : defaultSkin;
    }

    public static void manageVillagerTrades(TraderOfferList recipeList, VillagerEntity villager, int villagerType, Random random) {
        for (IVillageTradeHandler handler : instance().tradeHandlers.get(villagerType))
        {
            handler.manipulateTradesForVillager(villager, recipeList, random);
        }
    }

    public static void addExtraVillageComponents(ArrayList components, Random random, int i) {
        List<class_44> parts = components;
        for (IVillageCreationHandler handler : instance().villageCreationHandlers.values())
        {
            parts.add(handler.getVillagePieceWeight(random, i));
        }
    }

    public static Object getVillageComponent(class_44 villagePiece, class_50 startPiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5) {
        return ((VillagerRegistry.IVillageCreationHandler)instance().villageCreationHandlers.get(villagePiece.field_78)).buildComponent(villagePiece, startPiece, pieces, random, p1, p2, p3, p4, p5);
    }

    public static void addEmeraldBuyRecipe(VillagerEntity villager, TraderOfferList list, Random random, Item item, float chance, int min, int max) {
        if (min > 0 && max > 0) {
            VillagerEntity.field_3946.put(item.id, new Pair(min, max));
        }

        VillagerEntity.method_3107(list, item.getMaxDamage(), random, chance);
    }

    public static void addEmeraldSellRecipe(VillagerEntity villager, TraderOfferList list, Random random, Item item, float chance, int min, int max) {
        if (min > 0 && max > 0) {
            VillagerEntity.field_3947.put(item.id, new Pair(min, max));
        }

        VillagerEntity.method_3110(list, item.getMaxDamage(), random, chance);
    }

    public static void applyRandomTrade(VillagerEntity villager, Random rand) {
        int extra = instance().newVillagerIds.size();
        int trade = rand.nextInt(5 + extra);
        villager.setProfession(trade < 5 ? trade : (Integer)instance().newVillagerIds.get(trade - 5));
    }

    public interface IVillageTradeHandler {
        void manipulateTradesForVillager(VillagerEntity arg, TraderOfferList arg2, Random random);
    }

    public interface IVillageCreationHandler {
        class_44 getVillagePieceWeight(Random random, int i);

        Class<?> getComponentClass();

        Object buildComponent(class_44 arg, class_50 arg2, List list, Random random, int i, int j, int k, int l, int m);
    }
}
