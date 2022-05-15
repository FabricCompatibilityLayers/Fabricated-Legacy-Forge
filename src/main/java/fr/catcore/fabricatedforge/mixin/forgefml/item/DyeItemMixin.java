package fr.catcore.fabricatedforge.mixin.forgefml.item;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
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
     */
    @Overwrite
    public boolean method_3355(ItemStack par1ItemStack, PlayerEntity par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (!par2EntityPlayer.method_3204(par4, par5, par6)) {
            return false;
        } else {
            int var11;
            int var12;
            if (par1ItemStack.getMeta() == 15) {
                var11 = par3World.getBlock(par4, par5, par6);
                BonemealEvent event = new BonemealEvent(par2EntityPlayer, par3World, var11, par4, par5, par6);
                if (MinecraftForge.EVENT_BUS.post(event)) {
                    return false;
                }

                if (event.isHandeled()) {
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

                if (var11 == Block.BROWN_MUSHROOM.id || var11 == Block.RED_MUSHROOM.id) {
                    if (!par3World.isClient && ((MushroomBlock)Block.BLOCKS[var11]).method_345(par3World, par4, par5, par6, par3World.random)) {
                        --par1ItemStack.count;
                    }

                    return true;
                }

                if (var11 == Block.MELON_STEM.id || var11 == Block.PUMPKIN_STEM.id) {
                    if (par3World.getBlockData(par4, par5, par6) == 7) {
                        return false;
                    } else {
                        if (!par3World.isClient) {
                            ((AttachedStemBlock)Block.BLOCKS[var11]).method_385(par3World, par4, par5, par6);
                            --par1ItemStack.count;
                        }

                        return true;
                    }
                }

                if (var11 == Block.WHEAT.id) {
                    if (par3World.getBlockData(par4, par5, par6) == 7) {
                        return false;
                    }

                    if (!par3World.isClient) {
                        ((CropBlock)Block.WHEAT).method_293(par3World, par4, par5, par6);
                        --par1ItemStack.count;
                    }

                    return true;
                }

                if (var11 == Block.COCOA.id) {
                    if (!par3World.isClient) {
                        par3World.method_3672(par4, par5, par6, 8 | HorizontalFacingBlock.method_297(par3World.getBlockData(par4, par5, par6)));
                        --par1ItemStack.count;
                    }

                    return true;
                }

                if (var11 == Block.GRASS_BLOCK.id) {
                    if (!par3World.isClient) {
                        --par1ItemStack.count;

                        label137:
                        for(var12 = 0; var12 < 128; ++var12) {
                            int var13 = par4;
                            int var14 = par5 + 1;
                            int var15 = par6;

                            for(int var16 = 0; var16 < var12 / 16; ++var16) {
                                var13 += RANDOM.nextInt(3) - 1;
                                var14 += (RANDOM.nextInt(3) - 1) * RANDOM.nextInt(3) / 2;
                                var15 += RANDOM.nextInt(3) - 1;
                                if (par3World.getBlock(var13, var14 - 1, var15) != Block.GRASS_BLOCK.id || par3World.method_3783(var13, var14, var15)) {
                                    continue label137;
                                }
                            }

                            if (par3World.getBlock(var13, var14, var15) == 0) {
                                if (RANDOM.nextInt(10) != 0) {
                                    if (Block.TALLGRASS.method_450(par3World, var13, var14, var15)) {
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
            } else if (par1ItemStack.getMeta() == 3) {
                var11 = par3World.getBlock(par4, par5, par6);
                var12 = par3World.getBlockData(par4, par5, par6);
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
                        par3World.method_3690(par4, par5, par6, Block.COCOA.id);
                        if (par3World.getBlock(par4, par5, par6) == Block.COCOA.id) {
                            Block.BLOCKS[Block.COCOA.id].method_409(par3World, par4, par5, par6, par7, par8, par9, par10);
                        }

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
