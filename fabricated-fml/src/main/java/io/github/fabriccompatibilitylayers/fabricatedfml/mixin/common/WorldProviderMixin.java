package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.WorldTypeExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldProvider.class)
public class WorldProviderMixin {
    @Shadow public WorldChunkManager field_76578_c;

    @Shadow public WorldType field_76577_b;

    @Shadow public World field_76579_a;

    @Shadow public boolean field_76576_e;

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected void func_76572_b()
    {
        this.field_76578_c = ((WorldTypeExtension) this.field_76577_b).getChunkManager(this.field_76579_a);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public IChunkProvider func_76555_c()
    {
        return ((WorldTypeExtension) this.field_76577_b).getChunkGenerator(this.field_76579_a);
    }

    @ModifyReturnValue(method = "func_76557_i", at = @At("RETURN"))
    private int fml$getMinimumSpawnHeight(int original) {
        if (original == 4 || original == 64) {
            return ((WorldTypeExtension) this.field_76577_b).getMinimumSpawnHeight(this.field_76579_a);
        }

        return original;
    }

    /**
     * @author
     * @reason
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public boolean func_76564_j()
    {
        return ((WorldTypeExtension) this.field_76577_b).hasVoidParticles(this.field_76576_e);
    }

    @Environment(EnvType.CLIENT)
    @ModifyReturnValue(method = "func_76565_k", at = @At("RETURN"))
    private double fml$voidFadeMagnitude(double original) {
        if (original == 0.0D || original == 0.03125D) {
            return ((WorldTypeExtension) this.field_76577_b).voidFadeMagnitude();
        }

        return original;
    }
}
