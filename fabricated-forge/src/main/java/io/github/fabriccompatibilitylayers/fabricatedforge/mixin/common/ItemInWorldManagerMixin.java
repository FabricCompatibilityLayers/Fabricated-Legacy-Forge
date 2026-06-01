/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemInWorldManagerExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInWorldManager.class)
public abstract class ItemInWorldManagerMixin implements ItemInWorldManagerExtension {
    @Shadow public EntityPlayerMP thisPlayerMP;
    @Shadow public World theWorld;

    @Shadow public abstract boolean isCreative();

    @Shadow protected abstract boolean removeBlock(int par1, int par2, int par3);

    /** Forge reach distance */
    private double blockReachDistance = 5.0d;

    @Inject(method = "onBlockClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemInWorldManager;isCreative()Z"), cancellable = true)
    private void forge$onPlayerInteract(int par1, int par2, int par3, int par4, CallbackInfo ci,
                                        @Share(value = "event", namespace = "fabricated-forge") LocalRef<PlayerInteractEvent> eventRef) {
        PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(thisPlayerMP, PlayerInteractEvent.Action.LEFT_CLICK_BLOCK, par1, par2, par3, par4);

        if (event.isCanceled())
        {
            thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par1, par2, par3, theWorld));
            ci.cancel();
        }

        eventRef.set(event);
    }

    @Redirect(method = "onBlockClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;extinguishFire(Lnet/minecraft/src/EntityPlayer;IIII)Z"))
    private boolean forge$cancelExtinguishFire(World instance, EntityPlayer par2, int par3, int par4, int par5, int i) {
        return false;
    }

    @Definition(id = "var6", local = @Local(ordinal = 4, type = int.class))
    @Expression("var6 > 0")
    @WrapOperation(method = "onBlockClicked", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private boolean forge$NonNullBlock(int var6, int right, Operation<Boolean> original) {
        return Block.blocksList[var6] != null;
    }

    @WrapOperation(method = "onBlockClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;onBlockClicked(Lnet/minecraft/src/World;IIILnet/minecraft/src/EntityPlayer;)V"))
    private void forge$extinguishFire(Block block, World theWorld, int par1, int par2, int par3, EntityPlayer thisPlayerMP, Operation<Void> original,
                                      @Share(value = "event", namespace = "fabricated-forge") LocalRef<PlayerInteractEvent> eventRef,
                                      @Local(ordinal = 3, argsOnly = true) int par4) {
        if (eventRef.get().useBlock != Event.Result.DENY)
        {
            original.call(block, theWorld, par1, par2, par3, thisPlayerMP);
            theWorld.extinguishFire(thisPlayerMP, par1, par2, par3, par4);
        }
        else
        {
            ((EntityPlayerMP) thisPlayerMP).playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par1, par2, par3, theWorld));
        }
    }

    @Definition(id = "var6", local = @Local(ordinal = 4, type = int.class))
    @Expression("var6 > 0")
    @Inject(method = "onBlockClicked", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1), cancellable = true)
    private void forge$sendPacket53BlockChange(int par1, int par2, int par3, int par4, CallbackInfo ci,
                                               @Share(value = "event", namespace = "fabricated-forge") LocalRef<PlayerInteractEvent> eventRef,
                                               @Local float var5) {
        if (eventRef.get().useItem == Event.Result.DENY)
        {
            if (var5 >= 1.0f)
            {
                thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par1, par2, par3, theWorld));
            }

            ci.cancel();
        }
    }

    @Redirect(method = "removeBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;setBlockWithNotify(IIII)Z"))
    private boolean forge$removeBlockByPlayer(World instance, int par1, int par2, int par3, int i,
                                              @Local Block var4) {
        return var4 != null && ((BlockExtension) var4).removeBlockByPlayer(theWorld, thisPlayerMP, par1, par2, par3);
    }

    @Inject(method = "tryHarvestBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I"), cancellable = true)
    private void forge$onBlockStartBreak(int par1, int par2, int par3, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = thisPlayerMP.getCurrentEquippedItem();
        if (stack != null && ((ItemExtension) stack.getItem()).onBlockStartBreak(stack, par1, par2, par3, thisPlayerMP))
        {
            cir.setReturnValue(false);
        }
    }

    @WrapOperation(method = "tryHarvestBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemInWorldManager;removeBlock(III)Z"))
    private boolean forge$defaultValue(ItemInWorldManager instance, int par2, int par3, int i, Operation<Boolean> original) {
        boolean var6 = false;

        if (this.isCreative()) {
            var6 = original.call(instance, par2, par3, i);
        }

        return var6;
    }

    @Redirect(method = "tryHarvestBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerMP;canHarvestBlock(Lnet/minecraft/src/Block;)Z"))
    private boolean forge$canHarvestBlock(EntityPlayerMP instance, Block block,
                                          @Local(ordinal = 4) int var5) {
        boolean var8 = false;

        if (block != null)
        {
            var8 = ((BlockExtension) block).canHarvestBlock(thisPlayerMP, var5);
        }

        return var8;
    }

    @WrapOperation(method = "tryHarvestBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerMP;destroyCurrentEquippedItem()V"))
    private void forge$postPlayerDestroyItemEvent(EntityPlayerMP instance, Operation<Void> original,
                                                  @Local ItemStack var7) {
        original.call(instance);
        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thisPlayerMP, var7));
    }

    @ModifyVariable(method = "tryHarvestBlock", index = 6, at = @At(value = "LOAD", ordinal = 0))
    private int forge$removeBlock(int var6,
                                  @Local(ordinal = 0, argsOnly = true) int par1,
                                  @Local(ordinal = 1, argsOnly = true) int par2,
                                  @Local(ordinal = 2, argsOnly = true) int par3) {
        return this.removeBlock(par1, par2, par3) ? 1 : 0;
    }

    @Definition(id = "mainInventory", field = "Lnet/minecraft/src/InventoryPlayer;mainInventory:[Lnet/minecraft/src/ItemStack;")
    @Expression("?.mainInventory[?] = ?")
    @WrapOperation(method = "tryUseItem", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
    private void forge$postPlayerDestroyItemEvent(ItemStack[] array, int index, ItemStack value, Operation<Void> original, @Local(ordinal = 1) ItemStack var6) {
        original.call(array, index, value);
        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thisPlayerMP, var6));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean activateBlockOrUseItem(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(par1EntityPlayer, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, par4, par5, par6, par7);
        if (event.isCanceled())
        {
            thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par4, par5, par6, theWorld));
            return false;
        }

        Item item = (par3ItemStack != null ? par3ItemStack.getItem() : null);
        if (item != null && ((ItemExtension) item).onItemUseFirst(par3ItemStack, par1EntityPlayer, par2World, par4, par5, par6, par7, par8, par9, par10))
        {
            if (par3ItemStack.stackSize <= 0) ForgeEventFactory.onPlayerDestroyItem(thisPlayerMP, par3ItemStack);
            return true;
        }

        int var11 = par2World.getBlockId(par4, par5, par6);
        Block block = Block.blocksList[var11];
        boolean result = false;

        if (block != null)
        {
            if (event.useBlock != Event.Result.DENY)
            {
                result = block.onBlockActivated(par2World, par4, par5, par6, par1EntityPlayer, par7, par8, par9, par10);
            }
            else
            {
                thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par4, par5, par6, theWorld));
                result = event.useItem != Event.Result.ALLOW;
            }
        }

        if (par3ItemStack != null && !result)
        {
            int meta = par3ItemStack.getItemDamage();
            int size = par3ItemStack.stackSize;
            result = par3ItemStack.tryPlaceItemIntoWorld(par1EntityPlayer, par2World, par4, par5, par6, par7, par8, par9, par10);
            if (isCreative())
            {
                par3ItemStack.setItemDamage(meta);
                par3ItemStack.stackSize = size;
            }
            if (par3ItemStack.stackSize <= 0) ForgeEventFactory.onPlayerDestroyItem(thisPlayerMP, par3ItemStack);
        }

        /* Re-enable if this causes bukkit incompatibility, or re-write client side to only send a single packet per right click.
        if (par3ItemStack != null && ((!result && event.useItem != Event.Result.DENY) || event.useItem == Event.Result.ALLOW))
        {
            this.tryUseItem(thisPlayerMP, par2World, par3ItemStack);
        }*/
        return result;
    }

    @Override
    public double getBlockReachDistance()
    {
        return blockReachDistance;
    }

    @Override
    public void setBlockReachDistance(double distance)
    {
        blockReachDistance = distance;
    }
}
