package fr.catcore.fabricatedforge.mixin.forgefml.world.dimension;

import fr.catcore.fabricatedforge.mixininterface.IDimension;
import fr.catcore.fabricatedforge.mixininterface.ILevelGeneratorType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.LayeredBiomeSource;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.TheEndDimension;
import net.minecraft.world.dimension.TheNetherDimension;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelProperties;
import net.minecraftforge.client.SkyProvider;
import net.minecraftforge.common.DimensionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Dimension.class)
public class DimensionMixin implements IDimension {

    @Shadow public LayeredBiomeSource biomeSource;
    @Shadow public LevelGeneratorType generatorType;
    @Shadow public World world;


    @Shadow public boolean isNether;
    @Shadow public int dimensionType;
    @Shadow public String generatorOptions;

    private SkyProvider skyProvider = null;
    private SkyProvider cloudRenderer = null;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void init() {
        this.biomeSource = ((ILevelGeneratorType)this.generatorType).getChunkManager(this.world);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public ChunkProvider createChunkGenerator() {
        return ((ILevelGeneratorType)this.generatorType).getChunkGenerator(this.world, this.generatorOptions);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public static Dimension getById(int par0) {
        return DimensionManager.createProviderFor(par0);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public int getAverageYLevel() {
        return ((ILevelGeneratorType)this.generatorType).getMinimumSpawnHeight(this.world);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public boolean hasVoidFog() {
        return ((ILevelGeneratorType)this.generatorType).hasVoidParticles(this.isNether);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public double method_3994() {
        return ((ILevelGeneratorType)this.generatorType).voidFadeMagnitude();
    }

    @Override
    public void setDimension(int dim) {
        this.dimensionType = dim;
    }

    @Override
    public String getSaveFolder() {
        return this.dimensionType == 0 ? null : "DIM" + this.dimensionType;
    }

    @Override
    public String getWelcomeMessage() {
        if ((Object)this instanceof TheEndDimension) {
            return "Entering the End";
        } else {
            return (Object)this instanceof TheNetherDimension ? "Entering the Nether" : null;
        }
    }

    @Override
    public String getDepartMessage() {
        if ((Object)this instanceof TheEndDimension) {
            return "Leaving the End";
        } else {
            return (Object)this instanceof TheNetherDimension ? "Leaving the Nether" : null;
        }
    }

    @Override
    public double getMovementFactor() {
        return (Object)this instanceof TheNetherDimension ? 8.0 : 1.0;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public SkyProvider getSkyProvider() {
        return this.skyProvider;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void setSkyProvider(SkyProvider skyProvider) {
        this.skyProvider = skyProvider;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public SkyProvider getCloudRenderer() {
        return this.cloudRenderer;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void setCloudRenderer(SkyProvider renderer) {
        this.cloudRenderer = renderer;
    }

    @Override
    public BlockPos getRandomizedSpawnPoint() {
        BlockPos var5 = new BlockPos(this.world.getWorldSpawnPos());
        boolean isAdventure = this.world.getLevelProperties().getGamemode() == GameMode.ADVENTURE;
        int spawnFuzz = ((ILevelGeneratorType)this.generatorType).getSpawnFuzz();
        int spawnFuzzHalf = spawnFuzz / 2;
        if (!this.isNether && !isAdventure) {
            var5.x += this.world.random.nextInt(spawnFuzz) - spawnFuzzHalf;
            var5.z += this.world.random.nextInt(spawnFuzz) - spawnFuzzHalf;
            var5.y = this.world.method_3708(var5.x, var5.z);
        }

        return var5;
    }

    @Override
    public boolean shouldMapSpin(String entity, double x, double y, double z) {
        return this.dimensionType < 0;
    }

    @Override
    public int getRespawnDimension(ServerPlayerEntity player) {
        return 0;
    }

    @Override
    public Biome getBiomeGenForCoords(int x, int z) {
        return this.world.getBiomeGenForCoordsBody(x, z);
    }

    @Override
    public boolean isDaytime() {
        return this.world.ambientDarkness < 4;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
        return this.world.getSkyColorBody(cameraEntity, partialTicks);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Vec3d drawClouds(float partialTicks) {
        return this.world.drawCloudsBody(partialTicks);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public float getStarBrightness(float par1) {
        return this.world.getStarBrightnessBody(par1);
    }

    @Override
    public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful) {
        this.world.setSpawnAnimals(allowHostile);
        this.world.setSpawnMonsters(allowPeaceful);
    }

    @Override
    public void calculateInitialWeather() {
        this.world.calculateInitialWeatherBody();
    }

    @Override
    public void updateWeather() {
        this.world.updateWeatherBody();
    }

    @Override
    public void toggleRain() {
        this.world.getLevelProperties().setRainTime(1);
    }

    @Override
    public boolean canBlockFreeze(int x, int y, int z, boolean byWater) {
        return this.world.canBlockFreezeBody(x, y, z, byWater);
    }

    @Override
    public boolean canSnowAt(int x, int y, int z) {
        return this.world.canSnowAtBody(x, y, z);
    }

    @Override
    public void setWorldTime(long time) {
        this.world.getLevelProperties().setDayTime(time);
    }

    @Override
    public long getSeed() {
        return this.world.getLevelProperties().getSeed();
    }

    @Override
    public long getWorldTime() {
        return this.world.getLevelProperties().getTimeOfDay();
    }

    @Override
    public BlockPos getSpawnPoint() {
        LevelProperties info = this.world.getLevelProperties();
        return new BlockPos(info.getSpawnX(), info.getSpawnY(), info.getSpawnZ());
    }

    @Override
    public void setSpawnPoint(int x, int y, int z) {
        this.world.getLevelProperties().setSpawnPos(x, y, z);
    }

    @Override
    public boolean canMineBlock(PlayerEntity player, int x, int y, int z) {
        return this.world.canMineBlockBody(player, x, y, z);
    }

    @Override
    public boolean isBlockHighHumidity(int x, int y, int z) {
        return this.world.getBiome(x, z).hasHighHumidity();
    }

    @Override
    public int getHeight() {
        return 256;
    }

    @Override
    public int getActualHeight() {
        return this.isNether ? 128 : 256;
    }

    @Override
    public double getHorizon() {
        return ((ILevelGeneratorType)this.world.getLevelProperties().getGeneratorType()).getHorizon(this.world);
    }

    @Override
    public void resetRainAndThunder() {
        this.world.getLevelProperties().setRainTime(0);
        this.world.getLevelProperties().setRaining(false);
        this.world.getLevelProperties().setThunderTime(0);
        this.world.getLevelProperties().setThundering(false);
    }

    @Override
    public boolean canDoLightning(Chunk chunk) {
        return true;
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk) {
        return true;
    }
}
