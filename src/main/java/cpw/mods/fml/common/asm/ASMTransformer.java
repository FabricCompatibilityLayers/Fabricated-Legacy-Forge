/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.common.asm;

import cpw.mods.fml.common.registry.BlockProxy;
import cpw.mods.fml.relauncher.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

public class ASMTransformer implements IClassTransformer {
    public ASMTransformer() {
    }

    public byte[] transform(String name, byte[] bytes) {
        if ("net.minecraft.src.Block".equals(name)) {
            ClassReader cr = new ClassReader(bytes);
            ClassNode cn = new ClassNode(262144);
            cr.accept(cn, 8);
            cn.interfaces.add(Type.getInternalName(BlockProxy.class));
            ClassWriter cw = new ClassWriter(3);
            cn.accept(cw);
            return cw.toByteArray();
        } else {
            return bytes;
        }
    }
}
