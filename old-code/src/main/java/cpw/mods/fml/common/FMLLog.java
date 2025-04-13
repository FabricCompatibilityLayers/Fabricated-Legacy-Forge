/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.common;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FMLLog {
    private static FMLRelaunchLog coreLog;

    public FMLLog() {
    }

    public static void log(Level level, String format, Object... data) {
        FMLRelaunchLog var10000 = coreLog;
        FMLRelaunchLog.log(level, format, data);
    }

    public static void log(Level level, Throwable ex, String format, Object... data) {
        FMLRelaunchLog var10000 = coreLog;
        FMLRelaunchLog.log(level, ex, format, data);
    }

    public static void severe(String format, Object... data) {
        log(Level.SEVERE, format, data);
    }

    public static void warning(String format, Object... data) {
        log(Level.WARNING, format, data);
    }

    public static void info(String format, Object... data) {
        log(Level.INFO, format, data);
    }

    public static void fine(String format, Object... data) {
        log(Level.FINE, format, data);
    }

    public static void finer(String format, Object... data) {
        log(Level.FINER, format, data);
    }

    public static void finest(String format, Object... data) {
        log(Level.FINEST, format, data);
    }

    public static Logger getLogger() {
        return coreLog.getLogger();
    }

    static {
        coreLog = FMLRelaunchLog.log;
    }
}
