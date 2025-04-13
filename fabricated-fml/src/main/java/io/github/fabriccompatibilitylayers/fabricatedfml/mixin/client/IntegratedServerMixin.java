package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin extends MinecraftServer {
    public IntegratedServerMixin(File p_i3375_1_) {
        super(p_i3375_1_);
    }

    @Inject(method = "func_71197_b", at = @At("RETURN"))
    private void fml$handleServerStarting(CallbackInfoReturnable<Boolean> cir) {
        FMLCommonHandler.instance().handleServerStarting(this);
    }
}
