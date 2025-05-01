package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.NetHandlerExtension;
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

@Mixin(NetServerHandler.class)
public abstract class NetServerHandlerMixin extends NetHandler implements NetHandlerExtension {
    @Shadow private EntityPlayerMP field_72574_e;

    @Shadow public NetworkManager field_72575_b;

    @Inject(method = "func_72481_a", at = @At("HEAD"))
    private void fml$handleChatMessage(Packet3Chat p_72481_1_, CallbackInfo ci, @Local(argsOnly = true) LocalRef<Packet3Chat> ref) {
        ref.set(FMLNetworkHandler.handleChatMessage(this, p_72481_1_));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void func_72501_a(Packet250CustomPayload p_72501_1_)
    {
        FMLNetworkHandler.handlePacket250Packet(p_72501_1_, field_72575_b, this);
    }

    @Override
    public void handleVanilla250Packet(Packet250CustomPayload p_72501_1_) {
        if ("MC|BEdit".equals(p_72501_1_.field_73630_a)) {
            try {
                DataInputStream var2 = new DataInputStream(new ByteArrayInputStream(p_72501_1_.field_73629_c));
                ItemStack var3 = Packet.func_73276_c(var2);
                if (!ItemWritableBook.func_77829_a(var3.func_77978_p())) {
                    throw new IOException("Invalid book tag!");
                }

                ItemStack var4 = this.field_72574_e.field_71071_by.func_70448_g();
                if (var3 != null && var3.field_77993_c == Item.field_77821_bF.field_77779_bT && var3.field_77993_c == var4.field_77993_c) {
                    var4.func_77982_d(var3.func_77978_p());
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        } else if ("MC|BSign".equals(p_72501_1_.field_73630_a)) {
            try {
                DataInputStream var8 = new DataInputStream(new ByteArrayInputStream(p_72501_1_.field_73629_c));
                ItemStack var10 = Packet.func_73276_c(var8);
                if (!ItemEditableBook.func_77828_a(var10.func_77978_p())) {
                    throw new IOException("Invalid book tag!");
                }

                ItemStack var12 = this.field_72574_e.field_71071_by.func_70448_g();
                if (var10 != null && var10.field_77993_c == Item.field_77823_bG.field_77779_bT && var12.field_77993_c == Item.field_77821_bF.field_77779_bT) {
                    var12.func_77982_d(var10.func_77978_p());
                    var12.field_77993_c = Item.field_77823_bG.field_77779_bT;
                }
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        } else if ("MC|TrSel".equals(p_72501_1_.field_73630_a)) {
            try {
                DataInputStream var9 = new DataInputStream(new ByteArrayInputStream(p_72501_1_.field_73629_c));
                int var11 = var9.readInt();
                Container var13 = this.field_72574_e.field_71070_bA;
                if (var13 instanceof ContainerMerchant) {
                    ((ContainerMerchant)var13).func_75175_c(var11);
                }
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
    }

    @Override
    public void func_72494_a(Packet131MapData p_72494_1_)
    {
        FMLNetworkHandler.handlePacket131Packet(this, p_72494_1_);
    }

    // modloader compat -- yuk!
    @Override
    public EntityPlayerMP getPlayer()
    {
        return field_72574_e;
    }
}
