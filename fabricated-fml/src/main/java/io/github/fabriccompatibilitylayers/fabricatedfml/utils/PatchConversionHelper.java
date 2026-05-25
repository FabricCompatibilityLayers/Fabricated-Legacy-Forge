package io.github.fabriccompatibilitylayers.fabricatedfml.utils;

import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.MappingsHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.util.Annotations;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PatchConversionHelper {
    private static final String MAKE_STATIC_DESC =
            "L" + MakeStatic.class.getName().replace('.', '/') + ";";
    private static final String SHADOW_DESC =
            "Lorg/spongepowered/asm/mixin/Shadow;";

    /**
     * Called from the MixinPlugin's {@code preApply} hook.
     *
     * <p>Loads the mixin class and finds every {@code @Shadow @MakeStatic} field.
     * For each such field the matching field in {@code targetClass} has its
     * {@code ACC_STATIC} flag set (and {@code ACC_FINAL} cleared) so that Mixin's
     * shadow validator accepts the {@code static} shadow declaration that must be
     * present in the mixin class.
     */
    public static void preApply(String mixinClassName, ClassNode targetClass) {
        ClassNode mixinNode = new ClassNode();

        try {
            new ClassReader(mixinClassName.replace('.', '/'))
                    .accept(mixinNode, ClassReader.SKIP_FRAMES);
        } catch (IOException e) {
            return;
        }

        for (FieldNode mixinField : mixinNode.fields) {
            if (!hasAnnotation(mixinField.visibleAnnotations, MAKE_STATIC_DESC)) continue;

            String targetName = getShadowTargetName(mixinField);

            for (FieldNode targetField : targetClass.fields) {
                if (targetField.name.equals(targetName) && targetField.desc.equals(mixinField.desc)) {
                    targetField.access |= Opcodes.ACC_STATIC;
                    targetField.access &= ~Opcodes.ACC_FINAL;
                    break;
                }
            }
        }
    }

    /**
     * Called from the MixinPlugin's {@code postApply} hook.
     *
     * <p>Performs two passes over the target class:
     * <ol>
     *   <li>Renames methods annotated with {@link ServerImplementation} to their
     *       server-side mapped name (pre-existing behaviour).</li>
     *   <li>Rewrites {@code GETFIELD}/{@code PUTFIELD} instructions that reference
     *       fields now marked {@code static} into {@code GETSTATIC}/{@code PUTSTATIC},
     *       adjusting the operand stack so it stays consistent.</li>
     * </ol>
     */
    public static void postApply(ClassNode targetClass) {
        String name = targetClass.name;

        for (MethodNode method : targetClass.methods) {
            AnnotationNode node = Annotations.getVisible(method, ServerImplementation.class);

            if (node != null) {
                String originalName = Annotations.getValue(node);
                method.name = MappingsHelper.mapMethodFromRemappedClass(name, originalName, MappingsHelper.mapDescriptor(method.desc)).getName();
            }
        }

        fixStaticFieldAccess(targetClass);
    }

    /**
     * Scans every method in {@code targetClass} for field instructions that access a
     * field that is now static.  Such instructions must have been emitted when the
     * field was still an instance field, so their opcodes and stack discipline are
     * wrong.  This method corrects them in place.
     *
     * <h3>GETFIELD → GETSTATIC</h3>
     * <pre>
     *   Stack before: ..., objectref
     *   GETFIELD pops objectref, pushes value
     *
     *   Replacement:
     *     POP        // discard objectref
     *     GETSTATIC  // push value without consuming anything
     * </pre>
     *
     * <h3>PUTFIELD → PUTSTATIC (category-1 value)</h3>
     * <pre>
     *   Stack before: ..., objectref, value
     *
     *   Replacement:
     *     SWAP       // ..., value, objectref
     *     POP        // ..., value
     *     PUTSTATIC  // pops value
     * </pre>
     *
     * <h3>PUTFIELD → PUTSTATIC (category-2 value: long / double)</h3>
     * <pre>
     *   Stack before: ..., objectref, value(cat2)
     *
     *   Replacement:
     *     DUP2_X1    // ..., value(cat2), objectref, value(cat2)
     *     POP2       // ..., value(cat2), objectref
     *     POP        // ..., value(cat2)
     *     PUTSTATIC  // pops value
     * </pre>
     */
    private static void fixStaticFieldAccess(ClassNode targetClass) {
        // Build a quick-lookup set of "name:desc" keys for all static fields.
        Set<String> staticFields = new HashSet<>();

        for (FieldNode field : targetClass.fields) {
            if ((field.access & Opcodes.ACC_STATIC) != 0) {
                staticFields.add(field.name + ":" + field.desc);
            }
        }

        for (MethodNode method : targetClass.methods) {
            // toArray() gives a snapshot so we can safely mutate the instruction list.
            for (AbstractInsnNode insn : method.instructions.toArray()) {
                if (!(insn instanceof FieldInsnNode)) continue;

                FieldInsnNode fi = (FieldInsnNode) insn;

                // Only fix accesses to fields declared on this class.
                if (!fi.owner.equals(targetClass.name)) continue;
                if (!staticFields.contains(fi.name + ":" + fi.desc)) continue;

                if (fi.getOpcode() == Opcodes.GETFIELD) {
                    method.instructions.insertBefore(fi, new InsnNode(Opcodes.POP));
                    fi.setOpcode(Opcodes.GETSTATIC);
                } else if (fi.getOpcode() == Opcodes.PUTFIELD) {
                    boolean isCategory2 = "J".equals(fi.desc) || "D".equals(fi.desc);

                    if (isCategory2) {
                        method.instructions.insertBefore(fi, new InsnNode(Opcodes.DUP2_X1));
                        method.instructions.insertBefore(fi, new InsnNode(Opcodes.POP2));
                        method.instructions.insertBefore(fi, new InsnNode(Opcodes.POP));
                    } else {
                        method.instructions.insertBefore(fi, new InsnNode(Opcodes.SWAP));
                        method.instructions.insertBefore(fi, new InsnNode(Opcodes.POP));
                    }

                    fi.setOpcode(Opcodes.PUTSTATIC);
                }
            }
        }
    }

    private static boolean hasAnnotation(List<AnnotationNode> annotations, String desc) {
        if (annotations == null) return false;

        for (AnnotationNode ann : annotations) {
            if (desc.equals(ann.desc)) return true;
        }

        return false;
    }

    /**
     * Returns the target field name that a {@code @Shadow}-annotated mixin field
     * corresponds to.  Strips the Shadow prefix (default {@code "shadow$"}, or
     * whatever is declared in {@code @Shadow(prefix = "...")}) when present.
     */
    private static String getShadowTargetName(FieldNode field) {
        if (field.visibleAnnotations == null) return field.name;

        for (AnnotationNode ann : field.visibleAnnotations) {
            if (!SHADOW_DESC.equals(ann.desc)) continue;

            String prefix = "shadow$"; // Mixin's built-in default

            if (ann.values != null) {
                for (int i = 0; i + 1 < ann.values.size(); i += 2) {
                    if ("prefix".equals(ann.values.get(i))) {
                        prefix = (String) ann.values.get(i + 1);
                    }
                }
            }

            return field.name.startsWith(prefix) ? field.name.substring(prefix.length()) : field.name;
        }

        return field.name;
    }
}