package fr.catcore.fabricatedforge.mixin.forgefml.inventory.slot;

import cpw.mods.fml.common.registry.GameRegistry;
import fr.catcore.fabricatedforge.mixininterface.IItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.slot.CraftingResultSlot;
import net.minecraft.inventory.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CraftingResultSlot.class)
public class CraftingResultSlotMixin extends Slot {
    @Shadow private PlayerEntity player;

    @Shadow @Final private Inventory field_4147;

    public CraftingResultSlotMixin(Inventory inventory, int invSlot, int xPosition, int yPosition) {
        super(inventory, invSlot, xPosition, yPosition);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_3298(ItemStack par1ItemStack) {
        GameRegistry.onItemCrafted(this.player, par1ItemStack, this.field_4147);
        this.onCrafted(par1ItemStack);

        for(int var2 = 0; var2 < this.field_4147.getInvSize(); ++var2) {
            ItemStack var3 = this.field_4147.getInvStack(var2);
            if (var3 != null) {
                this.field_4147.takeInvStack(var2, 1);
                if (var3.getItem().isFood()) {
                    ItemStack var4 = ((IItem)var3.getItem()).getContainerItemStack(var3);
                    if (var4.isDamageable() && var4.getMeta() > var4.getMaxDamage()) {
                        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(this.player, var4));
                        var4 = null;
                    }

                    if (var4 != null && (!var3.getItem().method_3380(var3) || !this.player.inventory.insertStack(var4))) {
                        if (this.field_4147.getInvStack(var2) == null) {
                            this.field_4147.setInvStack(var2, var4);
                        } else {
                            this.player.dropStack(var4);
                        }
                    }
                }
            }
        }
    }
}
