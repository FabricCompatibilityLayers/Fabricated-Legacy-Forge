package fr.catcore.fabricatedforge.mixin.modloader.common;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RecipeDispatcher.class)
public interface RecipeDispatcherAccessor {

    @Invoker("method_3495")
    void method_3495_invoker(ItemStack itemStack, Object... objects);

    @Invoker("registerShapelessRecipe")
    void registerShapelessRecipe_invoker(ItemStack result, Object... args);
}
