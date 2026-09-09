/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.items.self.forge;

import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.MutableItemStackListMapMapper;
import net.minecraft.src.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
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
import java.util.HashMap;

@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(OreDictionary.class)
public class OreDictionaryMixin {
    @Shadow
    private static HashMap<Integer, ArrayList<ItemStack>> oreStacks;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void osl$registerMapper(CallbackInfo ci) {
        SyncedRegistries.registerMapper(
                RegistryKeys.ITEM,
                NamespacedIdentifiers.from("forge", "ore_dictionary/ore_stacks"),
                MutableItemStackListMapMapper.of(oreStacks)
        );
    }
}
