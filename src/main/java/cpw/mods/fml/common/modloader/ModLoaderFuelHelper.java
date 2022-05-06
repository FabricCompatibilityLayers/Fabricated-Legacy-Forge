package cpw.mods.fml.common.modloader;

import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.item.ItemStack;

public class ModLoaderFuelHelper implements IFuelHandler {
    private BaseModProxy mod;

    public ModLoaderFuelHelper(BaseModProxy mod) {
        this.mod = mod;
    }

    public int getBurnTime(ItemStack fuel) {
        return this.mod.addFuel(fuel.id, fuel.id);
    }
}
