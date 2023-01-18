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
package cpw.mods.fml.client.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

public class KeyBindingRegistry {
    private static final KeyBindingRegistry INSTANCE = new KeyBindingRegistry();
    private Set<KeyBindingRegistry.KeyHandler> keyHandlers = Sets.newLinkedHashSet();

    public KeyBindingRegistry() {
    }

    public static void registerKeyBinding(KeyBindingRegistry.KeyHandler handler) {
        instance().keyHandlers.add(handler);
        if (!handler.isDummy) {
            TickRegistry.registerTickHandler(handler, Side.CLIENT);
        }

    }

    /** @deprecated */
    @Deprecated
    public static KeyBindingRegistry instance() {
        return INSTANCE;
    }

    public void uploadKeyBindingsToGame(GameOptions settings) {
        ArrayList<KeyBinding> harvestedBindings = Lists.newArrayList();
        for (KeyHandler key : keyHandlers)
        {
            for (KeyBinding kb : key.keyBindings)
            {
                harvestedBindings.add(kb);
            }
        }

        KeyBinding[] modKeyBindings = (KeyBinding[])harvestedBindings.toArray(new KeyBinding[harvestedBindings.size()]);
        KeyBinding[] allKeys = new KeyBinding[settings.keysAll.length + modKeyBindings.length];
        System.arraycopy(settings.keysAll, 0, allKeys, 0, settings.keysAll.length);
        System.arraycopy(modKeyBindings, 0, allKeys, settings.keysAll.length, modKeyBindings.length);
        settings.keysAll = allKeys;
        settings.load();
    }

    public abstract static class KeyHandler implements ITickHandler {
        protected KeyBinding[] keyBindings;
        protected boolean[] keyDown;
        protected boolean[] repeatings;
        private boolean isDummy;

        public KeyHandler(KeyBinding[] keyBindings, boolean[] repeatings) {
            assert keyBindings.length == repeatings.length : "You need to pass two arrays of identical length";

            this.keyBindings = keyBindings;
            this.repeatings = repeatings;
            this.keyDown = new boolean[keyBindings.length];
        }

        public KeyHandler(KeyBinding[] keyBindings) {
            this.keyBindings = keyBindings;
            this.isDummy = true;
        }

        public KeyBinding[] getKeyBindings() {
            return this.keyBindings;
        }

        public final void tickStart(EnumSet<TickType> type, Object... tickData) {
            this.keyTick(type, false);
        }

        public final void tickEnd(EnumSet<TickType> type, Object... tickData) {
            this.keyTick(type, true);
        }

        private void keyTick(EnumSet<TickType> type, boolean tickEnd) {
            for(int i = 0; i < this.keyBindings.length; ++i) {
                KeyBinding keyBinding = this.keyBindings[i];
                int keyCode = keyBinding.code;
                boolean state = keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode);
                if (state != this.keyDown[i] || state && this.repeatings[i]) {
                    if (state) {
                        this.keyDown(type, keyBinding, tickEnd, state != this.keyDown[i]);
                    } else {
                        this.keyUp(type, keyBinding, tickEnd);
                    }

                    if (tickEnd) {
                        this.keyDown[i] = state;
                    }
                }
            }

        }

        public abstract void keyDown(EnumSet<TickType> enumSet, KeyBinding arg, boolean bl, boolean bl2);

        public abstract void keyUp(EnumSet<TickType> enumSet, KeyBinding arg, boolean bl);

        public abstract EnumSet<TickType> ticks();
    }
}
