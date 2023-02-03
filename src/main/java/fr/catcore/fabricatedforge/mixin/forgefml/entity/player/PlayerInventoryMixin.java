package fr.catcore.fabricatedforge.mixin.forgefml.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements Inventory {
    @Shadow public ItemStack[] main;

    @Shadow public PlayerEntity player;

    @Shadow public int selectedSlot;

    @Shadow public ItemStack[] armor;

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    public void updateItems() {
        for(int var1 = 0; var1 < this.main.length; ++var1) {
            if (this.main[var1] != null) {
                this.main[var1].inventoryTick(this.player.world, this.player, var1, this.selectedSlot == var1);
            }
        }

        for(int i = 0; i < this.armor.length; ++i) {
            if (this.armor[i] != null) {
                this.armor[i].getItem().onArmorTickUpdate(this.player.world, this.player, this.armor[i]);
            }
        }
    }
}
