package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import net.minecraft.block.Block;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.ItemEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin extends EntityRenderer {

    @Unique
    private int cachedItemId = -1;

    @Inject(
            method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V",
                    remap = false
            )
    )
    private void modLoader$renderfixPart1(ItemEntity d, double e, double f, double g, float h, float par6, CallbackInfo ci) {
        if (d.method_4548().id >= Block.BLOCKS.length) {
            this.cachedItemId = d.method_4548().id;
            d.method_4548().id = 0;
        } else {
            this.cachedItemId = -1;
        }
    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V",
            at = @At("STORE")
    )
    private Block modLoader$renderfixPart2(Block value) {
        if (this.cachedItemId != -1 && this.cachedItemId >= Block.BLOCKS.length) {
            return null;
        }
        return value;
    }

    @Inject(
            method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/item/ItemStack;id:I",
                    shift = At.Shift.AFTER
            )
    )
    private void modLoader$renderfixPart2(ItemEntity d, double e, double f, double g, float h, float par6, CallbackInfo ci) {
        if (this.cachedItemId != -1 && this.cachedItemId >= Block.BLOCKS.length) {
            d.method_4548().id = this.cachedItemId;
        }
    }

    @Unique
    private int cachedItemId2 = -1;

    @Inject(method = "method_4335", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V", ordinal = 1))
    private void modLoader$otherRenderFix1(ItemEntity i, int j, int f, float g, float h, float k, float par7, CallbackInfo ci) {
        if (i.method_4548().id >= Block.BLOCKS.length) {
            this.cachedItemId2 = i.method_4548().id;
            i.method_4548().id = 0;
        }
    }

    @ModifyArg(method = "method_4335",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;method_1529(Ljava/lang/String;)V", ordinal = 0)
    )
    private String modLoader$otherRenderFix2(String par1) {
        if (this.cachedItemId2 != -1) par1 = "/gui/items.png";

        return par1;
    }

    @Inject(method = "method_4335",
            at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V", ordinal = 0)
    )
    private void modLoader$otherRenderFix3(ItemEntity i, int j, int f, float g, float h, float k, float par7, CallbackInfo ci) {
        if (this.cachedItemId2 != -1) {
            i.method_4548().id = this.cachedItemId2;
            this.cachedItemId2 = -1;
        }
    }
}
