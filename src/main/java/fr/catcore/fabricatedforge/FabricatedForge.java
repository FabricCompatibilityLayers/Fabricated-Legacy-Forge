package fr.catcore.fabricatedforge;

import cpw.mods.fml.relauncher.FMLRelauncher;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class FabricatedForge implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) FMLRelauncher.preLaunchClientEntry();
        else FMLRelauncher.handleServerPreLaunch();
    }
}
