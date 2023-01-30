package fr.catcore.fabricatedforge.mixin.forgefml.world.biome;

import fr.catcore.fabricatedforge.mixininterface.IBiome;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Biome.class)
public abstract class BiomeMixin implements IBiome {
    @Shadow public abstract float getTemperatureValue();

    @Shadow public abstract float getRainfall();

    @Shadow public int waterColor;

    /**
     * @author forge
     * @reason add modded
     */
    @Overwrite
    public BiomeDecorator createBiomePopulator() {
        return this.getModdedBiomeDecorator(new BiomeDecorator((Biome)(Object) this));
    }

    /**
     * @author forge
     * @reason add modded
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public int getGrassColor() {
        double var1 = (double) MathHelper.clamp(this.getTemperatureValue(), 0.0F, 1.0F);
        double var3 = (double)MathHelper.clamp(this.getRainfall(), 0.0F, 1.0F);
        return this.getModdedBiomeGrassColor(GrassColors.getColor(var1, var3));
    }

    /**
     * @author forge
     * @reason add modded
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public int getFoliageColor() {
        double var1 = (double)MathHelper.clamp(this.getTemperatureValue(), 0.0F, 1.0F);
        double var3 = (double)MathHelper.clamp(this.getRainfall(), 0.0F, 1.0F);
        return this.getModdedBiomeFoliageColor(FoliageColors.getColor(var1, var3));
    }

    @Override
    public BiomeDecorator getModdedBiomeDecorator(BiomeDecorator original) {
        BiomeEvent.CreateDecorator event = new BiomeEvent.CreateDecorator((Biome)(Object) this, original);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.newBiomeDecorator;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public int getWaterColorMultiplier() {
        BiomeEvent.GetWaterColor event = new BiomeEvent.GetWaterColor((Biome)(Object) this, this.waterColor);
        MinecraftForge.EVENT_BUS.post(event);
        return event.newColor;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public int getModdedBiomeGrassColor(int original) {
        BiomeEvent.GetGrassColor event = new BiomeEvent.GetGrassColor((Biome)(Object) this, original);
        MinecraftForge.EVENT_BUS.post(event);
        return event.newColor;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public int getModdedBiomeFoliageColor(int original) {
        BiomeEvent.GetFoliageColor event = new BiomeEvent.GetFoliageColor((Biome)(Object) this, original);
        MinecraftForge.EVENT_BUS.post(event);
        return event.newColor;
    }
}
