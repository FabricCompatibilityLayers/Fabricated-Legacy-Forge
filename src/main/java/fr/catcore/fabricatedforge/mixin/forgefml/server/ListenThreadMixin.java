package fr.catcore.fabricatedforge.mixin.forgefml.server;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.network.PendingConnection;
import net.minecraft.server.ListenThread;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Mixin(ListenThread.class)
public abstract class ListenThreadMixin extends Thread {

    @Shadow @Final private List<PendingConnection> field_2744;

    @Shadow private static Logger LOGGER;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_2073() {
        List var2 = this.field_2744;
        synchronized(this.field_2744) {
            for(int var2x = 0; var2x < this.field_2744.size(); ++var2x) {
                PendingConnection var3 = (PendingConnection)this.field_2744.get(var2x);

                try {
                    var3.tick();
                } catch (Exception var7) {
                    var3.disconnect("Internal server error");
                    FMLLog.log(Level.SEVERE, var7, "Error handling login related packet - connection from %s refused", new Object[]{var3.method_2196()});
                    LOGGER.log(Level.WARNING, "Failed to handle packet for " + var3.method_2196() + ": " + var7, var7);
                }

                if (var3.field_2883) {
                    this.field_2744.remove(var2x--);
                }

                var3.connection.wakeThreads();
            }
        }
    }
}
