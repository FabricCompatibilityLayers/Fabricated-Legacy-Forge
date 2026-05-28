package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import fr.catcore.cursedmixinextensions.annotations.NewConstructor;
import fr.catcore.cursedmixinextensions.annotations.ReplaceConstructor;
import fr.catcore.cursedmixinextensions.annotations.ShadowConstructor;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ChunkExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.ChunkEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(Chunk.class)
public abstract class ChunkMixin implements ChunkExtension {
    @Shadow private ExtendedBlockStorage[] storageArrays;

    @Shadow @Final public int xPosition;

    @Shadow @Final public int zPosition;

    @Shadow public abstract int getBlockID(int par1, int par2, int par3);

    @Shadow public World worldObj;

    @Shadow public abstract int getBlockMetadata(int par1, int par2, int par3);

    @Shadow public Map chunkTileEntityMap;

    @Shadow public boolean isChunkLoaded;

    @ShadowConstructor
    abstract void constructor(World par1World, int par2, int par3);

    @ReplaceConstructor
    public void constructor(World par1World, byte[] par2ArrayOfByte, int par3, int par4) {
        constructor(par1World, par3, par4);
        int var5 = par2ArrayOfByte.length / 256;

        for (int var6 = 0; var6 < 16; var6++) {
            for (int var7 = 0; var7 < 16; var7++) {
                for (int var8 = 0; var8 < var5; var8++) {
                    /* FORGE: The following change, a cast from unsigned byte to int,
                     * fixes a vanilla bug when generating new chunks that contain a block ID > 127 */
                    int var9 = par2ArrayOfByte[var6 << 11 | var7 << 7 | var8] & 0xFF;
                    if (var9 != 0) {
                        int var10 = var8 >> 4;
                        if (this.storageArrays[var10] == null) {
                            this.storageArrays[var10] = new ExtendedBlockStorage(var10 << 4);
                        }

                        this.storageArrays[var10].setExtBlockID(var6, var8 & 15, var7, var9);
                    }
                }
            }
        }
    }

    /**
     * Metadata sensitive Chunk constructor for use in new ChunkProviders that
     * use metadata sensitive blocks during generation.
     *
     * @param world The world this chunk belongs to
     * @param ids A ByteArray containing all the BlockID's to set this chunk to
     * @param metadata A ByteArray containing all the metadata to set this chunk to
     * @param chunkX The chunk's X position
     * @param chunkZ The Chunk's Z position
     */
    @NewConstructor
    public void constructor(World world, byte[] ids, byte[] metadata, int chunkX, int chunkZ)
    {
        constructor(world, chunkX, chunkZ);
        int var5 = ids.length / 256;

        for (int x = 0; x < 16; ++x)
        {
            for (int z = 0; z < 16; ++z)
            {
                for (int y = 0; y < var5; ++y)
                {
                    int idx = x << 11 | z << 7 | y;
                    int id = ids[idx] & 0xFF;
                    int meta = metadata[idx];

                    if (id != 0)
                    {
                        int var10 = y >> 4;

                        if (this.storageArrays[var10] == null)
                        {
                            this.storageArrays[var10] = new ExtendedBlockStorage(var10 << 4);
                        }

                        this.storageArrays[var10].setExtBlockID(x, y & 15, z, id);
                        this.storageArrays[var10].setExtBlockMetadata(x, y & 15, z, meta);
                    }
                }
            }
        }
    }

