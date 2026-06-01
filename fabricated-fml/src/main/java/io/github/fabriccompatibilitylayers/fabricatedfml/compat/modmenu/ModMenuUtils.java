/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.compat.modmenu;

import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.util.TextureUtil;
import com.terraformersmc.modmenu.util.mod.Mod;
import com.terraformersmc.modmenu.util.mod.fabric.FabricIconHandler;
import cpw.mods.fml.common.ModContainer;
import io.github.fabriccompatibilitylayers.fabricatedfml.mixin.modmenu.FabricIconHandlerAccessor;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ModMenuUtils {
    private static final List<Path> IGNORED_PATHS = new ArrayList<>();

    public static BufferedImage createIcon(FabricIconHandler handler, FMLMod info) {
        try {
            FabricIconHandlerAccessor accessor = (FabricIconHandlerAccessor) handler;

            URL url = info.container.getClass().getResource(info.container.getMetadata().logoFile);

            if (url == null) {
                return null;
            }

            Path iconPath = Paths.get(url.toURI());

            if (IGNORED_PATHS.contains(iconPath)) {
                return null;
            }

            if (!accessor.getCache().containsKey(iconPath)) {
                if (!Files.exists(iconPath)) {
                    IGNORED_PATHS.add(iconPath);
                    return null;
                }

                InputStream inputStream = Files.newInputStream(iconPath);

                BufferedImage var8;
                try {
                    BufferedImage tex = TextureUtil.readImage(inputStream);
                    accessor.getCache().put(iconPath, tex);
                    var8 = tex;
                } catch (Throwable var10) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable var9) {
                            var10.addSuppressed(var9);
                        }
                    }

                    throw var10;
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                return var8;
            }

            return accessor.getCache().get(iconPath);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public static void addFMLMods(Collection<ModContainer> modInfos) {
        for (ModContainer modInfo : modInfos) {
            Mod mod = new FMLMod(modInfo);

            ModMenu.MODS.put(mod.getId(), mod);
        }

        ModMenu.clearModCountCache();
    }
}
