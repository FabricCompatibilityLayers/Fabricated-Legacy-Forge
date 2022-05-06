package fr.catcore.fabricatedforge.mixin.modloader.common;

import net.minecraft.stat.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Stat.class)
public interface StatAccessor {

    @Accessor("stringId")
    void setStringId(String stringId);
}
