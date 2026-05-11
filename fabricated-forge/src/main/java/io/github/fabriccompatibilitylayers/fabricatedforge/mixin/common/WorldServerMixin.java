package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldProviderExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldServerExtension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.*;

@Mixin(WorldServer.class)
public abstract class WorldServerMixin extends World implements WorldServerExtension {

    public WorldServerMixin(ISaveHandler par1ISaveHandler, String par2Str,
                            WorldSettings par3WorldSettings, WorldProvider par4WorldProvider,
                            Profiler par5Profiler) {
        super(par1ISaveHandler, par2Str, par3WorldSettings, par4WorldProvider, par5Profiler);
    }

    @Shadow private MinecraftServer mcServer;
    @Shadow public ChunkProviderServer theChunkProviderServer;

    // Pattern: @Unique field — new instance field injected into WorldServer.
    // Logic delta: 1:1 translation; protected visibility preserved via @Unique.
    @Unique
    protected Set<ChunkCoordIntPair> doneChunks = new HashSet<>();

    // Pattern A (@Inject at TAIL): fires at end of constructor to register dimension.
    // Logic delta: 1:1 translation.
    @Inject(
            method = "<init>(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/src/ISaveHandler;Ljava/lang/String;ILnet/minecraft/src/WorldSettings;Lnet/minecraft/src/Profiler;)V",
            at = @At("TAIL")
    )
    private void forge$registerDimension(CallbackInfo ci,
                                         @Local(argsOnly = true, ordinal = 0) int par4) {
        DimensionManager.setWorld(par4, (WorldServer) (Object) this);
    }

    // Pattern D (@Overwrite): 100% body replacement with single provider delegation.
    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates rain/thunder reset to WorldProvider for dimension overrides
     */
    @Overwrite
    private void resetRainAndThunder() {
        ((WorldProviderExtension) provider).resetRainAndThunder();
    }

    // Pattern E (@WrapOperation on super INVOKE): runs doneChunks init immediately after
    // super.tickBlocksAndAmbiance() returns, just before the active-chunk loop.
    // Logic delta: time is stored in a @Share LocalRef<Long> for use in the updateSkylight
    // guard (hunk 7) — the original patch used a plain local, but two separate injectors
    // need to share it across injection points.
    @WrapOperation(
        method = "tickBlocksAndAmbiance",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;tickBlocksAndAmbiance()V")
    )
    private void forge$initDoneChunks(WorldServer instance, Operation<Void> original,
                                       @Share(namespace = "forge", value = "tickTime") LocalRef<Long> tickTimeRef) {
        original.call(instance);
        doneChunks.retainAll(activeChunkSet);
        if (doneChunks.size() == activeChunkSet.size()) {
            doneChunks.clear();
        }
        tickTimeRef.set(-System.currentTimeMillis());
    }

    // Pattern Q (@WrapWithCondition): gates updateSkylight() behind a time-budget and doneChunks check.
    // Logic delta: time is retrieved from @Share (set in forge$initDoneChunks); doneChunks.add() both tests and records.
    @WrapWithCondition(
        method = "tickBlocksAndAmbiance",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Chunk;updateSkylight()V")
    )
    private boolean forge$skylightTimeGuard(Chunk instance,
                                            @Local(ordinal = 0) ChunkCoordIntPair var4,
                                            @Share(namespace = "forge", value = "tickTime") LocalRef<Long> tickTimeRef) {
        return System.currentTimeMillis() + tickTimeRef.get() <= 4 && doneChunks.add(var4);
    }

    // Pattern E (@WrapOperation on INVOKE): prepends provider.canDoLightning() to the lightning condition.
    // Logic delta: returns 1 (non-zero) when canDoLightning() is false so nextInt() == 0 evaluates false.
    // ordinal = 0 targets rand.nextInt(100000) — the first nextInt call in tickBlocksAndAmbiance.
    @WrapOperation(
        method = "tickBlocksAndAmbiance",
        at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", ordinal = 0)
    )
    private int forge$lightningGuard(Random instance, int bound, Operation<Integer> original,
                                      @Local(ordinal = 0) Chunk var7) {
        if (!((WorldProviderExtension) provider).canDoLightning(var7)) return 1;
        return original.call(instance, bound);
    }

    // Pattern E (@WrapOperation on INVOKE): prepends provider.canDoRainSnowIce() to the ice/snow condition.
    // Logic delta: same technique as forge$lightningGuard; ordinal = 1 targets rand.nextInt(16).
    @WrapOperation(
        method = "tickBlocksAndAmbiance",
        at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", ordinal = 1)
    )
    private int forge$iceSnowGuard(Random instance, int bound, Operation<Integer> original,
                                    @Local(ordinal = 0) Chunk var7) {
        if (!((WorldProviderExtension) provider).canDoRainSnowIce(var7)) return 1;
        return original.call(instance, bound);
    }

    // Pattern O (@ModifyExpressionValue on CONSTANT): intercepts the byte 8 constant assigned to var7,
    // replacing it with 0 when the chunk is force-loaded so the existence check is skipped.
    // Logic delta: patch computed a boolean first then selected; here we reuse the original value (8)
    // directly in the ternary, avoiding a redundant re-hardcode.
    @ModifyExpressionValue(
        method = "scheduleBlockUpdate(IIIII)V",
        at = @At(value = "CONSTANT", args = "intValue=8")
    )
    private int forge$forcedChunkScheduleRange(int original,
                                                @Local(ordinal = 0) NextTickListEntry var6) {
        boolean isForced = ForgeChunkManager.getPersistentChunksFor(this)
            .containsKey(new ChunkCoordIntPair(var6.xCoord >> 4, var6.zCoord >> 4));
        return isForced ? 0 : original;
    }

