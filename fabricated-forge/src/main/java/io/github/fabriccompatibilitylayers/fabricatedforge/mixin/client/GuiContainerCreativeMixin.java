/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.CreativeTabsExtension;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(GuiContainerCreative.class)
public abstract class GuiContainerCreativeMixin extends InventoryEffectRenderer {
    @Shadow private static int selectedTabIndex;
    @Shadow protected abstract boolean renderCreativeInventoryHoveringText(CreativeTabs par1CreativeTabs, int par2, int par3);
    @Shadow protected abstract void renderCreativeTab(CreativeTabs par1CreativeTabs);

    // New instance fields — merged into target by Mixin at load time.
    private int tabPage = 0;
    private int maxPages = 0;

    // Dummy constructor to satisfy the compiler (never called at runtime).
    protected GuiContainerCreativeMixin() {
        super(null);
    }

    // Pattern E (@WrapOperation INVOKE): wraps the func_74227_b call inside initGui to run
    // button-setup code immediately after it — user preference over @Inject AFTER-INVOKE.
    @WrapOperation(
            method = "initGui",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiContainerCreative;func_74227_b(Lnet/minecraft/src/CreativeTabs;)V")
    )
    private void forge$initGuiAddPageButtons(GuiContainerCreative instance, CreativeTabs par1CreativeTabs, Operation<Void> original) {
        original.call(instance, par1CreativeTabs);
        int tabCount = CreativeTabs.creativeTabArray.length;
        if (tabCount > 12) {
            this.controlList.add(new GuiButton(101, guiLeft, guiTop - 50, 20, 20, "<"));
            this.controlList.add(new GuiButton(102, guiLeft + xSize - 20, guiTop - 50, 20, 20, ">"));
            tabPage = 0;
            maxPages = ((tabCount - 12) / 10) + 1;
        }
    }

