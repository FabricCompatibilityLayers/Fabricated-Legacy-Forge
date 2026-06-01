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

import java.util.Collections;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ModClassVisitor extends ClassVisitor
{
    private ASMModParser discoverer;

    public ModClassVisitor(ASMModParser discoverer)
    {
        super(Opcodes.ASM9);
        this.discoverer = discoverer;
    }


    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
    {
        discoverer.beginNewTypeName(name, version, superName);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String annotationName, boolean runtimeVisible)
    {
        discoverer.startClassAnnotation(annotationName);
        return new ModAnnotationVisitor(discoverer);
    }


    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
    {
        return new ModFieldVisitor(name, discoverer);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
    {
        if (discoverer.isBaseMod(Collections.<String>emptyList()) && name.equals("getPriorities") && desc.equals(Type.getMethodDescriptor(Type.getType(String.class))))
        {
            return new ModMethodVisitor(name, discoverer);
        }
        return null;
    }
}
