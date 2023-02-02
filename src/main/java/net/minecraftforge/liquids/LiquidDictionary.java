/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.liquids;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
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

    public static Map<String, LiquidStack> getLiquids() {
        return ImmutableMap.copyOf(liquids);
    }

    static {
        getOrCreateLiquid("Water", new LiquidStack(Block.WATER, 1000));
        getOrCreateLiquid("Lava", new LiquidStack(Block.LAVA, 1000));
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
