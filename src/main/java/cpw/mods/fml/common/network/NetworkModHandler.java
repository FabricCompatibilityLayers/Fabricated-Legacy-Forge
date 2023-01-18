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
package cpw.mods.fml.common.network;

import com.google.common.base.Strings;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.InvalidVersionSpecificationException;
import cpw.mods.fml.common.versioning.VersionRange;
import net.minecraft.item.Item;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

public class NetworkModHandler {
    private static Object connectionHandlerDefaultValue;
    private static Object packetHandlerDefaultValue;
    private static Object clientHandlerDefaultValue;
    private static Object serverHandlerDefaultValue;
    private static Object tinyPacketHandlerDefaultValue;
    private static int assignedIds = 1;
    private int localId;
    private int networkId;
    private ModContainer container;
    private NetworkMod mod;
    private Method checkHandler;
    private VersionRange acceptableRange;
    private ITinyPacketHandler tinyPacketHandler;

    public NetworkModHandler(ModContainer container, NetworkMod modAnnotation) {
        this.container = container;
        this.mod = modAnnotation;
        this.localId = assignedIds++;
        this.networkId = this.localId;
        if (Item.MAP.id == assignedIds) {
            ++assignedIds;
        }

    }

    public NetworkModHandler(ModContainer container, Class<?> networkModClass, ASMDataTable table) {
        this(container, (NetworkMod)networkModClass.getAnnotation(NetworkMod.class));

        if (this.mod == null)
        {
            return;
        }

        Set<ASMDataTable.ASMData> versionCheckHandlers = table.getAnnotationsFor(container).get(NetworkMod.VersionCheckHandler.class.getName());
        String versionCheckHandlerMethod = null;
        for (ASMDataTable.ASMData vch : versionCheckHandlers)
        {
            if (vch.getClassName().equals(networkModClass.getName()))
            {
                versionCheckHandlerMethod = vch.getObjectName();
                break;
            }
        }
        if (versionCheckHandlerMethod != null)
        {
            try
            {
                Method checkHandlerMethod = networkModClass.getDeclaredMethod(versionCheckHandlerMethod, String.class);
                if (checkHandlerMethod.isAnnotationPresent(NetworkMod.VersionCheckHandler.class))
                {
                    this.checkHandler = checkHandlerMethod;
                }
            }
            catch (Exception e)
            {
                FMLLog.log(Level.WARNING, e, "The declared version check handler method %s on network mod id %s is not accessible", versionCheckHandlerMethod, container.getModId());
            }
        }

        if (this.checkHandler == null)
        {
            String versionBounds = mod.versionBounds();
            if (!Strings.isNullOrEmpty(versionBounds))
            {
                try
                {
                    this.acceptableRange = VersionRange.createFromVersionSpec(versionBounds);
                }
                catch (InvalidVersionSpecificationException e)
                {
                    FMLLog.log(Level.WARNING, e, "Invalid bounded range %s specified for network mod id %s", versionBounds, container.getModId());
                }
            }
        }

        FMLLog.finest("Testing mod %s to verify it accepts its own version in a remote connection", container.getModId());
        boolean acceptsSelf = acceptVersion(container.getVersion());
        if (!acceptsSelf)
        {
            FMLLog.severe("The mod %s appears to reject its own version number (%s) in its version handling. This is likely a severe bug in the mod!", container.getModId(), container.getVersion());
        }
        else
        {
            FMLLog.finest("The mod %s accepts its own version (%s)", container.getModId(), container.getVersion());
        }

        tryCreatingPacketHandler(container, mod.packetHandler(), mod.channels(), null);
        if (FMLCommonHandler.instance().getSide().isClient())
        {
            if (mod.clientPacketHandlerSpec() != getClientHandlerSpecDefaultValue())
            {
                tryCreatingPacketHandler(container, mod.clientPacketHandlerSpec().packetHandler(), mod.clientPacketHandlerSpec().channels(), Side.CLIENT);
            }
        }
        if (mod.serverPacketHandlerSpec() != getServerHandlerSpecDefaultValue())
        {
            tryCreatingPacketHandler(container, mod.serverPacketHandlerSpec().packetHandler(), mod.serverPacketHandlerSpec().channels(), Side.SERVER);
        }

        if (mod.connectionHandler() != getConnectionHandlerDefaultValue())
        {
            IConnectionHandler instance;
            try
            {
                instance = mod.connectionHandler().newInstance();
            }
            catch (Exception e)
            {
                FMLLog.log(Level.SEVERE, e, "Unable to create connection handler instance %s", mod.connectionHandler().getName());
                throw new FMLNetworkException(e);
            }

            NetworkRegistry.instance().registerConnectionHandler(instance);
        }

        if (mod.tinyPacketHandler()!=getTinyPacketHandlerDefaultValue())
        {
            try
            {
                tinyPacketHandler = mod.tinyPacketHandler().newInstance();
            }
            catch (Exception e)
            {
                FMLLog.log(Level.SEVERE, e, "Unable to create tiny packet handler instance %s", mod.tinyPacketHandler().getName());
                throw new FMLNetworkException(e);
            }
        }
    }

