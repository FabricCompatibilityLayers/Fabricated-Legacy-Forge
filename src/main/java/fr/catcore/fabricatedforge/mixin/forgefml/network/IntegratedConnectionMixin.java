package fr.catcore.fabricatedforge.mixin.forgefml.network;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import fr.catcore.fabricatedforge.mixininterface.IPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.network.IntegratedConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(IntegratedConnection.class)
public abstract class IntegratedConnectionMixin implements Connection {

    @Shadow @Final private List packetQueue;

    @Shadow private PacketListener packetListener;

    @Shadow private boolean disconnected;

    @Shadow private String disconnectReason;

    @Shadow private Object[] field_2310;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void applyQueuedPackets() {
        int var1 = 2500;

        while(var1-- >= 0 && !this.packetQueue.isEmpty()) {
            Packet var2 = (Packet)this.packetQueue.remove(0);
            var2.apply(this.packetListener);
        }

        if (this.packetQueue.size() > var1) {
            System.out.println("Memory connection overburdened; after processing 2500 packets, we still have " + this.packetQueue.size() + " to go!");
        }

        if (this.disconnected && this.packetQueue.isEmpty()) {
            this.packetListener.onDisconnected(this.disconnectReason, this.field_2310);
            FMLNetworkHandler.onConnectionClosed(this, ((IPacketListener)this.packetListener).getPlayer());
        }

    }
}
