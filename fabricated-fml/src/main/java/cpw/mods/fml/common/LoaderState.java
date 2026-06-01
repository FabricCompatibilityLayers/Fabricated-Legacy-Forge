/*
 * The FML Forge Mod Loader suite. Copyright (C) 2012 cpw
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

import com.google.common.base.Throwables;

import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.event.FMLStateEvent;

/**
 * The state enum used to help track state progression for the loader
 * @author cpw
 *
 */
public enum LoaderState
{
    NOINIT("Uninitialized",null),
    LOADING("Loading",null),
    CONSTRUCTING("Constructing mods",FMLConstructionEvent.class),
    PREINITIALIZATION("Pre-initializing mods", FMLPreInitializationEvent.class),
    INITIALIZATION("Initializing mods", FMLInitializationEvent.class),
    POSTINITIALIZATION("Post-initializing mods", FMLPostInitializationEvent.class),
    AVAILABLE("Mod loading complete", FMLLoadCompleteEvent.class),
    SERVER_STARTING("Server starting", FMLServerStartingEvent.class),
    SERVER_STARTED("Server started", FMLServerStartedEvent.class),
    SERVER_STOPPING("Server stopping", FMLServerStoppingEvent.class),
    ERRORED("Mod Loading errored",null);


    private Class<? extends FMLStateEvent> eventClass;
    private String name;

    private LoaderState(String name, Class<? extends FMLStateEvent> event)
    {
        this.name = name;
        this.eventClass = event;
    }

    public LoaderState transition(boolean errored)
    {
        if (errored)
        {
            return ERRORED;
        }
        // stopping -> available
        if (this == SERVER_STOPPING)
        {
            return AVAILABLE;
        }
        return values()[ordinal() < values().length ? ordinal()+1 : ordinal()];
    }

    public boolean hasEvent()
    {
        return eventClass != null;
    }
    
    public FMLStateEvent getEvent(Object... eventData)
    {
        try
        {
            return eventClass.getConstructor(Object[].class).newInstance((Object)eventData);
        }
        catch (Exception e)
        {
            throw Throwables.propagate(e);
        }
    }
    public LoaderState requiredState()
    {
        if (this == NOINIT) return NOINIT;
        return LoaderState.values()[this.ordinal()-1];
    }
    public enum ModState
    {
        UNLOADED("Unloaded"),
        LOADED("Loaded"),
        CONSTRUCTED("Constructed"),
        PREINITIALIZED("Pre-initialized"),
        INITIALIZED("Initialized"),
        POSTINITIALIZED("Post-initialized"),
        AVAILABLE("Available"),
        DISABLED("Disabled"),
        ERRORED("Errored");
        
        private String label;

        private ModState(String label)
        {
            this.label = label;
        }

        public String toString()
        {
            return this.label;
        }
    }
}