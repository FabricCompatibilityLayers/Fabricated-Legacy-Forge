package fr.catcore.fabricatedforge.compat;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class Dummy implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        System.out.println("Gotcha Mixin!");
    }
}
