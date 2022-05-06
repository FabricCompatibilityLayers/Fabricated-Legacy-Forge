package cpw.mods.fml.common.modloader;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.World;

public class ModLoaderGuiHelper implements IGuiHandler {
    private BaseModProxy mod;
    private int id;
    private ScreenHandler container;

    ModLoaderGuiHelper(BaseModProxy mod, int id) {
        this.mod = mod;
        this.id = id;
    }

    public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
        return this.container;
    }

    public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
        return ModLoaderHelper.getClientSideGui(this.mod, player, ID, x, y, z);
    }

    public void injectContainer(ScreenHandler container) {
        this.container = container;
    }

    public Object getMod() {
        return this.mod;
    }
}
