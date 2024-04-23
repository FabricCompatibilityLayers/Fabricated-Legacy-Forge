package fr.catcore.fabricatedforge.compat.mixin.immibiscore;

import cpw.mods.fml.relauncher.IClassTransformer;
import cpw.mods.fml.relauncher.RelaunchClassLoader;
import fr.catcore.modremapperapi.ClassTransformer;
import immibis.core.ImmibisCore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ImmibisCore.class)
public class ImmibisCoreMixin {
    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/lang/Class;getClassLoader()Ljava/lang/ClassLoader;"), remap = false)
    private static ClassLoader provideFakeClassLoader(Class instance) {
        return null;
    }

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/relauncher/RelaunchClassLoader;registerTransformer(Ljava/lang/String;)V"), remap = false)
    private static void registerClassLoader(RelaunchClassLoader instance, String s) {
        try {
            IClassTransformer classTransformer = (IClassTransformer)Class.forName(s).newInstance();
            ClassTransformer.registerPostTransformer(classTransformer);
            System.out.println("Registered ClassTransformer: " + s);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
