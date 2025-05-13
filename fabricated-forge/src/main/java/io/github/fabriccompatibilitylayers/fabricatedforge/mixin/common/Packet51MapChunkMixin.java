package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet51MapChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;

@Mixin(Packet51MapChunk.class)
public abstract class Packet51MapChunkMixin extends Packet {
    @Shadow public int yChMax;

    @Inject(method = "readPacketData", at = @At(value = "FIELD", target = "Lnet/minecraft/src/Packet51MapChunk;includeInitialize:Z", ordinal = 1))
    private void forge$fixBugs(DataInputStream par1, CallbackInfo ci,
                               @Local(ordinal = 1) LocalIntRef var3Ref) {
        int msb = 0; //BugFix: MC does not read the MSB array from the packet properly, causing issues for servers that use blocks > 256
        for (int x = 0; x < 16; x++)
        {
            msb += (yChMax >> x) & 1;
        }

        var3Ref.set(var3Ref.get() + (2048 * msb));
    }
}