    // Pattern E (@WrapOperation INVOKE): intercepts drawInForegroundOfTab call-site;
    // returns false if receiver is null rather than NPEing — 1:1 translation.
    @WrapOperation(
            method = "drawGuiContainerForegroundLayer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/CreativeTabs;drawInForegroundOfTab()Z")
    )
    private boolean forge$nullSafeDrawInForeground(CreativeTabs instance, Operation<Boolean> original) {
        return instance != null && original.call(instance);
    }

    // Pattern E (@WrapOperation INVOKE): intercepts func_74232_a call-site in mouseClicked;
    // short-circuits to false if the tab arg is null — 1:1 translation.
    @WrapOperation(
            method = "mouseClicked",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiContainerCreative;func_74232_a(Lnet/minecraft/src/CreativeTabs;II)Z")
    )
    private boolean forge$nullSafeTabClick(GuiContainerCreative instance, CreativeTabs par1CreativeTabs, int par2, int par3, Operation<Boolean> original) {
        return par1CreativeTabs != null && original.call(instance, par1CreativeTabs, par2, par3);
    }

    // Pattern C (@Inject HEAD cancellable): fires before the existing return expression and
    // returns false early if the tab array entry is null — HEAD is required since the
    // original expression would NPE on a null entry before reaching any RETURN site.
    @Inject(method = "needsScrollBars", at = @At("HEAD"), cancellable = true)
    private void forge$nullCheckScrollBars(CallbackInfoReturnable<Boolean> cir) {
        if (CreativeTabs.creativeTabArray[selectedTabIndex] == null) {
            cir.setReturnValue(false);
        }
    }

    // Pattern P (@WrapMethod): preferred over @Inject(HEAD, cancellable) for an early-return
    // null guard — wrapping intent is explicit and composes better with other injectors.
    @WrapMethod(method = "func_74227_b")
    private void forge$nullSafeSetTab(CreativeTabs par1CreativeTabs, Operation<Void> original) {
        if (par1CreativeTabs == null) {
            return;
        }
        original.call(par1CreativeTabs);
    }

    @WrapOperation(method = "drawScreen", at = @At(value = "FIELD", target = "Lnet/minecraft/src/CreativeTabs;creativeTabArray:[Lnet/minecraft/src/CreativeTabs;", ordinal = 0, opcode = Opcodes.GETSTATIC))
    private CreativeTabs[] forge$draw$limitArrayToPage(Operation<CreativeTabs[]> original,
                                                       @Share(namespace = "forge", value = "rendered")LocalBooleanRef ref) {
        CreativeTabs[] var7 = original.call();
        int start = tabPage * 10;
        int var8 = Math.min(var7.length, ((tabPage + 1) * 10 + 2));
        if (tabPage != 0) start += 2;

        ref.set(false);

        return Arrays.copyOfRange(var7, start, var8);
    }

    @WrapOperation(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiContainerCreative;renderCreativeInventoryHoveringText(Lnet/minecraft/src/CreativeTabs;II)Z"))
    private boolean forge$renderCreativeInventoryHoveringText1(GuiContainerCreative instance, CreativeTabs par2, int par3, int i, Operation<Boolean> original,
                                                              @Share(namespace = "forge", value = "rendered")LocalBooleanRef ref) {
        boolean result = par2 != null && original.call(instance, par2, par3, i);

        if (result) ref.set(true);

        return result;
    }

    @Inject(method = "drawScreen", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GuiContainerCreative;field_74235_v:Lnet/minecraft/src/Slot;", ordinal = 0, opcode = Opcodes.GETFIELD))
    private void forge$renderCreativeInventoryHoveringText$2(int par1, int par2, float par3, CallbackInfo ci,
                                                             @Share(namespace = "forge", value = "rendered")LocalBooleanRef ref) {
        if (!ref.get() && !renderCreativeInventoryHoveringText(CreativeTabs.tabAllSearch, par1, par2)) {
            renderCreativeInventoryHoveringText(CreativeTabs.tabInventory, par1, par2);
        }
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V", ordinal = 0))
    private void forge$renderPageIndicator(int par1, int par2, float par3, CallbackInfo ci) {
        if (maxPages != 0) {
            String page = String.format("%d / %d", tabPage + 1, maxPages + 1);
            int width = fontRenderer.getStringWidth(page);
            GL11.glDisable(GL11.GL_LIGHTING);
            this.zLevel = 300.0F;
            itemRenderer.zLevel = 300.0F;
            fontRenderer.drawString(page, guiLeft + (xSize / 2) - (width / 2), guiTop - 44, -1);
            this.zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
        }
    }

    @WrapOperation(method = "drawGuiContainerBackgroundLayer", at = @At(value = "FIELD", target = "Lnet/minecraft/src/CreativeTabs;creativeTabArray:[Lnet/minecraft/src/CreativeTabs;", ordinal = 1, opcode = Opcodes.GETSTATIC))
    private CreativeTabs[] forge$limitArrayToPage(Operation<CreativeTabs[]> original) {
        CreativeTabs[] var7 = original.call();
        int start = tabPage * 10;
        int var8 = Math.min(var7.length, ((tabPage + 1) * 10 + 2));
        if (tabPage != 0) start += 2;
        return Arrays.copyOfRange(var7, start, var8);
    }

    @Definition(id = "getTabIndex", method = "Lnet/minecraft/src/CreativeTabs;getTabIndex()I")
    @Definition(id = "selectedTabIndex", field = "Lnet/minecraft/src/GuiContainerCreative;selectedTabIndex:I")
    @Expression("?.getTabIndex() != selectedTabIndex")
    @WrapOperation(method = "drawGuiContainerBackgroundLayer", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$nullCheck(int left, int right, Operation<Boolean> original,
                                    @Local(ordinal = 1) CreativeTabs var10) {
        return var10 != null && original.call(left, right);
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;bindTexture(I)V", ordinal = 1))
    private void forge$specialTabRendering1(float par1, int par2, int par3, CallbackInfo ci,
                                           @Local(ordinal = 2) int var4,
                                           @Local(ordinal = 0) CreativeTabs var5) {
        if (tabPage != 0) {
            if (var5 != CreativeTabs.tabAllSearch) {
                this.mc.renderEngine.bindTexture(var4);
                this.renderCreativeTab(CreativeTabs.tabAllSearch);
            }
            if (var5 != CreativeTabs.tabInventory) {
                this.mc.renderEngine.bindTexture(var4);
                this.renderCreativeTab(CreativeTabs.tabInventory);
            }
        }
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/CreativeTabs;shouldHidePlayerInventory()Z"), cancellable = true)
    private void forge$specialTabRendering1(float par1, int par2, int par3, CallbackInfo ci,
                                            @Local(ordinal = 0) CreativeTabs var5) {
        if (var5 == null || ((CreativeTabsExtension) var5).getTabPage() != tabPage) {
            if (var5 != CreativeTabs.tabAllSearch && var5 != CreativeTabs.tabInventory) {
                ci.cancel();
            }
        }
    }

    @WrapMethod(method = "func_74232_a")
    private boolean forge$isTabeVisible(CreativeTabs par1CreativeTabs, int par2, int par3, Operation<Boolean> original) {
        if (((CreativeTabsExtension) par1CreativeTabs).getTabPage() != tabPage)
        {
            if (par1CreativeTabs != CreativeTabs.tabAllSearch &&
                    par1CreativeTabs != CreativeTabs.tabInventory)
            {
                return false;
            }
        }

        return original.call(par1CreativeTabs, par2, par3);
    }

    // Pattern N (@Redirect NEW): intercepts the new ItemStack(...) constructor call-site in
    // renderCreativeTab and returns getIconItemStack() instead — replaces both the constructor
    // and its argument in one handler.
    @Redirect(
        method = "renderCreativeTab",
        at = @At(value = "NEW", target = "net/minecraft/src/ItemStack")
    )
    private ItemStack forge$getTabIconItemStack(Item tabIconItem, @Local(argsOnly = true) CreativeTabs par1CreativeTabs) {
        return ((CreativeTabsExtension) par1CreativeTabs).getIconItemStack();
    }

    // Pattern A (@Inject RETURN): appends the page-navigation button handling after both
    // existing id==0 and id==1 checks — 1:1 translation.
    @Inject(method = "actionPerformed", at = @At("RETURN"))
    private void forge$actionPerformedPageNavigation(GuiButton par1GuiButton, CallbackInfo ci) {
        if (par1GuiButton.id == 101) {
            tabPage = Math.max(tabPage - 1, 0);
        } else if (par1GuiButton.id == 102) {
            tabPage = Math.min(tabPage + 1, maxPages);
        }
    }
}