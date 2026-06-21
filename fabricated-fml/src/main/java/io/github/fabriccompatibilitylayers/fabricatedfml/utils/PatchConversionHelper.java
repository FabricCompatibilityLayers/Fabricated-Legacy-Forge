/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.utils;

import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.MappingsHelper;
import io.github.fabriccompatibilitylayers.modremappingapi.api.v2.MappingUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.util.Annotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PatchConversionHelper {
    private static final String MAKE_STATIC_DESC =
            "L" + MakeStatic.class.getName().replace('.', '/') + ";";
    private static final String WIDENED_OVERLOAD_DESC =
            "L" + WidenedOverload.class.getName().replace('.', '/') + ";";
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
    public static void preApply(ClassNode mixinNode, ClassNode targetClass) {
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
        processWidenedOverloads(targetClass);
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

    /**
     * For each method in {@code targetClass} annotated with {@link WidenedOverload}:
     * <ol>
     *   <li>Resolves the narrow method's name and descriptor through {@code MappingsHelper}.</li>
     *   <li>Copies the narrow method's instructions into the wide (annotated) method, replacing
     *       the placeholder mixin body.</li>
     *   <li>Replaces the narrow method's body with a single delegate call to the wide method
     *       (no cast required — all argument conversions are implicit widenings on the JVM).</li>
     *   <li>Strips the annotation from the wide method.</li>
     * </ol>
     *
     * <h3>Generated narrow-method body</h3>
     * <pre>
     *   // original narrow: void foo(SubType a, int b)
     *   // wide (mixin):    void foo(SuperType a, int b)
     *   void foo(SubType a, int b) {
     *       this.foo(a, b);  // widening — no CHECKCAST emitted
     *   }
     * </pre>
     */
    private static void processWidenedOverloads(ClassNode targetClass) {
        for (MethodNode method : targetClass.methods) {
            AnnotationNode ann = Annotations.getVisible(method, WidenedOverload.class);
            if (ann == null) continue;

            String narrowName = null;
            String narrowDesc = null;

            if (ann.values != null) {
                for (int i = 0; i + 1 < ann.values.size(); i += 2) {
                    String key = (String) ann.values.get(i);

                    if ("name".equals(key)) narrowName = (String) ann.values.get(i + 1);
                    else if ("desc".equals(key)) narrowDesc = (String) ann.values.get(i + 1);
                }
            }

            if (narrowName == null || narrowDesc == null) continue;

            MappingUtils.ClassMember mapped = MappingsHelper.mapMethodFromRemappedClass(targetClass.name, narrowName, narrowDesc);

            MethodNode narrowMethod = null;

            for (MethodNode candidate : targetClass.methods) {
                if (candidate.name.equals(mapped.getName()) && candidate.desc.equals(mapped.getDesc())) {
                    narrowMethod = candidate;
                    break;
                }
            }

            if (narrowMethod == null) continue;

            copyInstructions(narrowMethod, method);
            rewriteNarrowTypeRefs(method, narrowMethod.desc, method.desc);
            replaceWithWideDelegate(targetClass, narrowMethod, method);

            if (method.visibleAnnotations != null) {
                method.visibleAnnotations.removeIf(a -> WIDENED_OVERLOAD_DESC.equals(a.desc));
            }
        }
    }

    /**
     * Deep-copies the instruction list (and associated try-catch blocks and local variable table)
     * from {@code source} into {@code dest}, replacing whatever body {@code dest} had.
     * Labels are cloned with a fresh mapping so the two methods remain independent.
     */
    private static void copyInstructions(MethodNode source, MethodNode dest) {
        dest.instructions.clear();

        if (dest.tryCatchBlocks != null) dest.tryCatchBlocks.clear();
        else dest.tryCatchBlocks = new ArrayList<>();

        dest.localVariables = null;
        dest.maxLocals = source.maxLocals;
        dest.maxStack = source.maxStack;

        Map<LabelNode, LabelNode> labelMap = new HashMap<>();

        for (AbstractInsnNode insn : source.instructions) {
            if (insn instanceof LabelNode) {
                labelMap.put((LabelNode) insn, new LabelNode());
            }
        }

        for (AbstractInsnNode insn : source.instructions) {
            dest.instructions.add(insn.clone(labelMap));
        }

        if (source.tryCatchBlocks != null) {
            for (TryCatchBlockNode tcb : source.tryCatchBlocks) {
                dest.tryCatchBlocks.add(new TryCatchBlockNode(
                        labelMap.get(tcb.start),
                        labelMap.get(tcb.end),
                        labelMap.get(tcb.handler),
                        tcb.type));
            }
        }

        if (source.localVariables != null) {
            dest.localVariables = new ArrayList<>();

            for (LocalVariableNode lv : source.localVariables) {
                dest.localVariables.add(new LocalVariableNode(
                        lv.name, lv.desc, lv.signature,
                        labelMap.get(lv.start), labelMap.get(lv.end), lv.index));
            }
        }
    }

    /**
     * Rewrites every instruction operand in {@code method} that still names a narrow parameter
     * type (present in {@code narrowDesc} but replaced by a supertype in {@code wideDesc}) with
     * the corresponding wide type.  This must be called after copying narrow-method instructions
     * into the wide method so the copied body refers to the correct types.
     *
     * <p>Covered instruction forms:
     * <ul>
     *   <li>CHECKCAST / INSTANCEOF / ANEWARRAY / NEW — {@link TypeInsnNode#desc}</li>
     *   <li>INVOKEVIRTUAL / INVOKESPECIAL / etc. — {@link MethodInsnNode#owner} and {@code desc}</li>
     *   <li>GETFIELD / PUTFIELD / etc. — {@link FieldInsnNode#owner} and {@code desc}</li>
     *   <li>MULTIANEWARRAY — {@link MultiANewArrayInsnNode#desc}</li>
     *   <li>Exception-table handler types — {@link TryCatchBlockNode#type}</li>
     *   <li>Local-variable table descriptors — {@link LocalVariableNode#desc}</li>
     * </ul>
     */
    private static void rewriteNarrowTypeRefs(MethodNode method, String narrowDesc, String wideDesc) {
        Type[] narrowArgs = Type.getArgumentTypes(narrowDesc);
        Type[] wideArgs = Type.getArgumentTypes(wideDesc);

        Map<String, String> typeMap = new HashMap<>();

        for (int i = 0; i < narrowArgs.length && i < wideArgs.length; i++) {
            if (narrowArgs[i].getSort() == Type.OBJECT && wideArgs[i].getSort() == Type.OBJECT) {
                String narrow = narrowArgs[i].getInternalName();
                String wide = wideArgs[i].getInternalName();

                if (!narrow.equals(wide)) typeMap.put(narrow, wide);
            }
        }

        if (typeMap.isEmpty()) return;

        for (AbstractInsnNode insn : method.instructions) {
            if (insn instanceof TypeInsnNode) {
                TypeInsnNode ti = (TypeInsnNode) insn;
                ti.desc = remapTypeInsnDesc(ti.desc, typeMap);
            } else if (insn instanceof MethodInsnNode) {
                MethodInsnNode mi = (MethodInsnNode) insn;
                String mapped = typeMap.get(mi.owner);
                if (mapped != null) mi.owner = mapped;
                mi.desc = remapDescriptor(mi.desc, typeMap);
            } else if (insn instanceof FieldInsnNode) {
                FieldInsnNode fi = (FieldInsnNode) insn;
                String mapped = typeMap.get(fi.owner);
                if (mapped != null) fi.owner = mapped;
                fi.desc = remapDescriptor(fi.desc, typeMap);
            } else if (insn instanceof MultiANewArrayInsnNode) {
                MultiANewArrayInsnNode ma = (MultiANewArrayInsnNode) insn;
                ma.desc = remapDescriptor(ma.desc, typeMap);
            }
        }

        if (method.tryCatchBlocks != null) {
            for (TryCatchBlockNode tcb : method.tryCatchBlocks) {
                if (tcb.type != null) {
                    String mapped = typeMap.get(tcb.type);
                    if (mapped != null) tcb.type = mapped;
                }
            }
        }

        if (method.localVariables != null) {
            for (LocalVariableNode lv : method.localVariables) {
                lv.desc = remapDescriptor(lv.desc, typeMap);
            }
        }
    }

    // TypeInsnNode.desc is a plain internal name for class types, but a descriptor for arrays.
    private static String remapTypeInsnDesc(String desc, Map<String, String> typeMap) {
        if (desc.startsWith("[")) return remapDescriptor(desc, typeMap);
        String mapped = typeMap.get(desc);
        return mapped != null ? mapped : desc;
    }

    // Replaces every L<narrowName>; with L<wideName>; in an ASM descriptor string.
    private static String remapDescriptor(String desc, Map<String, String> typeMap) {
        for (Map.Entry<String, String> entry : typeMap.entrySet()) {
            desc = desc.replace("L" + entry.getKey() + ";", "L" + entry.getValue() + ";");
        }

        return desc;
    }

    /**
     * Replaces the body of {@code narrow} with a delegate call to {@code wide}.
     * All arguments are forwarded using their load opcode from the narrow descriptor.
     * No explicit cast instruction is needed because each narrow argument type is a subtype
     * of the corresponding wide argument type (widening reference conversions are implicit).
     */
    private static void replaceWithWideDelegate(ClassNode owner, MethodNode narrow, MethodNode wide) {
        boolean isStatic = (narrow.access & Opcodes.ACC_STATIC) != 0;

        narrow.instructions.clear();

        if (narrow.tryCatchBlocks != null) narrow.tryCatchBlocks.clear();

        narrow.localVariables = null;

        InsnList insns = new InsnList();
        int slot = 0;

        if (!isStatic) {
            insns.add(new VarInsnNode(Opcodes.ALOAD, 0));
            slot = 1;
        }

        for (Type argType : Type.getArgumentTypes(narrow.desc)) {
            insns.add(new VarInsnNode(argType.getOpcode(Opcodes.ILOAD), slot));
            slot += argType.getSize();
        }

        int invoke = isStatic ? Opcodes.INVOKESTATIC : Opcodes.INVOKEVIRTUAL;
        insns.add(new MethodInsnNode(invoke, owner.name, wide.name, wide.desc, false));

        Type returnType = Type.getReturnType(narrow.desc);
        insns.add(new InsnNode(returnType.getOpcode(Opcodes.IRETURN)));

        narrow.instructions.add(insns);
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