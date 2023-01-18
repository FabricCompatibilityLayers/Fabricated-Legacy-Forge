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
package net.minecraft;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.modloader.BaseModProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.class_469;
import net.minecraft.client.BlockRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ControllablePlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Connection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerPacketListener;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Random;

public abstract class BaseMod implements BaseModProxy {
    public BaseMod() {
    }

    public final boolean doTickInGame(TickType tick, boolean tickEnd, Object... data) {
        Minecraft mc = FMLClientHandler.instance().getClient();
        boolean hasWorld = mc.world != null;
        return tickEnd && (tick == TickType.RENDER || tick == TickType.CLIENT) && hasWorld ? this.onTickInGame((Float) data[0], mc) : true;
    }

    public final boolean doTickInGUI(TickType tick, boolean tickEnd, Object... data) {
        Minecraft mc = FMLClientHandler.instance().getClient();
        boolean hasWorld = mc.world != null;
        return !tickEnd || tick != TickType.RENDER && (tick != TickType.CLIENT || !hasWorld) ? true : this.onTickInGUI((Float) data[0], mc, mc.currentScreen);
    }

    public int addFuel(int id, int metadata) {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Environment(EnvType.CLIENT)
    public void addRenderer(Map<Class<? extends Entity>, EntityRenderer> renderers) {
    }

    public int dispenseEntity(World world, ItemStack item, Random rnd, int x, int y, int z, int xVel, int zVel, double entX, double entY, double entZ) {
        return -1;
    }

    public void generateNether(World world, Random random, int chunkX, int chunkZ) {
    }

    public void generateSurface(World world, Random random, int chunkX, int chunkZ) {
    }

    @SideOnly(Side.CLIENT)
    @Environment(EnvType.CLIENT)
    public HandledScreen getContainerGUI(ControllablePlayerEntity player, int containerID, int x, int y, int z) {
        return null;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public String getPriorities() {
        return "";
    }

    public abstract String getVersion();

    @SideOnly(Side.CLIENT)
    @Environment(EnvType.CLIENT)
    public void keyboardEvent(KeyBinding event) {
    }

    public abstract void load();

    public void modsLoaded() {
    }

    public void onItemPickup(PlayerEntity player, ItemStack item) {
    }

    @SideOnly(Side.CLIENT)
    @Environment(EnvType.CLIENT)
    public boolean onTickInGame(float time, Minecraft minecraftInstance) {
        return false;
    }

    public boolean onTickInGame(MinecraftServer minecraftServer) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Environment(EnvType.CLIENT)
    public boolean onTickInGUI(float tick, Minecraft game, Screen gui) {
        return false;
    }

    public void clientChat(String text) {
    }

    @SideOnly(Side.CLIENT)
    @Environment(EnvType.CLIENT)
    public void clientConnect(class_469 handler) {
    }

    @SideOnly(Side.CLIENT)
    @Environment(EnvType.CLIENT)
    public void clientDisconnect(class_469 handler) {
    }

    public void receiveCustomPacket(CustomPayloadC2SPacket packet) {
    }

    @SideOnly(Side.CLIENT)
    @Environment(EnvType.CLIENT)
    public void registerAnimation(Minecraft game) {
    }

    @SideOnly(Side.CLIENT)
    @Environment(EnvType.CLIENT)
    public void renderInvBlock(BlockRenderer renderer, Block block, int metadata, int modelID) {
    }

    @SideOnly(Side.CLIENT)
    @Environment(EnvType.CLIENT)
    public boolean renderWorldBlock(BlockRenderer renderer, BlockView world, int x, int y, int z, Block block, int modelID) {
        return false;
    }

    public void serverConnect(PacketListener handler) {
    }

    public void serverCustomPayload(ServerPacketListener handler, CustomPayloadC2SPacket packet) {
    }

    public void serverDisconnect() {
    }

    public void takenFromCrafting(PlayerEntity player, ItemStack item, Inventory matrix) {
    }

    public void takenFromFurnace(PlayerEntity player, ItemStack item) {
    }

    public String toString() {
        return this.getName() + " " + this.getVersion();
    }

    public void serverChat(ServerPacketListener source, String message) {
    }

    public void onClientLogin(PlayerEntity player) {
    }

    public void onClientLogout(Connection mgr) {
    }

    @SideOnly(Side.CLIENT)
    @Environment(EnvType.CLIENT)
    public Entity spawnEntity(int entityId, World world, double scaledX, double scaledY, double scaledZ) {
        return null;
    }

    public void clientCustomPayload(class_469 handler, CustomPayloadC2SPacket packet) {
    }
}
