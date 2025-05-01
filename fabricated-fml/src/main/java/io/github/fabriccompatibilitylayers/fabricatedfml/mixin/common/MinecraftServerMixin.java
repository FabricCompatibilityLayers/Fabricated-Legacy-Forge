package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.relauncher.ArgsWrapper;
import cpw.mods.fml.relauncher.FMLRelauncher;
import fr.catcore.cursedmixinextensions.annotations.Public;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.DedicatedServer;
import net.minecraft.src.StatList;
import net.minecraft.src.ThreadDedicatedServer;
import net.minecraft.src.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow public WorldServer[] field_71305_c;

    @Shadow private boolean field_71317_u;

    @Shadow public static Logger field_71306_a;

    @Inject(method = "run", remap = false, at = @At(value = "INVOKE", target = "Ljava/lang/System;currentTimeMillis()J", ordinal = 0, remap = false))
    private void fml$handleServerStarted(CallbackInfo ci) {
        FMLCommonHandler.instance().handleServerStarted();
    }

    @Inject(method = "run", remap = false, at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/System;currentTimeMillis()J", ordinal = 0, remap = false))
    private void fml$onWorldLoadTick(CallbackInfo ci) {
        FMLCommonHandler.instance().onWorldLoadTick(field_71305_c);
    }

    @Inject(method = "run", remap = false, at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Thread;sleep(J)V", remap = false))
    private void fml$handleServerStopping(CallbackInfo ci) {
        if (!this.field_71317_u) {
            FMLCommonHandler.instance().handleServerStopping();
        }
    }

    @Inject(method = "func_71217_p", at = @At("HEAD"))
    private void fml$rescheduleTicks(CallbackInfo ci) {
        FMLCommonHandler.instance().rescheduleTicks(Side.SERVER);
    }

    @Inject(method = "func_71217_p", at = @At(value = "FIELD", target = "Lnet/minecraft/server/MinecraftServer;field_71315_w:I", ordinal = 0))
    private void fml$onPreServerTick(CallbackInfo ci) {
        FMLCommonHandler.instance().onPreServerTick();
    }

    @Inject(method = "func_71217_p", at = @At("RETURN"))
    private void fml$onPostServerTick(CallbackInfo ci) {
        FMLCommonHandler.instance().onPostServerTick();
    }

    @Inject(method = "func_71190_q", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/WorldServer;func_72835_b()V"))
    private void fml$onPreWorldTick(CallbackInfo ci, @Local WorldServer var4) {
        FMLCommonHandler.instance().onPreWorldTick(var4);
    }

    @Inject(method = "func_71190_q", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Profiler;func_76318_c(Ljava/lang/String;)V", ordinal = 0))
    private void fml$onPostWorldTick(CallbackInfo ci, @Local WorldServer var4) {
        FMLCommonHandler.instance().onPostWorldTick(var4);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    @Environment(EnvType.SERVER)
    public static void main(String[] p_main_0_) {
        FMLRelauncher.handleServerRelaunch(new ArgsWrapper(p_main_0_));
    }

    @Public
    @Environment(EnvType.SERVER)
    private static void fmlReentry(ArgsWrapper wrap) {
        String[] p_main_0_ = wrap.args;

        StatList.func_75919_a();

        try {
            boolean var1 = !GraphicsEnvironment.isHeadless();
            String var2 = null;
            String var3 = ".";
            String var4 = null;
            boolean var5 = false;
            boolean var6 = false;
            int var7 = -1;

            for (int var8 = 0; var8 < p_main_0_.length; var8++) {
                String var9 = p_main_0_[var8];
                String var10 = var8 == p_main_0_.length - 1 ? null : p_main_0_[var8 + 1];
                boolean var11 = false;
                if (var9.equals("nogui") || var9.equals("--nogui")) {
                    var1 = false;
                } else if (var9.equals("--port") && var10 != null) {
                    var11 = true;

                    try {
                        var7 = Integer.parseInt(var10);
                    } catch (NumberFormatException var13) {
                    }
                } else if (var9.equals("--singleplayer") && var10 != null) {
                    var11 = true;
                    var2 = var10;
                } else if (var9.equals("--universe") && var10 != null) {
                    var11 = true;
                    var3 = var10;
                } else if (var9.equals("--world") && var10 != null) {
                    var11 = true;
                    var4 = var10;
                } else if (var9.equals("--demo")) {
                    var5 = true;
                } else if (var9.equals("--bonusChest")) {
                    var6 = true;
                }

                if (var11) {
                    var8++;
                }
            }

            DedicatedServer var15 = new DedicatedServer(new File(var3));
            if (var2 != null) {
                var15.func_71224_l(var2);
            }

            if (var4 != null) {
                var15.func_71261_m(var4);
            }

            if (var7 >= 0) {
                var15.func_71208_b(var7);
            }

            if (var5) {
                var15.func_71204_b(true);
            }

            if (var6) {
                var15.func_71194_c(true);
            }

            if (var1) {
                var15.func_79001_aj();
            }

            var15.func_71256_s();
            Runtime.getRuntime().addShutdownHook(new ThreadDedicatedServer(var15));
        } catch (Exception var14) {
            field_71306_a.log(Level.SEVERE, "Failed to start the minecraft server", (Throwable)var14);
        }
    }
}
