/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.extension.common;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet250CustomPayload;

public interface NetHandlerExtension {
    void handleVanilla250Packet(Packet250CustomPayload payload);
    EntityPlayer getPlayer();
}
