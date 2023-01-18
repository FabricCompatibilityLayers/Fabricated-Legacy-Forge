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

import com.google.common.base.Equivalences;
import com.google.common.base.Supplier;
import com.google.common.collect.*;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.modloader.BaseModProxy;
import cpw.mods.fml.common.modloader.IModLoaderSidedHelper;
import cpw.mods.fml.common.modloader.ModLoaderHelper;
import cpw.mods.fml.common.modloader.ModLoaderModContainer;
import cpw.mods.fml.common.network.EntitySpawnPacket;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.BaseMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.class_469;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ControllablePlayerEntity;
import net.minecraft.network.Connection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

public class ModLoaderClientHelper implements IModLoaderSidedHelper {
    private Minecraft client;
    private static Multimap<ModLoaderModContainer, ModLoaderKeyBindingHandler> keyBindingContainers;
    private Map<Connection, PacketListener> managerLookups = new MapMaker().weakKeys().weakValues().makeMap();

    public static int obtainBlockModelIdFor(BaseMod mod, boolean inventoryRenderer) {
        int renderId = RenderingRegistry.getNextAvailableRenderId();
        ModLoaderBlockRendererHandler bri = new ModLoaderBlockRendererHandler(renderId, inventoryRenderer, mod);
        RenderingRegistry.registerBlockHandler(bri);
        return renderId;
    }

    public static void handleFinishLoadingFor(ModLoaderModContainer mc, Minecraft game) {
        FMLLog.finer("Handling post startup activities for ModLoader mod %s", new Object[]{mc.getModId()});
        BaseMod mod = (BaseMod)mc.getMod();
        Map<Class<? extends Entity>, EntityRenderer> renderers = Maps.newHashMap(EntityRenderDispatcher.INSTANCE.renderers);

        try {
            FMLLog.finest("Requesting renderers from basemod %s", new Object[]{mc.getModId()});
            mod.addRenderer(renderers);
            FMLLog.finest("Received %d renderers from basemod %s", new Object[]{renderers.size(), mc.getModId()});
        } catch (Exception var8) {
            FMLLog.log(
                    Level.SEVERE,
                    var8,
                    "A severe problem was detected with the mod %s during the addRenderer call. Continuing, but expect odd results",
                    new Object[]{mc.getModId()}
            );
        }

        MapDifference<Class<? extends Entity>, EntityRenderer> difference = Maps.difference(
                EntityRenderDispatcher.INSTANCE.renderers, renderers, Equivalences.identity()
        );

        for(Map.Entry<Class<? extends Entity>, EntityRenderer> e : difference.entriesOnlyOnLeft().entrySet()) {
            FMLLog.warning(
                    "The mod %s attempted to remove an entity renderer %s from the entity map. This will be ignored.",
                    new Object[]{mc.getModId(), ((Class)e.getKey()).getName()}
            );
        }

        for(Map.Entry<Class<? extends Entity>, EntityRenderer> e : difference.entriesOnlyOnRight().entrySet()) {
            FMLLog.finest(
                    "Registering ModLoader entity renderer %s as instance of %s",
                    new Object[]{((Class)e.getKey()).getName(), ((EntityRenderer)e.getValue()).getClass().getName()}
            );
            RenderingRegistry.registerEntityRenderingHandler((Class)e.getKey(), (EntityRenderer)e.getValue());
        }

        for(Map.Entry<Class<? extends Entity>, MapDifference.ValueDifference<EntityRenderer>> e : difference.entriesDiffering().entrySet()) {
            FMLLog.finest(
                    "Registering ModLoader entity rendering override for %s as instance of %s",
                    new Object[]{((Class)e.getKey()).getName(), ((EntityRenderer)((MapDifference.ValueDifference)e.getValue()).rightValue()).getClass().getName()}
            );
            RenderingRegistry.registerEntityRenderingHandler((Class)e.getKey(), (EntityRenderer)((MapDifference.ValueDifference)e.getValue()).rightValue());
        }

        try {
            mod.registerAnimation(game);
        } catch (Exception var7) {
            FMLLog.log(
                    Level.SEVERE,
                    var7,
                    "A severe problem was detected with the mod %s during the registerAnimation call. Continuing, but expect odd results",
                    new Object[]{mc.getModId()}
            );
        }
    }

    public ModLoaderClientHelper(Minecraft client) {
        this.client = client;
        ModLoaderHelper.sidedHelper = this;
        keyBindingContainers = Multimaps.newMultimap(Maps.newHashMap(), new Supplier<Collection<ModLoaderKeyBindingHandler>>() {
            public Collection<ModLoaderKeyBindingHandler> get() {
                return Collections.singleton(new ModLoaderKeyBindingHandler());
            }
        });
    }

    public void finishModLoading(ModLoaderModContainer mc) {
        handleFinishLoadingFor(mc, this.client);
    }

    public static void registerKeyBinding(BaseModProxy mod, KeyBinding keyHandler, boolean allowRepeat) {
        ModLoaderModContainer mlmc = (ModLoaderModContainer)Loader.instance().activeModContainer();
        ModLoaderKeyBindingHandler handler = (ModLoaderKeyBindingHandler)Iterables.getOnlyElement(keyBindingContainers.get(mlmc));
        handler.setModContainer(mlmc);
        handler.addKeyBinding(keyHandler, allowRepeat);
        KeyBindingRegistry.registerKeyBinding(handler);
    }

    public Object getClientGui(BaseModProxy mod, PlayerEntity player, int ID, int x, int y, int z) {
        return ((BaseMod)mod).getContainerGUI((ControllablePlayerEntity)player, ID, x, y, z);
    }

    public Entity spawnEntity(BaseModProxy mod, EntitySpawnPacket input, EntityRegistry.EntityRegistration er) {
        return ((BaseMod)mod).spawnEntity(er.getModEntityId(), this.client.world, input.scaledX, input.scaledY, input.scaledZ);
    }

    public void sendClientPacket(BaseModProxy mod, CustomPayloadC2SPacket packet) {
        ((BaseMod)mod).clientCustomPayload(this.client.playerEntity.field_1667, packet);
    }

    public void clientConnectionOpened(PacketListener netClientHandler, Connection manager, BaseModProxy mod) {
        this.managerLookups.put(manager, netClientHandler);
        ((BaseMod)mod).clientConnect((class_469)netClientHandler);
    }

    public boolean clientConnectionClosed(Connection manager, BaseModProxy mod) {
        if (this.managerLookups.containsKey(manager)) {
            ((BaseMod)mod).clientDisconnect((class_469)this.managerLookups.get(manager));
            return true;
        } else {
            return false;
        }
    }
}
