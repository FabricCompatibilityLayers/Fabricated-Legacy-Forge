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

import java.io.DataOutputStream;
import java.util.Iterator;
import java.util.List;

@Mixin(OutboundConnection.class)
public abstract class OutboundConnectionMixin implements Connection {

    @Shadow private boolean disconnecting;

    @Shadow private Object lock;

    @Shadow private int queuedBytes;

    @Shadow private List worldSendQueue;

    @Shadow private List sendQueue;

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
    public void send(Packet par1Packet) {
        if (!this.disconnecting) {
            Object var2 = this.lock;
            synchronized(this.lock) {
                this.queuedBytes += par1Packet.getSize() + 1;
                if (par1Packet.affectsWorld) {
                    this.worldSendQueue.add(par1Packet);
                } else {
                    this.sendQueue.add(par1Packet);
                }
            }
        }

    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    private Packet pollPacket(boolean par1) {
        Packet var2 = null;
        List var3 = par1 ? this.worldSendQueue : this.sendQueue;
        Object var4 = this.lock;
        synchronized(this.lock) {
            while(!var3.isEmpty() && var2 == null) {
                var2 = (Packet)var3.remove(0);
                this.queuedBytes -= var2.getSize() + 1;
                if (this.isRedundant(var2, par1)) {
                    var2 = null;
                }
            }

            return var2;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    private boolean isRedundant(Packet par1Packet, boolean par2) {
        if (par1Packet.targetsEntity()) {
            List var3 = par2 ? this.worldSendQueue : this.sendQueue;
            for (Object o : var3) {
                Packet var5 = (Packet) o;
                if (var5.getPacketId() == par1Packet.getPacketId()) {
                    return par1Packet.targetsSameEntity(var5);
                }
            }

        }
        return false;
    }

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
            FMLNetworkHandler.onConnectionClosed(this, ((IPacketListener)this.packetListener).getPlayer());
        }

    }

//    static boolean isOpen(OutboundConnection par0TcpConnection) {
//        return par0TcpConnection.open;
//    }
//
//    static boolean isDisconnecting(OutboundConnection par0TcpConnection) {
//        return par0TcpConnection.disconnecting;
//    }
//
//    static boolean readPacket(OutboundConnection par0TcpConnection) {
//        return par0TcpConnection.readPacket();
//    }
//
//    static boolean writePacket(OutboundConnection par0TcpConnection) {
//        return par0TcpConnection.writePacket();
//    }
//
//    static DataOutputStream getOutputStream(OutboundConnection par0TcpConnection) {
//        return par0TcpConnection.output;
//    }
//
//    static boolean isIngoringExceptions(OutboundConnection par0TcpConnection) {
//        return par0TcpConnection.ignoreExceptions;
//    }
//
//    static void handleException(OutboundConnection par0TcpConnection, Exception par1Exception) {
//        par0TcpConnection.exceptionCaught(par1Exception);
//    }
//
//    static Thread getReadThread(OutboundConnection par0TcpConnection) {
//        return par0TcpConnection.readThread;
//    }
//
//    static Thread getWriteThread(OutboundConnection par0TcpConnection) {
//        return par0TcpConnection.writeThread;
//    }
}
