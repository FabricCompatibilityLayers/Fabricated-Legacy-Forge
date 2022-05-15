package fr.catcore.fabricatedforge.mixin.forgefml.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NetworkSyncedItem;
import net.minecraft.item.map.MapState;
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
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public static MapState method_3455(short par0, World par1World) {
        MapState var3 = (MapState)par1World.method_3619(MapState.class, "map_" + par0);
        if (var3 == null) {
            int var4 = par1World.method_3660("map");
            String var2 = "map_" + var4;
            var3 = new MapState(var2);
            par1World.setItemData(var2, var3);
        }

        return var3;
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public MapState getMapState(ItemStack par1ItemStack, World par2World) {
        MapState var4 = (MapState)par2World.method_3619(MapState.class, "map_" + par1ItemStack.getMeta());
        if (var4 == null) {
            par1ItemStack.setDamage(par2World.method_3660("map"));
            String var3 = "map_" + par1ItemStack.getMeta();
            var4 = new MapState(var3);
            var4.xCenter = par2World.getLevelProperties().getSpawnX();
            var4.zCenter = par2World.getLevelProperties().getSpawnZ();
            var4.scale = 3;
            var4.setC(par2World.dimension.dimensionType);
            var4.markDirty();
            par2World.setItemData(var3, var4);
        }

        return var4;
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void updateColors(World par1World, Entity par2Entity, MapState par3MapData) {
        if (par1World.dimension.dimensionType == par3MapData.getC()) {
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

            ++par3MapData.field_206;

            for(int var12 = var9 - var11 + 1; var12 < var9 + var11; ++var12) {
                if ((var12 & 15) == (par3MapData.field_206 & 15)) {
                    int var13 = 255;
                    int var14 = 0;
                    double var15 = 0.0;

                    for(int var17 = var10 - var11 - 1; var17 < var10 + var11; ++var17) {
                        if (var12 >= 0 && var17 >= -1 && var12 < var4 && var17 < var5) {
                            int var18 = var12 - var9;
                            int var19 = var17 - var10;
                            boolean var20 = var18 * var18 + var19 * var19 > (var11 - 2) * (var11 - 2);
                            int var21 = (var7 / var6 + var12 - var4 / 2) * var6;
                            int var22 = (var8 / var6 + var17 - var5 / 2) * var6;
                            byte var23 = 0;
                            byte var24 = 0;
                            byte var25 = 0;
                            int[] var26 = new int[Block.BLOCKS.length];
                            Chunk var27 = par1World.getChunkFromPos(var21, var22);
                            if (!var27.isEmpty()) {
                                int var28 = var21 & 15;
                                int var29 = var22 & 15;
                                int var30 = 0;
                                double var31 = 0.0;
                                int var34;
                                int var35;
                                int var33;
                                int var38;
                                if (par1World.dimension.isNether) {
                                    var33 = var21 + var22 * 231871;
                                    var33 = var33 * var33 * 31287121 + var33 * 11;
                                    int var10001;
                                    if ((var33 >> 20 & 1) == 0) {
                                        var10001 = Block.DIRT.id;
                                        var26[var10001] += 10;
                                    } else {
                                        var10001 = Block.STONE_BLOCK.id;
                                        var26[var10001] += 10;
                                    }

                                    var31 = 100.0;
                                } else {
                                    for(var33 = 0; var33 < var6; ++var33) {
                                        for(var34 = 0; var34 < var6; ++var34) {
                                            var35 = var27.getHighestBlockY(var33 + var28, var34 + var29) + 1;
                                            int var36 = 0;
                                            if (var35 > 1) {
                                                boolean var37 = false;

                                                do {
                                                    var37 = true;
                                                    var36 = var27.method_3879(var33 + var28, var35 - 1, var34 + var29);
                                                    if (var36 == 0) {
                                                        var37 = false;
                                                    } else if (var35 > 0 && var36 > 0 && Block.BLOCKS[var36].material.color == MaterialColor.AIR) {
                                                        var37 = false;
                                                    }

                                                    if (!var37) {
                                                        --var35;
                                                        if (var35 <= 0) {
                                                            break;
                                                        }

                                                        var36 = var27.method_3879(var33 + var28, var35 - 1, var34 + var29);
                                                    }
                                                } while(var35 > 0 && !var37);

                                                if (var35 > 0 && var36 != 0 && Block.BLOCKS[var36].material.isFluid()) {
                                                    var38 = var35 - 1;
                                                    boolean var39 = false;

                                                    int var41;
                                                    do {
                                                        var41 = var27.method_3879(var33 + var28, var38--, var34 + var29);
                                                        ++var30;
                                                    } while(var38 > 0 && var41 != 0 && Block.BLOCKS[var41].material.isFluid());
                                                }
                                            }

                                            var31 += (double)var35 / (double)(var6 * var6);
                                            int var10002 = var26[var36]++;
                                        }
                                    }
                                }

                                var30 /= var6 * var6;
                                int var10000 = var23 / (var6 * var6);
                                var10000 = var24 / (var6 * var6);
                                var10000 = var25 / (var6 * var6);
                                var33 = 0;
                                var34 = 0;

                                for(var35 = 0; var35 < Block.BLOCKS.length; ++var35) {
                                    if (var26[var35] > var33) {
                                        var34 = var35;
                                        var33 = var26[var35];
                                    }
                                }

                                double var43 = (var31 - var15) * 4.0 / (double)(var6 + 4) + ((double)(var12 + var17 & 1) - 0.5) * 0.4;
                                byte var42 = 1;
                                if (var43 > 0.6) {
                                    var42 = 2;
                                }

                                if (var43 < -0.6) {
                                    var42 = 0;
                                }

                                var38 = 0;
                                if (var34 > 0) {
                                    MaterialColor var45 = Block.BLOCKS[var34].material.color;
                                    if (var45 == MaterialColor.WATER) {
                                        var43 = (double)var30 * 0.1 + (double)(var12 + var17 & 1) * 0.2;
                                        var42 = 1;
                                        if (var43 < 0.5) {
                                            var42 = 2;
                                        }

                                        if (var43 > 0.9) {
                                            var42 = 0;
                                        }
                                    }

                                    var38 = var45.id;
                                }

                                var15 = var31;
                                if (var17 >= 0 && var18 * var18 + var19 * var19 < var11 * var11 && (!var20 || (var12 + var17 & 1) != 0)) {
                                    byte var44 = par3MapData.colors[var12 + var17 * var4];
                                    byte var40 = (byte)(var38 * 4 + var42);
                                    if (var44 != var40) {
                                        if (var13 > var17) {
                                            var13 = var17;
                                        }

                                        if (var14 < var17) {
                                            var14 = var17;
                                        }

                                        par3MapData.colors[var12 + var17 * var4] = var40;
                                    }
                                }
                            }
                        }
                    }

                    if (var13 <= var14) {
                        par3MapData.method_182(var12, var13, var14);
                    }
                }
            }
        }

    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void onCraft(ItemStack par1ItemStack, World par2World, PlayerEntity par3EntityPlayer) {
        par1ItemStack.setDamage(par2World.method_3660("map"));
        String var4 = "map_" + par1ItemStack.getMeta();
        MapState var5 = new MapState(var4);
        par2World.setItemData(var4, var5);
        var5.xCenter = MathHelper.floor(par3EntityPlayer.x);
        var5.zCenter = MathHelper.floor(par3EntityPlayer.z);
        var5.scale = 3;
        var5.setC(par2World.dimension.dimensionType);
        var5.markDirty();
    }
}
