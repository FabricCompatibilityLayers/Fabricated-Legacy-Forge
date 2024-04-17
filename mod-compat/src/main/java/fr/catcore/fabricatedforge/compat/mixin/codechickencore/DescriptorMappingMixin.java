package fr.catcore.fabricatedforge.compat.mixin.codechickencore;

import codechicken.core.asm.ObfuscationMappings;
import fr.catcore.fabricatedforge.Constants;
import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ObfuscationMappings.DescriptorMapping.class)
public class DescriptorMappingMixin {

    @Shadow(remap = false) public String s_owner;

    @Shadow(remap = false) public String s_name;

    @Shadow(remap = false) public String s_desc;

    @Inject(method = "<init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", remap = false, at = @At("RETURN"))
    private void remap(String declaringclass, String methodname, String descriptor, CallbackInfo ci) {
        this.remap();
    }

    @Inject(method = "<init>(Ljava/lang/String;Lcodechicken/core/asm/ObfuscationMappings$DescriptorMapping;)V", remap = false, at = @At("RETURN"))
    private void remap(String owner, ObfuscationMappings.DescriptorMapping descmap, CallbackInfo ci) {
        this.remap();
    }

    @Unique
    private void remap() {
        if (!this.s_owner.contains("/")) {
            this.s_owner = Constants.mapClass(this.s_owner);
        }
        if (this.s_desc.startsWith("(")) {
            MappingUtils.ClassMember pair = Constants.mapMethodFromRemappedClass(this.s_owner, this.s_name, this.s_desc);
            this.s_name = pair.name;
            this.s_desc = Constants.mapMethodDescriptor(pair.desc);
        } else {
            MappingUtils.ClassMember pair = Constants.mapFieldFromRemappedClass(this.s_owner, this.s_name, this.s_desc);
            this.s_name = pair.name;
            this.s_desc = Constants.mapTypeDescriptor(pair.desc);
        }
    }
}
