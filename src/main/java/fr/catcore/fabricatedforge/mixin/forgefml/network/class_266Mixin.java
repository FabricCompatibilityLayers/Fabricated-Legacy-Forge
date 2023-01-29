package fr.catcore.fabricatedforge.mixin.forgefml.network;

import net.minecraft.network.class_266;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(class_266.class)
public class class_266Mixin {
    /**
     * @author catcore
     * @reason shortcircuit
     */
    @Overwrite
    public Object run() {
        return null;
    }
}
