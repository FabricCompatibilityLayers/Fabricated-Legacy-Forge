package fr.catcore.fabricatedforge.mixin.forgefml.world.chunk;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import fr.catcore.fabricatedforge.mixininterface.IBlockEntity;
import fr.catcore.fabricatedforge.mixininterface.IChunk;
import fr.catcore.fabricatedforge.forged.ReflectionUtils;
import fr.catcore.modremapperapi.api.mixin.NewConstructor;
import fr.catcore.modremapperapi.api.mixin.ShadowConstructor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.predicate.EntityPredicate;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkNibbleArray;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.ChunkEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(Chunk.class)
public abstract class ChunkMixin implements IChunk {

    @Shadow private ChunkSection[] chunkSections;

    @Shadow public abstract int getHighestNonEmptySectionYOffset();

    @Shadow public int[] surfaceCache;

    @Shadow public int[] heightmap;

    @Shadow public boolean modified;

    @Shadow @Final public int chunkX;

    @Shadow @Final public int chunkZ;

    @Shadow public World world;

    @Shadow public abstract void calculateSkyLight();

    @Shadow protected abstract void method_3917(int x, int i, int z);

    @Shadow protected abstract void method_3911(int x, int z);

    @Shadow public abstract boolean isAboveHighestBlock(int i, int j, int k);

    @Shadow public static boolean field_4724;

    @Shadow public boolean containsEntities;

    @Shadow public List<Entity>[] entities;

    @Shadow public Map<Vec3i, BlockEntity> blockEntities;

    @Shadow public boolean loaded;

    @Shadow private byte[] biomeArray;

    private byte[] par2ArrayOfByteCache;

