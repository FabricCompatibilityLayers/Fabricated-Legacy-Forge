package fr.catcore.fabricatedforge.compat.mixin.codechickencore;

import codechicken.core.asm.ObfuscationManager;
import fr.catcore.fabricatedforge.Constants;
import fr.catcore.fabricatedforge.compat.nei.NEIFixer;
import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;
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
        if (NEIFixer.FIX_CLASSES.containsKey(this.owner)) {
            this.owner = NEIFixer.FIX_CLASSES.get(this.owner);
        } else if (!this.owner.contains(".")) {
            this.owner = Constants.mapClass(this.owner);
        }

        MappingUtils.ClassMember pair = Constants.mapMethodFromRemappedClass(this.owner, this.name, this.desc);

        if (NEIFixer.FIX_METHOD_NAMES.containsKey(this.name)) {
            this.name = NEIFixer.FIX_METHOD_NAMES.get(this.name);
        } else {
            this.name = pair.name;
        }

        if (NEIFixer.FIX_METHOD_ARGS.containsKey(this.desc)) {
            this.desc = NEIFixer.FIX_METHOD_ARGS.get(this.desc);
        } else {
            this.desc = Constants.mapDescriptor(pair.desc);
        }
    }
}
