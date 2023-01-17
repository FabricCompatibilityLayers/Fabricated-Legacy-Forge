package fr.catcore.fabricatedforge.mixin.forgefml.item;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.BonemealEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DyeItem.class)
public class DyeItemMixin extends Item {
    protected DyeItemMixin(int id) {
        super(id);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_3355(
            ItemStack par1ItemStack, PlayerEntity par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10
    ) {
        if (!par2EntityPlayer.method_4570(par4, par5, par6, par7, par1ItemStack)) {
            return false;
        } else {
            if (par1ItemStack.getData() == 15) {
                int var11 = par3World.getBlock(par4, par5, par6);
                BonemealEvent event = new BonemealEvent(par2EntityPlayer, par3World, var11, par4, par5, par6);
                if (MinecraftForge.EVENT_BUS.post(event)) {
                    return false;
                }

                if (event.getResult() == Event.Result.ALLOW) {
                    if (!par3World.isClient) {
                        --par1ItemStack.count;
                    }

                    return true;
                }

                if (var11 == Block.SAPLING.id) {
                    if (!par3World.isClient) {
                        ((SaplingBlock)Block.SAPLING).method_382(par3World, par4, par5, par6, par3World.random);
                        --par1ItemStack.count;
                    }

                    return true;
                }

                if (var11 != Block.BROWN_MUSHROOM.id && var11 != Block.RED_MUSHROOM.id) {
                    if (var11 != Block.MELON_STEM.id && var11 != Block.PUMPKIN_STEM.id) {
                        if (var11 > 0 && Block.BLOCKS[var11] instanceof CropBlock) {
                            if (par3World.getBlockData(par4, par5, par6) == 7) {
                                return false;
                            }

                            if (!par3World.isClient) {
                                ((CropBlock)Block.BLOCKS[var11]).method_293(par3World, par4, par5, par6);
                                --par1ItemStack.count;
                            }

                            return true;
                        }

                        if (var11 == Block.COCOA.id) {
                            if (!par3World.isClient) {
                                par3World.method_3672(par4, par5, par6, 8 | FacingBlock.getRotation(par3World.getBlockData(par4, par5, par6)));
                                --par1ItemStack.count;
                            }

                            return true;
                        }

                        if (var11 == Block.GRASS_BLOCK.id) {
                            if (!par3World.isClient) {
                                --par1ItemStack.count;

                                label135:
                                for(int var12 = 0; var12 < 128; ++var12) {
                                    int var13 = par4;
                                    int var14 = par5 + 1;
                                    int var15 = par6;

                                    for(int var16 = 0; var16 < var12 / 16; ++var16) {
                                        var13 += RANDOM.nextInt(3) - 1;
                                        var14 += (RANDOM.nextInt(3) - 1) * RANDOM.nextInt(3) / 2;
                                        var15 += RANDOM.nextInt(3) - 1;
                                        if (par3World.getBlock(var13, var14 - 1, var15) != Block.GRASS_BLOCK.id || par3World.isBlockSolid(var13, var14, var15)) {
                                            continue label135;
                                        }
                                    }

                                    if (par3World.getBlock(var13, var14, var15) == 0) {
                                        if (RANDOM.nextInt(10) != 0) {
                                            if (Block.TALLGRASS.canStayPlaced(par3World, var13, var14, var15)) {
                                                par3World.method_3683(var13, var14, var15, Block.TALLGRASS.id, 1);
                                            }
                                        } else {
                                            ForgeHooks.plantGrass(par3World, var13, var14, var15);
                                        }
                                    }
                                }
                            }

                            return true;
                        }

                        return false;
                    }

                    if (par3World.getBlockData(par4, par5, par6) == 7) {
                        return false;
                    }

                    if (!par3World.isClient) {
                        ((StemBlock)Block.BLOCKS[var11]).method_385(par3World, par4, par5, par6);
                        --par1ItemStack.count;
                    }

                    return true;
                }

                if (!par3World.isClient && ((MushroomPlantBlock)Block.BLOCKS[var11]).method_345(par3World, par4, par5, par6, par3World.random)) {
                    --par1ItemStack.count;
                }

                return true;
            } else if (par1ItemStack.getData() == 3) {
                int var11 = par3World.getBlock(par4, par5, par6);
                int var12 = par3World.getBlockData(par4, par5, par6);
                if (var11 == Block.LOG.id && LogBlock.method_495(var12) == 3) {
                    if (par7 == 0) {
                        return false;
                    }

                    if (par7 == 1) {
                        return false;
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

                    if (par3World.isAir(par4, par5, par6)) {
                        int var13 = Block.BLOCKS[Block.COCOA.id].method_4185(par3World, par4, par5, par6, par7, par8, par9, par10, 0);
                        par3World.method_3683(par4, par5, par6, Block.COCOA.id, var13);
                        if (!par2EntityPlayer.abilities.creativeMode) {
                            --par1ItemStack.count;
                        }
                    }

                    return true;
                }
            }

            return false;
        }
    }
}
