package fr.catcore.fabricatedforge.compat.asm;

import fr.catcore.modremapperapi.api.IClassTransformer;
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

    private final static List<String> TRANSFORMED = new ArrayList<>();

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

        // Transform class here

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
}