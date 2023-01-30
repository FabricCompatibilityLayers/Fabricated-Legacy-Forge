package fr.catcore.fabricatedforge.mixin.forgefml.world.biome.layer;

import net.minecraft.world.biome.layer.*;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Layer.class)
public class LayerMixin {
    /**
     * @author forge
     * @reason hooks
     */
    @Overwrite
    public static Layer[] method_146(long par0, LevelGeneratorType par2WorldType) {
        class_74 var3 = new class_74(1L);
        class_71 var9 = new class_71(2000L, var3);
        class_67 var10 = new class_67(1L, var9);
        class_84 var11 = new class_84(2001L, var10);
        var10 = new class_67(2L, var11);
        class_69 var12 = new class_69(2L, var10);
        var11 = new class_84(2002L, var12);
        var10 = new class_67(3L, var11);
        var11 = new class_84(2003L, var10);
        var10 = new class_67(4L, var11);
        MushroomIslandLayer var16 = new MushroomIslandLayer(5L, var10);
        byte var4 = 4;
        if (par2WorldType == LevelGeneratorType.LARGE_BIOMES) {
            var4 = 6;
        }

        var4 = getModdedBiomeSize(par2WorldType, var4);
        Layer var5 = class_84.method_148(1000L, var16, 0);
        class_77 var13 = new class_77(100L, var5);
        var5 = class_84.method_148(1000L, var13, var4 + 2);
        AddRiverLayer var14 = new AddRiverLayer(1L, var5);
        class_81 var15 = new class_81(1000L, var14);
        Layer var6 = class_84.method_148(1000L, var16, 0);
        SetBaseBiomesLayer var17 = new SetBaseBiomesLayer(200L, var6, par2WorldType);
        var6 = class_84.method_148(1000L, var17, 2);
        Object var18 = new AddHillsLayer(1000L, var6);

        for(int var7 = 0; var7 < var4; ++var7) {
            var18 = new class_84((long)(1000 + var7), (Layer)var18);
            if (var7 == 0) {
                var18 = new class_67(3L, (Layer)var18);
            }

            if (var7 == 1) {
                var18 = new class_80(1000L, (Layer)var18);
            }

            if (var7 == 1) {
                var18 = new class_82(1000L, (Layer)var18);
            }
        }

        class_81 var19 = new class_81(1000L, (Layer)var18);
        class_79 var20 = new class_79(100L, var19, var15);
        class_83 var8 = new class_83(10L, var20);
        var20.method_144(par0);
        var8.method_144(par0);
        return new Layer[]{var20, var8, var20};
    }

    private static byte getModdedBiomeSize(LevelGeneratorType worldType, byte original) {
        WorldTypeEvent.BiomeSize event = new WorldTypeEvent.BiomeSize(worldType, original);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.newSize;
    }
}
