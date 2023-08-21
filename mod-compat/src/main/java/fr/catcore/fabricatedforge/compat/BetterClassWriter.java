package fr.catcore.fabricatedforge.compat;

import net.fabricmc.loader.api.FabricLoader;
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
    protected String getCommonSuperClass(String type1, String type2) {
        try {
            return super.getCommonSuperClass(type1, type2);
        } catch (TypeNotPresentException e) {
            System.out.println("Common of: " + type1 + " " + type2);
            e.printStackTrace();
            return "java/lang/Object";
        }
    }

    @Override
    protected ClassLoader getClassLoader() {
        return FabricLoader.getInstance().getClass().getClassLoader();
    }
}
