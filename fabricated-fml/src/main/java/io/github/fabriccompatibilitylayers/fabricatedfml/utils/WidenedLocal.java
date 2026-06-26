/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares that one or more local-variable type references in a target-class method should be
 * widened to a broader type before the mixin is applied.
 *
 * <h3>Usage</h3>
 * Place this annotation (repeatable) on a {@code @Shadow} method declaration in a
 * {@code @Mixin} class.  The shadow identifies the target method to modify; each
 * {@code @WidenedLocal} instance names the narrow type currently referenced by locals in that
 * method and the wide supertype that should replace it.  Both values are ASM internal names
 * (slash-separated).
 *
 * <pre>
 * &#64;Mixin(SomeClass.class)
 * public abstract class SomeClassMixin {
 *
 *     &#64;WidenedLocal(from = "cpw/mods/fml/common/SomeNarrowType", to = "java/lang/Object")
 *     &#64;Shadow
 *     public abstract void someMethod();   // ← identifies the target method to patch
 * }
 * </pre>
 *
 * <h3>Why this is needed</h3>
 * Some Minecraft or Forge methods hold local variables whose declared types may not be present
 * on all Fabric environments at runtime.  This annotation rewrites every instruction operand and
 * local-variable table entry that references the narrow type in the target method body — without
 * requiring a {@code catch} clause to be present — so the class verifier never sees the
 * unavailable narrow type.
 *
 * <h3>What the MixinPlugin does in {@code preApply}</h3>
 * <ol>
 *   <li>Scans the mixin class for {@code @Shadow} methods that also carry this annotation.</li>
 *   <li>Resolves the matching method in the target class by name and descriptor.</li>
 *   <li>Rewrites all instruction operands (CHECKCAST, INSTANCEOF, INVOKEVIRTUAL owners, etc.)
 *       and local-variable table descriptors that reference the narrow type with the wide type.</li>
 * </ol>
 *
 * <p>Because shadow methods are never copied into the target class, no annotation stripping is
 * needed.  The annotation is repeatable: apply multiple {@code @WidenedLocal} on the same shadow
 * to widen several distinct local types independently.
 *
 * <p>Use {@link WidenedCatch} instead when the type to widen appears in a {@code catch} clause.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(WidenedLocal.List.class)
public @interface WidenedLocal {
    /** ASM internal name of the narrow type to widen from (e.g. {@code "cpw/mods/fml/common/SomeType"}). */
    String from();

    /** ASM internal name of the wide type to use instead (e.g. {@code "java/lang/Object"}). */
    String to();

    String modid() default "";

    /** Container annotation required by {@link Repeatable}. */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        WidenedLocal[] value();
    }
}