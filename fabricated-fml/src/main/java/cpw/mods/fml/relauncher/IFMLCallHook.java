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

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * This call hook allows for code to execute at the very early stages of
 * minecraft initialization. FML uses it to validate that there is a
 * safe environment for further loading of FML.
 *
 * @author cpw
 *
 */
public interface IFMLCallHook extends Callable<Void>
{
    /**
     * Injected with data from the FML environment:
     * "classLoader" : The FML Class Loader
     * @param data
     */
    void injectData(Map<String,Object> data);
}
