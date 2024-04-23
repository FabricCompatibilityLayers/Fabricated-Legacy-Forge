package fr.catcore.fabricatedforge.compat.asm;

import fr.catcore.modremapperapi.api.IClassTransformer;
import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ASMRemapperTransformer implements IClassTransformer, Opcodes {
    private final static String[] EXCLUDED = new String[]{
            "fr.catcore.",
            "cpw.mods.fml.",
            "net.minecraftforge.",
            "org.objectweb.asm.",
            "net.fabricmc.",
            "com.llamalad7.",
            "org.spongepowered.",
            "org.lwjgl."
    };

    private final static Map<String, String> INSTANCE_REPLACER = arrayToMap(new String[][]{
            {"org/objectweb/asm/tree/FieldInsnNode", "fr/catcore/fabricatedforge/compat/asm/BetterFieldInsnNode"},
            {"org/objectweb/asm/tree/MethodInsnNode", "fr/catcore/fabricatedforge/compat/asm/BetterMethodInsnNode"},
            {"org/objectweb/asm/ClassWriter", "fr/catcore/fabricatedforge/compat/asm/BetterClassWriter"}
    });

    private final static Map<String, Map<String, Map<String, ClassAwareMember>>> CALL_REPLACER = new HashMap<>();

    private final static List<String> TRANSFORMED = new ArrayList<>();

    private static void registerCallReplacer(String className, Object... args) {
        Map<String, Map<String, ClassAwareMember>> classMap = CALL_REPLACER.compute(className, (s, stringMapMap) -> new HashMap<>());

        for (int i = 0; i < args.length; i++) {
            String name = (String) args[i];
            String desc = (String) args[i + 1];
            ClassAwareMember member = (ClassAwareMember) args[i + 2];

            Map<String, ClassAwareMember> methodMap = classMap.compute(name, (s, stringClassAwareMemberMap) -> new HashMap<>());
            methodMap.put(desc, member);

            i += 2;
        }
    }

    static {
        registerCallReplacer("java/lang/Class",
                "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", new ClassAwareMember(
                        "fr/catcore/fabricatedforge/compat/asm/RemapAwareClass",
                        "getDeclaredMethod",
                        "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;"
                    ),

                "getDeclaredField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;", new ClassAwareMember(
                        "fr/catcore/fabricatedforge/compat/asm/RemapAwareClass",
                        "getDeclaredField",
                        "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;"
                ),

                "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", new ClassAwareMember(
                        "fr/catcore/fabricatedforge/compat/asm/RemapAwareClass",
                        "getMethod",
                        "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;"
                ),

                "getField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;", new ClassAwareMember(
                        "fr/catcore/fabricatedforge/compat/asm/RemapAwareClass",
                        "getField",
                        "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;"
                ),

                "forName", "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;", new ClassAwareMember(
                        "fr/catcore/fabricatedforge/compat/asm/RemapAwareClass",
                        "forName",
                        "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;"
                ),

                "forName", "(Ljava/lang/String;)Ljava/lang/Class;", new ClassAwareMember(
                        "fr/catcore/fabricatedforge/compat/asm/RemapAwareClass",
                        "forName",
                        "(Ljava/lang/String;)Ljava/lang/Class;"
                )
        );
    }

    @Override
    public boolean handlesClass(String s, String s1) {
        for (String exclude : EXCLUDED) {
            if (s.startsWith(exclude)) return false;
        }

        return true;
    }

    @Override
    public byte[] transformClass(String s, String s1, byte[] bytes) {
        if (TRANSFORMED.contains(s)) return bytes;
        TRANSFORMED.add(s);

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        boolean touched = false;

        for (MethodNode methodNode : classNode.methods) {
            AbstractInsnNode insnNode = methodNode.instructions.getFirst();

            if (insnNode != null) {
                while (insnNode != null) {
                    if (insnNode.getOpcode() == NEW && insnNode instanceof TypeInsnNode) {
                        TypeInsnNode typeInsnNode = (TypeInsnNode) insnNode;

                        if (INSTANCE_REPLACER.containsKey(typeInsnNode.desc)) {
                            typeInsnNode.desc = INSTANCE_REPLACER.get(typeInsnNode.desc);
                            touched = true;
                        }
                    } else if (insnNode instanceof MethodInsnNode) {
                        MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;

                        if (methodInsnNode.getOpcode() == INVOKESPECIAL) {
                            if (methodInsnNode.name.equals("<init>") && INSTANCE_REPLACER.containsKey(methodInsnNode.owner)) {
                                methodInsnNode.owner = INSTANCE_REPLACER.get(methodInsnNode.owner);
                                touched = true;
                            }
                        } else if (methodInsnNode.getOpcode() == INVOKEVIRTUAL || methodInsnNode.getOpcode() == INVOKESTATIC) {
                            if (CALL_REPLACER.containsKey(methodInsnNode.owner)) {
                                Map<String, Map<String, ClassAwareMember>> classMethods = CALL_REPLACER.get(methodInsnNode.owner);

                                if (classMethods.containsKey(methodInsnNode.name)) {
                                    Map<String, ClassAwareMember> classMethod = classMethods.get(methodInsnNode.name);

                                    if (classMethod.containsKey(methodInsnNode.desc)) {
                                        ClassAwareMember classAwareMember = classMethod.get(methodInsnNode.desc);
                                        methodInsnNode.setOpcode(INVOKESTATIC);
                                        methodInsnNode.owner = classAwareMember.owner;
                                        methodInsnNode.name = classAwareMember.name;
                                        methodInsnNode.desc = classAwareMember.desc;
                                        touched = true;
                                    }
                                }
                            }
                        }
                    }

                    insnNode = insnNode.getNext();
                }
            }
        }

        if (touched) {
            ClassWriter classWriter = new BetterClassWriter(3);
            classNode.accept(classWriter);

            bytes = classWriter.toByteArray();
            System.out.println("ASMRemapper transformed: " + s);
        }

        return bytes;
    }

    private static Map<String, String> arrayToMap(String[][] arrays) {
        Map<String, String> map = new HashMap<>();

        for (String[] array : arrays) {
            map.put(array[0], array[1]);
        }

        return map;
    }

    public static class ClassAwareMember extends MappingUtils.ClassMember {
        public final String owner;
        public ClassAwareMember(String owner, String name, @Nullable String desc) {
            super(name, desc);
            this.owner = owner;
        }
    }
}
