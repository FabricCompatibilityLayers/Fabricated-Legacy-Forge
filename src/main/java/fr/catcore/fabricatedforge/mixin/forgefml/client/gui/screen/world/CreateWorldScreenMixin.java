package fr.catcore.fabricatedforge.mixin.forgefml.client.gui.screen.world;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Language;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin extends Screen {

    @Shadow private ButtonWidget gameModeButton;

    @Shadow private String gamemodeName;

    @Shadow private String firstGameModeDescriptionLine;

    @Shadow private String secondGameModeDescriptionLine;

    @Shadow private ButtonWidget generateStructuresButton;

    @Shadow private boolean structures;

    @Shadow private ButtonWidget bonusChestButton;

    @Shadow private boolean bonusChest;

    @Shadow private boolean hardcore;

    @Shadow private ButtonWidget mapTypeSwitchButton;

    @Shadow private ButtonWidget allowCommandsButton;

    @Shadow private int generatorType;

    @Shadow private boolean tweakedCheats;

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    private void updateSettingsLabels() {
        Language var1 = Language.getInstance();
        this.gameModeButton.message = var1.translate("selectWorld.gameMode") + " " + var1.translate("selectWorld.gameMode." + this.gamemodeName);
        this.firstGameModeDescriptionLine = var1.translate("selectWorld.gameMode." + this.gamemodeName + ".line1");
        this.secondGameModeDescriptionLine = var1.translate("selectWorld.gameMode." + this.gamemodeName + ".line2");
        this.generateStructuresButton.message = var1.translate("selectWorld.mapFeatures") + " ";
        if (this.structures) {
            this.generateStructuresButton.message = this.generateStructuresButton.message + var1.translate("options.on");
        } else {
            this.generateStructuresButton.message = this.generateStructuresButton.message + var1.translate("options.off");
        }

        this.bonusChestButton.message = var1.translate("selectWorld.bonusItems") + " ";
        if (this.bonusChest && !this.hardcore) {
            this.bonusChestButton.message = this.bonusChestButton.message + var1.translate("options.on");
        } else {
            this.bonusChestButton.message = this.bonusChestButton.message + var1.translate("options.off");
        }

        this.mapTypeSwitchButton.message = var1.translate("selectWorld.mapType") + " " + var1.translate(LevelGeneratorType.TYPES[this.generatorType].getTranslationKey());
        this.allowCommandsButton.message = var1.translate("selectWorld.allowCommands") + " ";
        if (this.tweakedCheats && !this.hardcore) {
            this.allowCommandsButton.message = this.allowCommandsButton.message + var1.translate("options.on");
        } else {
            this.allowCommandsButton.message = this.allowCommandsButton.message + var1.translate("options.off");
        }
    }

    @Inject(method = "buttonClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameMode;setGameModeWithString(Ljava/lang/String;)Lnet/minecraft/world/GameMode;"))
    private void FMLOnGUICreateWorldPress(ButtonWidget par1, CallbackInfo ci) {
        LevelGeneratorType.TYPES[this.generatorType].onGUICreateWorldPress();
    }
}
