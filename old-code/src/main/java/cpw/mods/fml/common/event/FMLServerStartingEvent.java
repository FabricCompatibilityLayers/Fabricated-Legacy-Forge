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
import net.minecraft.command.Command;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandRegistry;

public class FMLServerStartingEvent extends FMLStateEvent {
    private MinecraftServer server;

    public FMLServerStartingEvent(Object... data) {
        super(data);
        this.server = (MinecraftServer)data[0];
    }

    public LoaderState.ModState getModState() {
        return LoaderState.ModState.AVAILABLE;
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    public void registerServerCommand(Command command) {
        CommandRegistry ch = (CommandRegistry)this.getServer().getCommandManager();
        ch.registerCommand(command);
    }
}
