package cpw.mods.fml.common;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public interface IDispenseHandler {
    /** @deprecated */
    @Deprecated
    int dispense(double d, double e, double f, int i, int j, World arg, ItemStack arg2, Random random, double g, double h, double k);
}
