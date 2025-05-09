package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.minecraft.src.EntityItem;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NBTTagCompound;

import java.util.ArrayList;
import java.util.UUID;

public interface EntityExtension {
    NBTTagCompound getEntityData();

    boolean shouldRiderSit();

    ItemStack getPickedResult(MovingObjectPosition target);

    UUID getPersistentID();

    void generatePersistentID();

    ArrayList<EntityItem> getCapturedDrops();

    boolean isCapturingDrops();

    void setCaptureDrops(boolean captureDrops);
}
