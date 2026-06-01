/**
 * Copyright (C) 2024-2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.remapper.asm;

import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.MappingsHelper;
import io.github.fabriccompatibilitylayers.modremappingapi.api.v2.MappingUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.Map;

public class BetterMethodInsnNode extends MethodInsnNode implements Opcodes {
    public BetterMethodInsnNode(int opcode, String owner, String name, String descriptor) {
        this(opcode, owner, name, descriptor, opcode == Opcodes.INVOKEINTERFACE);
    }

    public BetterMethodInsnNode(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        this(opcode, MappingsHelper.mapClass(owner), MappingsHelper.mapMethodFromRemappedClass(MappingsHelper.mapClass(owner), name, descriptor), isInterface);
    }

    private BetterMethodInsnNode(int opcode, String owner, MappingUtils.ClassMember member, boolean isInterface) {
        super(opcode, owner, member.getName(), MappingsHelper.mapDescriptor(member.getDesc()), isInterface);
    }

    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> clonedLabels) {
        return new BetterMethodInsnNode(opcode, owner, name, desc, itf).cloneAnnotations(this);
    }
}
