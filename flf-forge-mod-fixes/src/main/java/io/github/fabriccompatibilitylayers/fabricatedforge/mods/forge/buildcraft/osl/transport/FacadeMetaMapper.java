/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.buildcraft.osl.transport;

import buildcraft.BuildCraftTransport;
import buildcraft.api.recipes.AssemblyRecipe;
import buildcraft.transport.ItemFacade;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.src.ItemStack;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FacadeMetaMapper implements IdMapper {
    private final List<ItemStack> facades;
    private final List<AssemblyRecipe> recipes;

    private final Set<ItemStack> missingFacades;
    private final Set<AssemblyRecipe> missingRecipes;
    private boolean applied;

    public static FacadeMetaMapper of(List<ItemStack> facades, List<AssemblyRecipe> recipes) {
        return new FacadeMetaMapper(facades, recipes);
    }

    private FacadeMetaMapper(List<ItemStack> facades, List<AssemblyRecipe> recipes) {
        this.facades = facades;
        this.recipes = recipes;
        this.missingFacades = new HashSet<>();
        this.missingRecipes = new HashSet<>();
    }

    @Override
    public void apply(RegistryMappings registryMappings) {
        fixData(registryMappings::remap, true);
        this.applied = true;
    }

    @Override
    public void undo(RegistryMappings registryMappings) {
        if (this.applied) {
            fixData(registryMappings::unmap, false);
            this.missingFacades.clear();
            this.missingRecipes.clear();

            this.applied = false;
        }
    }

    private void fixData(Int2IntFunction mapper, boolean storeMissing) {
        for (ItemStack stack : facades) {
            if (!this.missingFacades.contains(stack)) {
                if (canFix(mapper, stack)) {
                    fix(mapper, stack);
                } else if (storeMissing) {
                    missingFacades.add(stack);
                }
            }
        }

        for (AssemblyRecipe recipe : recipes) {
            if (applies(recipe) && !this.missingRecipes.contains(recipe)) {
                if (canFix(mapper, recipe.output)) {
                    fix(mapper, recipe.output);
                } else if (storeMissing) {
                    missingRecipes.add(recipe);
                }
            }
        }
    }

    private boolean applies(AssemblyRecipe recipe) {
        return recipe.output.itemID == BuildCraftTransport.facadeItem.shiftedIndex;
    }

    private boolean canFix(Int2IntFunction mapper, ItemStack stack) {
        int blockId = ItemFacade.getBlockId(stack.getItemDamage());
        return blockId >= 0 && mapper.apply(blockId) >= 0;
    }

    private void fix(Int2IntFunction mapper, ItemStack stack) {
        int oldBlockId = ItemFacade.getBlockId(stack.getItemDamage());
        int metadata = ItemFacade.getMetaData(stack.getItemDamage());
        int newBlockId = mapper.apply(oldBlockId);
        stack.setItemDamage(ItemFacade.encode(newBlockId, metadata));
    }
}
