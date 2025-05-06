package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.src.BlockDoor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockDoor.class)
public class BlockDoorMixin {
    @ModifyReturnValue(method = "onBlockActivated", at = @At(value = "RETURN", ordinal = 0))
    private boolean forge$allowItemsToInteractWithDoor(boolean original) {
        return false;
    }
}
