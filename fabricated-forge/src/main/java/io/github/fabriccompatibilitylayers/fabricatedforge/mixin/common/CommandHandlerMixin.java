/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.ICommand;
import net.minecraft.src.ICommandManager;
import net.minecraft.src.ICommandSender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandHandler.class)
public abstract class CommandHandlerMixin implements ICommandManager {
    @Expression("? > -1")
    @Inject(method = "executeCommand", at = @At(value = "MIXINEXTRAS:EXPRESSION"), cancellable = true)
    private void forge$postCommandEvent(ICommandSender par1ICommandSender, String par2, CallbackInfo ci,
                                        @Local String[] var3,
                                        @Local ICommand var5) throws Throwable {
        CommandEvent event = new CommandEvent(var5, par1ICommandSender, var3);
        if (MinecraftForge.EVENT_BUS.post(event))
        {
            if (event.exception != null) {
                throw event.exception;
            }
            ci.cancel();
        }
    }
}
