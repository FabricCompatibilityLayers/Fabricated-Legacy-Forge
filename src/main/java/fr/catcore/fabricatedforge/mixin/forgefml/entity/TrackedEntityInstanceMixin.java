package fr.catcore.fabricatedforge.mixin.forgefml.entity;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.decoration.PaintingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.thrown.*;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.class_642;
import net.minecraft.network.class_665;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.Set;

@Mixin(TrackedEntityInstance.class)
public abstract class TrackedEntityInstanceMixin {

    @Shadow public Entity trackedEntity;

    @Shadow public int serializedX;

    @Shadow public int serializedZ;

    @Shadow public int trackingDistance;

    @Shadow protected abstract boolean method_2187(ServerPlayerEntity player);

    @Shadow public Set players;

    @Shadow public double velocityX;

    @Shadow public double velocityY;

    @Shadow public double velocityZ;

    @Shadow public int serializedY;

    @Shadow private boolean trackVelocity;

    @Shadow public int headRotationYaw;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_2184(ServerPlayerEntity par1EntityPlayerMP) {
        if (par1EntityPlayerMP != this.trackedEntity) {
            double var2 = par1EntityPlayerMP.x - (double)(this.serializedX / 32);
            double var4 = par1EntityPlayerMP.z - (double)(this.serializedZ / 32);
            if (var2 >= (double)(-this.trackingDistance) && var2 <= (double)this.trackingDistance && var4 >= (double)(-this.trackingDistance) && var4 <= (double)this.trackingDistance) {
                if (!this.players.contains(par1EntityPlayerMP) && this.method_2187(par1EntityPlayerMP)) {
                    this.players.add(par1EntityPlayerMP);
                    Packet var6 = this.method_2182();
                    par1EntityPlayerMP.field_2823.sendPacket(var6);
                    this.velocityX = this.trackedEntity.velocityX;
                    this.velocityY = this.trackedEntity.velocityY;
                    this.velocityZ = this.trackedEntity.velocityZ;
                    int posX = MathHelper.floor(this.trackedEntity.x * 32.0);
                    int posY = MathHelper.floor(this.trackedEntity.y * 32.0);
                    int posZ = MathHelper.floor(this.trackedEntity.z * 32.0);
                    if (posX != this.serializedX || posY != this.serializedY || posZ != this.serializedZ) {
                        FMLNetworkHandler.makeEntitySpawnAdjustment(this.trackedEntity.id, par1EntityPlayerMP, this.serializedX, this.serializedY, this.serializedZ);
                    }

                    if (this.trackVelocity && !(var6 instanceof class_642)) {
                        par1EntityPlayerMP.field_2823.sendPacket(new EntityVelocityChangeS2CPacket(this.trackedEntity.id, this.trackedEntity.velocityX, this.trackedEntity.velocityY, this.trackedEntity.velocityZ));
                    }

                    if (this.trackedEntity.vehicle != null) {
                        par1EntityPlayerMP.field_2823.sendPacket(new EntityAttachS2CPacket(this.trackedEntity, this.trackedEntity.vehicle));
                    }

                    ItemStack[] var7 = this.trackedEntity.getArmorStacks();
                    if (var7 != null) {
                        for(int var8 = 0; var8 < var7.length; ++var8) {
                            par1EntityPlayerMP.field_2823.sendPacket(new EntityEquipmentUpdate_S2CPacket(this.trackedEntity.id, var8, var7[var8]));
                        }
                    }

                    if (this.trackedEntity instanceof PlayerEntity) {
                        PlayerEntity var11 = (PlayerEntity)this.trackedEntity;
                        if (var11.method_2641()) {
                            par1EntityPlayerMP.field_2823.sendPacket(new class_665(this.trackedEntity, 0, MathHelper.floor(this.trackedEntity.x), MathHelper.floor(this.trackedEntity.y), MathHelper.floor(this.trackedEntity.z)));
                        }
                    }

                    if (this.trackedEntity instanceof MobEntity) {
                        MobEntity var12 = (MobEntity)this.trackedEntity;

                        for (Object o : var12.method_2644()) {
                            StatusEffectInstance var10 = (StatusEffectInstance) o;
                            par1EntityPlayerMP.field_2823.sendPacket(new EntityStatusEffect_S2CPacket(this.trackedEntity.id, var10));
                        }
                    }
                }
            } else if (this.players.contains(par1EntityPlayerMP)) {
                this.players.remove(par1EntityPlayerMP);
                par1EntityPlayerMP.removedEntities.add(this.trackedEntity.id);
            }
        }

    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    private Packet method_2182() {
        if (this.trackedEntity.removed) {
            System.out.println("Fetching addPacket for removed entity");
        }

        Packet pkt = FMLNetworkHandler.getEntitySpawningPacket(this.trackedEntity);
        if (pkt != null) {
            return pkt;
        } else if (this.trackedEntity instanceof ItemEntity) {
            ItemEntity var8 = (ItemEntity)this.trackedEntity;
            ItemDropS2CPacket var9 = new ItemDropS2CPacket(var8);
            var8.x = (double)var9.x / 32.0;
            var8.y = (double)var9.y / 32.0;
            var8.z = (double)var9.z / 32.0;
            return var9;
        } else if (this.trackedEntity instanceof ServerPlayerEntity) {
            return new PlayerSpawn_S2CPacket((PlayerEntity)this.trackedEntity);
        } else {
            if (this.trackedEntity instanceof AbstractMinecartEntity) {
                AbstractMinecartEntity var2;
                var2 = (AbstractMinecartEntity)this.trackedEntity;
                if (var2.field_3897 == 0) {
                    return new EntitySpawn_S2CPacket(this.trackedEntity, 10);
                }

                if (var2.field_3897 == 1) {
                    return new EntitySpawn_S2CPacket(this.trackedEntity, 11);
                }

                if (var2.field_3897 == 2) {
                    return new EntitySpawn_S2CPacket(this.trackedEntity, 12);
                }
            }

            if (this.trackedEntity instanceof BoatEntity) {
                return new EntitySpawn_S2CPacket(this.trackedEntity, 1);
            } else if (!(this.trackedEntity instanceof EntityCategoryProvider) && !(this.trackedEntity instanceof EnderDragonEntity)) {
                if (this.trackedEntity instanceof FishingBobberEntity) {
                    PlayerEntity var7 = ((FishingBobberEntity)this.trackedEntity).thrower;
                    return new EntitySpawn_S2CPacket(this.trackedEntity, 90, var7 != null ? var7.id : this.trackedEntity.id);
                } else if (this.trackedEntity instanceof AbstractArrowEntity) {
                    Entity var6 = ((AbstractArrowEntity)this.trackedEntity).owner;
                    return new EntitySpawn_S2CPacket(this.trackedEntity, 60, var6 != null ? var6.id : this.trackedEntity.id);
                } else if (this.trackedEntity instanceof SnowballEntity) {
                    return new EntitySpawn_S2CPacket(this.trackedEntity, 61);
                } else if (this.trackedEntity instanceof PotionEntity) {
                    return new EntitySpawn_S2CPacket(this.trackedEntity, 73, ((PotionEntity)this.trackedEntity).method_3237());
                } else if (this.trackedEntity instanceof ExperienceBottleEntity) {
                    return new EntitySpawn_S2CPacket(this.trackedEntity, 75);
                } else if (this.trackedEntity instanceof EnderPearlEntity) {
                    return new EntitySpawn_S2CPacket(this.trackedEntity, 65);
                } else if (this.trackedEntity instanceof EyeOfEnderEntity) {
                    return new EntitySpawn_S2CPacket(this.trackedEntity, 72);
                } else {
                    EntitySpawn_S2CPacket var2;
                    if (this.trackedEntity instanceof SmallFireballEntity) {
                        SmallFireballEntity var5 = (SmallFireballEntity)this.trackedEntity;
                        var2 = null;
                        if (var5.field_4040 != null) {
                            var2 = new EntitySpawn_S2CPacket(this.trackedEntity, 64, var5.field_4040.id);
                        } else {
                            var2 = new EntitySpawn_S2CPacket(this.trackedEntity, 64, 0);
                        }

                        var2.velocityX = (int)(var5.powerX * 8000.0);
                        var2.velocityY = (int)(var5.powerY * 8000.0);
                        var2.velocityZ = (int)(var5.powerZ * 8000.0);
                        return var2;
                    } else if (this.trackedEntity instanceof ExplosiveProjectileEntity) {
                        ExplosiveProjectileEntity var4 = (ExplosiveProjectileEntity)this.trackedEntity;
                        if (var4.field_4040 != null) {
                            var2 = new EntitySpawn_S2CPacket(this.trackedEntity, 63, ((ExplosiveProjectileEntity)this.trackedEntity).field_4040.id);
                        } else {
                            var2 = new EntitySpawn_S2CPacket(this.trackedEntity, 63, 0);
                        }

                        var2.velocityX = (int)(var4.powerX * 8000.0);
                        var2.velocityY = (int)(var4.powerY * 8000.0);
                        var2.velocityZ = (int)(var4.powerZ * 8000.0);
                        return var2;
                    } else if (this.trackedEntity instanceof EggEntity) {
                        return new EntitySpawn_S2CPacket(this.trackedEntity, 62);
                    } else if (this.trackedEntity instanceof TntEntity) {
                        return new EntitySpawn_S2CPacket(this.trackedEntity, 50);
                    } else if (this.trackedEntity instanceof EndCrystalEntity) {
                        return new EntitySpawn_S2CPacket(this.trackedEntity, 51);
                    } else if (this.trackedEntity instanceof FallingBlockEntity) {
                        FallingBlockEntity var3 = (FallingBlockEntity)this.trackedEntity;
                        return new EntitySpawn_S2CPacket(this.trackedEntity, 70, var3.field_3888 | var3.field_3889 << 16);
                    } else if (this.trackedEntity instanceof PaintingEntity) {
                        return new PaintingSpawn_S2CPacket((PaintingEntity)this.trackedEntity);
                    } else if (this.trackedEntity instanceof ExperienceOrbEntity) {
                        return new ExperienceOrbSpawn_S2CPacket((ExperienceOrbEntity)this.trackedEntity);
                    } else {
                        throw new IllegalArgumentException("Don't know how to add " + this.trackedEntity.getClass() + "!");
                    }
                }
            } else {
                this.headRotationYaw = MathHelper.floor(this.trackedEntity.getHeadRotation() * 256.0F / 360.0F);
                return new class_642((MobEntity)this.trackedEntity);
            }
        }
    }
}
