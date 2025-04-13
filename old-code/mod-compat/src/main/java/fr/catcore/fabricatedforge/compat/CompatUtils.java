package fr.catcore.fabricatedforge.compat;

import fr.catcore.fabricatedforge.compat.asm.BetterClassWriter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public class CompatUtils {
    public static ClassNode createNode(byte[] bytes, int parsingOptions) {
        ClassNode node = new ClassNode(Opcodes.ASM4);
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, parsingOptions);

        return node;
    }

    public static byte[] writeClass(ClassNode node, int flags) {
        BetterClassWriter classWriter = new BetterClassWriter(flags);
        node.accept(classWriter);

        return classWriter.toByteArray();
    }
}
