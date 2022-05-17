package fr.catcore.fabricatedforge.mixin.forgefml.client;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import fr.catcore.fabricatedforge.mixininterface.Iclass_604;
import net.minecraft.client.class_604;
import net.minecraft.client.util.NetworkUtils;
import net.minecraft.server.ListenThread;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.PacketListenerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.net.InetAddress;

@Mixin(class_604.class)
public class class_604Mixin extends PacketListenerManager implements Iclass_604 {

    @Shadow private ListenThread field_2216;

    public class_604Mixin(MinecraftServer server) {
        super(server);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public String method_1642() throws IOException {
        if (this.field_2216 == null) {
            int var1 = -1;

            try {
                var1 = NetworkUtils.getFreePort();
            } catch (Exception var4) {
            }

            if (var1 <= 0) {
                var1 = 25564;
            }

            try {
                this.field_2216 = new ListenThread((class_604)(Object) this, null, var1);
                this.field_2216.start();
            } catch (Exception var3) {
                throw var3;
            }
        }

        return FMLNetworkHandler.computeLocalHost().getHostAddress() + ":" + this.field_2216.getPort();
    }

    @Override
    public MinecraftServer getServer() {
        return super.getServer();
    }
}
