package fr.catcore.fabricatedmodloader.mixin.modloader.common;

import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityType.class)
public interface EntityTypeAccessor {

    @Invoker("registerEntity")
    static void callRegister(Class clazz, String name, int id) {
        throw new AssertionError("@Invoker dummy body called");
    }
}
