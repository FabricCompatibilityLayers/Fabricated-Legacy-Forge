/**
 * Copyright (C) 2022-2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.src.ConsoleLogFormatter;
import net.minecraft.src.ConsoleLogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@Mixin(ConsoleLogManager.class)
public class ConsoleLogManagerMixin {
    @Shadow public static Logger field_73700_a;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void func_73699_a() {
        ConsoleLogFormatter var0 = new ConsoleLogFormatter();
        field_73700_a.setParent(FMLLog.getLogger());

        try {
            FileHandler var2 = new FileHandler("server.log", true);
            var2.setFormatter(var0);
            field_73700_a.addHandler(var2);
        } catch (Exception var3) {
            field_73700_a.log(Level.WARNING, "Failed to log to server.log", (Throwable)var3);
        }
    }
}
