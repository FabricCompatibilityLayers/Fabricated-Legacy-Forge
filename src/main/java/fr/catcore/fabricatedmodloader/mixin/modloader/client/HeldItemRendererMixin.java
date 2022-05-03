package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.class_1557;
import net.minecraft.client.class_535;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

//    @Unique
//    private int cachedItemId = -1;
//
//    @Inject(
//            method = "method_1357",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V",
//                    remap = false,
//                    ordinal = 0
//            )
//    )
//    private void modLoader$renderfixPart1(MobEntity mobEntity, ItemStack itemStack, int i, CallbackInfo ci) {
//        if (itemStack.id >= Block.BLOCKS.length) {
//            this.cachedItemId = itemStack.id;
//            itemStack.id = 0;
//        } else {
//            this.cachedItemId = -1;
//        }
//    }
//
//    @ModifyVariable(
//            method = "method_1357",
//            at = @At("STORE")
//    )
//    private Block modLoader$renderfixPart2(Block value) {
//        if (this.cachedItemId != -1 && this.cachedItemId >= Block.BLOCKS.length) {
//            return null;
//        }
//        return value;
//    }
//
//    @Inject(
//            method = "method_1357",
//            at = @At(
//                    value = "FIELD",
//                    opcode = Opcodes.GETFIELD,
//                    target = "Lnet/minecraft/item/ItemStack;id:I",
//                    shift = At.Shift.AFTER,
//                    ordinal = 0
//            )
//    )
//    private void modLoader$renderfixPart2(MobEntity mobEntity, ItemStack itemStack, int i, CallbackInfo ci) {
//        if (this.cachedItemId != -1 && this.cachedItemId >= Block.BLOCKS.length) {
//            itemStack.id = this.cachedItemId;
//        }
//    }

    @Shadow
    private Minecraft field_1876;

    @Shadow
    private class_535 field_1880;

    @Shadow
    public static void method_4306(Tessellator tessellator, float f, float g, float h, float i, int j, int k, float l) {
    }

    /**
     * @reason ModLoader patch
     * @author Risugami
     */
    @Overwrite
    public void method_1357(MobEntity mobEntity, ItemStack itemStack, int i) {
        GL11.glPushMatrix();
        Block block = null;
        if (itemStack.id < Block.BLOCKS.length) {
            block = Block.BLOCKS[itemStack.id];
        }

        if (itemStack.method_3429() == 0 && block != null && class_535.method_1455(block.getBlockType())) {
            this.field_1876.field_3813.method_5146("/terrain.png");
            this.field_1880.method_1447(Block.BLOCKS[itemStack.id], itemStack.getMeta(), 1.0F);
        } else {
            class_1557 var4 = mobEntity.method_2630(itemStack, i);
            if (var4 == null) {
                GL11.glPopMatrix();
                return;
            }

            if (itemStack.method_3429() == 0) {
                this.field_1876.field_3813.method_5146("/terrain.png");
            } else {
                this.field_1876.field_3813.method_5146("/gui/items.png");
            }

            Tessellator var5 = Tessellator.INSTANCE;
            float var6 = var4.getMinU();
            float var7 = var4.getMaxU();
            float var8 = var4.getMinV();
            float var9 = var4.getMaxV();
            float var10 = 0.0F;
            float var11 = 0.3F;
            GL11.glEnable(32826);
            GL11.glTranslatef(-var10, -var11, 0.0F);
            float var12 = 1.5F;
            GL11.glScalef(var12, var12, var12);
            GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
            method_4306(var5, var7, var8, var6, var9, var4.method_5349(), var4.method_5350(), 0.0625F);
            if (itemStack != null && itemStack.hasEnchantmentGlint() && i == 0) {
                GL11.glDepthFunc(514);
                GL11.glDisable(2896);
                this.field_1876.field_3813.method_5146("%blur%/misc/glint.png");
                GL11.glEnable(3042);
                GL11.glBlendFunc(768, 1);
                float var13 = 0.76F;
                GL11.glColor4f(0.5F * var13, 0.25F * var13, 0.8F * var13, 1.0F);
                GL11.glMatrixMode(5890);
                GL11.glPushMatrix();
                float var14 = 0.125F;
                GL11.glScalef(var14, var14, var14);
                float var15 = (float) (Minecraft.getTime() % 3000L) / 3000.0F * 8.0F;
                GL11.glTranslatef(var15, 0.0F, 0.0F);
                GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                method_4306(var5, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(var14, var14, var14);
                var15 = (float) (Minecraft.getTime() % 4873L) / 4873.0F * 8.0F;
                GL11.glTranslatef(-var15, 0.0F, 0.0F);
                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                method_4306(var5, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
                GL11.glPopMatrix();
                GL11.glMatrixMode(5888);
                GL11.glDisable(3042);
                GL11.glEnable(2896);
                GL11.glDepthFunc(515);
            }

            GL11.glDisable(32826);
        }

        GL11.glPopMatrix();
    }
}
