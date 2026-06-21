/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares that a mixin method is a widened-argument overload of an existing target method,
 * identifying the original (narrow) method by its Searge/MCP name and descriptor.
 *
 * <h3>Usage</h3>
 * Place this annotation on a method in a {@code @Mixin} class whose parameter list is identical
 * to the target method's except that one (or more) parameters use a <em>supertype</em> of what
 * the target method accepts.  {@link #name()} and {@link #desc()} identify the original
 * narrow method in Searge/MCP namespace; both are remapped via {@code MappingsHelper} at
 * post-apply time before the lookup.
 *
 * <pre>
 * &#64;Mixin(SomeBlock.class)
 * public class SomeBlockMixin {
 *     // Target has: void onEntityWalking(EntityLiving entity)  (Searge name)
 *     // We want a wider overload that handles any Entity.
 *
 *     &#64;WidenedOverload(name = "onEntityWalking", desc = "(Lnet/minecraft/entity/EntityLiving;)V")
 *     public void onEntityWalking(Entity entity) {
 *         // placeholder body — replaced at post-apply with the narrow method's instructions
 *     }
 * }
 * </pre>
 *
 * <h3>What the MixinPlugin does in {@code postApply}</h3>
 * <ol>
 *   <li>Locates this annotated method (wide signature) in the merged target class.</li>
 *   <li>Maps {@link #name()} and {@link #desc()} through {@code MappingsHelper} to resolve
 *       the Calamus name and descriptor used in the target class.</li>
 *   <li>Finds the narrow method with the resolved name and descriptor.</li>
 *   <li>Copies the narrow method's instructions into the wide method (replacing the
 *       placeholder mixin body).</li>
 *   <li>Replaces the narrow method's body with a simple delegate call to the wide method
 *       (no cast required — all argument conversions are widening and implicit on the JVM).</li>
 *   <li>Removes this annotation from the wide method so no bytecode artifact remains.</li>
 * </ol>
 *
 * <p>The result: both overloads exist in the target.  The narrow one delegates upward; the
 * wide one contains the original implementation and can be called with any compatible type.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WidenedOverload {
    /** Obfuscated name of the original narrow-argument method. */
    String name();

    /** ASM descriptor of the original narrow-argument method in Obfuscated namespace.
     *  Example: {@code "(La;)V"} */
    String desc();
}