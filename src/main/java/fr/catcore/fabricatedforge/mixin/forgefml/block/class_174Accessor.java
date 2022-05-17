package fr.catcore.fabricatedforge.mixin.forgefml.block;

import net.minecraft.block.class_174;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(class_174.class)
public interface class_174Accessor {

    @Invoker("method_363")
    int method_363_invoker();
}
