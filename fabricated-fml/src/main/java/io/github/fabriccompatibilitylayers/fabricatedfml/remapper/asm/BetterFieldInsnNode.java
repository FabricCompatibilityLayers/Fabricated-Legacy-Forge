package io.github.fabriccompatibilitylayers.fabricatedfml.remapper.asm;

import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.MappingsHelper;
import io.github.fabriccompatibilitylayers.modremappingapi.api.v2.MappingUtils;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LabelNode;

import java.util.Map;

public class BetterFieldInsnNode extends FieldInsnNode {
    /**
     * Constructs a new {@link FieldInsnNode}.
     *
     * @param opcode     the opcode of the type instruction to be constructed. This opcode must be
     *                   GETSTATIC, PUTSTATIC, GETFIELD or PUTFIELD.
     * @param owner      the internal name of the field's owner class (see {@link
     *                   Type#getInternalName()}).
     * @param name       the field's name.
     * @param descriptor the field's descriptor (see {@link Type}).
     */
    public BetterFieldInsnNode(int opcode, String owner, String name, String descriptor) {
        this(opcode, MappingsHelper.mapClass(owner), MappingsHelper.mapFieldFromRemappedClass(MappingsHelper.mapClass(owner), name, descriptor));
    }

    private BetterFieldInsnNode(int code, String owner, MappingUtils.ClassMember classMember) {
        super(code, owner, classMember.getName(), MappingsHelper.mapDescriptor(classMember.getDesc()));
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        return new BetterFieldInsnNode(opcode, owner, name, desc).cloneAnnotations(this);
    }
}
