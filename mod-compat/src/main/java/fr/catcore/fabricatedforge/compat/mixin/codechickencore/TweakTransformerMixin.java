package fr.catcore.fabricatedforge.compat.mixin.codechickencore;

import codechicken.core.asm.*;
import codechicken.core.config.ConfigFile;
import codechicken.core.config.ConfigTag;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.util.List;

@Mixin(TweakTransformer.class)
public abstract class TweakTransformerMixin {

//    @Shadow(remap = false) public static ConfigTag tweaks;
//
//    @Shadow(remap = false)
//    private static void alterMethod(ASMHelper.MethodAltercator ma) {
//    }
//
//    /**
//     * @author CatCore
//     * @reason fix mappings
//     */
//    @Overwrite(remap = false)
//    public static void load() {
//        File cfgDir = new File(CodeChickenCorePlugin.minecraftDir + "/config");
//        if (!cfgDir.exists()) {
//            cfgDir.mkdirs();
//        }
//
//        ConfigFile config = new ConfigFile(new File(cfgDir, "CodeChickenCore.cfg")).setComment("CodeChickenCore configuration file.");
//        tweaks = config.getTag("tweaks").setComment("Various tweaks that can be applied to game mechanics.").useBraces();
//        if (!tweaks.getTag("persistantLava")
//                .setComment("Set to false to make lava fade away like water if all the source blocks are destroyed")
//                .getBooleanValue(true)) {
//            ObfuscationMappings.ClassMapping blockFlowing = new ObfuscationMappings.ClassMapping("aky");
//            alterMethod(
//                    new ASMHelper.MethodAltercator(new ObfuscationMappings.DescriptorMapping(blockFlowing.s_class, new ObfuscationMappings.DescriptorMapping("amq", "b", "(Lyc;IIILjava/util/Random;)V"))) {
//                        public void alter(MethodNode mv) {
//                            InsnList needle = new InsnList();
//                            needle.add(new VarInsnNode(21, 6));
//                            needle.add(new VarInsnNode(54, -1));
//                            needle.add(new InsnNode(3));
//                            needle.add(new VarInsnNode(54, 8));
//                            List lists = InstructionComparator.insnListFindL(mv.instructions, needle);
//                            if (lists.size() != 1) {
//                                throw new RuntimeException(
//                                        "Needle found " + lists.size() + " times in Haystack: " + mv.instructions + "\n" + ASMHelper.printInsnList(needle)
//                                );
//                            } else {
//                                InstructionComparator.InsnListSection subsection = (InstructionComparator.InsnListSection)lists.get(0);
//                                AbstractInsnNode insn = subsection.first;
//
//                                while(true) {
//                                    AbstractInsnNode next = insn.getNext();
//                                    mv.instructions.remove(insn);
//                                    if (insn == subsection.last) {
//                                        return;
//                                    }
//
//                                    insn = next;
//                                }
//                            }
//                        }
//                    }
//            );
//        }
//
//        if (tweaks.getTag("environmentallyFriendlyCreepers")
//                .setComment("If set to true, creepers will not destroy landscape. (A version of mobGreifing setting just for creepers)")
//                .getBooleanValue(false)) {
//            final ObfuscationMappings.ClassMapping entityCreeper = new ObfuscationMappings.ClassMapping("qc");
//            alterMethod(
//                    new ASMHelper.MethodAltercator(new ObfuscationMappings.DescriptorMapping(entityCreeper.s_class, new ObfuscationMappings.DescriptorMapping("lq", "j_", "()V"))) {
//                        public void alter(MethodNode mv) {
//                            InsnList needle = new InsnList();
//                            needle.add(new VarInsnNode(25, 0));
//                            needle.add(new ObfuscationMappings.DescriptorMapping(entityCreeper.s_class, new ObfuscationMappings.DescriptorMapping("lq", "p", "Lyc;")).toFieldInsn(180));
//                            needle.add(new ObfuscationMappings.DescriptorMapping("yc", "L", "()Lxz;").toInsn(182));
//                            needle.add(new LdcInsnNode("mobGriefing"));
//                            needle.add(new ObfuscationMappings.DescriptorMapping("xz", "b", "(Ljava/lang/String;)Z").toInsn(182));
//                            List lists = InstructionComparator.insnListFindL(mv.instructions, needle);
//                            if (lists.size() != 1) {
//                                throw new RuntimeException(
//                                        "Needle found " + lists.size() + " times in Haystack: " + mv.instructions + "\n" + ASMHelper.printInsnList(needle)
//                                );
//                            } else {
//                                InstructionComparator.InsnListSection subsection = (InstructionComparator.InsnListSection)lists.get(0);
//                                mv.instructions.insertBefore(subsection.first, new InsnNode(3));
//                                AbstractInsnNode insn = subsection.first;
//
//                                while(true) {
//                                    AbstractInsnNode next = insn.getNext();
//                                    mv.instructions.remove(insn);
//                                    if (insn == subsection.last) {
//                                        return;
//                                    }
//
//                                    insn = next;
//                                }
//                            }
//                        }
//                    }
//            );
//        }
//
//        if (!tweaks.getTag("softLeafReplace").setComment("If set to false, leaves will only replace air when growing").getBooleanValue(false)) {
//            alterMethod(new ASMHelper.MethodAltercator(new ObfuscationMappings.DescriptorMapping("amq", "canBeReplacedByLeaves", "(Lyc;III)Z")) {
//                public void alter(MethodNode mv) {
//                    InsnList replacement = new InsnList();
//                    replacement.add(new VarInsnNode(25, 0));
//                    replacement.add(new VarInsnNode(25, 1));
//                    replacement.add(new VarInsnNode(21, 2));
//                    replacement.add(new VarInsnNode(21, 3));
//                    replacement.add(new VarInsnNode(21, 4));
//                    replacement.add(new ObfuscationMappings.DescriptorMapping("amq", "isAirBlock", "(Lyc;III)Z").toInsn(182));
//                    replacement.add(new InsnNode(172));
//                    mv.instructions = replacement;
//                }
//            });
//        }
//
//        if (tweaks.getTag("doFireTickOut")
//                .setComment("If set to true and doFireTick is disabed in the game rules, fire will still dissipate if it's not over a fire source")
//                .getBooleanValue(true)) {
//            alterMethod(
//                    new ASMHelper.MethodAltercator(new ObfuscationMappings.DescriptorMapping("akf", "b", "(Lyc;IIILjava/util/Random;)V")) {
//                        public void alter(MethodNode mv) {
//                            InsnList needle = new InsnList();
//                            needle.add(new LdcInsnNode("doFireTick"));
//                            needle.add(new ObfuscationMappings.DescriptorMapping("xz", "b", "(Ljava/lang/String;)Z").toInsn(182));
//                            needle.add(new JumpInsnNode(153, new LabelNode()));
//                            List lists = InstructionComparator.insnListFindL(mv.instructions, needle);
//                            if (lists.size() != 1) {
//                                throw new RuntimeException(
//                                        "Needle found " + lists.size() + " times in Haystack: " + mv.instructions + "\n" + ASMHelper.printInsnList(needle)
//                                );
//                            } else {
//                                InstructionComparator.InsnListSection subsection = (InstructionComparator.InsnListSection)lists.get(0);
//                                LabelNode jlabel = ((JumpInsnNode)subsection.last).label;
//                                LabelNode ret = new LabelNode();
//                                mv.instructions.insertBefore(jlabel, new JumpInsnNode(167, ret));
//                                InsnList inject = new InsnList();
//                                inject.add(new VarInsnNode(25, 1));
//                                inject.add(new VarInsnNode(21, 2));
//                                inject.add(new VarInsnNode(21, 3));
//                                inject.add(new VarInsnNode(21, 4));
//                                inject.add(new VarInsnNode(25, 5));
//                                inject.add(
//                                        new ObfuscationMappings.DescriptorMapping("codechicken/core/featurehack/TweakTransformerHelper", "quenchFireTick", "(Lyc;IIILjava/util/Random;)V").toInsn(184)
//                                );
//                                inject.add(ret);
//                                mv.instructions.insert(jlabel, inject);
//                            }
//                        }
//                    }
//            );
//        }
//    }
}