    // Pattern O (@ModifyExpressionValue on INVOKE): ANDs playerEntities.isEmpty() with
    // getPersistentChunks().isEmpty() so the 60-tick idle skip is suppressed when force-loaded chunks exist.
    @ModifyExpressionValue(
        method = "updateEntities",
        at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z")
    )
    private boolean forge$checkPersistentChunksForIdle(boolean original) {
        return original && ForgeChunkManager.getPersistentChunksFor(this).isEmpty();
    }

    // Pattern O (@ModifyExpressionValue on CONSTANT): same forced-chunk range check as
    // forge$forcedChunkScheduleRange, applied to the tick-processing range in tickUpdates.
    @ModifyExpressionValue(
        method = "tickUpdates(Z)Z",
        at = @At(value = "CONSTANT", args = "intValue=8")
    )
    private int forge$forcedChunkTickRange(int original,
                                            @Local(ordinal = 0) NextTickListEntry var4) {
        boolean isForced = ForgeChunkManager.getPersistentChunksFor(this)
            .containsKey(new ChunkCoordIntPair(var4.xCoord >> 4, var4.zCoord >> 4));
        return isForced ? 0 : original;
    }

    // Pattern D (@Overwrite): 100% body replacement — chunk-based spatial iteration replaces flat
    // loadedTileEntityList scan for better performance and correctness.
    /**
     * @author FabricCompatibilityLayers
     * @reason Iterates tile entities by chunk rather than scanning all loaded tile entities globally
     */
    @Overwrite
    public List getAllTileEntityInBox(int par1, int par2, int par3, int par4, int par5, int par6) {
        ArrayList var7 = new ArrayList();

        for(int x = (par1 >> 4); x <= (par4 >> 4); x++)
        {
            for(int z = (par3 >> 4); z <= (par6 >> 4); z++)
            {
                Chunk chunk = getChunkFromChunkCoords(x, z);
                if (chunk != null)
                {
                    for(Object obj : chunk.chunkTileEntityMap.values())
                    {
                        TileEntity entity = (TileEntity)obj;
                        if (!entity.isInvalid())
                        {
                            if (entity.xCoord >= par1 && entity.yCoord >= par2 && entity.zCoord >= par3 &&
                                    entity.xCoord <= par4 && entity.yCoord <= par5 && entity.zCoord <= par6)
                            {
                                var7.add(entity);
                            }
                        }
                    }
                }
            }
        }

        return var7;
    }

    // Pattern D (@Overwrite): routes WorldServer.canMineBlock through super so WorldMixin's
    // provider delegation fires, then bounces back to canMineBlockBody below.
    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates to provider via super; spawn-protection body moved to canMineBlockBody
     */
    @Overwrite
    public boolean canMineBlock(EntityPlayer par1EntityPlayer, int par2, int par3, int par4) {
        return super.canMineBlock(par1EntityPlayer, par2, par3, par4);
    }

    // Pattern J (extension interface override): WorldServer-specific canMineBlockBody overrides
    // WorldMixin's default (returns true) with spawn-protection logic.
    // Logic delta: hardcoded 16 replaced with mcServer.spawnProtectionSize.
    @Override
    public boolean canMineBlockBody(EntityPlayer par1EntityPlayer, int par2, int par3, int par4) {
        int var5 = MathHelper.abs_int(par2 - worldInfo.getSpawnX());
        int var6 = MathHelper.abs_int(par4 - worldInfo.getSpawnZ());
        if (var5 > var6) {
            var6 = var5;
        }
        return var6 > ((MinecraftServerAccessor) mcServer).getSpawnProtectionSize()
            || mcServer.getConfigurationManager().areCommandsAllowed(par1EntityPlayer.username)
            || mcServer.isSinglePlayer();
    }

    // Pattern G (@WrapOperation + @Expression): intercepts the new WorldGeneratorBonusChest(...)
    // constructor call-site and replaces both arguments with ChestGenHooks API calls.
    // Logic delta: 1:1 — bonusChestContent → getItems(BONUS_CHEST), 10 → getCount(BONUS_CHEST, rand).
    @Definition(id = "WorldGeneratorBonusChest", type = WorldGeneratorBonusChest.class)
    @Expression("new WorldGeneratorBonusChest(?, ?)")
    @WrapOperation(method = "createBonusChest", at = @At("MIXINEXTRAS:EXPRESSION"))
    private WorldGeneratorBonusChest forge$bonusChestFromHooks(
            WeightedRandomChestContent[] items, int count, Operation<WorldGeneratorBonusChest> original) {
        return original.call(
            ChestGenHooks.getItems(ChestGenHooks.BONUS_CHEST),
            ChestGenHooks.getCount(ChestGenHooks.BONUS_CHEST, rand)
        );
    }

    // Pattern E (@WrapOperation on INVOKE): delegates to original saveChunks, then immediately
    // posts WorldEvent.Save while still inside the canSave() guard.
    @WrapOperation(
        method = "saveAllChunks",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/IChunkProvider;saveChunks(ZLnet/minecraft/src/IProgressUpdate;)Z")
    )
    private boolean forge$postSaveEvent(IChunkProvider instance, boolean par1,
                                         IProgressUpdate par2IProgressUpdate, Operation<Boolean> original) {
        boolean result = original.call(instance, par1, par2IProgressUpdate);
        MinecraftForge.EVENT_BUS.post(new WorldEvent.Save(this));
        return result;
    }

    // Pattern J (extension interface): new public accessor exposed via WorldServerExtension.
    // Callers cast WorldServer to WorldServerExtension to call getChunkSaveLocation().
    @Override
    public File getChunkSaveLocation() {
        return ((AnvilChunkLoader) theChunkProviderServer.currentChunkLoader).chunkSaveLocation;
    }

}