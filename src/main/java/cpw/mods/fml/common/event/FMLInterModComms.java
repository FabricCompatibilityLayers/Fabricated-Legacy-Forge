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
import cpw.mods.fml.common.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class FMLInterModComms {
    private static final ImmutableList<FMLInterModComms.IMCMessage> emptyIMCList = ImmutableList.of();
    private static ArrayListMultimap<String, FMLInterModComms.IMCMessage> modMessages = ArrayListMultimap.create();

    public FMLInterModComms() {
    }

    public static boolean sendMessage(String modId, String key, NbtCompound value) {
        return enqueueStartupMessage(modId, new FMLInterModComms.IMCMessage(key, value));
    }

    public static boolean sendMessage(String modId, String key, ItemStack value) {
        return enqueueStartupMessage(modId, new FMLInterModComms.IMCMessage(key, value));
    }

    public static boolean sendMessage(String modId, String key, String value) {
        return enqueueStartupMessage(modId, new FMLInterModComms.IMCMessage(key, value));
    }

    public static void sendRuntimeMessage(Object sourceMod, String modId, String key, NbtCompound value) {
        enqueueMessage(sourceMod, modId, new FMLInterModComms.IMCMessage(key, value));
    }

    public static void sendRuntimeMessage(Object sourceMod, String modId, String key, ItemStack value) {
        enqueueMessage(sourceMod, modId, new FMLInterModComms.IMCMessage(key, value));
    }

    public static void sendRuntimeMessage(Object sourceMod, String modId, String key, String value) {
        enqueueMessage(sourceMod, modId, new FMLInterModComms.IMCMessage(key, value));
    }

    private static boolean enqueueStartupMessage(String modTarget, FMLInterModComms.IMCMessage message) {
        if (Loader.instance().activeModContainer() == null) {
            return false;
        } else {
            enqueueMessage(Loader.instance().activeModContainer(), modTarget, message);
            return Loader.isModLoaded(modTarget) && !Loader.instance().hasReachedState(LoaderState.POSTINITIALIZATION);
        }
    }

    private static void enqueueMessage(Object sourceMod, String modTarget, FMLInterModComms.IMCMessage message) {
        ModContainer mc;
        if (sourceMod instanceof ModContainer) {
            mc = (ModContainer)sourceMod;
        } else {
            mc = FMLCommonHandler.instance().findContainerFor(sourceMod);
        }

        if (mc != null && Loader.isModLoaded(modTarget)) {
            message.setSender(mc);
            modMessages.put(modTarget, message);
        }
    }

    public static ImmutableList<FMLInterModComms.IMCMessage> fetchRuntimeMessages(Object forMod) {
        ModContainer mc = FMLCommonHandler.instance().findContainerFor(forMod);
        return mc != null ? ImmutableList.copyOf(modMessages.removeAll(mc.getModId())) : emptyIMCList;
    }

    public static class IMCEvent extends FMLEvent {
        private ModContainer activeContainer;
        private ImmutableList<FMLInterModComms.IMCMessage> currentList;

        public IMCEvent() {
        }

        public void applyModContainer(ModContainer activeContainer) {
            this.activeContainer = activeContainer;
            FMLLog.finest(
                    "Attempting to deliver %d IMC messages to mod %s",
                    new Object[]{FMLInterModComms.modMessages.get(activeContainer.getModId()).size(), activeContainer.getModId()}
            );
        }

        public ImmutableList<FMLInterModComms.IMCMessage> getMessages() {
            if (this.currentList == null) {
                this.currentList = ImmutableList.copyOf(FMLInterModComms.modMessages.removeAll(this.activeContainer.getModId()));
            }

            return this.currentList;
        }
    }

    public static final class IMCMessage {
        private String sender;
        public final String key;
        private Object value;

        private IMCMessage(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return this.sender;
        }

        public String getSender() {
            return this.sender;
        }

        void setSender(ModContainer activeModContainer) {
            this.sender = activeModContainer.getModId();
        }

        public String getStringValue() {
            return (String)this.value;
        }

        public NbtCompound getNBTValue() {
            return (NbtCompound)this.value;
        }

        public ItemStack getItemStackValue() {
            return (ItemStack)this.value;
        }

        public Class<?> getMessageType() {
            return this.value.getClass();
        }

        public boolean isStringMessage() {
            return String.class.isAssignableFrom(this.getMessageType());
        }

        public boolean isItemStackMessage() {
            return ItemStack.class.isAssignableFrom(this.getMessageType());
        }

        public boolean isNBTMessage() {
            return NbtCompound.class.isAssignableFrom(this.getMessageType());
        }
    }
}
