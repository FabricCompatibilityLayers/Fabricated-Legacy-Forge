package fr.catcore.fabricatedforge.mixin.forgefml.util.crash;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.util.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReport.class)
public class CrashReportMixin {

    @Inject(method = "fillSystemDetails", at = @At("RETURN"))
    private void fmlEnhanceCrashReport(CallbackInfo ci) {
        FMLCommonHandler.instance().enhanceCrashReport((CrashReport)(Object) this);
    }
}
