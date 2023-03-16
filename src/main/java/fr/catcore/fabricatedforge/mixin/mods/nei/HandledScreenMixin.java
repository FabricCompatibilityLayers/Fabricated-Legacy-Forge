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
    @Shadow
    public int x;
    @Shadow
    public int y;
    @Shadow public ScreenHandler screenHandler;
    @Shadow
    public int backgroundWidth;
    @Shadow
    public int backgroundHeight;

    @Shadow
    public abstract Slot getSlotAt(int x, int y);

    @Shadow
    public static ItemRenderer field_1346;

    @Shadow protected abstract void method_1135();

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
     * @author chickenbones
     * @reason class overwrite
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
        Slot var6 = null;
        short var7 = 240;
        short var8 = 240;
        GLX.gl13MultiTexCoord2f(GLX.lightmapTextureUnit, (float)var7 / 1.0F, (float)var8 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        boolean objectundermouse = this.manager.objectUnderMouse(par1, par2);

        for(int var11 = 0; var11 < this.screenHandler.slots.size(); ++var11) {
            Slot var14 = (Slot)this.screenHandler.slots.get(var11);
            this.drawSlot(var14);
            if (this.isPointOverSlot(var14, par1, par2) && !objectundermouse) {
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                int var9 = var14.x;
                int var10 = var14.y;
                this.fillGradient(var9, var10, var9 + 16, var10 + 16, -2130706433, -2130706433);
                GL11.glEnable(2896);
                GL11.glEnable(2929);
            }
        }

        this.method_1135();
        PlayerInventory var12 = this.field_1229.playerEntity.inventory;
        GL11.glTranslatef((float)(-var4), (float)(-var5), 200.0F);
        this.manager.renderObjects(par1, par2);
        GL11.glTranslatef((float)var4, (float)var5, -200.0F);
        if (var12.getCursorStack() != null) {
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            this.zOffset = 500.0F;
            field_1346.zOffset = 500.0F;
            field_1346.method_1546(this.textRenderer, this.field_1229.textureManager, var12.getCursorStack(), par1 - var4 - 8, par2 - var5 - 8);
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
     * @author chickenbone
     * @reason class overwrite
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
            field_1346.method_1546(this.textRenderer, this.field_1229.textureManager, var4, var2, var3);
            field_1346.method_1549(this.textRenderer, this.field_1229.textureManager, var4, var2, var3);
            this.manager.renderSlotOverlay(par1Slot);
        }

        field_1346.zOffset = 0.0F;
        this.zOffset = 0.0F;
    }

    /**
     * @author chickenbone
     * @reason class overwrite
     */
    @Overwrite
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        if (!this.manager.mouseClicked(par1, par2, par3)) {
            Slot var4 = this.getSlotAt(par1, par2);
            int var5 = this.x;
            int var6 = this.y;
            boolean var7 = (par1 < var5 || par2 < var6 || par1 >= var5 + this.backgroundWidth || par2 >= var6 + this.backgroundHeight) && var4 == null;
            int var8 = -1;
            if (var4 != null) {
                var8 = var4.id;
            }

            if (var7) {
                var8 = -999;
            }

            if (var8 != -1) {
                boolean var9 = Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
                this.manager.handleMouseClick(var4, var8, par3, var9);
            }
        }
    }

    /**
     * @author chickenbone
     * @reason class overwrite
     */
    @Overwrite
    public void method_1131(Slot par1Slot, int par2, int par3, boolean par4) {
        if (par1Slot != null) {
            par2 = par1Slot.id;
        }

        if (par2 != -1) {
            if (this instanceof IContainerClientSide) {
                this.field_1229.playerEntity.openScreenHandler.method_3252(par2, par3, par4, this.field_1229.playerEntity);
            } else {
                this.field_1229.interactionManager.method_1224(this.screenHandler.syncId, par2, par3, par4, this.field_1229.playerEntity);
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
     * @author chickenbone
     * @reason class overwrite
     */
    @Overwrite
    protected void keyPressed(char par1, int par2) {
        if (par2 == 1) {
            this.field_1229.playerEntity.closeHandledScreen();
        } else if (!this.manager.lastKeyTyped(par2, par1)) {
            if (par2 == this.field_1229.options.keyInventory.code) {
                this.field_1229.playerEntity.closeHandledScreen();
            }
        }
    }

    /**
     * @author chickenbone
     * @reason class overwrite
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
            this.manager.mouseWheel(-i / 120);
        }
    }

    public void refresh() {
        this.manager.refresh();
    }
}
