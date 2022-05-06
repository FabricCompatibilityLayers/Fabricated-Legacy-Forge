package cpw.mods.fml.common;

import net.minecraft.entity.player.PlayerEntity;

public interface IPlayerTracker {
    void onPlayerLogin(PlayerEntity arg);

    void onPlayerLogout(PlayerEntity arg);

    void onPlayerChangedDimension(PlayerEntity arg);

    void onPlayerRespawn(PlayerEntity arg);
}
