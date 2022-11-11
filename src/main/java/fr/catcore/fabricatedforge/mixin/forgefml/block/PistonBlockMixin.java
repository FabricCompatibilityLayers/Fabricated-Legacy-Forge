package fr.catcore.fabricatedforge.mixin.forgefml.block;

import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin extends Block {

    @Shadow
    public static boolean method_558(int i) {
        return false;
    }

    public PistonBlockMixin(int id, Material material) {
        super(id, material);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    private boolean method_557(World par1World, int par2, int par3, int par4, int par5) {
        return par5 != 0 && par1World.method_3721(par2, par3 - 1, par4, 0)
                ? true
                : (
                par5 != 1 && par1World.method_3721(par2, par3 + 1, par4, 1)
                        ? true
                        : (
                        par5 != 2 && par1World.method_3721(par2, par3, par4 - 1, 2)
                                ? true
                                : (
                                par5 != 3 && par1World.method_3721(par2, par3, par4 + 1, 3)
                                        ? true
                                        : (
                                        par5 != 5 && par1World.method_3721(par2 + 1, par3, par4, 5)
                                                ? true
                                                : (
                                                par5 != 4 && par1World.method_3721(par2 - 1, par3, par4, 4)
                                                        ? true
                                                        : (
                                                        par1World.method_3721(par2, par3, par4, 0)
                                                                ? true
                                                                : (
                                                                par1World.method_3721(par2, par3 + 2, par4, 1)
                                                                        ? true
                                                                        : (
                                                                        par1World.method_3721(par2, par3 + 1, par4 - 1, 2)
                                                                                ? true
                                                                                : (
                                                                                par1World.method_3721(par2, par3 + 1, par4 + 1, 3)
                                                                                        ? true
                                                                                        : (
                                                                                        par1World.method_3721(par2 - 1, par3 + 1, par4, 4)
                                                                                                ? true
                                                                                                : par1World.method_3721(par2 + 1, par3 + 1, par4, 5)
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    private static boolean method_554(int par0, World par1World, int par2, int par3, int par4, boolean par5) {
        if (par0 == Block.OBSIDIAN.id) {
            return false;
        } else {
            if (par0 != Block.field_359.id && par0 != Block.field_355.id) {
                if (Block.BLOCKS[par0].method_471(par1World, par2, par3, par4) == -1.0F) {
                    return false;
                }

                if (Block.BLOCKS[par0].getPistonInteractionType() == 2) {
                    return false;
                }

                if (!par5 && Block.BLOCKS[par0].getPistonInteractionType() == 1) {
                    return false;
                }
            } else if (method_558(par1World.getBlockData(par2, par3, par4))) {
                return false;
            }

            return !par1World.hasBlockEntity(par2, par3, par4);
        }
    }
}
