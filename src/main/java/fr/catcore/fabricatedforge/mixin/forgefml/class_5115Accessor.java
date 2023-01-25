package fr.catcore.fabricatedforge.mixin.forgefml;

import net.minecraft.class_5115;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(class_5115.class)
public interface class_5115Accessor {
    @Invoker("<init>")
    static class_5115 newInstance(ParticleManager particleManager, Particle particle) {
        return null;
    }
}
