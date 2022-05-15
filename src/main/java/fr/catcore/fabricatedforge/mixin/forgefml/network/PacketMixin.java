package fr.catcore.fabricatedforge.mixin.forgefml.network;

import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Packet.class)
public class PacketMixin {

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;addPacketType(IZZLjava/lang/Class;)V", ordinal = 61), index = 2)
    private static boolean mapPacket(boolean server) {
        return true;
    }
}
