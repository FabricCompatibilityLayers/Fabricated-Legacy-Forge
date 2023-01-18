/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.client.event;

import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.Event;

@Cancelable
public class ClientChatReceivedEvent extends Event {
    public String message;

    public ClientChatReceivedEvent(String message) {
        this.message = message;
    }
}
