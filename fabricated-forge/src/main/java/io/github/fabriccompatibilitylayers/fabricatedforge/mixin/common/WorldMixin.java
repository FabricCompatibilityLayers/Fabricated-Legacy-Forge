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
    @Shadow public abstract Chunk getChunkFromBlockCoords(int par1, int par2);
    @Shadow public abstract Chunk getChunkFromChunkCoords(int par1, int par2);
    @Shadow public abstract int getBlockMetadata(int par1, int par2, int par3);
    @Shadow protected abstract IChunkProvider createChunkProvider();
    @Shadow public abstract void calculateInitialSkylight();
    @Shadow protected float rainingStrength;
    @Shadow protected float prevRainingStrength;
    @Shadow protected float thunderingStrength;
    @Shadow protected float prevThunderingStrength;
    @Shadow protected int lastLightningBolt;
    @Shadow public Random rand;
    @Shadow protected Set activeChunkSet;
    @Shadow public abstract float getCelestialAngle(float par1);
    @Shadow public abstract float getRainStrength(float par1);
    @Shadow public abstract float getWeightedThunderStrength(float par1);
    @Shadow public int lightningFlash;
    @Shadow private long cloudColour;
    @Shadow public void updateEntity(Entity par1Entity) {}
    @Shadow protected void releaseEntitySkin(Entity par1Entity) {}
    @Shadow protected void obtainEntitySkin(Entity par1Entity) {}
    @Shadow protected boolean chunkExists(int par1, int par2) { return false; }
    @Shadow public abstract boolean checkChunksExist(int par1, int par2, int par3, int par4, int par5, int par6);

    @Shadow
    public abstract int getSavedLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4);

    @Shadow
    public abstract Material getBlockMaterial(int par1, int par2, int par3);

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
    @Shadow
    public MapStorage mapStorage;

    // == Shadowed members section: end ==

    // == New fields section: start ==

    @Shadow
    public abstract Vec3Pool func_82732_R();

    @Public
    private static double MAX_ENTITY_RADIUS = 2.0D;

    private static MapStorage s_mapStorage;
    private static ISaveHandler s_savehandler;

    // == New fields section: end ==

    // == Injections section: start ==

    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider for dimension-aware biome resolution
     */
    @Overwrite
    public BiomeGenBase getBiomeGenForCoords(int par1, int par2) {
        return ((WorldProviderExtension) provider).getBiomeGenForCoords(par1, par2);
    }

    @Override
    public BiomeGenBase getBiomeGenForCoordsBody(int par1, int par2) {
        if (this.blockExists(par1, 0, par2)) {
            Chunk var3 = this.getChunkFromBlockCoords(par1, par2);
            if (var3 != null) {
                return var3.getBiomeGenForWorldCoords(par1 & 15, par2 & 15, this.provider.worldChunkMgr);
            }
        }

        return this.provider.worldChunkMgr.getBiomeGenAt(par1, par2);
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

        this.mapStorage = new MapStorage(saveHandler);
        VillageCollection var6 = (VillageCollection)this.mapStorage.loadData(VillageCollection.class, "villages");
        if (var6 == null)
        {
            this.villageCollectionObj = new VillageCollection((World) (Object) this);
            this.mapStorage.setData("villages", this.villageCollectionObj);
        }
        else
        {
            this.villageCollectionObj = var6;
            this.villageCollectionObj.func_82566_a((World) (Object) this);
        }

        provider.registerWorld((World) (Object) this);
        this.chunkProvider = this.createChunkProvider();
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
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

    // Pattern D (@Overwrite): single-line provider delegation.
    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider for dimension-specific sky colour
     */
    @Overwrite
    @Environment(EnvType.CLIENT)
    public Vec3 getSkyColor(Entity par1Entity, float par2) {
        return ((WorldProviderExtension) provider).getSkyColor(par1Entity, par2);
    }

    // Pattern J (extension interface): original vanilla sky-colour body for WorldProvider to call back into.
    @Environment(EnvType.CLIENT)
    @Override
    public Vec3 getSkyColorBody(Entity par1Entity, float par2) {
        float var3 = this.getCelestialAngle(par2);
        float var4 = MathHelper.cos(var3 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
        if (var4 < 0.0F) {
            var4 = 0.0F;
        }

        if (var4 > 1.0F) {
            var4 = 1.0F;
        }

        int var5 = MathHelper.floor_double(par1Entity.posX);
        int var6 = MathHelper.floor_double(par1Entity.posZ);
        BiomeGenBase var7 = this.getBiomeGenForCoords(var5, var6);
        float var8 = var7.getFloatTemperature();
        int var9 = var7.getSkyColorByTemp(var8);
        float var10 = (float)(var9 >> 16 & 255) / 255.0F;
        float var11 = (float)(var9 >> 8 & 255) / 255.0F;
        float var12 = (float)(var9 & 255) / 255.0F;
        var10 *= var4;
        var11 *= var4;
        var12 *= var4;
        float var13 = this.getRainStrength(par2);
        if (var13 > 0.0F) {
            float var14 = (var10 * 0.3F + var11 * 0.59F + var12 * 0.11F) * 0.6F;
            float var15 = 1.0F - var13 * 0.75F;
            var10 = var10 * var15 + var14 * (1.0F - var15);
            var11 = var11 * var15 + var14 * (1.0F - var15);
            var12 = var12 * var15 + var14 * (1.0F - var15);
        }

        float var20 = this.getWeightedThunderStrength(par2);
        if (var20 > 0.0F) {
            float var21 = (var10 * 0.3F + var11 * 0.59F + var12 * 0.11F) * 0.2F;
            float var16 = 1.0F - var20 * 0.75F;
            var10 = var10 * var16 + var21 * (1.0F - var16);
            var11 = var11 * var16 + var21 * (1.0F - var16);
            var12 = var12 * var16 + var21 * (1.0F - var16);
        }

        if (this.lightningFlash > 0) {
            float var22 = (float)this.lightningFlash - par2;
            if (var22 > 1.0F) {
                var22 = 1.0F;
            }

            var22 *= 0.45F;
            var10 = var10 * (1.0F - var22) + 0.8F * var22;
            var11 = var11 * (1.0F - var22) + 0.8F * var22;
            var12 = var12 * (1.0F - var22) + 1.0F * var22;
        }

        return this.func_82732_R().getVecFromPool((double)var10, (double)var11, (double)var12);
    }

    // Pattern D (@Overwrite) + Pattern J (extension interface): same delegation split as getSkyColor.
    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider for dimension-specific cloud colour
     */
    @Overwrite
    @Environment(EnvType.CLIENT)
    public Vec3 drawClouds(float par1) {
        return ((WorldProviderExtension) provider).drawClouds(par1);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Vec3 drawCloudsBody(float par1) {
        float var2 = this.getCelestialAngle(par1);
        float var3 = MathHelper.cos(var2 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
        if (var3 < 0.0F) {
            var3 = 0.0F;
        }

        if (var3 > 1.0F) {
            var3 = 1.0F;
        }

        float var4 = (float)(this.cloudColour >> 16 & 255L) / 255.0F;
        float var5 = (float)(this.cloudColour >> 8 & 255L) / 255.0F;
        float var6 = (float)(this.cloudColour & 255L) / 255.0F;
        float var7 = this.getRainStrength(par1);
        if (var7 > 0.0F) {
            float var8 = (var4 * 0.3F + var5 * 0.59F + var6 * 0.11F) * 0.6F;
            float var9 = 1.0F - var7 * 0.95F;
            var4 = var4 * var9 + var8 * (1.0F - var9);
            var5 = var5 * var9 + var8 * (1.0F - var9);
            var6 = var6 * var9 + var8 * (1.0F - var9);
        }

        var4 *= var3 * 0.9F + 0.1F;
        var5 *= var3 * 0.9F + 0.1F;
        var6 *= var3 * 0.85F + 0.15F;
        float var14 = this.getWeightedThunderStrength(par1);
        if (var14 > 0.0F) {
            float var15 = (var4 * 0.3F + var5 * 0.59F + var6 * 0.11F) * 0.2F;
            float var10 = 1.0F - var14 * 0.95F;
            var4 = var4 * var10 + var15 * (1.0F - var10);
            var5 = var5 * var10 + var15 * (1.0F - var10);
            var6 = var6 * var10 + var15 * (1.0F - var10);
        }

        return this.func_82732_R().getVecFromPool((double)var4, (double)var5, (double)var6);
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

    // Pattern D (@Overwrite) + Pattern J (extension interface): same delegation split as getSkyColor.
    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider for dimension-specific star brightness
     */
    @Overwrite
    @Environment(EnvType.CLIENT)
    public float getStarBrightness(float par1) {
        return ((WorldProviderExtension) provider).getStarBrightness(par1);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public float getStarBrightnessBody(float par1) {
        float var2 = this.getCelestialAngle(par1);
        float var3 = 1.0F - (MathHelper.cos(var2 * (float)Math.PI * 2.0F) * 2.0F + 0.25F);
        if (var3 < 0.0F) {
            var3 = 0.0F;
        }

        if (var3 > 1.0F) {
            var3 = 1.0F;
        }

        return var3 * var3 * 0.5F;
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

    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates initial weather setup to WorldProvider for dimension overrides
     */
    @Overwrite
    private void calculateInitialWeather() {
        ((WorldProviderExtension) provider).calculateInitialWeather();
    }

    @Override
    public void calculateInitialWeatherBody() {
        if (this.worldInfo.isRaining()) {
            this.rainingStrength = 1.0F;
            if (this.worldInfo.isThundering()) {
                this.thunderingStrength = 1.0F;
            }
        }
    }

    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates weather update to WorldProvider for dimension overrides
     */
    @Overwrite
    public void updateWeather() {
        ((WorldProviderExtension) provider).updateWeather();
    }

    @Override
    public void updateWeatherBody() {
        if (!this.provider.hasNoSky) {
            if (this.lastLightningBolt > 0) {
                --this.lastLightningBolt;
            }

            int var1 = this.worldInfo.getThunderTime();
            if (var1 <= 0) {
                if (this.worldInfo.isThundering()) {
                    this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
                } else {
                    this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
                }
            } else {
                --var1;
                this.worldInfo.setThunderTime(var1);
                if (var1 <= 0) {
                    this.worldInfo.setThundering(!this.worldInfo.isThundering());
                }
            }

            int var2 = this.worldInfo.getRainTime();
            if (var2 <= 0) {
                if (this.worldInfo.isRaining()) {
                    this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
                } else {
                    this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
                }
            } else {
                --var2;
                this.worldInfo.setRainTime(var2);
                if (var2 <= 0) {
                    this.worldInfo.setRaining(!this.worldInfo.isRaining());
                }
            }

            this.prevRainingStrength = this.rainingStrength;
            if (this.worldInfo.isRaining()) {
                this.rainingStrength = (float)((double)this.rainingStrength + 0.01);
            } else {
                this.rainingStrength = (float)((double)this.rainingStrength - 0.01);
            }

            if (this.rainingStrength < 0.0F) {
                this.rainingStrength = 0.0F;
            }

            if (this.rainingStrength > 1.0F) {
                this.rainingStrength = 1.0F;
            }

            this.prevThunderingStrength = this.thunderingStrength;
            if (this.worldInfo.isThundering()) {
                this.thunderingStrength = (float)((double)this.thunderingStrength + 0.01);
            } else {
                this.thunderingStrength = (float)((double)this.thunderingStrength - 0.01);
            }

            if (this.thunderingStrength < 0.0F) {
                this.thunderingStrength = 0.0F;
            }

            if (this.thunderingStrength > 1.0F) {
                this.thunderingStrength = 1.0F;
            }

        }
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

    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider for dimension-specific freeze logic
     */
    @Overwrite
    public boolean canBlockFreeze(int par1, int par2, int par3, boolean par4) {
        return ((WorldProviderExtension) provider).canBlockFreeze(par1, par2, par3, par4);
    }

    @Override
    public boolean canBlockFreezeBody(int par1, int par2, int par3, boolean par4) {
        BiomeGenBase var5 = this.getBiomeGenForCoords(par1, par3);
        float var6 = var5.getFloatTemperature();
        if (var6 > 0.15F) {
            return false;
        } else {
            if (par2 >= 0 && par2 < 256 && this.getSavedLightValue(EnumSkyBlock.Block, par1, par2, par3) < 10) {
                int var7 = this.getBlockId(par1, par2, par3);
                if ((var7 == Block.waterStill.blockID || var7 == Block.waterMoving.blockID) && this.getBlockMetadata(par1, par2, par3) == 0) {
                    if (!par4) {
                        return true;
                    }

                    boolean var8 = true;
                    if (var8 && this.getBlockMaterial(par1 - 1, par2, par3) != Material.water) {
                        var8 = false;
                    }

                    if (var8 && this.getBlockMaterial(par1 + 1, par2, par3) != Material.water) {
                        var8 = false;
                    }

                    if (var8 && this.getBlockMaterial(par1, par2, par3 - 1) != Material.water) {
                        var8 = false;
                    }

                    if (var8 && this.getBlockMaterial(par1, par2, par3 + 1) != Material.water) {
                        var8 = false;
                    }

                    if (!var8) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider for dimension-specific snow placement logic
     */
    @Overwrite
    public boolean canSnowAt(int par1, int par2, int par3) {
        return ((WorldProviderExtension) provider).canSnowAt(par1, par2, par3);
    }

    @Override
    public boolean canSnowAtBody(int par1, int par2, int par3) {
        BiomeGenBase var4 = this.getBiomeGenForCoords(par1, par3);
        float var5 = var4.getFloatTemperature();
        if (var5 > 0.15F) {
            return false;
        } else {
            if (par2 >= 0 && par2 < 256 && this.getSavedLightValue(EnumSkyBlock.Block, par1, par2, par3) < 10) {
                int var6 = this.getBlockId(par1, par2 - 1, par3);
                int var7 = this.getBlockId(par1, par2, par3);
                if (var7 == 0 && Block.snow.canPlaceBlockAt((World) (Object) this, par1, par2, par3) && var6 != 0 && var6 != Block.ice.blockID && Block.blocksList[var6].blockMaterial.blocksMovement()) {
                    return true;
                }
            }

            return false;
        }
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
            method = {"getEntitiesWithinAABBExcludingEntity", "func_82733_a"},
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

    @ModifyExpressionValue(method = "canPlaceEntityOnSide", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Material;isGroundCover()Z"))
    private boolean forge$isBlockReplaceable(boolean original,
                                             @Local(argsOnly = true, ordinal = 1) int par2,
                                             @Local(argsOnly = true, ordinal = 2) int par3,
                                             @Local(argsOnly = true, ordinal = 3) int par4,
                                             @Local(ordinal = 0) Block var9) {
        return original || ((BlockExtension) var9).isBlockReplaceable((World)(Object)this, par2, par3, par4);
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

    /** @author FabricCompatibilityLayers
     * @reason Delegates to WorldProvider for dimension-specific mine permissions */
    @Overwrite
    public boolean canMineBlock(EntityPlayer par1EntityPlayer, int par2, int par3, int par4) {
        return ((WorldProviderExtension) provider).canMineBlock(par1EntityPlayer, par2, par3, par4);
    }

    @Override
    public boolean canMineBlockBody(EntityPlayer par1EntityPlayer, int par2, int par3, int par4) {
        return true;
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