package fr.catcore.fabricatedforge.mixin.forgefml.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ToolItem.class)
public class ToolItemMixin extends Item {
    @Shadow public float miningSpeed;

    protected ToolItemMixin(int id) {
        super(id);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block, int meta) {
        return ForgeHooks.isToolEffective(stack, block, meta) ? this.miningSpeed : this.getMiningSpeedMultiplier(stack, block);
    }
}
