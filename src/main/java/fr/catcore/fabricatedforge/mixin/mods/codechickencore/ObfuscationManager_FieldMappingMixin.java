package fr.catcore.fabricatedforge.mixin.mods.codechickencore;

import codechicken.core.asm.ObfuscationManager;
import fr.catcore.fabricatedforge.Constants;
import net.fabricmc.tinyremapper.extension.mixin.common.data.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ObfuscationManager.FieldMapping.class)
public class ObfuscationManager_FieldMappingMixin {
    @Shadow(remap = false) public String owner;

    @Shadow(remap = false) public String name;

    @Shadow(remap = false) public String type;

    static {
        System.out.println("applied");
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void remap(CallbackInfo ci) {
        this.owner = Constants.getRemappedClassName(this.owner);
        Pair<String, String> pair = Constants.getRemappedFieldName(this.owner, this.name, this.type);
        this.name = pair.first();
        this.type = pair.second();
    }
}
