package fr.catcore.fabricatedforge.mixin.forgefml.item;

import fr.catcore.fabricatedforge.mixininterface.IMapState;
import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NetworkSyncedItem;
import net.minecraft.item.map.MapState;
import net.minecraft.item.map.class_90;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FilledMapItem.class)
public class FilledMapItemMixin extends NetworkSyncedItem {
    protected FilledMapItemMixin(int i) {
        super(i);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public MapState getMapState(ItemStack par1ItemStack, World par2World) {
        String var3 = "map_" + par1ItemStack.getMeta();
        MapState var4 = (MapState)par2World.getOrCreateState(MapState.class, var3);
        if (var4 == null && !par2World.isClient) {
            par1ItemStack.setDamage(par2World.getIntState("map"));
            var3 = "map_" + par1ItemStack.getMeta();
            var4 = new MapState(var3);
            var4.scale = 3;
            int var5 = 128 * (1 << var4.scale);
            var4.xCenter = Math.round((float)par2World.getLevelProperties().getSpawnX() / (float)var5) * var5;
            var4.zCenter = Math.round((float)(par2World.getLevelProperties().getSpawnZ() / var5)) * var5;
            ((IMapState)var4).setC(par2World.dimension.dimensionType);
            var4.markDirty();
            par2World.replaceState(var3, var4);
        }

        return var4;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void updateColors(World par1World, Entity par2Entity, MapState par3MapData) {
        if (par1World.dimension.dimensionType == ((IMapState)par3MapData).getC() && par2Entity instanceof PlayerEntity) {
            short var4 = 128;
            short var5 = 128;
            int var6 = 1 << par3MapData.scale;
            int var7 = par3MapData.xCenter;
            int var8 = par3MapData.zCenter;
            int var9 = MathHelper.floor(par2Entity.x - (double)var7) / var6 + var4 / 2;
            int var10 = MathHelper.floor(par2Entity.z - (double)var8) / var6 + var5 / 2;
            int var11 = 128 / var6;
            if (par1World.dimension.isNether) {
                var11 /= 2;
            }

            class_90 var12 = par3MapData.method_4127((PlayerEntity)par2Entity);
            ++var12.field_4983;

            for(int var13 = var9 - var11 + 1; var13 < var9 + var11; ++var13) {
                if ((var13 & 15) == (var12.field_4983 & 15)) {
                    int var14 = 255;
                    int var15 = 0;
                    double var16 = 0.0;

                    for(int var18 = var10 - var11 - 1; var18 < var10 + var11; ++var18) {
                        if (var13 >= 0 && var18 >= -1 && var13 < var4 && var18 < var5) {
                            int var19 = var13 - var9;
                            int var20 = var18 - var10;
                            boolean var21 = var19 * var19 + var20 * var20 > (var11 - 2) * (var11 - 2);
                            int var22 = (var7 / var6 + var13 - var4 / 2) * var6;
                            int var23 = (var8 / var6 + var18 - var5 / 2) * var6;
                            int[] var24 = new int[Block.BLOCKS.length];
                            Chunk var25 = par1World.getChunkFromPos(var22, var23);
                            if (!var25.isEmpty()) {
                                int var26 = var22 & 15;
                                int var27 = var23 & 15;
                                int var28 = 0;
                                double var29 = 0.0;
                                if (par1World.dimension.isNether) {
                                    int var31 = var22 + var23 * 231871;
                                    var31 = var31 * var31 * 31287121 + var31 * 11;
                                    if ((var31 >> 20 & 1) == 0) {
                                        var24[Block.DIRT.id] += 10;
                                    } else {
                                        var24[Block.STONE_BLOCK.id] += 10;
                                    }

                                    var29 = 100.0;
                                } else {
                                    for(int var31 = 0; var31 < var6; ++var31) {
                                        for(int var32 = 0; var32 < var6; ++var32) {
                                            int var33 = var25.getHighestBlockY(var31 + var26, var32 + var27) + 1;
                                            int var34 = 0;
                                            if (var33 > 1) {
                                                boolean var35;
                                                do {
                                                    var35 = true;
                                                    var34 = var25.getBlock(var31 + var26, var33 - 1, var32 + var27);
                                                    if (var34 == 0) {
                                                        var35 = false;
                                                    } else if (var33 > 0 && var34 > 0 && Block.BLOCKS[var34].material.color == MaterialColor.AIR) {
                                                        var35 = false;
                                                    }

                                                    if (!var35) {
                                                        if (--var33 <= 0) {
                                                            break;
                                                        }

                                                        var34 = var25.getBlock(var31 + var26, var33 - 1, var32 + var27);
                                                    }
                                                } while(var33 > 0 && !var35);

                                                if (var33 > 0 && var34 != 0 && Block.BLOCKS[var34].material.isFluid()) {
                                                    int var36 = var33 - 1;
                                                    boolean var37 = false;

                                                    int var43;
                                                    do {
                                                        var43 = var25.getBlock(var31 + var26, var36--, var32 + var27);
                                                        ++var28;
                                                    } while(var36 > 0 && var43 != 0 && Block.BLOCKS[var43].material.isFluid());
                                                }
                                            }

                                            var29 += (double)var33 / (double)(var6 * var6);
                                            var24[var34]++;
                                        }
                                    }
                                }

                                var28 /= var6 * var6;
                                int var43 = 0;
                                int var32 = 0;

                                for(int var33 = 0; var33 < Block.BLOCKS.length; ++var33) {
                                    if (var24[var33] > var43) {
                                        var32 = var33;
                                        var43 = var24[var33];
                                    }
                                }

                                double var40 = (var29 - var16) * 4.0 / (double)(var6 + 4) + ((double)(var13 + var18 & 1) - 0.5) * 0.4;
                                byte var39 = 1;
                                if (var40 > 0.6) {
                                    var39 = 2;
                                }

                                if (var40 < -0.6) {
                                    var39 = 0;
                                }

                                int var36 = 0;
                                if (var32 > 0) {
                                    MaterialColor var42 = Block.BLOCKS[var32].material.color;
                                    if (var42 == MaterialColor.WATER) {
                                        var40 = (double)var28 * 0.1 + (double)(var13 + var18 & 1) * 0.2;
                                        var39 = 1;
                                        if (var40 < 0.5) {
                                            var39 = 2;
                                        }

                                        if (var40 > 0.9) {
                                            var39 = 0;
                                        }
                                    }

                                    var36 = var42.id;
                                }

                                var16 = var29;
                                if (var18 >= 0 && var19 * var19 + var20 * var20 < var11 * var11 && (!var21 || (var13 + var18 & 1) != 0)) {
                                    byte var41 = par3MapData.colors[var13 + var18 * var4];
                                    byte var38 = (byte)(var36 * 4 + var39);
                                    if (var41 != var38) {
                                        if (var14 > var18) {
                                            var14 = var18;
                                        }

                                        if (var15 < var18) {
                                            var15 = var18;
                                        }

                                        par3MapData.colors[var13 + var18 * var4] = var38;
                                    }
                                }
                            }
                        }
                    }

                    if (var14 <= var15) {
                        par3MapData.method_182(var13, var14, var15);
                    }
                }
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void onCraft(ItemStack par1ItemStack, World par2World, PlayerEntity par3EntityPlayer) {
        if (par1ItemStack.hasNbt() && par1ItemStack.getNbt().getBoolean("map_is_scaling")) {
            MapState var4 = Item.MAP.getMapState(par1ItemStack, par2World);
            par1ItemStack.setDamage(par2World.getIntState("map"));
            MapState var5 = new MapState("map_" + par1ItemStack.getMeta());
            var5.scale = (byte)(var4.scale + 1);
            if (var5.scale > 4) {
                var5.scale = 4;
            }

            var5.xCenter = var4.xCenter;
            var5.zCenter = var4.zCenter;
            ((IMapState)var5).setC(((IMapState)var4).getC());
            var5.markDirty();
            par2World.replaceState("map_" + par1ItemStack.getMeta(), var5);
        }
    }
}
