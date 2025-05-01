package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.relauncher.ArgsWrapper;
import cpw.mods.fml.relauncher.FMLRelauncher;
import fr.catcore.cursedmixinextensions.annotations.Public;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.src.GameWindowListener;
import net.minecraft.src.MinecraftFakeLauncher;
import net.minecraft.src.ThreadShutdown;
import net.minecraft.src.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow private Timer field_71428_T;

    @Shadow
    public static long func_71386_F() {
        return 0;
    }

    @Inject(method = "func_71384_a", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;field_74363_ab:Ljava/lang/String;", ordinal = 0))
    private void fml$beginMinecraftLoading(CallbackInfo ci) {
        FMLClientHandler.instance().beginMinecraftLoading((Minecraft) (Object) this);
    }

    @Inject(method = "func_71384_a", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;field_71452_i:Lnet/minecraft/src/EffectRenderer;", shift = At.Shift.AFTER))
    private void fml$finishMinecraftLoading(CallbackInfo ci) {
        FMLClientHandler.instance().finishMinecraftLoading();
    }

    @Inject(method = "func_71384_a", at = @At("RETURN"))
    private void fml$onInitializationComplete(CallbackInfo ci) {
        FMLClientHandler.instance().onInitializationComplete();
    }

    @Inject(method = "func_71411_J", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Profiler;func_76318_c(Ljava/lang/String;)V", ordinal = 3))
    private void fml$onRenderTickStart(CallbackInfo ci) {
        FMLCommonHandler.instance().onRenderTickStart(this.field_71428_T.field_74281_c);
    }

    @Inject(method = "func_71411_J", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/src/Profiler;func_76319_b()V", ordinal = 2))
    private void fml$onRenderTickEnd(CallbackInfo ci) {
        FMLCommonHandler.instance().onRenderTickEnd(this.field_71428_T.field_74281_c);
    }

    @Inject(method = "func_71407_l", at = @At("HEAD"))
    private void fml$rescheduleTicks(CallbackInfo ci) {
        FMLCommonHandler.instance().rescheduleTicks(Side.CLIENT);
    }

    @Inject(method = "func_71407_l", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Profiler;func_76320_a(Ljava/lang/String;)V"))
    private void fml$onPreClientTick(CallbackInfo ci) {
        FMLCommonHandler.instance().onPreClientTick();
    }

    @Inject(method = "func_71407_l", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Profiler;func_76319_b()V"))
    private void fml$onPostClientTick(CallbackInfo ci) {
        FMLCommonHandler.instance().onPostClientTick();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void main(String[] p_main_0_)
    {
        FMLRelauncher.handleClientRelaunch(new ArgsWrapper(p_main_0_));
    }

    @Public
    private static void fmlReentry(ArgsWrapper wrapper) {
        String[] p_main_0_ = wrapper.args;

        HashMap var1 = new HashMap();
        boolean var2 = false;
        boolean var3 = true;
        boolean var4 = false;
        String var5 = "Player" + func_71386_F() % 1000L;
        if (p_main_0_.length > 0) {
            var5 = p_main_0_[0];
        }

        String var6 = "-";
        if (p_main_0_.length > 1) {
            var6 = p_main_0_[1];
        }

        for (int var7 = 2; var7 < p_main_0_.length; var7++) {
            String var8 = p_main_0_[var7];
            if (var7 == p_main_0_.length - 1) {
                Object var10000 = null;
            } else {
                String var14 = p_main_0_[var7 + 1];
            }

            boolean var10 = false;
            if (var8.equals("-demo") || var8.equals("--demo")) {
                var2 = true;
            } else if (var8.equals("--applet")) {
                var3 = false;
            }

            if (var10) {
                var7++;
            }
        }

        var1.put("demo", "" + var2);
        var1.put("stand-alone", "" + var3);
        var1.put("username", var5);
        var1.put("fullscreen", "" + var4);
        var1.put("sessionid", var6);
        Frame var11 = new Frame();
        var11.setTitle("Minecraft");
        var11.setBackground(Color.BLACK);
        JPanel var12 = new JPanel();
        var11.setLayout(new BorderLayout());
        var12.setPreferredSize(new Dimension(854, 480));
        var11.add(var12, "Center");
        var11.pack();
        var11.setLocationRelativeTo(null);
        var11.setVisible(true);
        var11.addWindowListener(new GameWindowListener());
        MinecraftFakeLauncher var9 = new MinecraftFakeLauncher(var1);
        MinecraftApplet var13 = new MinecraftApplet();
        var13.setStub(var9);
        var9.setLayout(new BorderLayout());
        var9.add(var13, "Center");
        var9.validate();
        var11.removeAll();
        var11.setLayout(new BorderLayout());
        var11.add(var9, "Center");
        var11.validate();
        var13.init();
        var13.start();
        Runtime.getRuntime().addShutdownHook(new ThreadShutdown());
    }
}
