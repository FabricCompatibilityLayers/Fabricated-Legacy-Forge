/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event;

import net.minecraft.command.Command;
import net.minecraft.command.CommandSource;

@Cancelable
public class CommandEvent extends Event {
    public final Command command;
    public final CommandSource sender;
    public String[] parameters;
    public Throwable exception;

    public CommandEvent(Command command, CommandSource sender, String[] parameters) {
        this.command = command;
        this.sender = sender;
        this.parameters = parameters;
    }
}
