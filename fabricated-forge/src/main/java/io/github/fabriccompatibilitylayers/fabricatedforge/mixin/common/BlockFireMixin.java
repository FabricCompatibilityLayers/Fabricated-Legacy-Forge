package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockFireExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.forged.ForgedBlock;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static net.minecraftforge.common.ForgeDirection.*;

@Mixin(BlockFire.class)
public abstract class BlockFireMixin extends Block implements BlockFireExtension {
    @Shadow private int[] abilityToCatchFire;

    @Shadow private int[] chanceToEncourageFire;

    public BlockFireMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @Inject(method = "initializeBlock", at = @At("HEAD"))
    private void forge$initializeBlock(CallbackInfo ci) {
        abilityToCatchFire = ForgedBlock.blockFlammability;
        chanceToEncourageFire = ForgedBlock.blockFireSpreadSpeed;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void setBurnRate(int par1, int par2, int par3)
    {
        BlockAccessor.callSetBurnProperties(par1, par2, par3);
    }

    @WrapOperation(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 0))
    private int forge$isFireSource(World par1World, int par2, int i, int par4, Operation<Integer> original, @Local(argsOnly = true, ordinal = 1) int par3) {
        int blockId = original.call(par1World, par2, i, par4);
        Block base = Block.blocksList[blockId];

        return (base != null && ((BlockExtension) base).isFireSource(par1World, par2, par3 - 1, par4, par1World.getBlockMetadata(par2, par3 - 1, par4), UP))
                ? Block.netherrack.blockID : 0;
    }

    @ModifyConstant(method = "updateTick", constant = @Constant(classValue = WorldProviderEnd.class))
    private Class<?> forge$hackCheck(Object instance, Class<?> type) {
        return Number.class;
    }

