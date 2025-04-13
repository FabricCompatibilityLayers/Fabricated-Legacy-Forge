package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import fr.catcore.cursedmixinextensions.annotations.Public;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.client.NetClientHandlerExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

@Mixin(NetClientHandler.class)
public abstract class NetClientHandlerMixin extends NetHandler implements NetClientHandlerExtension {
    @Shadow private NetworkManager field_72555_g;

    @Shadow public abstract void func_72552_c(Packet par1);

    @Shadow private boolean field_72554_f;
    @Shadow private Minecraft field_72563_h;

    private static byte connectionCompatibilityLevel;

    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/lang/String;I)V", at = @At("RETURN"))
    private void fml$onClientConnectionToRemoteServer(Minecraft p_i3103_1_, String p_i3103_2_, int p_i3103_3_, CallbackInfo ci) {
        FMLNetworkHandler.onClientConnectionToRemoteServer(this, p_i3103_2_, p_i3103_3_, this.field_72555_g);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Lnet/minecraft/src/IntegratedServer;)V", at = @At("RETURN"))
    private void fml$onClientConnectionToIntegratedServer(Minecraft p_i3104_1_, IntegratedServer p_i3104_2_, CallbackInfo ci) {
        FMLNetworkHandler.onClientConnectionToIntegratedServer(this, p_i3104_2_, this.field_72555_g);
    }

    @Inject(method = "func_72513_a", at = @At("HEAD"))
    private void fml$sendFMLFakeLoginPacket(Packet252SharedKey p_72513_1_, CallbackInfo ci) {
        this.func_72552_c(FMLNetworkHandler.getFMLFakeLoginPacket());
    }

    @Inject(method = "func_72455_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/NetClientHandler;func_72552_c(Lnet/minecraft/src/Packet;)V"))
    private void fml$onConnectionEstablishedToServer(Packet1Login p_72455_1_, CallbackInfo ci) {
        FMLNetworkHandler.onConnectionEstablishedToServer(this, field_72555_g, p_72455_1_);
    }

    @Inject(method = "func_72546_b", at = @At("RETURN"))
    private void fml$onConnectionClosed(Packet p_72546_1_, CallbackInfo ci) {
        if (!this.field_72554_f) {
            FMLNetworkHandler.onConnectionClosed(this.field_72555_g, this.getPlayer());
        }
    }

    @Inject(method = "func_72481_a", at = @At("HEAD"))
    private void fml$handleChatMessage(Packet3Chat p_72481_1_, CallbackInfo ci, @Local(argsOnly = true) LocalRef<Packet3Chat> ref) {
        ref.set(FMLNetworkHandler.handleChatMessage(this, p_72481_1_));
    }

    /**
     * @author cpw?
     * @reason redirecting to FMLNetworking system
     */
    @Overwrite
    public void func_72494_a(Packet131MapData p_72494_1_)
    {
        FMLNetworkHandler.handlePacket131Packet(this, p_72494_1_);
    }

    @Override
    public void fmlPacket131Callback(Packet131MapData p_72494_1_) {
        if (p_72494_1_.field_73438_a == Item.field_77744_bd.field_77779_bT) {
            ItemMap.func_77874_a(p_72494_1_.field_73436_b, this.field_72563_h.field_71441_e).func_76192_a(p_72494_1_.field_73437_c);
        } else {
            System.out.println("Unknown itemid: " + p_72494_1_.field_73436_b);
        }
    }

    /**
     * @author cpw?
     * @reason redirecting to FMLNetworking system
     */
    @Overwrite
    public void func_72501_a(Packet250CustomPayload p_72501_1_)
    {
        FMLNetworkHandler.handlePacket250Packet(p_72501_1_, field_72555_g, this);
    }

    @Override
    public void handleVanilla250Packet(Packet250CustomPayload p_72501_1_) {
        if ("MC|TPack".equals(p_72501_1_.field_73630_a)) {
            String[] var2 = (new String(p_72501_1_.field_73629_c)).split("\u0000");
            String var3 = var2[0];
            if (var2[1].equals("16")) {
                if (this.field_72563_h.field_71418_C.func_77298_g()) {
                    this.field_72563_h.field_71418_C.func_77296_a(var3);
                } else if (this.field_72563_h.field_71418_C.func_77300_f()) {
                    this.field_72563_h.func_71373_a(new GuiYesNo(new NetClientWebTextures(this, var3), StringTranslate.func_74808_a().func_74805_b("multiplayer.texturePrompt.line1"), StringTranslate.func_74808_a().func_74805_b("multiplayer.texturePrompt.line2"), 0));
                }
            }
        } else if ("MC|TrList".equals(p_72501_1_.field_73630_a)) {
            DataInputStream var8 = new DataInputStream(new ByteArrayInputStream(p_72501_1_.field_73629_c));

            try {
                int var9 = var8.readInt();
                GuiScreen var4 = this.field_72563_h.field_71462_r;
                if (var4 != null && var4 instanceof GuiMerchant && var9 == this.field_72563_h.field_71439_g.field_71070_bA.field_75152_c) {
                    IMerchant var5 = ((GuiMerchant)var4).func_74199_h();
                    MerchantRecipeList var6 = MerchantRecipeList.func_77204_a(var8);
                    var5.func_70930_a(var6);
                }
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }

    }

    @Override
    public EntityPlayer getPlayer()
    {
        return field_72563_h.field_71439_g;
    }

    @Public
    private static void setConnectionCompatibilityLevel(byte connectionCompatibilityLevel)
    {
        NetClientHandlerMixin.connectionCompatibilityLevel = connectionCompatibilityLevel;
    }

    @Public
    private static byte getConnectionCompatibilityLevel()
    {
        return connectionCompatibilityLevel;
    }
}
