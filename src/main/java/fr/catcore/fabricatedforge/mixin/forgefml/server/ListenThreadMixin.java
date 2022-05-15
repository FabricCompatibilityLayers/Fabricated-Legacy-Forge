package fr.catcore.fabricatedforge.mixin.forgefml.server;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.network.PendingConnection;
import net.minecraft.server.ListenThread;
import net.minecraft.server.network.PacketListenerManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Mixin(ListenThread.class)
public abstract class ListenThreadMixin extends Thread {

    @Shadow @Final private List field_2744;

    @Shadow private static Logger LOGGER;

    @Shadow private PacketListenerManager manager;

    @Shadow @Final private ServerSocket serverSocket;

    @Shadow @Final private HashMap field_2745;

    @Shadow
    private static boolean isLoopback(InetAddress address) {
        return false;
    }

    @Shadow private int field_2746;

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void method_2073() {
        List var1 = this.field_2744;
        synchronized(this.field_2744) {
            for(int var2 = 0; var2 < this.field_2744.size(); ++var2) {
                PendingConnection var3 = (PendingConnection)this.field_2744.get(var2);

                try {
                    var3.tick();
                } catch (Exception var7) {
                    var3.disconnect("Internal server error");
                    FMLLog.log(Level.SEVERE, var7, "Error handling login related packet - connection from %s refused", var3.username);
                    LOGGER.log(Level.WARNING, "Failed to handle packet: " + var7, var7);
                }

                if (var3.field_2883) {
                    this.field_2744.remove(var2--);
                }

                var3.connection.wakeThreads();
            }

        }
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void run() {
        while(this.manager.running) {
            try {
                Socket var1 = this.serverSocket.accept();
                InetAddress var2 = var1.getInetAddress();
                long var3 = System.currentTimeMillis();
                HashMap var5 = this.field_2745;
                synchronized(this.field_2745) {
                    if (this.field_2745.containsKey(var2) && !isLoopback(var2) && var3 - (Long)this.field_2745.get(var2) < 4000L) {
                        this.field_2745.put(var2, var3);
                        var1.close();
                        continue;
                    }

                    this.field_2745.put(var2, var3);
                }

                PendingConnection var9 = new PendingConnection(this.manager.getServer(), var1, "Connection #" + this.field_2746++);
                this.method_2074(var9);
            } catch (IOException var9) {
                var9.printStackTrace();
            }
        }

        System.out.println("Closing listening thread");
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    private void method_2074(PendingConnection par1NetLoginHandler) {
        if (par1NetLoginHandler == null) {
            throw new IllegalArgumentException("Got null pendingconnection!");
        } else {
            List var2 = this.field_2744;
            synchronized(this.field_2744) {
                this.field_2744.add(par1NetLoginHandler);
            }
        }
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void method_2075(InetAddress par1InetAddress) {
        if (par1InetAddress != null) {
            HashMap var2 = this.field_2745;
            synchronized(this.field_2745) {
                this.field_2745.remove(par1InetAddress);
            }
        }

    }
}
