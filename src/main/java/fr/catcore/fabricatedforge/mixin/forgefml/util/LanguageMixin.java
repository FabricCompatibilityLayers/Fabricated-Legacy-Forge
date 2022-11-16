package fr.catcore.fabricatedforge.mixin.forgefml.util;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Properties;

@Mixin(Language.class)
public class LanguageMixin {
    @Inject(method = "method_633", at = @At("RETURN"))
    private void fmlLoadLanguageTable(Properties par1Properties, String par2Str, CallbackInfo ci) {
        LanguageRegistry.instance().loadLanguageTable(par1Properties, par2Str);
    }
}
