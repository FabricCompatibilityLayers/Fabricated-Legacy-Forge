package fr.catcore.fabricatedforge.compat.asm;

import fr.catcore.modremapperapi.api.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.HashMap;
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

    @Override
    public boolean handlesClass(String s, String s1) {
        for (String exclude : EXCLUDED) {
            if (s.startsWith(exclude)) return false;
        }

        return true;
    }

    @Override
    public byte[] transformClass(String s, String s1, byte[] bytes) {
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
                    } else if (insnNode.getOpcode() == INVOKESPECIAL && insnNode instanceof MethodInsnNode) {
                        MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;

                        if (methodInsnNode.name.equals("<init>") && INSTANCE_REPLACER.containsKey(methodInsnNode.owner)) {
                            methodInsnNode.owner = INSTANCE_REPLACER.get(methodInsnNode.owner);
                            touched = true;
                        }
                    }

                    insnNode = insnNode.getNext();
                }
            }
        }

        if (touched) {
            ClassWriter classWriter = new ClassWriter(3);
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
}
