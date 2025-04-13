package fr.catcore.fabricatedforge.mixin.forgefml.client.options;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOption;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.network.class_651;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {

    @Shadow public float fov;

    @Shadow public float gamma;

    @Shadow public float musicVolume;

    @Shadow public float soundVolume;

    @Shadow public float sensitivity;

    @Shadow public float chatOpacity;

    @Shadow public abstract boolean gteIntOption(GameOption option);

    @Shadow @Final private static String[] RENDER_DISTANCE;

    @Shadow
    private static String translateArrayElement(String[] array, int index) {
        return null;
    }

    @Shadow public int renderDistance;

    @Shadow @Final private static String[] DIFFICULTY;

    @Shadow public int difficultyLevel;

    @Shadow @Final private static String[] GUI_SCALE;

    @Shadow public int guiScale;

    @Shadow @Final private static String[] CHAT_VISIBILITY;

    @Shadow public int chatVisibility;

    @Shadow @Final private static String[] PARTICLES;

    @Shadow public int particle;

    @Shadow @Final private static String[] STREAM_CHAT_USERFILTER;

    @Shadow public int maxFramerate;

    @Shadow public boolean fancyGraphics;

    @Shadow private File optionsFile;

    @Shadow public boolean invertYMouse;

    @Shadow public boolean bobView;

    @Shadow public boolean anaglyph3d;

    @Shadow public boolean advancedOpengl;

    @Shadow public boolean ambientOcculsion;

    @Shadow public boolean renderClouds;

    @Shadow public String currentTexturePackName;

    @Shadow public String lastServer;

    @Shadow public String language;

    @Shadow public boolean chatColor;

    @Shadow public boolean chatLink;

    @Shadow public boolean chatLinkPrompt;

    @Shadow public boolean useServerTextures;

    @Shadow public boolean snopperEnabled;

    @Shadow public boolean fullscreen;

    @Shadow public boolean vsync;

    @Shadow public boolean hideServerAddress;

    @Shadow public KeyBinding[] keysAll;

    @Shadow protected Minecraft field_946;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public float getFLoatOption(GameOption par1EnumOptions) {
        return par1EnumOptions == GameOption.FOV ? this.fov : (par1EnumOptions == GameOption.GAMMA ? this.gamma : (par1EnumOptions == GameOption.MUSIC ? this.musicVolume : (par1EnumOptions == GameOption.SOUND ? this.soundVolume : (par1EnumOptions == GameOption.SENSITIVITY ? this.sensitivity : (par1EnumOptions == GameOption.CHAT_OPACITY ? this.chatOpacity : 0.0F)))));
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public String getStringOption(GameOption par1EnumOptions) {
        Language var2 = Language.getInstance();
        String var3 = var2.translate(par1EnumOptions.getTranslationKey()) + ": ";
        if (par1EnumOptions.method_879()) {
            float var5 = this.getFLoatOption(par1EnumOptions);
            return par1EnumOptions == GameOption.SENSITIVITY ? (var5 == 0.0F ? var3 + var2.translate("options.sensitivity.min") : (var5 == 1.0F ? var3 + var2.translate("options.sensitivity.max") : var3 + (int)(var5 * 200.0F) + "%")) : (par1EnumOptions == GameOption.FOV ? (var5 == 0.0F ? var3 + var2.translate("options.fov.min") : (var5 == 1.0F ? var3 + var2.translate("options.fov.max") : var3 + (int)(70.0F + var5 * 40.0F))) : (par1EnumOptions == GameOption.GAMMA ? (var5 == 0.0F ? var3 + var2.translate("options.gamma.min") : (var5 == 1.0F ? var3 + var2.translate("options.gamma.max") : var3 + "+" + (int)(var5 * 100.0F) + "%")) : (par1EnumOptions == GameOption.CHAT_OPACITY ? var3 + (int)(var5 * 90.0F + 10.0F) + "%" : (var5 == 0.0F ? var3 + var2.translate("options.off") : var3 + (int)(var5 * 100.0F) + "%"))));
        } else if (par1EnumOptions.method_881()) {
            boolean var4 = this.gteIntOption(par1EnumOptions);
            return var4 ? var3 + var2.translate("options.on") : var3 + var2.translate("options.off");
        } else {
            return par1EnumOptions == GameOption.RENDER_DISTANCE ? var3 + translateArrayElement(RENDER_DISTANCE, this.renderDistance) : (par1EnumOptions == GameOption.DIFFICULTY ? var3 + translateArrayElement(DIFFICULTY, this.difficultyLevel) : (par1EnumOptions == GameOption.GUI_SCALE ? var3 + translateArrayElement(GUI_SCALE, this.guiScale) : (par1EnumOptions == GameOption.CHAT_VISIBILITY ? var3 + translateArrayElement(CHAT_VISIBILITY, this.chatVisibility) : (par1EnumOptions == GameOption.PARTICLES ? var3 + translateArrayElement(PARTICLES, this.particle) : (par1EnumOptions == GameOption.FRAMERATE_LIMIT ? var3 + translateArrayElement(STREAM_CHAT_USERFILTER, this.maxFramerate) : (par1EnumOptions == GameOption.GRAPHICS ? (this.fancyGraphics ? var3 + var2.translate("options.graphics.fancy") : var3 + var2.translate("options.graphics.fast")) : var3))))));
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void save() {
        if (!FMLClientHandler.instance().isLoading()) {
            try {
                PrintWriter var1 = new PrintWriter(new FileWriter(this.optionsFile));
                var1.println("music:" + this.musicVolume);
                var1.println("sound:" + this.soundVolume);
                var1.println("invertYMouse:" + this.invertYMouse);
                var1.println("mouseSensitivity:" + this.sensitivity);
                var1.println("fov:" + this.fov);
                var1.println("gamma:" + this.gamma);
                var1.println("viewDistance:" + this.renderDistance);
                var1.println("guiScale:" + this.guiScale);
                var1.println("particles:" + this.particle);
                var1.println("bobView:" + this.bobView);
                var1.println("anaglyph3d:" + this.anaglyph3d);
                var1.println("advancedOpengl:" + this.advancedOpengl);
                var1.println("fpsLimit:" + this.maxFramerate);
                var1.println("difficulty:" + this.difficultyLevel);
                var1.println("fancyGraphics:" + this.fancyGraphics);
                var1.println("ao:" + this.ambientOcculsion);
                var1.println("clouds:" + this.renderClouds);
                var1.println("skin:" + this.currentTexturePackName);
                var1.println("lastServer:" + this.lastServer);
                var1.println("lang:" + this.language);
                var1.println("chatVisibility:" + this.chatVisibility);
                var1.println("chatColors:" + this.chatColor);
                var1.println("chatLinks:" + this.chatLink);
                var1.println("chatLinksPrompt:" + this.chatLinkPrompt);
                var1.println("chatOpacity:" + this.chatOpacity);
                var1.println("serverTextures:" + this.useServerTextures);
                var1.println("snooperEnabled:" + this.snopperEnabled);
                var1.println("fullscreen:" + this.fullscreen);
                var1.println("enableVsync:" + this.vsync);
                var1.println("hideServerAddress:" + this.hideServerAddress);
                KeyBinding[] var2 = this.keysAll;

                for (KeyBinding var5 : var2) {
                    var1.println("key_" + var5.translationKey + ":" + var5.code);
                }

                var1.close();
            } catch (Exception var6) {
                System.out.println("Failed to save options");
                var6.printStackTrace();
            }

            if (this.field_946.playerEntity != null) {
                this.field_946.playerEntity.field_1667.sendPacket(new class_651(this.language, this.renderDistance, this.chatVisibility, this.chatColor, this.difficultyLevel));
            }

        }
    }
}
