package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {
    @Invoker
    void callFunc_71390_a(boolean p_71390_1_);
}
