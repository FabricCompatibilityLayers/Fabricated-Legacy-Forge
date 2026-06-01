/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fr.catcore.cursedmixinextensions.annotations.NewConstructor;
import fr.catcore.cursedmixinextensions.annotations.Public;
import fr.catcore.cursedmixinextensions.annotations.ShadowConstructor;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockRailExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.EntityMinecartExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.IMinecartCollisionHandler;
import net.minecraftforge.common.MinecartRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(EntityMinecart.class)
public abstract class EntityMinecartMixin extends Entity implements IInventory, EntityMinecartExtension {
    @Shadow public int minecartType;

    @Shadow private int fuel;

    @Shadow public abstract int getSizeInventory();

    @Shadow private ItemStack[] cargoItems;

    @Shadow public double pushX;

    @Shadow public double pushZ;

    @Shadow protected abstract void setMinecartPowered(boolean par1);

    @Shadow public abstract void func_70494_i(int par1);

    @Shadow public abstract int func_70493_k();

    @Shadow public abstract void func_70497_h(int par1);

    @Shadow public abstract void setDamage(int par1);

    @Shadow public abstract int getDamage();

    @Shadow public abstract int func_70496_j();

    @Shadow protected abstract boolean isMinecartPowered();

    @Shadow private int turnProgress;

    @Shadow private double minecartX;

    @Shadow private double minecartY;

    @Shadow private double minecartZ;

    @Shadow private double minecartYaw;

    @Shadow private double minecartPitch;

    @Shadow public abstract Vec3 func_70489_a(double par1, double par3, double par5);

    @Shadow @Final private static int[][][] field_70500_g;

    @Shadow private boolean field_70499_f;

    public EntityMinecartMixin(World par1World) {
        super(par1World);
    }

    /* Forge: Minecart Compatibility Layer Integration. */
    @Public
    private static float defaultMaxSpeedRail = 0.4f;
    @Public
    private static float defaultMaxSpeedGround = 0.4f;
    @Public
    private static float defaultMaxSpeedAirLateral = 0.4f;
    @Public
    private static float defaultMaxSpeedAirVertical = -1f;
    // Forge used a decompiler which didn't handle those values correctly
    // we are keeping them to be as close as possible to the original Forge behavior
    @Public
    private static double defaultDragRidden = 0.996999979019165D;
    @Public
    private static double defaultDragEmpty = 0.9599999785423279D;
    @Public
    private static double defaultDragAir = 0.94999998807907104D;
    protected boolean canUseRail = true;
    protected boolean canBePushed = true;
    private static IMinecartCollisionHandler collisionHandler = null;

    /* Instance versions of the above physics properties */
    protected float maxSpeedRail;
    protected float maxSpeedGround;
    protected float maxSpeedAirLateral;
    protected float maxSpeedAirVertical;
    protected double dragAir;

    @Inject(method = "<init>(Lnet/minecraft/src/World;)V", at = @At(value = "RETURN") )
    private void forge$defaultValues(World par1, CallbackInfo ci) {
        maxSpeedRail = defaultMaxSpeedRail;
        maxSpeedGround = defaultMaxSpeedGround;
        maxSpeedAirLateral = defaultMaxSpeedAirLateral;
        maxSpeedAirVertical = defaultMaxSpeedAirVertical;
        dragAir = defaultDragAir;
    }

    @ShadowConstructor
    abstract void constructor(World par1);

    @NewConstructor
    public void constructor(World world, int type)
    {
        constructor(world);
        minecartType = type;
    }

    @Inject(method = "getCollisionBox", at = @At("HEAD"), cancellable = true)
    private void forge$getCollisionBox(Entity par1Entity, CallbackInfoReturnable<AxisAlignedBB> cir) {
        if (getCollisionHandler() != null)
        {
            cir.setReturnValue(getCollisionHandler().getCollisionBox((EntityMinecart) (Object) this, par1Entity));
        }
    }

