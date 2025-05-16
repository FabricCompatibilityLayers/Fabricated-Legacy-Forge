package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpecialSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

@Mixin(SpawnerAnimals.class)
public class SpawnerAnimalsMixin {
    @Redirect(method = "findChunksForSpawning", at = @At(value = "INVOKE", target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"))
    private static Iterator forge$shuffle(Set instance) {
        ArrayList tmp = new ArrayList<>(instance);
        Collections.shuffle(tmp);
        return tmp.iterator();
    }

    @Definition(id = "bedrock", field = "Lnet/minecraft/src/Block;bedrock:Lnet/minecraft/src/Block;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/Block;blockID:I")
    @Definition(id = "var5", local = @Local(ordinal = 3, type = int.class))
    @Expression("var5 != bedrock.blockID")
    @WrapOperation(method = "canCreatureTypeSpawnAtLocation", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static boolean forge$spawnBlock(int var5, int right, Operation<Boolean> original,
                                            @Local(argsOnly = true) EnumCreatureType par0EnumCreatureType,
                                            @Local(argsOnly = true) World par1World,
                                            @Local(ordinal = 0, argsOnly = true) int par2,
                                            @Local(ordinal = 1, argsOnly = true) int par3,
                                            @Local(ordinal = 2, argsOnly = true) int par4) {
        boolean spawnBlock = (Block.blocksList[var5] != null && ((BlockExtension) Block.blocksList[var5]).canCreatureSpawn(par0EnumCreatureType, par1World, par2, par3 - 1, par4));
        return spawnBlock && original.call(var5, right);
    }

    @Inject(method = "creatureSpecificInit", at = @At("HEAD"), cancellable = true)
    private static void forge$postLivingSpecialSpawnEvent(EntityLiving par0EntityLiving, World par1World, float par2, float par3, float par4, CallbackInfo ci) {
        LivingSpecialSpawnEvent event = new LivingSpecialSpawnEvent(par0EntityLiving, par1World, par2, par3, par4);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isHandeled())
        {
            ci.cancel();
        }
    }
}
