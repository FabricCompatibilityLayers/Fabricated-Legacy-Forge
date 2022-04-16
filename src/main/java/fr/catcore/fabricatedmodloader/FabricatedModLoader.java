package fr.catcore.fabricatedmodloader;

import fr.catcore.fabricatedmodloader.utils.FakeModManager;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class FabricatedModLoader implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        FakeModManager.init();
    }
}
