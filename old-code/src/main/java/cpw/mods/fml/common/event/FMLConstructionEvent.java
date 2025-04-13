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
package cpw.mods.fml.common.event;

import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.common.discovery.ASMDataTable;

public class FMLConstructionEvent extends FMLStateEvent {
    private ModClassLoader modClassLoader;
    private ASMDataTable asmData;

    public FMLConstructionEvent(Object... eventData) {
        super(new Object[0]);
        this.modClassLoader = (ModClassLoader)eventData[0];
        this.asmData = (ASMDataTable)eventData[1];
    }

    public ModClassLoader getModClassLoader() {
        return this.modClassLoader;
    }

    public LoaderState.ModState getModState() {
        return LoaderState.ModState.CONSTRUCTED;
    }

    public ASMDataTable getASMHarvestedData() {
        return this.asmData;
    }
}