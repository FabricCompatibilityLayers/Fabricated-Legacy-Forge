/*
 * The FML Forge Mod Loader suite. Copyright (C) 2012 cpw
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

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import cpw.mods.fml.common.registry.BlockProxy;
import cpw.mods.fml.relauncher.IClassTransformer;

public class ASMTransformer implements IClassTransformer
{
    @Override
    public byte[] transform(String name, byte[] bytes)
    {
        if ("net.minecraft.src.Block".equals(name))
        {
            ClassReader cr = new ClassReader(bytes);
            ClassNode cn = new ClassNode(Opcodes.ASM4);
            cr.accept(cn, ClassReader.EXPAND_FRAMES);
            cn.interfaces.add(Type.getInternalName(BlockProxy.class));
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            cn.accept(cw);
            return cw.toByteArray();
        }
        
        return bytes;
    }

}
