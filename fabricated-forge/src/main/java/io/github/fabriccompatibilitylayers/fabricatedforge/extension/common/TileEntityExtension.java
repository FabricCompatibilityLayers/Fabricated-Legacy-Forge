/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet132TileEntityData;

public interface TileEntityExtension {
    boolean canUpdate();

    void onDataPacket(INetworkManager net, Packet132TileEntityData pkt);

    void onChunkUnload();
}