    @WrapOperation(method = "generateHeightMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Chunk;getBlockID(III)I"))
    private int forge$capturePos(Chunk instance, int par2, int par3, int i, Operation<Integer> original,
                                 @Share(namespace = "fabricated-forge", value = "pos") LocalRef<ChunkCoordinates> posRef) {
        posRef.set(new ChunkCoordinates(par2, par3, i));
        return original.call(instance, par2, par3, i);
    }

    @Definition(id = "lightOpacity", field = "Lnet/minecraft/src/Block;lightOpacity:[I")
    @Expression("lightOpacity[?]")
    @WrapOperation(method = "generateHeightMap", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int forge$getBlockLightOpacity$generateHeightMap(int[] array, int index, Operation<Integer> original,
                                                             @Share(namespace = "fabricated-forge", value = "pos") LocalRef<ChunkCoordinates> posRef) {
        ChunkCoordinates pos = posRef.get();
        return getBlockLightOpacity(pos.posX, pos.posY, pos.posZ);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getBlockLightOpacity(int par1, int par2, int par3)
    {
        int x = (xPosition << 4) + par1;
        int z = (zPosition << 4) + par3;
        Block block = Block.blocksList[getBlockID(par1, par2, par3)];
        return (block == null ? 0 : ((BlockExtension) block).getLightOpacity(worldObj, x, par2, z));
    }

    @Definition(id = "par2", local = @Local(type = int.class, ordinal = 1, argsOnly = true))
    @Definition(id = "storageArrays", field = "Lnet/minecraft/src/Chunk;storageArrays:[Lnet/minecraft/src/ExtendedBlockStorage;")
    @Expression("par2 >> 4 >= this.storageArrays.length")
    @WrapOperation(method = {"getBlockID", "getBlockMetadata"}, at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$extraCheck(int left, int right, Operation<Boolean> original,
                                     @Local(ordinal = 1, argsOnly = true) int par2) {
        return original.call(left, right) || par2 >> 4 < 0;
    }

    @Definition(id = "storageArrays", field = "Lnet/minecraft/src/Chunk;storageArrays:[Lnet/minecraft/src/ExtendedBlockStorage;")
    @Expression("?.storageArrays[?]")
    @Inject(method = "setBlockIDWithMetadata", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0), cancellable = true)
    private void forge$extraCheck(int par1, int par2, int par3, int par4, int par5, CallbackInfoReturnable<Boolean> cir) {
        if (par2 >> 4 >= storageArrays.length || par2 >> 4 < 0)
        {
            cir.setReturnValue(false);
        }
    }

    @Definition(id = "blocksList", field = "Lnet/minecraft/src/Block;blocksList:[Lnet/minecraft/src/Block;")
    @Definition(id = "BlockContainer", type = BlockContainer.class)
    @Expression("blocksList[?] instanceof BlockContainer")
    @WrapOperation(method = "setBlockIDWithMetadata", at = {@At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0)})
    private boolean forge$NonNullBlock1(Object object, Operation<Boolean> original) {
        return object != null;
    }

    @Definition(id = "par4", local = @Local(type = int.class, ordinal = 3, argsOnly = true))
    @Definition(id = "var8", local = @Local(type = int.class, ordinal = 7))
    @Expression("var8 != par4")
    @WrapOperation(method = "setBlockIDWithMetadata", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
    private boolean forge$hasTileEntity(int var8, int par4, Operation<Boolean> original,
                                        @Local(ordinal = 8) int var9) {
        return ((BlockExtension) Block.blocksList[var8]).hasTileEntity(var9);
    }

    @Definition(id = "lightOpacity", field = "Lnet/minecraft/src/Block;lightOpacity:[I")
    @Definition(id = "par4", local = @Local(type = int.class, ordinal = 3, argsOnly = true))
    @Expression("lightOpacity[par4 & 4095]")
    @WrapOperation(method = "setBlockIDWithMetadata", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int forge$getBlockLightOpacity$setBlockIDWithMetadata(int[] array, int index, Operation<Integer> original,
                                                                  @Local(argsOnly = true, ordinal = 0) int par1,
                                                                  @Local(argsOnly = true, ordinal = 1) int par2,
                                                                  @Local(argsOnly = true, ordinal = 2) int par3) {
        return getBlockLightOpacity(par1, par2, par3);
    }

    @Definition(id = "blocksList", field = "Lnet/minecraft/src/Block;blocksList:[Lnet/minecraft/src/Block;")
    @Definition(id = "BlockContainer", type = BlockContainer.class)
    @Expression("blocksList[?] instanceof BlockContainer")
    @WrapOperation(method = "setBlockIDWithMetadata", at = {@At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1)})
    private boolean forge$NonNullBlock2(Object object, Operation<Boolean> original,
                                        @Local(ordinal = 4, argsOnly = true) int par5) {
        return object != null && ((BlockExtension) object).hasTileEntity(par5);
    }

    @Definition(id = "BlockContainer", type = BlockContainer.class)
    @Expression("(BlockContainer) ?")
    @WrapOperation(method = "setBlockIDWithMetadata", at = @At("MIXINEXTRAS:EXPRESSION"))
    private BlockContainer forge$fixCast(Object object, Operation<BlockContainer> original) {
        return null;
    }

    @Redirect(method = "setBlockIDWithMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockContainer;createNewTileEntity(Lnet/minecraft/src/World;)Lnet/minecraft/src/TileEntity;"))
    private TileEntity forge$createTileEntity(BlockContainer instance, World world,
                                              @Local(argsOnly = true, ordinal = 3) int par4,
                                              @Local(argsOnly = true, ordinal = 4) int par5) {
        return ((BlockExtension) Block.blocksList[par4]).createTileEntity(world, par5);
    }

    @WrapOperation(method = "setBlockIDWithMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/TileEntity;updateContainingBlockInfo()V", ordinal = 0))
    private void forge$setBlockMetadata(TileEntity instance, Operation<Void> original,
                                        @Local(argsOnly = true, ordinal = 4) int par5) {
        original.call(instance);
        instance.blockMetadata = par5;
    }

    @Definition(id = "var8", local = @Local(type = int.class, ordinal = 7))
    @Expression("var8 > 0")
    @WrapOperation(method = "setBlockIDWithMetadata", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$cancelBranch(int left, int right, Operation<Boolean> original) {
        return false;
    }

    @Definition(id = "storageArrays", field = "Lnet/minecraft/src/Chunk;storageArrays:[Lnet/minecraft/src/ExtendedBlockStorage;")
    @Definition(id = "par2", local = @Local(type = int.class, ordinal = 1, argsOnly = true))
    @Expression("this.storageArrays[par2 >> 4]")
    @WrapOperation(method = {"setBlockMetadata", "getSavedLightValue", "getBlockLightValue"}, at = @At("MIXINEXTRAS:EXPRESSION"))
    private ExtendedBlockStorage forge$includeCheck(ExtendedBlockStorage[] array, int index, Operation<ExtendedBlockStorage> original) {
        return (index >= array.length || index < 0 ? null : original.call(array, index));
    }

    @Definition(id = "blocksList", field = "Lnet/minecraft/src/Block;blocksList:[Lnet/minecraft/src/Block;")
    @Definition(id = "BlockContainer", type = BlockContainer.class)
    @Expression("blocksList[?] instanceof BlockContainer")
    @WrapOperation(method = "setBlockMetadata", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$hasTileEntity(Object object, Operation<Boolean> original,
                                        @Local(ordinal = 3, argsOnly = true) int par4) {
        return object != null && ((BlockExtension) object).hasTileEntity(par4);
    }

    @Inject(method = "setLightValue", at = @At("HEAD"), cancellable = true)
    private void forge$check(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4, int par5, CallbackInfo ci) {
        if (par3 >> 4 >= storageArrays.length || par3 >> 4 < 0)
        {
            ci.cancel();
        }
    }

    @Definition(id = "addedToChunk", field = "Lnet/minecraft/src/Entity;addedToChunk:Z")
    @Expression("?.addedToChunk = ?")
    @WrapOperation(method = "addEntity", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private void forge$postEntityEventEnteringChunk(Entity instance, boolean value, Operation<Void> original,
                                                    @Local(argsOnly = true, ordinal = 0) Entity par1Entity) {
        MinecraftForge.EVENT_BUS.post(new EntityEvent.EnteringChunk(par1Entity, this.xPosition, this.zPosition, par1Entity.chunkCoordX, par1Entity.chunkCoordZ));

        original.call(instance, value);
    }

    @WrapOperation(method = "getChunkBlockTileEntity", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0))
    private Object forge$removeInvalidTileEntity(Map chunkTileEntityMap, Object var4, Operation<Object> original) {
        TileEntity var5 = (TileEntity) original.call(chunkTileEntityMap, var4);

        if (var5 != null && var5.isInvalid())
        {
            chunkTileEntityMap.remove(var4);
            var5 = null;
        }

        return var5;
    }

    @Redirect(method = "getChunkBlockTileEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;hasTileEntity()Z"))
    private boolean forge$hasTileEntity(Block instance,
                                        @Local(argsOnly = true, ordinal = 0) int par1,
                                        @Local(argsOnly = true, ordinal = 1) int par2,
                                        @Local(argsOnly = true, ordinal = 2) int par3,
                                        @Share(value = "meta", namespace = "fabricated-forge") LocalIntRef metaRef) {
        int meta = this.getBlockMetadata(par1, par2, par3);
        metaRef.set(meta);
        return ((BlockExtension) instance).hasTileEntity(meta);
    }

    @Redirect(method = "getChunkBlockTileEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockContainer;createNewTileEntity(Lnet/minecraft/src/World;)Lnet/minecraft/src/TileEntity;"))
    private TileEntity forge$createTileEntity(BlockContainer instance, World world,
                                              @Share(value = "meta", namespace = "fabricated-forge") LocalIntRef metaRef) {
        int meta = metaRef.get();
        return ((BlockExtension) instance).createTileEntity(world, meta);
    }

    @Definition(id = "var5", local = @Local(type = TileEntity.class))
    @Expression("var5 != null")
    @WrapOperation(method = "getChunkBlockTileEntity", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$hackCheck(Object left, Object right, Operation<Boolean> original) {
        return false;
    }

    @Redirect(method = "addTileEntity", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", remap = false))
    private boolean forge$addTileEntity(List instance, Object e) {
        ((WorldExtension) this.worldObj).addTileEntity((TileEntity) e);
        return true;
    }

    @Definition(id = "getBlockID", method = "Lnet/minecraft/src/Chunk;getBlockID(III)I")
    @Expression("this.getBlockID(?, ?, ?) != 0")
    @WrapOperation(method = "setChunkBlockTileEntity", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$NonNullBlock(int blockId, int right, Operation<Boolean> original) {
        return Block.blocksList[blockId] != null;
    }

    @Definition(id = "blocksList", field = "Lnet/minecraft/src/Block;blocksList:[Lnet/minecraft/src/Block;")
    @Definition(id = "BlockContainer", type = BlockContainer.class)
    @Expression("blocksList[?] instanceof BlockContainer")
    @WrapOperation(method = "setChunkBlockTileEntity", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isTileEntity(Object object, Operation<Boolean> original,
                                       @Local(ordinal = 0, argsOnly = true) int par1,
                                       @Local(ordinal = 1, argsOnly = true) int par2,
                                       @Local(ordinal = 2, argsOnly = true) int par3) {
        Block block = (Block) object;

        if (block == null) return false;

        return ((BlockExtension) block).hasTileEntity(this.getBlockMetadata(par1, par2, par3));
    }

    @Inject(method = "setChunkBlockTileEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/TileEntity;validate()V"))
    private void forge$invalidateTileEntity(int par1, int par2, int par3, TileEntity par4TileEntity, CallbackInfo ci,
                                            @Local ChunkPosition var5) {
        TileEntity old = (TileEntity)chunkTileEntityMap.get(var5);
        if (old != null)
        {
            old.invalidate();
        }
    }

    @Inject(method = "onChunkLoad", at = @At("RETURN"))
    private void forge$postChunkEventLoad(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new ChunkEvent.Load((Chunk) (Object) this));
    }

    @Inject(method = "onChunkUnload", at = @At("RETURN"))
    private void forge$postChunkEventUnload(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new ChunkEvent.Unload((Chunk) (Object) this));
    }

    @ModifyConstant(method = {"getEntitiesWithinAABBForEntity", "getEntitiesOfTypeWithinAAAB"}, constant = @Constant(doubleValue = 2.0))
    private double forge$getMaxEntityRadius(double constant) {
        return WorldAccessor.getMaxEntityRadius();
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "fillChunk", at = @At("HEAD"))
    private void forge$updateTileEntity(byte[] par1ArrayOfByte, int par2, int par3, boolean par4, CallbackInfo ci) {
        Iterator iterator = chunkTileEntityMap.values().iterator();
        while(iterator.hasNext())
        {
            TileEntity tileEntity = (TileEntity)iterator.next();
            tileEntity.updateContainingBlockInfo();
            tileEntity.getBlockMetadata();
            tileEntity.getBlockType();
        }
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "fillChunk", at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;"))
    private void forge$createLocal(byte[] par2, int par3, int par4, boolean par5, CallbackInfo ci,
                                   @Share(value = "invalidList", namespace = "fabricated-forge") LocalRef<List<TileEntity>> invalidListRef) {
        invalidListRef.set(new ArrayList<>());
    }

    @Environment(EnvType.CLIENT)
    @WrapOperation(method = "fillChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/TileEntity;updateContainingBlockInfo()V"))
    private void forge$collectInvalidTileEntities(TileEntity tileEntity, Operation<Void> original,
                                                  @Share(value = "invalidList", namespace = "fabricated-forge") LocalRef<List<TileEntity>> invalidListRef) {
        int x = tileEntity.xCoord & 15;
        int y = tileEntity.yCoord;
        int z = tileEntity.zCoord & 15;
        Block block = tileEntity.getBlockType();
        if (block == null || block.blockID != getBlockID(x, y, z) || tileEntity.getBlockMetadata() != getBlockMetadata(x, y, z))
        {
            invalidListRef.get().add(tileEntity);
        }

        original.call(tileEntity);
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "fillChunk", at = @At("RETURN"))
    private void forge$invalidateInvalidTileEntities(byte[] par2, int par3, int par4, boolean par5, CallbackInfo ci,
                                                     @Share(value = "invalidList", namespace = "fabricated-forge") LocalRef<List<TileEntity>> invalidListRef) {
        for (TileEntity tileEntity : invalidListRef.get())
        {
            tileEntity.invalidate();
        }
    }

    /** FORGE: Used to remove only invalid TileEntities */
    @Override
    public void cleanChunkBlockTileEntity(int x, int y, int z)
    {
        ChunkPosition position = new ChunkPosition(x, y, z);
        if (isChunkLoaded)
        {
            TileEntity entity = (TileEntity)chunkTileEntityMap.get(position);
            if (entity != null && entity.isInvalid())
            {
                chunkTileEntityMap.remove(position);
            }
        }
    }
}