    @Redirect(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z"))
    private boolean forge$canBlockCatchFire(BlockFire instance, IBlockAccess par2, int par3, int par4, int i) {
        return ((BlockFireExtension) instance).canBlockCatchFire(par2, par3, par4, i, UP);
    }

    @Redirect(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;tryToCatchBlockOnFire(Lnet/minecraft/src/World;IIIILjava/util/Random;I)V", ordinal = 0))
    private void forge$tryToCatchBlockOnFireWest(BlockFire instance, World par2, int par3, int par4, int par5, int par6Random, Random par7, int i) {
        ((BlockFireExtension) instance).tryToCatchBlockOnFire(par2, par3, par4, par5, par6Random, par7, i, WEST);
    }

    @Redirect(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;tryToCatchBlockOnFire(Lnet/minecraft/src/World;IIIILjava/util/Random;I)V", ordinal = 1))
    private void forge$tryToCatchBlockOnFireEast(BlockFire instance, World par2, int par3, int par4, int par5, int par6Random, Random par7, int i) {
        ((BlockFireExtension) instance).tryToCatchBlockOnFire(par2, par3, par4, par5, par6Random, par7, i, EAST);
    }

    @Redirect(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;tryToCatchBlockOnFire(Lnet/minecraft/src/World;IIIILjava/util/Random;I)V", ordinal = 2))
    private void forge$tryToCatchBlockOnFireUp(BlockFire instance, World par2, int par3, int par4, int par5, int par6Random, Random par7, int i) {
        ((BlockFireExtension) instance).tryToCatchBlockOnFire(par2, par3, par4, par5, par6Random, par7, i, UP);
    }

    @Redirect(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;tryToCatchBlockOnFire(Lnet/minecraft/src/World;IIIILjava/util/Random;I)V", ordinal = 3))
    private void forge$tryToCatchBlockOnFireDown(BlockFire instance, World par2, int par3, int par4, int par5, int par6Random, Random par7, int i) {
        ((BlockFireExtension) instance).tryToCatchBlockOnFire(par2, par3, par4, par5, par6Random, par7, i, DOWN);
    }

    @Redirect(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;tryToCatchBlockOnFire(Lnet/minecraft/src/World;IIIILjava/util/Random;I)V", ordinal = 4))
    private void forge$tryToCatchBlockOnFireSouth(BlockFire instance, World par2, int par3, int par4, int par5, int par6Random, Random par7, int i) {
        ((BlockFireExtension) instance).tryToCatchBlockOnFire(par2, par3, par4, par5, par6Random, par7, i, SOUTH);
    }

    @Redirect(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;tryToCatchBlockOnFire(Lnet/minecraft/src/World;IIIILjava/util/Random;I)V", ordinal = 5))
    private void forge$tryToCatchBlockOnFireNorth(BlockFire instance, World par2, int par3, int par4, int par5, int par6Random, Random par7, int i) {
        ((BlockFireExtension) instance).tryToCatchBlockOnFire(par2, par3, par4, par5, par6Random, par7, i, NORTH);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void tryToCatchBlockOnFire(World par1World, int par2, int par3, int par4, int par5, Random par6Random, int par7)
    {
        tryToCatchBlockOnFire(par1World, par2, par3, par4, par5, par6Random, par7, UP);
    }

    /**
     * Originally private but made public for ease of writing mixins.
     */
    @Override
    public void tryToCatchBlockOnFire(World par1World, int par2, int par3, int par4, int par5, Random par6Random, int par7, ForgeDirection face)
    {
        int var8 = 0;
        Block block = Block.blocksList[par1World.getBlockId(par2, par3, par4)];
        if (block != null)
        {
            var8 = ((BlockExtension) block).getFlammability(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), face);
        }

        if (par6Random.nextInt(par5) < var8) {
            boolean var9 = par1World.getBlockId(par2, par3, par4) == Block.tnt.blockID;
            if (par6Random.nextInt(par7 + 10) < 5 && !par1World.canLightningStrikeAt(par2, par3, par4)) {
                int var10 = par7 + par6Random.nextInt(5) / 4;
                if (var10 > 15) {
                    var10 = 15;
                }

                par1World.setBlockAndMetadataWithNotify(par2, par3, par4, this.blockID, var10);
            } else {
                par1World.setBlockWithNotify(par2, par3, par4, 0);
            }

            if (var9) {
                Block.tnt.onBlockDestroyedByPlayer(par1World, par2, par3, par4, 1);
            }
        }
    }

    @Redirect(method = "canNeighborBurn", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 0))
    private boolean forge$canBlockCatchFireWest$canNeighborBurn(BlockFire instance, IBlockAccess par2, int par3, int par4, int i) {
        return ((BlockFireExtension) instance).canBlockCatchFire(par2, par3, par4, i, WEST);
    }

    @Redirect(method = "canNeighborBurn", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 1))
    private boolean forge$canBlockCatchFireEast$canNeighborBurn(BlockFire instance, IBlockAccess par2, int par3, int par4, int i) {
        return ((BlockFireExtension) instance).canBlockCatchFire(par2, par3, par4, i, EAST);
    }

    @Redirect(method = "canNeighborBurn", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 2))
    private boolean forge$canBlockCatchFireUp$canNeighborBurn(BlockFire instance, IBlockAccess par2, int par3, int par4, int i) {
        return ((BlockFireExtension) instance).canBlockCatchFire(par2, par3, par4, i, UP);
    }

    @Redirect(method = "canNeighborBurn", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 3))
    private boolean forge$canBlockCatchFireDown$canNeighborBurn(BlockFire instance, IBlockAccess par2, int par3, int par4, int i) {
        return ((BlockFireExtension) instance).canBlockCatchFire(par2, par3, par4, i, DOWN);
    }

    @Redirect(method = "canNeighborBurn", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 4))
    private boolean forge$canBlockCatchFireSouth$canNeighborBurn(BlockFire instance, IBlockAccess par2, int par3, int par4, int i) {
        return ((BlockFireExtension) instance).canBlockCatchFire(par2, par3, par4, i, SOUTH);
    }

    @Redirect(method = "canNeighborBurn", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 5))
    private boolean forge$canBlockCatchFireNorth$canNeighborBurn(BlockFire instance, IBlockAccess par2, int par3, int par4, int i) {
        return ((BlockFireExtension) instance).canBlockCatchFire(par2, par3, par4, i, NORTH);
    }

    @Redirect(method = "getChanceOfNeighborsEncouragingFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;getChanceToEncourageFire(Lnet/minecraft/src/World;IIII)I", ordinal = 0))
    private int forge$getChanceToEncourageFireWest(BlockFire instance, World par2, int par3, int par4, int par5, int i) {
        return ((BlockFireExtension) instance).getChanceToEncourageFire(par2, par3, par4, par5, i, WEST);
    }

