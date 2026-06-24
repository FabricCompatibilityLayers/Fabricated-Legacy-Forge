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
 * Declares that one or more {@code catch} clauses in a target-class method should be widened to a
 * broader exception type before the mixin is applied.
 *
 * <h3>Usage</h3>
 * Place this annotation (repeatable) on a {@code @Shadow} method declaration in a
 * {@code @Mixin} class.  The shadow identifies the target method to modify; each
 * {@code @WidenedCatch} instance names the narrow exception type currently in a {@code catch}
 * clause and the wide supertype that should replace it.  Both values are ASM internal names
 * (slash-separated).
 *
 * <pre>
 * &#64;Mixin(SomeClass.class)
 * public abstract class SomeClassMixin {
 *
 *     &#64;WidenedCatch(from = "cpw/mods/fml/common/LoaderException", to = "java/lang/Exception")
 *     &#64;Shadow
 *     public abstract void someMethod();   // ← identifies the target method to patch
 * }
 * </pre>
 *
 * <h3>Why this is needed</h3>
 * Some Minecraft or Forge methods catch exception types that may not be present on all Fabric
 * environments at runtime, or that should be widened for compatibility.  This annotation rewrites
 * the catch clause directly in the target class bytecode — together with every instruction that
 * refers to the narrow exception type (virtual calls, field accesses, local variable table
 * entries) — so the class verifier never sees the narrow type.
 *
 * <h3>What the MixinPlugin does in {@code preApply}</h3>
 * <ol>
 *   <li>Scans the mixin class for {@code @Shadow} methods that also carry this annotation.</li>
 *   <li>Resolves the matching method in the target class by name and descriptor.</li>
 *   <li>Replaces every {@link org.objectweb.asm.tree.TryCatchBlockNode#type} equal to
 *       {@link #from()} with {@link #to()} in that method's exception table.</li>
 *   <li>Rewrites all instruction operands and local-variable descriptors that reference
 *       the narrow type with the wide type (INVOKEVIRTUAL/GETFIELD owners, CHECKCAST
 *       targets, local-variable table entries, etc.).</li>
 * </ol>
 *
 * <p>Because shadow methods are never copied into the target class, no annotation stripping is
 * needed.  The annotation is repeatable: apply multiple {@code @WidenedCatch} on the same shadow
 * to widen several distinct catch clauses independently.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(WidenedCatch.List.class)
public @interface WidenedCatch {
    /** ASM internal name of the narrow exception type to widen from (e.g. {@code "cpw/mods/fml/common/LoaderException"}). */
    String from();

    /** ASM internal name of the wide exception type to use instead (e.g. {@code "java/lang/Exception"}). */
    String to();

    /** Container annotation required by {@link Repeatable}. */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        WidenedCatch[] value();
    }
}