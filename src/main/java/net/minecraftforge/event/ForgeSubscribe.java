package net.minecraftforge.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ForgeSubscribe {
    EventPriority priority() default EventPriority.NORMAL;

    boolean receiveCanceled() default false;
}
