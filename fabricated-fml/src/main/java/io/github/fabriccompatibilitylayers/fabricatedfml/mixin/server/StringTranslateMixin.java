package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.server;

import io.github.fabriccompatibilitylayers.fabricatedfml.utils.ServerImplementation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.StringTranslate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.SERVER)
@Mixin(StringTranslate.class)
public class StringTranslateMixin {
    @Shadow public String field_74813_d;

    // Readd this client-only method to the server
    @ServerImplementation("c")
    public String func_74811_c() {
        return this.field_74813_d;
    }
}
