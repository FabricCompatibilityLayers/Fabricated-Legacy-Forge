/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.google.common.collect.SetMultimap;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import fr.catcore.cursedmixinextensions.annotations.Public;
import fr.catcore.cursedmixinextensions.annotations.ReplaceConstructor;
import fr.catcore.cursedmixinextensions.annotations.ShadowSuperConstructor;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(World.class)
public abstract class WorldMixin implements WorldExtension {
    // == Shadowed members section: start ==

    @Shadow @Final
    @Mutable
    public WorldProvider provider;
    @Shadow @Final @Mutable protected ISaveHandler saveHandler;
    @Shadow @Final @Mutable public Profiler theProfiler;
    @Shadow protected WorldInfo worldInfo;
    @Shadow protected IChunkProvider chunkProvider;

    @Shadow public List loadedEntityList;
    @Shadow protected List<Entity> unloadedEntityList;
    @Shadow public List loadedTileEntityList;
    @Shadow private List<TileEntity> addedTileEntityList;
    @Shadow public List weatherEffects;
    @Shadow
    private boolean scanningTileEntities;
    @Shadow private List entityRemoval;

    @Shadow public abstract int getBlockId(int par1, int par2, int par3);
    @Shadow public abstract int getBlockLightOpacity(int par1, int par2, int par3);
    @Shadow public abstract boolean blockExists(int par1, int par2, int par3);
    @Shadow public abstract Chunk getChunkFromChunkCoords(int par1, int par2);
    @Shadow public abstract int getBlockMetadata(int par1, int par2, int par3);
    @Shadow protected abstract IChunkProvider createChunkProvider();
    @Shadow public abstract void calculateInitialSkylight();
    @Shadow protected int lastLightningBolt;
    @Shadow public Random rand;
    @Shadow protected Set activeChunkSet;
    @Shadow public int lightningFlash;
    @Shadow private long cloudColour;
    @Shadow public void updateEntity(Entity par1Entity) {}
    @Shadow protected void releaseEntitySkin(Entity par1Entity) {}
    @Shadow protected void obtainEntitySkin(Entity par1Entity) {}
    @Shadow protected boolean chunkExists(int par1, int par2) { return false; }

    @Shadow
    private int ambientTickCountdown;
    @Shadow
    int[] lightUpdateBlockList;
    @Shadow
    private List entitiesWithinAABBExcludingEntity;
    @Shadow
    public boolean isRemote;
    @Shadow
    public boolean scheduledUpdatesAreImmediate;
    @Shadow
    public List playerEntities;
    @Shadow
    public int skylightSubtracted;
    @Shadow
    protected int updateLCG;
    @Mutable
    @Shadow
    @Final
    protected int DIST_HASH_MAGIC;
    @Shadow
    public boolean editingBlocks;
    @Shadow
    protected List worldAccesses;
    @Mutable
    @Shadow
    @Final
    public VillageCollection villageCollectionObj;
    @Mutable
    @Shadow
    @Final
    protected VillageSiege villageSiegeObj;
    @Shadow
    private ArrayList collidingBoundingBoxes;
    @Shadow
    protected boolean spawnHostileMobs;
    @Shadow
    protected boolean spawnPeacefulMobs;

    // == Shadowed members section: end ==

    // == New fields section: start ==

    @Shadow
    public abstract BiomeGenBase getBiomeGenForCoords(int par1, int par2);

    @Shadow
    public abstract Vec3 getSkyColor(Entity par1Entity, float par2);

    @Shadow
    public abstract Vec3 drawClouds(float par1);

    @Shadow
    public abstract float getStarBrightness(float par1);

    @Shadow
    protected abstract void calculateInitialWeather();

    @Shadow
    protected abstract void updateWeather();

    @Shadow
    public abstract boolean canBlockFreeze(int par1, int par2, int par3, boolean par4);

    @Shadow
    public abstract boolean canSnowAt(int par1, int par2, int par3);

    @Shadow
    public abstract boolean canMineBlock(EntityPlayer par1EntityPlayer, int par2, int par3, int par4);

    @Public
    private static double MAX_ENTITY_RADIUS = 2.0D;

    private static MapStorage s_mapStorage;
    private static ISaveHandler s_savehandler;

    // == New fields section: end ==

    // == Injections section: start ==

    private boolean getBiomeGenForCoordsBody = false;

    @WrapMethod(method = "getBiomeGenForCoords")
    private BiomeGenBase forge$getBiomeGenForCoords(int par1, int par2, Operation<BiomeGenBase> original) {
        if (getBiomeGenForCoordsBody) {
            getBiomeGenForCoordsBody = false;
            return original.call(par1, par2);
        } else {
            return ((WorldProviderExtension) provider).getBiomeGenForCoords(par1, par2);
        }
    }

