package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import net.minecraft.block.Block;
import net.minecraft.client.class_1557;
import net.minecraft.client.class_535;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin extends EntityRenderer {

//    @Unique
//    private int cachedItemId = -1;
//
//    @Inject(
//            method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V",
//                    remap = false
//            )
//    )
//    private void modLoader$renderfixPart1(ItemEntity d, double e, double f, double g, float h, float par6, CallbackInfo ci) {
//        if (d.method_4548().id >= Block.BLOCKS.length) {
//            this.cachedItemId = d.method_4548().id;
//            d.method_4548().id = 0;
//        } else {
//            this.cachedItemId = -1;
//        }
//    }
//
//    @ModifyVariable(
//            method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V",
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
//            method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V",
//            at = @At(
//                    value = "FIELD",
//                    opcode = Opcodes.GETFIELD,
//                    target = "Lnet/minecraft/item/ItemStack;id:I",
//                    shift = At.Shift.AFTER
//            )
//    )
//    private void modLoader$renderfixPart2(ItemEntity d, double e, double f, double g, float h, float par6, CallbackInfo ci) {
//        if (this.cachedItemId != -1 && this.cachedItemId >= Block.BLOCKS.length) {
//            d.method_4548().id = this.cachedItemId;
//        }
//    }

    @Shadow
    private Random field_2126;

    @Shadow
    public static boolean field_5197;

    @Shadow
    private class_535 field_2125;

    @Shadow
    public boolean field_2123;

    @Shadow
    protected abstract void method_4335(ItemEntity itemEntity, class_1557 arg, int i, float f, float g, float h, float j);

    /**
     * @reason ModLoader patch
     * @author Risugami
     */
    @Overwrite
    public void render(ItemEntity itemEntity, double d, double e, double f, float g, float h) {
        this.field_2126.setSeed(187L);
        ItemStack var10 = itemEntity.method_4548();
        if (var10.getItem() != null) {
            GL11.glPushMatrix();
            float var11 = MathHelper.sin(((float) itemEntity.age + h) / 10.0F + itemEntity.hoverHeight) * 0.1F + 0.1F;
            float var12 = (((float) itemEntity.age + h) / 20.0F + itemEntity.hoverHeight) * 57.295776F;
            byte var13 = 1;
            if (itemEntity.method_4548().count > 1) {
                var13 = 2;
            }

            if (itemEntity.method_4548().count > 5) {
                var13 = 3;
            }

            if (itemEntity.method_4548().count > 20) {
                var13 = 4;
            }

            if (itemEntity.method_4548().count > 40) {
                var13 = 5;
            }

            GL11.glTranslatef((float) d, (float) e + var11, (float) f);
            GL11.glEnable(32826);

            Block var28 = null;
            if (var10.id < Block.BLOCKS.length) {
                var28 = Block.BLOCKS[var10.id];
            }

            int var17;
            float var18;
            float var19;
            float var20;
            if (var10.method_3429() == 0 && var28 != null && class_535.method_1455(var28.getBlockType())) {
                GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);
                if (field_5197) {
                    GL11.glScalef(1.25F, 1.25F, 1.25F);
                    GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                this.method_1529("/terrain.png");
                float var24 = 0.25F;
                int var25 = var28.getBlockType();
                if (var25 == 1 || var25 == 19 || var25 == 12 || var25 == 2) {
                    var24 = 0.5F;
                }

                GL11.glScalef(var24, var24, var24);

                for (var17 = 0; var17 < var13; ++var17) {
                    GL11.glPushMatrix();
                    if (var17 > 0) {
                        var18 = (this.field_2126.nextFloat() * 2.0F - 1.0F) * 0.2F / var24;
                        var19 = (this.field_2126.nextFloat() * 2.0F - 1.0F) * 0.2F / var24;
                        var20 = (this.field_2126.nextFloat() * 2.0F - 1.0F) * 0.2F / var24;
                        GL11.glTranslatef(var18, var19, var20);
                    }

                    var18 = 1.0F;
                    this.field_2125.method_1447(var28, var10.getMeta(), var18);
                    GL11.glPopMatrix();
                }
            } else {
                float var16;
                if (var10.getItem().method_3397()) {
                    if (field_5197) {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    } else {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    this.method_1529("/gui/items.png");

                    for (int var14 = 0; var14 <= 1; ++var14) {
                        this.field_2126.setSeed(187L);
                        class_1557 var15 = var10.getItem().method_3369(var10.getMeta(), var14);
                        var16 = 1.0F;
                        if (this.field_2123) {
                            var17 = Item.ITEMS[var10.id].getDisplayColor(var10, var14);
                            var18 = (float) (var17 >> 16 & 255) / 255.0F;
                            var19 = (float) (var17 >> 8 & 255) / 255.0F;
                            var20 = (float) (var17 & 255) / 255.0F;
                            GL11.glColor4f(var18 * var16, var19 * var16, var20 * var16, 1.0F);
                            this.method_4335(itemEntity, var15, var13, h, var18 * var16, var19 * var16, var20 * var16);
                        } else {
                            this.method_4335(itemEntity, var15, var13, h, 1.0F, 1.0F, 1.0F);
                        }
                    }
                } else {
                    if (field_5197) {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    } else {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    class_1557 var21 = var10.method_5465();
                    if (var10.method_3429() == 0) {
                        this.method_1529("/terrain.png");
                    } else {
                        this.method_1529("/gui/items.png");
                    }

                    if (this.field_2123) {
                        int var23 = Item.ITEMS[var10.id].getDisplayColor(var10, 0);
                        var16 = (float) (var23 >> 16 & 255) / 255.0F;
                        float var26 = (float) (var23 >> 8 & 255) / 255.0F;
                        var18 = (float) (var23 & 255) / 255.0F;
                        var19 = 1.0F;
                        this.method_4335(itemEntity, var21, var13, h, var16 * var19, var26 * var19, var18 * var19);
                    } else {
                        this.method_4335(itemEntity, var21, var13, h, 1.0F, 1.0F, 1.0F);
                    }
                }
            }

            GL11.glDisable(32826);
            GL11.glPopMatrix();
        }
    }

    @Unique
    private int cachedItemId2 = -1;

    @Inject(method = "method_4335", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V", ordinal = 1, remap = false))
    private void modLoader$otherRenderFix1(ItemEntity arg, class_1557 i, int f, float g, float h, float j, float par7, CallbackInfo ci) {
        if (arg.method_4548().id >= Block.BLOCKS.length) {
            this.cachedItemId2 = arg.method_4548().id;
            arg.method_4548().id = 0;
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
            at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V", ordinal = 0, remap = false)
    )
    private void modLoader$otherRenderFix3(ItemEntity arg, class_1557 i, int f, float g, float h, float j, float par7, CallbackInfo ci) {
        if (this.cachedItemId2 != -1) {
            arg.method_4548().id = this.cachedItemId2;
            this.cachedItemId2 = -1;
        }
    }
}
