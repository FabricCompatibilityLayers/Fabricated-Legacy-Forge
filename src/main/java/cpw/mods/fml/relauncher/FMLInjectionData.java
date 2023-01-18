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
package cpw.mods.fml.relauncher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

public class FMLInjectionData {
    static File minecraftHome;
    static String major;
    static String minor;
    static String rev;
    static String build;
    static String mccversion;
    static String mcpversion;
    public static List<String> containers = new ArrayList();

    public FMLInjectionData() {
    }

    static void build(File mcHome, RelaunchClassLoader classLoader) {
        minecraftHome = mcHome;
        InputStream stream = classLoader.getResourceAsStream("fmlversion.properties");
        Properties properties = new Properties();
        if (stream != null) {
            try {
                properties.load(stream);
            } catch (IOException var5) {
                FMLRelaunchLog.log(Level.SEVERE, var5, "Could not get FML version information - corrupted installation detected!", new Object[0]);
            }
        }

        major = properties.getProperty("fmlbuild.major.number", "missing");
        minor = properties.getProperty("fmlbuild.minor.number", "missing");
        rev = properties.getProperty("fmlbuild.revision.number", "missing");
        build = properties.getProperty("fmlbuild.build.number", "missing");
        mccversion = properties.getProperty("fmlbuild.mcversion", "missing");
        mcpversion = properties.getProperty("fmlbuild.mcpversion", "missing");
    }

    public static Object[] data() {
        return new Object[]{major, minor, rev, build, mccversion, mcpversion, minecraftHome, containers};
    }
}
