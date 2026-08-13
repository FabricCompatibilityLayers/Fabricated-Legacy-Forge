/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.items.self.fml;

import cpw.mods.fml.common.modloader.ModLoaderVillageTradeHandler;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.TradeEntriesMapper;
import net.minecraft.src.TradeEntry;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(ModLoaderVillageTradeHandler.class)
public class ModLoaderVillageTradeHandlerMixin {

    @Shadow
    private List<TradeEntry> trades;
    @Unique
    private int counter = 0;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void osl$registerMapper(CallbackInfo ci) {
        SyncedRegistries.registerMapper(
                RegistryKeys.ITEM,
                NamespacedIdentifiers.from("fml", "mod_loader_village_trade_handler/" + (counter++) + "/trades"),
                TradeEntriesMapper.of(trades)
        );
    }
}
