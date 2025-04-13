/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

import net.minecraft.world.World;

public interface IPlantable {
    EnumPlantType getPlantType(World arg, int i, int j, int k);

    int getPlantID(World arg, int i, int j, int k);

    int getPlantMetadata(World arg, int i, int j, int k);
}
