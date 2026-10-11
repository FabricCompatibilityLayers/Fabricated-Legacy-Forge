/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric;

import cpw.mods.fml.common.Loader;

import java.util.Locale;

public class IdUtils {
    public static boolean isForgeModLoading() {
        return Loader.instance().activeModContainer() != null;
    }

    public static String getModId() {
        return Loader.instance().activeModContainer().getModId().toLowerCase(Locale.ENGLISH).replace("|", "__");
    }

    /**
     * @author Space Walker in OSL 0.22
     */
    public static String pascalCaseToSnakeCase(String s) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < s.length(); ++i) {
            char chr = s.charAt(i);
            if (Character.isUpperCase(chr)) {
                chr = Character.toLowerCase(chr);
                if (i != 0 && (!Character.isUpperCase(s.charAt(i - 1)) || i != s.length() - 1 && !Character.isUpperCase(s.charAt(i + 1)))) {
                    sb.append('_');
                }
            }

            sb.append(chr);
        }

        return sb.toString();
    }
}
