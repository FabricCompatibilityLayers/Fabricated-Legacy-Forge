package fr.catcore.fabricatedmodloader.mixin.modloader.common;

import fr.catcore.fabricatedmodloader.mixininterface.IServerPacketListener;
import modloader.ModLoader;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessage_S2CPacket;
import net.minecraft.server.ServerPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerPacketListener.class)
public class ServerPacketListenerMixin implements IServerPacketListener {
    @Shadow
    private ServerPlayerEntity player;

    @Override
    public ServerPlayerEntity getPlayer() {
        return this.player;
    }

    @Inject(method = "onChatMessage", at = @At("HEAD"))
    private void modLoaderServerChat(ChatMessage_S2CPacket par1, CallbackInfo ci) {
        ModLoader.serverChat((ServerPacketListener) (Object) this, par1.message);
    }

    @Inject(method = "onCustomPayload", at = @At("RETURN"))
    private void modLoaderServerCustomPayload(CustomPayloadC2SPacket par1, CallbackInfo ci) {
        if (!Objects.equals(par1.channel, "MC|BEdit")
                && !Objects.equals(par1.channel, "MC|BSign")
                && !Objects.equals(par1.channel, "MC|TrSel")
                && !Objects.equals(par1.channel, "MC|AdvCdm")
                && !Objects.equals(par1.channel, "MC|Beacon")
                && !Objects.equals(par1.channel, "MC|ItemName")) {
            ModLoader.serverCustomPayload((ServerPacketListener) (Object) this, par1);
        }
    }
}
