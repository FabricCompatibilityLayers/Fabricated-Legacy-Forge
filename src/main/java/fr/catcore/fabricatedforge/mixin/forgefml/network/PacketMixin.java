package fr.catcore.fabricatedforge.mixin.forgefml.network;

import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Packet.class)
public class PacketMixin {

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;addPacketType(IZZLjava/lang/Class;)V"), index = 2)
    private static boolean mapPacket(int i, boolean client, boolean server, Class<?> type) {
        return server || (type == MapUpdateS2CPacket.class && client && i == 131);
    }
}
