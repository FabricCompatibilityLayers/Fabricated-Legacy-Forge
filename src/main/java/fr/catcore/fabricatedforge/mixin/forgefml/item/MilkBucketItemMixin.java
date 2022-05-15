package fr.catcore.fabricatedforge.mixin.forgefml.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MilkBucketItem.class)
public class MilkBucketItemMixin extends Item {
    protected MilkBucketItemMixin(int id) {
        super(id);
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public ItemStack onFinishUse(ItemStack par1ItemStack, World par2World, PlayerEntity par3EntityPlayer) {
        if (!par3EntityPlayer.abilities.creativeMode) {
            --par1ItemStack.count;
        }

        if (!par2World.isClient) {
            par3EntityPlayer.curePotionEffects(par1ItemStack);
        }

        return par1ItemStack.count <= 0 ? new ItemStack(Item.BUCKET) : par1ItemStack;
    }
}
