package fr.catcore.fabricatedforge.mixin.forgefml.world.chunk;

import net.minecraft.util.collection.LongObjectStorage;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.ClientChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientChunkProvider.class)
public abstract class ClientChunkProviderMixin implements ChunkProvider {
    @Shadow private World world;

    @Shadow private LongObjectStorage chunkStorage;

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    public Chunk getOrGenerateChunk(int par1, int par2) {
        Chunk var3 = new Chunk(this.world, par1, par2);
        this.chunkStorage.set(ChunkPos.getIdFromCoords(par1, par2), var3);
        MinecraftForge.EVENT_BUS.post(new ChunkEvent.Load(var3));
        var3.loaded = true;
        return var3;
    }
}
