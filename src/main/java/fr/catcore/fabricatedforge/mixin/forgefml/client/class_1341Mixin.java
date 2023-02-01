package fr.catcore.fabricatedforge.mixin.forgefml.client;

import net.minecraft.client.class_1341;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1341.class)
public class class_1341Mixin extends Particle {
    @Shadow private NbtList field_5162;

    protected class_1341Mixin(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtList;size()I", ordinal = 0))
    private void preventNull$ctr(
            World par1World,
            double par2,
            double par4,
            double par6,
            double par8,
            double par10,
            double par12,
            ParticleManager par14EffectRenderer,
            NbtCompound par15NBTTagCompound,
            CallbackInfo ci
    ) {
        if (this.field_5162 == null) {
            this.field_5162 = new NbtList();
        }
    }
}
