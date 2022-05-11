package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.client.sound.Sound;

import java.net.URL;

public interface ISoundLoader {

    public Sound addSound(String par1Str, URL url);
}
