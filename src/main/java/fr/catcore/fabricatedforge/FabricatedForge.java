package fr.catcore.fabricatedforge;

import fr.catcore.fabricatedforge.utils.FakeModManager;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class FabricatedForge implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        FakeModManager.init();
    }
}
