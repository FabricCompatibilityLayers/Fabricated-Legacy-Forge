package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import cpw.mods.fml.relauncher.FMLRelauncher;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.client.MinecraftAppletExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.src.CanvasMinecraftApplet;
import net.minecraft.src.MinecraftAppletImpl;
import net.minecraft.src.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.applet.Applet;
import java.awt.*;

@Mixin(MinecraftApplet.class)
public class MinecraftAppletMixin extends Applet implements MinecraftAppletExtension {
    @Shadow private Canvas field_71483_a;
    @Shadow private Minecraft field_71481_b;

    @Unique
    private boolean relaunched = false;

    @Override
    public void setRelaunched(boolean relaunched) {
        this.relaunched = relaunched;
    }

    @Override
    public boolean isRelaunched() {
        return relaunched;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void init() {
        FMLRelauncher.appletEntry((MinecraftApplet)(Object)this);
    }

    @Override
    public void fmlInitReentry() {
        this.field_71483_a = new CanvasMinecraftApplet((MinecraftApplet)(Object) this);
        boolean var1 = "true".equalsIgnoreCase(this.getParameter("fullscreen"));
        this.field_71481_b = new MinecraftAppletImpl((MinecraftApplet) (Object) this, this.field_71483_a, (MinecraftApplet)(Object) this, this.getWidth(), this.getHeight(), var1);
        this.field_71481_b.field_71450_k = this.getDocumentBase().getHost();
        if (this.getDocumentBase().getPort() > 0) {
            this.field_71481_b.field_71450_k = this.field_71481_b.field_71450_k + ":" + this.getDocumentBase().getPort();
        }

        if (this.getParameter("username") != null && this.getParameter("sessionid") != null) {
            this.field_71481_b.field_71449_j = new Session(this.getParameter("username"), this.getParameter("sessionid"));
            System.out.println("Setting user: " + this.field_71481_b.field_71449_j.field_74286_b + ", " + this.field_71481_b.field_71449_j.field_74287_c);
        } else {
            this.field_71481_b.field_71449_j = new Session("Player", "");
        }

        ((MinecraftAccessor) this.field_71481_b).callFunc_71390_a("true".equals(this.getParameter("demo")));
        if (this.getParameter("server") != null && this.getParameter("port") != null) {
            this.field_71481_b.func_71367_a(this.getParameter("server"), Integer.parseInt(this.getParameter("port")));
        }

        this.field_71481_b.field_71448_m = !"true".equals(this.getParameter("stand-alone"));
        this.setLayout(new BorderLayout());
        this.add(this.field_71483_a, "Center");
        this.field_71483_a.setFocusable(true);
        this.validate();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void start()
    {
        FMLRelauncher.appletStart(this);
    }

    @Override
    public void fmlStartReentry()
    {
        if (this.field_71481_b != null) {
            this.field_71481_b.field_71445_n = false;
        }
    }
}
