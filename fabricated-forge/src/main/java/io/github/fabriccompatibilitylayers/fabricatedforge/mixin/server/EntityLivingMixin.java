package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;

@Environment(EnvType.SERVER)
@Mixin(EntityLiving.class)
public class EntityLivingMixin {
    @Shadow protected HashMap activePotionsMap;

    // Readd this client-only method to the server
    // mcp
    public void removePotionEffect(int par1) {
        this.activePotionsMap.remove(par1);
    }

    // legacy fabric 1
    public void method_2674(int par1) {
        removePotionEffect(par1);
    }

    // ornithe gen 1
    public void m_3848394(int par1) {
        removePotionEffect(par1);
    }
}
