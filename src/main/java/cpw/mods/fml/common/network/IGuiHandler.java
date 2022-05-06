package cpw.mods.fml.common.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IGuiHandler {
    Object getServerGuiElement(int i, PlayerEntity arg, World arg2, int j, int k, int l);

    Object getClientGuiElement(int i, PlayerEntity arg, World arg2, int j, int k, int l);
}
