/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event;

import net.minecraft.block.Block;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.LivingSpecialSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ForgeEventFactory {
    public ForgeEventFactory() {
    }

    public static boolean doPlayerHarvestCheck(PlayerEntity player, Block block, boolean success) {
        PlayerEvent.HarvestCheck event = new PlayerEvent.HarvestCheck(player, block, success);
        MinecraftForge.EVENT_BUS.post(event);
        return event.success;
    }

    public static float getBreakSpeed(PlayerEntity player, Block block, int metadata, float original) {
        PlayerEvent.BreakSpeed event = new PlayerEvent.BreakSpeed(player, block, metadata, original);
        return MinecraftForge.EVENT_BUS.post(event) ? -1.0F : event.newSpeed;
    }

    public static PlayerInteractEvent onPlayerInteract(PlayerEntity player, PlayerInteractEvent.Action action, int x, int y, int z, int face) {
        PlayerInteractEvent event = new PlayerInteractEvent(player, action, x, y, z, face);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static void onPlayerDestroyItem(PlayerEntity player, ItemStack stack) {
        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, stack));
    }

    public static Event.Result canEntitySpawn(MobEntity entity, World world, float x, float y, float z) {
        LivingSpawnEvent.CheckSpawn event = new LivingSpawnEvent.CheckSpawn(entity, world, x, y, z);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getResult();
    }

    public static boolean doSpecialSpawn(MobEntity entity, World world, float x, float y, float z) {
        boolean result = MinecraftForge.EVENT_BUS.post(new LivingSpecialSpawnEvent(entity, world, x, y, z));
        LivingSpawnEvent.SpecialSpawn nEvent = new LivingSpawnEvent.SpecialSpawn(entity, world, x, y, z);
        if (result) {
            nEvent.setCanceled(true);
        }

        return MinecraftForge.EVENT_BUS.post(nEvent);
    }
}
