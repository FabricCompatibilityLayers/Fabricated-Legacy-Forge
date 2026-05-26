package io.github.fabriccompatibilitylayers.fabricatedfml.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Two-annotation, two-step mechanism for promoting an instance field in the target class
 * to {@code static} via the MixinPlugin.
 *
 * <h3>Why @Shadow must be non-static</h3>
 * Mixin validates {@code @Shadow} field staticness inside {@code createContextFor()}, which
 * runs <em>before</em> the plugin's {@code preApply} hook.  Declaring the shadow as
 * {@code static} therefore fails validation before the plugin has any chance to act.
 * The shadow must be declared to match the vanilla (non-static) target field:
 *
 * <pre>
 * &#64;Shadow
 * &#64;MakeStatic
 * private SomeType fieldName;   // NOT static — must match the vanilla instance field
 * </pre>
 *
 * <h3>Two-step promotion performed by the MixinPlugin</h3>
 * <ol>
 *   <li><b>preApply</b> (after validation) — sets {@code ACC_STATIC} on the target field
 *       so subsequent bytecode in the target treats it as static.</li>
 *   <li><b>postApply</b> (after @Overwrite / @Inject methods have been copied in) —
 *       rewrites every {@code GETFIELD}/{@code PUTFIELD} that references these now-static
 *       fields to {@code GETSTATIC}/{@code PUTSTATIC}, including in the freshly-injected
 *       mixin methods.</li>
 * </ol>
 *
 * <h3>Accessing @MakeStatic fields from static mixin methods</h3>
 * Because the mixin declares the field as non-static, it cannot be referenced directly
 * from a {@code static} method (e.g. a {@code @Inject} into {@code <clinit>}).  Use the
 * singleton instance instead — postApply's rewrite will fix the resulting
 * {@code GETFIELD}/{@code PUTFIELD}:
 *
 * <pre>
 * &#64;Inject(method = "&lt;clinit&gt;", at = &#64;At("RETURN"))
 * private static void classInit(CallbackInfo ci) {
 *     MyMixin self = (MyMixin)(Object) instance; // instance is a @Shadow static field
 *     self.fieldName = ...;
 * }
 * </pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MakeStatic {
}