package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.server;

import io.github.fabriccompatibilitylayers.fabricatedfml.utils.ServerImplementation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IBossDisplayData;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.SERVER)
@Mixin(IBossDisplayData.class)
public interface IBossDisplayDataMixin {
    @ServerImplementation("aS")
    int getMaxHealth();
    @ServerImplementation("b")
    int getDragonHealth();
    @ServerImplementation("an")
    String getEntityName();
}
