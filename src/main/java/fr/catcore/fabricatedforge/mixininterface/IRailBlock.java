package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public interface IRailBlock {
    public void setRenderType(int value);
    public boolean isFlexibleRail(World world, int y, int x, int z);
    public boolean canMakeSlopes(World world, int x, int y, int z);
    public int getBasicRailMetadata(WorldView world, AbstractMinecartEntity cart, int x, int y, int z);
    public float getRailMaxSpeed(World world, AbstractMinecartEntity cart, int y, int x, int z);
    public void onMinecartPass(World world, AbstractMinecartEntity cart, int y, int x, int z);
    public boolean hasPowerBit(World world, int x, int y, int z);
}
