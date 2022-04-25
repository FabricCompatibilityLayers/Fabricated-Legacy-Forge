package fr.catcore.fabricatedmodloader.mixin.modloader.common;

import modloader.ModLoader;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReport.class)
public abstract class CrashReportMixin {

    @Shadow
    @Final
    private CrashReportSection systemDetailsSection;

    @Inject(method = "fillSystemDetails", at = @At("RETURN"))
    private void addModLoaderSection(CallbackInfo ci) {
        this.systemDetailsSection.add("ModLoader", ModLoader.getCrashReport());
    }
}
