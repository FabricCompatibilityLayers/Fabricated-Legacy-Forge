package fr.catcore.fabricatedforge.mixin.forgefml.client;

import net.minecraft.client.class_469;
import net.minecraft.client.class_470;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(class_470.class)
public interface class_470Accessor {

    @Invoker("<init>")
    static class_470 newInstance(class_469 arg, String string) {
        return null;
    }
}
