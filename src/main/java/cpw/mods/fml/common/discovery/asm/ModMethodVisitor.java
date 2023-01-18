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

import com.google.common.collect.Lists;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.LinkedList;

public class ModMethodVisitor extends MethodVisitor {
    private ASMModParser discoverer;
    private boolean inCode;
    private LinkedList<Label> labels = Lists.newLinkedList();
    private String foundProperties;
    private boolean validProperties;

    public ModMethodVisitor(String name, ASMModParser discoverer) {
        super(262144);
        this.discoverer = discoverer;
    }

    public void visitCode() {
        this.labels.clear();
    }

    public void visitLdcInsn(Object cst) {
        if (cst instanceof String && this.labels.size() == 1) {
            this.foundProperties = (String)cst;
        }
    }

    public void visitInsn(int opcode) {
        if (176 == opcode && this.labels.size() == 1 && this.foundProperties != null) {
            this.validProperties = true;
        }
    }

    public void visitLabel(Label label) {
        this.labels.push(label);
    }

    public void visitEnd() {
        if (this.validProperties) {
            this.discoverer.setBaseModProperties(this.foundProperties);
        }
    }
}
