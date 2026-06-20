/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.EntityExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.EntityMinecartExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityExtension {

    @Shadow public Entity ridingEntity;

    @Shadow
    public World worldObj;
    @Shadow
    public double posX;
    @Shadow
    public double posY;

    @Shadow
    public abstract float getEyeHeight();

    @Shadow
    public double posZ;
    /** Forge: Used to store custom data for each entity. */
    private NBTTagCompound customEntityData;
    public boolean captureDrops = false;
    public ArrayList<EntityItem> capturedDrops = new ArrayList<>();
    private UUID persistentID;

    @Inject(method = "writeToNBT", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Entity;writeEntityToNBT(Lnet/minecraft/src/NBTTagCompound;)V"))
    private void forge$writeToNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        if (persistentID != null)
        {
            par1NBTTagCompound.setLong("PersistentIDMSB", persistentID.getMostSignificantBits());
            par1NBTTagCompound.setLong("PersistentIDLSB", persistentID.getLeastSignificantBits());
        }
        if (customEntityData != null)
        {
            par1NBTTagCompound.setCompoundTag("ForgeData", customEntityData);
        }
    }

    @Inject(method = "readFromNBT", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Entity;readEntityFromNBT(Lnet/minecraft/src/NBTTagCompound;)V"))
    private void forge$readFromNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        if (par1NBTTagCompound.hasKey("ForgeData"))
        {
            customEntityData = par1NBTTagCompound.getCompoundTag("ForgeData");
        }
        if (par1NBTTagCompound.hasKey("PersistentIDMSB") && par1NBTTagCompound.hasKey("PersistentIDLSB"))
        {
            persistentID = new UUID(par1NBTTagCompound.getLong("PersistentIDMSB"), par1NBTTagCompound.getLong("PersistentIDLSB"));
        }
    }

    @WrapWithCondition(method = "entityDropItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;spawnEntityInWorld(Lnet/minecraft/src/Entity;)Z"))
    private boolean forge$entityDropItem(World instance, Entity entity) {
        if (captureDrops) {
            capturedDrops.add((EntityItem) entity);
            return false;
        }

        return true;
    }

    @Definition(id = "ridingEntity", field = "Lnet/minecraft/src/Entity;ridingEntity:Lnet/minecraft/src/Entity;")
    @Expression("this.ridingEntity != null")
    @ModifyExpressionValue(method = "isRiding", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$shouldRiderSit(boolean original) {
        return original && ((EntityExtension) this.ridingEntity).shouldRiderSit();
    }

    /**
     * @author CatCore
     * @reason redirect to forge extension method
     */
    @Overwrite
    public float func_82146_a(Explosion par1Explosion, Block par2Block, int par3, int par4, int par5) {
        return ((BlockExtension) par2Block).getExplosionResistance((Entity) (Object) this, worldObj, par3, par4, par5, posX, posY + (double) getEyeHeight(), posZ);
    }

    /* ================================== Forge Start =====================================*/
    /**
     * Returns a NBTTagCompound that can be used to store custom data for this entity.
     * It will be written, and read from disc, so it persists over world saves.
     * @return A NBTTagCompound
     */
    @Override
    public NBTTagCompound getEntityData()
    {
        if (customEntityData == null)
        {
            customEntityData = new NBTTagCompound();
        }
        return customEntityData;
    }

    /**
     * Used in model rendering to determine if the entity riding this entity should be in the 'sitting' position.
     * @return false to prevent an entity that is mounted to this entity from displaying the 'sitting' animation.
     */
    @Override
    public boolean shouldRiderSit()
    {
        return true;
    }

    /**
     * Called when a user uses the creative pick block button on this entity.
     *
     * @param target The full target the player is looking at
     * @return A ItemStack to add to the player's inventory, Null if nothing should be added.
     */
    @Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
        if ((Object) this instanceof EntityPainting)
        {
            return new ItemStack(Item.painting);
        }
        else if ((Object) this instanceof EntityMinecart)
        {
            return ((EntityMinecartExtension) this).getCartItem();
        }
        else if ((Object) this instanceof EntityBoat)
        {
            return new ItemStack(Item.boat);
        }
        else if ((Object) this instanceof EntityItemFrame)
        {
            ItemStack held = ((EntityItemFrame) (Object)this).func_82335_i();
            if (held == null)
            {
                return new ItemStack(Item.field_82802_bI);
            }
            else
            {
                return held.copy();
            }
        }
        else
        {
            int id = EntityList.getEntityID((Entity) (Object) this);
            if (id > 0 && EntityList.entityEggs.containsKey(id))
            {
                return new ItemStack(Item.monsterPlacer, 1, id);
            }
        }
        return null;
    }

    @Override
    public UUID getPersistentID()
    {
        return persistentID;
    }

    @Override
    public synchronized void generatePersistentID()
    {
        if (persistentID == null)
        {
            persistentID = UUID.randomUUID();
        }
    }

    @Override
    public ArrayList<EntityItem> getCapturedDrops() {
        return capturedDrops;
    }

    @Override
    public boolean isCapturingDrops() {
        return captureDrops;
    }

    @Override
    public void setCaptureDrops(boolean captureDrops) {
        this.captureDrops = captureDrops;
    }
}
