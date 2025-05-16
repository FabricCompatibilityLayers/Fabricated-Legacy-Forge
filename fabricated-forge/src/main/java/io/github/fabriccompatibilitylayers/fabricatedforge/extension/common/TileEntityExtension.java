package io.github.fabriccompatibilitylayers.fabricatedforge.extension.common;

import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet132TileEntityData;

public interface TileEntityExtension {
    boolean canUpdate();

    void onDataPacket(NetworkManager net, Packet132TileEntityData pkt);

    void onChunkUnload();

    double getRenderDistance();
}
