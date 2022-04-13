package fr.catcore.fabricatedforge.mixin.modloader.common;

import modloader.ModLoader;
import net.minecraft.util.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReport.class)
public abstract class CrashReportMixin {

    @Shadow public abstract void addSection(String name, Object section);

    @Inject(method = "fillSystemDetails", at = @At("RETURN"))
    private void addModLoaderSection(CallbackInfo ci) {
        this.addSection("ModLoader", ModLoader.getCrashReport());
    }
}
