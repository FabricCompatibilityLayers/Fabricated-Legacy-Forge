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
    public Sound addSound(String par1Str, URL par2URL) {
        String var3 = par1Str;
        par1Str = par1Str.substring(0, par1Str.indexOf("."));
        if (this.field_2269) {
            while (Character.isDigit(par1Str.charAt(par1Str.length() - 1))) {
                par1Str = par1Str.substring(0, par1Str.length() - 1);
            }
        }

        par1Str = par1Str.replaceAll("/", ".");
        if (!this.soundMap.containsKey(par1Str)) {
            this.soundMap.put(par1Str, new ArrayList());
        }

        Sound var4 = new Sound(var3, par2URL);
        ((List) this.soundMap.get(par1Str)).add(var4);
        this.soundList.add(var4);
        ++this.soundCount;
        return var4;
    }
}