    @Redirect(method = "getChanceOfNeighborsEncouragingFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;getChanceToEncourageFire(Lnet/minecraft/src/World;IIII)I", ordinal = 1))
    private int forge$getChanceToEncourageFireEast(BlockFire instance, World par2, int par3, int par4, int par5, int i) {
        return ((BlockFireExtension) instance).getChanceToEncourageFire(par2, par3, par4, par5, i, EAST);
    }

    @Redirect(method = "getChanceOfNeighborsEncouragingFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;getChanceToEncourageFire(Lnet/minecraft/src/World;IIII)I", ordinal = 2))
    private int forge$getChanceToEncourageFireUp(BlockFire instance, World par2, int par3, int par4, int par5, int i) {
        return ((BlockFireExtension) instance).getChanceToEncourageFire(par2, par3, par4, par5, i, UP);
    }

    @Redirect(method = "getChanceOfNeighborsEncouragingFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;getChanceToEncourageFire(Lnet/minecraft/src/World;IIII)I", ordinal = 3))
    private int forge$getChanceToEncourageFireDown(BlockFire instance, World par2, int par3, int par4, int par5, int i) {
        return ((BlockFireExtension) instance).getChanceToEncourageFire(par2, par3, par4, par5, i, DOWN);
    }

    @Redirect(method = "getChanceOfNeighborsEncouragingFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;getChanceToEncourageFire(Lnet/minecraft/src/World;IIII)I", ordinal = 4))
    private int forge$getChanceToEncourageFireSouth(BlockFire instance, World par2, int par3, int par4, int par5, int i) {
        return ((BlockFireExtension) instance).getChanceToEncourageFire(par2, par3, par4, par5, i, SOUTH);
    }

    @Redirect(method = "getChanceOfNeighborsEncouragingFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;getChanceToEncourageFire(Lnet/minecraft/src/World;IIII)I", ordinal = 5))
    private int forge$getChanceToEncourageFireNorth(BlockFire instance, World par2, int par3, int par4, int par5, int i) {
        return ((BlockFireExtension) instance).getChanceToEncourageFire(par2, par3, par4, par5, i, NORTH);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canBlockCatchFire(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return canBlockCatchFire(par1IBlockAccess, par2, par3, par4, UP);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getChanceToEncourageFire(World par1World, int par2, int par3, int par4, int par5)
    {
        return getChanceToEncourageFire(par1World, par2, par3, par4, par5, UP);
    }

    @Redirect(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 0))
    private boolean forge$canBlockCatchFireUp$randomDisplayTick(BlockFire instance, IBlockAccess par2, int par3, int par4, int i) {
        return ((BlockFireExtension) instance).canBlockCatchFire(par2, par3, par4, i, UP);
    }

    @Redirect(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 1))
    private boolean forge$canBlockCatchFireEast$randomDisplayTick(BlockFire instance, IBlockAccess par2, int par3, int par4, int i) {
        return ((BlockFireExtension) instance).canBlockCatchFire(par2, par3, par4, i, EAST);
    }

    @Redirect(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 2))
    private boolean forge$canBlockCatchFireWest$randomDisplayTick(BlockFire instance, IBlockAccess par2, int par3, int par4, int i) {
        return ((BlockFireExtension) instance).canBlockCatchFire(par2, par3, par4, i, WEST);
    }

    @Redirect(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 3))
    private boolean forge$canBlockCatchFireSouth$randomDisplayTick(BlockFire instance, IBlockAccess par2, int par3, int par4, int i) {
        return ((BlockFireExtension) instance).canBlockCatchFire(par2, par3, par4, i, SOUTH);
    }

    @Redirect(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 4))
    private boolean forge$canBlockCatchFireNorth$randomDisplayTick(BlockFire instance, IBlockAccess par2, int par3, int par4, int i) {
        return ((BlockFireExtension) instance).canBlockCatchFire(par2, par3, par4, i, NORTH);
    }

    @Redirect(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockFire;canBlockCatchFire(Lnet/minecraft/src/IBlockAccess;III)Z", ordinal = 5))
    private boolean forge$canBlockCatchFireDown$randomDisplayTick(BlockFire instance, IBlockAccess par2, int par3, int par4, int i) {
        return ((BlockFireExtension) instance).canBlockCatchFire(par2, par3, par4, i, DOWN);
    }

    /**
     * Side sensitive version that calls the block function.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param face The side the fire is coming from
     * @return True if the face can catch fire.
     */
    @Override
    public boolean canBlockCatchFire(IBlockAccess world, int x, int y, int z, ForgeDirection face)
    {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        if (block != null)
        {
            return ((BlockExtension) block).isFlammable(world, x, y, z, world.getBlockMetadata(x, y, z), face);
        }
        return false;
    }

    /**
     * Side sensitive version that calls the block function.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param oldChance The previous maximum chance.
     * @param face The side the fire is coming from
     * @return The chance of the block catching fire, or oldChance if it is higher
     */
    @Override
    public int getChanceToEncourageFire(World world, int x, int y, int z, int oldChance, ForgeDirection face)
    {
        int newChance = 0;
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        if (block != null)
        {
            newChance = ((BlockExtension) block).getFireSpreadSpeed(world, x, y, z, world.getBlockMetadata(x, y, z), face);
        }
        return (newChance > oldChance ? newChance : oldChance);
    }
}
