package io.github.fabriccompatibilitylayers.fabricatedfml.utils;

import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.MappingsHelper;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.util.Annotations;

public class PatchConversionHelper {
    public static void postApply(ClassNode targetClass) {
        String name = targetClass.name;

        for (MethodNode method : targetClass.methods) {
            AnnotationNode node = Annotations.getVisible(method, ServerImplementation.class);
            if (node != null) {
                String originalName = Annotations.getValue(node);
                method.name = MappingsHelper.mapMethodFromRemappedClass(name, originalName, MappingsHelper.mapDescriptor(method.desc)).getName();
            }
        }
    }
}
