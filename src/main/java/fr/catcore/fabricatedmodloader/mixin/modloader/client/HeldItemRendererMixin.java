package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
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

    @Shadow
    private Minecraft field_1876;

    @Shadow
    private class_535 field_1880;

    @Shadow
    protected abstract void method_4306(Tessellator tessellator, float f, float g, float h, float i);

    /**
     * @author
     */
    @Overwrite
    public void method_1357(MobEntity mobEntity, ItemStack itemStack, int i) {
        GL11.glPushMatrix();
        Block var4 = itemStack.id < Block.BLOCKS.length ? Block.BLOCKS[itemStack.id] : null;
        if (var4 != null && class_535.method_1455(var4.getBlockType())) {
            GL11.glBindTexture(3553, this.field_1876.field_3813.getTextureFromPath("/terrain.png"));
            this.field_1880.method_1447(var4, itemStack.getMeta(), 1.0F);
        } else {
            if (var4 != null) {
                GL11.glBindTexture(3553, this.field_1876.field_3813.getTextureFromPath("/terrain.png"));
            } else {
                GL11.glBindTexture(3553, this.field_1876.field_3813.getTextureFromPath("/gui/items.png"));
            }

            Tessellator var5 = Tessellator.INSTANCE;
            int var6 = mobEntity.method_2630(itemStack, i);
            float var7 = ((float) (var6 % 16 * 16) + 0.0F) / 256.0F;
            float var8 = ((float) (var6 % 16 * 16) + 15.99F) / 256.0F;
            float var9 = ((float) (var6 / 16 * 16) + 0.0F) / 256.0F;
            float var10 = ((float) (var6 / 16 * 16) + 15.99F) / 256.0F;
            float var11 = 0.0F;
            float var12 = 0.3F;
            GL11.glEnable(32826);
            GL11.glTranslatef(-var11, -var12, 0.0F);
            float var13 = 1.5F;
            GL11.glScalef(var13, var13, var13);
            GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
            this.method_4306(var5, var8, var9, var7, var10);
            if (itemStack != null && itemStack.hasEnchantmentGlint() && i == 0) {
                GL11.glDepthFunc(514);
                GL11.glDisable(2896);
                this.field_1876.field_3813.method_1426(this.field_1876.field_3813.getTextureFromPath("%blur%/misc/glint.png"));
                GL11.glEnable(3042);
                GL11.glBlendFunc(768, 1);
                float var14 = 0.76F;
                GL11.glColor4f(0.5F * var14, 0.25F * var14, 0.8F * var14, 1.0F);
                GL11.glMatrixMode(5890);
                GL11.glPushMatrix();
                float var15 = 0.125F;
                GL11.glScalef(var15, var15, var15);
                float var16 = (float) (Minecraft.getTime() % 3000L) / 3000.0F * 8.0F;
                GL11.glTranslatef(var16, 0.0F, 0.0F);
                GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                this.method_4306(var5, 0.0F, 0.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(var15, var15, var15);
                var16 = (float) (Minecraft.getTime() % 4873L) / 4873.0F * 8.0F;
                GL11.glTranslatef(-var16, 0.0F, 0.0F);
                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                this.method_4306(var5, 0.0F, 0.0F, 1.0F, 1.0F);
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
