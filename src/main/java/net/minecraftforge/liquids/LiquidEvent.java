/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.liquids;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;

public class LiquidEvent extends Event {
    public final LiquidStack liquid;
    public final int x;
    public final int y;
    public final int z;
    public final World world;

    public LiquidEvent(LiquidStack liquid, World world, int x, int y, int z) {
        this.liquid = liquid;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static final void fireEvent(LiquidEvent event) {
        MinecraftForge.EVENT_BUS.post(event);
    }

    public static class LiquidDrainingEvent extends LiquidEvent {
        public final ILiquidTank tank;

        public LiquidDrainingEvent(LiquidStack liquid, World world, int x, int y, int z, ILiquidTank tank) {
            super(liquid, world, x, y, z);
            this.tank = tank;
        }
    }

    public static class LiquidFillingEvent extends LiquidEvent {
        public final ILiquidTank tank;

        public LiquidFillingEvent(LiquidStack liquid, World world, int x, int y, int z, ILiquidTank tank) {
            super(liquid, world, x, y, z);
            this.tank = tank;
        }
    }

    public static class LiquidMotionEvent extends LiquidEvent {
        public LiquidMotionEvent(LiquidStack liquid, World world, int x, int y, int z) {
            super(liquid, world, x, y, z);
        }
    }

    public static class LiquidSpilledEvent extends LiquidEvent {
        public LiquidSpilledEvent(LiquidStack liquid, World world, int x, int y, int z) {
            super(liquid, world, x, y, z);
        }
    }
}
