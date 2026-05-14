package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiIngame;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiIngame.class)
public class GuiIngameMixin {

    // Pattern N (@Redirect): replaces the getTotalArmorValue() call-site — single,
    // non-composable change; @Redirect is simpler than @WrapOperation here.
    @Redirect(method = "renderGameOverlay", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/src/EntityClientPlayerMP;getTotalArmorValue()I"))
    private int forge$getTotalArmorValue(EntityClientPlayerMP instance) {
        return ForgeHooks.getTotalArmorValue(instance);
    }
}