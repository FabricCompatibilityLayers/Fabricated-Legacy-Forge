package fr.catcore.fabricatedforge.mixin.mods.nei;

import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.forge.IContainerClientSide;
import com.mojang.blaze3d.platform.GLX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.item.ItemRenderer;
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

import java.util.List;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {
    @Shadow public int x;
    @Shadow public int y;
    @Shadow
    public Slot focusedSlot;
    @Shadow public ScreenHandler screenHandler;
    @Shadow public static ItemRenderer field_1346;
    @Shadow public int backgroundWidth;
    @Shadow public int backgroundHeight;

    @Shadow protected abstract boolean handleHotbarKeyPressed(int keyCode);

    @Shadow public abstract Slot getSlotAt(int x, int y);

    @Shadow protected abstract void drawForeground(int mouseX, int mouseY);

    @Shadow protected abstract boolean isPointOverSlot(Slot slot, int pointX, int pointY);

    @Shadow protected abstract void drawBackground(float delta, int mouseX, int mouseY);

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
     * @reason impl manager
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

    public List handleTooltip(int mousex, int mousey, List currenttip) {
        return currenttip;
    }

    public List handleItemTooltip(ItemStack stack, int mousex, int mousey, List currenttip) {
        return currenttip;
    }

    /**
     * @author ChickenBones
     * @reason impl manager
     */
    @Overwrite
    private void drawSlot(Slot par1Slot) {
        int var2 = par1Slot.x;
        int var3 = par1Slot.y;
        ItemStack var4 = par1Slot.getStack();
        boolean var5 = false;
        this.zOffset = 100.0F;
        field_1346.zOffset = 100.0F;
        if (var4 == null) {
            int var6 = par1Slot.method_3297();
            if (var6 >= 0) {
                GL11.glDisable(2896);
                this.field_1229.textureManager.bindTexture(this.field_1229.textureManager.getTextureFromPath("/gui/items.png"));
                this.drawTexture(var2, var3, var6 % 16 * 16, var6 / 16 * 16, 16, 16);
                GL11.glEnable(2896);
                var5 = true;
            }
        }

        if (!var5) {
            this.manager.renderSlotUnderlay(par1Slot);
            GL11.glEnable(2929);
            field_1346.method_4336(this.textRenderer, this.field_1229.textureManager, var4, var2, var3);
            field_1346.method_1549(this.textRenderer, this.field_1229.textureManager, var4, var2, var3);
            this.manager.renderSlotOverlay(par1Slot);
        }

        field_1346.zOffset = 0.0F;
        this.zOffset = 0.0F;
    }

    /**
     * @author ChickenBones
     * @reason impl manager
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

    /**
     * @author ChickenBones
     * @reason impl manager
     */
    @Overwrite
    public void onMouseClick(Slot par1Slot, int par2, int par3, int par4) {
        if (par1Slot != null) {
            par2 = par1Slot.id;
        }

        if (par2 != -1) {
            if (this instanceof IContainerClientSide) {
                this.field_1229.playerEntity.openScreenHandler.onSlotClick(par2, par3, par4, this.field_1229.playerEntity);
            } else {
                this.field_1229.interactionManager.clickSlot(this.screenHandler.syncId, par2, par3, par4, this.field_1229.playerEntity);
            }
        }
    }

    @Override
    protected void mouseReleased(int i, int j, int k) {
        if (k >= 0) {
            this.manager.mouseUp(i, j, k);
        }
    }

    /**
     * @author ChickenBones
     * @reason impl manager
     */
    @Overwrite
    protected void keyPressed(char par1, int par2) {
        if (par2 == 1) {
            this.field_1229.playerEntity.closeHandledScreen();
        } else if (!this.manager.lastKeyTyped(par2, par1)) {
            if (!this.handleHotbarKeyPressed(par2)
                    && par2 == this.field_1229.options.keyPickItem.code
                    && this.focusedSlot != null
                    && this.focusedSlot.hasStack()) {
                this.onMouseClick(this.focusedSlot, this.focusedSlot.id, this.backgroundHeight, 3);
            }

            if (par2 == this.field_1229.options.keyInventory.code) {
                this.field_1229.playerEntity.closeHandledScreen();
            }
        }
    }

    /**
     * @author ChickenBones
     * @reason impl manager
     */
    @Overwrite
    public void tick() {
        super.tick();
        this.manager.guiTick();
        if (!this.field_1229.playerEntity.isAlive() || this.field_1229.playerEntity.removed) {
            this.field_1229.playerEntity.closeHandledScreen();
        }
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

    public void refresh() {
        this.manager.refresh();
    }
}
