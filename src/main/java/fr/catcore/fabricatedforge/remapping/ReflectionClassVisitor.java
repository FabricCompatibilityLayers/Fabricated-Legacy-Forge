package fr.catcore.fabricatedforge.remapping;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

public class ReflectionClassVisitor extends ClassVisitor {
    public ReflectionClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM9, classVisitor);
    }

    private static final Map<Entry, Entry> METHOD_OVERWRITES = new HashMap<>();

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return new ReflectionMethodVisitor(super.visitMethod(access, name, descriptor, signature, exceptions));
    }

    static {
        METHOD_OVERWRITES.put(new Entry(
                "setBurnProperties",
                "(III)V",
                "net/minecraft/class_197"
        ), new Entry(
                "Block_setBurnProperties",
                "(III)V",
                "fr/catcore/fabricatedforge/forged/ReflectionUtils"
        ));
    }

    public static class Entry {
        public final String name;
        public final String descriptor;
        public final String owner;

        public Entry(String name, String descriptor, String owner) {
            this.name = name;
            this.descriptor = descriptor;
            this.owner = owner;
        }
    }

    public static class ReflectionMethodVisitor extends MethodVisitor {

        public ReflectionMethodVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            for (Map.Entry<Entry, Entry> entry : METHOD_OVERWRITES.entrySet()) {
                Entry original = entry.getKey();
                Entry newEntry = entry.getValue();

                if (original.name.equals(name) && original.descriptor.equals(descriptor) && original.owner.equals(owner)) {
                    name = newEntry.name;
                    descriptor = newEntry.descriptor;
                    owner = newEntry.owner;
                    break;
                }
            }
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }
}
