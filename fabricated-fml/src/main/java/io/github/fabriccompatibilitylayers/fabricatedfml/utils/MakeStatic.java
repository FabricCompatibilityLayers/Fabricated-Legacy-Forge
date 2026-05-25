package io.github.fabriccompatibilitylayers.fabricatedfml.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a {@code @Shadow} field in a Mixin so that the MixinPlugin will promote the
 * corresponding field in the target class to {@code static} before Mixin validates the
 * shadow.  Declare the shadow field as {@code static} in the mixin to match:
 *
 * <pre>
 * &#64;Shadow
 * &#64;MakeStatic
 * private static SomeType fieldName;
 * </pre>
 *
 * The MixinPlugin handles two things automatically:
 * <ol>
 *   <li><b>preApply</b> — sets {@code ACC_STATIC} on the target field so Mixin's shadow
 *       validator sees a static field and accepts the static shadow declaration.</li>
 *   <li><b>postApply</b> — rewrites any {@code GETFIELD}/{@code PUTFIELD} instructions
 *       in the target's existing methods to {@code GETSTATIC}/{@code PUTSTATIC} so the
 *       bytecode remains valid after the field's storage model has changed.</li>
 * </ol>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MakeStatic {
}