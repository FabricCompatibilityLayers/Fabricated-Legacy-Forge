package cpw.mods.fml.common.modloader;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.Connection;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

public class ModLoaderPacketHandler implements IPacketHandler {
    private BaseModProxy mod;

    public ModLoaderPacketHandler(BaseModProxy mod) {
        this.mod = mod;
    }

    public void onPacketData(Connection manager, CustomPayloadC2SPacket packet, Player player) {
        if (player instanceof ServerPlayerEntity) {
            this.mod.serverCustomPayload(((ServerPlayerEntity)player).field_2823, packet);
        } else {
            ModLoaderHelper.sidedHelper.sendClientPacket(this.mod, packet);
        }
    }
}
