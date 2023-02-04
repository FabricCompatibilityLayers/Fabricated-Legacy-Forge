package fr.catcore.fabricatedforge.mixin.forgefml.client.gui.screen.world;

import fr.catcore.fabricatedforge.mixininterface.ILevelGeneratorType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.Language;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin extends Screen {
    @Shadow private int generatorType;

    @Shadow private Screen parent;

    @Shadow private boolean creatingLevel;

    @Shadow private TextFieldWidget seedField;

    @Shadow private String gamemodeName;

    @Shadow private boolean structures;

    @Shadow private boolean hardcore;

    @Shadow public String generatorOptions;

    @Shadow private boolean bonusChest;

    @Shadow private boolean tweakedCheats;

    @Shadow private String saveDirectoryName;

    @Shadow private TextFieldWidget levelNameField;

    @Shadow private boolean cheatsEnabled;

    @Shadow private ButtonWidget allowCommandsButton;

    @Shadow private ButtonWidget bonusChestButton;

    @Shadow private boolean moreOptionsOpen;

    @Shadow private ButtonWidget gameModeButton;

    @Shadow private ButtonWidget generateStructuresButton;

    @Shadow private ButtonWidget mapTypeSwitchButton;

    @Shadow private ButtonWidget customizeButton;

    @Shadow private ButtonWidget moreWorldOptionsButton;

    @Shadow protected abstract void updateSettingsLabels();

    @Shadow protected abstract void toggleMoreOptions();

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    protected void buttonClicked(ButtonWidget par1GuiButton) {
        if (par1GuiButton.active) {
            if (par1GuiButton.id == 1) {
                this.field_1229.openScreen(this.parent);
            } else if (par1GuiButton.id == 0) {
                this.field_1229.openScreen((Screen)null);
                if (this.creatingLevel) {
                    return;
                }

                this.creatingLevel = true;
                long var2 = new Random().nextLong();
                String var4 = this.seedField.getText();
                if (!MathHelper.isEmpty(var4)) {
                    try {
                        long var5 = Long.parseLong(var4);
                        if (var5 != 0L) {
                            var2 = var5;
                        }
                    } catch (NumberFormatException var7) {
                        var2 = (long)var4.hashCode();
                    }
                }

                ((ILevelGeneratorType)LevelGeneratorType.TYPES[this.generatorType]).onGUICreateWorldPress();
                GameMode var8 = GameMode.setGameModeWithString(this.gamemodeName);
                LevelInfo var6 = new LevelInfo(var2, var8, this.structures, this.hardcore, LevelGeneratorType.TYPES[this.generatorType]);
                var6.setGeneratorOptions(this.generatorOptions);
                if (this.bonusChest && !this.hardcore) {
                    var6.setBonusChest();
                }

                if (this.tweakedCheats && !this.hardcore) {
                    var6.enableCommands();
                }

                this.field_1229.method_2935(this.saveDirectoryName, this.levelNameField.getText().trim(), var6);
            } else if (par1GuiButton.id == 3) {
                this.toggleMoreOptions();
            } else if (par1GuiButton.id == 2) {
                if (this.gamemodeName.equals("survival")) {
                    if (!this.cheatsEnabled) {
                        this.tweakedCheats = false;
                    }

                    this.hardcore = false;
                    this.gamemodeName = "hardcore";
                    this.hardcore = true;
                    this.allowCommandsButton.active = false;
                    this.bonusChestButton.active = false;
                    this.updateSettingsLabels();
                } else if (this.gamemodeName.equals("hardcore")) {
                    if (!this.cheatsEnabled) {
                        this.tweakedCheats = true;
                    }

                    this.hardcore = false;
                    this.gamemodeName = "creative";
                    this.updateSettingsLabels();
                    this.hardcore = false;
                    this.allowCommandsButton.active = true;
                    this.bonusChestButton.active = true;
                } else {
                    if (!this.cheatsEnabled) {
                        this.tweakedCheats = false;
                    }

                    this.gamemodeName = "survival";
                    this.updateSettingsLabels();
                    this.allowCommandsButton.active = true;
                    this.bonusChestButton.active = true;
                    this.hardcore = false;
                }

                this.updateSettingsLabels();
            } else if (par1GuiButton.id == 4) {
                this.structures = !this.structures;
                this.updateSettingsLabels();
            } else if (par1GuiButton.id == 7) {
                this.bonusChest = !this.bonusChest;
                this.updateSettingsLabels();
            } else if (par1GuiButton.id == 5) {
                ++this.generatorType;
                if (this.generatorType >= LevelGeneratorType.TYPES.length) {
                    this.generatorType = 0;
                }

                while(LevelGeneratorType.TYPES[this.generatorType] == null || !LevelGeneratorType.TYPES[this.generatorType].isVisible()) {
                    ++this.generatorType;
                    if (this.generatorType >= LevelGeneratorType.TYPES.length) {
                        this.generatorType = 0;
                    }
                }

                this.generatorOptions = "";
                this.updateSettingsLabels();
                this.setMoreOptionsOpen(this.moreOptionsOpen);
            } else if (par1GuiButton.id == 6) {
                this.cheatsEnabled = true;
                this.tweakedCheats = !this.tweakedCheats;
                this.updateSettingsLabels();
            } else if (par1GuiButton.id == 8) {
                ((ILevelGeneratorType)LevelGeneratorType.TYPES[this.generatorType]).onCustomizeButton(this.field_1229, (CreateWorldScreen)(Object) this);
            }
        }
    }

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    private void setMoreOptionsOpen(boolean par1) {
        this.moreOptionsOpen = par1;
        this.gameModeButton.visible = !this.moreOptionsOpen;
        this.generateStructuresButton.visible = this.moreOptionsOpen;
        this.bonusChestButton.visible = this.moreOptionsOpen;
        this.mapTypeSwitchButton.visible = this.moreOptionsOpen;
        this.allowCommandsButton.visible = this.moreOptionsOpen;
        this.customizeButton.visible = this.moreOptionsOpen && ((ILevelGeneratorType)LevelGeneratorType.TYPES[this.generatorType]).isCustomizable();
        if (this.moreOptionsOpen) {
            Language var2 = Language.getInstance();
            this.moreWorldOptionsButton.message = var2.translate("gui.done");
        } else {
            Language var2 = Language.getInstance();
            this.moreWorldOptionsButton.message = var2.translate("selectWorld.moreWorldOptions");
        }
    }
}
