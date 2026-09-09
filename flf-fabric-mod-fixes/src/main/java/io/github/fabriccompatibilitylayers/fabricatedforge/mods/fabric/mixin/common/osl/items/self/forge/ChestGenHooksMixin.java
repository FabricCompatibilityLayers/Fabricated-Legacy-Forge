/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.items.self.forge;

import io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common.WeightedRandomChestContentAccessor;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.ItemStackContainerListMapper;
import net.minecraft.src.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Locale;

@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(ChestGenHooks.class)
public class ChestGenHooksMixin {
    @Shadow
    private ArrayList<WeightedRandomChestContent> contents;

    @Inject(method = "<init>(Ljava/lang/String;)V", at = @At("RETURN"))
    private void osl$registerMapper(String category, CallbackInfo ci) {
        SyncedRegistries.registerMapper(
                RegistryKeys.ITEM,
                NamespacedIdentifiers.from("forge", "chest_gen_hooks/" + category.toLowerCase(Locale.ENGLISH)),
                ItemStackContainerListMapper.of(contents, (content) -> ((WeightedRandomChestContentAccessor) content).getItemStack())
        );
    }
}
