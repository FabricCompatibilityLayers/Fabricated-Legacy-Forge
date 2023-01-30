package fr.catcore.fabricatedforge.mixin;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.objectweb.asm.Opcodes.*;

public class FabricatedForgeMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        switch (targetClassName) {
            case "net.minecraft.class_582":
            case "net.minecraft.class_583":
            case "net.minecraft.class_585":
            case "net.minecraft.class_586":
            case "net.minecraft.class_587":
            case "net.minecraft.class_588":
            case "net.minecraft.class_589":
            case "net.minecraft.class_590":
                targetClass.superName = "cpw/mods/fml/client/FMLTextureFX";
                break;
            default:
                break;
        }
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        switch (targetClassName) {
            case "net.minecraft.class_582":
            case "net.minecraft.class_583":
            case "net.minecraft.class_585":
            case "net.minecraft.class_586":
            case "net.minecraft.class_587":
            case "net.minecraft.class_588":
            case "net.minecraft.class_589":
            case "net.minecraft.class_590":
                {
                    for (MethodNode node : targetClass.methods) {
                        if (Objects.equals(node.name, "<init>")) {
                            for (AbstractInsnNode insNode : node.instructions) {
                                if (insNode instanceof MethodInsnNode && insNode.getOpcode() == INVOKESPECIAL) {
                                    MethodInsnNode mTheNode = (MethodInsnNode) insNode;
                                    if (Objects.equals(mTheNode.owner, "net/minecraft/class_584"))
                                        mTheNode.owner = "cpw/mods/fml/client/FMLTextureFX";
                                }
                            }
                        }
                    }
                }
                break;
            case "net.minecraft.class_1041":
                {
                    // getNextID
                    MethodVisitor getNextIDVisitor = targetClass.visitMethod(ACC_PUBLIC + ACC_STATIC, "getNextID", "()I", null, null);
                    getNextIDVisitor.visitFieldInsn(GETSTATIC, "net/minecraft/class_1041", "field_4173", "[Lnet/minecraft/class_1041;");
                    getNextIDVisitor.visitInsn(ARRAYLENGTH);
                    getNextIDVisitor.visitInsn(IRETURN);
                    getNextIDVisitor.visitMaxs(1, 0);
                    getNextIDVisitor.visitEnd();

                    // <init>
                    MethodVisitor initVisitor = targetClass.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;)V", null, null);
                    Label l0 = new Label();
                    initVisitor.visitLabel(l0);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitMethodInsn(INVOKESTATIC, "net/minecraft/class_1041", "getNextID", "()I", false);
                    initVisitor.visitVarInsn(ALOAD, 1);
                    initVisitor.visitMethodInsn(INVOKESPECIAL, "net/minecraft/class_1041", "<init>", "(ILjava/lang/String;)V", false);
                    Label l1 = new Label();
                    initVisitor.visitLabel(l1);
                    initVisitor.visitInsn(RETURN);
                    Label l2 = new Label();
                    initVisitor.visitLabel(l2);
                    initVisitor.visitLocalVariable("this", "Lnet/minecraft/class_1041;", null, l0, l2, 0);
                    initVisitor.visitLocalVariable("label", "Ljava/lang/String;", null, l0, l2, 1);
                    initVisitor.visitMaxs(3, 2);
                    initVisitor.visitEnd();
                }
                break;
            case "net.minecraft.class_847":
                {
                    // <init>
                    MethodVisitor initVisitor = targetClass.visitMethod(ACC_PUBLIC, "<init>", "(Lnet/minecraft/class_1071;III)V", null, null);
                    Label l0 = new Label();
                    initVisitor.visitLabel(l0);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitVarInsn(ILOAD, 4);
                    initVisitor.visitMethodInsn(INVOKESPECIAL, "net/minecraft/class_846", "<init>", "(I)V", false);
                    Label l1 = new Label();
                    initVisitor.visitLabel(l1);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitVarInsn(ALOAD, 1);
                    initVisitor.visitFieldInsn(PUTFIELD, "net/minecraft/class_847", "itemStack", "Lnet/minecraft/class_1071;");
                    Label l2 = new Label();
                    initVisitor.visitLabel(l2);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitVarInsn(ILOAD, 2);
                    initVisitor.visitFieldInsn(PUTFIELD, "net/minecraft/class_847", "field_3107", "I");
                    Label l3 = new Label();
                    initVisitor.visitLabel(l3);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitVarInsn(ILOAD, 3);
                    initVisitor.visitFieldInsn(PUTFIELD, "net/minecraft/class_847", "field_3108", "I");
                    Label l4 = new Label();
                    initVisitor.visitLabel(l4);
                    initVisitor.visitInsn(RETURN);
                    Label l5 = new Label();
                    initVisitor.visitLabel(l5);
                    initVisitor.visitLocalVariable("this", "Lnet/minecraft/class_847;", null, l0, l5, 0);
                    initVisitor.visitLocalVariable("stack", "Lnet/minecraft/class_1071;", null, l0, l5, 1);
                    initVisitor.visitLocalVariable("min", "I", null, l0, l5, 2);
                    initVisitor.visitLocalVariable("max", "I", null, l0, l5, 3);
                    initVisitor.visitLocalVariable("weight", "I", null, l0, l5, 4);
                    initVisitor.visitMaxs(2, 5);
                    initVisitor.visitEnd();
                }
                break;
            case "net.minecraft.class_1196":
                {
                    MethodVisitor initVisitor = targetClass.visitMethod(ACC_PUBLIC, "<init>", "(Lnet/minecraft/class_1150;[B[BII)V", null, null);
                    Label l0 = new Label();
                    initVisitor.visitLabel(l0);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitVarInsn(ALOAD, 1);
                    initVisitor.visitVarInsn(ILOAD, 4);
                    initVisitor.visitVarInsn(ILOAD, 5);
                    initVisitor.visitMethodInsn(INVOKESPECIAL, "net/minecraft/class_1196", "<init>", "(Lnet/minecraft/class_1150;II)V", false);
                    Label l1 = new Label();
                    initVisitor.visitLabel(l1);
                    initVisitor.visitVarInsn(ALOAD, 2);
                    initVisitor.visitInsn(ARRAYLENGTH);
                    initVisitor.visitIntInsn(SIPUSH, 256);
                    initVisitor.visitInsn(IDIV);
                    initVisitor.visitVarInsn(ISTORE, 6);
                    Label l2 = new Label();
                    initVisitor.visitLabel(l2);
                    initVisitor.visitInsn(ICONST_0);
                    initVisitor.visitVarInsn(ISTORE, 7);
                    Label l3 = new Label();
                    Label l4 = new Label();
                    initVisitor.visitLabel(l3);
                    initVisitor.visitFrame(F_FULL, 8, new Object[]{
                            "net/minecraft/class_1196",
                            "net/minecraft/class_1150",
                            "[B", "[B", INTEGER, INTEGER, INTEGER, INTEGER
                    }, 0, new Object[0]);
                    initVisitor.visitVarInsn(ILOAD, 7);
                    initVisitor.visitIntInsn(BIPUSH, 16);
                    initVisitor.visitJumpInsn(IF_ICMPGE, l4);
                    Label l5 = new Label();
                    initVisitor.visitLabel(l5);
                    initVisitor.visitInsn(ICONST_0);
                    initVisitor.visitVarInsn(ISTORE, 8);
                    Label l6 = new Label();
                    Label l7 = new Label();
                    initVisitor.visitLabel(l6);
                    initVisitor.visitFrame(F_APPEND, 1, new Object[]{INTEGER}, 0, new Object[0]);
                    initVisitor.visitVarInsn(ILOAD, 8);
                    initVisitor.visitIntInsn(BIPUSH, 16);
                    initVisitor.visitJumpInsn(IF_ICMPGE, l7);
                    Label l8 = new Label();
                    initVisitor.visitLabel(l8);
                    initVisitor.visitInsn(ICONST_0);
                    initVisitor.visitVarInsn(ISTORE, 9);
                    Label l9 = new Label();
                    Label l10 = new Label();
                    initVisitor.visitLabel(l9);
                    initVisitor.visitFrame(F_APPEND, 1, new Object[]{INTEGER}, 0, new Object[0]);
                    initVisitor.visitVarInsn(ILOAD, 9);
                    initVisitor.visitVarInsn(ILOAD, 6);
                    initVisitor.visitJumpInsn(IF_ICMPGE, l10);
                    Label l11 = new Label();
                    initVisitor.visitLabel(l11);
                    initVisitor.visitVarInsn(ILOAD, 7);
                    initVisitor.visitIntInsn(BIPUSH, 11);
                    initVisitor.visitInsn(ISHL);
                    initVisitor.visitVarInsn(ILOAD, 8);
                    initVisitor.visitIntInsn(BIPUSH, 7);
                    initVisitor.visitInsn(ISHL);
                    initVisitor.visitInsn(IOR);
                    initVisitor.visitVarInsn(ILOAD, 9);
                    initVisitor.visitInsn(IOR);
                    initVisitor.visitVarInsn(ISTORE, 10);
                    Label l12 = new Label();
                    initVisitor.visitLabel(l12);
                    initVisitor.visitVarInsn(ALOAD, 2);
                    initVisitor.visitVarInsn(ILOAD, 10);
                    initVisitor.visitInsn(BALOAD);
                    initVisitor.visitIntInsn(SIPUSH, 255);
                    initVisitor.visitInsn(IAND);
                    initVisitor.visitVarInsn(ISTORE, 11);
                    Label l13 = new Label();
                    initVisitor.visitLabel(l13);
                    initVisitor.visitVarInsn(ALOAD, 3);
                    initVisitor.visitVarInsn(ILOAD, 10);
                    initVisitor.visitInsn(BALOAD);
                    initVisitor.visitVarInsn(ISTORE, 12);
                    Label l14 = new Label();
                    Label l15 = new Label();
                    initVisitor.visitLabel(l14);
                    initVisitor.visitVarInsn(ILOAD, 11);
                    initVisitor.visitJumpInsn(IFEQ, l15);
                    Label l16 = new Label();
                    initVisitor.visitLabel(l16);
                    initVisitor.visitVarInsn(ILOAD, 9);
                    initVisitor.visitInsn(ICONST_4);
                    initVisitor.visitInsn(ISHR);
                    initVisitor.visitVarInsn(ISTORE, 13);
                    Label l17 = new Label();
                    Label l18 = new Label();
                    initVisitor.visitLabel(l17);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitFieldInsn(GETFIELD, "net/minecraft/class_1196", "field_4740", "[Lnet/minecraft/class_1197;");
                    initVisitor.visitVarInsn(ILOAD, 13);
                    initVisitor.visitInsn(AALOAD);
                    initVisitor.visitJumpInsn(IFNONNULL, l18);
                    Label l19 = new Label();
                    initVisitor.visitLabel(l19);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitFieldInsn(GETFIELD, "net/minecraft/class_1196", "field_4740", "[Lnet/minecraft/class_1197;");
                    initVisitor.visitVarInsn(ILOAD, 13);
                    initVisitor.visitTypeInsn(NEW, "net/minecraft/class_1197");
                    initVisitor.visitInsn(DUP);
                    initVisitor.visitVarInsn(ILOAD, 13);
                    initVisitor.visitInsn(ICONST_4);
                    initVisitor.visitInsn(ISHL);
                    initVisitor.visitMethodInsn(INVOKESPECIAL, "net/minecraft/class_1197", "<init>", "(I)V", false);
                    initVisitor.visitInsn(AASTORE);

                    initVisitor.visitLabel(l18);
                    initVisitor.visitFrame(F_FULL, 14, new Object[]{
                            "net/minecraft/class_1196",
                            "net/minecraft/class_1150",
                            "[B", "[B",
                            INTEGER, INTEGER, INTEGER, INTEGER, INTEGER,
                            INTEGER, INTEGER, INTEGER, INTEGER, INTEGER
                    }, 0, new Object[0]);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitFieldInsn(GETFIELD, "net/minecraft/class_1196", "field_4740", "[Lnet/minecraft/class_1197;");
                    initVisitor.visitVarInsn(ILOAD, 13);
                    initVisitor.visitInsn(AALOAD);
                    initVisitor.visitVarInsn(ILOAD, 7);
                    initVisitor.visitVarInsn(ILOAD, 9);
                    initVisitor.visitIntInsn(BIPUSH, 15);
                    initVisitor.visitInsn(IAND);
                    initVisitor.visitVarInsn(ILOAD, 8);
                    initVisitor.visitVarInsn(ILOAD, 11);
                    initVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/class_1197", "method_3927", "(IIII)V", false);
                    Label l20 = new Label();
                    initVisitor.visitLabel(l20);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitFieldInsn(GETFIELD, "net/minecraft/class_1196", "field_4740", "[Lnet/minecraft/class_1197;");
                    initVisitor.visitVarInsn(ILOAD, 13);
                    initVisitor.visitInsn(AALOAD);
                    initVisitor.visitVarInsn(ILOAD, 7);
                    initVisitor.visitVarInsn(ILOAD, 9);
                    initVisitor.visitIntInsn(BIPUSH, 15);
                    initVisitor.visitInsn(IAND);
                    initVisitor.visitVarInsn(ILOAD, 8);
                    initVisitor.visitVarInsn(ILOAD, 12);
                    initVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/class_1197", "method_3927", "(IIII)V", false);

                    initVisitor.visitLabel(l15);
                    initVisitor.visitFrame(F_FULL, 10, new Object[]{
                            "net/minecraft/class_1196",
                            "net/minecraft/class_1150",
                            "[B", "[B",
                            INTEGER, INTEGER, INTEGER,
                            INTEGER, INTEGER, INTEGER,
                    }, 0, new Object[0]);
                    initVisitor.visitIincInsn(9, 1);
                    initVisitor.visitJumpInsn(GOTO, l9);

                    initVisitor.visitLabel(l10);
                    initVisitor.visitFrame(F_CHOP, 1, new Object[0], 0, new Object[0]);
                    initVisitor.visitIincInsn(8, 1);
                    initVisitor.visitJumpInsn(GOTO, l6);

                    initVisitor.visitLabel(l7);
                    initVisitor.visitFrame(F_CHOP, 1, new Object[0], 0, new Object[0]);
                    initVisitor.visitIincInsn(7, 1);
                    initVisitor.visitJumpInsn(GOTO, l3);

                    initVisitor.visitLabel(l4);
                    initVisitor.visitFrame(F_CHOP, 1, new Object[0], 0, new Object[0]);
                    initVisitor.visitInsn(RETURN);
                    Label l21 = new Label();
                    initVisitor.visitLabel(l21);
                    initVisitor.visitLocalVariable("var10", "I", null, l17, l15, 13);
                    initVisitor.visitLocalVariable("idx", "I", null, l12, l15, 10);
                    initVisitor.visitLocalVariable("id", "I", null, l13, l15, 11);
                    initVisitor.visitLocalVariable("meta", "I", null, l14, l15, 12);
                    initVisitor.visitLocalVariable("y", "I", null, l9, l10, 9);
                    initVisitor.visitLocalVariable("z", "I", null, l6, l7, 8);
                    initVisitor.visitLocalVariable("x", "I", null, l3, l4, 7);
                    initVisitor.visitLocalVariable("this", "Lnet/minecraft/class_1196;", null, l0, l21, 0);
                    initVisitor.visitLocalVariable("world", "Lnet/minecraft/class_1150;", null, l0, l21, 1);
                    initVisitor.visitLocalVariable("ids", "[B", null, l0, l21, 2);
                    initVisitor.visitLocalVariable("metadata", "[B", null, l0, l21, 3);
                    initVisitor.visitLocalVariable("chunkX", "I", null, l0, l21, 4);
                    initVisitor.visitLocalVariable("chunkZ", "I", null, l0, l21, 5);
                    initVisitor.visitLocalVariable("var5", "I", null, l2, l21, 6);
                    initVisitor.visitMaxs(6, 14);
                    initVisitor.visitEnd();
                }
                break;
            default:
                break;
        }
    }
}
