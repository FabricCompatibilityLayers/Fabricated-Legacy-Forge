package fr.catcore.fabricatedforge.compat.mixin.nei;

import codechicken.core.asm.ASMHelper;
import codechicken.core.asm.ClassHeirachyManager;
import codechicken.core.asm.InstructionComparator;
import codechicken.core.asm.ObfuscationMappings;
import codechicken.nei.asm.NEITransformer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(NEITransformer.class)
public class NEITransformerMixin {
//    /**
//     * @author CatCore
//     * @reason E
//     */
//    @Overwrite(remap = false)
//    public byte[] transformer001(String name, byte[] bytes) {
//        ObfuscationMappings.ClassMapping classmap = new ObfuscationMappings.ClassMapping("avf");
//
//        if (ClassHeirachyManager.classExtends(name, classmap.javaClass(), bytes)) {
//            ClassNode node = ASMHelper.createClassNode(bytes);
//
//            ObfuscationMappings.DescriptorMapping methodmap = new ObfuscationMappings.DescriptorMapping("aul", "c", "()V");
//            ObfuscationMappings.DescriptorMapping supermap = new ObfuscationMappings.DescriptorMapping(node.superName, methodmap);
//            InsnList supercall = new InsnList();
//            supercall.add(new VarInsnNode(25, 0));
//            supercall.add(supermap.toInsn(183));
//
//            for(MethodNode methodnode : node.methods) {
//                if (methodmap.matches(methodnode)) {
//                    InsnList importantNodeList = InstructionComparator.getImportantList(methodnode.instructions);
//
//                    if (!InstructionComparator.insnListMatches(importantNodeList, supercall, 0)) {
//                        methodnode.instructions.insertBefore(methodnode.instructions.getFirst(), supercall);
//                        System.out.println("Inserted super call into " + name + "." + supermap.s_name);
//                    }
//                }
//            }
//
//            bytes = ASMHelper.createBytes(node, 3);
//        }
//
//        return bytes;
//    }
}
