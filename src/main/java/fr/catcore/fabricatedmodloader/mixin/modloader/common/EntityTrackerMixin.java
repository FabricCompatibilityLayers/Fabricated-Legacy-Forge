package fr.catcore.fabricatedmodloader.mixin.modloader.common;

import modloader.EntityTrackerNonliving;
import modloader.ModLoader;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.decoration.PaintingEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.thrown.*;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(EntityTracker.class)
public abstract class EntityTrackerMixin {

    @Shadow public abstract void startTracking(Entity entity, int i, int j, boolean bl);

    @Inject(method = "startTracking(Lnet/minecraft/entity/Entity;)V", at = @At("RETURN"))
    private void modLoaderAddTrackers(Entity entity, CallbackInfo ci) {
        if (!(
                entity instanceof ServerPlayerEntity
                        || entity instanceof FishingBobberEntity
                        || entity instanceof AbstractArrowEntity
                        || entity instanceof SmallFireballEntity
                        || entity instanceof ExplosiveProjectileEntity
                        || entity instanceof SnowballEntity
                        || entity instanceof EnderPearlEntity
                        || entity instanceof EyeOfEnderEntity
                        || entity instanceof EggEntity
                        || entity instanceof PotionEntity
                        || entity instanceof ExperienceBottleEntity
                        || entity instanceof ItemEntity
                        || entity instanceof AbstractMinecartEntity
                        || entity instanceof BoatEntity
                        || entity instanceof SquidEntity
                        || entity instanceof EntityCategoryProvider
                        || entity instanceof EnderDragonEntity
                        || entity instanceof TntEntity
                        || entity instanceof FallingBlockEntity
                        || entity instanceof PaintingEntity
                        || entity instanceof ExperienceOrbEntity
                        || entity instanceof EndCrystalEntity
        )) {
            Iterator i$ = ModLoader.getTrackers().values().iterator();

            while(i$.hasNext()) {
                EntityTrackerNonliving tracker = (EntityTrackerNonliving)i$.next();
                if (tracker.entityClass.isAssignableFrom(entity.getClass())) {
                    this.startTracking(entity, tracker.viewDistance, tracker.updateFrequency, tracker.trackMotion);
                }
            }
        }
    }


}
