package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.hit.HitResult;

public interface IParticleManager {

    public void addEffect(Particle effect, Object obj);

    public void addBlockHitEffects(int x, int y, int z, HitResult target);
}
