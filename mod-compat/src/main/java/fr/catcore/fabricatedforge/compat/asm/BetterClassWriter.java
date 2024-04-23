package fr.catcore.fabricatedforge.compat.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class BetterClassWriter extends ClassWriter {
    public BetterClassWriter(int flags) {
        super(flags);
    }

    public BetterClassWriter(ClassReader classReader, int flags) {
        super(classReader, flags);
    }

    @Override
    protected ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }
}
