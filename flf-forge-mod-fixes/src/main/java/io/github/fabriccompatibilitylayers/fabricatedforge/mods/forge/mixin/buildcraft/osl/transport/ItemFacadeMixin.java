/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.mixin.buildcraft.osl.transport;

import buildcraft.api.recipes.AssemblyRecipe;
import buildcraft.transport.ItemFacade;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.ItemUtils;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.buildcraft.osl.transport.FacadeMetaMapper;
import net.minecraft.src.ItemStack;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;

@Pseudo
@Conditional(modLoaded = {@Mod("osl-blocks"), @Mod("osl-items")})
@Mixin(ItemFacade.class)
public class ItemFacadeMixin {

    @Shadow
    @Final
    public static LinkedList<ItemStack> allFacades;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void osl$registerMapper(CallbackInfo ci) {
        SyncedRegistries.registerMapper(RegistryKeys.BLOCK, NamespacedIdentifiers.from("buildcraft", "item_facade/facades_and_recipes"),
                FacadeMetaMapper.of(allFacades, AssemblyRecipe.assemblyRecipes));
    }

    @Definition(id = "ItemStack", type = ItemStack.class)
    @Definition(id = "blockId", local = @Local(type = int.class, name = "blockId"))
    @Expression("new ItemStack(blockId, ?, ?)")
    @WrapOperation(method = "initialize", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static ItemStack osl$fixBlockItemStack(int par2, int par3, int i, Operation<ItemStack> original) {
        return original.call(ItemUtils.itemId(par2), par2, i);
    }
}
