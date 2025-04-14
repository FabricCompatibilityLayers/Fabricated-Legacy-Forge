package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import cpw.mods.fml.client.TextureFXManager;
import net.minecraft.src.Block;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @WrapOperation(method = "func_78443_a", at = @At(
            value = "FIELD",
            opcode = Opcodes.GETSTATIC,
            target = "Lnet/minecraft/src/Block;field_71973_m:[Lnet/minecraft/src/Block;",
            args = "array=get"
    ))
    private Block fml$fixBlocksOOB(Block[] array, int index, Operation<Block> original) {
        if (index >= 0 && index < array.length) {
            return original.call(array, index);
        }

        return null;
    }

    /* Gets the width/16 of the currently bound texture, used
     * to fix the side rendering issues on textures != 16 */
    @Inject(method = "func_78439_a", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/src/Tessellator;func_78375_b(FFF)V", ordinal = 2))
    private void fml$storeFixedTextureDimensions(Tessellator p_78439_2_, float p_78439_3_, float p_78439_4_, float p_78439_5_, float par5, CallbackInfo ci,
                                                 @Share(value = "tileSize", namespace = "fml") LocalIntRef tileSizeRef, @Share(value = "tx", namespace = "fml") LocalFloatRef txRef, @Share(value = "tz", namespace = "fml") LocalFloatRef tzRef) {
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
