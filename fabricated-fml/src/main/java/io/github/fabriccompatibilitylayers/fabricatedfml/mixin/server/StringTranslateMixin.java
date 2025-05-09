package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.server;

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
    // Legacy Fabric intermediary 1
    public String method_636() {
        return this.field_74813_d;
    }

    // Ornithe gen 1
    public String m_2823481() {
        return this.field_74813_d;
    }

    // searge
    public String func_74811_c() {
        return this.field_74813_d;
    }
}
