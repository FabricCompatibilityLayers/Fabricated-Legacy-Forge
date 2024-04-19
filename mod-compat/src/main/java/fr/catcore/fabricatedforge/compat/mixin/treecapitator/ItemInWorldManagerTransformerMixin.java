package fr.catcore.fabricatedforge.compat.mixin.treecapitator;

import bspkrs.treecapitator.fml.asm.ItemInWorldManagerTransformer;
import fr.catcore.fabricatedforge.Constants;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(ItemInWorldManagerTransformer.class)
public class ItemInWorldManagerTransformerMixin {
    @Shadow @Final private HashMap obfStrings;

    @Shadow @Final private String targetMethodDesc;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void flf$remapStrings(CallbackInfo ci) {
        String className = Constants.mapClass("ir");
        String blockClass = Constants.mapClass("amq");
        String worldClass = Constants.mapClass("yc");
        String playerClass = Constants.mapClass("qx");
        String playerMPClass = Constants.mapClass("iq");
        this.obfStrings.put("className", className.replace("/", "."));
        this.obfStrings.put("javaClassName", className);
        this.obfStrings.put("targetMethodName", Constants.mapMethodFromRemappedClass(className, "d", targetMethodDesc).name);
        this.obfStrings.put("worldFieldName", Constants.mapFieldFromRemappedClass(className, "a", null).name);
        this.obfStrings.put("entityPlayerFieldName", Constants.mapFieldFromRemappedClass(className, "b", null).name);
        this.obfStrings.put("worldJavaClassName", worldClass);
        this.obfStrings.put("getBlockMetadataMethodName", Constants.mapMethodFromRemappedClass(worldClass, "h", "(III)I").name);
        this.obfStrings.put("blockJavaClassName", blockClass);
        this.obfStrings.put("blocksListFieldName", Constants.mapFieldFromRemappedClass(blockClass, "p", null).name);
        this.obfStrings.put("entityPlayerJavaClassName", playerClass);
        this.obfStrings.put("entityPlayerMPJavaClassName", playerMPClass);
    }
}
