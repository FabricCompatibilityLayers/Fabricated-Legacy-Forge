package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import net.minecraft.block.Block;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.ItemEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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
}
