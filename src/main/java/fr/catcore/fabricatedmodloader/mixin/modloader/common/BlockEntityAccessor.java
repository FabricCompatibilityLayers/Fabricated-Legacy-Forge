package fr.catcore.fabricatedmodloader.mixin.modloader.common;

import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockEntity.class)
public interface BlockEntityAccessor {

    @Invoker("registerBlockEntity")
    static void callRegister(Class clazz, String stringId) {
        throw new AssertionError("@Invoker dummy body called");
    }
}
