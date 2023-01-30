package fr.catcore.fabricatedforge.mixin.forgefml.util.crash.provider;

import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.class_1390;
import net.minecraft.util.crash.provider.SuspiciousClassesProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.Callable;

@Mixin(SuspiciousClassesProvider.class)
public class SuspiciousClassesProviderMixin implements Callable<String> {
    /**
     * @author forge
     * @reason catch exception
     */
    @Overwrite
    public String call() {
        StringBuilder var1 = new StringBuilder();

        ArrayList<Class> var3;
        try {
            Field var2 = ClassLoader.class.getDeclaredField("classes");
            var2.setAccessible(true);
            var3 = new ArrayList((Vector)var2.get(CrashReport.class.getClassLoader()));
        } catch (Exception var131) {
            return "";
        }

        boolean var4 = true;
        boolean var5 = !CrashReport.class.getCanonicalName().equals("net.minecraft.CrashReport");
        HashMap<String, Integer> var6 = new HashMap();
        String var7 = "";
        Collections.sort(var3, new class_1390((SuspiciousClassesProvider)(Object) this));

        for(Class var9 : var3) {
            if (var9 != null) {
                String var10 = var9.getCanonicalName();
                if (var10 != null
                        && !var10.startsWith("org.lwjgl.")
                        && !var10.startsWith("paulscode.")
                        && !var10.startsWith("org.bouncycastle.")
                        && !var10.startsWith("argo.")
                        && !var10.startsWith("com.jcraft.")
                        && !var10.equals("util.GLX")
                        && (
                        var5
                                ? var10.length() > 3
                                && !var10.equals("net.minecraft.client.MinecraftApplet")
                                && !var10.equals("net.minecraft.client.Minecraft")
                                && !var10.equals("net.minecraft.client.ClientBrandRetriever")
                                && !var10.equals("net.minecraft.server.MinecraftServer")
                                : !var10.startsWith("net.minecraft")
                )) {
                    Package var11 = var9.getPackage();
                    String var12 = var11 == null ? "" : var11.getName();
                    if (var6.containsKey(var12)) {
                        int var13 = var6.get(var12);
                        var6.put(var12, var13 + 1);
                        if (var13 == 3) {
                            if (!var4) {
                                var1.append(", ");
                            }

                            var1.append("...");
                            var4 = false;
                            continue;
                        }

                        if (var13 > 3) {
                            continue;
                        }
                    } else {
                        var6.put(var12, 1);
                    }

                    if (var7 != var12 && var7.length() > 0) {
                        var1.append("], ");
                    }

                    if (!var4 && var7 == var12) {
                        var1.append(", ");
                    }

                    if (var7 != var12) {
                        var1.append("[");
                        var1.append(var12);
                        var1.append(".");
                    }

                    var1.append(var9.getSimpleName());
                    var7 = var12;
                    var4 = false;
                }
            }
        }

        if (var4) {
            var1.append("No suspicious classes found.");
        } else {
            var1.append("]");
        }

        return var1.toString();
    }

//    probably bridge method I guess?
//    public Object call() {
//        return "FML and Forge are installed";
//    }
}
