package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import fr.catcore.fabricatedmodloader.mixininterface.ISoundLoader;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(SoundLoader.class)
public class SoundLoaderMixin implements ISoundLoader {
    @Shadow
    public boolean field_2269;

    @Shadow
    private Map soundMap;

    @Shadow
    private List soundList;

    @Shadow
    public int soundCount;

    /**
     * @author
     */
    @Overwrite
    public Sound getSound(String key, File file) {
        try {
            return this.addSound(key, file.toURI().toURL());
        } catch (MalformedURLException var4) {
            var4.printStackTrace();
            throw new RuntimeException(var4);
        }
    }

    @Override
    public Sound addSound(String key, URL url) {
        String var3 = key;
        key = key.substring(0, key.indexOf("."));
        if (this.field_2269) {
            while (Character.isDigit(key.charAt(key.length() - 1))) {
                key = key.substring(0, key.length() - 1);
            }
        }

        key = key.replaceAll("/", ".");
        if (!this.soundMap.containsKey(key)) {
            this.soundMap.put(key, new ArrayList());
        }

        Sound var4 = new Sound(var3, url);
        ((List) this.soundMap.get(key)).add(var4);
        this.soundList.add(var4);
        ++this.soundCount;
        return var4;
    }
}
