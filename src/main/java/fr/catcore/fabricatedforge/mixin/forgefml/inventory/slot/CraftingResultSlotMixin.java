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
    public void onTakeItem(PlayerEntity par1EntityPlayer, ItemStack par2ItemStack) {
        GameRegistry.onItemCrafted(par1EntityPlayer, par2ItemStack, this.field_4147);
        this.onCrafted(par2ItemStack);

        for(int var3 = 0; var3 < this.field_4147.getInvSize(); ++var3) {
            ItemStack var4 = this.field_4147.getInvStack(var3);
            if (var4 != null) {
                this.field_4147.takeInvStack(var3, 1);
                if (var4.getItem().isFood()) {
                    ItemStack var5 = var4.getItem().getContainerItemStack(var4);
                    if (var5.isDamageable() && var5.getMeta() > var5.getMaxDamage()) {
                        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(this.player, var5));
                        var4 = null;
                    }

                    if (var5 != null && (!var4.getItem().method_3380(var4) || !this.player.inventory.insertStack(var5))) {
                        if (this.field_4147.getInvStack(var3) == null) {
                            this.field_4147.setInvStack(var3, var5);
                        } else {
                            this.player.dropStack(var5);
                        }
                    }
                }
            }
        }
    }
}
