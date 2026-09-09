/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.client.osl.keybinds.self.forge;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.client.GuiAccessor;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiControls;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.Tessellator;
import net.minecraftforge.client.GuiControlsScrollPanel;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.keybinds.api.keybind.KeyBindingExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

@Environment(EnvType.CLIENT)
@Conditional(modLoaded = @Mod(value = "osl-keybinds", version = ">=0.3.0-"))
@Mixin(GuiControlsScrollPanel.class)
public class GuiControlsScrollPanelMixin {

    @Shadow
    private GuiControls controls;
    @Shadow
    private Minecraft mc;
    @Unique
    private KeyBinding[] reordered;
    @Unique
    private Int2IntMap newToOldIndex = new Int2IntOpenHashMap();
    @Unique
    private Int2ObjectMap<String> categoryStartingIndex = new Int2ObjectOpenHashMap<>();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void osl$reorder(GuiControls controls, GameSettings options, Minecraft mc, CallbackInfo ci) {
        reordered = new KeyBinding[options.keyBindings.length];
        System.arraycopy(options.keyBindings, 0, reordered, 0, options.keyBindings.length);
        Arrays.sort(reordered, Comparator.comparing(k -> ((KeyBindingExtension) k).getCategory(), (cat1, cat2) -> {
            if (Objects.equals(cat1, "Vanilla") && Objects.equals(cat2, "Vanilla"))
                return 0;
            if (Objects.equals(cat1, "Vanilla"))
                return -1;
            if (Objects.equals(cat2, "Vanilla"))
                return 1;
            if (Objects.equals(cat1, "None") && Objects.equals(cat2, "None"))
                return 0;
            if (Objects.equals(cat1, "None"))
                return 1;
            if (Objects.equals(cat2, "None"))
                return -1;
            return cat1.compareTo(cat2);
        }));

        for (int i = 0; i < reordered.length; i++) {
            newToOldIndex.put(i, Arrays.asList(options.keyBindings).indexOf(reordered[i]));
        }

        String currentCategory = null;
        for (int i = 0; i < reordered.length; i++) {
            String category = ((KeyBindingExtension) reordered[i]).getCategory();
            if (!category.equals(currentCategory)) {
                categoryStartingIndex.put(i, category);
                currentCategory = category;
            }
        }
    }

    @WrapOperation(method = "drawSlot", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/src/GameSettings;getKeyBindingDescription(I)Ljava/lang/String;"),
            @At(value = "INVOKE", target = "Lnet/minecraft/src/GameSettings;getOptionDisplayString(I)Ljava/lang/String;")
    })
    private String osl$redirectIndex(GameSettings instance, int i, Operation<String> original) {
        return original.call(instance, newToOldIndex.get(i));
    }

    @Definition(id = "keyBindings", field = "Lnet/minecraft/src/GameSettings;keyBindings:[Lnet/minecraft/src/KeyBinding;")
    @Expression("?.keyBindings[?]")
    @WrapOperation(method = "drawSlot", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
    private KeyBinding osl$redirectIndex(KeyBinding[] array, int index, Operation<KeyBinding> original) {
        return original.call(array, newToOldIndex.get(index));
    }

    @Inject(method = "drawSlot", at = @At("RETURN"))
    private void osl$drawCategoryName(int index, int xPosition, int yPosition, int l, Tessellator tessellator, CallbackInfo ci) {
        if (!categoryStartingIndex.containsKey(index)) return;

        String category = categoryStartingIndex.get(index);
        ((GuiAccessor) controls).callDrawHorizontalLine(xPosition, xPosition + 200, yPosition + 20, 0xFFFFFF);
        controls.drawString(mc.fontRenderer, category, xPosition - 4 - mc.fontRenderer.getStringWidth(category), yPosition + 3, 0xFFFFFF);
    }

    @Definition(id = "index", local = @Local(type = int.class, ordinal = 0, argsOnly = true))
    @Expression("? != index")
    @WrapOperation(method = "drawSlot", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean osl$fixIndexComparison(int left, int right, Operation<Boolean> original) {
        return original.call(left, newToOldIndex.get(right));
    }

    @WrapOperation(method = {
            "elementClicked",
            "drawScreen",
            "keyTyped"
    }, at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GameSettings;setKeyBinding(II)V"))
    private void osl$fixSetKey(GameSettings instance, int par2, int i, Operation<Void> original) {
        original.call(instance, newToOldIndex.get(par2), i);
    }
}
