/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.NetHandlerExtension;
import net.minecraft.server.MinecraftServer;
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

    @Shadow public INetworkManager field_72575_b;

    @Shadow
    private MinecraftServer field_72573_d;

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
            } catch (Exception var12) {
                var12.printStackTrace();
            }
        } else if ("MC|BSign".equals(p_72501_1_.field_73630_a)) {
            try {
                DataInputStream var13 = new DataInputStream(new ByteArrayInputStream(p_72501_1_.field_73629_c));
                ItemStack var18 = Packet.func_73276_c(var13);
                if (!ItemEditableBook.func_77828_a(var18.func_77978_p())) {
                    throw new IOException("Invalid book tag!");
                }

                ItemStack var23 = this.field_72574_e.field_71071_by.func_70448_g();
                if (var18 != null && var18.field_77993_c == Item.field_77823_bG.field_77779_bT && var23.field_77993_c == Item.field_77821_bF.field_77779_bT) {
                    var23.func_77982_d(var18.func_77978_p());
                    var23.field_77993_c = Item.field_77823_bG.field_77779_bT;
                }
            } catch (Exception var11) {
                var11.printStackTrace();
            }
        } else if ("MC|TrSel".equals(p_72501_1_.field_73630_a)) {
            try {
                DataInputStream var14 = new DataInputStream(new ByteArrayInputStream(p_72501_1_.field_73629_c));
                int var19 = var14.readInt();
                Container var24 = this.field_72574_e.field_71070_bA;
                if (var24 instanceof ContainerMerchant) {
                    ((ContainerMerchant)var24).func_75175_c(var19);
                }
            } catch (Exception var10) {
                var10.printStackTrace();
            }
        } else if ("MC|AdvCdm".equals(p_72501_1_.field_73630_a)) {
            if (!this.field_72573_d.func_82356_Z()) {
                this.field_72574_e.func_70006_a(this.field_72574_e.func_70004_a("advMode.notEnabled", new Object[0]));
            } else if (this.field_72574_e.func_70003_b(2, "") && this.field_72574_e.field_71075_bZ.field_75098_d) {
                try {
                    DataInputStream var15 = new DataInputStream(new ByteArrayInputStream(p_72501_1_.field_73629_c));
                    int var20 = var15.readInt();
                    int var25 = var15.readInt();
                    int var5 = var15.readInt();
                    String var6 = Packet.func_73282_a(var15, 256);
                    TileEntity var7 = this.field_72574_e.field_70170_p.func_72796_p(var20, var25, var5);
                    if (var7 != null && var7 instanceof TileEntityCommandBlock) {
                        ((TileEntityCommandBlock)var7).func_82352_b(var6);
                        this.field_72574_e.field_70170_p.func_72845_h(var20, var25, var5);
                        this.field_72574_e.func_70006_a("Command set: " + var6);
                    }
                } catch (Exception var9) {
                    var9.printStackTrace();
                }
            } else {
                this.field_72574_e.func_70006_a(this.field_72574_e.func_70004_a("advMode.notAllowed", new Object[0]));
            }
        } else if ("MC|Beacon".equals(p_72501_1_.field_73630_a)) {
            if (this.field_72574_e.field_71070_bA instanceof ContainerBeacon) {
                try {
                    DataInputStream var16 = new DataInputStream(new ByteArrayInputStream(p_72501_1_.field_73629_c));
                    int var21 = var16.readInt();
                    int var26 = var16.readInt();
                    ContainerBeacon var27 = (ContainerBeacon)this.field_72574_e.field_71070_bA;
                    Slot var28 = var27.func_75139_a(0);
                    if (var28.func_75216_d()) {
                        var28.func_75209_a(1);
                        TileEntityBeacon var29 = var27.func_82863_d();
                        var29.func_82128_d(var21);
                        var29.func_82127_e(var26);
                        var29.func_70296_d();
                    }
                } catch (Exception var8) {
                    var8.printStackTrace();
                }
            }
        } else if ("MC|ItemName".equals(p_72501_1_.field_73630_a) && this.field_72574_e.field_71070_bA instanceof ContainerRepair) {
            ContainerRepair var17 = (ContainerRepair)this.field_72574_e.field_71070_bA;
            if (p_72501_1_.field_73629_c != null && p_72501_1_.field_73629_c.length >= 1) {
                String var22 = ChatAllowedCharacters.func_71565_a(new String(p_72501_1_.field_73629_c));
                if (var22.length() <= 30) {
                    var17.func_82850_a(var22);
                }
            } else {
                var17.func_82850_a("");
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
