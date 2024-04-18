package fr.catcore.fabricatedforge.compat.mixin.codechickencore;

import codechicken.core.asm.ASMHelper;
import codechicken.core.asm.FeatureHackTransformer;
import codechicken.core.asm.InstructionComparator;
import codechicken.core.asm.ObfuscationMappings;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

@Mixin(FeatureHackTransformer.class)
public class FeatureHackTransformerMixin {
//    @Shadow(remap = false)
//    ObfuscationMappings.DescriptorMapping f_lastBrightness;
//
//    /**
//     * @author CatCore
//     * @reason fix mappings
//     */
//    @Overwrite(remap = false)
//    private byte[] transformer002(String name, byte[] bytes) {
//        ClassNode cnode = ASMHelper.createClassNode(bytes);
//        FieldNode fnode = ASMHelper.findField(this.f_lastBrightness, cnode);
//        if (fnode == null) {
//            cnode.fields.add(new FieldNode(9, this.f_lastBrightness.s_name, this.f_lastBrightness.s_desc, null, null));
//            MethodNode mlightmap = ASMHelper.findMethod(new ObfuscationMappings.DescriptorMapping("bfe", "a", "(IFF)V"), cnode);
//            InsnList hook = new InsnList();
//            LabelNode lend = new LabelNode();
//            hook.add(new VarInsnNode(21, 0));
//            hook.add(new ObfuscationMappings.DescriptorMapping("bfe", "b", "I").toFieldInsn(178));
//            hook.add(new JumpInsnNode(160, lend));
//            hook.add(new VarInsnNode(23, 2));
//            hook.add(new InsnNode(139));
//            hook.add(new IntInsnNode(16, 16));
//            hook.add(new InsnNode(120));
//            hook.add(new VarInsnNode(23, 1));
//            hook.add(new InsnNode(139));
//            hook.add(new InsnNode(128));
//            hook.add(this.f_lastBrightness.toFieldInsn(179));
//            hook.add(lend);
//            InsnList needle = new InsnList();
//            needle.add(new InsnNode(177));
//            List ret = InstructionComparator.insnListFindEnd(mlightmap.instructions, needle);
//            if (ret.size() != 1) {
//                throw new RuntimeException(
//                        "Needle not found in Haystack: " + ASMHelper.printInsnList(mlightmap.instructions) + "\n" + ASMHelper.printInsnList(needle)
//                );
//            }
//
//            mlightmap.instructions.insertBefore((AbstractInsnNode)ret.get(0), hook);
//            bytes = ASMHelper.createBytes(cnode, 3);
//            System.out.println("Brightness hook injected");
//        }
//
//        return bytes;
//    }
}
