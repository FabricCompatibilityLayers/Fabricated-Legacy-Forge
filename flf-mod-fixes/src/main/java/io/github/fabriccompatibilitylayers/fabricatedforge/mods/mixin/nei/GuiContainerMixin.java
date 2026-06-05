/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.mixin.nei;

import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.forge.IContainerClientSide;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiContainer.class)
public class GuiContainerMixin extends GuiScreen {
    public GuiContainerManager manager;

    @Override
    public void setWorldAndResolution(Minecraft mc, int i, int j) {
        super.setWorldAndResolution(mc, i, j);
        if (mc.currentScreen == this) {
            this.manager = new GuiContainerManager((GuiContainer) (Object) this);
            this.manager.load();
        }

    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
    private void nei$draw$preDraw(int par1, int par2, float par3, CallbackInfo ci) {
        this.manager.preDraw();
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V", ordinal = 1, shift = At.Shift.AFTER))
    private void nei$draw$objectUnderMouse(int par1, int par2, float par3, CallbackInfo ci,
                                           @Share(namespace = "nei", value = "objectUnderMouse") LocalIntRef ref) {
        ref.set(this.manager.objectUnderMouse(par1, par2) ? 1 : 0);
    }

    @ModifyExpressionValue(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiContainer;isMouseOverSlot(Lnet/minecraft/src/Slot;II)Z"))
    private boolean nei$draw$isMouseOverSlot(boolean original,
                                             @Share(namespace = "nei", value = "objectUnderMouse") LocalIntRef ref) {
        return original && ref.get() == 0;
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/InventoryPlayer;getItemStack()Lnet/minecraft/src/ItemStack;", ordinal = 0))
    private void nei$draw$renderObjects(int par1, int par2, float par3, CallbackInfo ci,
                                        @Local(ordinal = 3) int var4,
                                        @Local(ordinal = 4) int var5) {
        GL11.glTranslatef((float)(-var4), (float)(-var5), 200.0F);
        this.manager.renderObjects(par1, par2);
        GL11.glTranslatef((float)var4, (float)var5, -200.0F);
    }

    @ModifyConstant(method = "drawScreen", constant = @Constant(floatValue = 200.0F))
    private float nei$draw$ModifyConstant(float constant) {
        return 500.0F;
    }

    @Definition(id = "getItemStack", method = "Lnet/minecraft/src/InventoryPlayer;getItemStack()Lnet/minecraft/src/ItemStack;")
    @Expression("?.getItemStack() == null")
    @WrapOperation(method = "drawScreen", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean nei$draw$renderTooltips(Object left, Object right, Operation<Boolean> original,
                                            @Local(argsOnly = true, ordinal = 0) int par1,
                                            @Local(argsOnly = true, ordinal = 1) int par2) {
        this.manager.renderToolTips(par1, par2);
        return false;
    }

    public List handleTooltip(int mousex, int mousey, List currenttip) {
        return currenttip;
    }

    public List handleItemTooltip(ItemStack stack, int mousex, int mousey, List currenttip) {
        return currenttip;
    }

    @Inject(method = "drawSlotInventory", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V", ordinal = 1))
    private void nei$drawSlot$renderSlotUnderlay(Slot par1Slot, CallbackInfo ci) {
        this.manager.renderSlotUnderlay(par1Slot);
    }

    @Inject(method = "drawSlotInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderItem;renderItemOverlayIntoGUI(Lnet/minecraft/src/FontRenderer;Lnet/minecraft/src/RenderEngine;Lnet/minecraft/src/ItemStack;II)V", shift = At.Shift.AFTER))
    private void nei$drawSlot$renderSlotOverlay(Slot par1Slot, CallbackInfo ci) {
        this.manager.renderSlotOverlay(par1Slot);
    }

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiScreen;mouseClicked(III)V", shift = At.Shift.AFTER))
    private void nei$mouseClicked$mouseClicked(int par1, int par2, int par3, CallbackInfo ci,
                                               @Share(namespace = "nei", value = "mouseCheck") LocalIntRef ref) {
        ref.set(!this.manager.mouseClicked(par1, par2, par3) ? 1 : 0);
    }

    @Definition(id = "par3", local = @Local(type = int.class, ordinal = 2, argsOnly = true))
    @Expression("par3 == ?")
    @WrapOperation(method = "mouseClicked", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean nei$mouseClicked$mouseClicked$condition(int left, int right, Operation<Boolean> operation,
                                                            @Share(namespace = "nei", value = "mouseCheck") LocalIntRef ref) {
        return ref.get() == 1;
    }

    @Inject(method = "mouseClicked", at = @At(value = "CONSTANT", args = "intValue=-1", ordinal = 0))
    private void nei$mouseClicked$addCondition(int par1, int par2, int par3, CallbackInfo ci,
                                                  @Local(ordinal = 5) LocalIntRef ref,
                                                  @Local Slot slot) {
        ref.set((ref.get() == 1 && slot == null) ? 1 : 0);
    }

    @Expression("? != -999")
    @WrapOperation(method = "mouseClicked", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean nei$mouseClicked$removeCondition(int left, int right, Operation<Boolean> operation) {
        return operation.call(0, right);
    }

    @Redirect(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiContainer;handleMouseClick(Lnet/minecraft/src/Slot;IIZ)V"))
    private void nei$mouseClicked$handleMouseClick(GuiContainer screen, Slot slot, int par2, int par3, boolean par4) {
        this.manager.handleMouseClick(slot, par2, par3, par4);
    }

    @WrapOperation(method = "handleMouseClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/PlayerControllerMP;windowClick(IIIZLnet/minecraft/src/EntityPlayer;)Lnet/minecraft/src/ItemStack;"))
    private ItemStack nei$handleMouseClick$wrap(PlayerControllerMP instance, int par1, int par2, int par3, boolean par4, EntityPlayer par5EntityPlayer, Operation<ItemStack> original) {
        if (par2 != -1) {
            if (this instanceof IContainerClientSide) {
                return par5EntityPlayer.craftingInventory.slotClick(par2, par3, par4, par5EntityPlayer);
            } else {
                return original.call(instance, par1, par2, par3, par4, par5EntityPlayer);
            }
        }

        return null;
    }

    @Override
    protected void mouseMovedOrUp(int i, int j, int k) {
        if (k >= 0) {
            this.manager.mouseUp(i, j, k);
        }

    }

    @Definition(id = "keyCode", field = "Lnet/minecraft/src/KeyBinding;keyCode:I")
    @Expression("? == ?.keyCode")
    @WrapOperation(method = "keyTyped", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean nei$keyTyped(int left, int right, Operation<Boolean> original,
                                 @Local(ordinal = 0) char par1) {
        return !this.manager.lastKeyTyped(left, par1) && original.call(left, right);
    }

    @Inject(method = "updateScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiScreen;updateScreen()V", shift = At.Shift.AFTER))
    private void nei$updateScreen(CallbackInfo ci) {
        this.manager.guiTick();
    }

    @Override
    public void handleKeyboardInput() {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == 87) {
                this.mc.toggleFullscreen();
                return;
            }

            if (this.manager.firstKeyTyped(Keyboard.getEventKey(), Keyboard.getEventCharacter())) {
                return;
            }

            this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
        }

    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0) {
            this.manager.mouseWheel(-i / 120);
        }

    }

    public void refresh() {
        this.manager.refresh();
    }
}
