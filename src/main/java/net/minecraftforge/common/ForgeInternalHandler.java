/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

import cpw.mods.fml.common.FMLLog;
import fr.catcore.fabricatedforge.mixininterface.IItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;

public class ForgeInternalHandler {
    public ForgeInternalHandler() {
    }

    @ForgeSubscribe(
            priority = EventPriority.HIGHEST
    )
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.world.isClient) {
            if (event.entity.getPersistentID() == null) {
                event.entity.generatePersistentID();
            } else {
                ForgeChunkManager.loadEntity(event.entity);
            }
        }

        Entity entity = event.entity;
        if (entity.getClass().equals(ItemEntity.class)) {
            ItemStack stack = ((ItemEntity)entity).getDataTracker().getStack(10);
            if (stack == null) {
                return;
            }

            Item item = stack.getItem();
            if (item == null) {
                FMLLog.warning(
                        "Attempted to add a EntityItem to the world with a invalid item: ID %d at (%2.2f,  %2.2f, %2.2f), this is most likely a config issue between you and the server. Please double check your configs",
                        new Object[]{stack.id, entity.x, entity.y, entity.z}
                );
                entity.remove();
                event.setCanceled(true);
                return;
            }

            if (item.hasCustomEntity(stack)) {
                Entity newEntity = item.createEntity(event.world, entity, stack);
                if (newEntity != null) {
                    entity.remove();
                    event.setCanceled(true);
                    event.world.spawnEntity(newEntity);
                }
            }
        }
    }

    @ForgeSubscribe(
            priority = EventPriority.HIGHEST
    )
    public void onDimensionLoad(WorldEvent.Load event) {
        ForgeChunkManager.loadWorld(event.world);
    }

    @ForgeSubscribe(
            priority = EventPriority.HIGHEST
    )
    public void onDimensionSave(WorldEvent.Save event) {
        ForgeChunkManager.saveWorld(event.world);
    }

    @ForgeSubscribe(
            priority = EventPriority.HIGHEST
    )
    public void onDimensionUnload(WorldEvent.Unload event) {
        ForgeChunkManager.unloadWorld(event.world);
    }
}
