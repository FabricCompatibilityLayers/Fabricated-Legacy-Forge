package fr.catcore.fabricatedforge.mixin.forgefml.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.DungeonFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.ChestGenHooks;
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

        for(int var10 = par3 - var7 - 1; var10 <= par3 + var7 + 1; ++var10) {
            for(int var11 = par4 - 1; var11 <= par4 + var6 + 1; ++var11) {
                for(int var12 = par5 - var8 - 1; var12 <= par5 + var8 + 1; ++var12) {
                    Material var13 = par1World.getMaterial(var10, var11, var12);
                    if (var11 == par4 - 1 && !var13.isSolid()) {
                        return false;
                    }

                    if (var11 == par4 + var6 + 1 && !var13.isSolid()) {
                        return false;
                    }

                    if ((var10 == par3 - var7 - 1 || var10 == par3 + var7 + 1 || var12 == par5 - var8 - 1 || var12 == par5 + var8 + 1)
                            && var11 == par4
                            && par1World.isAir(var10, var11, var12)
                            && par1World.isAir(var10, var11 + 1, var12)) {
                        ++var9;
                    }
                }
            }
        }

        if (var9 >= 1 && var9 <= 5) {
            for(int var17 = par3 - var7 - 1; var17 <= par3 + var7 + 1; ++var17) {
                for(int var11 = par4 + var6; var11 >= par4 - 1; --var11) {
                    for(int var12 = par5 - var8 - 1; var12 <= par5 + var8 + 1; ++var12) {
                        if (var17 != par3 - var7 - 1
                                && var11 != par4 - 1
                                && var12 != par5 - var8 - 1
                                && var17 != par3 + var7 + 1
                                && var11 != par4 + var6 + 1
                                && var12 != par5 + var8 + 1) {
                            par1World.method_3690(var17, var11, var12, 0);
                        } else if (var11 >= 0 && !par1World.getMaterial(var17, var11 - 1, var12).isSolid()) {
                            par1World.method_3690(var17, var11, var12, 0);
                        } else if (par1World.getMaterial(var17, var11, var12).isSolid()) {
                            if (var11 == par4 - 1 && par2Random.nextInt(4) != 0) {
                                par1World.method_3690(var17, var11, var12, Block.MOSSY_COBBLESTONE.id);
                            } else {
                                par1World.method_3690(var17, var11, var12, Block.STONE_BRICKS.id);
                            }
                        }
                    }
                }
            }

            for(int var18 = 0; var18 < 2; ++var18) {
                for(int var11 = 0; var11 < 3; ++var11) {
                    int var12 = par3 + par2Random.nextInt(var7 * 2 + 1) - var7;
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
                            if (var16 != null) {
                                ChestGenHooks info = ChestGenHooks.getInfo("dungeonChest");
                                WeightedRandomChestContent.method_2379(par2Random, info.getItems(par2Random), var16, info.getCount(par2Random));
                            }
                            break;
                        }
                    }
                }
            }

            par1World.method_3690(par3, par4, par5, Block.SPAWNER.id);
            MobSpawnerBlockEntity var19 = (MobSpawnerBlockEntity)par1World.getBlockEntity(par3, par4, par5);
            if (var19 != null) {
                var19.method_527(this.getRandomSpawnerMob(par2Random));
            } else {
                System.err.println("Failed to fetch mob spawner entity at (" + par3 + ", " + par4 + ", " + par5 + ")");
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    private ItemStack method_4030(Random par1Random) {
        return ChestGenHooks.getOneItem("dungeonChest", par1Random);
    }

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    private String getRandomSpawnerMob(Random par1Random) {
        return DungeonHooks.getRandomDungeonMob(par1Random);
    }
}
