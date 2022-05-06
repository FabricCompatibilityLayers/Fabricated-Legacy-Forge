package cpw.mods.fml.common.asm;

import cpw.mods.fml.common.registry.BlockProxy;
import cpw.mods.fml.relauncher.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

public class ASMTransformer implements IClassTransformer {
    public ASMTransformer() {
    }

    public byte[] transform(String name, byte[] bytes) {
        if ("net.minecraft.src.Block".equals(name)) {
            ClassReader cr = new ClassReader(bytes);
            ClassNode cn = new ClassNode(262144);
            cr.accept(cn, 8);
            cn.interfaces.add(Type.getInternalName(BlockProxy.class));
            ClassWriter cw = new ClassWriter(3);
            cn.accept(cw);
            return cw.toByteArray();
        } else {
            return bytes;
        }
    }
}
