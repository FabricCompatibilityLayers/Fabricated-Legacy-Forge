package cpw.mods.fml.common.discovery.asm;

import org.objectweb.asm.AnnotationVisitor;

public class ModAnnotationVisitor extends AnnotationVisitor {
    private ASMModParser discoverer;
    private boolean array;
    private String name;
    private boolean isSubAnnotation;

    public ModAnnotationVisitor(ASMModParser discoverer) {
        super(262144);
        this.discoverer = discoverer;
    }

    public ModAnnotationVisitor(ASMModParser discoverer, String name) {
        this(discoverer);
        this.array = true;
        this.name = name;
        discoverer.addAnnotationArray(name);
    }

    public ModAnnotationVisitor(ASMModParser discoverer, boolean isSubAnnotation) {
        this(discoverer);
        this.isSubAnnotation = true;
    }

    public void visit(String key, Object value) {
        this.discoverer.addAnnotationProperty(key, value);
    }

    public void visitEnum(String name, String desc, String value) {
        this.discoverer.addAnnotationEnumProperty(name, desc, value);
    }

    public AnnotationVisitor visitArray(String name) {
        return new ModAnnotationVisitor(this.discoverer, name);
    }

    public AnnotationVisitor visitAnnotation(String name, String desc) {
        this.discoverer.addSubAnnotation(name, desc);
        return new ModAnnotationVisitor(this.discoverer, true);
    }

    public void visitEnd() {
        if (this.array) {
            this.discoverer.endArray();
        }

        if (this.isSubAnnotation) {
            this.discoverer.endSubAnnotation();
        }
    }
}
