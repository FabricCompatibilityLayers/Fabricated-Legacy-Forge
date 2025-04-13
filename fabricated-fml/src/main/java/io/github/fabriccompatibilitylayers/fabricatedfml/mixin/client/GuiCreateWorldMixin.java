package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiCreateWorld;
import net.minecraft.src.WorldType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiCreateWorld.class)
public class GuiCreateWorldMixin {
    @Shadow private int field_73916_E;

    @Inject(method = "func_73875_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EnumGameType;func_77142_a(Ljava/lang/String;)Lnet/minecraft/src/EnumGameType;"))
    private void fml$onGUICreateWorldPress(GuiButton p_73875_1_, CallbackInfo ci) {
        WorldType.field_77139_a[this.field_73916_E].onGUICreateWorldPress();
    }
}
