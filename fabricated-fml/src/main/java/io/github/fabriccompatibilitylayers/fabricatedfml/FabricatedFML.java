/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml;

import cpw.mods.fml.relauncher.FMLRelauncher;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class FabricatedFML implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) FMLRelauncher.preLaunchClientEntry();
        else FMLRelauncher.handleServerPreLaunch();
    }
}