    @Inject(method = "getBoundingBox", at = @At("HEAD"), cancellable = true)
    private void forge$getBoundingBox(CallbackInfoReturnable<AxisAlignedBB> cir) {
        if (getCollisionHandler() != null)
        {
            cir.setReturnValue(getCollisionHandler().getBoundingBox((EntityMinecart) (Object) this));
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canBePushed()
    {
        return canBePushed;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
        if (!this.worldObj.isRemote && !this.isDead) {
            this.func_70494_i(-this.func_70493_k());
            this.func_70497_h(10);
            this.setBeenAttacked();
            this.setDamage(this.getDamage() + par2 * 10);
            if (par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode) {
                this.setDamage(100);
            }

            if (this.getDamage() > 40) {
                if (this.riddenByEntity != null) {
                    this.riddenByEntity.mountEntity(this);
                }

                this.setDead();
                dropCartAsItem();
            }

            return true;
        } else {
            return true;
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onUpdate() {
        if (this.func_70496_j() > 0) {
            this.func_70497_h(this.func_70496_j() - 1);
        }

        if (this.getDamage() > 0) {
            this.setDamage(this.getDamage() - 1);
        }

        if (this.posY < -64.0) {
            this.kill();
        }

        if (this.isMinecartPowered() && this.rand.nextInt(4) == 0 && minecartType == 2 && ((Class) getClass()) == EntityMinecart.class) {
            this.worldObj.spawnParticle("largesmoke", this.posX, this.posY + 0.8, this.posZ, 0.0, 0.0, 0.0);
        }

        if (this.worldObj.isRemote) {
            if (this.turnProgress > 0) {
                double var45 = this.posX + (this.minecartX - this.posX) / this.turnProgress;
                double var46 = this.posY + (this.minecartY - this.posY) / this.turnProgress;
                double var5 = this.posZ + (this.minecartZ - this.posZ) / this.turnProgress;
                double var7 = MathHelper.wrapAngleTo180_double(this.minecartYaw - this.rotationYaw);
                this.rotationYaw = (float)(this.rotationYaw + var7 / this.turnProgress);
                this.rotationPitch = (float)(this.rotationPitch + (this.minecartPitch - this.rotationPitch) / this.turnProgress);
                this.turnProgress--;
                this.setPosition(var45, var46, var5);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            } else {
                this.setPosition(this.posX, this.posY, this.posZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        } else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= 0.04F;
            int var1 = MathHelper.floor_double(this.posX);
            int var2 = MathHelper.floor_double(this.posY);
            int var3 = MathHelper.floor_double(this.posZ);
            if (BlockRail.isRailBlockAt(this.worldObj, var1, var2 - 1, var3)) {
                var2--;
            }

            double var4 = 0.4;
            double var6 = 0.0078125;
            int var8 = this.worldObj.getBlockId(var1, var2, var3);
            if (canUseRail() && BlockRail.isRailBlock(var8)) {
                Vec3 var9 = this.func_70489_a(this.posX, this.posY, this.posZ);
                int var10 = ((BlockRailExtension)Block.blocksList[var8]).getBasicRailMetadata(worldObj, (EntityMinecart) (Object) this, var1, var2, var3);
                this.posY = var2;
                boolean var11 = false;
                boolean var12 = false;
                if (var8 == Block.railPowered.blockID) {
                    var11 = (worldObj.getBlockMetadata(var1, var2, var3) & 8) != 0;
                    var12 = !var11;
                }

                if (((BlockRail)Block.blocksList[var8]).isPowered()) {
                    var10 &= 7;
                }

                adjustSlopeVelocities(var10);

                int[][] var13 = field_70500_g[var10];
                double var14 = var13[1][0] - var13[0][0];
                double var16 = var13[1][2] - var13[0][2];
                double var18 = Math.sqrt(var14 * var14 + var16 * var16);
                double var20 = this.motionX * var14 + this.motionZ * var16;
                if (var20 < 0.0) {
                    var14 = -var14;
                    var16 = -var16;
                }

                double var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.motionX = var22 * var14 / var18;
                this.motionZ = var22 * var16 / var18;
                if (this.riddenByEntity != null) {
                    double var24 = this.riddenByEntity.motionX * this.riddenByEntity.motionX + this.riddenByEntity.motionZ * this.riddenByEntity.motionZ;
                    double var26 = this.motionX * this.motionX + this.motionZ * this.motionZ;
                    if (var24 > 1.0E-4 && var26 < 0.01) {
                        this.motionX = this.motionX + this.riddenByEntity.motionX * 0.1;
                        this.motionZ = this.motionZ + this.riddenByEntity.motionZ * 0.1;
                        var12 = false;
                    }
                }

                if (var12 && shouldDoRailFunctions()) {
                    double var55 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    if (var55 < 0.03) {
                        this.motionX *= 0.0;
                        this.motionY *= 0.0;
                        this.motionZ *= 0.0;
                    } else {
                        this.motionX *= 0.5;
                        this.motionY *= 0.0;
                        this.motionZ *= 0.5;
                    }
                }

                double var56 = 0.0;
                double var58 = var1 + 0.5 + var13[0][0] * 0.5;
                double var28 = var3 + 0.5 + var13[0][2] * 0.5;
                double var30 = var1 + 0.5 + var13[1][0] * 0.5;
                double var32 = var3 + 0.5 + var13[1][2] * 0.5;
                var14 = var30 - var58;
                var16 = var32 - var28;
                if (var14 == 0.0) {
                    this.posX = var1 + 0.5;
                    var56 = this.posZ - var3;
                } else if (var16 == 0.0) {
                    this.posZ = var3 + 0.5;
                    var56 = this.posX - var1;
                } else {
                    double var34 = this.posX - var58;
                    double var36 = this.posZ - var28;
                    var56 = (var34 * var14 + var36 * var16) * 2.0;
                }

                this.posX = var58 + var14 * var56;
                this.posZ = var28 + var16 * var56;
                this.setPosition(this.posX, this.posY + this.yOffset, this.posZ);

                moveMinecartOnRail(var1, var2, var3);

                if (var13[0][1] != 0 && MathHelper.floor_double(this.posX) - var1 == var13[0][0] && MathHelper.floor_double(this.posZ) - var3 == var13[0][2]) {
                    this.setPosition(this.posX, this.posY + var13[0][1], this.posZ);
                } else if (var13[1][1] != 0 && MathHelper.floor_double(this.posX) - var1 == var13[1][0] && MathHelper.floor_double(this.posZ) - var3 == var13[1][2]
                )
                {
                    this.setPosition(this.posX, this.posY + var13[1][1], this.posZ);
                }

                applyDragAndPushForces();

                Vec3 var62 = this.func_70489_a(this.posX, this.posY, this.posZ);
                if (var62 != null && var9 != null) {
                    double var39 = (var9.yCoord - var62.yCoord) * 0.05;
                    var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    if (var22 > 0.0) {
                        this.motionX = this.motionX / var22 * (var22 + var39);
                        this.motionZ = this.motionZ / var22 * (var22 + var39);
                    }

                    this.setPosition(this.posX, var62.yCoord, this.posZ);
                }

                int var63 = MathHelper.floor_double(this.posX);
                int var64 = MathHelper.floor_double(this.posZ);
                if (var63 != var1 || var64 != var3) {
                    var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    this.motionX = var22 * (var63 - var1);
                    this.motionZ = var22 * (var64 - var3);
                }

                updatePushForces();

                if(shouldDoRailFunctions())
                {
                    ((BlockRailExtension)Block.blocksList[var8]).onMinecartPass(worldObj, (EntityMinecart) (Object) this, var1, var2, var3);
                }

                if (var11 && shouldDoRailFunctions()) {
                    double var66 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    if (var66 > 0.01) {
                        double var43 = 0.06;
                        this.motionX = this.motionX + this.motionX / var66 * var43;
                        this.motionZ = this.motionZ + this.motionZ / var66 * var43;
                    } else if (var10 == 1) {
                        if (this.worldObj.isBlockNormalCube(var1 - 1, var2, var3)) {
                            this.motionX = 0.02;
                        } else if (this.worldObj.isBlockNormalCube(var1 + 1, var2, var3)) {
                            this.motionX = -0.02;
                        }
                    } else if (var10 == 0) {
                        if (this.worldObj.isBlockNormalCube(var1, var2, var3 - 1)) {
                            this.motionZ = 0.02;
                        } else if (this.worldObj.isBlockNormalCube(var1, var2, var3 + 1)) {
                            this.motionZ = -0.02;
                        }
                    }
                }

                this.doBlockCollisions();
            } else {
                moveMinecartOffRail(var1, var2, var3);
            }

            this.rotationPitch = 0.0F;
            double var47 = this.prevPosX - this.posX;
            double var48 = this.prevPosZ - this.posZ;
            if (var47 * var47 + var48 * var48 > 0.001) {
                this.rotationYaw = (float)(Math.atan2(var48, var47) * 180.0 / Math.PI);
                if (this.field_70499_f) {
                    this.rotationYaw += 180.0F;
                }
            }

            double var49 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.prevRotationYaw);
            if (var49 < -170.0 || var49 >= 170.0) {
                this.rotationYaw += 180.0F;
                this.field_70499_f = !this.field_70499_f;
            }

            this.setRotation(this.rotationYaw, this.rotationPitch);
            AxisAlignedBB box = null;
            if (getCollisionHandler() != null)
            {
                box = getCollisionHandler().getMinecartCollisionBox((EntityMinecart) (Object) this);
            }
            else
            {
                box = boundingBox.expand(0.2D, 0.0D, 0.2D);
            }

            List var15 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, box);
            if (var15 != null && !var15.isEmpty()) {
                for (int var52 = 0; var52 < var15.size(); var52++) {
                    Entity var17 = (Entity)var15.get(var52);
                    if (var17 != this.riddenByEntity && var17.canBePushed() && var17 instanceof EntityMinecart) {
                        var17.applyEntityCollision(this);
                    }
                }
            }

            if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
                if (this.riddenByEntity.ridingEntity == this) {
                    this.riddenByEntity.ridingEntity = null;
                }

                this.riddenByEntity = null;
            }

            updateFuel();
            MinecraftForge.EVENT_BUS.post(new MinecartUpdateEvent((EntityMinecart) (Object) this, var1, var2, var3));
        }
    }

    @Redirect(method = {"func_70495_a", "func_70489_a"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockMetadata(III)I"))
    private int forge$getBasicRailMetadata(World worldObj, int var9, int var10, int var11,
                                           @Local(ordinal = 3) int var12) {
        return ((BlockRailExtension)Block.blocksList[var12]).getBasicRailMetadata(worldObj, (EntityMinecart) (Object) this, var9, var10, var11);
    }

    @Redirect(method = {"func_70495_a", "func_70489_a"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockRail;isPowered()Z"))
    private boolean forge$cancel(BlockRail instance) {
        return false;
    }

    @Definition(id = "minecartType", field = "Lnet/minecraft/src/EntityMinecart;minecartType:I")
    @Expression("this.minecartType == 2")
    @WrapOperation(method = {"writeEntityToNBT", "readEntityFromNBT", "applyEntityCollision"}, at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isPoweredCart(int left, int right, Operation<Boolean> original) {
        return isPoweredCart();
    }

    @Redirect(method = "writeEntityToNBT", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/NBTTagCompound;setShort(Ljava/lang/String;S)V"))
    private void forge$setInteger(NBTTagCompound instance, String par2, short i) {
        instance.setInteger(par2, this.fuel);
    }

    @Definition(id = "minecartType", field = "Lnet/minecraft/src/EntityMinecart;minecartType:I")
    @Expression("this.minecartType == 1")
    @WrapOperation(method = {"writeEntityToNBT", "readEntityFromNBT"}, at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$cancel(int left, int right, Operation<Boolean> original) {
        return false;
    }

    @Inject(method = "writeEntityToNBT", at = @At("RETURN"))
    private void forge$writeNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        if (getSizeInventory() > 0) {
            NBTTagList var2 = new NBTTagList();

            for (int var3 = 0; var3 < this.cargoItems.length; var3++) {
                if (this.cargoItems[var3] != null) {
                    NBTTagCompound var4 = new NBTTagCompound();
                    var4.setByte("Slot", (byte)var3);
                    this.cargoItems[var3].writeToNBT(var4);
                    var2.appendTag(var4);
                }
            }

            par1NBTTagCompound.setTag("Items", var2);
        }
    }

    @Redirect(method = "readEntityFromNBT", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/NBTTagCompound;getShort(Ljava/lang/String;)S"))
    private short forge$hack(NBTTagCompound instance, String s) {
        return 0;
    }

    @Inject(method = "readEntityFromNBT", at = @At("RETURN"))
    private void forge$readNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        if (isPoweredCart()) {
            this.fuel = par1NBTTagCompound.getInteger("Fuel");
        }

        if (getSizeInventory() > 0) {
            NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
            this.cargoItems = new ItemStack[this.getSizeInventory()];

            for (int var3 = 0; var3 < var2.tagCount(); var3++) {
                NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
                int var5 = var4.getByte("Slot") & 255;
                if (var5 >= 0 && var5 < this.cargoItems.length) {
                    this.cargoItems[var5] = ItemStack.loadItemStackFromNBT(var4);
                }
            }
        }
    }

    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    private void forge$postMinecartCollisionEvent(Entity par1Entity, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new MinecartCollisionEvent((EntityMinecart) (Object) this, par1Entity));
        if (getCollisionHandler() != null)
        {
            getCollisionHandler().onEntityCollision((EntityMinecart) (Object) this, par1Entity);
            ci.cancel();
        }
    }

    @Definition(id = "minecartType", field = "Lnet/minecraft/src/EntityMinecart;minecartType:I")
    @Expression("this.minecartType == 0")
    @WrapOperation(method = {"applyEntityCollision", "interact"}, at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$canBeRidden(int left, int right, Operation<Boolean> original) {
        return canBeRidden();
    }

    @Definition(id = "EntityMinecart", type = EntityMinecart.class)
    @Definition(id = "par1Entity", local = @Local(type = Entity.class, argsOnly = true))
    @Definition(id = "minecartType", field = "Lnet/minecraft/src/EntityMinecart;minecartType:I")
    @Expression("((EntityMinecart) par1Entity).minecartType == 2")
    @WrapOperation(method = "applyEntityCollision", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isPoweredCart(int left, int right, Operation<Boolean> original,
                                        @Local(argsOnly = true) Entity par1Entity) {
        return ((EntityMinecartExtension) par1Entity).isPoweredCart();
    }

    @Definition(id = "EntityMinecart", type = EntityMinecart.class)
    @Definition(id = "par1Entity", local = @Local(type = Entity.class, argsOnly = true))
    @Definition(id = "minecartType", field = "Lnet/minecraft/src/EntityMinecart;minecartType:I")
    @Expression("((EntityMinecart) par1Entity).minecartType != 2")
    @WrapOperation(method = "applyEntityCollision", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isntPoweredCart(int left, int right, Operation<Boolean> original,
                                        @Local(argsOnly = true) Entity par1Entity) {
        return !((EntityMinecartExtension) par1Entity).isPoweredCart();
    }

    @Definition(id = "minecartType", field = "Lnet/minecraft/src/EntityMinecart;minecartType:I")
    @Expression("this.minecartType != 2")
    @WrapOperation(method = "applyEntityCollision", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isntPoweredCart(int left, int right, Operation<Boolean> original) {
        return !isPoweredCart();
    }

    @ModifyReturnValue(method = "getSizeInventory", at = @At("RETURN"))
    private int forge$getSizeInventory(int original) {
        return (minecartType == 1 && (Class) getClass() == EntityMinecart.class ? original : 0);
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void forge$postMinecartInteractEvent(EntityPlayer par1EntityPlayer, CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent((EntityMinecart) (Object) this, par1EntityPlayer)))
        {
            cir.setReturnValue(true);
        }
    }

    @Definition(id = "minecartType", field = "Lnet/minecraft/src/EntityMinecart;minecartType:I")
    @Expression("this.minecartType == 1")
    @WrapOperation(method = "interact", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$getSizeInventory(int left, int right, Operation<Boolean> original) {
        return getSizeInventory() > 0;
    }

    @Definition(id = "minecartType", field = "Lnet/minecraft/src/EntityMinecart;minecartType:I")
    @Expression("this.minecartType == 2")
    @ModifyExpressionValue(method = "interact", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$check(boolean original) {
        return original && (Class) getClass() == EntityMinecart.class;
    }

    /**
     * Drops the cart as a item. The exact item dropped is defined by getItemDropped().
     */
    @Override
    public void dropCartAsItem()
    {
        for(ItemStack item : getItemsDropped())
        {
            entityDropItem(item, 0);
        }
    }

    /**
     * Override this to define which items your cart drops when broken.
     * This does not include items contained in the inventory,
     * that is handled elsewhere.
     * @return A list of items dropped.
     */
    @Override
    public List<ItemStack> getItemsDropped()
    {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Item.minecartEmpty));

        switch(minecartType)
        {
            case 1:
                items.add(new ItemStack(Block.chest));
                break;
            case 2:
                items.add(new ItemStack(Block.stoneOvenIdle));
                break;
        }
        return items;
    }

    /**
     * This function returns an ItemStack that represents this cart.
     * This should be an ItemStack that can be used by the player to place the cart.
     * This is the item that was registered with the cart via the registerMinecart function,
     * but is not necessary the item the cart drops when destroyed.
     * @return An ItemStack that can be used to place the cart.
     */
    @Override
    public ItemStack getCartItem()
    {
        return MinecartRegistry.getItemForCart((EntityMinecart) (Object) this);
    }

    /**
     * Returns true if this cart is self propelled.
     * @return True if powered.
     */
    @Override
    public boolean isPoweredCart()
    {
        return minecartType == 2 && (Class) getClass() == EntityMinecart.class;
    }

    /**
     * Returns true if this cart is a storage cart
     * Some carts may have inventories but not be storage carts
     * and some carts without inventories may be storage carts.
     * @return True if this cart should be classified as a storage cart.
     */
    @Override
    public boolean isStorageCart()
    {
        return minecartType == 1 && (Class) getClass() == EntityMinecart.class;
    }

    /**
     * Returns true if this cart can be ridden by an Entity.
     * @return True if this cart can be ridden.
     */
    @Override
    public boolean canBeRidden()
    {
        if(minecartType == 0 && (Class) getClass() == EntityMinecart.class)
        {
            return true;
        }
        return false;
    }

    /**
     * Returns true if this cart can currently use rails.
     * This function is mainly used to gracefully detach a minecart from a rail.
     * @return True if the minecart can use rails.
     */
    @Override
    public boolean canUseRail()
    {
        return canUseRail;
    }

    /**
     * Set whether the minecart can use rails.
     * This function is mainly used to gracefully detach a minecart from a rail.
     * @param use Whether the minecart can currently use rails.
     */
    @Override
    public void setCanUseRail(boolean use)
    {
        canUseRail = use;
    }

    /**
     * Return false if this cart should not call IRail.onMinecartPass() and should ignore Powered Rails.
     * @return True if this cart should call IRail.onMinecartPass().
     */
    @Override
    public boolean shouldDoRailFunctions()
    {
        return true;
    }

    /**
     * Simply returns the minecartType variable.
     * @return minecartType
     */
    @Override
    public int getMinecartType()
    {
        return minecartType;
    }

    /**
     * Gets the current global Minecart Collision handler if none
     * is registered, returns null
     * @return The collision handler or null
     */
    @Public
    private static IMinecartCollisionHandler getCollisionHandler()
    {
        return collisionHandler;
    }

    /**
     * Sets the global Minecart Collision handler, overwrites any
     * that is currently set.
     * @param handler The new handler
     */
    @Public
    private static void setCollisionHandler(IMinecartCollisionHandler handler)
    {
        collisionHandler = handler;
    }

    /**
     * Carts should return their drag factor here
     * @return The drag rate.
     */
    @Override
    public double getDrag()
    {
        return riddenByEntity != null ? defaultDragRidden : defaultDragEmpty;
    }

    /**
     * Moved to allow overrides.
     * This code applies drag and updates push forces.
     */
    @Override
    public void applyDragAndPushForces()
    {
        if(isPoweredCart())
        {
            double d27 = MathHelper.sqrt_double(pushX * pushX + pushZ * pushZ);
            if(d27 > 0.01D)
            {
                pushX /= d27;
                pushZ /= d27;
                double d29 = 0.04;
                motionX *= 0.8D;
                motionY *= 0.0D;
                motionZ *= 0.8D;
                motionX += pushX * d29;
                motionZ += pushZ * d29;
            }
            else
            {
                motionX *= 0.9D;
                motionY *= 0.0D;
                motionZ *= 0.9D;
            }
        }
        motionX *= getDrag();
        motionY *= 0.0D;
        motionZ *= getDrag();
    }

    /**
     * Moved to allow overrides.
     * This code updates push forces.
     */
    @Override
    public void updatePushForces()
    {
        if(isPoweredCart())
        {
            double push = MathHelper.sqrt_double(pushX * pushX + pushZ * pushZ);
            if(push > 0.01D && motionX * motionX + motionZ * motionZ > 0.001D)
            {
                pushX /= push;
                pushZ /= push;
                if(pushX * motionX + pushZ * motionZ < 0.0D)
                {
                    pushX = 0.0D;
                    pushZ = 0.0D;
                }
                else
                {
                    pushX = motionX;
                    pushZ = motionZ;
                }
            }
        }
    }

    /**
     * Moved to allow overrides.
     * This code handles minecart movement and speed capping when on a rail.
     */
    @Override
    public void moveMinecartOnRail(int i, int j, int k)
    {
        int id = worldObj.getBlockId(i, j, k);
        if (!BlockRail.isRailBlock(id))
        {
            return;
        }
        float railMaxSpeed = ((BlockRailExtension)Block.blocksList[id]).getRailMaxSpeed(worldObj, (EntityMinecart) (Object) this, i, j, k);

        double maxSpeed = Math.min(railMaxSpeed, getMaxSpeedRail());
        double mX = motionX;
        double mZ = motionZ;
        if(riddenByEntity != null)
        {
            mX *= 0.75D;
            mZ *= 0.75D;
        }
        if(mX < -maxSpeed) mX = -maxSpeed;
        if(mX >  maxSpeed) mX =  maxSpeed;
        if(mZ < -maxSpeed) mZ = -maxSpeed;
        if(mZ >  maxSpeed) mZ =  maxSpeed;
        moveEntity(mX, 0.0D, mZ);
    }

    /**
     * Moved to allow overrides.
     * This code handles minecart movement and speed capping when not on a rail.
     */
    @Override
    public void moveMinecartOffRail(int i, int j, int k)
    {
        double d2 = getMaxSpeedGround();
        if(!onGround)
        {
            d2 = getMaxSpeedAirLateral();
        }
        if(motionX < -d2) motionX = -d2;
        if(motionX >  d2) motionX =  d2;
        if(motionZ < -d2) motionZ = -d2;
        if(motionZ >  d2) motionZ =  d2;
        double moveY = motionY;
        if(getMaxSpeedAirVertical() > 0 && motionY > getMaxSpeedAirVertical())
        {
            moveY = getMaxSpeedAirVertical();
            if(Math.abs(motionX) < 0.3f && Math.abs(motionZ) < 0.3f)
            {
                moveY = 0.15f;
                motionY = moveY;
            }
        }
        if(onGround)
        {
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        }
        moveEntity(motionX, moveY, motionZ);
        if(!onGround)
        {
            motionX *= getDragAir();
            motionY *= getDragAir();
            motionZ *= getDragAir();
        }
    }

    /**
     * Moved to allow overrides.
     * This code applies fuel consumption.
     */
    @Override
    public void updateFuel()
    {
        if (fuel > 0) fuel--;
        if (fuel <= 0) pushX = pushZ = 0.0D;
        setMinecartPowered(fuel > 0);
    }

    /**
     * Moved to allow overrides, This code handle slopes affecting velocity.
     * @param metadata The blocks position metadata
     */
    @Override
    public void adjustSlopeVelocities(int metadata)
    {
        double acceleration = 0.0078125D;
        if (metadata == 2)
        {
            motionX -= acceleration;
        }
        else if (metadata == 3)
        {
            motionX += acceleration;
        }
        else if (metadata == 4)
        {
            motionZ += acceleration;
        }
        else if (metadata == 5)
        {
            motionZ -= acceleration;
        }
    }

    /**
     * Getters/setters for physics variables
     */

    /**
     * Returns the carts max speed.
     * Carts going faster than 1.1 cause issues with chunk loading.
     * Carts cant traverse slopes or corners at greater than 0.5 - 0.6.
     * This value is compared with the rails max speed to determine
     * the carts current max speed. A normal rails max speed is 0.4.
     * @return Carts max speed.
     */
    @Override
    public float getMaxSpeedRail()
    {
        return maxSpeedRail;
    }

    @Override
    public void setMaxSpeedRail(float value)
    {
        maxSpeedRail = value;
    }

    @Override
    public float getMaxSpeedGround()
    {
        return maxSpeedGround;
    }

    @Override
    public void setMaxSpeedGround(float value)
    {
        maxSpeedGround = value;
    }

    @Override
    public float getMaxSpeedAirLateral()
    {
        return maxSpeedAirLateral;
    }

    @Override
    public void setMaxSpeedAirLateral(float value)
    {
        maxSpeedAirLateral = value;
    }

    @Override
    public float getMaxSpeedAirVertical()
    {
        return maxSpeedAirVertical;
    }

    @Override
    public void setMaxSpeedAirVertical(float value)
    {
        maxSpeedAirVertical = value;
    }

    @Override
    public double getDragAir()
    {
        return dragAir;
    }

    @Override
    public void setDragAir(double value)
    {
        dragAir = value;
    }
}
