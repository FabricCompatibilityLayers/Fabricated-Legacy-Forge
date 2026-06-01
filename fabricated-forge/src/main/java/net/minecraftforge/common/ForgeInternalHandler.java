/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

import java.util.UUID;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.EntityExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.event.world.WorldEvent;

public class ForgeInternalHandler
{
    @ForgeSubscribe(priority = EventPriority.HIGHEST)
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (!event.world.isRemote)
        {
            if (((EntityExtension) event.entity).getPersistentID() == null)
            {
                ((EntityExtension) event.entity).generatePersistentID();
            }
            else
            {
                ForgeChunkManager.loadEntity(event.entity);
            }
        }
        Entity entity = event.entity;
        if (entity.getClass().equals(EntityItem.class))
        {
            ItemStack item = ((EntityItem)entity).item;
            if (item != null && ((ItemExtension) item.getItem()).hasCustomEntity(item))
            {
                Entity newEntity = ((ItemExtension) item.getItem()).createEntity(event.world, entity, item);
                if (newEntity != null)
                {
                    entity.setDead();
                    event.setCanceled(true);
                    event.world.spawnEntityInWorld(newEntity);
                }
            }
        }
    }

    @ForgeSubscribe(priority = EventPriority.HIGHEST)
    public void onDimensionLoad(WorldEvent.Load event)
    {
        ForgeChunkManager.loadWorld(event.world);
    }

    @ForgeSubscribe(priority = EventPriority.HIGHEST)
    public void onDimensionSave(WorldEvent.Save event)
    {
    	ForgeChunkManager.saveWorld(event.world);
    }
}
