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
package cpw.mods.fml.client.modloader;

import com.google.common.collect.ObjectArrays;
import com.google.common.primitives.Booleans;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.modloader.ModLoaderModContainer;
import net.minecraft.BaseMod;
import net.minecraft.client.options.KeyBinding;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class ModLoaderKeyBindingHandler extends KeyBindingRegistry.KeyHandler {
    private ModLoaderModContainer modContainer;
    private List<KeyBinding> helper;
    private boolean[] active = new boolean[0];
    private boolean[] mlRepeats = new boolean[0];
    private boolean[] armed = new boolean[0];

    public ModLoaderKeyBindingHandler() {
        super(new KeyBinding[0], new boolean[0]);
    }

    void setModContainer(ModLoaderModContainer modContainer) {
        this.modContainer = modContainer;
    }

    public void fireKeyEvent(KeyBinding kb) {
        ((BaseMod)this.modContainer.getMod()).keyboardEvent(kb);
    }

    public void keyDown(EnumSet<TickType> type, KeyBinding kb, boolean end, boolean repeats) {
        if (end) {
            int idx = this.helper.indexOf(kb);
            if (type.contains(TickType.CLIENT)) {
                this.armed[idx] = true;
            }

            if (this.armed[idx] && type.contains(TickType.RENDER) && (!this.active[idx] || this.mlRepeats[idx])) {
                this.fireKeyEvent(kb);
                this.active[idx] = true;
                this.armed[idx] = false;
            }
        }
    }

    public void keyUp(EnumSet<TickType> type, KeyBinding kb, boolean end) {
        if (end) {
            int idx = this.helper.indexOf(kb);
            this.active[idx] = false;
        }
    }

    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT, TickType.RENDER);
    }

    public String getLabel() {
        return this.modContainer.getModId() + " KB " + this.keyBindings[0].code;
    }

    void addKeyBinding(KeyBinding binding, boolean repeats) {
        this.keyBindings = (KeyBinding[])ObjectArrays.concat(this.keyBindings, binding);
        this.repeatings = new boolean[this.keyBindings.length];
        Arrays.fill(this.repeatings, true);
        this.active = new boolean[this.keyBindings.length];
        this.armed = new boolean[this.keyBindings.length];
        this.mlRepeats = Booleans.concat(new boolean[][]{this.mlRepeats, {repeats}});
        this.keyDown = new boolean[this.keyBindings.length];
        this.helper = Arrays.asList(this.keyBindings);
    }
}
