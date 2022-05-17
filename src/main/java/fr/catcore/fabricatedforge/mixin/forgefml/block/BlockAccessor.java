package fr.catcore.fabricatedforge.mixin.forgefml.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Pseudo
@Mixin(targets = {"net/minecraft/block/Block", "net/minecraft/class_197"})
public interface BlockAccessor {

    @Invoker(value = "setBurnProperties(III)V", remap = false)
    static void setBurnProperties_invoker(int id, int encouragement, int flammability) {

    }

    @Accessor(value = "blockFireSpreadSpeed", remap = false)
    static int[] getBlockFireSpreadSpeed() {
        return new int[0];
    }

    @Accessor(value = "blockFlammability", remap = false)
    static int[] getBlockFlammability() {
        return new int[0];
    }
}
