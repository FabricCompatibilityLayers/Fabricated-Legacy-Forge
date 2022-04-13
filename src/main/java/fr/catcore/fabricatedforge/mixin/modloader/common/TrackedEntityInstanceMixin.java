package fr.catcore.fabricatedforge.mixin.modloader.common;

import modloader.EntityTrackerNonliving;
import modloader.ModLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TrackedEntityInstance;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;

@Mixin(TrackedEntityInstance.class)
public class TrackedEntityInstanceMixin {

    @Shadow public Entity trackedEntity;

    @Inject(method = "method_2182", at = @At(value = "NEW", target = "Ljava/lang/IllegalArgumentException;<init>(Ljava/lang/String;)V"), cancellable = true)
    private void modLoaderGetSpawnPacket(CallbackInfoReturnable<Packet> cir) {
        Iterator i$ = ModLoader.getTrackers().values().iterator();

        EntityTrackerNonliving tracker;
        do {
            if (!i$.hasNext()) {
                throw new IllegalArgumentException("Don't know how to add " + this.trackedEntity.getClass() + "!");
            }

            tracker = (EntityTrackerNonliving)i$.next();
        } while(!tracker.entityClass.isAssignableFrom(this.trackedEntity.getClass()));

        cir.setReturnValue(tracker.mod.getSpawnPacket(this.trackedEntity, tracker.id));
    }
}
