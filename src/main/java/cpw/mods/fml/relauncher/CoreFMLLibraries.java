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

public class CoreFMLLibraries implements ILibrarySet {
    private static String[] libraries = new String[]{"argo-2.25.jar", "guava-12.0.1.jar", "asm-all-4.0.jar"};
    private static String[] checksums = new String[]{
            "bb672829fde76cb163004752b86b0484bd0a7f4b", "b8e78b9af7bf45900e14c6f958486b6ca682195f", "98308890597acb64047f7e896638e0d98753ae82"
    };

    public CoreFMLLibraries() {
    }

    public String[] getLibraries() {
        return new String[0];
    }

    public String[] getHashes() {
        return new String[0];
    }

    public String getRootURL() {
        return "http://files.minecraftforge.net/fmllibs/%s";
    }
}
