/**
 * Copyright (C) 2024-2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.remapper.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class BetterClassWriter extends ClassWriter {
    public BetterClassWriter(int flags) {
        super(flags);
    }

    public BetterClassWriter(ClassReader classReader, int flags) {
        super(classReader, flags);
    }

    @Override
    protected ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }
}
