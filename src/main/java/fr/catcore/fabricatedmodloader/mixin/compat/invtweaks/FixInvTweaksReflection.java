package fr.catcore.fabricatedmodloader.mixin.compat.invtweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = {"net/minecraft/InvTweaksObfuscation"}, remap = false)
public class FixInvTweaksReflection {

    @ModifyArg(method = "<clinit>", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/InvTweaksObfuscation;makeFieldPublic(Ljava/lang/Class;Ljava/lang/String;)V", remap = false), remap = false)
    private static String fixStaticR(String arg) {
        return arg.replace("b", "field_1386").replace("k", "field_1981");
    }

    @ModifyArg(method = "hasTexture", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/InvTweaksObfuscation;getThroughReflection(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", remap = false), remap = false)
    private String fixRK(String arg) {
        return "field_1981";
    }

    @ModifyArg(method = "getSlotNumber", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/InvTweaksObfuscation;getThroughReflection(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", remap = false), remap = false)
    private String fixRB(String arg) {
        return "field_1386";
    }
}
