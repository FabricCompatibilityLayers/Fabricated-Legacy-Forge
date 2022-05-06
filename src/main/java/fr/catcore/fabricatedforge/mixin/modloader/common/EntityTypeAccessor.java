package fr.catcore.fabricatedforge.mixin.modloader.common;

import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(EntityType.class)
public interface EntityTypeAccessor {

    @Accessor("NAME_CLASS_MAP")
    static Map<String, Class<?>> getClassMap() {
        return null;
    }

    @Invoker("registerEntity")
    static void callRegister(Class clazz, String name, int id) {
        throw new AssertionError("@Invoker dummy body called");
    }
}
