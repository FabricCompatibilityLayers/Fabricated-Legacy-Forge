package fr.catcore.fabricatedforge.compat.asm;

import fr.catcore.fabricatedforge.Constants;
import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;
import org.objectweb.asm.Type;
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
        this(opcode, Constants.mapClass(owner), Constants.mapFieldFromRemappedClass(Constants.mapClass(owner), name, descriptor));
    }

    private BetterFieldInsnNode(int code, String owner, MappingUtils.ClassMember classMember) {
        super(code, owner, classMember.name, Constants.mapTypeDescriptor(classMember.desc));
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        return new BetterFieldInsnNode(opcode, owner, name, desc).cloneAnnotations(this);
    }
}