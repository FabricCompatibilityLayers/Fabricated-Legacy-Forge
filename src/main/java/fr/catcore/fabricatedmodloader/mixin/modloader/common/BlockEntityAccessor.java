package fr.catcore.fabricatedmodloader.mixin.modloader.common;

import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(BlockEntity.class)
public interface BlockEntityAccessor {

    @Invoker("registerBlockEntity")
    static void callRegister(Class clazz, String stringId) {
        throw new AssertionError("@Invoker dummy body called");
    }

    @Accessor("stringClassMap")
    static Map getStringClassMap() {
        return null;
    }
}
