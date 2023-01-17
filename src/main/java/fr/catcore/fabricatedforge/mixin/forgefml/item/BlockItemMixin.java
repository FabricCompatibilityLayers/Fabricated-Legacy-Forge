package fr.catcore.fabricatedforge.mixin.forgefml.item;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import fr.catcore.fabricatedforge.mixininterface.IBlockItem;
import fr.catcore.fabricatedforge.mixininterface.IItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item implements IBlockItem {
    @Shadow private int blockItemId;

    @Shadow public abstract int method_3464();

    protected BlockItemMixin(int id) {
        super(id);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void fmlCtr(int par1, CallbackInfo ci) {
        ((IItem)this).isDefaultTexture(((IBlock)Block.BLOCKS[par1 + 256]).isDefaultTexture());
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_3355(
            ItemStack par1ItemStack, PlayerEntity par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10
    ) {
        int var11 = par3World.getBlock(par4, par5, par6);
        if (var11 == Block.SNOW_LAYER.id) {
            par7 = 1;
        } else if (var11 != Block.VINE.id
                && var11 != Block.TALLGRASS.id
                && var11 != Block.DEADBUSH.id
                && (Block.BLOCKS[var11] == null || !((IBlock)Block.BLOCKS[var11]).isBlockReplaceable(par3World, par4, par5, par6))) {
            if (par7 == 0) {
                --par5;
            }

            if (par7 == 1) {
                ++par5;
            }

            if (par7 == 2) {
                --par6;
            }

            if (par7 == 3) {
                ++par6;
            }

            if (par7 == 4) {
                --par4;
            }

            if (par7 == 5) {
                ++par4;
            }
        }

        if (par1ItemStack.count == 0) {
            return false;
        } else if (!par2EntityPlayer.method_4570(par4, par5, par6, par7, par1ItemStack)) {
            return false;
        } else if (par5 == 255 && Block.BLOCKS[this.blockItemId].material.isSolid()) {
            return false;
        } else if (par3World.method_3602(this.blockItemId, par4, par5, par6, false, par7, par2EntityPlayer)) {
            Block var12 = Block.BLOCKS[this.blockItemId];
            int var13 = this.getMeta(par1ItemStack.getData());
            int var14 = Block.BLOCKS[this.blockItemId].method_4185(par3World, par4, par5, par6, par7, par8, par9, par10, var13);
            if (this.placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, var14)) {
                par3World.playSound(
                        (double)((float)par4 + 0.5F),
                        (double)((float)par5 + 0.5F),
                        (double)((float)par6 + 0.5F),
                        var12.soundGroup.getPlaceId(),
                        (var12.soundGroup.getVolume() + 1.0F) / 2.0F,
                        var12.soundGroup.getPitch() * 0.8F
                );
                --par1ItemStack.count;
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public boolean method_3463(World par1World, int par2, int par3, int par4, int par5, PlayerEntity par6EntityPlayer, ItemStack par7ItemStack) {
        int var8 = par1World.getBlock(par2, par3, par4);
        if (var8 == Block.SNOW_LAYER.id) {
            par5 = 1;
        } else if (var8 != Block.VINE.id
                && var8 != Block.TALLGRASS.id
                && var8 != Block.DEADBUSH.id
                && (Block.BLOCKS[var8] == null || !((IBlock)Block.BLOCKS[var8]).isBlockReplaceable(par1World, par2, par3, par4))) {
            if (par5 == 0) {
                --par3;
            }

            if (par5 == 1) {
                ++par3;
            }

            if (par5 == 2) {
                --par4;
            }

            if (par5 == 3) {
                ++par4;
            }

            if (par5 == 4) {
                --par2;
            }

            if (par5 == 5) {
                ++par2;
            }
        }

        return par1World.method_3602(this.method_3464(), par2, par3, par4, false, par5, (Entity)null);
    }

    @Override
    public boolean placeBlockAt(
            ItemStack stack, PlayerEntity player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata
    ) {
        if (!world.method_3683(x, y, z, this.blockItemId, metadata)) {
            return false;
        } else {
            if (world.getBlock(x, y, z) == this.blockItemId) {
                Block.BLOCKS[this.blockItemId].method_419(world, x, y, z, player);
                Block.BLOCKS[this.blockItemId].method_4186(world, x, y, z, metadata);
            }

            return true;
        }
    }
}
