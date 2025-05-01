package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlotFurnace.class)
public class SlotFurnaceMixin extends Slot {
    @Shadow private EntityPlayer field_75229_a;

    public SlotFurnaceMixin(IInventory p_i3616_1_, int p_i3616_2_, int p_i3616_3_, int p_i3616_4_) {
        super(p_i3616_1_, p_i3616_2_, p_i3616_3_, p_i3616_4_);
    }

    @Inject(method = "func_75208_c", at = @At(value = "FIELD", target = "Lnet/minecraft/src/SlotFurnace;field_75228_b:I", shift = At.Shift.AFTER))
    private void fml$onItemSmelted(ItemStack p_75208_1_, CallbackInfo ci) {
        GameRegistry.onItemSmelted(field_75229_a, p_75208_1_);
    }
}
