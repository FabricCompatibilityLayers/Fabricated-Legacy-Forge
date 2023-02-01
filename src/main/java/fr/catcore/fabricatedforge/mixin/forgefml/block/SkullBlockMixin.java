package fr.catcore.fabricatedforge.mixin.forgefml.block;

import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;

@Mixin(SkullBlock.class)
public abstract class SkullBlockMixin extends BlockWithEntity {
    protected SkullBlockMixin(int i, Material material) {
        super(i, material);
    }

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    public void method_412(World par1World, int par2, int par3, int par4, int par5, PlayerEntity par6EntityPlayer) {
        if (par6EntityPlayer.abilities.creativeMode) {
            par5 |= 8;
            par1World.method_3672(par2, par3, par4, par5);
        }

        this.canStayPlaced(par1World, par2, par3, par4, par5, 0);
        super.method_412(par1World, par2, par3, par4, par5, par6EntityPlayer);
    }

    /**
     * @author forge
     * @reason report call
     */
    @Overwrite
    public void method_411(World par1World, int par2, int par3, int par4, int par5, int par6) {
        super.method_411(par1World, par2, par3, par4, par5, par6);
    }

    /**
     * @author forge
     * @reason report call
     */
    @Overwrite
    public void method_410(World world, int i, int j, int k, int l, float f, int m) {
        super.method_410(world, i, j, k, l, f, m);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList();
        if ((metadata & 8) == 0) {
            ItemStack var7 = new ItemStack(Item.SKULL.id, 1, this.method_463(world, x, y, z));
            SkullBlockEntity var8 = (SkullBlockEntity)world.getBlockEntity(x, y, z);
            if (var8 == null) {
                return drops;
            }

            if (var8.getSkullType() == 3 && var8.method_4214() != null && var8.method_4214().length() > 0) {
                var7.setNbt(new NbtCompound());
                var7.getNbt().putString("SkullOwner", var8.method_4214());
            }

            drops.add(var7);
        }

        return drops;
    }
}
