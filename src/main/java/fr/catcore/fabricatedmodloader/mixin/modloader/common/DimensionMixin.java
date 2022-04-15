package fr.catcore.fabricatedmodloader.mixin.modloader.common;

import fr.catcore.fabricatedmodloader.mixininterface.ILevelGeneratorType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.LayeredBiomeSource;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Dimension.class)
public class DimensionMixin {

    @Shadow public LayeredBiomeSource biomeSource;

    @Shadow public LevelGeneratorType generatorType;

    @Shadow public World world;

    @Shadow public boolean isNether;

    /**
     * @author
     */
    @Overwrite
    public void init() {
        this.biomeSource = ((ILevelGeneratorType)this.generatorType).getChunkManager(this.world);
    }

    /**
     * @author
     */
    @Overwrite
    public ChunkProvider createChunkGenerator() {
        return ((ILevelGeneratorType)this.generatorType).getChunkGenerator(this.world);
    }

    /**
     * @author
     */
    @Overwrite
    public int getAverageYLevel() {
        return ((ILevelGeneratorType)this.generatorType).getSeaLevel(this.world);
    }

    /**
     * @author
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public boolean method_3993() {
        return ((ILevelGeneratorType)this.generatorType).hasVoidParticles(this.isNether);
    }

    /**
     * @author
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public double method_3994() {
        return ((ILevelGeneratorType)this.generatorType).voidFadeMagnitude();
    }
}
