/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.event;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.*;
import static java.lang.annotation.ElementType.*;

@Retention(value = RUNTIME)
@Target(value = METHOD)
public @interface ForgeSubscribe
{
    public EventPriority priority() default EventPriority.NORMAL;
    public boolean receiveCanceled() default false;
}
