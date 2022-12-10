package fr.catcore.fabricatedforge.mixin.forgefml.network;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import fr.catcore.fabricatedforge.mixininterface.IPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.network.OutboundConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(OutboundConnection.class)
public abstract class OutboundConnectionMixin implements Connection {
    @Shadow private int queuedBytes;

    @Shadow private List field_2328;

    @Shadow private int ticksWithoutPackets;

    @Shadow private PacketListener packetListener;

    @Shadow private volatile boolean ignoreExceptions;

    @Shadow private String disconnectReason;

    @Shadow private Object[] args;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void applyQueuedPackets() {
        if (this.queuedBytes > 2097152) {
            this.disconnect("disconnect.overflow");
        }

        if (this.field_2328.isEmpty()) {
            if (this.ticksWithoutPackets++ == 1200) {
                this.disconnect("disconnect.timeout");
            }
        } else {
            this.ticksWithoutPackets = 0;
        }

        int var1 = 1000;

        while(!this.field_2328.isEmpty() && var1-- >= 0) {
            Packet var2 = (Packet)this.field_2328.remove(0);
            var2.apply(this.packetListener);
        }

        this.wakeThreads();
        if (this.ignoreExceptions && this.field_2328.isEmpty()) {
            this.packetListener.onDisconnected(this.disconnectReason, this.args);
            FMLNetworkHandler.onConnectionClosed(this, this.packetListener.getPlayer());
        }
    }
}
