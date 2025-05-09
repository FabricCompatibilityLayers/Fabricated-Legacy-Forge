package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import net.minecraft.src.CraftingManager;
import net.minecraft.src.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftingManager.class)
public class CraftingManagerMixin {
    @Redirect(method = "findMatchingRecipe", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Item;isDamageable()Z"))
    private boolean forge$isRepairable(Item instance) {
        return instance.isRepairable();
    }
}
