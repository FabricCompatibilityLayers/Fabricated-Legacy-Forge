package fr.catcore.fabricatedforge.mixin.forgefml.entity;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.MobSpawnerHelper;
import net.minecraft.entity.SpawnEntry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpecialSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

@Mixin(MobSpawnerHelper.class)
public abstract class MobSpawnerHelperMixin {

    @Shadow private static HashMap field_4593;

    @Shadow
    protected static Vec3i getRandomPosInChunk(World world, int i, int j) {
        return null;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public static final int method_3796(ServerWorld par0WorldServer, boolean par1, boolean par2) {
        if (!par1 && !par2) {
            return 0;
        } else {
            field_4593.clear();

            int var3;
            int var6;
            int var7;
            ChunkPos var37;
            for(var3 = 0; var3 < par0WorldServer.playerEntities.size(); ++var3) {
                PlayerEntity var4 = (PlayerEntity)par0WorldServer.playerEntities.get(var3);
                int var5 = MathHelper.floor(var4.x / 16.0);
                var6 = MathHelper.floor(var4.z / 16.0);
                var7 = 8;

                for(int var8 = -var7; var8 <= var7; ++var8) {
                    for(int var9 = -var7; var9 <= var7; ++var9) {
                        boolean var10 = var8 == -var7 || var8 == var7 || var9 == -var7 || var9 == var7;
                        var37 = new ChunkPos(var8 + var5, var9 + var6);
                        if (!var10) {
                            field_4593.put(var37, false);
                        } else if (!field_4593.containsKey(var37)) {
                            field_4593.put(var37, true);
                        }
                    }
                }
            }

            var3 = 0;
            BlockPos var31 = par0WorldServer.getWorldSpawnPos();
            EntityCategory[] var32 = EntityCategory.values();
            var6 = var32.length;

            label120:
            for(var7 = 0; var7 < var6; ++var7) {
                EntityCategory var34 = var32[var7];
                if ((!var34.isHostile() || par2) && (var34.isHostile() || par1) && par0WorldServer.getPersistentEntityCount(var34.getCategoryClass()) <= var34.getSpawnCap() * field_4593.size() / 256) {
                    ArrayList<ChunkPos> tmp = new ArrayList<>(field_4593.keySet());
                    Collections.shuffle(tmp);
                    Iterator<ChunkPos> var35 = tmp.iterator();

                    label117:
                    while(true) {
                        int var12;
                        int var13;
                        int var14;
                        do {
                            do {
                                do {
                                    if (!var35.hasNext()) {
                                        continue label120;
                                    }

                                    var37 = (ChunkPos)var35.next();
                                } while((Boolean)field_4593.get(var37));

                                Vec3i var36 = getRandomPosInChunk(par0WorldServer, var37.x, var37.z);
                                var12 = var36.x;
                                var13 = var36.y;
                                var14 = var36.z;
                            } while(par0WorldServer.isBlockSolid(var12, var13, var14));
                        } while(par0WorldServer.getMaterial(var12, var13, var14) != var34.getMaterial());

                        int var15 = 0;

                        for(int var16 = 0; var16 < 3; ++var16) {
                            int var17 = var12;
                            int var18 = var13;
                            int var19 = var14;
                            byte var20 = 6;
                            SpawnEntry var21 = null;

                            for(int var22 = 0; var22 < 4; ++var22) {
                                var17 += par0WorldServer.random.nextInt(var20) - par0WorldServer.random.nextInt(var20);
                                var18 += par0WorldServer.random.nextInt(1) - par0WorldServer.random.nextInt(1);
                                var19 += par0WorldServer.random.nextInt(var20) - par0WorldServer.random.nextInt(var20);
                                if (canSpawnAt(var34, par0WorldServer, var17, var18, var19)) {
                                    float var23 = (float)var17 + 0.5F;
                                    float var24 = (float)var18;
                                    float var25 = (float)var19 + 0.5F;
                                    if (par0WorldServer.getClosestPlayer((double)var23, (double)var24, (double)var25, 24.0) == null) {
                                        float var26 = var23 - (float)var31.x;
                                        float var27 = var24 - (float)var31.y;
                                        float var28 = var25 - (float)var31.z;
                                        float var29 = var26 * var26 + var27 * var27 + var28 * var28;
                                        if (var29 >= 576.0F) {
                                            if (var21 == null) {
                                                var21 = par0WorldServer.method_2136(var34, var17, var18, var19);
                                                if (var21 == null) {
                                                    break;
                                                }
                                            }

                                            MobEntity var38;
                                            try {
                                                var38 = (MobEntity)var21.type.getConstructor(World.class).newInstance(par0WorldServer);
                                            } catch (Exception var33) {
                                                var33.printStackTrace();
                                                return var3;
                                            }

                                            var38.refreshPositionAndAngles((double)var23, (double)var24, (double)var25, par0WorldServer.random.nextFloat() * 360.0F, 0.0F);
                                            if (var38.canSpawn()) {
                                                ++var15;
                                                par0WorldServer.spawnEntity(var38);
                                                method_3797(var38, par0WorldServer, var23, var24, var25);
                                                if (var15 >= var38.getLimitPerChunk()) {
                                                    continue label117;
                                                }
                                            }

                                            var3 += var15;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return var3;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public static boolean canSpawnAt(EntityCategory par0EnumCreatureType, World par1World, int par2, int par3, int par4) {
        if (par0EnumCreatureType.getMaterial() == Material.WATER) {
            return par1World.getMaterial(par2, par3, par4).isFluid() && !par1World.isBlockSolid(par2, par3 + 1, par4);
        } else if (!par1World.isTopSolid(par2, par3 - 1, par4)) {
            return false;
        } else {
            int var5 = par1World.getBlock(par2, par3 - 1, par4);
            boolean spawnBlock = Block.BLOCKS[var5] != null && ((IBlock)Block.BLOCKS[var5]).canCreatureSpawn(par0EnumCreatureType, par1World, par2, par3 - 1, par4);
            return spawnBlock && var5 != Block.BEDROCK.id && !par1World.isBlockSolid(par2, par3, par4) && !par1World.getMaterial(par2, par3, par4).isFluid() && !par1World.isBlockSolid(par2, par3 + 1, par4);
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    private static void method_3797(MobEntity par0EntityLiving, World par1World, float par2, float par3, float par4) {
        LivingSpecialSpawnEvent event = new LivingSpecialSpawnEvent(par0EntityLiving, par1World, par2, par3, par4);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isHandeled()) {
            if (par0EntityLiving instanceof SpiderEntity && par1World.random.nextInt(100) == 0) {
                SkeletonEntity var7 = new SkeletonEntity(par1World);
                var7.refreshPositionAndAngles((double)par2, (double)par3, (double)par4, par0EntityLiving.yaw, 0.0F);
                par1World.spawnEntity(var7);
                var7.startRiding(par0EntityLiving);
            } else if (par0EntityLiving instanceof SheepEntity) {
                ((SheepEntity)par0EntityLiving).method_2862(SheepEntity.method_2861(par1World.random));
            } else if (par0EntityLiving instanceof OcelotEntity && par1World.random.nextInt(7) == 0) {
                for(int var5 = 0; var5 < 2; ++var5) {
                    OcelotEntity var6 = new OcelotEntity(par1World);
                    var6.refreshPositionAndAngles((double)par2, (double)par3, (double)par4, par0EntityLiving.yaw, 0.0F);
                    var6.setAge(-24000);
                    par1World.spawnEntity(var6);
                }
            }

        }
    }
}
