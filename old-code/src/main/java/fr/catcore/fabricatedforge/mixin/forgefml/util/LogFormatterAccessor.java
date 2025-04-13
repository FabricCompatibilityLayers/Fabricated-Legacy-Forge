package fr.catcore.fabricatedforge.mixin.forgefml.util;

import net.minecraft.util.LogFormatter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LogFormatter.class)
public interface LogFormatterAccessor {

    @Invoker("<init>")
    static LogFormatter newInstance() {
        return null;
    }
}
