package cpw.mods.fml.client;

import cpw.mods.fml.common.IFMLHandledException;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import fr.catcore.fabricatedforge.forged.FatalErrorScreenForged;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.FatalErrorScreen;

@Environment(EnvType.CLIENT)
@SideOnly(Side.CLIENT)
public abstract class CustomModLoadingErrorDisplayException extends RuntimeException implements IFMLHandledException {
    public CustomModLoadingErrorDisplayException() {
    }

    public abstract void initGui(FatalErrorScreenForged arg, TextRenderer arg2);

    public abstract void drawScreen(FatalErrorScreenForged arg, TextRenderer arg2, int i, int j, float f);
}
