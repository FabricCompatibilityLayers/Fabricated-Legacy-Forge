package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.NetLoginHandlerExtension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetLoginHandler.class)
public abstract class NetLoginHandlerMixin extends NetHandler implements NetLoginHandlerExtension {
    @Shadow public abstract void func_72527_a(String p_72527_1_);

    @Shadow private MinecraftServer field_72534_f;

    @Shadow public String field_72543_h;

    @Shadow public TcpConnection field_72538_b;

    @Shadow public boolean field_72539_c;

    @ModifyConstant(method = "func_72532_c", constant = @Constant(intValue = 600))
    private int fml$increaseThreshold(int constant) {
        return 6000;
    }

    @Inject(method = "func_72455_a", at = @At("RETURN"))
    private void fml$handleLoginPacketOnServer(Packet1Login p_72455_1_, CallbackInfo ci) {
        FMLNetworkHandler.handleLoginPacketOnServer((NetLoginHandler) (Object) this, p_72455_1_);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void func_72529_d() {
        FMLNetworkHandler.onConnectionReceivedFromClient((NetLoginHandler) (Object) this, this.field_72534_f, this.field_72538_b.func_74430_c(), this.field_72543_h);
    }

    @Override
    public void completeConnection(String var1) {
        if (var1 != null) {
            this.func_72527_a(var1);
        } else {
            EntityPlayerMP var2 = this.field_72534_f.func_71203_ab().func_72366_a(this.field_72543_h);
            if (var2 != null) {
                this.field_72534_f.func_71203_ab().func_72355_a(this.field_72538_b, var2);
            }
        }

        this.field_72539_c = true;
    }

    @Override
    public void func_72501_a(Packet250CustomPayload p_72501_1_)
    {
        FMLNetworkHandler.handlePacket250Packet(p_72501_1_, field_72538_b, this);
    }

    @Override
    public void handleVanilla250Packet(Packet250CustomPayload payload)
    {
        // NOOP for login
    }

    @Override
    public EntityPlayer getPlayer()
    {
        return null;
    };
}
