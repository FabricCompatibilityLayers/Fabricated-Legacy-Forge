package io.github.fabriccompatibilitylayers.fabricatedfml.extension.common;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet250CustomPayload;

public interface NetHandlerExtension {
    void handleVanilla250Packet(Packet250CustomPayload payload);
    EntityPlayer getPlayer();
}
