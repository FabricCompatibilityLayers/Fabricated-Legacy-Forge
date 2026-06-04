/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.remapper.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public class ClassNodeHelper {
    public static ClassNode createNode(byte[] bytes, int parsingOptions) {
        ClassNode node = new ClassNode(Opcodes.ASM4);
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, parsingOptions);

        return node;
    }

    public static byte[] writeClass(ClassNode node, int flags) {
        BetterClassWriter classWriter = new BetterClassWriter(flags);
        node.accept(classWriter);

        return classWriter.toByteArray();
    }
}
