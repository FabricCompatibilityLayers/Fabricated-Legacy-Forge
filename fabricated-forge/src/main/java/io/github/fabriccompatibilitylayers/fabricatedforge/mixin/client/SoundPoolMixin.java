package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.client.SoundPoolExtension;
import net.minecraft.src.SoundPool;
import net.minecraft.src.SoundPoolEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(SoundPool.class)
public class SoundPoolMixin implements SoundPoolExtension {

    @Shadow
    private Map nameToSoundPoolEntriesMapping;

    @Shadow
    private List allSoundPoolEntries;

    @Shadow
    public int numberOfSoundPoolEntries;

    @Shadow
    public boolean isGetRandomSound;

    // Pattern D (@Overwrite): 100% of the method body changes — the File version becomes a
    // 4-line thin wrapper delegating to the new URL overload (Pattern J).
    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates to URL-based addSound overload; allows callers to pass java.net.URL directly
     */
    @Overwrite
    public SoundPoolEntry addSound(String par1Str, File par2File) {
        try {
            return ((SoundPoolExtension) (Object) this).addSound(par1Str, par2File.toURI().toURL());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    // Pattern J (Extension interface): new addSound(String, URL) method added by Forge —
    // contains the original addSound logic but accepts a URL directly instead of a File,
    // and broadens the catch from MalformedURLException to Exception.
    // Logic delta: the decompiled Calamus source mutates par1Str in-place; here we use
    // var3 as a local copy (matching the Forge patch intent) with the processed name
    // used consistently for both map operations and SoundPoolEntry construction.
    @Override
    public SoundPoolEntry addSound(String par1Str, URL url) {
        try {
            String var3 = par1Str;
            var3 = var3.substring(0, var3.indexOf("."));

            if (this.isGetRandomSound) {
                while (Character.isDigit(var3.charAt(var3.length() - 1))) {
                    var3 = var3.substring(0, var3.length() - 1);
                }
            }

            var3 = var3.replaceAll("/", ".");

            if (!this.nameToSoundPoolEntriesMapping.containsKey(var3)) {
                this.nameToSoundPoolEntriesMapping.put(var3, new ArrayList());
            }

            SoundPoolEntry var4 = new SoundPoolEntry(var3, url);
            ((List) this.nameToSoundPoolEntriesMapping.get(var3)).add(var4);
            this.allSoundPoolEntries.add(var4);
            this.numberOfSoundPoolEntries++;
            return var4;
        } catch (Exception var5) {
            var5.printStackTrace();
            throw new RuntimeException(var5);
        }
    }
}