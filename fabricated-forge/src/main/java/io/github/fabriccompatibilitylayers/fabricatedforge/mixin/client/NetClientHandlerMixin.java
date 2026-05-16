package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.TileEntityExtension;
import net.minecraft.src.GuiNewChat;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.Packet255KickDisconnect;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.WorldClient;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetClientHandler.class)
public class NetClientHandlerMixin {

    @Shadow private NetworkManager netManager;

    // Pattern E (@WrapOperation): intercepts the networkShutdown(String, Object...) call-site
    // in handleKickDisconnect and re-calls it with the kick reason as the vararg instead of the
    // empty Object[] the vanilla code passes. Surgical — rest of the method is untouched.
    @WrapOperation(
            method = "handleKickDisconnect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/NetworkManager;networkShutdown(Ljava/lang/String;[Ljava/lang/Object;)V")
    )
    private void forge$kickDisconnectWithReason(NetworkManager instance, String key, Object[] args,
            Operation<Void> op,
            @Local(argsOnly = true) Packet255KickDisconnect packet) {
        op.call(instance, key, new Object[]{packet.reason});
    }

    // Pattern Q (@WrapWithCondition): gates printChatMessage on ClientChatReceivedEvent not being
    // cancelled and event.message being non-null. printChatMessage returns void so suppression is safe.
    @WrapWithCondition(
            method = "handleChat",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiNewChat;printChatMessage(Ljava/lang/String;)V")
    )
    private boolean forge$chatReceivedEvent(GuiNewChat chatGui, String message) {
        ClientChatReceivedEvent event = new ClientChatReceivedEvent(message);
        return !MinecraftForge.EVENT_BUS.post(event) && event.message != null;
    }

    // Pattern E + H (@WrapOperation + @Share): captures the TileEntity returned by getBlockTileEntity
    // (which is inside the blockExists guard) via @Share, then at RETURN calls onDataPacket if var2
    // is non-null and wasn't already handled by the vanilla mob-spawner branch.
    // Logic delta: patch adds an inline else-if; here the same condition is evaluated at RETURN —
    // same net effect since no other code runs between the inner if and the method's end.
    // If blockExists was false, getBlockTileEntity never fires, ref stays null, and the RETURN
    // injector is a no-op.
    @WrapOperation(
            method = "handleTileEntityData",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/WorldClient;getBlockTileEntity(III)Lnet/minecraft/src/TileEntity;")
    )
    private TileEntity forge$captureTileEntityData(WorldClient world, int x, int y, int z, Operation<TileEntity> op,
            @Share(namespace = "fabricated-forge", value = "tileEntity") LocalRef<TileEntity> ref) {
        TileEntity te = op.call(world, x, y, z);
        ref.set(te);
        return te;
    }

    @Inject(method = "handleTileEntityData", at = @At("RETURN"))
    private void forge$tileEntityOnDataPacket(Packet132TileEntityData par1, CallbackInfo ci,
            @Share(namespace = "fabricated-forge", value = "tileEntity") LocalRef<TileEntity> ref) {
        TileEntity var2 = ref.get();
        if (var2 != null && !(par1.actionType == 1 && var2 instanceof TileEntityMobSpawner)) {
            ((TileEntityExtension) var2).onDataPacket(netManager, par1);
        }
    }
}