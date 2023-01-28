package net.minecraftforge.event.terraingen;

import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraftforge.event.world.WorldEvent;

import java.util.Random;

public class InitNoiseGensEvent extends WorldEvent {
    public final Random rand;
    public final NoiseGenerator[] originalNoiseGens;
    public NoiseGenerator[] newNoiseGens;

    public InitNoiseGensEvent(World world, Random rand, NoiseGenerator[] original) {
        super(world);
        this.rand = rand;
        this.originalNoiseGens = original;
        this.newNoiseGens = (NoiseGenerator[])original.clone();
    }
}
