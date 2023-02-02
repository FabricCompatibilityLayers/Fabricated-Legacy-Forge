package fr.catcore.fabricatedforge.mixin.forgefml.client;

import com.mojang.blaze3d.platform.GLX;
import fr.catcore.modremapperapi.api.mixin.NewConstructor;
import fr.catcore.modremapperapi.api.mixin.Public;
import fr.catcore.modremapperapi.api.mixin.SuperConstructor;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.GL13;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GLX.class)
public class GLXMixin {
    @Shadow private static boolean arbMultitexture;
    @Shadow public static int lightmapTextureUnit;
    @Public
    private static float lastBrightnessX = 0.0F;
    @Public
    private static float lastBrightnessY = 0.0F;

    @SuperConstructor
    public void obj$ctr() {}

    @NewConstructor
    public void ctr() {
        obj$ctr();
    }

    /**
     * @author forge
     * @reason set constants
     */
    @Overwrite
    public static void gl13MultiTexCoord2f(int par0, float par1, float par2) {
        if (arbMultitexture) {
            ARBMultitexture.glMultiTexCoord2fARB(par0, par1, par2);
        } else {
            GL13.glMultiTexCoord2f(par0, par1, par2);
        }

        if (par0 == lightmapTextureUnit) {
            lastBrightnessX = par1;
            lastBrightnessY = par2;
        }
    }
}
