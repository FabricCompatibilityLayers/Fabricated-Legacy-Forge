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
package cpw.mods.fml.common.discovery.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;

public class ModFieldVisitor extends FieldVisitor {
    private String fieldName;
    private ASMModParser discoverer;

    public ModFieldVisitor(String name, ASMModParser discoverer) {
        super(262144);
        this.fieldName = name;
        this.discoverer = discoverer;
    }

    public AnnotationVisitor visitAnnotation(String annotationName, boolean runtimeVisible) {
        this.discoverer.startFieldAnnotation(this.fieldName, annotationName);
        return new ModAnnotationVisitor(this.discoverer);
    }
}
