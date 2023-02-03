package fr.catcore.fabricatedforge.mixin.forgefml.client;

import net.minecraft.client.class_417;
import net.minecraft.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_417.class)
public interface class_417Accessor {
    @Accessor
    Slot getField_1386();
}
