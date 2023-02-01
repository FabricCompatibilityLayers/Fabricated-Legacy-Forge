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

import com.google.common.collect.Sets;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.World;

import java.util.Set;

public class ModLoaderGuiHelper implements IGuiHandler {
    private BaseModProxy mod;
    private Set<Integer> ids;
    private ScreenHandler container;
    private int currentID;

    ModLoaderGuiHelper(BaseModProxy mod) {
        this.mod = mod;
        this.ids = Sets.newHashSet();
    }

    public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
        return this.container;
    }

    public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
        return ModLoaderHelper.getClientSideGui(this.mod, player, ID, x, y, z);
    }

    public void injectContainerAndID(ScreenHandler container, int ID) {
        this.container = container;
        this.currentID = ID;
    }

    public Object getMod() {
        return this.mod;
    }

    public void associateId(int additionalID) {
        this.ids.add(additionalID);
    }
}
