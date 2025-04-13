package fr.catcore.fabricatedforge.mixin.forgefml.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.SERVER)
@Mixin(Language.class)
public class Server_LanguageMixin {
    @Shadow
    public String code;

    public String method_636() {
        return this.code;
    }
}
