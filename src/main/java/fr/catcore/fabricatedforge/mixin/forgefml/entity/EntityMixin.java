package fr.catcore.fabricatedforge.mixin.forgefml.entity;

import fr.catcore.fabricatedforge.mixininterface.IAbstractMinecartEntity;
import fr.catcore.fabricatedforge.mixininterface.IBlock;
import fr.catcore.fabricatedforge.mixininterface.IEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntity {

    @Shadow public World world;
    @Shadow public double y;

    @Shadow public abstract void updatePosition(double x, double y, double z);

    @Shadow public double x;
    @Shadow public double z;
    @Shadow @Final public Box boundingBox;
    @Shadow public double velocityX;
    @Shadow public double velocityY;
    @Shadow public double velocityZ;
    @Shadow public float pitch;

    @Shadow protected abstract NbtList toListNbt(float... values);

    @Shadow protected abstract NbtList toListNbt(double... values);

    @Shadow public float field_3214;
    @Shadow public float yaw;
    @Shadow public float fallDistance;
    @Shadow private int fireTicks;

    @Shadow public abstract int getAir();

    @Shadow public boolean onGround;

    @Shadow protected abstract void writeCustomDataToNbt(NbtCompound nbt);

    @Shadow public double prevTickX;
    @Shadow public double prevTickY;
    @Shadow public double prevTickZ;
    @Shadow public double prevX;
    @Shadow public double prevY;
    @Shadow public double prevZ;
    @Shadow public float prevYaw;
    @Shadow public float prevPitch;

    @Shadow public abstract void setAir(int air);

    @Shadow protected abstract void setRotation(float yaw, float pitch);

    @Shadow protected abstract void readCustomDataFromNbt(NbtCompound nbt);

    @Shadow public Entity vehicle;

    @Shadow protected abstract boolean getFlag(int index);

    @Shadow protected Random random;
    @Shadow public int dimension;

    @Shadow public abstract float getEyeHeight();

    @Shadow private boolean invulnerable;
    @Shadow public int netherPortalCooldown;

    @Shadow public abstract void populateCrashReport(CrashReportSection section);

    @Shadow public int ticksAlive;
    @Shadow public float prevHorizontalSpeed;
    @Shadow public float horizontalSpeed;

    @Shadow public abstract int getMaxNetherPortalTime();

    @Shadow protected boolean changingDimension;
    @Shadow
    protected int netherPortalTime;

    @Shadow public abstract int getDefaultNetherPortalCooldown();

    @Shadow public abstract void teleportToDimension(int dimensionId);

    @Shadow public abstract boolean isSprinting();

    @Shadow public abstract boolean isTouchingWater();

    @Shadow public float heightOffset;
    @Shadow public float width;

    @Shadow public abstract boolean updateWaterState();

    @Shadow protected boolean isFireImmune;

    @Shadow public abstract boolean damage(DamageSource source, int damage);

    @Shadow public abstract boolean method_2469();

    @Shadow protected abstract void setOnFireFromLava();

    @Shadow protected abstract void destroy();

    @Shadow protected abstract void setFlag(int index, boolean value);

    @Shadow private boolean firstUpdate;
    @Shadow public boolean noClip;
    @Shadow protected boolean inLava;

    @Shadow public abstract boolean isSneaking();

    @Shadow public boolean field_3203;
    @Shadow public float stepHeight;
    @Shadow public boolean horizontalCollision;
    @Shadow public boolean verticalCollision;
    @Shadow public boolean colliding;

    @Shadow protected abstract void method_2489(double d, boolean bl);

    @Shadow protected abstract boolean canClimb();

    @Shadow public float distanceTraveled;
    @Shadow private int field_3233;

    @Shadow public abstract void playSound(String id, float volume, float pitch);

    @Shadow protected abstract void method_2494(int i, int j, int k, int l);

    @Shadow protected abstract void checkBlockCollision();

    @Shadow public abstract boolean tickFire();

    @Shadow protected abstract void burn(int time);

    @Shadow public abstract void setOnFireFor(int seconds);

    @Shadow public int fireResistance;
    @Shadow public int id;
    @Shadow private static int entityCount;
    private NbtCompound customEntityData;
    public boolean captureDrops = false;
    public ArrayList<ItemEntity> capturedDrops = new ArrayList<>();
    private UUID persistentID;

    /**
     * @author forge
     * @reason additional check
     */
    @Overwrite
    public void baseTick() {
        this.world.profiler.push("entityBaseTick");
        if (this.vehicle != null && this.vehicle.removed) {
            this.vehicle = null;
        }

        this.prevHorizontalSpeed = this.horizontalSpeed;
        this.prevX = this.x;
        this.prevY = this.y;
        this.prevZ = this.z;
        this.prevPitch = this.pitch;
        this.prevYaw = this.yaw;
        if (!this.world.isClient && this.world instanceof ServerWorld) {
            this.world.profiler.push("portal");
            MinecraftServer var1 = ((ServerWorld)this.world).getServer();
            int var2 = this.getMaxNetherPortalTime();
            if (this.changingDimension) {
                if (var1.isNetherAllowed()) {
                    if (this.vehicle == null && this.netherPortalTime++ >= var2) {
                        this.netherPortalTime = var2;
                        this.netherPortalCooldown = this.getDefaultNetherPortalCooldown();
                        byte var3;
                        if (this.world.dimension.dimensionType == -1) {
                            var3 = 0;
                        } else {
                            var3 = -1;
                        }

                        this.teleportToDimension(var3);
                    }

                    this.changingDimension = false;
                }
            } else {
                if (this.netherPortalTime > 0) {
                    this.netherPortalTime -= 4;
                }

                if (this.netherPortalTime < 0) {
                    this.netherPortalTime = 0;
                }
            }

            if (this.netherPortalCooldown > 0) {
                --this.netherPortalCooldown;
            }

            this.world.profiler.pop();
        }

        if (this.isSprinting() && !this.isTouchingWater()) {
            int var5 = MathHelper.floor(this.x);
            int var2 = MathHelper.floor(this.y - 0.2F - (double)this.heightOffset);
            int var6 = MathHelper.floor(this.z);
            int var4 = this.world.getBlock(var5, var2, var6);
            if (var4 > 0) {
                this.world
                        .spawnParticle(
                                "tilecrack_" + var4 + "_" + this.world.getBlockData(var5, var2, var6),
                                this.x + ((double)this.random.nextFloat() - 0.5) * (double)this.width,
                                this.boundingBox.minY + 0.1,
                                this.z + ((double)this.random.nextFloat() - 0.5) * (double)this.width,
                                -this.velocityX * 4.0,
                                1.5,
                                -this.velocityZ * 4.0
                        );
            }
        }

        this.updateWaterState();
        if (this.world.isClient) {
            this.fireTicks = 0;
        } else if (this.fireTicks > 0) {
            if (this.isFireImmune) {
                this.fireTicks -= 4;
                if (this.fireTicks < 0) {
                    this.fireTicks = 0;
                }
            } else {
                if (this.fireTicks % 20 == 0) {
                    this.damage(DamageSource.ON_FIRE, 1);
                }

                --this.fireTicks;
            }
        }

        if (this.method_2469()) {
            this.setOnFireFromLava();
            this.fallDistance *= 0.5F;
        }

        if (this.y < -64.0) {
            this.destroy();
        }

        if (!this.world.isClient) {
            this.setFlag(0, this.fireTicks > 0);
            this.setFlag(2, this.vehicle != null && this.vehicle.shouldRiderSit());
        }

        this.firstUpdate = false;
        this.world.profiler.pop();
    }

    /**
     * @author forge
     * @reason idk tbh
     */
    @Overwrite
    public void move(double par1, double par3, double par5) {
        if (this.noClip) {
            this.boundingBox.offset(par1, par3, par5);
            this.x = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0;
            this.y = this.boundingBox.minY + (double)this.heightOffset - (double)this.field_3214;
            this.z = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0;
        } else {
            this.world.profiler.push("move");
            this.field_3214 *= 0.4F;
            double var7 = this.x;
            double var9 = this.y;
            double var11 = this.z;
            if (this.inLava) {
                this.inLava = false;
                par1 *= 0.25;
                par3 *= 0.05F;
                par5 *= 0.25;
                this.velocityX = 0.0;
                this.velocityY = 0.0;
                this.velocityZ = 0.0;
            }

            double var13 = par1;
            double var15 = par3;
            double var17 = par5;
            Box var19 = this.boundingBox.method_591();
            boolean var20 = this.onGround && this.isSneaking() && (Object)this instanceof PlayerEntity;
            if (var20) {
                double var21;
                for(var21 = 0.05; par1 != 0.0 && this.world.doesBoxCollide((Entity)(Object) this, this.boundingBox.method_592(par1, -1.0, 0.0)).isEmpty(); var13 = par1) {
                    if (par1 < var21 && par1 >= -var21) {
                        par1 = 0.0;
                    } else if (par1 > 0.0) {
                        par1 -= var21;
                    } else {
                        par1 += var21;
                    }
                }

                for(; par5 != 0.0 && this.world.doesBoxCollide((Entity)(Object) this, this.boundingBox.method_592(0.0, -1.0, par5)).isEmpty(); var17 = par5) {
                    if (par5 < var21 && par5 >= -var21) {
                        par5 = 0.0;
                    } else if (par5 > 0.0) {
                        par5 -= var21;
                    } else {
                        par5 += var21;
                    }
                }

                while(par1 != 0.0 && par5 != 0.0 && this.world.doesBoxCollide((Entity)(Object) this, this.boundingBox.method_592(par1, -1.0, par5)).isEmpty()) {
                    if (par1 < var21 && par1 >= -var21) {
                        par1 = 0.0;
                    } else if (par1 > 0.0) {
                        par1 -= var21;
                    } else {
                        par1 += var21;
                    }

                    if (par5 < var21 && par5 >= -var21) {
                        par5 = 0.0;
                    } else if (par5 > 0.0) {
                        par5 -= var21;
                    } else {
                        par5 += var21;
                    }

                    var13 = par1;
                    var17 = par5;
                }
            }

            List var35 = this.world.doesBoxCollide((Entity)(Object) this, this.boundingBox.stretch(par1, par3, par5));

            for(int var22 = 0; var22 < var35.size(); ++var22) {
                par3 = ((Box)var35.get(var22)).method_589(this.boundingBox, par3);
            }

            this.boundingBox.offset(0.0, par3, 0.0);
            if (!this.field_3203 && var15 != par3) {
                par5 = 0.0;
                par3 = 0.0;
                par1 = 0.0;
            }

            boolean var34 = this.onGround || var15 != par3 && var15 < 0.0;

            for(int var23 = 0; var23 < var35.size(); ++var23) {
                par1 = ((Box)var35.get(var23)).method_583(this.boundingBox, par1);
            }

            this.boundingBox.offset(par1, 0.0, 0.0);
            if (!this.field_3203 && var13 != par1) {
                par5 = 0.0;
                par3 = 0.0;
                par1 = 0.0;
            }

            for(int var391 = 0; var391 < var35.size(); ++var391) {
                par5 = ((Box)var35.get(var391)).method_594(this.boundingBox, par5);
            }

            this.boundingBox.offset(0.0, 0.0, par5);
            if (!this.field_3203 && var17 != par5) {
                par5 = 0.0;
                par3 = 0.0;
                par1 = 0.0;
            }

            if (this.stepHeight > 0.0F && var34 && (var20 || this.field_3214 < 0.05F) && (var13 != par1 || var17 != par5)) {
                double var36 = par1;
                double var25 = par3;
                double var27 = par5;
                par1 = var13;
                par3 = (double)this.stepHeight;
                par5 = var17;
                Box var29 = this.boundingBox.method_591();
                this.boundingBox.copyFrom(var19);
                var35 = this.world.doesBoxCollide((Entity)(Object) this, this.boundingBox.stretch(var13, par3, var17));

                for(int var30 = 0; var30 < var35.size(); ++var30) {
                    par3 = ((Box)var35.get(var30)).method_589(this.boundingBox, par3);
                }

                this.boundingBox.offset(0.0, par3, 0.0);
                if (!this.field_3203 && var15 != par3) {
                    par5 = 0.0;
                    par3 = 0.0;
                    par1 = 0.0;
                }

                for(int var42 = 0; var42 < var35.size(); ++var42) {
                    par1 = ((Box)var35.get(var42)).method_583(this.boundingBox, par1);
                }

                this.boundingBox.offset(par1, 0.0, 0.0);
                if (!this.field_3203 && var13 != par1) {
                    par5 = 0.0;
                    par3 = 0.0;
                    par1 = 0.0;
                }

                for(int var43 = 0; var43 < var35.size(); ++var43) {
                    par5 = ((Box)var35.get(var43)).method_594(this.boundingBox, par5);
                }

                this.boundingBox.offset(0.0, 0.0, par5);
                if (!this.field_3203 && var17 != par5) {
                    par5 = 0.0;
                    par3 = 0.0;
                    par1 = 0.0;
                }

                if (!this.field_3203 && var15 != par3) {
                    par5 = 0.0;
                    par3 = 0.0;
                    par1 = 0.0;
                } else {
                    par3 = (double)(-this.stepHeight);

                    for(int var44 = 0; var44 < var35.size(); ++var44) {
                        par3 = ((Box)var35.get(var44)).method_589(this.boundingBox, par3);
                    }

                    this.boundingBox.offset(0.0, par3, 0.0);
                }

                if (var36 * var36 + var27 * var27 >= par1 * par1 + par5 * par5) {
                    par1 = var36;
                    par3 = var25;
                    par5 = var27;
                    this.boundingBox.copyFrom(var29);
                }
            }

            this.world.profiler.pop();
            this.world.profiler.push("rest");
            this.x = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0;
            this.y = this.boundingBox.minY + (double)this.heightOffset - (double)this.field_3214;
            this.z = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0;
            this.horizontalCollision = var13 != par1 || var17 != par5;
            this.verticalCollision = var15 != par3;
            this.onGround = var15 != par3 && var15 < 0.0;
            this.colliding = this.horizontalCollision || this.verticalCollision;
            this.method_2489(par3, this.onGround);
            if (var13 != par1) {
                this.velocityX = 0.0;
            }

            if (var15 != par3) {
                this.velocityY = 0.0;
            }

            if (var17 != par5) {
                this.velocityZ = 0.0;
            }

            double var36 = this.x - var7;
            double var25 = this.y - var9;
            double var27 = this.z - var11;
            if (this.canClimb() && !var20 && this.vehicle == null) {
                int var37 = MathHelper.floor(this.x);
                int var30 = MathHelper.floor(this.y - 0.2F - (double)this.heightOffset);
                int var31 = MathHelper.floor(this.z);
                int var32 = this.world.getBlock(var37, var30, var31);
                if (var32 == 0) {
                    int var33 = this.world.getBlockType(var37, var30 - 1, var31);
                    if (var33 == 11 || var33 == 32 || var33 == 21) {
                        var32 = this.world.getBlock(var37, var30 - 1, var31);
                    }
                }

                if (var32 != Block.LADDER_BLOCK.id) {
                    var25 = 0.0;
                }

                this.horizontalSpeed = (float)((double)this.horizontalSpeed + (double)MathHelper.sqrt(var36 * var36 + var27 * var27) * 0.6);
                this.distanceTraveled = (float)((double)this.distanceTraveled + (double)MathHelper.sqrt(var36 * var36 + var25 * var25 + var27 * var27) * 0.6);
                if (this.distanceTraveled > (float)this.field_3233 && var32 > 0) {
                    this.field_3233 = (int)this.distanceTraveled + 1;
                    if (this.isTouchingWater()) {
                        float var39 = MathHelper.sqrt(
                                this.velocityX * this.velocityX * 0.2F + this.velocityY * this.velocityY + this.velocityZ * this.velocityZ * 0.2F
                        )
                                * 0.35F;
                        if (var39 > 1.0F) {
                            var39 = 1.0F;
                        }

                        this.playSound("liquid.swim", var39, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                    }

                    this.method_2494(var37, var30, var31, var32);
                    Block.BLOCKS[var32].method_437(this.world, var37, var30, var31, (Entity)(Object) this);
                }
            }

            this.checkBlockCollision();
            boolean var38 = this.tickFire();
            if (this.world.containsFireSource(this.boundingBox.increment(0.001, 0.001, 0.001))) {
                this.burn(1);
                if (!var38) {
                    ++this.fireTicks;
                    if (this.fireTicks == 0) {
                        this.setOnFireFor(8);
                    }
                }
            } else if (this.fireTicks <= 0) {
                this.fireTicks = -this.fireResistance;
            }

            if (var38 && this.fireTicks > 0) {
                this.playSound("random.fizz", 0.7F, 1.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                this.fireTicks = -this.fireResistance;
            }

            this.world.profiler.pop();
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    protected void afterSpawn() {
        if (this.world != null) {
            while(true) {
                if (this.y > 0.0) {
                    this.updatePosition(this.x, this.y, this.z);
                    if (!this.world.doesBoxCollide((Entity)(Object) this, this.boundingBox).isEmpty()) {
                        ++this.y;
                        continue;
                    }
                }

                this.velocityX = this.velocityY = this.velocityZ = 0.0;
                this.pitch = 0.0F;
                break;
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void writePlayerData(NbtCompound par1NBTTagCompound) {
        try {
            par1NBTTagCompound.put("Pos", this.toListNbt(this.x, this.y + (double)this.field_3214, this.z));
            par1NBTTagCompound.put("Motion", this.toListNbt(this.velocityX, this.velocityY, this.velocityZ));
            par1NBTTagCompound.put("Rotation", this.toListNbt(this.yaw, this.pitch));
            par1NBTTagCompound.putFloat("FallDistance", this.fallDistance);
            par1NBTTagCompound.putShort("Fire", (short)this.fireTicks);
            par1NBTTagCompound.putShort("Air", (short)this.getAir());
            par1NBTTagCompound.putBoolean("OnGround", this.onGround);
            par1NBTTagCompound.putInt("Dimension", this.dimension);
            par1NBTTagCompound.putBoolean("Invulnerable", this.invulnerable);
            par1NBTTagCompound.putInt("PortalCooldown", this.netherPortalCooldown);
            if (this.persistentID != null) {
                par1NBTTagCompound.putLong("PersistentIDMSB", this.persistentID.getMostSignificantBits());
                par1NBTTagCompound.putLong("PersistentIDLSB", this.persistentID.getLeastSignificantBits());
            }

            if (this.customEntityData != null) {
                par1NBTTagCompound.put("ForgeData", this.customEntityData);
            }

            this.writeCustomDataToNbt(par1NBTTagCompound);
        } catch (Throwable var5) {
            CrashReport var3 = CrashReport.create(var5, "Saving entity NBT");
            CrashReportSection var4 = var3.addElement("Entity being saved");
            this.populateCrashReport(var4);
            throw new CrashException(var3);
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void fromNbt(NbtCompound par1NBTTagCompound) {
        try {
            NbtList var2 = par1NBTTagCompound.getList("Pos");
            NbtList var6 = par1NBTTagCompound.getList("Motion");
            NbtList var7 = par1NBTTagCompound.getList("Rotation");
            this.velocityX = ((NbtDouble)var6.method_1218(0)).value;
            this.velocityY = ((NbtDouble)var6.method_1218(1)).value;
            this.velocityZ = ((NbtDouble)var6.method_1218(2)).value;
            if (Math.abs(this.velocityX) > 10.0) {
                this.velocityX = 0.0;
            }

            if (Math.abs(this.velocityY) > 10.0) {
                this.velocityY = 0.0;
            }

            if (Math.abs(this.velocityZ) > 10.0) {
                this.velocityZ = 0.0;
            }

            this.prevX = this.prevTickX = this.x = ((NbtDouble)var2.method_1218(0)).value;
            this.prevY = this.prevTickY = this.y = ((NbtDouble)var2.method_1218(1)).value;
            this.prevZ = this.prevTickZ = this.z = ((NbtDouble)var2.method_1218(2)).value;
            this.prevYaw = this.yaw = ((NbtFloat)var7.method_1218(0)).value;
            this.prevPitch = this.pitch = ((NbtFloat)var7.method_1218(1)).value;
            this.fallDistance = par1NBTTagCompound.getFloat("FallDistance");
            this.fireTicks = par1NBTTagCompound.getShort("Fire");
            this.setAir(par1NBTTagCompound.getShort("Air"));
            this.onGround = par1NBTTagCompound.getBoolean("OnGround");
            this.dimension = par1NBTTagCompound.getInt("Dimension");
            this.invulnerable = par1NBTTagCompound.getBoolean("Invulnerable");
            this.netherPortalCooldown = par1NBTTagCompound.getInt("PortalCooldown");
            this.updatePosition(this.x, this.y, this.z);
            this.setRotation(this.yaw, this.pitch);
            if (par1NBTTagCompound.contains("ForgeData")) {
                this.customEntityData = par1NBTTagCompound.getCompound("ForgeData");
            }

            if (par1NBTTagCompound.contains("PersistentIDMSB") && par1NBTTagCompound.contains("PersistentIDLSB")) {
                this.persistentID = new UUID(par1NBTTagCompound.getLong("PersistentIDMSB"), par1NBTTagCompound.getLong("PersistentIDLSB"));
            }

            this.readCustomDataFromNbt(par1NBTTagCompound);
        } catch (Throwable var51) {
            CrashReport var3 = CrashReport.create(var51, "Loading entity NBT");
            CrashReportSection var4 = var3.addElement("Entity being loaded");
            this.populateCrashReport(var4);
            throw new CrashException(var3);
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public ItemEntity dropItem(ItemStack par1ItemStack, float par2) {
        ItemEntity var3 = new ItemEntity(this.world, this.x, this.y + (double)par2, this.z, par1ItemStack);
        var3.pickupDelay = 10;
        if (this.captureDrops) {
            this.capturedDrops.add(var3);
        } else {
            this.world.spawnEntity(var3);
        }

        return var3;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public boolean hasVehicle() {
        return this.vehicle != null && this.vehicle.shouldRiderSit() || this.getFlag(2);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public float method_4444(Explosion par1Explosion, Block par2Block, int par3, int par4, int par5) {
        return ((IBlock)par2Block).getExplosionResistance((Entity)(Object) this, this.world, par3, par4, par5, this.x, this.y + (double)this.getEyeHeight(), this.z);
    }

    @Override
    public NbtCompound getEntityData() {
        if (this.customEntityData == null) {
            this.customEntityData = new NbtCompound();
        }

        return this.customEntityData;
    }

    @Override
    public boolean shouldRiderSit() {
        return true;
    }

    @Override
    public ItemStack getPickedResult(BlockHitResult target) {
        if ((Object)this instanceof PaintingEntity) {
            return new ItemStack(Item.PAINTING);
        } else if ((Object)this instanceof AbstractMinecartEntity) {
            return ((IAbstractMinecartEntity) this).getCartItem();
        } else if ((Object)this instanceof BoatEntity) {
            return new ItemStack(Item.BOAT);
        } else if ((Object)this instanceof ItemFrameEntity) {
            ItemStack held = ((ItemFrameEntity)(Object)this).getHeldItemStack();
            return held == null ? new ItemStack(Item.ITEM_FRAME) : held.copy();
        } else {
            int id = EntityType.getIdByEntity((Entity) (Object)this);
            return id > 0 && EntityType.field_3267.containsKey(id) ? new ItemStack(Item.SPAWN_EGG, 1, id) : null;
        }
    }

    @Override
    public UUID getPersistentID() {
        return this.persistentID;
    }

    @Override
    public synchronized void generatePersistentID() {
        if (this.persistentID == null) {
            this.persistentID = UUID.randomUUID();
        }
    }

    @Override
    public boolean captureDrops() {
        return this.captureDrops;
    }

    @Override
    public void captureDrops(boolean captureDrops) {
        this.captureDrops = captureDrops;
    }

    @Override
    public ArrayList<ItemEntity> getCapturedDrops() {
        return this.capturedDrops;
    }

    @Override
    public Random getRandom() {
        return this.random;
    }

    @Override
    public final void resetEntityId() {
        this.id = entityCount++;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 0;
    }
}
