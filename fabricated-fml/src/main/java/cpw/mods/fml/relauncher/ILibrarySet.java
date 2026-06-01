/*
 * The FML Forge Mod Loader suite. Copyright (C) 2012 cpw
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

/**
 * Interface for certain core plugins to register libraries to
 * be loaded in by the FML class loader at launch time
 *
 * @author cpw
 *
 */
public interface ILibrarySet
{
    /**
     * Return a list of libraries available from a common location
     *
     * @return a list of libraries available from a common location
     */
    String[] getLibraries();
    /**
     * Return the string encoded sha1 hash for each library in the returned list
     *
     * @return the string encoded sha1 hash for each library in the returned list
     */
    String[] getHashes();
    /**
     * Return the root URL format string from which this library set can be obtained
     * There needs to be a single %s string substitution which is the library name
     * @return the root URL format string from which this library set can be obtained
     */
    String getRootURL();
}
