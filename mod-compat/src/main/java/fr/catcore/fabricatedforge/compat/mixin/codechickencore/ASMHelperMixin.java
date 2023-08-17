package fr.catcore.fabricatedforge.compat.mixin.codechickencore;

import codechicken.core.asm.ASMHelper;
import fr.catcore.fabricatedforge.compat.BetterClassWriter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ASMHelper.class)
public class ASMHelperMixin {
    /**
     * @author E
     * @reason E
     */
    @Overwrite(remap = false)
    public static ClassNode createClassNode(byte[] bytes) {
        ClassNode cnode = new ClassNode(Opcodes.ASM4);
        ClassReader reader = new ClassReader(bytes);
        reader.accept(cnode, 0);
        return cnode;
    }

    /**
     * @author E
     * @reason E
     */
    @Overwrite(remap = false)
    public static byte[] createBytes(ClassNode cnode, int i) {
        ClassWriter cw = new BetterClassWriter(i);
        cnode.accept(cw);
        return cw.toByteArray();
    }
}
