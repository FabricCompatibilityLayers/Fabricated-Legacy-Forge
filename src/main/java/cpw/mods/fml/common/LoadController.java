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

import com.google.common.base.Joiner;
import com.google.common.collect.*;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.event.FMLLoadEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLStateEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class LoadController {
    private Loader loader;
    private EventBus masterChannel;
    private ImmutableMap<String, EventBus> eventChannels;
    private LoaderState state;
    private Multimap<String, LoaderState.ModState> modStates = ArrayListMultimap.create();
    private Multimap<String, Throwable> errors = ArrayListMultimap.create();
    private Map<String, ModContainer> modList;
    private List<ModContainer> activeModList = Lists.newArrayList();
    private ModContainer activeContainer;
    private BiMap<ModContainer, Object> modObjectList;

    public LoadController(Loader loader) {
        this.loader = loader;
        this.masterChannel = new EventBus("FMLMainChannel");
        this.masterChannel.register(this);
        this.state = LoaderState.NOINIT;
    }

    @Subscribe
    public void buildModList(FMLLoadEvent event) {
        this.modList = loader.getIndexedModList();
        ImmutableMap.Builder<String, EventBus> eventBus = ImmutableMap.builder();

        for (ModContainer mod : loader.getModList())
        {
            EventBus bus = new EventBus(mod.getModId());
            boolean isActive = mod.registerBus(bus, this);
            if (isActive)
            {
                FMLLog.fine("Activating mod %s", mod.getModId());
                activeModList.add(mod);
                modStates.put(mod.getModId(), LoaderState.ModState.UNLOADED);
                eventBus.put(mod.getModId(), bus);
            }
            else
            {
                FMLLog.warning("Mod %s has been disabled through configuration", mod.getModId());
                modStates.put(mod.getModId(), LoaderState.ModState.UNLOADED);
                modStates.put(mod.getModId(), LoaderState.ModState.DISABLED);
            }
        }

        eventChannels = eventBus.build();
    }

    public void distributeStateMessage(LoaderState state, Object... eventData) {
        if (state.hasEvent()) {
            this.masterChannel.post(state.getEvent(eventData));
        }

    }

    public void transition(LoaderState desiredState) {
        LoaderState oldState = state;
        state = state.transition(!errors.isEmpty());
        if (state != desiredState)
        {
            Throwable toThrow = null;
            FMLLog.severe("Fatal errors were detected during the transition from %s to %s. Loading cannot continue", oldState, desiredState);
            StringBuilder sb = new StringBuilder();
            printModStates(sb);
            FMLLog.severe(sb.toString());
            FMLLog.severe("The following problems were captured during this phase");
            for (Map.Entry<String, Throwable> error : errors.entries())
            {
                FMLLog.log(Level.SEVERE, error.getValue(), "Caught exception from %s", error.getKey());
                if (error.getValue() instanceof IFMLHandledException)
                {
                    toThrow = error.getValue();
                }
                else if (toThrow == null)
                {
                    toThrow = error.getValue();
                }
            }
            if (toThrow != null && toThrow instanceof RuntimeException)
            {
                throw (RuntimeException)toThrow;
            }
            else
            {
                throw new LoaderException(toThrow);
            }
        }
    }

    public ModContainer activeContainer() {
        return this.activeContainer;
    }

    @Subscribe
    public void propogateStateMessage(FMLStateEvent stateEvent) {
        if (stateEvent instanceof FMLPreInitializationEvent)
        {
            modObjectList = buildModObjectList();
        }
        for (ModContainer mc : activeModList)
        {
            activeContainer = mc;
            String modId = mc.getModId();
            stateEvent.applyModContainer(activeContainer());
            FMLLog.finer("Posting state event %s to mod %s", stateEvent.getEventType(), modId);
            eventChannels.get(modId).post(stateEvent);
            FMLLog.finer("State event %s delivered to mod %s", stateEvent.getEventType(), modId);
            activeContainer = null;
            if (!errors.containsKey(modId))
            {
                modStates.put(modId, stateEvent.getModState());
            }
            else
            {
                modStates.put(modId, LoaderState.ModState.ERRORED);
            }
        }
    }

    public ImmutableBiMap<ModContainer, Object> buildModObjectList() {
        ImmutableBiMap.Builder<ModContainer, Object> builder = ImmutableBiMap.<ModContainer, Object>builder();
        for (ModContainer mc : activeModList)
        {
            if (!mc.isImmutable() && mc.getMod()!=null)
            {
                builder.put(mc, mc.getMod());
            }
            if (mc.getMod()==null && !mc.isImmutable() && state!=LoaderState.CONSTRUCTING)
            {
                FMLLog.severe("There is a severe problem with %s - it appears not to have constructed correctly", mc.getModId());
                if (state != LoaderState.CONSTRUCTING)
                {
                    this.errorOccurred(mc, new RuntimeException());
                }
            }
        }
        return builder.build();
    }

    public void errorOccurred(ModContainer modContainer, Throwable exception) {
        if (exception instanceof InvocationTargetException) {
            this.errors.put(modContainer.getModId(), ((InvocationTargetException)exception).getCause());
        } else {
            this.errors.put(modContainer.getModId(), exception);
        }

    }

    public void printModStates(StringBuilder ret) {
        for (ModContainer mc : loader.getModList())
        {
            ret.append("\n\t").append(mc.getModId()).append(" [").append(mc.getName()).append("] (").append(mc.getSource().getName()).append(") ");
            Joiner.on("->"). appendTo(ret, modStates.get(mc.getModId()));
        }
    }

    public List<ModContainer> getActiveModList() {
        return this.activeModList;
    }

    public LoaderState.ModState getModState(ModContainer selectedMod) {
        return (LoaderState.ModState)Iterables.getLast(this.modStates.get(selectedMod.getModId()), LoaderState.ModState.AVAILABLE);
    }

    public void distributeStateMessage(Class<?> customEvent) {
        try {
            this.masterChannel.post(customEvent.newInstance());
        } catch (Exception var3) {
            FMLLog.log(Level.SEVERE, var3, "An unexpected exception", new Object[0]);
            throw new LoaderException(var3);
        }
    }

    public BiMap<ModContainer, Object> getModObjectList() {
        if (this.modObjectList == null) {
            FMLLog.severe("Detected an attempt by a mod %s to perform game activity during mod construction. This is a serious programming error.", new Object[]{this.activeContainer});
            return this.buildModObjectList();
        } else {
            return ImmutableBiMap.copyOf(this.modObjectList);
        }
    }

    public boolean isInState(LoaderState state) {
        return this.state == state;
    }
}
