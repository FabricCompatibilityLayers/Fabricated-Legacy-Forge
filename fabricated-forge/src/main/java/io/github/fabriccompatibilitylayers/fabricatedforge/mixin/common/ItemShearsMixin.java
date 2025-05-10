package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.IShearable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;

@Mixin(ItemShears.class)
public abstract class ItemShearsMixin extends Item implements ItemExtension {
    protected ItemShearsMixin(int par1) {
        super(par1);
    }

    @Definition(id = "par3", local = @Local(type = int.class, ordinal = 0, argsOnly = true))
    @Definition(id = "tripWire", field = "Lnet/minecraft/src/Block;tripWire:Lnet/minecraft/src/Block;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/Block;blockID:I")
    @Expression("par3 != tripWire.blockID")
    @WrapOperation(method = "onBlockDestroyed", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isShearable(int par3, int right, Operation<Boolean> original) {
        return original.call(par3, right) && !(Block.blocksList[par3] instanceof IShearable);
    }

    @Redirect(method = "onBlockDestroyed", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemStack;damageItem(ILnet/minecraft/src/EntityLiving;)V"))
    private void forge$cancelDamage(ItemStack instance, int par2EntityLiving, EntityLiving entityLiving) {

    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityLiving entity)
    {
        if (entity.worldObj.isRemote)
        {
            return false;
        }
        if (entity instanceof IShearable)
        {
            IShearable target = (IShearable)entity;
            if (target.isShearable(itemstack, entity.worldObj, (int)entity.posX, (int)entity.posY, (int)entity.posZ))
            {
                ArrayList<ItemStack> drops = target.onSheared(itemstack, entity.worldObj, (int)entity.posX, (int)entity.posY, (int)entity.posZ,
                        EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack));
                for(ItemStack stack : drops)
                {
                    EntityItem ent = entity.entityDropItem(stack, 1.0F);
                    ent.motionY += entity.rand.nextFloat() * 0.05F;
                    ent.motionX += (entity.rand.nextFloat() - entity.rand.nextFloat()) * 0.1F;
                    ent.motionZ += (entity.rand.nextFloat() - entity.rand.nextFloat()) * 0.1F;
                }
                itemstack.damageItem(1, entity);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player)
    {
        if (player.worldObj.isRemote)
        {
            return false;
        }
        int id = player.worldObj.getBlockId(x, y, z);
        if (Block.blocksList[id] instanceof IShearable)
        {
            IShearable target = (IShearable)Block.blocksList[id];
            if (target.isShearable(itemstack, player.worldObj, x, y, z))
            {
                ArrayList<ItemStack> drops = target.onSheared(itemstack, player.worldObj, x, y, z,
                        EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack));
                for(ItemStack stack : drops)
                {
                    float f = 0.7F;
                    double d  = (double)(player.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d1 = (double)(player.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d2 = (double)(player.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    EntityItem entityitem = new EntityItem(player.worldObj, (double)x + d, (double)y + d1, (double)z + d2, stack);
                    entityitem.delayBeforeCanPickup = 10;
                    player.worldObj.spawnEntityInWorld(entityitem);
                }
                itemstack.damageItem(1, player);
                player.addStat(StatList.mineBlockStatArray[id], 1);
            }
        }
        return false;
    }
}
