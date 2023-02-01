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
package cpw.mods.fml.common.asm.transformers;

import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.FMLRelauncher;
import cpw.mods.fml.relauncher.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;
import java.util.List;

public class SideTransformer implements IClassTransformer {
    private static String SIDE = FMLRelauncher.side();
    private static final boolean DEBUG = false;

    public SideTransformer() {
    }

    public byte[] transform(String name, byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(bytes);
            classReader.accept(classNode, 0);
            if (this.remove(classNode.visibleAnnotations, SIDE)) {
                throw new RuntimeException(String.format("Attempted to load class %s for invalid side %s", classNode.name, SIDE));
            } else {
                Iterator<FieldNode> fields = classNode.fields.iterator();

                while(fields.hasNext()) {
                    FieldNode field = (FieldNode)fields.next();
                    if (this.remove(field.visibleAnnotations, SIDE)) {
                        fields.remove();
                    }
                }

                Iterator<MethodNode> methods = classNode.methods.iterator();

                while(methods.hasNext()) {
                    MethodNode method = (MethodNode)methods.next();
                    if (this.remove(method.visibleAnnotations, SIDE)) {
                        methods.remove();
                    }
                }

                ClassWriter writer = new ClassWriter(1);
                classNode.accept(writer);
                return writer.toByteArray();
            }
        }
    }

    private boolean remove(List<AnnotationNode> anns, String side) {
        if (anns == null) {
            return false;
        } else {
            for(AnnotationNode ann : anns) {
                if (ann.desc.equals(Type.getDescriptor(SideOnly.class)) && ann.values != null) {
                    for(int x = 0; x < ann.values.size() - 1; x += 2) {
                        Object key = ann.values.get(x);
                        Object value = ann.values.get(x + 1);
                        if (key instanceof String && key.equals("value") && value instanceof String[] && !((String[])value)[1].equals(side)) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }
    }
}
