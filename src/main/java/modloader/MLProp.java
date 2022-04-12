package modloader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface MLProp {
    String name() default "";

    String info() default "";

    double min() default -1.0D / 0.0;

    double max() default 1.0D / 0.0;
}
