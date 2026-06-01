/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.CreativeTabsExtension;
import net.minecraft.src.ContainerCreative;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainerCreative;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiTextField;
import net.minecraft.src.InventoryEffectRenderer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.Slot;
import net.minecraft.src.StringTranslate;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiContainerCreative.class)
public abstract class GuiContainerCreativeMixin extends InventoryEffectRenderer {
    @Shadow private static int selectedTabIndex;
    @Shadow private boolean wasClicking;
    @Shadow private boolean isScrolling;
    @Shadow private float currentScroll;
    @Shadow private Slot field_74235_v;
    @Shadow private GuiTextField searchField;
    @Shadow
    protected abstract boolean needsScrollBars();
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

    // Pattern D (@Overwrite): loop replacement + appended GL block together touch most of the
    // visible method body — fewer injectors than 3+ surgicals, cleaner to read.
    /**
     * @author Fabricated-Legacy-Forge
     * @reason Replace tab hover loop with page-windowed version and add page indicator.
     */
    @Overwrite
    public void drawScreen(int par1, int par2, float par3) {
        boolean var4 = Mouse.isButtonDown(0);
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        int var7 = var5 + 175;
        int var8 = var6 + 18;
        int var9 = var7 + 14;
        int var10 = var8 + 112;
        if (!this.wasClicking && var4 && par1 >= var7 && par2 >= var8 && par1 < var9 && par2 < var10) {
            this.isScrolling = this.needsScrollBars();
        }

        if (!var4) {
            this.isScrolling = false;
        }

        this.wasClicking = var4;
        if (this.isScrolling) {
            this.currentScroll = (par2 - var8 - 7.5F) / (var10 - var8 - 15.0F);
            if (this.currentScroll < 0.0F) {
                this.currentScroll = 0.0F;
            }
            if (this.currentScroll > 1.0F) {
                this.currentScroll = 1.0F;
            }
            ((ContainerCreative) this.inventorySlots).scrollTo(this.currentScroll);
        }

        super.drawScreen(par1, par2, par3);
        CreativeTabs[] var11 = CreativeTabs.creativeTabArray;
        int start = tabPage * 10;
        int var12 = Math.min(var11.length, ((tabPage + 1) * 10) + 2);
        if (tabPage != 0) start += 2;
        boolean rendered = false;

        for (int var13 = start; var13 < var12; ++var13) {
            CreativeTabs var14 = var11[var13];
            if (var14 != null && this.renderCreativeInventoryHoveringText(var14, par1, par2)) {
                rendered = true;
                break;
            }
        }

        if (!rendered && !this.renderCreativeInventoryHoveringText(CreativeTabs.tabAllSearch, par1, par2)) {
            this.renderCreativeInventoryHoveringText(CreativeTabs.tabInventory, par1, par2);
        }

        if (this.field_74235_v != null
                && selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()
                && this.func_74188_c(this.field_74235_v.xDisplayPosition, this.field_74235_v.yDisplayPosition, 16, 16, par1, par2)) {
            this.drawCreativeTabHoveringText(StringTranslate.getInstance().translateKey("inventory.binSlot"), par1, par2);
        }

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

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(2896);
    }

    // Pattern D (@Overwrite): covers both the loop replacement (hunk 8) and the page guard
    // (hunk 9) in one method — both hunks are in drawGuiContainerBackgroundLayer.
    /**
     * @author Fabricated-Legacy-Forge
     * @reason Replace tab render loop with page-windowed version; add always-visible tabs and page guard.
     */
    @Overwrite
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.enableGUIStandardItemLighting();
        int var4 = this.mc.renderEngine.getTexture("/gui/allitems.png");
        CreativeTabs var5 = CreativeTabs.creativeTabArray[selectedTabIndex];
        int var6 = this.mc.renderEngine.getTexture("/gui/creative_inv/" + var5.getBackgroundImageName());

        CreativeTabs[] var7 = CreativeTabs.creativeTabArray;
        int start = tabPage * 10;
        int var8 = Math.min(var7.length, ((tabPage + 1) * 10 + 2));
        if (tabPage != 0) start += 2;

        for (int var9 = start; var9 < var8; ++var9) {
            CreativeTabs var10 = var7[var9];
            this.mc.renderEngine.bindTexture(var4);
            if (var10 != null && var10.getTabIndex() != selectedTabIndex) {
                this.renderCreativeTab(var10);
            }
        }

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

        this.mc.renderEngine.bindTexture(var6);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.drawTextBox();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int var11 = this.guiLeft + 175;
        int var12 = this.guiTop + 18;
        int var13 = var12 + 112;
        this.mc.renderEngine.bindTexture(var4);

        if (var5 == null || ((CreativeTabsExtension) var5).getTabPage() != tabPage) {
            if (var5 != CreativeTabs.tabAllSearch && var5 != CreativeTabs.tabInventory) {
                return;
            }
        }

        if (var5.shouldHidePlayerInventory()) {
            this.drawTexturedModalRect(var11, var12 + (int)((var13 - var12 - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }

        this.renderCreativeTab(var5);
        if (var5 == CreativeTabs.tabInventory) {
            GuiInventory.func_74223_a(this.mc, this.guiLeft + 43, this.guiTop + 45, 20, this.guiLeft + 43 - par2, this.guiTop + 45 - 30 - par3);
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