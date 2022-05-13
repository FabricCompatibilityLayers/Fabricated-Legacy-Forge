package fr.catcore.fabricatedforge.mixin.forgefml.client.render.block.entity;

import com.mojang.blaze3d.platform.GLX;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {

    @Shadow public double cameraX;

    @Shadow public double cameraY;

    @Shadow public double cameraZ;

    @Shadow public static double CAMERA_X;

    @Shadow public static double CAMERA_Y;

    @Shadow public static double CAMERA_Z;

    @Shadow public abstract void renderBlockEntity(BlockEntity blockEntity, double x, double y, double z, float tickDelta);

    @Shadow public World world;

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void method_1626(BlockEntity par1TileEntity, float par2) {
        double dist = par1TileEntity.getRenderDistance();
        dist *= dist;
        if (par1TileEntity.getSquaredDistance(this.cameraX, this.cameraY, this.cameraZ) < dist) {
            int var3 = this.world.method_3778(par1TileEntity.x, par1TileEntity.y, par1TileEntity.z, 0);
            int var4 = var3 % 65536;
            int var5 = var3 / 65536;
            GLX.gl13MultiTexCoord2f(GLX.lightmapTextureUnit, (float) var4, (float) var5);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.renderBlockEntity(par1TileEntity, (double)par1TileEntity.x - CAMERA_X, (double)par1TileEntity.y - CAMERA_Y, (double)par1TileEntity.z - CAMERA_Z, par2);
        }

    }
}
