package cpw.mods.fml.client;

import cpw.mods.fml.common.DuplicateModsFoundException;
import cpw.mods.fml.common.ModContainer;
import fr.catcore.fabricatedforge.forged.FatalErrorScreenForged;
import net.minecraft.client.gui.screen.FatalErrorScreen;

import java.io.File;
import java.util.Map;

public class GuiDupesFound extends FatalErrorScreenForged {
    private DuplicateModsFoundException dupes;

    public GuiDupesFound(DuplicateModsFoundException dupes) {
        this.dupes = dupes;
    }

    public void init() {
        super.init();
    }

    public void render(int par1, int par2, float par3) {
        this.renderBackground();
        int offset = Math.max(85 - this.dupes.dupes.size() * 10, 10);
        this.drawCenteredString(this.textRenderer, "Forge Mod Loader has found a problem with your minecraft installation", this.width / 2, offset, 16777215);
        offset += 10;
        this.drawCenteredString(this.textRenderer, "You have mod sources that are duplicate within your system", this.width / 2, offset, 16777215);
        offset += 10;
        this.drawCenteredString(this.textRenderer, "Mod Id : File name", this.width / 2, offset, 16777215);
        offset += 5;

        for(Map.Entry<ModContainer, File> mc : this.dupes.dupes.entries()) {
            offset += 10;
            this.drawCenteredString(
                    this.textRenderer,
                    String.format("%s : %s", ((ModContainer)mc.getKey()).getModId(), ((File)mc.getValue()).getName()),
                    this.width / 2,
                    offset,
                    15658734
            );
        }
    }
}
