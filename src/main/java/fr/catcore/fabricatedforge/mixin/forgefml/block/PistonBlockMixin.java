package fr.catcore.fabricatedforge.mixin.forgefml.block;

import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.class_830;
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

    @Shadow private boolean sticky;

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

    /**
     * @author forge
     * @reason max build height
     */
    @Overwrite
    private static boolean method_559(World par0World, int par1, int par2, int par3, int par4) {
        int var5 = par1 + class_830.field_3062[par4];
        int var6 = par2 + class_830.field_3063[par4];
        int var7 = par3 + class_830.field_3064[par4];

        for(int var8 = 0; var8 < 13; ++var8) {
            if (var6 <= 0 || var6 >= par0World.getMaxBuildHeight() - 1) {
                return false;
            }

            int var9 = par0World.getBlock(var5, var6, var7);
            if (var9 != 0) {
                if (!method_554(var9, par0World, var5, var6, var7, true)) {
                    return false;
                }

                if (Block.BLOCKS[var9].getPistonInteractionType() != 1) {
                    if (var8 == 12) {
                        return false;
                    }

                    var5 += class_830.field_3062[par4];
                    var6 += class_830.field_3063[par4];
                    var7 += class_830.field_3064[par4];
                    continue;
                }
            }
            break;
        }

        return true;
    }

    /**
     * @author forge
     * @reason max build height
     */
    @Overwrite
    private boolean method_560(World par1World, int par2, int par3, int par4, int par5) {
        int var6 = par2 + class_830.field_3062[par5];
        int var7 = par3 + class_830.field_3063[par5];
        int var8 = par4 + class_830.field_3064[par5];
        int var9 = 0;

        while(var9 < 13) {
            if (var7 <= 0 || var7 >= par1World.getMaxBuildHeight() - 1) {
                return false;
            }

            int var10 = par1World.getBlock(var6, var7, var8);
            if (var10 == 0) {
                break;
            }

            if (!method_554(var10, par1World, var6, var7, var8, true)) {
                return false;
            }

            if (Block.BLOCKS[var10].getPistonInteractionType() != 1) {
                if (var9 == 12) {
                    return false;
                }

                var6 += class_830.field_3062[par5];
                var7 += class_830.field_3063[par5];
                var8 += class_830.field_3064[par5];
                ++var9;
            } else {
                Block.BLOCKS[var10].canStayPlaced(par1World, var6, var7, var8, par1World.getBlockData(var6, var7, var8), 0);
                par1World.method_3690(var6, var7, var8, 0);
                break;
            }
        }

        while(var6 != par2 || var7 != par3 || var8 != par4) {
            var9 = var6 - class_830.field_3062[par5];
            int var10 = var7 - class_830.field_3063[par5];
            int var11 = var8 - class_830.field_3064[par5];
            int var12 = par1World.getBlock(var9, var10, var11);
            int var13 = par1World.getBlockData(var9, var10, var11);
            if (var12 == this.id && var9 == par2 && var10 == par3 && var11 == par4) {
                par1World.method_3601(var6, var7, var8, Block.PISTON_EXTENSION.id, par5 | (this.sticky ? 8 : 0), false);
                par1World.method_3603(var6, var7, var8, PistonExtensionBlock.method_566(Block.PISTON_HEAD.id, par5 | (this.sticky ? 8 : 0), par5, true, false));
            } else {
                par1World.method_3601(var6, var7, var8, Block.PISTON_EXTENSION.id, var13, false);
                par1World.method_3603(var6, var7, var8, PistonExtensionBlock.method_566(var12, var13, par5, true, false));
            }

            var6 = var9;
            var7 = var10;
            var8 = var11;
        }

        return true;
    }
}
