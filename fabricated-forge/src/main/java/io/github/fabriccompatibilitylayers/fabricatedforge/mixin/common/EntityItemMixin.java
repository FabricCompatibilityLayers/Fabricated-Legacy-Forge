package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.EntityExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityItem.class)
public abstract class EntityItemMixin extends Entity implements EntityExtension {
    @Shadow public ItemStack item;

    @Shadow public int delayBeforeCanPickup;

    public EntityItemMixin(World par1World) {
        super(par1World);
    }

    /**
     * The maximum age of this EntityItem.  The item is expired once this is reached.
     */
    public int lifespan = 6000;

    @Inject(method = "<init>(Lnet/minecraft/src/World;DDDLnet/minecraft/src/ItemStack;)V", at = @At("RETURN"))
    private void forge$setLifespan(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack, CallbackInfo ci) {
        this.lifespan = (par8ItemStack.getItem() == null ? 6000 : ((ItemExtension) par8ItemStack.getItem()).getEntityLifespan(par8ItemStack, par1World));
    }

    @ModifyConstant(method = "onUpdate", constant = @Constant(intValue = 6000))
    private int forge$lifespan(int constant) {
        return this.lifespan;
    }

    @WrapWithCondition(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityItem;setDead()V"))
    private boolean forge$postItemExpireEvent(EntityItem instance) {
        ItemExpireEvent event = new ItemExpireEvent((EntityItem) (Object) this, (item.getItem() == null ? 6000 : ((ItemExtension) item.getItem()).getEntityLifespan(item, worldObj)));
        if (MinecraftForge.EVENT_BUS.post(event))
        {
            lifespan += event.extraLife;
            return false;
        }

        return true;
    }

    @Inject(method = "onUpdate", at = @At("HEAD"))
    private void forge$setDead(CallbackInfo ci) {
        if (this.item == null || this.item.stackSize <= 0)
        {
            this.setDead();
        }
    }

    @Inject(method = "writeEntityToNBT", at = @At(value = "FIELD", target = "Lnet/minecraft/src/EntityItem;item:Lnet/minecraft/src/ItemStack;"))
    private void forge$writeEntityToNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        par1NBTTagCompound.setInteger("Lifespan", lifespan);
    }

    @Definition(id = "item", field = "Lnet/minecraft/src/EntityItem;item:Lnet/minecraft/src/ItemStack;")
    @Expression("this.item == null")
    @ModifyExpressionValue(method = "readEntityFromNBT", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$checkStackEmpty(boolean original) {
        return original || this.item.stackSize <= 0;
    }

    @Inject(method = "readEntityFromNBT", at = @At("RETURN"))
    private void forge$readEntityFromNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        if (par1NBTTagCompound.hasKey("Lifespan"))
        {
            lifespan = par1NBTTagCompound.getInteger("Lifespan");
        }
    }

    @Inject(method = "onCollideWithPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/src/ItemStack;stackSize:I", ordinal = 0), cancellable = true)
    private void forge$postEntityItemPickupEvent(EntityPlayer par1EntityPlayer, CallbackInfo ci,
                                                 @Share(value = "event", namespace = "fabricated-forge") LocalRef<EntityItemPickupEvent> eventRef) {
        if (this.delayBeforeCanPickup > 0)
        {
            ci.cancel();
            return;
        }

        EntityItemPickupEvent event = new EntityItemPickupEvent(par1EntityPlayer, (EntityItem) (Object) this);

        if (MinecraftForge.EVENT_BUS.post(event))
        {
            ci.cancel();
        } else {
            eventRef.set(event);
        }
    }

    @Definition(id = "delayBeforeCanPickup", field = "Lnet/minecraft/src/EntityItem;delayBeforeCanPickup:I")
    @Expression("this.delayBeforeCanPickup == 0")
    @WrapOperation(method = "onCollideWithPlayer", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$modifyExpression(int left, int right, Operation<Boolean> original) {
        return left <= 0;
    }

    @WrapOperation(method = "onCollideWithPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/InventoryPlayer;addItemStackToInventory(Lnet/minecraft/src/ItemStack;)Z"))
    private boolean forge$modifyExpression(InventoryPlayer instance, ItemStack itemStack, Operation<Boolean> original,
                                           @Share(value = "event", namespace = "fabricated-forge") LocalRef<EntityItemPickupEvent> eventRef) {
        return eventRef.get().isHandled() || itemStack.stackSize <= 0 || original.call(instance, itemStack);
    }
}
