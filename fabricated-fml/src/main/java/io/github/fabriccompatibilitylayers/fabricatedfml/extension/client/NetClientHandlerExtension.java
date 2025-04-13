package io.github.fabriccompatibilitylayers.fabricatedfml.extension.client;

import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.NetHandlerExtension;
import net.minecraft.src.Packet131MapData;
import net.minecraft.src.Packet250CustomPayload;

public interface NetClientHandlerExtension extends NetHandlerExtension {
    void fmlPacket131Callback(Packet131MapData p_72494_1_);
    void handleVanilla250Packet(Packet250CustomPayload p_72501_1_);
}
