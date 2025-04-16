package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(EntitySlime.class)
public abstract class EntitySlimeMixin extends EntityLiving {
    public EntitySlimeMixin(World p_i3443_1_) {
        super(p_i3443_1_);
    }

    @ModifyExpressionValue(method = "func_70601_bi", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/WorldInfo;func_76067_t()Lnet/minecraft/src/WorldType;"))
    private WorldType fml$handleSlimeSpawnReduction(WorldType original) {
        if (original.handleSlimeSpawnReduction(field_70146_Z, field_70170_p)) {
            return WorldType.field_77138_c;
        }

        return original;
    }

    @Redirect(method = "func_70601_bi", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", remap = false))
    private int fml$trick2(Random instance, int i) {
        return 2;
    }
}
