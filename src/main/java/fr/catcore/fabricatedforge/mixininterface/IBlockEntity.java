package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.network.Connection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;

public interface IBlockEntity {

    boolean canUpdate();

    void onDataPacket(Connection net, BlockEntityUpdateS2CPacket pkt);

    void onChunkUnload();
}
