/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items;

import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

import java.util.*;

public class FLFRecipesMapper implements IdMapper {
    private final List<Object> recipes;
    private final List<Object> missing;
    private boolean applied;

    public static FLFRecipesMapper of(List<Object> recipes) {
        return new FLFRecipesMapper(recipes);
    }

    private FLFRecipesMapper(List<Object> recipes) {
        this.recipes = recipes;
        this.missing = new ArrayList<>();
    }

    @Override
    public void apply(RegistryMappings registryMappings) {
        this.missing.clear();
        Objects.requireNonNull(registryMappings);
        this.fixRecipes(registryMappings::remap, true);
        this.applied = true;
    }

    @Override
    public void undo(RegistryMappings registryMappings) {
        if (this.applied) {
            Objects.requireNonNull(registryMappings);
            this.fixRecipes(registryMappings::unmap, false);
            this.recipes.addAll(this.missing);
        }

        this.applied = false;
    }

    private void fixRecipes(FLFFixableRecipe.ItemMapper mapper, boolean storeMissing) {
        Iterator<Object> it = this.recipes.iterator();

        while(it.hasNext()) {
            Object recipe = it.next();
            if (this.canFixRecipe(mapper, recipe)) {
                this.fixRecipe(mapper, recipe);
            } else {
                it.remove();
                if (storeMissing) {
                    this.missing.add(recipe);
                }
            }
        }

    }

    private boolean canFixRecipe(FLFFixableRecipe.ItemMapper mapper, Object recipe) {
        return recipe instanceof FLFFixableRecipe && ((FLFFixableRecipe)recipe).flf$osl$canFixRecipe(mapper);
    }

    private void fixRecipe(FLFFixableRecipe.ItemMapper mapper, Object recipe) {
        ((FLFFixableRecipe)recipe).flf$osl$fixRecipe(mapper);
    }
}
