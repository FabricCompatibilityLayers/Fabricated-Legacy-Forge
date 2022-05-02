package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import net.minecraft.block.Block;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Unique
    private int cachedItemId = -1;

    @Inject(
            method = "method_1357",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V",
                    remap = false,
                    ordinal = 0
            )
    )
    private void modLoader$renderfixPart1(MobEntity mobEntity, ItemStack itemStack, int i, CallbackInfo ci) {
        if (itemStack.id >= Block.BLOCKS.length) {
            this.cachedItemId = itemStack.id;
            itemStack.id = 0;
        } else {
            this.cachedItemId = -1;
        }
    }

    @ModifyVariable(
            method = "method_1357",
            at = @At("STORE")
    )
    private Block modLoader$renderfixPart2(Block value) {
        if (this.cachedItemId != -1 && this.cachedItemId >= Block.BLOCKS.length) {
            return null;
        }
        return value;
    }

    @Inject(
            method = "method_1357",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/item/ItemStack;id:I",
                    shift = At.Shift.AFTER,
                    ordinal = 0
            )
    )
    private void modLoader$renderfixPart2(MobEntity mobEntity, ItemStack itemStack, int i, CallbackInfo ci) {
        if (this.cachedItemId != -1 && this.cachedItemId >= Block.BLOCKS.length) {
            itemStack.id = this.cachedItemId;
        }
    }
}
