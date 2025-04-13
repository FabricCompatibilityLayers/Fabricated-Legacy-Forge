package fr.catcore.fabricatedforge.mixin.forgefml.network;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import fr.catcore.fabricatedforge.mixininterface.IPacketListener;
import fr.catcore.fabricatedforge.mixininterface.IPendingConnection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.OutboundConnection;
import net.minecraft.network.PendingConnection;
import net.minecraft.network.class_690;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PendingConnection.class)
public abstract class PendingConnectionMixin extends PacketListener implements IPendingConnection, IPacketListener {

    @Shadow private volatile boolean field_2889;

    @Shadow private int loginTicks;

    @Shadow public abstract void disconnect(String reason);

    @Shadow public OutboundConnection connection;

    @Shadow private MinecraftServer server;

    @Shadow public String username;

    @Shadow public boolean field_2883;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void tick() {
        if (this.field_2889) {
            this.method_2194();
        }

        if (this.loginTicks++ == 6000) {
            this.disconnect("Took too long to log in");
        } else {
            this.connection.applyQueuedPackets();
        }

    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void onGameJoin(class_690 par1Packet1Login) {
        FMLNetworkHandler.handleLoginPacketOnServer((PendingConnection)(Object) this, par1Packet1Login);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_2194() {
        FMLNetworkHandler.onConnectionReceivedFromClient((PendingConnection)(Object) this, this.server, this.connection.getAddress(), this.username);
    }

    @Override
    public void completeConnection(String var1) {
        if (var1 != null) {
            this.disconnect(var1);
        } else {
            ServerPlayerEntity var2 = this.server.getPlayerManager().createPlayer(this.username);
            if (var2 != null) {
                this.server.getPlayerManager().onPlayerConnect(this.connection, var2);
            }
        }

        this.field_2883 = true;
    }

    @Override
    public void onCustomPayload(CustomPayloadC2SPacket par1Packet250CustomPayload) {
        FMLNetworkHandler.handlePacket250Packet(par1Packet250CustomPayload, this.connection, this);
    }

    @Override
    public void handleVanilla250Packet(CustomPayloadC2SPacket payload) {
    }

    @Override
    public PlayerEntity getPlayer() {
        return null;
    }

    @Override
    public void method_2189_fabric(boolean bol) {
        this.field_2889 = bol;
    }
}
