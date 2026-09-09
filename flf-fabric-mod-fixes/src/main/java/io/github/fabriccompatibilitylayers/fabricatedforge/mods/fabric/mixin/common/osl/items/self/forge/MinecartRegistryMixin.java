/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.items.self.forge;

import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.ItemStack2ObjectMapMapper;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.Object2ItemStackMapMapper;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.MinecartRegistry;
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

import java.util.Map;

@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(MinecartRegistry.class)
public class MinecartRegistryMixin {
    @Shadow
    private static Map<MinecartRegistry.MinecartKey, ItemStack> itemForMinecart;

    @Shadow
    private static Map<ItemStack, MinecartRegistry.MinecartKey> minecartForItem;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void osl$registerMappers(CallbackInfo ci) {
        SyncedRegistries.registerMapper(
                RegistryKeys.ITEM,
                NamespacedIdentifiers.from("forge", "minecart_registry/item_for_minecart"),
                Object2ItemStackMapMapper.of(itemForMinecart)
        );
        SyncedRegistries.registerMapper(
                RegistryKeys.ITEM,
                NamespacedIdentifiers.from("forge", "minecart_registry/minecart_for_item"),
                ItemStack2ObjectMapMapper.of(minecartForItem)
        );
    }
}