    @Inject(method = "<init>(Lnet/minecraft/world/World;[BII)V", at = @At(value = "CONSTANT", args = "intValue=256"))
    private void fmlCtrTop(World bs, byte[] i, int j, int par4, CallbackInfo ci) {
        this.par2ArrayOfByteCache = new byte[i.length];
        System.arraycopy(i, 0, this.par2ArrayOfByteCache, 0, i.length);
        Arrays.fill(i, (byte) 0);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;[BII)V", at = @At("RETURN"))
    private void fmlCtrBottom(World par1World, byte[] par2ArrayOfByte, int par3, int par4, CallbackInfo ci) {
        par2ArrayOfByte = this.par2ArrayOfByteCache;
        int var5 = par2ArrayOfByte.length / 256;

        for(int var6 = 0; var6 < 16; ++var6) {
            for(int var7 = 0; var7 < 16; ++var7) {
                for(int var8 = 0; var8 < var5; ++var8) {
                    int var9 = par2ArrayOfByte[var6 << 11 | var7 << 7 | var8] & 255;
                    if (var9 != 0) {
                        int var10 = var8 >> 4;
                        if (this.chunkSections[var10] == null) {
                            this.chunkSections[var10] = new ChunkSection(var10 << 4);
                        }

                        this.chunkSections[var10].setBlock(var6, var8 & 15, var7, var9);
                    }
                }
            }
        }
    }

    @ShadowConstructor
    public abstract void vanilla$ctr(World world, int chunkX, int chunkZ);

    @NewConstructor
    public void forge$ctr(World world, byte[] ids, byte[] metadata, int chunkX, int chunkZ) {
        vanilla$ctr(world, chunkX, chunkZ);
        int var5 = ids.length / 256;

        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                for(int y = 0; y < var5; ++y) {
                    int idx = x << 11 | z << 7 | y;
                    int id = ids[idx] & 255;
                    int meta = metadata[idx];
                    if (id != 0) {
                        int var10 = y >> 4;
                        if (this.chunkSections[var10] == null) {
                            this.chunkSections[var10] = new ChunkSection(var10 << 4);
                        }

                        this.chunkSections[var10].setBlock(x, y & 15, z, id);
                        this.chunkSections[var10].setBlockData(x, y & 15, z, meta);
                    }
                }
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public void generateHeightmap() {
        int var1 = this.getHighestNonEmptySectionYOffset();

        for(int var2 = 0; var2 < 16; ++var2) {
            for(int var3 = 0; var3 < 16; ++var3) {
                this.surfaceCache[var2 + (var3 << 4)] = -999;

                for(int var4 = var1 + 16 - 1; var4 > 0; --var4) {
                    int var5 = this.getBlock(var2, var4 - 1, var3);
                    if (this.getBlockOpacity(var2, var4 - 1, var3) != 0) {
                        this.heightmap[var3 << 4 | var2] = var4;
                        break;
                    }
                }
            }
        }

        this.modified = true;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public int getBlockOpacity(int par1, int par2, int par3) {
        int x = (this.chunkX << 4) + par1;
        int z = (this.chunkZ << 4) + par3;
        Block block = Block.BLOCKS[this.getBlock(par1, par2, par3)];
        return block == null ? 0 : ((IBlock)block).getLightOpacity(this.world, x, par2, z);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public int getBlock(int par1, int par2, int par3) {
        if (par2 >> 4 < this.chunkSections.length && par2 >> 4 >= 0) {
            ChunkSection var4 = this.chunkSections[par2 >> 4];
            return var4 != null ? var4.getBlock(par1, par2 & 15, par3) : 0;
        } else {
            return 0;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public int getBlockData(int par1, int par2, int par3) {
        if (par2 >> 4 < this.chunkSections.length && par2 >> 4 >= 0) {
            ChunkSection var4 = this.chunkSections[par2 >> 4];
            return var4 != null ? var4.getBlockData(par1, par2 & 15, par3) : 0;
        } else {
            return 0;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_3881(int par1, int par2, int par3, int par4, int par5) {
        int var6 = par3 << 4 | par1;
        if (par2 >= this.surfaceCache[var6] - 1) {
            this.surfaceCache[var6] = -999;
        }

        int var7 = this.heightmap[var6];
        int var8 = this.getBlock(par1, par2, par3);
        int var9 = this.getBlockData(par1, par2, par3);
        if (var8 == par4 && var9 == par5) {
            return false;
        } else if (par2 >> 4 < this.chunkSections.length && par2 >> 4 >= 0) {
            ChunkSection var10 = this.chunkSections[par2 >> 4];
            boolean var11 = false;
            if (var10 == null) {
                if (par4 == 0) {
                    return false;
                }

                var10 = this.chunkSections[par2 >> 4] = new ChunkSection(par2 >> 4 << 4);
                var11 = par2 >= var7;
            }

            int var12 = this.chunkX * 16 + par1;
            int var13 = this.chunkZ * 16 + par3;
            if (var8 != 0 && !this.world.isClient) {
                Block.BLOCKS[var8].method_464(this.world, var12, par2, var13, var9);
            }

            var10.setBlock(par1, par2 & 15, par3, par4);
            if (var8 != 0) {
                if (!this.world.isClient) {
                    Block.BLOCKS[var8].method_411(this.world, var12, par2, var13, var8, var9);
                } else if (Block.BLOCKS[var8] != null && Block.BLOCKS[var8].hasTileEntity(var9)) {
                    IBlockEntity te = this.world.getBlockEntity(var12, par2, var13);
                    if (te != null && te.shouldRefresh(var8, par4, var9, par5, this.world, var12, par2, var13)) {
                        this.world.method_3725(var12, par2, var13);
                    }
                }
            }

            if (var10.getBlock(par1, par2 & 15, par3) != par4) {
                return false;
            } else {
                var10.setBlockData(par1, par2 & 15, par3, par5);
                if (var11) {
                    this.calculateSkyLight();
                } else {
                    if (this.getBlockOpacity(par1, par2, par3) > 0) {
                        if (par2 >= var7) {
                            this.method_3917(par1, par2 + 1, par3);
                        }
                    } else if (par2 == var7 - 1) {
                        this.method_3917(par1, par2, par3);
                    }

                    this.method_3911(par1, par3);
                }

                if (par4 != 0) {
                    if (!this.world.isClient) {
                        Block.BLOCKS[par4].breakNaturally(this.world, var12, par2, var13);
                    }

                    if (Block.BLOCKS[par4] != null && Block.BLOCKS[par4].hasTileEntity(par5)) {
                        BlockEntity var14 = this.getBlockEntity(par1, par2, par3);
                        if (var14 == null) {
                            var14 = Block.BLOCKS[par4].createTileEntity(this.world, par5);
                            this.world.method_3603(var12, par2, var13, var14);
                        }

                        if (var14 != null) {
                            var14.resetBlock();
                            var14.dataValue = par5;
                        }
                    }
                }

                this.modified = true;
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_3900(int par1, int par2, int par3, int par4) {
        ChunkSection var5 = par2 >> 4 < this.chunkSections.length && par2 >> 4 >= 0 ? this.chunkSections[par2 >> 4] : null;
        if (var5 == null) {
            return false;
        } else {
            int var6 = var5.getBlockData(par1, par2 & 15, par3);
            if (var6 == par4) {
                return false;
            } else {
                this.modified = true;
                var5.setBlockData(par1, par2 & 15, par3, par4);
                int var7 = var5.getBlock(par1, par2 & 15, par3);
                if (var7 > 0 && Block.BLOCKS[var7] != null && ((IBlock)Block.BLOCKS[var7]).hasTileEntity(par4)) {
                    BlockEntity var8 = this.getBlockEntity(par1, par2, par3);
                    if (var8 != null) {
                        var8.resetBlock();
                        var8.dataValue = par4;
                    }
                }

                return true;
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public int method_3890(LightType par1EnumSkyBlock, int par2, int par3, int par4) {
        ChunkSection var5 = par3 >> 4 < this.chunkSections.length && par3 >> 4 >= 0 ? this.chunkSections[par3 >> 4] : null;
        return var5 == null
                ? (this.isAboveHighestBlock(par2, par3, par4) ? par1EnumSkyBlock.defaultValue : 0)
                : (
                par1EnumSkyBlock == LightType.SKY
                        ? var5.getSkyLight(par2, par3 & 15, par4)
                        : (par1EnumSkyBlock == LightType.BLOCK ? var5.getBlockLight(par2, par3 & 15, par4) : par1EnumSkyBlock.defaultValue)
        );
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_3891(LightType par1EnumSkyBlock, int par2, int par3, int par4, int par5) {
        if (par3 >> 4 < this.chunkSections.length && par3 >> 4 >= 0) {
            ChunkSection var6 = this.chunkSections[par3 >> 4];
            if (var6 == null) {
                var6 = this.chunkSections[par3 >> 4] = new ChunkSection(par3 >> 4 << 4);
                this.calculateSkyLight();
            }

            this.modified = true;
            if (par1EnumSkyBlock == LightType.SKY) {
                if (!this.world.dimension.isNether) {
                    var6.setSkyLight(par2, par3 & 15, par4, par5);
                }
            } else if (par1EnumSkyBlock == LightType.BLOCK) {
                var6.setBlockLight(par2, par3 & 15, par4, par5);
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public int method_3905(int par1, int par2, int par3, int par4) {
        ChunkSection var5 = par2 >> 4 < this.chunkSections.length && par2 >> 4 >= 0 ? this.chunkSections[par2 >> 4] : null;
        if (var5 != null) {
            int var6 = this.world.dimension.isNether ? 0 : var5.getSkyLight(par1, par2 & 15, par3);
            if (var6 > 0) {
                field_4724 = true;
            }

            var6 -= par4;
            int var7 = var5.getBlockLight(par1, par2 & 15, par3);
            if (var7 > var6) {
                var6 = var7;
            }

            return var6;
        } else {
            return !this.world.dimension.isNether && par4 < LightType.SKY.defaultValue ? LightType.SKY.defaultValue - par4 : 0;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void addEntity(Entity par1Entity) {
        this.containsEntities = true;
        int var2 = MathHelper.floor(par1Entity.x / 16.0);
        int var3 = MathHelper.floor(par1Entity.z / 16.0);
        if (var2 != this.chunkX || var3 != this.chunkZ) {
            System.out.println("Wrong location! " + par1Entity);
            Thread.dumpStack();
        }

        int var4 = MathHelper.floor(par1Entity.y / 16.0);
        if (var4 < 0) {
            var4 = 0;
        }

        if (var4 >= this.entities.length) {
            var4 = this.entities.length - 1;
        }

        MinecraftForge.EVENT_BUS.post(new EntityEvent.EnteringChunk(par1Entity, this.chunkX, this.chunkZ, par1Entity.chunkX, par1Entity.chunkZ));
        par1Entity.updateNeeded = true;
        par1Entity.chunkX = this.chunkX;
        par1Entity.chunkY = var4;
        par1Entity.chunkZ = this.chunkZ;
        this.entities[var4].add(par1Entity);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public BlockEntity getBlockEntity(int par1, int par2, int par3) {
        Vec3i var4 = new Vec3i(par1, par2, par3);
        BlockEntity var5 = (BlockEntity)this.blockEntities.get(var4);
        if (var5 != null && var5.isRemoved()) {
            this.blockEntities.remove(var4);
            var5 = null;
        }

        if (var5 == null) {
            int var6 = this.getBlock(par1, par2, par3);
            int meta = this.getBlockData(par1, par2, par3);
            if (var6 <= 0 || !((IBlock)Block.BLOCKS[var6]).hasTileEntity(meta)) {
                return null;
            }

            if (var5 == null) {
                var5 = ((IBlock)Block.BLOCKS[var6]).createTileEntity(this.world, meta);
                this.world.method_3603(this.chunkX * 16 + par1, par2, this.chunkZ * 16 + par3, var5);
            }

            var5 = (BlockEntity)this.blockEntities.get(var4);
        }

        return var5;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void addBlockEntity(BlockEntity par1TileEntity) {
        int var2 = par1TileEntity.x - this.chunkX * 16;
        int var3 = par1TileEntity.y;
        int var4 = par1TileEntity.z - this.chunkZ * 16;
        this.addBlockEntity(var2, var3, var4, par1TileEntity);
        if (this.loaded) {
            this.world.addTileEntity(par1TileEntity);
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void addBlockEntity(int par1, int par2, int par3, BlockEntity par4TileEntity) {
        Vec3i var5 = new Vec3i(par1, par2, par3);
        par4TileEntity.setWorld(this.world);
        par4TileEntity.x = this.chunkX * 16 + par1;
        par4TileEntity.y = par2;
        par4TileEntity.z = this.chunkZ * 16 + par3;
        Block block = Block.BLOCKS[this.getBlock(par1, par2, par3)];
        if (block != null && ((IBlock)block).hasTileEntity(this.getBlockData(par1, par2, par3))) {
            BlockEntity old = (BlockEntity)this.blockEntities.get(var5);
            if (old != null) {
                old.markRemoved();
            }

            par4TileEntity.cancelRemoval();
            this.blockEntities.put(var5, par4TileEntity);
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void loadToWorld() {
        this.loaded = true;
        this.world.addBlockEntities(this.blockEntities.values());

        for(int var1 = 0; var1 < this.entities.length; ++var1) {
            this.world.loadEntities(this.entities[var1]);
        }

        MinecraftForge.EVENT_BUS.post(new ChunkEvent.Load((Chunk)(Object) this));
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void unloadFromWorld() {
        this.loaded = false;

        for(BlockEntity var2 : this.blockEntities.values()) {
            this.world.queueBlockEntity(var2);
        }

        for(int var3 = 0; var3 < this.entities.length; ++var3) {
            this.world.unloadEntities(this.entities[var3]);
        }

        MinecraftForge.EVENT_BUS.post(new ChunkEvent.Unload((Chunk)(Object) this));
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_3889(Entity par1Entity, Box par2AxisAlignedBB, List par3List) {
        int var4 = MathHelper.floor((par2AxisAlignedBB.minY - ReflectionUtils.World_MAX_ENTITY_RADIUS) / 16.0);
        int var5 = MathHelper.floor((par2AxisAlignedBB.maxY + ReflectionUtils.World_MAX_ENTITY_RADIUS) / 16.0);
        if (var4 < 0) {
            var4 = 0;
        }

        if (var5 >= this.entities.length) {
            var5 = this.entities.length - 1;
        }

        for(int var6 = var4; var6 <= var5; ++var6) {
            List var7 = this.entities[var6];

            for(int var8 = 0; var8 < var7.size(); ++var8) {
                Entity var9 = (Entity)var7.get(var8);
                if (var9 != par1Entity && var9.boundingBox.intersects(par2AxisAlignedBB)) {
                    par3List.add(var9);
                    Entity[] var10 = var9.getParts();
                    if (var10 != null) {
                        for(int var11 = 0; var11 < var10.length; ++var11) {
                            var9 = var10[var11];
                            if (var9 != par1Entity && var9.boundingBox.intersects(par2AxisAlignedBB)) {
                                par3List.add(var9);
                            }
                        }
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
    public void getEntitiesInBox(Class par1Class, Box par2AxisAlignedBB, List par3List, EntityPredicate par4IEntitySelector) {
        int var5 = MathHelper.floor((par2AxisAlignedBB.minY - ReflectionUtils.World_MAX_ENTITY_RADIUS) / 16.0);
        int var6 = MathHelper.floor((par2AxisAlignedBB.maxY + ReflectionUtils.World_MAX_ENTITY_RADIUS) / 16.0);
        if (var5 < 0) {
            var5 = 0;
        } else if (var5 >= this.entities.length) {
            var5 = this.entities.length - 1;
        }

        if (var6 >= this.entities.length) {
            var6 = this.entities.length - 1;
        } else if (var6 < 0) {
            var6 = 0;
        }

        for(int var7 = var5; var7 <= var6; ++var7) {
            List var8 = this.entities[var7];

            for(int var9 = 0; var9 < var8.size(); ++var9) {
                Entity var10 = (Entity)var8.get(var9);
                if (par1Class.isAssignableFrom(var10.getClass())
                        && var10.boundingBox.intersects(par2AxisAlignedBB)
                        && (par4IEntitySelector == null || par4IEntitySelector.test(var10))) {
                    par3List.add(var10);
                }
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public void method_3895(byte[] par1ArrayOfByte, int par2, int par3, boolean par4) {
        for(BlockEntity tileEntity : this.blockEntities.values()) {
            tileEntity.resetBlock();
            tileEntity.getDataValue();
            tileEntity.getBlock();
        }

        int var5 = 0;

        for(int var6 = 0; var6 < this.chunkSections.length; ++var6) {
            if ((par2 & 1 << var6) != 0) {
                if (this.chunkSections[var6] == null) {
                    this.chunkSections[var6] = new ChunkSection(var6 << 4);
                }

                byte[] var7 = this.chunkSections[var6].getBlocks();
                System.arraycopy(par1ArrayOfByte, var5, var7, 0, var7.length);
                var5 += var7.length;
            } else if (par4 && this.chunkSections[var6] != null) {
                this.chunkSections[var6] = null;
            }
        }

        for(int var17 = 0; var17 < this.chunkSections.length; ++var17) {
            if ((par2 & 1 << var17) != 0 && this.chunkSections[var17] != null) {
                ChunkNibbleArray var8 = this.chunkSections[var17].getBlockData();
                System.arraycopy(par1ArrayOfByte, var5, var8.bytes, 0, var8.bytes.length);
                var5 += var8.bytes.length;
            }
        }

        for(int var18 = 0; var18 < this.chunkSections.length; ++var18) {
            if ((par2 & 1 << var18) != 0 && this.chunkSections[var18] != null) {
                ChunkNibbleArray var8 = this.chunkSections[var18].getBlockLight();
                System.arraycopy(par1ArrayOfByte, var5, var8.bytes, 0, var8.bytes.length);
                var5 += var8.bytes.length;
            }
        }

        for(int var19 = 0; var19 < this.chunkSections.length; ++var19) {
            if ((par2 & 1 << var19) != 0 && this.chunkSections[var19] != null) {
                ChunkNibbleArray var8 = this.chunkSections[var19].getSkyLight();
                System.arraycopy(par1ArrayOfByte, var5, var8.bytes, 0, var8.bytes.length);
                var5 += var8.bytes.length;
            }
        }

        for(int var20 = 0; var20 < this.chunkSections.length; ++var20) {
            if ((par3 & 1 << var20) != 0) {
                if (this.chunkSections[var20] == null) {
                    var5 += 2048;
                } else {
                    ChunkNibbleArray var8 = this.chunkSections[var20].method_3944();
                    if (var8 == null) {
                        var8 = this.chunkSections[var20].method_3948();
                    }

                    System.arraycopy(par1ArrayOfByte, var5, var8.bytes, 0, var8.bytes.length);
                    var5 += var8.bytes.length;
                }
            } else if (par4 && this.chunkSections[var20] != null && this.chunkSections[var20].method_3944() != null) {
                this.chunkSections[var20].method_3943();
            }
        }

        if (par4) {
            System.arraycopy(par1ArrayOfByte, var5, this.biomeArray, 0, this.biomeArray.length);
            int var10000 = var5 + this.biomeArray.length;
        }

        for(int var21 = 0; var21 < this.chunkSections.length; ++var21) {
            if (this.chunkSections[var21] != null && (par2 & 1 << var21) != 0) {
                this.chunkSections[var21].calculateCounts();
            }
        }

        this.generateHeightmap();
        List<BlockEntity> invalidList = new ArrayList();

        for(BlockEntity tileEntity : this.blockEntities.values()) {
            int x = tileEntity.x & 15;
            int y = tileEntity.y;
            int z = tileEntity.z & 15;
            Block block = tileEntity.getBlock();
            if (block == null || block.id != this.getBlock(x, y, z) || tileEntity.getDataValue() != this.getBlockData(x, y, z)) {
                invalidList.add(tileEntity);
            }

            tileEntity.resetBlock();
        }

        for(BlockEntity tileEntity : invalidList) {
            tileEntity.markRemoved();
        }
    }

    @Override
    public void cleanChunkBlockTileEntity(int x, int y, int z) {
        Vec3i position = new Vec3i(x, y, z);
        if (this.loaded) {
            BlockEntity entity = (BlockEntity)this.blockEntities.get(position);
            if (entity != null && entity.isRemoved()) {
                this.blockEntities.remove(position);
            }
        }
    }

    @Override
    public ChunkSection getChunkSection(int index) {
        return this.chunkSections[index];
    }

    @Override
    public void setChunkSection(int index, ChunkSection section) {
        this.chunkSections[index] = section;
    }
}
