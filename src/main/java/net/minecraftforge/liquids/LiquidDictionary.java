/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.liquids;

import com.google.common.collect.ImmutableMap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;

import java.util.HashMap;
import java.util.Map;

public abstract class LiquidDictionary {
    private static Map<String, LiquidStack> liquids = new HashMap();

    public LiquidDictionary() {
    }

    public static LiquidStack getOrCreateLiquid(String name, LiquidStack liquid) {
        LiquidStack existing = (LiquidStack)liquids.get(name);
        if (existing != null) {
            return existing.copy();
        } else {
            liquids.put(name, liquid.copy());
            MinecraftForge.EVENT_BUS.post(new LiquidDictionary.LiquidRegisterEvent(name, liquid));
            return liquid;
        }
    }

    public static LiquidStack getLiquid(String name, int amount) {
        LiquidStack liquid = (LiquidStack)liquids.get(name);
        if (liquid == null) {
            return null;
        } else {
            liquid = liquid.copy();
            liquid.amount = amount;
            return liquid;
        }
    }

    public Map<String, LiquidStack> getDefinedLiquids() {
        return ImmutableMap.copyOf(liquids);
    }

    public static class LiquidRegisterEvent extends Event {
        public final String Name;
        public final LiquidStack Liquid;

        public LiquidRegisterEvent(String name, LiquidStack liquid) {
            this.Name = name;
            this.Liquid = liquid.copy();
        }
    }
}