    @Override
    public BiomeGenBase getBiomeGenForCoordsBody(int par1, int par2) {
        getBiomeGenForCoordsBody = true;
        return getBiomeGenForCoords(par1, par2);
    }

    @ShadowSuperConstructor
    private void superConstructor() {}


    // Pattern K (@ReplaceConstructor): client-side constructor is truncated to field assignment only;
    // the removed tail (registerWorld + chunkProvider init + skylight + weather) moves into finishSetup().
    // Logic delta: mapStorage init is removed entirely — WorldClient sets it before calling finishSetup().
    @ReplaceConstructor
    @Environment(EnvType.CLIENT)
    public void constructor(ISaveHandler par1ISaveHandler, String par2Str, WorldProvider par3WorldProvider, WorldSettings par4WorldSettings, Profiler par5Profiler) {
        superConstructor();

        scheduledUpdatesAreImmediate = false;
        loadedEntityList = new ArrayList();
        unloadedEntityList = new ArrayList();
        loadedTileEntityList = new ArrayList();
        addedTileEntityList = new ArrayList();
        entityRemoval = new ArrayList();
        playerEntities = new ArrayList();
        weatherEffects = new ArrayList();
        cloudColour = 16777215L;
        skylightSubtracted = 0;
        updateLCG = (new Random()).nextInt();
        DIST_HASH_MAGIC = 1013904223;
        lastLightningBolt = 0;
        lightningFlash = 0;
        editingBlocks = false;
        rand = new Random();
        worldAccesses = new ArrayList();
        villageCollectionObj = new VillageCollection((World)(Object)this);
        villageSiegeObj = new VillageSiege((World)(Object)this);
        collidingBoundingBoxes = new ArrayList();
        spawnHostileMobs = true;
        spawnPeacefulMobs = true;
        activeChunkSet = new HashSet();

        this.ambientTickCountdown = this.rand.nextInt(12000);
        this.lightUpdateBlockList = new int['耀'];
        this.entitiesWithinAABBExcludingEntity = new ArrayList();
        this.isRemote = false;
        this.saveHandler = par1ISaveHandler;
        this.theProfiler = par5Profiler;
        this.worldInfo = new WorldInfo(par4WorldSettings, par2Str);
        this.provider = par3WorldProvider;
    }

    // Pattern J (extension interface): finishSetup() holds the constructor tail that WorldClient
    // calls after setting mapStorage, so the dimension initializes in the right order.
    // Broken up so that the WorldClient gets the chance to set the mapstorage object before the dimension initializes
    @Environment(EnvType.CLIENT)
    @Override
    public void finishSetup() {
        provider.registerWorld((World)(Object)this);
        chunkProvider = createChunkProvider();
        calculateInitialSkylight();
        calculateInitialWeather();
    }

    // Pattern N (@Redirect on NEW): replaces the single MapStorage constructor call-site in the
    // server constructor with the singleton-cache helper defined in hunk 5.
    // Logic delta: instead of always constructing a new MapStorage, delegates to getMapStorage()
    // which reuses a cached instance when the same ISaveHandler is passed again.
    @Redirect(
            method = "<init>(Lnet/minecraft/src/ISaveHandler;Ljava/lang/String;Lnet/minecraft/src/WorldSettings;Lnet/minecraft/src/WorldProvider;Lnet/minecraft/src/Profiler;)V",
            at = @At(value = "NEW", target = "net/minecraft/src/MapStorage")
    )
    private MapStorage forge$redirectGetMapStorage(ISaveHandler par1ISaveHandler) {
        return getMapStorage(par1ISaveHandler);
    }

    // Internal singleton cache helper — called only from forge$redirectGetMapStorage (hunk 4).
    //Provides a solution for different worlds getting different copies of the same data, potentially rewriting the data or causing race conditions/stale data
    //Buildcraft has suffered from the issue this fixes.  If you load the same data from two different worlds they can get two different copies of the same object, thus the last saved gets final say.
    private MapStorage getMapStorage(ISaveHandler savehandler) {
        if (s_savehandler != savehandler || s_mapStorage == null) {
            s_mapStorage = new MapStorage(savehandler);
            s_savehandler = savehandler;
        }
        return s_mapStorage;
    }

