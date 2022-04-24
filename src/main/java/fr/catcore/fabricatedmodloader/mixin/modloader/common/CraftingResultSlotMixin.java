package fr.catcore.fabricatedmodloader.mixin.modloader.common;

import modloader.ModLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.slot.CraftingResultSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingResultSlot.class)
public class CraftingResultSlotMixin {

    @Shadow
    private PlayerEntity player;

    @Shadow
    @Final
    private Inventory field_4147;

    @Inject(method = "onTakeItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/slot/CraftingResultSlot;onCrafted(Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER))
    private void modLoaderTakenFromCrafting(PlayerEntity stack, ItemStack par2, CallbackInfo ci) {
        ModLoader.takenFromCrafting(this.player, par2, this.field_4147);
    }
}
