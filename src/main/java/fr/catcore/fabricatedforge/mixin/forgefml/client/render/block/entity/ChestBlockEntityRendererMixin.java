package fr.catcore.fabricatedforge.mixin.forgefml.client.render.block.entity;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.entity.model.ChestBlockEntityModel;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChestBlockEntityRenderer.class)
public abstract class ChestBlockEntityRendererMixin extends BlockEntityRenderer {
    @Shadow private ChestBlockEntityModel model;

    @Shadow private boolean christmas;

    @Shadow private ChestBlockEntityModel largeModel;

    /**
     * @author forge
     * @reason handle exception
     */
    @Overwrite
    public void render(ChestBlockEntity par1TileEntityChest, double par2, double par4, double par6, float par8) {
        int var9;
        if (!par1TileEntityChest.hasWorld()) {
            var9 = 0;
        } else {
            Block var10 = par1TileEntityChest.getBlock();
            var9 = par1TileEntityChest.getDataValue();
            if (var10 != null && var9 == 0) {
                try {
                    ((ChestBlock)var10).method_287(par1TileEntityChest.getEntityWorld(), par1TileEntityChest.x, par1TileEntityChest.y, par1TileEntityChest.z);
                } catch (ClassCastException var141) {
                    FMLLog.severe(
                            "Attempted to render a chest at %d,  %d, %d that was not a chest",
                            new Object[]{par1TileEntityChest.x, par1TileEntityChest.y, par1TileEntityChest.z}
                    );
                }

                var9 = par1TileEntityChest.getDataValue();
            }

            par1TileEntityChest.checkNeighborChests();
        }

        if (par1TileEntityChest.neighborChestNorth == null && par1TileEntityChest.neighborChestWest == null) {
            ChestBlockEntityModel var14;
            if (par1TileEntityChest.neighborChestEast == null && par1TileEntityChest.neighborChestSouth == null) {
                var14 = this.model;
                if (this.christmas) {
                    this.bindTexture("/item/xmaschest.png");
                } else {
                    this.bindTexture("/item/chest.png");
                }
            } else {
                var14 = this.largeModel;
                if (this.christmas) {
                    this.bindTexture("/item/largexmaschest.png");
                } else {
                    this.bindTexture("/item/largechest.png");
                }
            }

            GL11.glPushMatrix();
            GL11.glEnable(32826);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float)par2, (float)par4 + 1.0F, (float)par6 + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            short var11 = 0;
            if (var9 == 2) {
                var11 = 180;
            }

            if (var9 == 3) {
                var11 = 0;
            }

            if (var9 == 4) {
                var11 = 90;
            }

            if (var9 == 5) {
                var11 = -90;
            }

            if (var9 == 2 && par1TileEntityChest.neighborChestEast != null) {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if (var9 == 5 && par1TileEntityChest.neighborChestSouth != null) {
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            }

            GL11.glRotatef((float)var11, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float var12 = par1TileEntityChest.animationAnglePrev + (par1TileEntityChest.animationAngle - par1TileEntityChest.animationAnglePrev) * par8;
            if (par1TileEntityChest.neighborChestNorth != null) {
                float var13 = par1TileEntityChest.neighborChestNorth.animationAnglePrev
                        + (par1TileEntityChest.neighborChestNorth.animationAngle - par1TileEntityChest.neighborChestNorth.animationAnglePrev) * par8;
                if (var13 > var12) {
                    var12 = var13;
                }
            }

            if (par1TileEntityChest.neighborChestWest != null) {
                float var13 = par1TileEntityChest.neighborChestWest.animationAnglePrev
                        + (par1TileEntityChest.neighborChestWest.animationAngle - par1TileEntityChest.neighborChestWest.animationAnglePrev) * par8;
                if (var13 > var12) {
                    var12 = var13;
                }
            }

            var12 = 1.0F - var12;
            var12 = 1.0F - var12 * var12 * var12;
            var14.lid.posX = -(var12 * (float) Math.PI / 2.0F);
            var14.renderParts();
            GL11.glDisable(32826);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