    @Expression("? == 0")
    @WrapOperation(method = "isAirBlock", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isAirBlock(int left, int right, Operation<Boolean> original,
                                     @Local(argsOnly = true, ordinal = 0) int par1,
                                     @Local(argsOnly = true, ordinal = 1) int par2,
                                     @Local(argsOnly = true, ordinal = 2) int par3) {
        boolean originalResult = original.call(left, right);

        if (!originalResult) {
            Block block = Block.blocksList[left];

            return block == null || ((BlockExtension)block).isAirBlock((World)(Object)this, par1, par2, par3);
        }

        return true;
    }

    // Pattern N (@Redirect): replaces the no-arg hasTileEntity() call-site with the
    // metadata-aware Forge overload hasTileEntity(meta).
    @Redirect(
            method = "blockHasTileEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;hasTileEntity()Z")
    )
    private boolean forge$hasTileEntityWithMeta(Block block,
                                                @Local(argsOnly = true, ordinal = 0) int par1,
                                                @Local(argsOnly = true, ordinal = 1) int par2,
                                                @Local(argsOnly = true, ordinal = 2) int par3) {
        int meta = getBlockMetadata(par1, par2, par3);
        return ((BlockExtension) block).hasTileEntity(meta);
    }

    // Pattern D (@Overwrite): single-expression method fully replaced with provider delegation.
    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider to allow dimension-specific day/night logic
     */
    @Overwrite
    public boolean isDaytime() {
        return ((WorldProviderExtension) provider).isDaytime();
    }

    // Pattern E (@WrapOperation on INVOKE): guards the first canCollideCheck call-site against a
    // NPE that occurs when var13 is null but var11 > 0 (unknown block ID).
    // Logic delta: patch prepends var13 != null to the full condition; wrapping canCollideCheck
    // to return false when the receiver is null produces identical runtime behaviour without
    // touching the surrounding boolean expression.
    @WrapOperation(
            method = "rayTraceBlocks_do_do",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;canCollideCheck(IZ)Z", ordinal = 0)
    )
    private boolean forge$nullGuardCanCollideCheck(Block block, int meta, boolean liquid, Operation<Boolean> original) {
        if (block == null) return false;
        return original.call(block, meta, liquid);
    }

