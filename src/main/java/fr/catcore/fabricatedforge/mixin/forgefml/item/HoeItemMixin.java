package fr.catcore.fabricatedforge.mixin.forgefml.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.UseHoeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(HoeItem.class)
public class HoeItemMixin extends Item {
    protected HoeItemMixin(int id) {
        super(id);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_3355(ItemStack par1ItemStack, PlayerEntity par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (!par2EntityPlayer.method_3204(par4, par5, par6)) {
            return false;
        } else {
            UseHoeEvent event = new UseHoeEvent(par2EntityPlayer, par1ItemStack, par3World, par4, par5, par6);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return false;
            } else if (event.isHandeled()) {
                par1ItemStack.method_3406(1, par2EntityPlayer);
                return true;
            } else {
                int var11 = par3World.getBlock(par4, par5, par6);
                int var12 = par3World.getBlock(par4, par5 + 1, par6);
                if ((par7 == 0 || var12 != 0 || var11 != Block.GRASS_BLOCK.id) && var11 != Block.DIRT.id) {
                    return false;
                } else {
                    Block var13 = Block.FARMLAND;
                    par3World.playSound((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), var13.soundGroup.method_487(), (var13.soundGroup.method_485() + 1.0F) / 2.0F, var13.soundGroup.method_486() * 0.8F);
                    if (par3World.isClient) {
                        return true;
                    } else {
                        par3World.method_3690(par4, par5, par6, var13.id);
                        par1ItemStack.method_3406(1, par2EntityPlayer);
                        return true;
                    }
                }
            }
        }
    }
}
