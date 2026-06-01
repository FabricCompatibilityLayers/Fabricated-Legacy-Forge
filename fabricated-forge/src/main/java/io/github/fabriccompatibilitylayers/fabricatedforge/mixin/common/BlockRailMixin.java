/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockRailExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.minecraftforge.common.ForgeDirection.UP;

@Mixin(BlockRail.class)
public abstract class BlockRailMixin extends Block implements BlockRailExtension {
    @Shadow @Final private boolean isPowered;

    @Shadow
    public static boolean isRailBlock(int par0) {
        return false;
    }

    public BlockRailMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    /**
     * Forge: Moved render type to a field and a setter.
     * This allows for a mod to change the render type
     * for vanilla rails, and any mod rails that extend
     * this class.
     */
    private int renderType = 9;

    @Override
    public void setRenderType(int value)
    {
        renderType = value;
    }

    @ModifyReturnValue(method = "isRailBlockAt", at = @At("RETURN"))
    private static boolean forge$isRail(boolean original, @Local(ordinal = 3) int var4) {
        return isRailBlock(var4) || original;
    }

    @ModifyReturnValue(method = "isRailBlock", at = @At("RETURN"))
    private static boolean forge$instanceOfBlockRail(boolean original, @Local(argsOnly = true) int par0) {
        return Block.blocksList[par0] instanceof BlockRail || original;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getRenderType()
    {
        return renderType;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3 - 1, par4, UP);
    }

    @Redirect(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;doesBlockHaveSolidTopSurface(III)Z"))
    private boolean forge$isBlockSolidOnSide(World instance, int par2, int par3, int i) {
        return ((WorldExtension) instance).isBlockSolidOnSide(par2, par3, i, UP);
    }

    /**
     * Return true if the rail can make corners.
     * Used by placement logic.
     * @param world The world.
     * @param x The rail X coordinate.
     * @param y The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return True if the rail can make corners.
     */
    @Override
    public boolean isFlexibleRail(World world, int y, int x, int z)
    {
        return !isPowered;
    }

    /**
     * Returns true if the rail can make up and down slopes.
     * Used by placement logic.
     * @param world The world.
     * @param x The rail X coordinate.
     * @param y The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return True if the rail can make slopes.
     */
    @Override
    public boolean canMakeSlopes(World world, int x, int y, int z)
    {
        return true;
    }

    /**
     * Return the rails metadata (without the power bit if the rail uses one).
     * Can be used to make the cart think the rail something other than it is,
     * for example when making diamond junctions or switches.
     * The cart parameter will often be null unless it it called from EntityMinecart.
     *
     * Valid rail metadata is defined as follows:
     * 0x0: flat track going North-South
     * 0x1: flat track going West-East
     * 0x2: track ascending to the East
     * 0x3: track ascending to the West
     * 0x4: track ascending to the North
     * 0x5: track ascending to the South
     * 0x6: WestNorth corner (connecting East and South)
     * 0x7: EastNorth corner (connecting West and South)
     * 0x8: EastSouth corner (connecting West and North)
     * 0x9: WestSouth corner (connecting East and North)
     *
     * All directions are Notch defined.
     * In MC Beta 1.8.3 the Sun rises in the North.
     * In MC 1.0.0 the Sun rises in the East.
     *
     * @param world The world.
     * @param cart The cart asking for the metadata, null if it is not called by EntityMinecart.
     * @param y The rail X coordinate.
     * @param x The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return The metadata.
     */
    @Override
    public int getBasicRailMetadata(IBlockAccess world, EntityMinecart cart, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        if(isPowered)
        {
            meta = meta & 7;
        }
        return meta;
    }

    /**
     * Returns the max speed of the rail at the specified position.
     * @param world The world.
     * @param cart The cart on the rail, may be null.
     * @param x The rail X coordinate.
     * @param y The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return The max speed of the current rail.
     */
    @Override
    public float getRailMaxSpeed(World world, EntityMinecart cart, int y, int x, int z)
    {
        return 0.4f;
    }

    /**
     * This function is called by any minecart that passes over this rail.
     * It is called once per update tick that the minecart is on the rail.
     * @param world The world.
     * @param cart The cart on the rail.
     * @param y The rail X coordinate.
     * @param x The rail Y coordinate.
     * @param z The rail Z coordinate.
     */
    @Override
    public void onMinecartPass(World world, EntityMinecart cart, int y, int x, int z)
    {
    }

    /**
     * Return true if this rail uses the 4th bit as a power bit.
     * Avoid using this function when getBasicRailMetadata() can be used instead.
     * The only reason to use this function is if you wish to change the rails metadata.
     * @param world The world.
     * @param x The rail X coordinate.
     * @param y The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return True if the 4th bit is a power bit.
     */
    @Override
    public boolean hasPowerBit(World world, int x, int y, int z)
    {
        return isPowered;
    }
}
