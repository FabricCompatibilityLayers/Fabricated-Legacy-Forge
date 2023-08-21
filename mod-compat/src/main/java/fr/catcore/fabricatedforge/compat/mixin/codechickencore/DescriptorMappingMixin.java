package fr.catcore.fabricatedforge.compat.mixin.codechickencore;

import codechicken.core.asm.ObfuscationMappings;
import fr.catcore.fabricatedforge.Constants;
import net.fabricmc.tinyremapper.extension.mixin.common.data.Pair;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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
            this.s_owner = Constants.getRemappedClassName(this.s_owner).replace(".", "/");
        }
        if (this.s_desc.startsWith("(")) {
            Pair<String, String> pair = Constants.getRemappedMethodName(this.s_owner, this.s_name, this.s_desc);
            this.s_name = pair.first();
            this.s_desc = Constants.remapMethodDescriptor(pair.second());
        } else {
            Pair<String, String> pair = Constants.getRemappedFieldName(this.s_owner, this.s_name, this.s_desc);
            this.s_name = pair.first();
            this.s_desc = Constants.remapIndividualType(pair.second());
        }
    }
}
