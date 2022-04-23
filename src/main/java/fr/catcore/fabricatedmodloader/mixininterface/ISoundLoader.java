package fr.catcore.fabricatedmodloader.mixininterface;

import net.minecraft.client.sound.Sound;

import java.net.URL;

public interface ISoundLoader {

    Sound addSound(String par1Str, URL par2URL);
}
