/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.server;

import io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common.MinecraftServerAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.GuiStatsComponent;
import net.minecraft.src.TcpConnection;
import net.minecraft.src.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.swing.*;
import java.text.DecimalFormat;

@Mixin(GuiStatsComponent.class)
public class GuiStatsComponentMixin extends JComponent {

    @Shadow
    private String[] displayStrings;

    @Shadow
    @Final
    private MinecraftServer field_79017_e;

    @Final
    @Shadow
    private static DecimalFormat field_79020_a;

    @Shadow
    private int[] memoryUse;

    @Shadow
    private int updateCounter;

    @Shadow
    private double func_79015_a(long[] par1ArrayOfLong) {
        return 0;
    }

    // Pattern D (@Overwrite): loop replacement touches data sources, loop variable,
    // and array size all at once — too structurally different for a surgical injector.
    // Folds in the hunk-2 displayStrings resize, making a separate @Inject(HEAD) unnecessary.
    /**
     * @author FabricCompatibilityLayers
     * @reason Replaces fixed worldServers index loop with DimensionManager.getIDs()
     *         so modded dimensions appear and tick times are read from worldTickTimes.
     */
    @Overwrite
    private void updateStats() {
        this.displayStrings = new String[5 + DimensionManager.getIDs().length];
        long var1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.gc();
        this.displayStrings[0] = "Memory use: " + var1 / 1024L / 1024L + " mb (" + Runtime.getRuntime().freeMemory() * 100L / Runtime.getRuntime().maxMemory() + "% free)";
        this.displayStrings[1] = "Threads: " + TcpConnection.field_74471_a.get() + " + " + TcpConnection.field_74469_b.get();
        this.displayStrings[2] = "Avg tick: " + field_79020_a.format(this.func_79015_a(this.field_79017_e.tickTimeArray) * 1.0E-6D) + " ms";
        this.displayStrings[3] = "Avg sent: " + (int) this.func_79015_a(this.field_79017_e.sentPacketCountArray) + ", Avg size: " + (int) this.func_79015_a(this.field_79017_e.sentPacketSizeArray);
        this.displayStrings[4] = "Avg rec: " + (int) this.func_79015_a(this.field_79017_e.receivedPacketCountArray) + ", Avg size: " + (int) this.func_79015_a(this.field_79017_e.receivedPacketSizeArray);

        if (this.field_79017_e.worldServers != null) {
            int x = 0;
            for (Integer id : DimensionManager.getIDs()) {
                this.displayStrings[5 + x] = "Lvl " + id + " tick: " + field_79020_a.format(this.func_79015_a(((MinecraftServerAccessor) this.field_79017_e).getWorldTickTimes().get(id)) * 1.0E-6D) + " ms";
                WorldServer world = DimensionManager.getWorld(id);
                if (world != null && world.theChunkProviderServer != null) {
                    this.displayStrings[5 + x] = this.displayStrings[5 + x] + ", " + world.theChunkProviderServer.makeString();
                }
                x++;
            }
        }

        this.memoryUse[this.updateCounter++ & 0xFF] = (int) (this.func_79015_a(this.field_79017_e.sentPacketSizeArray) * 100.0 / 12500.0);
        this.repaint();
    }
}