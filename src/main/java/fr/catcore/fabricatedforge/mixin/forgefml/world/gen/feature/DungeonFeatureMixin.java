package fr.catcore.fabricatedforge.mixin.forgefml.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.DungeonFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.DungeonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(DungeonFeature.class)
public abstract class DungeonFeatureMixin extends Feature {

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_4028(World par1World, Random par2Random, int par3, int par4, int par5) {
        byte var6 = 3;
        int var7 = par2Random.nextInt(2) + 2;
        int var8 = par2Random.nextInt(2) + 2;
        int var9 = 0;

        int var10;
        int var11;
        int var12;
        for(var10 = par3 - var7 - 1; var10 <= par3 + var7 + 1; ++var10) {
            for(var11 = par4 - 1; var11 <= par4 + var6 + 1; ++var11) {
                for(var12 = par5 - var8 - 1; var12 <= par5 + var8 + 1; ++var12) {
                    Material var13 = par1World.getMaterial(var10, var11, var12);
                    if (var11 == par4 - 1 && !var13.isSolid()) {
                        return false;
                    }

                    if (var11 == par4 + var6 + 1 && !var13.isSolid()) {
                        return false;
                    }

                    if ((var10 == par3 - var7 - 1 || var10 == par3 + var7 + 1 || var12 == par5 - var8 - 1 || var12 == par5 + var8 + 1) && var11 == par4 && par1World.isAir(var10, var11, var12) && par1World.isAir(var10, var11 + 1, var12)) {
                        ++var9;
                    }
                }
            }
        }

        if (var9 >= 1 && var9 <= 5) {
            for(var10 = par3 - var7 - 1; var10 <= par3 + var7 + 1; ++var10) {
                for(var11 = par4 + var6; var11 >= par4 - 1; --var11) {
                    for(var12 = par5 - var8 - 1; var12 <= par5 + var8 + 1; ++var12) {
                        if (var10 != par3 - var7 - 1 && var11 != par4 - 1 && var12 != par5 - var8 - 1 && var10 != par3 + var7 + 1 && var11 != par4 + var6 + 1 && var12 != par5 + var8 + 1) {
                            par1World.method_3690(var10, var11, var12, 0);
                        } else if (var11 >= 0 && !par1World.getMaterial(var10, var11 - 1, var12).isSolid()) {
                            par1World.method_3690(var10, var11, var12, 0);
                        } else if (par1World.getMaterial(var10, var11, var12).isSolid()) {
                            if (var11 == par4 - 1 && par2Random.nextInt(4) != 0) {
                                par1World.method_3690(var10, var11, var12, Block.MOSSY_COBBLESTONE.id);
                            } else {
                                par1World.method_3690(var10, var11, var12, Block.STONE_BRICKS.id);
                            }
                        }
                    }
                }
            }

            label116:
            for(var10 = 0; var10 < 2; ++var10) {
                for(var11 = 0; var11 < 3; ++var11) {
                    var12 = par3 + par2Random.nextInt(var7 * 2 + 1) - var7;
                    int var14 = par5 + par2Random.nextInt(var8 * 2 + 1) - var8;
                    if (par1World.isAir(var12, par4, var14)) {
                        int var15 = 0;
                        if (par1World.getMaterial(var12 - 1, par4, var14).isSolid()) {
                            ++var15;
                        }

                        if (par1World.getMaterial(var12 + 1, par4, var14).isSolid()) {
                            ++var15;
                        }

                        if (par1World.getMaterial(var12, par4, var14 - 1).isSolid()) {
                            ++var15;
                        }

                        if (par1World.getMaterial(var12, par4, var14 + 1).isSolid()) {
                            ++var15;
                        }

                        if (var15 == 1) {
                            par1World.method_3690(var12, par4, var14, Block.field_407.id);
                            ChestBlockEntity var16 = (ChestBlockEntity)par1World.getBlockEntity(var12, par4, var14);
                            if (var16 == null) {
                                break;
                            }

                            int var17 = 0;

                            while(true) {
                                if (var17 >= DungeonHooks.getDungeonLootTries()) {
                                    continue label116;
                                }

                                ItemStack var18 = DungeonHooks.getRandomDungeonLoot(par2Random);
                                if (var18 != null) {
                                    var16.setInvStack(par2Random.nextInt(var16.getInvSize()), var18);
                                }

                                ++var17;
                            }
                        }
                    }
                }
            }

            par1World.method_3690(par3, par4, par5, Block.SPAWNER.id);
            MobSpawnerBlockEntity var19 = (MobSpawnerBlockEntity)par1World.getBlockEntity(par3, par4, par5);
            if (var19 != null) {
                var19.method_527(DungeonHooks.getRandomDungeonMob(par2Random));
            } else {
                System.err.println("Failed to fetch mob spawner entity at (" + par3 + ", " + par4 + ", " + par5 + ")");
            }

            return true;
        } else {
            return false;
        }
    }
}
