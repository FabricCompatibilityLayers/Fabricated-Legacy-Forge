package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import modloader.ModLoader;
import net.minecraft.client.class_1557;
import net.minecraft.client.texture.SpriteAtlasTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(SpriteAtlasTexture.class)
public class SpriteAtlasTextureMixin {

    @Shadow
    @Final
    private Map spritesToLoad;

    @Shadow
    @Final
    private int field_6014;

    @Inject(
            method = "method_5351",
            cancellable = true,
            at = @At("HEAD")
    )
    private void modLoaderGetCustomAnimationLogic(String string, CallbackInfoReturnable<class_1557> cir) {
        class_1557 sprite = (class_1557) this.spritesToLoad.get(string);

        if (sprite == null && this.field_6014 == 1) {
            sprite = ModLoader.getCustomAnimationLogic(string);

            if (sprite != null) {
                this.spritesToLoad.put(string, sprite);
                cir.setReturnValue(sprite);
            }
        }
    }
}
