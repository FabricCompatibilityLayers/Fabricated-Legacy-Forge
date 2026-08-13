/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.items.self.forge;

import net.minecraft.src.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.items.impl.item.FixableRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;

@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(ShapelessOreRecipe.class)
public class ShapelessOreRecipeMixin implements FixableRecipe {
    @Shadow
    private ArrayList input;

    @Shadow
    private ItemStack output;

    @Override
    public boolean osl$items$canFixRecipe(ItemMapper itemMapper) {
        for (Object ingredient : input) {
            if (ingredient instanceof ItemStack && !itemMapper.canFixItem((ItemStack) ingredient)) {
                return false;
            }
        }

        return itemMapper.canFixItem(output);
    }

    @Override
    public void osl$items$fixRecipe(ItemMapper itemMapper) {
        for (Object ingredient : input) {
            if (ingredient instanceof ItemStack) {
                itemMapper.fixItem((ItemStack) ingredient);
            }
        }

        itemMapper.fixItem(output);
    }
}
