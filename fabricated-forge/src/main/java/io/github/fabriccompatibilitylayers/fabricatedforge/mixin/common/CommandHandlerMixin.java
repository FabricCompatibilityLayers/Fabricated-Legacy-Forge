/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.ICommand;
import net.minecraft.src.ICommandManager;
import net.minecraft.src.ICommandSender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommandHandler.class)
public abstract class CommandHandlerMixin implements ICommandManager {
    @WrapOperation(method = "executeCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ICommand;processCommand(Lnet/minecraft/src/ICommandSender;[Ljava/lang/String;)V"))
    private void forge$postCommandEvent(ICommand var5, ICommandSender par1ICommandSender, String[] var3, Operation<Void> original) throws Throwable {
        CommandEvent event = new CommandEvent(var5, par1ICommandSender, var3);
        if (!MinecraftForge.EVENT_BUS.post(event))
        {
            original.call(var5, par1ICommandSender, event.parameters);
        }
        else
        {
            if (event.exception != null)
            {
                throw event.exception;
            }
        }
    }
}
