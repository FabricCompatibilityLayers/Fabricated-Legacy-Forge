package fr.catcore.fabricatedforge.mixin.modloader.common;

import modloader.ModLoader;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.ServerChunkProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkProvider.class)
public class ServerChunkProviderMixin {

    @Shadow
    private ChunkProvider chunkGenerator;

    @Shadow
    private ServerWorld world;

    @Inject(method = "decorateChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;setModified()V"))
    private void modLoaderPopulateChunk(ChunkProvider provider, int x, int z, CallbackInfo ci) {
        ModLoader.populateChunk(this.chunkGenerator, x, z, this.world);
    }
}
