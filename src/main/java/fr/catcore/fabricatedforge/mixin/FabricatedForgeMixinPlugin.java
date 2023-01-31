package fr.catcore.fabricatedforge.mixin;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
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
    private static final String
            SPRITE = "net/minecraft/class_584",
            FML_TEXTURE_FX = "cpw/mods/fml/client/FMLTextureFX",
            STRING = "java/lang/String",
            STRING_DESC = to(STRING),
            INIT = "<init>",
            ITEM_GROUP = "net/minecraft/class_1041",
            ITEM_GROUP_DESC = to(ITEM_GROUP),
            ITEM_GROUP_INIT_DESC_NEW = "(" + STRING_DESC + ")V",
            ITEM_GROUP_INIT_DESC = "(I" + STRING_DESC + ")V",
            ITEM_GROUP_ARRAY = "field_4173",
            ITEM_GROUP_ARRAY_DESC = "[" + ITEM_GROUP_DESC + "",
            NEXT_ID = "getNextID",
            NEXT_ID_DESC = "()I",
            THIS = "this",
            ITEMSTACK = "net/minecraft/class_1071",
            ITEMSTACK_DESC = to(ITEMSTACK),
            WEIGHT = "net/minecraft/class_846",
            WEIGHT_INIT = "(I)V",
            WEIGHTED_RANDOM_CHEST_CONTENT = "net/minecraft/class_847",
            WEIGHTED_RANDOM_CHEST_CONTENT_DESC = to(WEIGHTED_RANDOM_CHEST_CONTENT),
            WEIGHTED_RANDOM_CHEST_CONTENT_INIT = "(" + ITEMSTACK_DESC + "III)V",
            WEIGHTED_MIN = "field_3107",
            WEIGHTED_MAX = "field_3108",
            CHUNK = "net/minecraft/class_1196",
            CHUNK_DESC = to(CHUNK),
            WORLD = "net/minecraft/class_1150",
            WORLD_DESC = to(WORLD),
            CHUNK_SECTION = "net/minecraft/class_1197",
            CHUNK_SECTION_DESC = to(CHUNK_SECTION),
            CHUNK_CHUNK_SECTIONS = "field_4740",
            CHUNK_CHUNK_SECTIONS_DESC = "[" + CHUNK_SECTION_DESC,
            CHUNK_SECTION_SET_BLOCK = "method_3927",
            CHUNK_SECTION_SET_BLOCK_DESC = "(IIII)V",
            CHUNK_SECTION_INIT = "(I)V",
            CHUNK_INIT = "(" + WORLD_DESC + "II)V",
            CHUNK_INIT_NEW = "(" + WORLD_DESC + "[B[BII)V"
                    ;

    private static String to(String s) {
        return "L" + s + ";";
    }

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
                targetClass.superName = FML_TEXTURE_FX;
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
                        if (Objects.equals(node.name, INIT)) {
                            for (AbstractInsnNode insNode : node.instructions) {
                                if (insNode instanceof MethodInsnNode && insNode.getOpcode() == INVOKESPECIAL) {
                                    MethodInsnNode mTheNode = (MethodInsnNode) insNode;
                                    if (Objects.equals(mTheNode.owner, SPRITE))
                                        mTheNode.owner = FML_TEXTURE_FX;
                                }
                            }
                        }
                    }
                }
                break;
            case "net.minecraft.class_1041": // ItemGroup
                {
                    // getNextID
                    MethodVisitor getNextIDVisitor = targetClass.visitMethod(ACC_PUBLIC + ACC_STATIC, NEXT_ID, NEXT_ID_DESC, null, null);
                    getNextIDVisitor.visitFieldInsn(GETSTATIC, ITEM_GROUP, ITEM_GROUP_ARRAY, ITEM_GROUP_ARRAY_DESC);
                    getNextIDVisitor.visitInsn(ARRAYLENGTH);
                    getNextIDVisitor.visitInsn(IRETURN);
                    getNextIDVisitor.visitMaxs(1, 0);
                    getNextIDVisitor.visitEnd();

                    // <init>
                    MethodVisitor initVisitor = targetClass.visitMethod(ACC_PUBLIC, INIT, ITEM_GROUP_INIT_DESC_NEW, null, null);
                    Label l0 = new Label();
                    initVisitor.visitLabel(l0);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitMethodInsn(INVOKESTATIC, ITEM_GROUP, NEXT_ID, NEXT_ID_DESC, false);
                    initVisitor.visitVarInsn(ALOAD, 1);
                    initVisitor.visitMethodInsn(INVOKESPECIAL, ITEM_GROUP, INIT, ITEM_GROUP_INIT_DESC, false);
                    Label l1 = new Label();
                    initVisitor.visitLabel(l1);
                    initVisitor.visitInsn(RETURN);
                    Label l2 = new Label();
                    initVisitor.visitLabel(l2);
                    initVisitor.visitLocalVariable(THIS, ITEM_GROUP_DESC, null, l0, l2, 0);
                    initVisitor.visitLocalVariable("label", STRING_DESC, null, l0, l2, 1);
                    initVisitor.visitMaxs(3, 2);
                    initVisitor.visitEnd();
                }
                break;
            case "net.minecraft.class_847": // WeightedRandomChestContent
                {
                    // <init>
                    MethodVisitor initVisitor = targetClass.visitMethod(ACC_PUBLIC, INIT, WEIGHTED_RANDOM_CHEST_CONTENT_INIT, null, null);
                    Label l0 = new Label();
                    initVisitor.visitLabel(l0);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitVarInsn(ILOAD, 4);
                    initVisitor.visitMethodInsn(INVOKESPECIAL, WEIGHT, INIT, WEIGHT_INIT, false);
                    Label l1 = new Label();
                    initVisitor.visitLabel(l1);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitVarInsn(ALOAD, 1);
                    initVisitor.visitFieldInsn(PUTFIELD, WEIGHTED_RANDOM_CHEST_CONTENT, "itemStack", ITEMSTACK_DESC);
                    Label l2 = new Label();
                    initVisitor.visitLabel(l2);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitVarInsn(ILOAD, 2);
                    initVisitor.visitFieldInsn(PUTFIELD, WEIGHTED_RANDOM_CHEST_CONTENT, WEIGHTED_MIN, "I");
                    Label l3 = new Label();
                    initVisitor.visitLabel(l3);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitVarInsn(ILOAD, 3);
                    initVisitor.visitFieldInsn(PUTFIELD, WEIGHTED_RANDOM_CHEST_CONTENT, WEIGHTED_MAX, "I");
                    Label l4 = new Label();
                    initVisitor.visitLabel(l4);
                    initVisitor.visitInsn(RETURN);
                    Label l5 = new Label();
                    initVisitor.visitLabel(l5);
                    initVisitor.visitLocalVariable(THIS, WEIGHTED_RANDOM_CHEST_CONTENT_DESC, null, l0, l5, 0);
                    initVisitor.visitLocalVariable("stack", ITEMSTACK_DESC, null, l0, l5, 1);
                    initVisitor.visitLocalVariable("min", "I", null, l0, l5, 2);
                    initVisitor.visitLocalVariable("max", "I", null, l0, l5, 3);
                    initVisitor.visitLocalVariable("weight", "I", null, l0, l5, 4);
                    initVisitor.visitMaxs(2, 5);
                    initVisitor.visitEnd();
                }
                break;
            case "net.minecraft.class_1196": // Chunk
                {
                    // <init>
                    MethodVisitor initVisitor = targetClass.visitMethod(ACC_PUBLIC, INIT, CHUNK_INIT_NEW, null, null);
                    Label l0 = new Label();
                    initVisitor.visitLabel(l0);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitVarInsn(ALOAD, 1);
                    initVisitor.visitVarInsn(ILOAD, 4);
                    initVisitor.visitVarInsn(ILOAD, 5);
                    initVisitor.visitMethodInsn(INVOKESPECIAL, CHUNK, INIT, CHUNK_INIT, false);
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
                            CHUNK,
                            WORLD,
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
                    initVisitor.visitFieldInsn(GETFIELD, CHUNK, CHUNK_CHUNK_SECTIONS, CHUNK_CHUNK_SECTIONS_DESC);
                    initVisitor.visitVarInsn(ILOAD, 13);
                    initVisitor.visitInsn(AALOAD);
                    initVisitor.visitJumpInsn(IFNONNULL, l18);
                    Label l19 = new Label();
                    initVisitor.visitLabel(l19);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitFieldInsn(GETFIELD, CHUNK, CHUNK_CHUNK_SECTIONS, CHUNK_CHUNK_SECTIONS_DESC);
                    initVisitor.visitVarInsn(ILOAD, 13);
                    initVisitor.visitTypeInsn(NEW, CHUNK_SECTION);
                    initVisitor.visitInsn(DUP);
                    initVisitor.visitVarInsn(ILOAD, 13);
                    initVisitor.visitInsn(ICONST_4);
                    initVisitor.visitInsn(ISHL);
                    initVisitor.visitMethodInsn(INVOKESPECIAL, CHUNK_SECTION, INIT, CHUNK_SECTION_INIT, false);
                    initVisitor.visitInsn(AASTORE);

                    initVisitor.visitLabel(l18);
                    initVisitor.visitFrame(F_FULL, 14, new Object[]{
                            CHUNK,
                            WORLD,
                            "[B", "[B",
                            INTEGER, INTEGER, INTEGER, INTEGER, INTEGER,
                            INTEGER, INTEGER, INTEGER, INTEGER, INTEGER
                    }, 0, new Object[0]);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitFieldInsn(GETFIELD, CHUNK, CHUNK_CHUNK_SECTIONS, CHUNK_CHUNK_SECTIONS_DESC);
                    initVisitor.visitVarInsn(ILOAD, 13);
                    initVisitor.visitInsn(AALOAD);
                    initVisitor.visitVarInsn(ILOAD, 7);
                    initVisitor.visitVarInsn(ILOAD, 9);
                    initVisitor.visitIntInsn(BIPUSH, 15);
                    initVisitor.visitInsn(IAND);
                    initVisitor.visitVarInsn(ILOAD, 8);
                    initVisitor.visitVarInsn(ILOAD, 11);
                    initVisitor.visitMethodInsn(INVOKEVIRTUAL, CHUNK_SECTION, CHUNK_SECTION_SET_BLOCK, CHUNK_SECTION_SET_BLOCK_DESC, false);
                    Label l20 = new Label();
                    initVisitor.visitLabel(l20);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitFieldInsn(GETFIELD, CHUNK, CHUNK_CHUNK_SECTIONS, CHUNK_CHUNK_SECTIONS_DESC);
                    initVisitor.visitVarInsn(ILOAD, 13);
                    initVisitor.visitInsn(AALOAD);
                    initVisitor.visitVarInsn(ILOAD, 7);
                    initVisitor.visitVarInsn(ILOAD, 9);
                    initVisitor.visitIntInsn(BIPUSH, 15);
                    initVisitor.visitInsn(IAND);
                    initVisitor.visitVarInsn(ILOAD, 8);
                    initVisitor.visitVarInsn(ILOAD, 12);
                    initVisitor.visitMethodInsn(INVOKEVIRTUAL, CHUNK_SECTION, CHUNK_SECTION_SET_BLOCK, CHUNK_SECTION_SET_BLOCK_DESC, false);

                    initVisitor.visitLabel(l15);
                    initVisitor.visitFrame(F_FULL, 10, new Object[]{
                            CHUNK,
                            WORLD,
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
                    initVisitor.visitLocalVariable(THIS, CHUNK_DESC, null, l0, l21, 0);
                    initVisitor.visitLocalVariable("world", WORLD_DESC, null, l0, l21, 1);
                    initVisitor.visitLocalVariable("ids", "[B", null, l0, l21, 2);
                    initVisitor.visitLocalVariable("metadata", "[B", null, l0, l21, 3);
                    initVisitor.visitLocalVariable("chunkX", "I", null, l0, l21, 4);
                    initVisitor.visitLocalVariable("chunkZ", "I", null, l0, l21, 5);
                    initVisitor.visitLocalVariable("var5", "I", null, l2, l21, 6);
                    initVisitor.visitMaxs(6, 14);
                    initVisitor.visitEnd();
                }
                break;
            case "net.minecraft.class_965": // AbstractMinecartEntity
                {
                    // <init>(Lnet/minecraft/world/World;I)V
                    MethodVisitor initVisitor = targetClass.visitMethod(ACC_PUBLIC, "<init>", "(Lnet/minecraft/class_1150;I)V", null, null);
                    Label l0 = new Label();
                    initVisitor.visitLabel(l0);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitVarInsn(ALOAD, 1);
                    initVisitor.visitMethodInsn(INVOKESPECIAL, "net/minecraft/class_965", "<init>", "(Lnet/minecraft/class_1150;)V", false);
                    Label l1 = new Label();
                    initVisitor.visitLabel(l1);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitVarInsn(ILOAD, 2);
                    initVisitor.visitFieldInsn(PUTFIELD, "net/minecraft/class_965", "field_3897", "I");
                    Label l2 = new Label();
                    initVisitor.visitLabel(l2);
                    initVisitor.visitInsn(RETURN);
                    Label l3 = new Label();
                    initVisitor.visitLabel(l3);
                    initVisitor.visitLocalVariable("this", "Lnet/minecraft/class_965;", null, l0, l3, 0);
                    initVisitor.visitLocalVariable("world", "Lnet/minecraft/class_1150;", null, l0, l3, 1);
                    initVisitor.visitLocalVariable("type", "I", null, l0, l3, 2);
                    initVisitor.visitMaxs(2, 3);
                    initVisitor.visitEnd();
                }
                break;
            case "net.minecraft.class_1239": // OreFeature
                {
                    // <init>(III)V
                    MethodVisitor initVisitor = targetClass.visitMethod(ACC_PUBLIC, "<init>", "(III)V", null, null);
                    Label l0 = new Label();
                    initVisitor.visitLabel(l0);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitVarInsn(ILOAD, 1);
                    initVisitor.visitVarInsn(ILOAD, 3);
                    Label l1 = new Label();
                    initVisitor.visitLabel(l1);
                    initVisitor.visitVarInsn(ALOAD, 0);
                    initVisitor.visitVarInsn(ILOAD, 2);
                    initVisitor.visitFieldInsn(PUTFIELD, "net/minecraft/class_1239", "minableBlockMeta", "I");
                    Label l2 = new Label();
                    initVisitor.visitLabel(l2);
                    initVisitor.visitInsn(RETURN);
                    Label l3 = new Label();
                    initVisitor.visitLabel(l3);
                    initVisitor.visitLocalVariable("this", "Lnet/minecraft/class_1239;", null, l0, l3, 0);
                    initVisitor.visitLocalVariable("id", "I", null, l0, l3, 1);
                    initVisitor.visitLocalVariable("meta", "I", null, l0, l3, 2);
                    initVisitor.visitLocalVariable("number", "I", null, l0, l3, 3);
                    initVisitor.visitMaxs(3, 4);
                    initVisitor.visitEnd();
                }
                break;
            default:
                break;
        }
    }
}
