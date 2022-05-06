package fr.catcore.fabricatedforge.mixin.modloader.common;

import net.minecraft.util.collection.Weight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Weight.class)
public interface WeightAccessor {

    @Accessor("weight")
    void setWeight(int weight);
}
