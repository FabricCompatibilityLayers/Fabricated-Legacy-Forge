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
package cpw.mods.fml.common.discovery.asm;

import java.util.LinkedList;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.google.common.collect.Lists;

public class ModMethodVisitor extends MethodVisitor
{

    private ASMModParser discoverer;
    private boolean inCode;
    private LinkedList<Label> labels = Lists.newLinkedList();
    private String foundProperties;
    private boolean validProperties;

    public ModMethodVisitor(String name, ASMModParser discoverer)
    {
        super(Opcodes.ASM9);
        this.discoverer = discoverer;
    }
    @Override
    public void visitCode()
    {
        labels.clear();
    }
    
    @Override
    public void visitLdcInsn(Object cst)
    {
        if (cst instanceof String && labels.size() == 1)
        {
            foundProperties = (String) cst;
        }
    }
    @Override
    public void visitInsn(int opcode)
    {
        if (Opcodes.ARETURN == opcode && labels.size() == 1 && foundProperties != null)
        {
            validProperties = true;
        }
    }
    @Override
    public void visitLabel(Label label)
    {
        labels.push(label);
    }
    
    @Override
    public void visitEnd()
    {
        if (validProperties)
        {
            discoverer.setBaseModProperties(foundProperties);
        }
    }
}
