package fr.catcore.fabricatedforge.mixin.compat.invtweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = {"net/minecraft/InvTweaksLocalization"}, remap = false)
public class FixInvTweaksLocalization {

    @ModifyArg(method = "load(Ljava/lang/String;)Ljava/lang/String;", remap = false, at = @At(value = "INVOKE", target = "Ljava/lang/Class;getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;", remap = false), index = 0)
    private static String fixLangFilePath(String arg) {
        return "/" + arg;
    }
}
