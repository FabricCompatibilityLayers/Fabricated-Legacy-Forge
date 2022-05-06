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
