package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiControls;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;
import net.minecraftforge.client.GuiControlsScrollPanel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiControls.class)
public abstract class GuiControlsMixin extends GuiScreen {

    @Shadow private GameSettings options;
    @Shadow protected String screenTitle;
    @Shadow private GuiScreen parentScreen;

    private GuiControlsScrollPanel scrollPane;

    // Pattern D (@Overwrite): >60% of the method body is replaced — key-binding loop removed
    // and GuiControlsScrollPanel added. @Overwrite is the clearest option.
    // Logic delta: var2 / func_73907_g() call dropped (var2 became unused after the loop was removed);
    // done-button Y changed from height/6 + 168 to height - 28.
    /**
     * @author Fabricated-Legacy-Forge
     * @reason Replace flat key-binding button loop with GuiControlsScrollPanel
     */
    @Overwrite
    public void initGui() {
        scrollPane = new GuiControlsScrollPanel((GuiControls)(Object) this, options, mc);
        StringTranslate var1 = StringTranslate.getInstance();
        this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height - 28, var1.translateKey("gui.done")));
        scrollPane.registerScrollButtons(controlList, 7, 8);
        this.screenTitle = var1.translateKey("controls.title");
    }

    // Pattern D (@Overwrite): >60% of the method body removed — loop and key-binding
    // capture branch both gone. @Overwrite is the clearest option.
    // Logic delta: only the id==200 close-screen branch survives; key-binding capture removed.
    /**
     * @author Fabricated-Legacy-Forge
     * @reason Remove key-binding capture logic; scroll panel handles it instead
     */
    @Overwrite
    protected void actionPerformed(GuiButton par1GuiButton) {
        if (par1GuiButton.id == 200) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    // Pattern D (@Overwrite): entire body replaced with a super delegation.
    // Logic delta: mouse-based key-binding capture (buttonId >= 0 branch) removed entirely.
    /**
     * @author Fabricated-Legacy-Forge
     * @reason Remove mouse key-binding capture; scroll panel handles input instead
     */
    @Overwrite
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
    }

    // Pattern D (@Overwrite): entire body replaced — key-binding capture removed,
    // super call now gated on scrollPane.keyTyped().
    // Logic delta: inverted control flow — original ran super in the else branch;
    // now super only runs when scrollPane.keyTyped() returns true.
    /**
     * @author Fabricated-Legacy-Forge
     * @reason Replace key-binding capture with scroll panel key handling
     */
    @Overwrite
    protected void keyTyped(char par1, int par2) {
        if (scrollPane.keyTyped(par1, par2)) {
            super.keyTyped(par1, par2);
        }
    }

    // Pattern D (@Overwrite): ~90% of the body replaced — entire key-binding draw loop removed.
    // Logic delta: title y-position changed from 20 to 4; scrollPane.drawScreen() added before super call.
    /**
     * @author Fabricated-Legacy-Forge
     * @reason Replace key-binding draw loop with scroll panel rendering
     */
    @Overwrite
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        scrollPane.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 4, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }
}