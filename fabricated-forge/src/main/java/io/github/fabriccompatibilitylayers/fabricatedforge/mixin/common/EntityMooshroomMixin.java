/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.EntityExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.IShearable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;

@Mixin(EntityMooshroom.class)
public abstract class EntityMooshroomMixin extends EntityCow implements EntityExtension, IShearable {
    public EntityMooshroomMixin(World par1World) {
        super(par1World);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean interact(EntityPlayer par1EntityPlayer) {
        ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();
        if (var2 != null && var2.itemID == Item.bowlEmpty.shiftedIndex && this.getGrowingAge() >= 0) {
            if (var2.stackSize == 1) {
                par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, new ItemStack(Item.bowlSoup));
                return true;
            }

            if (par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Item.bowlSoup)) && !par1EntityPlayer.capabilities.isCreativeMode) {
                par1EntityPlayer.inventory.decrStackSize(par1EntityPlayer.inventory.currentItem, 1);
                return true;
            }
        }

        return super.interact(par1EntityPlayer);
    }

    @Override
    public boolean isShearable(ItemStack item, World world, int X, int Y, int Z)
    {
        return getGrowingAge() >= 0;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, World world, int X, int Y, int Z, int fortune)
    {
        setDead();
        EntityCow entitycow = new EntityCow(worldObj);
        entitycow.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
        entitycow.setEntityHealth(getHealth());
        entitycow.renderYawOffset = renderYawOffset;
        worldObj.spawnEntityInWorld(entitycow);
        worldObj.spawnParticle("largeexplode", posX, posY + (double)(height / 2.0F), posZ, 0.0D, 0.0D, 0.0D);

        ArrayList<ItemStack> ret = new ArrayList<>();
        for (int x = 0; x < 5; x++)
        {
            ret.add(new ItemStack(Block.mushroomRed));
        }
        return ret;
    }
}
