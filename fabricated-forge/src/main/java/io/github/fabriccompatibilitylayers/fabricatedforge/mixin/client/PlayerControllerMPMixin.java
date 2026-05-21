package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {

    @Final
    @Shadow private Minecraft mc;

    // Pattern C (@Inject HEAD, cancellable): fires before the adventure-mode guard to
    // give items a chance to cancel block break via onBlockStartBreak.
    @Inject(method = "onPlayerDestroyBlock", at = @At("HEAD"), cancellable = true)
    private void forge$onBlockStartBreak(int par1, int par2, int par3, int par4,
                                         CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = this.mc.thePlayer.getCurrentEquippedItem();
        if (stack != null && stack.getItem() != null
                && ((ItemExtension)stack.getItem()).onBlockStartBreak(stack, par1, par2, par3, this.mc.thePlayer)) {
            cir.setReturnValue(false);
        }
    }

    // Pattern E (@WrapOperation INVOKE): replaces the setBlockWithNotify call-site with
    // removeBlockByPlayer, delegating block removal to the Block itself.
    @WrapOperation(
        method = "onPlayerDestroyBlock",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;setBlockWithNotify(IIII)Z")
    )
    private boolean forge$removeBlockByPlayer(World world, int x, int y, int z, int id,
                                              Operation<Boolean> original,
                                              @Local(ordinal = 0) Block block) {
        return ((BlockExtension) block).removeBlockByPlayer(world, this.mc.thePlayer, x, y, z);
    }

    // Pattern C (@Inject INVOKE+AFTER, cancellable): injects just after getBlockId so all
    // float offsets (var9/10/11) are already computed; onItemUseFirst gets first refusal.
    @Inject(
        method = "onPlayerRightClick",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", shift = At.Shift.AFTER),
        cancellable = true
    )
    private void forge$onItemUseFirst(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack,
                                      int par4, int par5, int par6, int par7, Vec3 par8Vec3,
                                      CallbackInfoReturnable<Boolean> cir,
                                      @Local(ordinal = 0) float var9,
                                      @Local(ordinal = 1) float var10,
                                      @Local(ordinal = 2) float var11) {
        if (par3ItemStack != null && par3ItemStack.getItem() != null
                && ((ItemExtension)par3ItemStack.getItem()).onItemUseFirst(par3ItemStack, par1EntityPlayer, par2World,
                        par4, par5, par6, par7, var9, var10, var11)) {
            cir.setReturnValue(true);
        }
    }

    // Pattern E (@WrapOperation INVOKE, ordinal=1): intercepts the survival-branch
    // tryPlaceItemIntoWorld call; ordinal 0 is the creative branch (different handling).
    // Logic delta: original returns the result directly; here we additionally fire
    // PlayerDestroyItemEvent when the stack is consumed, then always return true on success.
    @WrapOperation(
        method = "onPlayerRightClick",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemStack;tryPlaceItemIntoWorld(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/World;IIIIFFF)Z", ordinal = 1)
    )
    private boolean forge$postDestroyItemOnPlace(ItemStack par3ItemStack,
                                                 EntityPlayer par1EntityPlayer, World par2World,
                                                 int par4, int par5, int par6, int par7,
                                                 float var9, float var10, float var11,
                                                 Operation<Boolean> original) {
        if (!original.call(par3ItemStack, par1EntityPlayer, par2World, par4, par5, par6, par7, var9, var10, var11)) {
            return false;
        }
        if (par3ItemStack.stackSize <= 0) {
            MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(par1EntityPlayer, par3ItemStack));
        }
        return true;
    }

    // Pattern G (@WrapOperation EXPRESSION): widens the == 0 stack-size check to <= 0
    // so negative stack sizes also trigger item destruction.
    @Definition(id = "stackSize", field = "Lnet/minecraft/src/ItemStack;stackSize:I")
    @Expression("?.stackSize == 0")
    @WrapOperation(method = "sendUseItem", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$widenStackSizeCheck(int left, int right, Operation<Boolean> original) {
        return left <= right;
    }

    // Pattern A (@Inject RETURN ordinal 0): fires PlayerDestroyItemEvent when the item was
    // consumed, after the inventory slot has been nulled.
    // Logic delta: fires at the return-true site rather than inline in the inner if-block;
    // re-checks stackSize <= 0 to gate correctly.
    @Inject(method = "sendUseItem", at = @At(value = "RETURN", ordinal = 0))
    private void forge$postPlayerDestroyItemEvent(EntityPlayer par1EntityPlayer, World par2World,
                                                  ItemStack par3ItemStack,
                                                  CallbackInfoReturnable<Boolean> cir,
                                                  @Local(ordinal = 1) ItemStack var5) {
        if (var5 != null && var5.stackSize <= 0) {
            MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(par1EntityPlayer, var5));
        }
    }
}