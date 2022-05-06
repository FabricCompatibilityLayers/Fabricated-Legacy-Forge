package fr.catcore.fabricatedforge.mixin.modloader.common;

import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Stats.class)
public interface StatsAccessor {

    @Accessor("ID_TO_STAT")
    static Map getIdToStat() {
        return null;
    }
}
