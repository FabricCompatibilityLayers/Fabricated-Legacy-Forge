package fr.catcore.fabricatedforge.mixin.forgefml.server;

import net.minecraft.server.PlayerWorldManager;
import net.minecraft.server.class_793;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_793.class)
public abstract class class_793Mixin {

    @Inject(method = "<init>", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerWorldManager;getWorld()Lnet/minecraft/server/world/ServerWorld;"))
    private void fmlCtr(PlayerWorldManager par1PlayerManager, int par2, int par3, CallbackInfo ci) {
        par1PlayerManager.getWorld().chunkCache.method_3871(par2, par3);

        ci.cancel();
    }

    @ModifyArg(method = "method_2117", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;method_2134(IIIIII)Ljava/util/List;"))
    private int fml15_1(int i) {
        return i - 1;
    }

    @ModifyArg(method = "method_2117", index = 5, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;method_2134(IIIIII)Ljava/util/List;"))
    private int fml15_2(int i) {
        return i - 1;
    }
}
