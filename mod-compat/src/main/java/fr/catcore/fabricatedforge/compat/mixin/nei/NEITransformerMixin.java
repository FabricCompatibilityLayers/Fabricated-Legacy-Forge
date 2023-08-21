package fr.catcore.fabricatedforge.compat.mixin.nei;

import codechicken.core.asm.ClassHeirachyManager;
import codechicken.core.asm.InstructionComparator;
import codechicken.core.asm.ObfuscationManager;
import codechicken.nei.asm.NEITransformer;
import fr.catcore.fabricatedforge.compat.CompatUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(NEITransformer.class)
public class NEITransformerMixin {
    /**
     * @author CatCore
     * @reason fixer classwriter
     */
    @Overwrite(remap = false)
    public byte[] transformer001(String name, byte[] bytes) {
        ObfuscationManager.ClassMapping classmap = new ObfuscationManager.ClassMapping("net.minecraft.src.GuiContainer");

        if (ClassHeirachyManager.classExtends(name, classmap.classname, bytes)) {
            ClassNode node = CompatUtils.createNode(bytes, 0);

            ObfuscationManager.MethodMapping methodmap = new ObfuscationManager.MethodMapping("net.minecraft.src.GuiScreen", "updateScreen", "()V");
            ObfuscationManager.MethodMapping supermap = new ObfuscationManager.MethodMapping(node.superName, methodmap);
            InsnList supercall = new InsnList();
            supercall.add(new VarInsnNode(25, 0));
            supercall.add(supermap.toInsn(183));

            for(MethodNode methodnode : node.methods) {
                if (methodmap.matches(methodnode)) {
                    InsnList importantNodeList = InstructionComparator.getImportantList(methodnode.instructions);

                    if (!InstructionComparator.insnListMatches(importantNodeList, supercall, 0)) {
                        methodnode.instructions.insertBefore(methodnode.instructions.getFirst(), supercall);
                        System.out.println("Inserted super call into " + name + "." + supermap.name);
                    }
                }
            }

            bytes = CompatUtils.writeClass(node, 3);
        }

        return bytes;
    }
}
