package fr.catcore.fabricatedforge.compat.mixin.codechickencore;

import codechicken.core.asm.ObfuscationManager;
import fr.catcore.fabricatedforge.Constants;
import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ObfuscationManager.FieldMapping.class)
public class FieldMappingMixin {
    @Shadow(remap = false) public String owner;

    @Shadow(remap = false) public String name;

    @Shadow(remap = false) public String type;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void remap(String declaringclass, String fieldname, String type, CallbackInfo ci) {
        if (!this.owner.contains(".")) {
            this.owner = Constants.mapClass(this.owner);
        }
        MappingUtils.ClassMember pair = Constants.mapFieldFromRemappedClass(this.owner, this.name, this.type);
        this.name = pair.name;
        this.type = pair.desc;
    }
}
