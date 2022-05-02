package fr.catcore.fabricatedmodloader.mixin.modloader.common;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeDispatcher;
import net.minecraft.recipe.ShapedRecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RecipeDispatcher.class)
public interface RecipeDispatcherAccessor {

    @Invoker("registerShapedRecipe")
    ShapedRecipeType registerShapedRecipe_invoker(ItemStack itemStack, Object... objects);

    @Invoker("registerShapelessRecipe")
    void registerShapelessRecipe_invoker(ItemStack result, Object... args);
}
