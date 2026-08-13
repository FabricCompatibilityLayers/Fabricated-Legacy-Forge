/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.items.self.forge;

import cpw.mods.fml.common.FMLLog;
import net.minecraftforge.common.ForgeInternalHandler;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventBus;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Conditional(modLoaded = {
        @Mod("osl-items"),
        @Mod("osl-blocks")
})
@Mixin(MinecraftForge.class)
public class MinecraftForgeMixin {
    @Shadow
    @Final
    public static EventBus EVENT_BUS;

    @Shadow
    @Final
    private static ForgeInternalHandler INTERNAL_HANDLER;

    /**
     * @author CatCore
     * @reason skip 80% of the original logic
     */
    @Overwrite(remap = false)
    public static void initialize()
    {
        System.out.printf("MinecraftForge v%s Initialized\n", ForgeVersion.getVersion());
        FMLLog.info("MinecraftForge v%s Initialized", ForgeVersion.getVersion());

        EVENT_BUS.register(INTERNAL_HANDLER);
    }
}
