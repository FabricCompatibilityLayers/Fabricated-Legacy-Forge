package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.IShearable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;

@Mixin(BlockLeaves.class)
public abstract class BlockLeavesMixin extends BlockLeavesBase implements BlockExtension, IShearable {
    protected BlockLeavesMixin(int par1, int par2, Material par3Material, boolean par4) {
        super(par1, par2, par3Material, par4);
    }

    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Expression("? == leaves.blockID")
    @WrapOperation(method = "breakBlock", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$beginLeavesDecay$check(int left, int right, Operation<Boolean> original,
                                                 @Share(namespace = "fabricated-forge", value = "block") LocalRef<Block> blockRef) {
        Block block = Block.blocksList[left];
        blockRef.set(block);
        return block != null;
    }

    @Redirect(method = "breakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;setBlockMetadata(IIII)Z"))
    private boolean forge$beginLeavesDecay(World instance, int i, int j, int k, int l,
                                           @Share(namespace = "fabricated-forge", value = "block") LocalRef<Block> blockRef) {
        ((BlockExtension) blockRef.get()).beginLeavesDecay(instance, i, j, k);
        return true;
    }

    @WrapOperation(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I"))
    private int forge$capturePos(World instance, int par2, int par3, int i, Operation<Integer> original,
                                 @Share(namespace = "fabricated-forge", value = "pos") LocalRef<ChunkCoordinates> posRef) {
        posRef.set(new ChunkCoordinates(par2, par3, i));
        return original.call(instance, par2, par3, i);
    }

    @Definition(id = "wood", field = "Lnet/minecraft/src/Block;wood:Lnet/minecraft/src/Block;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/Block;blockID:I")
    @Expression("? == wood.blockID")
    @WrapOperation(method = "updateTick", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private boolean forge$canSustainLeaves(int left, int right, Operation<Boolean> original,
                                           @Local(argsOnly = true) World par1World,
                                           @Share(namespace = "fabricated-forge", value = "pos") LocalRef<ChunkCoordinates> posRef) {
        Block block = Block.blocksList[left];
        ChunkCoordinates pos = posRef.get();
        return (block != null && ((BlockExtension) block).canSustainLeaves(par1World, pos.posX, pos.posY, pos.posZ));
    }

    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Expression("? == leaves.blockID")
    @WrapOperation(method = "updateTick", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isLeaves(int left, int right, Operation<Boolean> original,
                               @Local(argsOnly = true) World par1World,
                               @Share(namespace = "fabricated-forge", value = "pos") LocalRef<ChunkCoordinates> posRef) {
        Block block = Block.blocksList[left];
        ChunkCoordinates pos = posRef.get();
        return (block != null && ((BlockExtension) block).isLeaves(par1World, pos.posX, pos.posY, pos.posZ));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6)
    {
        super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
    }

    @Override
    public boolean isShearable(ItemStack item, World world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, World world, int x, int y, int z, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<>();
        ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z) & 3));
        return ret;
    }

    @Override
    public void beginLeavesDecay(World world, int x, int y, int z)
    {
        world.setBlockMetadata(x, y, z, world.getBlockMetadata(x, y, z) | 8);
    }

    @Override
    public boolean isLeaves(World world, int x, int y, int z)
    {
        return true;
    }
}
