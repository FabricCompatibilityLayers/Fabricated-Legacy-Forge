package fr.catcore.fabricatedforge.mixin.forgefml.network.listener;

import fr.catcore.fabricatedforge.mixininterface.IPacketListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PacketListener.class)
public abstract class PacketListenerMixin implements IPacketListener {

    @Override
    public abstract void handleVanilla250Packet(CustomPayloadC2SPacket arg);

    @Override
    public abstract PlayerEntity getPlayer();
}
