package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.FMLEntityPlayerExtension;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin implements FMLEntityPlayerExtension {
    @Inject(method = "func_70071_h_", at = @At("HEAD"))
    private void fml$onPlayerPreTick(CallbackInfo ci) {
        FMLCommonHandler.instance().onPlayerPreTick((EntityPlayer)(Object) this);
    }

    @Inject(method = "func_70071_h_", at = @At("RETURN"))
    private void fml$onPlayerPostTick(CallbackInfo ci) {
        FMLCommonHandler.instance().onPlayerPostTick((EntityPlayer)(Object) this);
    }

    @Override
    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z)
    {
        FMLNetworkHandler.openGui((EntityPlayer)(Object) this, mod, modGuiId, world, x, y, z);
    }
}
