package cpw.mods.fml.common;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface IPickupNotifier {
    void notifyPickup(ItemEntity arg, PlayerEntity arg2);
}
