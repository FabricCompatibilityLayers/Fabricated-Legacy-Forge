package fr.catcore.fabricatedforge.compat.mixin.nei;

import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.forge.IContainerClientSide;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.GLX;
import fr.catcore.fabricatedforge.compat.nei.NEIHandledScreen;
import net.minecraft.client.Minecraft;
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
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen implements NEIHandledScreen {
    @Shadow protected int x;
    @Shadow protected int y;

    @Shadow private Slot focusedSlot;
    @Shadow public ScreenHandler screenHandler;
    @Shadow protected int backgroundHeight;

    @Shadow protected abstract boolean handleHotbarKeyPressed(int keyCode);

    @Shadow protected abstract Slot getSlotAt(int x, int y);

    @Shadow protected int backgroundWidth;

    @Shadow protected abstract void onMouseClick(Slot slot, int invSlot, int button, int slotAction);

    @Shadow protected abstract void drawBackground(float delta, int mouseX, int mouseY);

    @Shadow protected abstract void drawSlot(Slot slot);

    @Shadow protected abstract boolean isPointOverSlot(Slot slot, int pointX, int pointY);

    @Shadow protected abstract void drawForeground(int mouseX, int mouseY);

    @Shadow protected static ItemRenderer field_1346;
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
     * @reason hooks for NEI
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

        for(int var10 = 0; var10 < this.screenHandler.slots.size(); ++var10) {
            Slot var11 = (Slot)this.screenHandler.slots.get(var10);
            this.drawSlot(var11);
            if (this.isPointOverSlot(var11, par1, par2) && !objectundermouse) {
                this.focusedSlot = var11;
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                int var8 = var11.x;
                int var9 = var11.y;
                this.fillGradient(var8, var9, var8 + 16, var9 + 16, -2130706433, -2130706433);
                GL11.glEnable(2896);
                GL11.glEnable(2929);
            }
        }

        this.drawForeground(par1, par2);
        PlayerInventory var12 = this.field_1229.playerEntity.inventory;
        GL11.glTranslatef((float)(-var4), (float)(-var5), 200.0F);
        this.manager.renderObjects(par1, par2);
        GL11.glTranslatef((float)var4, (float)var5, -200.0F);
        if (var12.getCursorStack() != null) {
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            this.zOffset = 500.0F;
            field_1346.zOffset = 500.0F;
            field_1346.method_4336(this.textRenderer, this.field_1229.textureManager, var12.getCursorStack(), par1 - var4 - 8, par2 - var5 - 8);
            field_1346.method_1549(this.textRenderer, this.field_1229.textureManager, var12.getCursorStack(), par1 - var4 - 8, par2 - var5 - 8);
            this.zOffset = 0.0F;
            field_1346.zOffset = 0.0F;
        }

        this.manager.renderToolTips(par1, par2);
        GL11.glPopMatrix();
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        DiffuseLighting.enableNormally();
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
    private void flf$renderSlotUnderlay(Slot par1Slot, CallbackInfo ci) {
        this.manager.renderSlotUnderlay(par1Slot);
    }

    @Inject(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;method_1549(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/TextureManager;Lnet/minecraft/item/ItemStack;II)V",
    shift = At.Shift.AFTER))
    private void flf$renderSlotOverlay(Slot par1Slot, CallbackInfo ci) {
        this.manager.renderSlotOverlay(par1Slot);
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

            if (var9 != -1) {
                boolean var10 = Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
                this.manager.handleMouseClick(var5, var9, par3, var4 ? 3 : (var10 ? 1 : 0));
            }
        }
    }

    @WrapOperation(method = "onMouseClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;clickSlot(IIIILnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack flf$onMouseClick(ClientPlayerInteractionManager instance, int syncId, int slotId, int mouseButton, int actionType, PlayerEntity player, Operation<ItemStack> original) {
        if (slotId != -1) {
            if (this instanceof IContainerClientSide) {
                return this.field_1229.playerEntity.openScreenHandler.onSlotClick(slotId, mouseButton, actionType, player);
            } else {
                return original.call(instance, syncId, slotId, mouseButton, actionType, player);
            }
        }

        return null;
    }

    @Override
    protected void mouseReleased(int i, int j, int k) {
        if (k >= 0) {
            this.manager.mouseUp(i, j, k);
        }
    }

    /**
     * @author ChickenBones
     * @reason idk
     */
    @Overwrite
    protected void keyPressed(char par1, int par2) {
        if (par2 == 1) {
            this.field_1229.playerEntity.closeHandledScreen();
        } else if (!this.manager.lastKeyTyped(par2, par1)) {
            if (!this.handleHotbarKeyPressed(par2) && par2 == this.field_1229.options.keyPickItem.code && this.focusedSlot != null && this.focusedSlot.hasStack()) {
                this.onMouseClick(this.focusedSlot, this.focusedSlot.id, this.backgroundHeight, 3);
            }

            if (par2 == this.field_1229.options.keyInventory.code) {
                this.field_1229.playerEntity.closeHandledScreen();
            }
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