    // Pattern P (@WrapMethod): wraps entire method to post event and pass possibly-modified
    // sound name to the original — preferred over @Inject(HEAD, cancellable) because par2Str
    // must be reassigned from event.name before the original executes.
    // Logic delta: patch uses early return if post() is true; here condition is inverted so
    // original.call() only runs when post() returns false — same net effect.
    @WrapMethod(method = "playSoundAtEntity")
    private void forge$playSoundAtEntity(Entity par1Entity, String par2Str, float par3, float par4, Operation<Void> original) {
        PlaySoundAtEntityEvent event = new PlaySoundAtEntityEvent(par1Entity, par2Str, par3, par4);
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            original.call(par1Entity, event.name, par3, par4);
        }
    }

    // Pattern C (@Inject cancellable at INVOKE): fires just before addEntity is called on the chunk —
    // equivalent to the patch's injection point (right after the instanceof EntityPlayer block closes).
    // Logic delta: var4 (boolean isPlayer) stored as int in bytecode; var4 == 0 means !var4.
    @Inject(
        method = "spawnEntityInWorld",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Chunk;addEntity(Lnet/minecraft/src/Entity;)V"),
        cancellable = true
    )
    private void forge$entityJoinWorldEvent(Entity par1Entity, CallbackInfoReturnable<Boolean> cir,
                                            @Local(ordinal = 2) int var4) {
        if (var4 == 0 && MinecraftForge.EVENT_BUS.post(new EntityJoinWorldEvent(par1Entity, (World)(Object)this))) {
            cir.setReturnValue(false);
        }
    }

    private boolean getSkyColorBody = false;

    @WrapMethod(method = "getSkyColor")
    private Vec3 forge$getSkyColor(Entity par1Entity, float par2, Operation<Vec3> original) {
        if (getSkyColorBody) {
            getSkyColorBody = false;
            return original.call(par1Entity, par2);
        } else {
            return ((WorldProviderExtension) provider).getSkyColor(par1Entity, par2);
        }
    }

    // Pattern J (extension interface): original vanilla sky-colour body for WorldProvider to call back into.
    @Environment(EnvType.CLIENT)
    @Override
    public Vec3 getSkyColorBody(Entity par1Entity, float par2) {
        getSkyColorBody = true;
        return getSkyColor(par1Entity, par2);
    }

    private boolean drawCloudsBody = false;

    @WrapMethod(method = "drawClouds")
    private Vec3 forge$drawClouds(float par1, Operation<Vec3> original) {
        if (drawCloudsBody) {
            drawCloudsBody = false;
            return original.call(par1);
        } else {
            return ((WorldProviderExtension) provider).drawClouds(par1);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Vec3 drawCloudsBody(float par1) {
        drawCloudsBody = true;
        return drawClouds(par1);
    }

    @Definition(id = "blockMaterial", field = "Lnet/minecraft/src/Block;blockMaterial:Lnet/minecraft/src/Material;")
    @Expression("?.blockMaterial")
    @WrapOperation(method = "getTopSolidOrLiquidBlock", at = @At("MIXINEXTRAS:EXPRESSION"))
    private Material forge$captureBlock1(Block instance, Operation<Material> original,
                                         @Share(namespace = "forge", value = "block") LocalRef<Block> block) {
        if (instance != null) block.set(instance);
        return original.call(instance);
    }

    // Pattern O (@ModifyExpressionValue with @Expression): intercepts the result of the
    // `?.blockMaterial != Material.leaves` comparison and additionally gates on !isBlockFoliage().
    // Logic delta: original returns true when material != leaves; we also return false if the
    // block considers itself foliage, preventing it from stopping the downward scan.
    @Definition(id = "blockMaterial", field = "Lnet/minecraft/src/Block;blockMaterial:Lnet/minecraft/src/Material;")
    @Definition(id = "leaves", field = "Lnet/minecraft/src/Material;leaves:Lnet/minecraft/src/Material;")
    @Expression("?.blockMaterial != leaves")
    @ModifyExpressionValue(method = "getTopSolidOrLiquidBlock", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$checkFoliage(boolean original,
                                       @Local(argsOnly = true, ordinal = 0) int par1,
                                       @Local(argsOnly = true, ordinal = 1) int par2,
                                       @Local(ordinal = 2) int var4,
                                       @Share(namespace = "forge", value = "block") LocalRef<Block> blockRef) {
        if (!original) return false;
        Block block = blockRef.get();
        return block == null || !((BlockExtension) block).isBlockFoliage((World)(Object)this, par1, var4, par2);
    }

    private boolean getStarBrightnessBody = false;

    @WrapMethod(method = "getStarBrightness")
    private float forge$getStarBrightness(float par1, Operation<Float> original) {
        if (getStarBrightnessBody) {
            getStarBrightnessBody = false;
            return original.call(par1);
        } else {
            return ((WorldProviderExtension) provider).getStarBrightness(par1);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public float getStarBrightnessBody(float par1) {
        getStarBrightnessBody = true;
        return getStarBrightness(par1);
    }

    // Pattern D (@Overwrite): absorbs hunks 16, 17, and 18 — three structural changes to
    // updateEntities that cannot be layered via injectors without conflicts:
    //   • cleanChunkBlockTileEntity instead of removeChunkBlockTileEntity (hunk 16)
    //   • onChunkUnload() loop before entityRemoval drain (hunk 17)
    //   • setChunkBlockTileEntity moved to the isInvalid else-branch; markBlockNeedsUpdate removed (hunk 18)
    /**
     * @author FabricCompatibilityLayers
     * @reason Forge tile entity lifecycle fixes — see inline comments for per-hunk detail
     */
    @Overwrite
    public void updateEntities() {
        theProfiler.startSection("entities");
        theProfiler.startSection("global");

        for (int var1 = 0; var1 < weatherEffects.size(); var1++) {
            Entity var2 = (Entity)weatherEffects.get(var1);
            var2.onUpdate();
            if (var2.isDead) {
                weatherEffects.remove(var1--);
            }
        }

        theProfiler.endStartSection("remove");
        loadedEntityList.removeAll(unloadedEntityList);

        for (Entity var9 : unloadedEntityList) {
            int var3 = var9.chunkCoordX;
            int var4 = var9.chunkCoordZ;
            if (var9.addedToChunk && chunkExists(var3, var4)) {
                getChunkFromChunkCoords(var3, var4).removeEntity(var9);
            }
        }

        for (Entity var10 : unloadedEntityList) {
            releaseEntitySkin(var10);
        }

        unloadedEntityList.clear();
        theProfiler.endStartSection("regular");

        for (int var7 = 0; var7 < loadedEntityList.size(); var7++) {
            Entity var11 = (Entity)loadedEntityList.get(var7);
            if (var11.ridingEntity != null) {
                if (!var11.ridingEntity.isDead && var11.ridingEntity.riddenByEntity == var11) {
                    continue;
                }

                var11.ridingEntity.riddenByEntity = null;
                var11.ridingEntity = null;
            }

            theProfiler.startSection("tick");
            if (!var11.isDead) {
                updateEntity(var11);
            }

            theProfiler.endSection();
            theProfiler.startSection("remove");
            if (var11.isDead) {
                int var14 = var11.chunkCoordX;
                int var17 = var11.chunkCoordZ;
                if (var11.addedToChunk && chunkExists(var14, var17)) {
                    getChunkFromChunkCoords(var14, var17).removeEntity(var11);
                }

                loadedEntityList.remove(var7--);
                releaseEntitySkin(var11);
            }

            theProfiler.endSection();
        }

        theProfiler.endStartSection("tileEntities");
        scanningTileEntities = true;
        Iterator var8 = loadedTileEntityList.iterator();

        while (var8.hasNext()) {
            TileEntity var12 = (TileEntity)var8.next();
            if (!var12.isInvalid() && ((TileEntityExtension) var12).canUpdate() && blockExists(var12.xCoord, var12.yCoord, var12.zCoord)) {
                var12.updateEntity();
            }

            if (var12.isInvalid()) {
                var8.remove();
                if (chunkExists(var12.xCoord >> 4, var12.zCoord >> 4)) {
                    // hunk 16: cleanChunkBlockTileEntity instead of removeChunkBlockTileEntity
                    Chunk var15 = getChunkFromChunkCoords(var12.xCoord >> 4, var12.zCoord >> 4);
                    if (var15 != null) {
                        ((ChunkExtension)var15).cleanChunkBlockTileEntity(var12.xCoord & 15, var12.yCoord, var12.zCoord & 15);
                    }
                }
            }
        }

        scanningTileEntities = false;
        if (!entityRemoval.isEmpty()) {
            // hunk 17: notify tile entities before removal
            for (Object tile : entityRemoval) {
                ((TileEntityExtension)tile).onChunkUnload();
            }
            loadedTileEntityList.removeAll(entityRemoval);
            entityRemoval.clear();
        }

        theProfiler.endStartSection("pendingTileEntities");
        if (!addedTileEntityList.isEmpty()) {
            for (TileEntity var16 : addedTileEntityList) {
                // hunk 18: setChunkBlockTileEntity moves to else (isInvalid) branch; markBlockNeedsUpdate removed
                if (!var16.isInvalid()) {
                    if (!loadedTileEntityList.contains(var16)) {
                        loadedTileEntityList.add(var16);
                    }
                } else {
                    if (chunkExists(var16.xCoord >> 4, var16.zCoord >> 4)) {
                        Chunk var18 = getChunkFromChunkCoords(var16.xCoord >> 4, var16.zCoord >> 4);
                        if (var18 != null) {
                            var18.setChunkBlockTileEntity(var16.xCoord & 15, var16.yCoord, var16.zCoord & 15, var16);
                        }
                    }
                }
            }

            addedTileEntityList.clear();
        }

        theProfiler.endSection();
        theProfiler.endSection();
    }

    /**
     * @author FabricCompatibilityLayers
     * @reason Filters tile entities through canUpdate() instead of bulk-adding all
     */
    @Overwrite
    public void addTileEntity(Collection par1Collection) {
        List dest = scanningTileEntities ? addedTileEntityList : loadedTileEntityList;
        for (Object entity : par1Collection) {
            if (((TileEntityExtension) entity).canUpdate()) {
                dest.add(entity);
            }
        }
    }

    @ModifyExpressionValue(method = "updateEntityWithOptionalForce", at = @At(value = "CONSTANT", args = "intValue=32"))
    private int forge$modifyChunkRangeCheck(int range,
                                            @Local(ordinal = 0) int var3,
                                            @Local(ordinal = 1) int var4) {
        boolean isForced = getPersistentChunks().containsKey(new ChunkCoordIntPair(var3 >> 4, var4 >> 4));
        return isForced ? 0 : range;
    }

    @WrapOperation(method = "updateEntityWithOptionalForce", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;checkChunksExist(IIIIII)Z"))
    private boolean forge$canUpdateEntityEvent(World instance, int par2, int par3, int par4, int par5, int par6, int i, Operation<Boolean> original,
                                               @Local(argsOnly = true) Entity par1Entity) {
        boolean canUpdate = original.call(instance, par2, par3, par4, par5, par6, i);

        if (!canUpdate) {
            EntityEvent.CanUpdate event = new EntityEvent.CanUpdate(par1Entity);
            MinecraftForge.EVENT_BUS.post(event);
            canUpdate = event.canUpdate;
        }

        return canUpdate;
    }

    @WrapOperation(method = "isBoundingBoxBurning", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I"))
    private int forge$captureBurningBlockPos(World instance, int x, int y, int z, Operation<Integer> original,
                                             @Share(namespace = "forge", value = "pos") LocalRef<ChunkCoordinates> posRef) {
        int result = original.call(instance, x, y, z);
        posRef.set(new ChunkCoordinates(x, y, z));
        return result;
    }

    @Definition(id = "lavaStill", field = "Lnet/minecraft/src/Block;lavaStill:Lnet/minecraft/src/Block;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/Block;blockID:I")
    @Expression("? == lavaStill.blockID")
    @WrapOperation(method = "isBoundingBoxBurning", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isBlockBurning(int left, int right, Operation<Boolean> original,
                                         @Share(namespace = "forge", value = "pos") LocalRef<ChunkCoordinates> posRef) {
        boolean originalResult = original.call(left, right);

        if (!originalResult) {
            Block block = Block.blocksList[left];
            ChunkCoordinates pos = posRef.get();
            return block != null && ((BlockExtension) block).isBlockBurning((World)(Object)this, pos.posX, pos.posY, pos.posZ);
        }

        return true;
    }

    /**
     * @author FabricCompatibilityLayers
     * @reason Always registers with chunk; filters tick list through canUpdate()
     */
    @Overwrite
    public void setBlockTileEntity(int par1, int par2, int par3, TileEntity par4TileEntity) {
        if (par4TileEntity == null || par4TileEntity.isInvalid()) {
            return;
        }
        if (((TileEntityExtension) par4TileEntity).canUpdate()) {
            List dest = scanningTileEntities ? addedTileEntityList : loadedTileEntityList;
            dest.add(par4TileEntity);
        }
        Chunk chunk = getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
        if (chunk != null) {
            chunk.setChunkBlockTileEntity(par1 & 15, par2, par3 & 15, par4TileEntity);
        }
    }

    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates removal entirely to the chunk; list cleanup handled by chunk
     */
    @Overwrite
    public void removeBlockTileEntity(int par1, int par2, int par3) {
        Chunk chunk = getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
        if (chunk != null) {
            chunk.removeChunkBlockTileEntity(par1 & 15, par2, par3 & 15);
        }
    }

    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates to instance isBlockNormalCube() with position context instead of static lookup
     */
    @Overwrite
    public boolean isBlockNormalCube(int par1, int par2, int par3) {
        Block block = Block.blocksList[getBlockId(par1, par2, par3)];
        return block != null && ((BlockExtension) block).isBlockNormalCube((World)(Object)this, par1, par2, par3);
    }

    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates to isBlockSolidOnSide(UP) to allow Forge block overrides
     */
    @Overwrite
    public boolean doesBlockHaveSolidTopSurface(int par1, int par2, int par3) {
        return isBlockSolidOnSide(par1, par2, par3, ForgeDirection.UP);
    }

    /**
     * @author FabricCompatibilityLayers
     * @reason Uses isBlockNormalCube() with Forge override support instead of inline material check
     */
    @Overwrite
    public boolean isBlockNormalCubeDefault(int par1, int par2, int par3, boolean par4) {
        if (par1 >= -30000000 && par3 >= -30000000 && par1 < 30000000 && par3 < 30000000) {
            Chunk var5 = chunkProvider.provideChunk(par1 >> 4, par3 >> 4);
            if (var5 != null && !var5.isEmpty()) {
                Block var6 = Block.blocksList[getBlockId(par1, par2, par3)];
                return var6 != null && isBlockNormalCube(par1, par2, par3);
            } else {
                return par4;
            }
        } else {
            return par4;
        }
    }

    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider to allow dimension-specific spawn type control
     */
    @Overwrite
    public void setAllowedSpawnTypes(boolean par1, boolean par2) {
        ((WorldProviderExtension) provider).setAllowedSpawnTypes(par1, par2);
    }

    private boolean calculateInitialWeatherBody = false;

    @WrapMethod(method = "calculateInitialWeather")
    private void forge$calculateInitialWeather(Operation<Void> original) {
        if (calculateInitialWeatherBody) {
            calculateInitialWeatherBody = true;
            original.call();
        } else {
            ((WorldProviderExtension) provider).calculateInitialWeather();
        }
    }

    @Override
    public void calculateInitialWeatherBody() {
        calculateInitialWeatherBody = true;
        calculateInitialWeather();
    }

    private boolean updateWeatherBody = false;

    @WrapMethod(method = "updateWeather")
    private void forge$updateWeather(Operation<Void> original) {
        if (updateWeatherBody) {
            updateWeatherBody = false;
            original.call();
        } else {
            ((WorldProviderExtension) provider).updateWeather();
        }
    }

    @Override
    public void updateWeatherBody() {
        updateWeatherBody = true;
        updateWeather();
    }

    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates rain toggle to WorldProvider for dimension overrides
     */
    @Overwrite
    public void toggleRain() {
        ((WorldProviderExtension) provider).toggleRain();
    }

    // Pattern C (@Inject before startSection): inserts persistent (force-loaded) chunks into
    // activeChunkSet so they remain ticked even when no player is nearby.
    @Inject(
            method = "setActivePlayerChunksAndCheckLight",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Profiler;startSection(Ljava/lang/String;)V", ordinal = 0)
    )
    private void forge$addPersistentChunks(CallbackInfo ci) {
        activeChunkSet.addAll(getPersistentChunks().keySet());
    }

    private boolean canBlockFreezeBody = false;

    @WrapMethod(method = "canBlockFreeze")
    private boolean forge$canBlockFreeze(int par1, int par2, int par3, boolean par4, Operation<Boolean> original) {
        if (canBlockFreezeBody) {
            canBlockFreezeBody = false;
            return original.call(par1, par2, par3, par4);
        } else {
            return ((WorldProviderExtension) provider).canBlockFreeze(par1, par2, par3, par4);
        }
    }

    @Override
    public boolean canBlockFreezeBody(int par1, int par2, int par3, boolean par4) {
        canBlockFreezeBody = true;
        return canBlockFreeze(par1, par2, par3, par4);
    }

    private boolean canSnowAtBody = false;

    @WrapMethod(method = "canSnowAt")
    private boolean forge$canSnowAt(int par1, int par2, int par3, Operation<Boolean> original) {
        if (canSnowAtBody) {
            canSnowAtBody = false;
            return original.call(par1, par2, par3);
        } else {
            return ((WorldProviderExtension) provider).canSnowAt(par1, par2, par3);
        }
    }

    @Override
    public boolean canSnowAtBody(int par1, int par2, int par3) {
        canSnowAtBody = true;
        return canSnowAt(par1, par2, par3);
    }

    @Definition(id = "lightValue", field = "Lnet/minecraft/src/Block;lightValue:[I")
    @Expression("lightValue[?]")
    @WrapOperation(method = "computeBlockLightValue", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int forge$getBlockLightValue(int[] lightValue, int par5, Operation<Integer> original,
                                         @Local(argsOnly = true, ordinal = 1) int par2,
                                         @Local(argsOnly = true, ordinal = 2) int par3,
                                         @Local(argsOnly = true, ordinal = 3) int par4) {
        return par5 == 0 || Block.blocksList[par5] == null ? 0
                : ((BlockExtension) Block.blocksList[par5]).getLightValue((World)(Object)this, par2, par3, par4);
    }

    @WrapOperation(method = "updateLightByType", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 1))
    private int forge$captureLightPos1(World instance, int par2, int par3, int i, Operation<Integer> original,
                                       @Share(namespace = "forge", value = "lightPos1") LocalRef<ChunkCoordinates> posRef) {
        int result = original.call(instance, par2, par3, i);
        posRef.set(new ChunkCoordinates(par2, par3, i));
        return result;
    }

    // hunk 34 — lightOpacity[getBlockId(var20,var21,var22)] inside the nested for loop
    @Definition(id = "lightOpacity", field = "Lnet/minecraft/src/Block;lightOpacity:[I")
    @Expression("lightOpacity[?]")
    @WrapOperation(method = "updateLightByType", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private int forge$lightOpacityInnerLoop(int[] lightOpacity, int index, Operation<Integer> original,
                                            @Share(namespace = "forge", value = "lightPos1") LocalRef<ChunkCoordinates> posRef) {
        ChunkCoordinates pos = posRef.get();
        return getBlockLightOpacity(pos.posX, pos.posY, pos.posZ);
    }

    @WrapOperation(method = "updateLightByType", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I", ordinal = 2))
    private int forge$captureLightPos2(World instance, int par2, int par3, int i, Operation<Integer> original,
                                       @Share(namespace = "forge", value = "lightPos2") LocalRef<ChunkCoordinates> posRef) {
        int result = original.call(instance, par2, par3, i);
        posRef.set(new ChunkCoordinates(par2, par3, i));
        return result;
    }

    // hunk 35 — lightOpacity[var34] in the second while loop
    @Definition(id = "lightOpacity", field = "Lnet/minecraft/src/Block;lightOpacity:[I")
    @Expression("lightOpacity[?]")
    @WrapOperation(method = "updateLightByType", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
    private int forge$lightOpacityOuterLoop(int[] lightOpacity, int index, Operation<Integer> original,
                                            @Share(namespace = "forge", value = "lightPos2") LocalRef<ChunkCoordinates> posRef) {
        ChunkCoordinates pos = posRef.get();
        return getBlockLightOpacity(pos.posX, pos.posY, pos.posZ);
    }

    @ModifyConstant(
            method = {"getEntitiesWithinAABBExcludingEntity", "getEntitiesWithinAABB"},
            constant = @Constant(doubleValue = 2.0D)
    )
    private double forge$maxEntityRadius(double constant) {
        return MAX_ENTITY_RADIUS;
    }

    /**
     * @author FabricCompatibilityLayers
     * @reason Fires EntityJoinWorldEvent per entity instead of bulk-adding all
     */
    @Overwrite
    public void addLoadedEntities(List par1List) {
        for (int var2 = 0; var2 < par1List.size(); ++var2) {
            Entity entity = (Entity) par1List.get(var2);
            if (!MinecraftForge.EVENT_BUS.post(new EntityJoinWorldEvent(entity, (World)(Object)this))) {
                loadedEntityList.add(entity);
                obtainEntitySkin(entity);
            }
        }
    }

    @Definition(id = "par1", local = @Local(argsOnly = true, ordinal = 0, type = int.class))
    @Expression("par1 > 0")
    @WrapOperation(method = "canPlaceEntityOnSide", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isBlockReplaceable(int left, int right, Operation<Boolean> original,
                                             @Local(ordinal = 0) LocalRef<Block> var9Ref,
                                             @Local(argsOnly = true, ordinal = 1) int par2,
                                             @Local(argsOnly = true, ordinal = 2) int par3,
                                             @Local(argsOnly = true, ordinal = 3) int par4) {
        Block var9 = var9Ref.get();
        if (var9 != null && ((BlockExtension) var9).isBlockReplaceable((World)(Object)this, par2, par3, par4)) {
            var9Ref.set(null);
        }
        return original.call(left, right);
    }

    /** @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider */
    @Overwrite
    public void setWorldTime(long par1) {
        ((WorldProviderExtension) provider).setWorldTime(par1);
    }

    /** @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider */
    @Overwrite
    public long getSeed() {
        return ((WorldProviderExtension) provider).getSeed();
    }

    /** @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider */
    @Overwrite
    public long getWorldTime() {
        return ((WorldProviderExtension) provider).getWorldTime();
    }

    /** @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider */
    @Overwrite
    public ChunkCoordinates getSpawnPoint() {
        return ((WorldProviderExtension) provider).getSpawnPoint();
    }

    /** @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider */
    @Overwrite
    @Environment(EnvType.CLIENT)
    public void setSpawnLocation(int par1, int par2, int par3) {
        ((WorldProviderExtension) provider).setSpawnPoint(par1, par2, par3);
    }

    @WrapOperation(
            method = "joinEntityInSurroundings",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z")
    )
    private boolean forge$entityJoinWorld(List list, Object entity, Operation<Boolean> original) {
        if (!MinecraftForge.EVENT_BUS.post(new EntityJoinWorldEvent((Entity) entity, (World) (Object) this))) {
            return original.call(list, entity);
        } else {
            return false;
        }
    }

    private boolean canMineBlockBody = false;

    @WrapMethod(method = "canMineBlock")
    private boolean forge$canMineBlock(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, Operation<Boolean> original) {
        if (canMineBlockBody) {
            canMineBlockBody = false;
            return original.call(par1EntityPlayer, par2, par3, par4);
        } else {
            return ((WorldProviderExtension) provider).canMineBlock(par1EntityPlayer, par2, par3, par4);
        }
    }

    @Override
    public boolean canMineBlockBody(EntityPlayer par1EntityPlayer, int par2, int par3, int par4) {
        canMineBlockBody = true;
        return canMineBlock(par1EntityPlayer, par2, par3, par4);
    }

    /** @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider */
    @Overwrite
    public boolean isBlockHighHumidity(int par1, int par2, int par3) {
        return ((WorldProviderExtension) provider).isBlockHighHumidity(par1, par2, par3);
    }

    /** @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider */
    @Overwrite
    public int getHeight() {
        return ((WorldProviderExtension) provider).getHeight();
    }

    /** @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider */
    @Overwrite
    public int getActualHeight() {
        return ((WorldProviderExtension) provider).getActualHeight();
    }

    /** @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider */
    @Overwrite
    public double getHorizon() {
        return ((WorldProviderExtension) provider).getHorizon();
    }

    @Override
    public void addTileEntity(TileEntity entity) {
        List dest = scanningTileEntities ? addedTileEntityList : loadedTileEntityList;
        if (((TileEntityExtension) entity).canUpdate()) {
            dest.add(entity);
        }
    }

    @Override
    public boolean isBlockSolidOnSide(int X, int Y, int Z, ForgeDirection side) {
        return isBlockSolidOnSide(X, Y, Z, side, false);
    }

    @Override
    public boolean isBlockSolidOnSide(int X, int Y, int Z, ForgeDirection side, boolean _default) {
        if (X < -30000000 || Z < -30000000 || X >= 30000000 || Z >= 30000000) {
            return _default;
        }

        Chunk var5 = chunkProvider.provideChunk(X >> 4, Z >> 4);

        if (var5 == null || var5.isEmpty()) {
            return _default;
        }

        Block block = Block.blocksList[getBlockId(X, Y, Z)];

        if (block == null) {
            return false;
        }

        return ((BlockExtension) block).isBlockSolidOnSide((World)(Object)this, X, Y, Z, side);
    }

    @Override
    public SetMultimap<ChunkCoordIntPair, ForgeChunkManager.Ticket> getPersistentChunks() {
        return ForgeChunkManager.getPersistentChunksFor((World)(Object)this);
    }

    // == Injections section: end ==
}