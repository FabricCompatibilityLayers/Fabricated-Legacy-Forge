package fr.catcore.fabricatedforge.mixin.forgefml.client;

import net.minecraft.client.class_469;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_469.class)
public interface class_469Accessor {

    @Accessor(value = "connectionCompatibilityLevel", remap = false)
    public static void setConnectionCompatibilityLevel(byte connectionCompatibilityLevel) {

    }

    @Accessor(value = "connectionCompatibilityLevel", remap = false)
    public static byte getConnectionCompatibilityLevel() {
        return 0;
    }
}
