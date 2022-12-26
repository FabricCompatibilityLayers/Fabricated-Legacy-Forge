package cpw.mods.fml.common.modloader;

import cpw.mods.fml.common.IDispenserHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class ModLoaderDispenseHelper implements IDispenserHandler {
    private BaseModProxy mod;

    public ModLoaderDispenseHelper(BaseModProxy mod) {
        this.mod = mod;
    }

    public int dispense(int x, int y, int z, int xVelocity, int zVelocity, World world, ItemStack item, Random random, double entX, double entY, double entZ) {
        return -1;
    }
}
