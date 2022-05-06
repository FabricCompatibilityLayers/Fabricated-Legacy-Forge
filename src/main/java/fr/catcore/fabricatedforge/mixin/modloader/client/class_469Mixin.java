package fr.catcore.fabricatedforge.mixin.modloader.client;

import com.google.common.collect.Lists;
import modloader.EntityTrackerNonliving;
import modloader.ModLoader;
import net.minecraft.client.class_469;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.class_690;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessage_S2CPacket;
import net.minecraft.network.packet.s2c.play.Disconnect_S2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawn_S2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreen_S2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(class_469.class)
public abstract class class_469Mixin {

    @Shadow
    private ClientWorld world;

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void modLoaderClientConnect(class_690 par1, CallbackInfo ci) {
        ModLoader.clientConnect((class_469) (Object) this, par1);
    }

    private static final List<Integer> PACKET_TYPES = Lists.newArrayList(
            10, 11, 12, 90,
            60, 61, 65, 72, 63,
            64, 62, 73, 75, 1, 50, 51,
            70
    );

    @Unique
    private EntitySpawn_S2CPacket cachedPacket = null;

    @Inject(method = "onEntitySpawn", at = @At("HEAD"))
    private void onEntitySpawn$getPacket(EntitySpawn_S2CPacket par1, CallbackInfo ci) {
        this.cachedPacket = par1;
    }

    @ModifyVariable(method = "onEntitySpawn",
            at = @At(value = "STORE", ordinal = 0)
    )
    private Entity modLoaderSpawnEntity(Entity value) {
        if (this.cachedPacket != null && !PACKET_TYPES.contains(this.cachedPacket.type)) {
            double var2 = (double) this.cachedPacket.x / 32.0;
            double var4 = (double) this.cachedPacket.y / 32.0;
            double var6 = (double) this.cachedPacket.z / 32.0;

            for (EntityTrackerNonliving tracker : (Iterable<EntityTrackerNonliving>) ModLoader.getTrackers().values()) {
                if (this.cachedPacket.type == tracker.id) {
                    value = tracker.mod.spawnEntity(this.cachedPacket.type, this.world, var2, var4, var6);
                    this.cachedPacket = null;
                    break;
                }
            }
        }

        return value;
    }

    @Inject(method = "onDisconnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;connect(Lnet/minecraft/client/world/ClientWorld;)V"))
    private void modLoaderClientDisconnect(Disconnect_S2CPacket par1, CallbackInfo ci) {
        ModLoader.clientDisconnect();
    }

    @Inject(method = "onDisconnected", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;connect(Lnet/minecraft/client/world/ClientWorld;)V"))
    private void modLoaderClientDisconnect(String objects, Object[] par2, CallbackInfo ci) {
        ModLoader.clientDisconnect();
    }

    @Inject(method = "onChatMessage", at = @At("RETURN"))
    private void modLoaderClientChat(ChatMessage_S2CPacket par1, CallbackInfo ci) {
        ModLoader.clientChat(par1.message);
    }

    @Inject(method = "method_1204", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;wakeThreads()V"))
    private void modLoaderClientDisconnect(CallbackInfo ci) {
        ModLoader.clientDisconnect();
    }

    @Inject(method = "onOpenScreen", at = @At("HEAD"), cancellable = true)
    private void modLoaderClientOpenWindow(OpenScreen_S2CPacket par1, CallbackInfo ci) {
        if (par1.type > 6 || par1.type < 0) {
            ModLoader.clientOpenWindow(par1);
            ci.cancel();
        }
    }

    @Inject(method = "onCustomPayload", at = @At("RETURN"))
    private void modLoaderClientCustomPayload(CustomPayloadC2SPacket par1, CallbackInfo ci) {
        if (!Objects.equals(par1.channel, "MC|TPack") && !Objects.equals(par1.channel, "MC|TrList")) {
            ModLoader.clientCustomPayload(par1);
        }
    }
}
