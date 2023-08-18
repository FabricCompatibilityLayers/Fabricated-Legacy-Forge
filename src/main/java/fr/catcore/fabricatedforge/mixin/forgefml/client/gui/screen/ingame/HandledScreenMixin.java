package fr.catcore.fabricatedforge.mixin.forgefml.client.gui.screen.ingame;

import com.llamalad7.mixinextras.sugar.Local;
import fr.catcore.fabricatedforge.mixininterface.ISlot;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HandledScreen.class)
public class HandledScreenMixin extends Screen {

    @ModifyArg(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/TextureManager;getTextureFromPath(Ljava/lang/String;)I"), index = 0)
    private String fixSlotIcon(String path, @Local Slot par1Slot) {
        return ((ISlot) par1Slot).getBackgroundIconTexture();
    }
}
