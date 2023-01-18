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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModContainer;

public class FMLInterModComms {
    private static ArrayListMultimap<String, IMCMessage> modMessages = ArrayListMultimap.create();

    public FMLInterModComms() {
    }

    public static boolean sendMessage(String modId, String key, String value) {
        if (Loader.instance().activeModContainer() == null) {
            return false;
        } else {
            modMessages.put(modId, new FMLInterModComms.IMCMessage(Loader.instance().activeModContainer().getModId(), key, value));
            return Loader.isModLoaded(modId) && !Loader.instance().hasReachedState(LoaderState.POSTINITIALIZATION);
        }
    }

    public static class IMCEvent extends FMLEvent {
        private ImmutableList<IMCMessage> currentList;

        public IMCEvent() {
        }

        public void applyModContainer(ModContainer activeContainer) {
            this.currentList = ImmutableList.copyOf(FMLInterModComms.modMessages.get(activeContainer.getModId()));
        }

        public ImmutableList<FMLInterModComms.IMCMessage> getMessages() {
            return this.currentList;
        }
    }

    public static final class IMCMessage {
        public final String sender;
        public final String key;
        public final String value;

        private IMCMessage(String sender, String key, String value) {
            this.key = key;
            this.value = value;
            this.sender = sender;
        }

        public String toString() {
            return this.sender;
        }
    }
}
