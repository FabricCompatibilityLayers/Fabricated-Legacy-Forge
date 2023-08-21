package fr.catcore.fabricatedforge.compat.mixin.codechickencore;

import codechicken.core.asm.ASMHelper;
import fr.catcore.fabricatedforge.compat.BetterClassWriter;
import fr.catcore.fabricatedforge.compat.CompatUtils;
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
        return CompatUtils.createNode(bytes, 0);
    }

    /**
     * @author E
     * @reason E
     */
    @Overwrite(remap = false)
    public static byte[] createBytes(ClassNode cnode, int i) {
        return CompatUtils.writeClass(cnode, i);
    }
}
