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
                    initVisitor.visitVarInsn(ILOAD, 2);
                    initVisitor.visitFieldInsn(PUTFIELD, "net/minecraft/class_847", "field_3108", "I");
                    Label l4 = new Label();
                    initVisitor.visitLabel(l4);
                    initVisitor.visitInsn(RETURN);
                    Label l5 = new Label();
                    initVisitor.visitLocalVariable("this", "Lnet/minecraft/class_847;", null, l0, l5, 0);
                    initVisitor.visitLocalVariable("stack", "Lnet/minecraft/class_1071;", null, l0, l5, 1);
                    initVisitor.visitLocalVariable("min", "I", null, l0, l5, 2);
                    initVisitor.visitLocalVariable("max", "I", null, l0, l5, 3);
                    initVisitor.visitLocalVariable("weight", "I", null, l0, l5, 4);
                    initVisitor.visitMaxs(2, 5);
                    initVisitor.visitEnd();
                }
                break;
            default:
                break;
        }
    }
}
