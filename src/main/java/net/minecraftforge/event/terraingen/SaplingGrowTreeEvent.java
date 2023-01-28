package net.minecraftforge.event.terraingen;

import net.minecraft.world.World;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.world.WorldEvent;

import java.util.Random;

@Event.HasResult
public class SaplingGrowTreeEvent extends WorldEvent {
    public final int x;
    public final int y;
    public final int z;
    public final Random rand;

    public SaplingGrowTreeEvent(World world, Random rand, int x, int y, int z) {
        super(world);
        this.rand = rand;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
