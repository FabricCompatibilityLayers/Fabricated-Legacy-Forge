package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.NetHandlerExtension;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet250CustomPayload;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NetHandler.class)
public abstract class NetHandlerMixin implements NetHandlerExtension {
    @Override
    public abstract void handleVanilla250Packet(Packet250CustomPayload payload);

    @Override
    public abstract EntityPlayer getPlayer();
}