    private void tryCreatingPacketHandler(ModContainer container, Class<? extends IPacketHandler> clazz, String[] channels, Side side) {
        if (side!=null && side.isClient() && ! FMLCommonHandler.instance().getSide().isClient())
        {
            return;
        }
        if (clazz!=getPacketHandlerDefaultValue())
        {
            if (channels.length==0)
            {
                FMLLog.log(Level.WARNING, "The mod id %s attempted to register a packet handler without specifying channels for it", container.getModId());
            }
            else
            {
                IPacketHandler instance;
                try
                {
                    instance = clazz.newInstance();
                }
                catch (Exception e)
                {
                    FMLLog.log(Level.SEVERE, e, "Unable to create a packet handler instance %s for mod %s", clazz.getName(), container.getModId());
                    throw new FMLNetworkException(e);
                }

                for (String channel : channels)
                {
                    NetworkRegistry.instance().registerChannel(instance, channel, side);
                }
            }
        }
        else if (channels.length > 0)
        {
            FMLLog.warning("The mod id %s attempted to register channels without specifying a packet handler", container.getModId());
        }
    }

    private Object getConnectionHandlerDefaultValue() {
        try {
            if (connectionHandlerDefaultValue == null) {
                connectionHandlerDefaultValue = NetworkMod.class.getMethod("connectionHandler").getDefaultValue();
            }

            return connectionHandlerDefaultValue;
        } catch (NoSuchMethodException var2) {
            throw new RuntimeException("Derp?", var2);
        }
    }

    private Object getPacketHandlerDefaultValue() {
        try {
            if (packetHandlerDefaultValue == null) {
                packetHandlerDefaultValue = NetworkMod.class.getMethod("packetHandler").getDefaultValue();
            }

            return packetHandlerDefaultValue;
        } catch (NoSuchMethodException var2) {
            throw new RuntimeException("Derp?", var2);
        }
    }

    private Object getTinyPacketHandlerDefaultValue() {
        try {
            if (tinyPacketHandlerDefaultValue == null) {
                tinyPacketHandlerDefaultValue = NetworkMod.class.getMethod("tinyPacketHandler").getDefaultValue();
            }

            return tinyPacketHandlerDefaultValue;
        } catch (NoSuchMethodException var2) {
            throw new RuntimeException("Derp?", var2);
        }
    }

    private Object getClientHandlerSpecDefaultValue() {
        try {
            if (clientHandlerDefaultValue == null) {
                clientHandlerDefaultValue = NetworkMod.class.getMethod("clientPacketHandlerSpec").getDefaultValue();
            }

            return clientHandlerDefaultValue;
        } catch (NoSuchMethodException var2) {
            throw new RuntimeException("Derp?", var2);
        }
    }

    private Object getServerHandlerSpecDefaultValue() {
        try {
            if (serverHandlerDefaultValue == null) {
                serverHandlerDefaultValue = NetworkMod.class.getMethod("serverPacketHandlerSpec").getDefaultValue();
            }

            return serverHandlerDefaultValue;
        } catch (NoSuchMethodException var2) {
            throw new RuntimeException("Derp?", var2);
        }
    }

    public boolean requiresClientSide() {
        return this.mod.clientSideRequired();
    }

    public boolean requiresServerSide() {
        return this.mod.serverSideRequired();
    }

    public boolean acceptVersion(String version) {
        if (this.checkHandler != null) {
            try {
                return (Boolean)this.checkHandler.invoke(this.container.getMod(), version);
            } catch (Exception var3) {
                FMLLog.log(Level.WARNING, var3, "There was a problem invoking the checkhandler method %s for network mod id %s", new Object[]{this.checkHandler.getName(), this.container.getModId()});
                return false;
            }
        } else {
            return this.acceptableRange != null ? this.acceptableRange.containsVersion(new DefaultArtifactVersion(version)) : this.container.getVersion().equals(version);
        }
    }

    public int getLocalId() {
        return this.localId;
    }

    public int getNetworkId() {
        return this.networkId;
    }

    public ModContainer getContainer() {
        return this.container;
    }

    public NetworkMod getMod() {
        return this.mod;
    }

    public boolean isNetworkMod() {
        return this.mod != null;
    }

    public void setNetworkId(int value) {
        this.networkId = value;
    }

    public boolean hasTinyPacketHandler() {
        return this.tinyPacketHandler != null;
    }

    public ITinyPacketHandler getTinyPacketHandler() {
        return this.tinyPacketHandler;
    }
}
