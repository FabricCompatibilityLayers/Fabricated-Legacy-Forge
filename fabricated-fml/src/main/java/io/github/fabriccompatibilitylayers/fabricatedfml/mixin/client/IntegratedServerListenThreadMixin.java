package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.src.IntegratedServerListenThread;
import net.minecraft.src.NetworkListenThread;
import net.minecraft.src.ServerListenThread;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.io.IOException;
import java.net.InetAddress;

@Mixin(IntegratedServerListenThread.class)
public class IntegratedServerListenThreadMixin {
    @WrapOperation(method = "func_71755_c", at = @At(value = "NEW", target = "Lnet/minecraft/src/ServerListenThread;"))
    private ServerListenThread fml$useCustomLocalHost(NetworkListenThread par2InetAddress, InetAddress par3, int i, Operation<ServerListenThread> original) {
        return original.call(par2InetAddress, null, i);
    }

    @WrapOperation(method = "func_71755_c", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ServerListenThread;func_71767_c()Ljava/net/InetAddress;"))
    private InetAddress fml$replaceInetAddress(ServerListenThread instance, Operation<InetAddress> original) throws IOException {
        return FMLNetworkHandler.computeLocalHost();
    }
}
