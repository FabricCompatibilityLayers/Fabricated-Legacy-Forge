package net.minecraftforge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ListWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiControlsScrollPanel extends ListWidget {
    private ControlsOptionsScreen controls;
    private GameOptions options;
    private Minecraft mc;
    private String[] message;
    private int _mouseX;
    private int _mouseY;
    private int selected = -1;

    public GuiControlsScrollPanel(ControlsOptionsScreen controls, GameOptions options, Minecraft mc) {
        super(mc, controls.width, controls.height, 16, controls.height - 32 + 4, 25);
        this.controls = controls;
        this.options = options;
        this.mc = mc;
    }

    protected int getEntryCount() {
        return this.options.keysAll.length;
    }

    protected void method_1057(int i, boolean flag) {
        if (!flag) {
            if (this.selected == -1) {
                this.selected = i;
            } else {
                this.options.method_867(this.selected, -100);
                this.selected = -1;
                KeyBinding.updateKeysByCode();
            }
        }

    }

    protected boolean isEntrySelected(int i) {
        return false;
    }

    protected void renderBackground() {
    }

    public void render(int mX, int mY, float f) {
        this._mouseX = mX;
        this._mouseY = mY;
        if (this.selected != -1 && !Mouse.isButtonDown(0) && Mouse.getDWheel() == 0 && Mouse.next() && Mouse.getEventButtonState()) {
            System.out.println(Mouse.getEventButton());
            this.options.method_867(this.selected, -100 + Mouse.getEventButton());
            this.selected = -1;
            KeyBinding.updateKeysByCode();
        }

        super.render(mX, mY, f);
    }

    protected void method_1055(int index, int xPosition, int yPosition, int l, Tessellator tessellator) {
        int width = 70;
        int height = 20;
        xPosition -= 20;
        boolean flag = this._mouseX >= xPosition && this._mouseY >= yPosition && this._mouseX < xPosition + width && this._mouseY < yPosition + height;
        int k = flag ? 2 : 1;
        GL11.glBindTexture(3553, this.mc.textureManager.getTextureFromPath("/gui/gui.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.controls.drawTexture(xPosition, yPosition, 0, 46 + k * 20, width / 2, height);
        this.controls.drawTexture(xPosition + width / 2, yPosition, 200 - width / 2, 46 + k * 20, width / 2, height);
        this.controls.drawWithShadow(this.mc.textRenderer, this.options.getLanguageFromId(index), xPosition + width + 4, yPosition + 6, -1);
        boolean conflict = false;

        for(int x = 0; x < this.options.keysAll.length; ++x) {
            if (x != index && this.options.keysAll[x].code == this.options.keysAll[index].code) {
                conflict = true;
                break;
            }
        }

        String str = (conflict ? "§c" : "") + this.options.method_874(index);
        str = index == this.selected ? "§f> §e??? §f<" : str;
        this.controls.drawCenteredString(this.mc.textRenderer, str, xPosition + width / 2, yPosition + (height - 8) / 2, -1);
    }

    public boolean keyTyped(char c, int i) {
        if (this.selected != -1) {
            this.options.method_867(this.selected, i);
            this.selected = -1;
            KeyBinding.updateKeysByCode();
            return false;
        } else {
            return true;
        }
    }
}
