package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.EntityLivingExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.PotionEffectExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Iterator;

@Mixin(EntityLiving.class)
public abstract class EntityLivingMixin extends Entity implements EntityLivingExtension {
    @Shadow protected int scoreValue;

    @Shadow protected boolean dead;

    @Shadow public abstract boolean isChild();

    @Shadow protected abstract void dropFewItems(boolean par1, int par2);

    @Shadow protected int recentlyHit;

    @Shadow protected abstract void dropRareDrop(int par1);

    @Shadow protected HashMap activePotionsMap;

    @Shadow protected abstract void onFinishedPotionEffect(PotionEffect par1PotionEffect);

    public EntityLivingMixin(World par1World) {
        super(par1World);
    }

    @Inject(method = "setAttackTarget", at = @At("RETURN"))
    private void forge$onLivingSetAttackTarget$attack(EntityLiving par1EntityLiving, CallbackInfo ci) {
        ForgeHooks.onLivingSetAttackTarget((EntityLiving) (Object) this, par1EntityLiving);
    }

    @Inject(method = "setRevengeTarget", at = @At("RETURN"))
    private void forge$onLivingSetAttackTarget$revenge(EntityLiving par1EntityLiving, CallbackInfo ci) {
        ForgeHooks.onLivingSetAttackTarget((EntityLiving) (Object) this, par1EntityLiving);
    }

    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    private void forge$onLivingUpdate(CallbackInfo ci) {
        if (ForgeHooks.onLivingUpdate((EntityLiving) (Object) this))
        {
            ci.cancel();
        }
    }

    @Inject(method = "attackEntityFrom", at = @At("HEAD"), cancellable = true)
    private void forge$onLivingAttack(DamageSource par1DamageSource, int par2, CallbackInfoReturnable<Boolean> cir) {
        if (ForgeHooks.onLivingAttack((EntityLiving) (Object) this, par1DamageSource, par2))
        {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "damageEntity", at = @At("HEAD"), cancellable = true)
    private void forge$onLivingHurt(DamageSource par1DamageSource, int par2, CallbackInfo ci, @Local(argsOnly = true) LocalIntRef par2Ref) {
        par2 = ForgeHooks.onLivingHurt((EntityLiving) (Object) this, par1DamageSource, par2);
        if (par2 <= 0)
        {
            ci.cancel();
        } else {
            par2Ref.set(par2);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onDeath(DamageSource par1DamageSource) {
        if (ForgeHooks.onLivingDeath((EntityLiving) (Object) this, par1DamageSource))
        {
            return;
        }

        Entity var2 = par1DamageSource.getEntity();
        if (this.scoreValue >= 0 && var2 != null) {
            var2.addToPlayerScore(this, this.scoreValue);
        }

        if (var2 != null) {
            var2.onKillEntity((EntityLiving) (Object) this);
        }

        this.dead = true;
        if (!this.worldObj.isRemote) {
            int var3 = 0;
            if (var2 instanceof EntityPlayer) {
                var3 = EnchantmentHelper.getLootingModifier(((EntityPlayer)var2).inventory);
            }

            setCaptureDrops(true);
            getCapturedDrops().clear();
            int var4 = 0;

            if (!this.isChild()) {
                this.dropFewItems(this.recentlyHit > 0, var3);
                if (this.recentlyHit > 0) {
                    var4 = this.rand.nextInt(200) - var3;
                    if (var4 < 5) {
                        this.dropRareDrop(var4 <= 0 ? 1 : 0);
                    }
                }
            }

            setCaptureDrops(false);

            if (!ForgeHooks.onLivingDrops((EntityLiving) (Object) this, par1DamageSource, getCapturedDrops(), var3, recentlyHit > 0, var4))
            {
                for (EntityItem item : getCapturedDrops())
                {
                    worldObj.spawnEntityInWorld(item);
                }
            }
        }

        this.worldObj.setEntityState(this, (byte)3);
    }

    @Inject(method = "fall", at = @At("HEAD"), cancellable = true)
    private void forge$onLivingFall(float par1, CallbackInfo ci, @Local(argsOnly = true) LocalFloatRef par1Ref) {
        par1 = ForgeHooks.onLivingFall((EntityLiving) (Object) this, par1);
        if (par1 <= 0)
        {
            ci.cancel();
        } else {
            par1Ref.set(par1);
        }
    }

    @ModifyReturnValue(method = "isOnLadder", at = @At("RETURN"))
    private boolean forge$isLivingOnLadder(boolean original,
                                           @Local(ordinal = 0) int var1,
                                           @Local(ordinal = 1) int var2,
                                           @Local(ordinal = 2) int var3,
                                           @Local(ordinal = 3) int var4) {
        return ForgeHooks.isLivingOnLadder(Block.blocksList[var4], worldObj, var1, var2, var3) || original;
    }

    @Inject(method = "jump", at = @At("RETURN"))
    private void forge$onLivingJump(CallbackInfo ci) {
        ForgeHooks.onLivingJump((EntityLiving) (Object) this);
    }

    /***
     * Removes all potion effects that have curativeItem as a curative item for its effect
     * @param curativeItem The itemstack we are using to cure potion effects
     */
    @Override
    public void curePotionEffects(ItemStack curativeItem)
    {
        Iterator<Integer> potionKey = activePotionsMap.keySet().iterator();

        if (worldObj.isRemote)
        {
            return;
        }

        while (potionKey.hasNext())
        {
            Integer key = potionKey.next();
            PotionEffect effect = (PotionEffect)activePotionsMap.get(key);

            if (((PotionEffectExtension) effect).isCurativeItem(curativeItem))
            {
                potionKey.remove();
                onFinishedPotionEffect(effect);
            }
        }
    }
}
