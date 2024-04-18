package fr.catcore.fabricatedforge.compat.mixin.nei;

import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.forge.IContainerClientSide;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.GLX;
import fr.catcore.fabricatedforge.compat.nei.NEIHandledScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.TextureManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen implements NEIHandledScreen {

    @Shadow
    public int x;
    @Shadow
    public int y;

    @Shadow protected abstract void drawBackground(float delta, int mouseX, int mouseY);

    @Shadow private Slot focusedSlot;
    @Shadow public ScreenHandler screenHandler;

    @Shadow
    public abstract void drawSlot(Slot slot);

    @Shadow protected abstract boolean isPointOverSlot(Slot slot, int pointX, int pointY);

    @Shadow protected abstract void drawForeground(int mouseX, int mouseY);

    @Shadow private ItemStack touchDragStack;
    @Shadow private boolean touchIsRightClickDrag;

    @Shadow protected abstract void method_4264(ItemStack itemStack, int i, int j);

    @Shadow private ItemStack touchDropReturningStack;
    @Shadow private long touchDropTime;
    @Shadow private Slot touchDropOriginSlot;
    @Shadow private int touchDropX;
    @Shadow private int touchDropY;

    @Shadow
    public abstract Slot getSlotAt(int x, int y);

    @Shadow
    public int backgroundWidth;
    @Shadow
    public int backgroundHeight;
    @Shadow private Slot touchDragSlotStart;

    @Shadow
    public static ItemRenderer field_1346;
    public GuiContainerManager manager;

    @Override
    public void method_1028(Minecraft mc, int i, int j) {
        super.method_1028(mc, i, j);
        if (mc.currentScreen == this) {
            this.manager = new GuiContainerManager((HandledScreen)(Object) this);
            this.manager.load();
        }
    }

    /**
     * @author ChickenBones
     * @reason idk
     */
    @Overwrite
    public void render(int par1, int par2, float par3) {
        this.manager.preDraw();
        this.renderBackground();
        int var4 = this.x;
        int var5 = this.y;
        this.drawBackground(par3, par1, par2);
        GL11.glDisable(32826);
        DiffuseLighting.disable();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        super.render(par1, par2, par3);
        DiffuseLighting.enable();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var4, (float)var5, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826);
        this.focusedSlot = null;
        short var6 = 240;
        short var7 = 240;
        GLX.gl13MultiTexCoord2f(GLX.lightmapTextureUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        boolean objectundermouse = this.manager.objectUnderMouse(par1, par2);

        int var9;
        for(int var13 = 0; var13 < this.screenHandler.slots.size(); ++var13) {
            Slot var14 = (Slot)this.screenHandler.slots.get(var13);
            this.drawSlot(var14);
            if (this.isPointOverSlot(var14, par1, par2) && !objectundermouse) {
                this.focusedSlot = var14;
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                int var8 = var14.x;
                var9 = var14.y;
                this.fillGradient(var8, var9, var8 + 16, var9 + 16, -2130706433, -2130706433);
                GL11.glEnable(2896);
                GL11.glEnable(2929);
            }
        }

        this.drawForeground(par1, par2);
        GL11.glTranslatef((float)(-var4), (float)(-var5), 200.0F);
        this.manager.renderObjects(par1, par2);
        GL11.glTranslatef((float)var4, (float)var5, -200.0F);
        PlayerInventory var15 = this.field_1229.playerEntity.inventory;
        ItemStack var16 = this.touchDragStack == null ? var15.getCursorStack() : this.touchDragStack;
        if (var16 != null) {
            byte var18 = 8;
            var9 = this.touchDragStack == null ? 8 : 16;
            if (this.touchDragStack != null && this.touchIsRightClickDrag) {
                var16 = var16.copy();
                var16.count = MathHelper.ceil((float)var16.count / 2.0F);
            }

            this.method_4264(var16, par1 - var4 - var18, par2 - var5 - var9);
        }

        if (this.touchDropReturningStack != null) {
            float var17 = (float)(Minecraft.getTime() - this.touchDropTime) / 100.0F;
            if (var17 >= 1.0F) {
                var17 = 1.0F;
                this.touchDropReturningStack = null;
            }

            var9 = this.touchDropOriginSlot.x - this.touchDropX;
            int var10 = this.touchDropOriginSlot.y - this.touchDropY;
            int var11 = this.touchDropX + (int)((float)var9 * var17);
            int var12 = this.touchDropY + (int)((float)var10 * var17);
            this.method_4264(this.touchDropReturningStack, var11, var12);
        }

        this.manager.renderToolTips(par1, par2);
        GL11.glPopMatrix();
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        DiffuseLighting.enableNormally();
    }

    @ModifyConstant(method = "method_4264", constant = {
            @Constant(floatValue = 200.0F, ordinal = 0),
            @Constant(floatValue = 200.0F, ordinal = 1)
    })
    private float nei$changeConstants(float constant) {
        return 500.0F;
    }

    @Override
    public List handleTooltip(int mousex, int mousey, List currenttip) {
        return currenttip;
    }

    @Override
    public List handleItemTooltip(ItemStack stack, int mousex, int mousey, List currenttip) {
        return currenttip;
    }

    @Inject(method = "drawSlot", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V", ordinal = 1, remap = false))
    private void nei$renderSlotUnderlay(Slot par1Slot, CallbackInfo ci) {
        this.manager.renderSlotUnderlay(par1Slot);
    }

    @Redirect(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;method_4336(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/TextureManager;Lnet/minecraft/item/ItemStack;II)V"))
    private void nei$drawSlotItemOverridable(ItemRenderer instance, TextRenderer textRenderer, TextureManager textureManager,
                                         ItemStack itemstack, int i, int j, @Local(argsOnly = true) Slot slot) {
        this.drawSlotItem(slot, itemstack, i, j);
    }

    @Override
    public void drawSlotItem(Slot par1Slot, ItemStack itemstack, int i, int j) {
        field_1346.method_4336(this.textRenderer, this.field_1229.textureManager, itemstack, i, j);
        field_1346.method_1549(this.textRenderer, this.field_1229.textureManager, itemstack, i, j);
    }

    @Redirect(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;method_1549(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/TextureManager;Lnet/minecraft/item/ItemStack;II)V"))
    private void nei$renderSlotOverlay(ItemRenderer instance, TextRenderer textureManager, TextureManager itemStack, ItemStack stack, int j, int i,
                                       @Local(argsOnly = true) Slot slot) {
        this.manager.renderSlotOverlay(slot);
    }

    /**
     * @author ChickenBones
     * @reason idk
     */
    @Overwrite
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        boolean var4 = par3 == this.field_1229.options.keyPickItem.code + 100;
        if (!this.manager.mouseClicked(par1, par2, par3) && (par3 == 0 || par3 == 1 || var4)) {
            Slot var5 = this.getSlotAt(par1, par2);
            int var6 = this.x;
            int var7 = this.y;
            boolean var8 = (par1 < var6 || par2 < var7 || par1 >= var6 + this.backgroundWidth || par2 >= var7 + this.backgroundHeight) && var5 == null;
            int var9 = -1;
            if (var5 != null) {
                var9 = var5.id;
            }

            if (var8) {
                var9 = -999;
            }

            if (this.field_1229.options.touchScreen && var8 && this.field_1229.playerEntity.inventory.getCursorStack() == null) {
                this.field_1229.openScreen((Screen)null);
                return;
            }

            if (var9 != -1) {
                if (this.field_1229.options.touchScreen) {
                    if (var5 != null && var5.hasStack()) {
                        this.touchDragSlotStart = var5;
                        this.touchDragStack = null;
                        this.touchIsRightClickDrag = par3 == 1;
                    } else {
                        this.touchDragSlotStart = null;
                    }
                } else {
                    boolean var10 = Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
                    this.manager.handleMouseClick(var5, var9, par3, var4 ? 3 : (var10 ? 1 : 0));
                }
            }
        }

    }

    @Inject(method = "mouseDragged", at = @At("HEAD"))
    private void nei$mouseDragged(int par1, int par2, int par3, long par4, CallbackInfo ci) {
        this.manager.mouseDragged(par1, par2, par3, par4);
    }

    @Inject(method = "mouseReleased", at = @At("RETURN"))
    private void mouseReleased$mouseUp(int par1, int par2, int par3, CallbackInfo ci) {
        if (
                !(this.touchDragSlotStart != null && this.field_1229.options.touchScreen)
                && par3 >= 0
        ) {
            this.manager.mouseUp(par1, par2, par3);
        }
    }

    @WrapWithCondition(method = "onMouseClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;clickSlot(IIIILnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/item/ItemStack;"))
    private boolean nei$wrapClickSlot(
            ClientPlayerInteractionManager interactionManager, int syncId, int slotId, int mouseButton, int actionType, PlayerEntity player
    ) {
        if (slotId != 1) {
            if (this instanceof IContainerClientSide) {
                player.openScreenHandler.onSlotClick(slotId, mouseButton, actionType, player);
            } else {
                return true;
            }
        }

        return false;
    }

    @Inject(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;handleHotbarKeyPressed(I)Z"), cancellable = true)
    private void nei$keyPressed$condition(char code, int par2, CallbackInfo ci) {
        if (code == 1 || this.manager.lastKeyTyped(par2, code)) ci.cancel();
    }

    @Inject(method = "keyPressed", at = @At("RETURN"))
    private void nei$keyPressed$close(char code, int par2, CallbackInfo ci) {
        if (par2 == this.field_1229.options.keyInventory.code) {
            this.field_1229.playerEntity.closeHandledScreen();
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ControllablePlayerEntity;isAlive()Z"))
    private void tickManager(CallbackInfo ci) {
        this.manager.guiTick();
    }

    @Override
    public void method_1040() {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == 87) {
                this.field_1229.toggleFullscreen();
                return;
            }

            if (this.manager.firstKeyTyped(Keyboard.getEventKey(), Keyboard.getEventCharacter())) {
                return;
            }

            this.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey());
        }

    }

    @Override
    public void handleMouse() {
        super.handleMouse();
        int i = Mouse.getEventDWheel();
        if (i != 0) {
            this.manager.mouseWheel(i > 0 ? 1 : -1);
        }

    }

    @Override
    public void refresh() {
        this.manager.refresh();
    }
}
