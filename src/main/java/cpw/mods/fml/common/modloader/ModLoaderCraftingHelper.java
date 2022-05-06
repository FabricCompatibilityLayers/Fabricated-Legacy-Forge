package cpw.mods.fml.common.modloader;

import cpw.mods.fml.common.ICraftingHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class ModLoaderCraftingHelper implements ICraftingHandler {
    private BaseModProxy mod;

    public ModLoaderCraftingHelper(BaseModProxy mod) {
        this.mod = mod;
    }

    public void onCrafting(PlayerEntity player, ItemStack item, Inventory craftMatrix) {
        this.mod.takenFromCrafting(player, item, craftMatrix);
    }

    public void onSmelting(PlayerEntity player, ItemStack item) {
        this.mod.takenFromFurnace(player, item);
    }
}
