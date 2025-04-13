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
package cpw.mods.fml.common.modloader;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

import java.util.EnumSet;
import java.util.Iterator;

public class BaseModTicker implements ITickHandler {
    private BaseModProxy mod;
    private EnumSet<TickType> ticks;
    private boolean clockTickTrigger;
    private boolean sendGuiTicks;

    BaseModTicker(BaseModProxy mod, boolean guiTicker) {
        this.mod = mod;
        this.ticks = EnumSet.of(TickType.WORLDLOAD);
        this.sendGuiTicks = guiTicker;
    }

    BaseModTicker(EnumSet<TickType> ticks, boolean guiTicker) {
        this.ticks = ticks;
        this.sendGuiTicks = guiTicker;
    }

    public void tickStart(EnumSet<TickType> types, Object... tickData) {
        this.tickBaseMod(types, false, tickData);
    }

    public void tickEnd(EnumSet<TickType> types, Object... tickData) {
        this.tickBaseMod(types, true, tickData);
    }

    private void tickBaseMod(EnumSet<TickType> types, boolean end, Object... tickData) {
        if (!FMLCommonHandler.instance().getSide().isClient() || !this.ticks.contains(TickType.CLIENT) && !this.ticks.contains(TickType.WORLDLOAD)) {
            this.sendTick(types, end, tickData);
        } else {
            EnumSet cTypes = EnumSet.copyOf(types);
            if (end && types.contains(TickType.CLIENT) || types.contains(TickType.WORLDLOAD)) {
                this.clockTickTrigger = true;
                cTypes.remove(TickType.CLIENT);
                cTypes.remove(TickType.WORLDLOAD);
            }

            if (end && this.clockTickTrigger && types.contains(TickType.RENDER)) {
                this.clockTickTrigger = false;
                cTypes.remove(TickType.RENDER);
                cTypes.add(TickType.CLIENT);
            }

            this.sendTick(cTypes, end, tickData);
        }

    }

    private void sendTick(EnumSet<TickType> types, boolean end, Object... tickData) {
        for (TickType type : types)
        {
            if (!ticks.contains(type))
            {
                continue;
            }

            boolean keepTicking=true;
            if (sendGuiTicks)
            {
                keepTicking = mod.doTickInGUI(type, end, tickData);
            }
            else
            {
                keepTicking = mod.doTickInGame(type, end, tickData);
            }
            if (!keepTicking) {
                ticks.remove(type);
                ticks.removeAll(type.partnerTicks());
            }
        }
    }

    public EnumSet<TickType> ticks() {
        return this.clockTickTrigger ? EnumSet.of(TickType.RENDER) : this.ticks;
    }

    public String getLabel() {
        return this.mod.getClass().getSimpleName();
    }

    public void setMod(BaseModProxy mod) {
        this.mod = mod;
    }
}
