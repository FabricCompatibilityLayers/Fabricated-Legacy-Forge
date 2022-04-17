package fr.catcore.fabricatedmodloader.mixin.modloader.common;

import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Properties;

@Mixin(Language.class)
public interface LanguageAccessor {

    @Accessor("_translations")
    Properties getTranslationMap();
}
