package fr.catcore.fabricatedforge.mixin.forgefml.entity.boss.dragon;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.mob.class_956;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin extends class_956 {

    @Shadow public float prevWingPosition;

    @Shadow public float wingPosition;

    @Shadow protected abstract void tickWithEndCrystals();

    @Shadow public boolean field_3745;

    @Shadow public int latestSegment;

    @Shadow public double[][] segmentCircularBuffer;

    @Shadow public double field_3742;

    @Shadow public double field_3751;

    @Shadow public double field_3752;

    @Shadow private Entity target;

    @Shadow public boolean field_3744;

    @Shadow protected abstract void method_2906();

    @Shadow public EnderDragonPart partHead;

    @Shadow public EnderDragonPart partTail1;

    @Shadow public EnderDragonPart partTail2;

    @Shadow public EnderDragonPart partTail3;

    @Shadow public EnderDragonPart partBody;

    @Shadow public EnderDragonPart partWingRight;

    @Shadow public EnderDragonPart partWingLeft;

    @Shadow public abstract double[] getSegmentProperties(int segmentNumber, float tickDelta);

    @Shadow protected abstract void launchLivingEntities(List entities);

    @Shadow protected abstract void damageLivingEntities(List entities);

    @Shadow protected abstract float wrapYawChange(double yawDegrees);

    public EnderDragonEntityMixin(World world) {
        super(world);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_2651() {
        this.prevWingPosition = this.wingPosition;
        if (!this.world.isClient) {
            this.dataTracker.setProperty(16, this.field_3294);
        }

        float var1;
        float var3;
        float var26;
        if (this.field_3294 <= 0) {
            var1 = (this.random.nextFloat() - 0.5F) * 8.0F;
            var26 = (this.random.nextFloat() - 0.5F) * 4.0F;
            var3 = (this.random.nextFloat() - 0.5F) * 8.0F;
            this.world.spawnParticle("largeexplode", this.x + (double)var1, this.y + 2.0 + (double)var26, this.z + (double)var3, 0.0, 0.0, 0.0);
        } else {
            this.tickWithEndCrystals();
            var1 = 0.2F / (MathHelper.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ) * 10.0F + 1.0F);
            var1 *= (float)Math.pow(2.0, this.velocityY);
            if (this.field_3745) {
                this.wingPosition += var1 * 0.5F;
            } else {
                this.wingPosition += var1;
            }

            this.yaw = MathHelper.wrapDegrees(this.yaw);
            if (this.latestSegment < 0) {
                for(int var2 = 0; var2 < this.segmentCircularBuffer.length; ++var2) {
                    this.segmentCircularBuffer[var2][0] = (double)this.yaw;
                    this.segmentCircularBuffer[var2][1] = this.y;
                }
            }

            if (++this.latestSegment == this.segmentCircularBuffer.length) {
                this.latestSegment = 0;
            }

            this.segmentCircularBuffer[this.latestSegment][0] = (double)this.yaw;
            this.segmentCircularBuffer[this.latestSegment][1] = this.y;
            double var6;
            double var8;
            double var25;
            float var33;
            float var21;
            float var22;
            float var24;
            double var4;
            float var17;
            if (this.world.isClient) {
                if (this.field_3338 > 0) {
                    var25 = this.x + (this.field_3339 - this.x) / (double)this.field_3338;
                    var4 = this.y + (this.field_3340 - this.y) / (double)this.field_3338;
                    var6 = this.z + (this.field_3341 - this.z) / (double)this.field_3338;
                    var8 = MathHelper.wrapDegrees(this.field_3342 - (double)this.yaw);
                    this.yaw = (float)((double)this.yaw + var8 / (double)this.field_3338);
                    this.pitch = (float)((double)this.pitch + (this.field_3343 - (double)this.pitch) / (double)this.field_3338);
                    --this.field_3338;
                    this.updatePosition(var25, var4, var6);
                    this.setRotation(this.yaw, this.pitch);
                }
            } else {
                var25 = this.field_3742 - this.x;
                var4 = this.field_3751 - this.y;
                var6 = this.field_3752 - this.z;
                var8 = var25 * var25 + var4 * var4 + var6 * var6;
                double var11;
                double var13;
                if (this.target != null) {
                    this.field_3742 = this.target.x;
                    this.field_3752 = this.target.z;
                    var11 = this.field_3742 - this.x;
                    var13 = this.field_3752 - this.z;
                    double var14 = Math.sqrt(var11 * var11 + var13 * var13);
                    double var16 = 0.4000000059604645 + var14 / 80.0 - 1.0;
                    if (var16 > 10.0) {
                        var16 = 10.0;
                    }

                    this.field_3751 = this.target.boundingBox.minY + var16;
                } else {
                    this.field_3742 += this.random.nextGaussian() * 2.0;
                    this.field_3752 += this.random.nextGaussian() * 2.0;
                }

                if (this.field_3744 || var8 < 100.0 || var8 > 22500.0 || this.horizontalCollision || this.verticalCollision) {
                    this.method_2906();
                }

                var4 /= (double)MathHelper.sqrt(var25 * var25 + var6 * var6);
                var33 = 0.6F;
                if (var4 < (double)(-var33)) {
                    var4 = (double)(-var33);
                }

                if (var4 > (double)var33) {
                    var4 = (double)var33;
                }

                this.velocityY += var4 * 0.10000000149011612;
                this.yaw = MathHelper.wrapDegrees(this.yaw);
                var11 = 180.0 - Math.atan2(var25, var6) * 180.0 / Math.PI;
                var13 = MathHelper.wrapDegrees(var11 - (double)this.yaw);
                if (var13 > 50.0) {
                    var13 = 50.0;
                }

                if (var13 < -50.0) {
                    var13 = -50.0;
                }

                Vec3d var15 = Vec3d.method_603().getOrCreate(this.field_3742 - this.x, this.field_3751 - this.y, this.field_3752 - this.z).normalize();
                Vec3d var40 = Vec3d.method_603().getOrCreate((double)MathHelper.sin(this.yaw * 3.1415927F / 180.0F), this.velocityY, (double)(-MathHelper.cos(this.yaw * 3.1415927F / 180.0F))).normalize();
                var17 = (float)(var40.dotProduct(var15) + 0.5) / 1.5F;
                if (var17 < 0.0F) {
                    var17 = 0.0F;
                }

                this.field_3349 *= 0.8F;
                float var18 = MathHelper.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ) * 1.0F + 1.0F;
                double var19 = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ) * 1.0D + 1.0D;
                if (var19 > 40.0) {
                    var19 = 40.0;
                }

                this.field_3349 = (float)((double)this.field_3349 + var13 * (0.699999988079071 / var19 / (double)var18));
                this.yaw += this.field_3349 * 0.1F;
                var21 = (float)(2.0 / (var19 + 1.0));
                var22 = 0.06F;
                this.updateVelocity(0.0F, -1.0F, var22 * (var17 * var21 + (1.0F - var21)));
                if (this.field_3745) {
                    this.move(this.velocityX * 0.800000011920929, this.velocityY * 0.800000011920929, this.velocityZ * 0.800000011920929);
                } else {
                    this.move(this.velocityX, this.velocityY, this.velocityZ);
                }

                Vec3d var23 = Vec3d.method_603().getOrCreate(this.velocityX, this.velocityY, this.velocityZ).normalize();
                var24 = (float)(var23.dotProduct(var40) + 1.0) / 2.0F;
                var24 = 0.8F + 0.15F * var24;
                this.velocityX *= (double)var24;
                this.velocityZ *= (double)var24;
                this.velocityY *= 0.9100000262260437;
            }

            this.field_3313 = this.yaw;
            this.partHead.width = this.partHead.height = 3.0F;
            this.partTail1.width = this.partTail1.height = 2.0F;
            this.partTail2.width = this.partTail2.height = 2.0F;
            this.partTail3.width = this.partTail3.height = 2.0F;
            this.partBody.height = 3.0F;
            this.partBody.width = 5.0F;
            this.partWingRight.height = 2.0F;
            this.partWingRight.width = 4.0F;
            this.partWingLeft.height = 3.0F;
            this.partWingLeft.width = 4.0F;
            var26 = (float)(this.getSegmentProperties(5, 1.0F)[1] - this.getSegmentProperties(10, 1.0F)[1]) * 10.0F / 180.0F * 3.1415927F;
            var3 = MathHelper.cos(var26);
            float var28 = -MathHelper.sin(var26);
            float var5 = this.yaw * 3.1415927F / 180.0F;
            float var27 = MathHelper.sin(var5);
            float var7 = MathHelper.cos(var5);
            this.partBody.tick();
            this.partBody.refreshPositionAndAngles(this.x + (double)(var27 * 0.5F), this.y, this.z - (double)(var7 * 0.5F), 0.0F, 0.0F);
            this.partWingRight.tick();
            this.partWingRight.refreshPositionAndAngles(this.x + (double)(var7 * 4.5F), this.y + 2.0, this.z + (double)(var27 * 4.5F), 0.0F, 0.0F);
            this.partWingLeft.tick();
            this.partWingLeft.refreshPositionAndAngles(this.x - (double)(var7 * 4.5F), this.y + 2.0, this.z - (double)(var27 * 4.5F), 0.0F, 0.0F);
            if (!this.world.isClient && this.field_3297 == 0) {
                this.launchLivingEntities(this.world.getEntitiesIn(this, this.partWingRight.boundingBox.expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0)));
                this.launchLivingEntities(this.world.getEntitiesIn(this, this.partWingLeft.boundingBox.expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0)));
                this.damageLivingEntities(this.world.getEntitiesIn(this, this.partHead.boundingBox.expand(1.0, 1.0, 1.0)));
            }

            double[] var29 = this.getSegmentProperties(5, 1.0F);
            double[] var9 = this.getSegmentProperties(0, 1.0F);
            var33 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F - this.field_3349 * 0.01F);
            var17 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F - this.field_3349 * 0.01F);
            this.partHead.tick();
            this.partHead.refreshPositionAndAngles(this.x + (double)(var33 * 5.5F * var3), this.y + (var9[1] - var29[1]) * 1.0 + (double)(var28 * 5.5F), this.z - (double)(var17 * 5.5F * var3), 0.0F, 0.0F);

            for(int var30 = 0; var30 < 3; ++var30) {
                EnderDragonPart var31 = null;
                if (var30 == 0) {
                    var31 = this.partTail1;
                }

                if (var30 == 1) {
                    var31 = this.partTail2;
                }

                if (var30 == 2) {
                    var31 = this.partTail3;
                }

                double[] var35 = this.getSegmentProperties(12 + var30 * 2, 1.0F);
                var21 = this.yaw * 3.1415927F / 180.0F + this.wrapYawChange(var35[0] - var29[0]) * 3.1415927F / 180.0F * 1.0F;
                var22 = MathHelper.sin(var21);
                float var37 = MathHelper.cos(var21);
                var24 = 1.5F;
                float var39 = (float)(var30 + 1) * 2.0F;
                var31.tick();
                var31.refreshPositionAndAngles(this.x - (double)((var27 * var24 + var22 * var39) * var3), this.y + (var35[1] - var29[1]) * 1.0 - (double)((var39 + var24) * var28) + 1.5, this.z + (double)((var7 * var24 + var37 * var39) * var3), 0.0F, 0.0F);
            }

            if (!this.world.isClient) {
                this.field_3745 = this.destroyBlocks(this.partHead.boundingBox) | this.destroyBlocks(this.partBody.boundingBox);
            }
        }

    }

    /**
     * @author Miencraft Forge
     * @reason none
     */
    @Overwrite
    private boolean destroyBlocks(Box par1AxisAlignedBB) {
        int var2 = MathHelper.floor(par1AxisAlignedBB.minX);
        int var3 = MathHelper.floor(par1AxisAlignedBB.minY);
        int var4 = MathHelper.floor(par1AxisAlignedBB.minZ);
        int var5 = MathHelper.floor(par1AxisAlignedBB.maxX);
        int var6 = MathHelper.floor(par1AxisAlignedBB.maxY);
        int var7 = MathHelper.floor(par1AxisAlignedBB.maxZ);
        boolean var8 = false;
        boolean var9 = false;

        for(int var10 = var2; var10 <= var5; ++var10) {
            for(int var11 = var3; var11 <= var6; ++var11) {
                for(int var12 = var4; var12 <= var7; ++var12) {
                    int var13 = this.world.getBlock(var10, var11, var12);
                    Block block = Block.BLOCKS[var13];
                    if (block != null) {
                        if (((IBlock)block).canDragonDestroy(this.world, var10, var11, var12)) {
                            var9 = true;
                            this.world.method_3690(var10, var11, var12, 0);
                        } else {
                            var8 = true;
                        }
                    }
                }
            }
        }

        if (var9) {
            double var16 = par1AxisAlignedBB.minX + (par1AxisAlignedBB.maxX - par1AxisAlignedBB.minX) * (double)this.random.nextFloat();
            double var17 = par1AxisAlignedBB.minY + (par1AxisAlignedBB.maxY - par1AxisAlignedBB.minY) * (double)this.random.nextFloat();
            double var14 = par1AxisAlignedBB.minZ + (par1AxisAlignedBB.maxZ - par1AxisAlignedBB.minZ) * (double)this.random.nextFloat();
            this.world.spawnParticle("largeexplode", var16, var17, var14, 0.0, 0.0, 0.0);
        }

        return var8;
    }
}
