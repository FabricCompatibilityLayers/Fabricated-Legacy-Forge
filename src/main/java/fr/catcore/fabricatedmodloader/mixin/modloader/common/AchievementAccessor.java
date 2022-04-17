package fr.catcore.fabricatedmodloader.mixin.modloader.common;

import net.minecraft.advancement.Achievement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Achievement.class)
public interface AchievementAccessor {

    @Accessor("translationKey")
    void setAchievementDescription(String description);
}
