package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.NetHandlerExtension;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.TcpConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TcpConnection.class)
public abstract class TcpConnectionMixin implements NetworkManager {
    @Shadow private volatile boolean field_74472_n;

    @Shadow private List field_74473_o;

    @Shadow private NetHandler field_74485_r;

    @Inject(method = "func_74428_b", at = @At("RETURN"))
    private void fml$onConnectionClosed(CallbackInfo ci) {
        if (this.field_74472_n && this.field_74473_o.isEmpty()) {
            FMLNetworkHandler.onConnectionClosed(this, ((NetHandlerExtension) this.field_74485_r).getPlayer());
        }
    }
}
