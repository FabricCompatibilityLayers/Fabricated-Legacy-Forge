package fr.catcore.fabricatedforge.mixin.forgefml.structure;

import fr.catcore.fabricatedforge.forged.reflection.ReflectedStrongholdStructure;
import net.minecraft.structure.StrongholdStructure;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(StrongholdStructure.class)
public class StrongholdStructureMixin {
    @Shadow private Biome[] field_18;

    @Inject(method = "<init>()V", at = @At("RETURN"))
    private void overrideBiomes(CallbackInfo ci) {
        this.field_18 = (Biome[]) ReflectedStrongholdStructure.allowedBiomes.toArray(new Biome[0]);
    }

    @Inject(method = "<init>(Ljava/util/Map;)V", at = @At(value = "INVOKE", remap = false, target = "Ljava/util/Map;entrySet()Ljava/util/Set;", ordinal = 0))
    private void overrideBiomes(Map map, CallbackInfo ci) {
        this.field_18 = (Biome[]) ReflectedStrongholdStructure.allowedBiomes.toArray(new Biome[0]);
    }
}
