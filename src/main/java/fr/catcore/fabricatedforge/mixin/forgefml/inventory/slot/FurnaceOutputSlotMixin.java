package fr.catcore.fabricatedforge.mixin.forgefml.inventory.slot;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.advancement.AchievementsAndCriterions;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.slot.FurnaceOutputSlot;
import net.minecraft.inventory.slot.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FurnaceOutputSlot.class)
public class FurnaceOutputSlotMixin extends Slot {

    @Shadow private PlayerEntity player;

    @Shadow private int amount;

    public FurnaceOutputSlotMixin(Inventory inventory, int invSlot, int xPosition, int yPosition) {
        super(inventory, invSlot, xPosition, yPosition);
    }

    /**
     * @author Minecraft
     */
    @Overwrite
    protected void onCrafted(ItemStack par1ItemStack) {
        par1ItemStack.onCraft(this.player.world, this.player, this.amount);
        if (!this.player.world.isClient) {
            int var2 = this.amount;
            float var3 = SmeltingRecipeRegistry.getInstance().method_3491(par1ItemStack.id);
            int var4;
            if (var3 == 0.0F) {
                var2 = 0;
            } else if (var3 < 1.0F) {
                var4 = MathHelper.floor((float)var2 * var3);
                if (var4 < MathHelper.ceil((float)var2 * var3) && (float)Math.random() < (float)var2 * var3 - (float)var4) {
                    ++var4;
                }

                var2 = var4;
            }

            while(var2 > 0) {
                var4 = ExperienceOrbEntity.roundToOrbSize(var2);
                var2 -= var4;
                this.player.world.spawnEntity(new ExperienceOrbEntity(this.player.world, this.player.x, this.player.y + 0.5, this.player.z + 0.5, var4));
            }
        }

        this.amount = 0;
        GameRegistry.onItemSmelted(this.player, par1ItemStack);
        if (par1ItemStack.id == Item.IRON_INGOT.id) {
            this.player.incrementStat(AchievementsAndCriterions.ACQUIRE_IRON, 1);
        }

        if (par1ItemStack.id == Item.FISH_COOKED.id) {
            this.player.incrementStat(AchievementsAndCriterions.COOK_FISH, 1);
        }
    }
}
