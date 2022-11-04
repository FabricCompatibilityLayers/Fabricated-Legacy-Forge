package fr.catcore.fabricatedforge.mixin.forgefml.entity.mob;

import fr.catcore.fabricatedforge.mixininterface.IMobEntity;
import fr.catcore.fabricatedforge.mixininterface.IStatusEffectInstance;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.control.BodyControl;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends Entity implements IMobEntity {
    @Shadow public int field_3333;

    @Shadow public int field_3334;

    @Shadow public float field_3313;

    @Shadow protected float field_3318;

    @Shadow protected float field_3317;

    @Shadow public float field_3293;

    @Shadow private BodyControl bodyControl;

    @Shadow public float field_3314;

    @Shadow public float field_3316;

    @Shadow public float field_3315;

    @Shadow protected float field_3319;

    @Shadow protected int field_3346;

    @Shadow protected int field_3294;

    @Shadow public abstract boolean method_2581(StatusEffect statusEffect);

    @Shadow public float field_3309;

    @Shadow public int field_3310;

    @Shadow protected int field_3345;

    @Shadow public int field_3295;

    @Shadow public int field_3297;

    @Shadow public int field_3298;

    @Shadow public float field_3299;

    @Shadow public abstract void method_2650(MobEntity mobEntity);

    @Shadow protected int field_3332;

    @Shadow protected PlayerEntity field_3331;

    @Shadow public abstract void method_2584(Entity entity, int i, double d, double e);

    @Shadow protected abstract String method_2605();

    @Shadow protected abstract float method_2602();

    @Shadow protected abstract float method_2665();

    @Shadow protected abstract String method_2604();

    @Shadow protected abstract int method_2626(DamageSource damageSource, int i);

    @Shadow protected abstract int method_2648(DamageSource damageSource, int i);

    @Shadow protected int field_3287;

    @Shadow protected boolean field_3304;

    @Shadow public abstract boolean method_2662();

    @Shadow protected abstract void method_2587(boolean bl, int i);

    @Shadow protected abstract void method_2672(int i);

    @Shadow public abstract float method_2622();

    @Shadow public float field_3289;

    @Shadow public float field_3290;

    @Shadow public float field_3308;

    @Shadow public float field_3330;

    @Shadow private int field_3327;

    @Shadow protected int field_3338;

    @Shadow protected double field_3339;

    @Shadow protected double field_3340;

    @Shadow protected double field_3341;

    @Shadow protected double field_3342;

    @Shadow protected double field_3343;

    @Shadow protected abstract boolean method_2610();

    @Shadow public boolean field_3350;

    @Shadow protected float field_3347;

    @Shadow protected float field_3348;

    @Shadow protected float field_3349;

    @Shadow protected abstract boolean method_2609();

    @Shadow protected abstract void mobTick();

    @Shadow protected abstract void method_2635();

    @Shadow protected abstract float method_2646();

    @Shadow public abstract StatusEffectInstance method_2627(StatusEffect statusEffect);

    @Shadow protected HashMap field_3335;

    @Shadow protected abstract void method_2649(StatusEffectInstance statusEffectInstance);

    @Shadow protected abstract boolean method_2608();

    @Shadow private float field_3344;

    public MobEntityMixin(World world) {
        super(world);
    }

    @Inject(method = {"method_2629", "method_2650"}, at = @At("RETURN"))
    private void FMLOnLivingSetAttackTarget(MobEntity par1EntityLiving, CallbackInfo ci) {
        ForgeHooks.onLivingSetAttackTarget((MobEntity)(Object) this, par1EntityLiving);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void tick() {
        if (!ForgeHooks.onLivingUpdate((MobEntity)(Object) this)) {
            super.tick();
            if (this.field_3333 > 0) {
                if (this.field_3334 <= 0) {
                    this.field_3334 = 60;
                }

                --this.field_3334;
                if (this.field_3334 <= 0) {
                    --this.field_3333;
                }
            }

            this.method_2651();
            double var1 = this.x - this.prevX;
            double var3 = this.z - this.prevZ;
            float var5 = (float)(var1 * var1 + var3 * var3);
            float var6 = this.field_3313;
            float var7 = 0.0F;
            this.field_3317 = this.field_3318;
            float var8 = 0.0F;
            if (var5 > 0.0025000002F) {
                var8 = 1.0F;
                var7 = (float)Math.sqrt((double)var5) * 3.0F;
                var6 = (float)Math.atan2(var3, var1) * 180.0F / 3.1415927F - 90.0F;
            }

            if (this.field_3293 > 0.0F) {
                var6 = this.yaw;
            }

            if (!this.onGround) {
                var8 = 0.0F;
            }

            this.field_3318 += (var8 - this.field_3318) * 0.3F;
            this.world.profiler.push("headTurn");
            if (this.method_2608()) {
                this.bodyControl.tick();
            } else {
                float var9 = MathHelper.wrapDegrees(var6 - this.field_3313);
                this.field_3313 += var9 * 0.3F;
                float var10 = MathHelper.wrapDegrees(this.yaw - this.field_3313);
                boolean var11 = var10 < -90.0F || var10 >= 90.0F;
                if (var10 < -75.0F) {
                    var10 = -75.0F;
                }

                if (var10 >= 75.0F) {
                    var10 = 75.0F;
                }

                this.field_3313 = this.yaw - var10;
                if (var10 * var10 > 2500.0F) {
                    this.field_3313 += var10 * 0.2F;
                }

                if (var11) {
                    var7 *= -1.0F;
                }
            }

            this.world.profiler.pop();
            this.world.profiler.push("rangeChecks");

            while(this.yaw - this.prevYaw < -180.0F) {
                this.prevYaw -= 360.0F;
            }

            while(this.yaw - this.prevYaw >= 180.0F) {
                this.prevYaw += 360.0F;
            }

            while(this.field_3313 - this.field_3314 < -180.0F) {
                this.field_3314 -= 360.0F;
            }

            while(this.field_3313 - this.field_3314 >= 180.0F) {
                this.field_3314 += 360.0F;
            }

            while(this.pitch - this.prevPitch < -180.0F) {
                this.prevPitch -= 360.0F;
            }

            while(this.pitch - this.prevPitch >= 180.0F) {
                this.prevPitch += 360.0F;
            }

            while(this.field_3315 - this.field_3316 < -180.0F) {
                this.field_3316 -= 360.0F;
            }

            while(this.field_3315 - this.field_3316 >= 180.0F) {
                this.field_3316 += 360.0F;
            }

            this.world.profiler.pop();
            this.field_3319 += var7;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean damage(DamageSource par1DamageSource, int par2) {
        if (ForgeHooks.onLivingAttack((MobEntity)(Object) this, par1DamageSource, par2)) {
            return false;
        } else if (this.world.isClient) {
            return false;
        } else {
            this.field_3346 = 0;
            if (this.field_3294 <= 0) {
                return false;
            } else if (par1DamageSource.isFire() && this.method_2581(StatusEffect.FIRE_RESISTANCE)) {
                return false;
            } else {
                this.field_3309 = 1.5F;
                boolean var3 = true;
                if ((float)this.field_3223 > (float)this.field_3310 / 2.0F) {
                    if (par2 <= this.field_3345) {
                        return false;
                    }

                    this.method_2653(par1DamageSource, par2 - this.field_3345);
                    this.field_3345 = par2;
                    var3 = false;
                } else {
                    this.field_3345 = par2;
                    this.field_3295 = this.field_3294;
                    this.field_3223 = this.field_3310;
                    this.method_2653(par1DamageSource, par2);
                    this.field_3297 = this.field_3298 = 10;
                }

                this.field_3299 = 0.0F;
                Entity var4 = par1DamageSource.getAttacker();
                if (var4 != null) {
                    if (var4 instanceof MobEntity) {
                        this.method_2650((MobEntity)var4);
                    }

                    if (var4 instanceof PlayerEntity) {
                        this.field_3332 = 60;
                        this.field_3331 = (PlayerEntity)var4;
                    } else if (var4 instanceof WolfEntity) {
                        WolfEntity var5 = (WolfEntity)var4;
                        if (var5.isTamed()) {
                            this.field_3332 = 60;
                            this.field_3331 = null;
                        }
                    }
                }

                if (var3) {
                    this.world.sendEntityStatus(this, (byte)2);
                    if (par1DamageSource != DamageSource.DROWN && par1DamageSource != DamageSource.EXPLOSION) {
                        this.scheduleVelocityUpdate();
                    }

                    if (var4 != null) {
                        double var9 = var4.x - this.x;

                        double var7;
                        for(var7 = var4.z - this.z; var9 * var9 + var7 * var7 < 1.0E-4; var7 = (Math.random() - Math.random()) * 0.01) {
                            var9 = (Math.random() - Math.random()) * 0.01;
                        }

                        this.field_3299 = (float)(Math.atan2(var7, var9) * 180.0 / Math.PI) - this.yaw;
                        this.method_2584(var4, par2, var9, var7);
                    } else {
                        this.field_3299 = (float)((int)(Math.random() * 2.0) * 180);
                    }
                }

                if (this.field_3294 <= 0) {
                    if (var3) {
                        this.world.playSound(this, this.method_2605(), this.method_2602(), this.method_2665());
                    }

                    this.dropInventory(par1DamageSource);
                } else if (var3) {
                    this.world.playSound(this, this.method_2604(), this.method_2602(), this.method_2665());
                }

                return true;
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_2653(DamageSource par1DamageSource, int par2) {
        par2 = ForgeHooks.onLivingHurt((MobEntity)(Object) this, par1DamageSource, par2);
        if (par2 > 0) {
            par2 = this.method_2626(par1DamageSource, par2);
            par2 = this.method_2648(par1DamageSource, par2);
            this.field_3294 -= par2;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void dropInventory(DamageSource par1DamageSource) {
        if (!ForgeHooks.onLivingDeath((MobEntity)(Object) this, par1DamageSource)) {
            Entity var2 = par1DamageSource.getAttacker();
            if (this.field_3287 >= 0 && var2 != null) {
                var2.updateKilledAdvancementCriterion(this, this.field_3287);
            }

            if (var2 != null) {
                var2.method_2501((MobEntity)(Object) this);
            }

            this.field_3304 = true;
            if (!this.world.isClient) {
                int var3 = 0;
                if (var2 instanceof PlayerEntity) {
                    var3 = EnchantmentHelper.method_3536(((PlayerEntity)var2).inventory);
                }

                this.captureDrops(true);
                this.getCapturedDrops().clear();
                int var4 = 0;
                if (!this.method_2662()) {
                    this.method_2587(this.field_3332 > 0, var3);
                    if (this.field_3332 > 0) {
                        var4 = this.random.nextInt(200) - var3;
                        if (var4 < 5) {
                            this.method_2672(var4 <= 0 ? 1 : 0);
                        }
                    }
                }

                this.captureDrops(false);
                if (!ForgeHooks.onLivingDrops((MobEntity)(Object) this, par1DamageSource, this.getCapturedDrops(), var3, this.field_3332 > 0, var4)) {
                    for (ItemEntity item : this.getCapturedDrops()) {
                        this.world.spawnEntity(item);
                    }
                }
            }

            this.world.sendEntityStatus(this, (byte)3);
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_2490(float par1) {
        par1 = ForgeHooks.onLivingFall((MobEntity)(Object) this, par1);
        if (!(par1 <= 0.0F)) {
            super.method_2490(par1);
            int var2 = MathHelper.ceil(par1 - 3.0F);
            if (var2 > 0) {
                if (var2 > 4) {
                    this.world.playSound(this, "damage.fallbig", 1.0F, 1.0F);
                } else {
                    this.world.playSound(this, "damage.fallsmall", 1.0F, 1.0F);
                }

                this.damage(DamageSource.FALL, var2);
                int var3 = this.world.getBlock(MathHelper.floor(this.x), MathHelper.floor(this.y - 0.20000000298023224 - (double)this.heightOffset), MathHelper.floor(this.z));
                if (var3 > 0) {
                    BlockSoundGroup var4 = Block.BLOCKS[var3].soundGroup;
                    this.world.playSound(this, var4.getStepId(), var4.getVolume() * 0.5F, var4.getPitch() * 0.75F);
                }
            }

        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_2657(float par1, float par2) {
        double var9;
        float var5;
        if (this.isTouchingWater() && (!((Object)this instanceof PlayerEntity) || !((PlayerEntity)(Object)this).abilities.flying)) {
            var9 = this.y;
            this.updateVelocity(par1, par2, this.method_2608() ? 0.04F : 0.02F);
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= 0.800000011920929;
            this.velocityY *= 0.800000011920929;
            this.velocityZ *= 0.800000011920929;
            this.velocityY -= 0.02;
            if (this.horizontalCollision && this.doesNotCollide(this.velocityX, this.velocityY + 0.6000000238418579 - this.y + var9, this.velocityZ)) {
                this.velocityY = 0.30000001192092896;
            }
        } else if (this.method_2469() && (!((Object)this instanceof PlayerEntity) || !((PlayerEntity)(Object)this).abilities.flying)) {
            var9 = this.y;
            this.updateVelocity(par1, par2, 0.02F);
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= 0.5;
            this.velocityY *= 0.5;
            this.velocityZ *= 0.5;
            this.velocityY -= 0.02;
            if (this.horizontalCollision && this.doesNotCollide(this.velocityX, this.velocityY + 0.6000000238418579 - this.y + var9, this.velocityZ)) {
                this.velocityY = 0.30000001192092896;
            }
        } else {
            float var3 = 0.91F;
            if (this.onGround) {
                var3 = 0.54600006F;
                int var4 = this.world.getBlock(MathHelper.floor(this.x), MathHelper.floor(this.boundingBox.minY) - 1, MathHelper.floor(this.z));
                if (var4 > 0) {
                    var3 = Block.BLOCKS[var4].slipperiness * 0.91F;
                }
            }

            float var8 = 0.16277136F / (var3 * var3 * var3);
            if (this.onGround) {
                if (this.method_2608()) {
                    var5 = this.method_2622();
                } else {
                    var5 = this.field_3289;
                }

                var5 *= var8;
            } else {
                var5 = this.field_3290;
            }

            this.updateVelocity(par1, par2, var5);
            var3 = 0.91F;
            if (this.onGround) {
                var3 = 0.54600006F;
                int var6 = this.world.getBlock(MathHelper.floor(this.x), MathHelper.floor(this.boundingBox.minY) - 1, MathHelper.floor(this.z));
                if (var6 > 0) {
                    var3 = Block.BLOCKS[var6].slipperiness * 0.91F;
                }
            }

            if (this.method_2660()) {
                float var10 = 0.15F;
                if (this.velocityX < (double)(-var10)) {
                    this.velocityX = (double)(-var10);
                }

                if (this.velocityX > (double)var10) {
                    this.velocityX = (double)var10;
                }

                if (this.velocityZ < (double)(-var10)) {
                    this.velocityZ = (double)(-var10);
                }

                if (this.velocityZ > (double)var10) {
                    this.velocityZ = (double)var10;
                }

                this.fallDistance = 0.0F;
                if (this.velocityY < -0.15) {
                    this.velocityY = -0.15;
                }

                boolean var7 = this.isSneaking() && (Object)this instanceof PlayerEntity;
                if (var7 && this.velocityY < 0.0) {
                    this.velocityY = 0.0;
                }
            }

            this.move(this.velocityX, this.velocityY, this.velocityZ);
            if (this.horizontalCollision && this.method_2660()) {
                this.velocityY = 0.2;
            }

            this.velocityY -= 0.08;
            this.velocityY *= 0.9800000190734863;
            this.velocityX *= (double)var3;
            this.velocityZ *= (double)var3;
        }

        this.field_3308 = this.field_3309;
        var9 = this.x - this.prevX;
        double var12 = this.z - this.prevZ;
        var5 = MathHelper.sqrt(var9 * var9 + var12 * var12) * 4.0F;
        if (var5 > 1.0F) {
            var5 = 1.0F;
        }

        this.field_3309 += (var5 - this.field_3309) * 0.4F;
        this.field_3330 += this.field_3309;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_2660() {
        int var1 = MathHelper.floor(this.x);
        int var2 = MathHelper.floor(this.boundingBox.minY);
        int var3 = MathHelper.floor(this.z);
        int var4 = this.world.getBlock(var1, var2, var3);
        return ForgeHooks.isLivingOnLadder(Block.BLOCKS[var4], this.world, var1, var2, var3);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_2651() {
        if (this.field_3327 > 0) {
            --this.field_3327;
        }

        if (this.field_3338 > 0) {
            double var1 = this.x + (this.field_3339 - this.x) / (double)this.field_3338;
            double var3 = this.y + (this.field_3340 - this.y) / (double)this.field_3338;
            double var5 = this.z + (this.field_3341 - this.z) / (double)this.field_3338;
            double var7 = MathHelper.wrapDegrees(this.field_3342 - (double)this.yaw);
            this.yaw = (float)((double)this.yaw + var7 / (double)this.field_3338);
            this.pitch = (float)((double)this.pitch + (this.field_3343 - (double)this.pitch) / (double)this.field_3338);
            --this.field_3338;
            this.updatePosition(var1, var3, var5);
            this.setRotation(this.yaw, this.pitch);
        }

        if (Math.abs(this.velocityX) < 0.005) {
            this.velocityX = 0.0;
        }

        if (Math.abs(this.velocityY) < 0.005) {
            this.velocityY = 0.0;
        }

        if (Math.abs(this.velocityZ) < 0.005) {
            this.velocityZ = 0.0;
        }

        this.world.profiler.push("ai");
        if (this.method_2610()) {
            this.field_3350 = false;
            this.field_3347 = 0.0F;
            this.field_3348 = 0.0F;
            this.field_3349 = 0.0F;
        } else if (this.method_2609()) {
            if (this.method_2608()) {
                this.world.profiler.push("newAi");
                this.mobTick();
                this.world.profiler.pop();
            } else {
                this.world.profiler.push("oldAi");
                this.method_2635();
                this.world.profiler.pop();
                this.field_3315 = this.yaw;
            }
        }

        this.world.profiler.pop();
        this.world.profiler.push("jump");
        if (this.field_3350) {
            if (!this.isTouchingWater() && !this.method_2469()) {
                if (this.onGround && this.field_3327 == 0) {
                    this.method_2612();
                    this.field_3327 = 10;
                }
            } else {
                this.velocityY += 0.03999999910593033;
            }
        } else {
            this.field_3327 = 0;
        }

        this.world.profiler.pop();
        this.world.profiler.push("travel");
        this.field_3347 *= 0.98F;
        this.field_3348 *= 0.98F;
        this.field_3349 *= 0.9F;
        float var9 = this.field_3289;
        this.field_3289 *= this.method_2646();
        this.method_2657(this.field_3347, this.field_3348);
        this.field_3289 = var9;
        this.world.profiler.pop();
        this.world.profiler.push("push");
        if (!this.world.isClient) {
            List var2 = this.world.getEntitiesIn(this, this.boundingBox.expand(0.20000000298023224, 0.0, 0.20000000298023224));
            if (var2 != null && !var2.isEmpty()) {
                for (Object o : var2) {
                    Entity var4 = (Entity) o;
                    if (var4.isPushable()) {
                        var4.pushAwayFrom(this);
                    }
                }
            }
        }

        this.world.profiler.pop();
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_2612() {
        this.velocityY = 0.41999998688697815;
        if (this.method_2581(StatusEffect.JUMP_BOOST)) {
            this.velocityY += (double)((float)(this.method_2627(StatusEffect.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
        }

        if (this.isSprinting()) {
            float var1 = this.yaw * 0.017453292F;
            this.velocityX -= (double)(MathHelper.sin(var1) * 0.2F);
            this.velocityZ += (double)(MathHelper.cos(var1) * 0.2F);
        }

        this.velocityDirty = true;
        ForgeHooks.onLivingJump((MobEntity)(Object) this);
    }

    @Override
    public void method_2674(int par1) {
        this.field_3335.remove(par1);
    }

    @Override
    public void curePotionEffects(ItemStack curativeItem) {
        Iterator<Integer> potionKey = this.field_3335.keySet().iterator();
        if (!this.world.isClient) {
            while(potionKey.hasNext()) {
                Integer key = (Integer)potionKey.next();
                StatusEffectInstance effect = (StatusEffectInstance)this.field_3335.get(key);
                if (((IStatusEffectInstance)effect).isCurativeItem(curativeItem)) {
                    potionKey.remove();
                    this.method_2649(effect);
                }
            }

        }
    }

    @Override
    public float getField_3344() {
        return this.field_3344;
    }

    @Override
    public void setField_3344(float field_3344) {
        this.field_3344 = field_3344;
    }
}
