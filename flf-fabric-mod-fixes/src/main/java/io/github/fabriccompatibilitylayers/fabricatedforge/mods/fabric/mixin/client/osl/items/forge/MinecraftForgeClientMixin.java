/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.client.osl.items.forge;

import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.ArrayMapper;
import net.ornithemc.osl.registries.api.registry.sync.DynamicArray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(MinecraftForgeClient.class)
public class MinecraftForgeClientMixin {
    @Shadow
    private static IItemRenderer[] customItemRenderers;

    @Inject(method = "registerItemRenderer", at = @At("HEAD"))
    private static void osl$growArray(int itemID, IItemRenderer renderer, CallbackInfo ci) {
        customItemRenderers = DynamicArray.grow(customItemRenderers, itemID + 1);
    }

    @Unique
    @Subscribe
    public void osl$registerMapper(FMLLoadCompleteEvent complete) {
        SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("forge", "minecraft_forge_client/custom_item_renderers"), ArrayMapper.of(
                () -> customItemRenderers,
                a -> customItemRenderers = a
        ));
    }
}
