package fr.catcore.fabricatedforge.mixin.forgefml.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SeedItem;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SeedItem.class)
public abstract class SeedItemMixin extends Item implements IPlantable {
    @Shadow private int plantId;

    protected SeedItemMixin(int id) {
        super(id);
    }

    /**
     * @author forge
     * @reason yes
     */
    @Overwrite
    public boolean method_3355(
            ItemStack par1ItemStack, PlayerEntity par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10
    ) {
        if (par7 != 1) {
            return false;
        } else if (par2EntityPlayer.method_4570(par4, par5, par6, par7, par1ItemStack)
                && par2EntityPlayer.method_4570(par4, par5 + 1, par6, par7, par1ItemStack)) {
            int var11 = par3World.getBlock(par4, par5, par6);
            Block soil = Block.BLOCKS[var11];
            if (soil != null && soil.canSustainPlant(par3World, par4, par5, par6, ForgeDirection.UP, this) && par3World.isAir(par4, par5 + 1, par6)) {
                par3World.method_3690(par4, par5 + 1, par6, this.plantId);
                --par1ItemStack.count;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z) {
        return this.plantId == Block.NETHER_WART.id ? EnumPlantType.Nether : EnumPlantType.Crop;
    }

    @Override
    public int getPlantID(World world, int x, int y, int z) {
        return this.plantId;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z) {
        return 0;
    }
}
