package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import net.minecraft.client.sound.SoundLoader;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SoundSystem.class)
public interface SoundSystemAccessor {

    @Accessor("soundsLoader")
    SoundLoader getSoundsLoader();

    @Accessor("bgmusicLoader")
    SoundLoader getStreamingLoader();

    @Accessor("musicLoader")
    SoundLoader getMusicLoader();
}
