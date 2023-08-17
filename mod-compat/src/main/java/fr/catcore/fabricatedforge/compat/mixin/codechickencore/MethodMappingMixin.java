package fr.catcore.fabricatedforge.compat.mixin.codechickencore;

import codechicken.core.asm.ObfuscationManager;
import fr.catcore.fabricatedforge.Constants;
import net.fabricmc.tinyremapper.extension.mixin.common.data.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ObfuscationManager.MethodMapping.class)
public class MethodMappingMixin {
    @Shadow(remap = false) public String owner;

    @Shadow(remap = false) public String name;

    @Shadow(remap = false) public String desc;

    @Inject(method = "<init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", remap = false, at = @At("RETURN"))
    private void remap(String declaringclass, String methodname, String descriptor, CallbackInfo ci) {
        this.remap();
    }

    @Inject(method = "<init>(Ljava/lang/String;Lcodechicken/core/asm/ObfuscationManager$MethodMapping;)V", remap = false, at = @At("RETURN"))
    private void remap(String declaringclass, ObfuscationManager.MethodMapping methodmap, CallbackInfo ci) {
        this.remap();
    }

    @Unique
    private void remap() {
        if (!this.owner.contains(".")) {
            this.owner = Constants.getRemappedClassName(this.owner);
        }
        Pair<String, String> pair = Constants.getRemappedMethodName(this.owner, this.name, this.desc);
        this.name = pair.first();
        this.desc = pair.second();
    }
}
