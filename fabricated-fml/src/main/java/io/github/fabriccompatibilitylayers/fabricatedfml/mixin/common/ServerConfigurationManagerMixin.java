package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.ServerConfigurationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerConfigurationManager.class)
public class ServerConfigurationManagerMixin {
    @Inject(method = "func_72355_a", at = @At("RETURN"))
    private void fml$handlePlayerLogin(NetworkManager p_72355_1_, EntityPlayerMP p_72355_2_, CallbackInfo ci, @Local NetServerHandler var6) {
        FMLNetworkHandler.handlePlayerLogin(p_72355_2_, var6, p_72355_1_);
    }

    @Inject(method = "func_72367_e", at = @At("HEAD"))
    private void fml$onPlayerLogout(EntityPlayerMP p_72367_1_, CallbackInfo ci) {
        GameRegistry.onPlayerLogout(p_72367_1_);
    }

    @ModifyReturnValue(method = "func_72368_a", at = @At("RETURN"))
    private EntityPlayerMP fml$onPlayerRespawn(EntityPlayerMP original) {
        GameRegistry.onPlayerRespawn(original);
        return original;
    }

    @Inject(method = "func_72356_a", at = @At("RETURN"))
    private void fml$onPlayerChangedDimension(EntityPlayerMP p_72356_1_, int p_72356_2_, CallbackInfo ci) {
        GameRegistry.onPlayerChangedDimension(p_72356_1_);
    }
}
