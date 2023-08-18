package fr.catcore.fabricatedforge.compat.mixin.codechickencore;

import codechicken.core.asm.ClassHeirachyManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClassHeirachyManager.class)
public class ClassHeirachyManagerMixin {
    @Redirect(method = "classExtends(Ljava/lang/String;Ljava/lang/String;)Z", at = @At(value = "INVOKE", target = "Ljava/lang/Class;forName(Ljava/lang/String;)Ljava/lang/Class;", remap = false), remap = false)
    private static Class<?> fixClassExtends(String className) throws ClassNotFoundException {
        return Class.forName(className, false, ClassHeirachyManager.class.getClassLoader());
    }
}
