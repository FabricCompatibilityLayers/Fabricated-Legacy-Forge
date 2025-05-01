package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.ItemMonsterPlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(ItemMonsterPlacer.class)
public class ItemMonsterPlacerMixin {
    @Redirect(method = "func_77840_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityVillager;func_70938_b(I)V"))
    private static void fml$applyRandomTrade(EntityVillager var9, int i) {
        VillagerRegistry.applyRandomTrade(var9, var9.func_70681_au());
    }

    @Redirect(method = "func_77840_a", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", remap = false))
    private static int fml$trickRandom(Random instance, int i) {
        return 0;
    }
}
