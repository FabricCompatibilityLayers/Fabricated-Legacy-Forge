/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import cpw.mods.fml.client.TextureFXManager;
import net.minecraft.src.Block;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @IfModAbsent("fabricated-forge")
    @Definition(id = "field_71973_m", field = "Lnet/minecraft/src/Block;field_71973_m:[Lnet/minecraft/src/Block;")
    @Definition(id = "p_78443_2_", local = @Local(type = ItemStack.class))
    @Definition(id = "field_77993_c", field = "Lnet/minecraft/src/ItemStack;field_77993_c:I")
    @Expression("field_71973_m[p_78443_2_.field_77993_c]")
    @WrapOperation(method = "func_78443_a", at = @At("MIXINEXTRAS:EXPRESSION"))
    private Block fml$fixBlocksOOB(Block[] array, int index, Operation<Block> original) {
        if (index >= 0 && index < array.length) {
            return original.call(array, index);
        }

        return null;
    }

    /* Gets the width/16 of the currently bound texture, used
     * to fix the side rendering issues on textures != 16 */
    @WrapOperation(method = "func_78439_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;func_78375_b(FFF)V", ordinal = 2))
    private void fml$storeFixedTextureDimensions(Tessellator instance, float p_78375_2_, float p_78375_3_, float v, Operation<Void> original,
                                                 @Share(value = "tileSize", namespace = "fml") LocalIntRef tileSizeRef, @Share(value = "tx", namespace = "fml") LocalFloatRef txRef, @Share(value = "tz", namespace = "fml") LocalFloatRef tzRef) {
        original.call(instance, p_78375_2_, p_78375_3_, v);

        tileSizeRef.set(TextureFXManager.instance().getTextureDimensions(GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)).width / 16);

        txRef.set(1.0f / (32 * tileSizeRef.get()));
        tzRef.set(1.0f / tileSizeRef.get());
    }

    @ModifyConstant(method = "func_78439_a", constant = @Constant(intValue = 16))
    private int fml$fixTileSize(int constant, @Share(value = "tileSize", namespace = "fml") LocalIntRef tileSizeRef) {
        return tileSizeRef.get();
    }

    @ModifyConstant(method = "func_78439_a", constant = @Constant(floatValue = 16.0f))
    private float fml$fixTileSize(float constant, @Share(value = "tileSize", namespace = "fml") LocalIntRef tileSizeRef) {
        return tileSizeRef.get();
    }

    @ModifyConstant(method = "func_78439_a", constant = @Constant(floatValue = 0.001953125f))
    private float fml$fixTx(float constant, @Share(value = "tx", namespace = "fml") LocalFloatRef txRef) {
        return txRef.get();
    }

    @ModifyConstant(method = "func_78439_a", constant = {
            @Constant(floatValue = 0.0625f, ordinal = 1),
            @Constant(floatValue = 0.0625f, ordinal = 2),
    })
    private float fml$fixTz(float constant, @Share(value = "tz", namespace = "fml") LocalFloatRef tzRef) {
        return tzRef.get();
    }
}
