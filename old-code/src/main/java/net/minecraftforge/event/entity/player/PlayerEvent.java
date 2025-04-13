/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event.entity.player;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.entity.living.LivingEvent;

public class PlayerEvent extends LivingEvent {
    public final PlayerEntity entityPlayer;

    public PlayerEvent(PlayerEntity player) {
        super(player);
        this.entityPlayer = player;
    }

    @Cancelable
    public static class BreakSpeed extends PlayerEvent {
        public final Block block;
        public final int metadata;
        public final float originalSpeed;
        public float newSpeed = 0.0F;

        public BreakSpeed(PlayerEntity player, Block block, int metadata, float original) {
            super(player);
            this.block = block;
            this.metadata = metadata;
            this.originalSpeed = original;
            this.newSpeed = original;
        }
    }

    public static class HarvestCheck extends PlayerEvent {
        public final Block block;
        public boolean success;

        public HarvestCheck(PlayerEntity player, Block block, boolean success) {
            super(player);
            this.block = block;
            this.success = success;
        }
    }
}
