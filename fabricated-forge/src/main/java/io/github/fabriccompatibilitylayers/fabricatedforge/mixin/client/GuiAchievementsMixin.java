/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.src.Achievement;
import net.minecraft.src.AchievementList;
import net.minecraft.src.GuiAchievements;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.StatFileWriter;
import net.minecraftforge.common.AchievementPage;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiAchievements.class)
public abstract class GuiAchievementsMixin extends GuiScreen {
    // achievementsPaneWidth lives on GuiAchievements (not GuiScreen), so it needs @Shadow.
    // width, height, controlList come from the GuiScreen parent we already extend.
    @Shadow protected int achievementsPaneWidth;

    // New instance fields — declared directly on the mixin class; Mixin merges them
    // into the target class at load time. No annotation required for new fields.
    private int currentPage = -1;
    private GuiSmallButton button;
    private LinkedList<Achievement> minecraftAchievements = new LinkedList<Achievement>();

    // Pattern A (@Inject RETURN): appends to the constructor tail — 1:1 translation.
    @Inject(method = "<init>(Lnet/minecraft/src/StatFileWriter;)V", at = @At("RETURN"))
    private void forge$initMinecraftAchievements(StatFileWriter par1StatFileWriter, CallbackInfo ci) {
        minecraftAchievements.clear();
        for (Object achievement : AchievementList.achievementList) {
            if (!AchievementPage.isAchievementInPages((Achievement) achievement)) {
                minecraftAchievements.add((Achievement) achievement);
            }
        }
    }

    // Pattern A (@Inject RETURN): appends the page button after the existing done button — 1:1 translation.
    @Inject(method = "initGui", at = @At("RETURN"))
    private void forge$initGuiPageButton(CallbackInfo ci) {
        this.controlList.add(this.button = new GuiSmallButton(2,
                (this.width - this.achievementsPaneWidth) / 2 + 24,
                this.height / 2 + 74, 125, 20,
                AchievementPage.getTitle(this.currentPage)));
    }

    // Pattern A (@Inject HEAD): runs the id==2 branch before vanilla's id==1 check —
    // the two branches are independent so HEAD vs RETURN is equivalent; HEAD chosen by user.
    @Inject(method = "actionPerformed", at = @At("HEAD"))
    private void forge$actionPerformedPageButton(GuiButton par1GuiButton, CallbackInfo ci) {
        if (par1GuiButton.id == 2) {
            this.currentPage++;
            if (this.currentPage >= AchievementPage.getAchievementPages().size()) {
                this.currentPage = -1;
            }
            this.button.displayString = AchievementPage.getTitle(this.currentPage);
        }
    }

    // Pattern E (@WrapOperation FIELD): intercepts all 4 GETSTATIC reads of
    // AchievementList.achievementList in this method and substitutes the page-filtered list —
    // one injector covers both loops.
    // Logic delta: list is recomputed on each of the 4 reads rather than once as a local; cost is negligible.
    @WrapOperation(
        method = "genAchievementBackground",
        at = @At(value = "FIELD",
                 target = "Lnet/minecraft/src/AchievementList;achievementList:Ljava/util/List;",
                 opcode = Opcodes.GETSTATIC)
    )
    private List forge$getPageAchievementList(Operation<List> original) {
        return this.currentPage == -1
            ? this.minecraftAchievements
            : AchievementPage.getAchievementPage(this.currentPage).getAchievements();
    }

    // Pattern O (@ModifyExpressionValue EXPRESSION): extends the null-parent check to also
    // require the parent is in the current page's list — matching the patch's && addition.
    // Logic delta: achievementList is recomputed inline rather than from a pre-existing local.
    @Definition(id = "parentAchievement", field = "Lnet/minecraft/src/Achievement;parentAchievement:Lnet/minecraft/src/Achievement;")
    @Expression("?.parentAchievement != null")
    @ModifyExpressionValue(method = "genAchievementBackground", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$checkParentInList(boolean original, @Local(ordinal = 0) Achievement var34) {
        if (!original) return false;
        List achievementList = this.currentPage == -1
            ? this.minecraftAchievements
            : AchievementPage.getAchievementPage(this.currentPage).getAchievements();
        return achievementList.contains(var34.parentAchievement);
    }

}