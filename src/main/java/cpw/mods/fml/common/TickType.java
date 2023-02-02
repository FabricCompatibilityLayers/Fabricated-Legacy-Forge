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

import java.util.EnumSet;

public enum TickType {
    WORLD,
    RENDER,
    @Deprecated
    GUI,
    @Deprecated
    CLIENTGUI,
    WORLDLOAD,
    CLIENT,
    PLAYER,
    SERVER;

    private TickType() {
    }

    public EnumSet<TickType> partnerTicks() {
        if (this == CLIENT) {
            return EnumSet.of(RENDER);
        } else if (this == RENDER) {
            return EnumSet.of(CLIENT);
        } else if (this == GUI) {
            return EnumSet.of(CLIENTGUI);
        } else {
            return this == CLIENTGUI ? EnumSet.of(GUI) : EnumSet.noneOf(TickType.class);
        }
    }
}
