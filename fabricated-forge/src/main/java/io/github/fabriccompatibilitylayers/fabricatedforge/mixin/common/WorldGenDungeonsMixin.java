package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import net.minecraft.src.ItemStack;
import net.minecraft.src.WorldGenDungeons;
import net.minecraftforge.common.DungeonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(WorldGenDungeons.class)
public class WorldGenDungeonsMixin {
    @ModifyConstant(method = "generate", constant = @Constant(intValue = 8))
    private int forge$getDungeonLootTries(int constant) {
        return DungeonHooks.getDungeonLootTries();
    }

    @Redirect(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/WorldGenDungeons;pickCheckLootItem(Ljava/util/Random;)Lnet/minecraft/src/ItemStack;"))
    private ItemStack forge$getRandomDungeonLoot(WorldGenDungeons instance, Random random) {
        return DungeonHooks.getRandomDungeonLoot(random);
    }

    @Redirect(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/WorldGenDungeons;pickMobSpawner(Ljava/util/Random;)Ljava/lang/String;"))
    private String forge$getRandomDungeonMob(WorldGenDungeons instance, Random random) {
        return DungeonHooks.getRandomDungeonMob(random);
    }
}
