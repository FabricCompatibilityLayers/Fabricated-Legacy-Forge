/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.FMLCommonHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiMainMenu.class)
public class GuiMainMenuMixin extends GuiScreen {
    @WrapOperation(method = "func_73866_w_", at = @At(value = "NEW", target = "Lnet/minecraft/src/GuiButton;", ordinal = 0))
    private GuiButton fml$moveTexturePackButton(int p_i3055_1_, int p_i3055_2_, int p_i3055_3_, String p_i3055_4_, Operation<GuiButton> original) {
        if (FabricLoader.getInstance().isModLoaded("modmenu")) {
            return original.call(p_i3055_1_, p_i3055_2_, p_i3055_3_, p_i3055_4_);
        }

        return new GuiButton(p_i3055_1_, p_i3055_2_, p_i3055_3_, 98, 20, p_i3055_4_);
    }

    @Inject(method = "func_73866_w_", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;field_71448_m:Z"))
    private void fml$addModsButton(CallbackInfo ci, @Local int var4) {
        if (!FabricLoader.getInstance().isModLoaded("modmenu")) this.field_73887_h.add(new GuiButton(6, this.field_73880_f / 2 + 2, var4 + 48, 98, 20, "Mods"));
    }

    @Inject(method = "func_73875_a", at = @At("RETURN"))
    private void fml$onModsButtonClicked(GuiButton p_73875_1_, CallbackInfo ci) {
        if (!FabricLoader.getInstance().isModLoaded("modmenu") && p_73875_1_.field_73741_f == 6)
        {
            this.field_73882_e.func_71373_a(new GuiModList(this));
        }
    }

    @WrapOperation(method = "func_73863_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiMainMenu;func_73731_b(Lnet/minecraft/src/FontRenderer;Ljava/lang/String;III)V", ordinal = 0))
    private void fml$drawMultilineBranding(GuiMainMenu instance, FontRenderer fontRenderer, String s, int x, int y, int z, Operation<Void> original) {
        List<String> brandings = Lists.reverse(FMLCommonHandler.instance().getBrandings());
        brandings.add(s);

        for (int i = 0; i < brandings.size(); i++) {
            String brd = brandings.get(i);

            if (!Strings.isNullOrEmpty(brd)) {
                this.func_73731_b(this.field_73886_k, brd, 2, this.field_73881_g - ( 10 + i * (this.field_73886_k.field_78288_b + 1)), 16777215);
            }
        }
    }
}
