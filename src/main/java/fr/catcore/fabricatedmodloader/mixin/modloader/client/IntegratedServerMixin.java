package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import modloader.ModLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.level.LevelInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {

    @Inject(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/client/class_604", ordinal = 0))
    private void modLoaderRegisterCommands(Minecraft string, String string2, String levelInfo, LevelInfo par4, CallbackInfo ci) {
        ModLoader.registerCommands((IntegratedServer) (Object) this);
    }
}
