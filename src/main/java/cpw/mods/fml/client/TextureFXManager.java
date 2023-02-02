/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.client;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.ModTextureStatic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.TextureManager;
import net.minecraft.client.Sprite;
import net.minecraft.client.texture.ITexturePack;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;
import java.util.logging.Level;

public class TextureFXManager {
    private static final TextureFXManager INSTANCE = new TextureFXManager();
    private Map<Integer, TextureFXManager.TextureProperties> textureProperties = Maps.newHashMap();
    private Multimap<String, OverrideInfo> overrideInfo = ArrayListMultimap.create();
    private HashSet<OverrideInfo> animationSet = new HashSet();
    private List<Sprite> addedTextureFX = new ArrayList();
    private Minecraft client;
    private HashMap<Integer, Dimension> textureDims = new HashMap();
    private IdentityHashMap<Sprite, Integer> effectTextures = new IdentityHashMap();
    private ITexturePack earlyTexturePack;

    public TextureFXManager() {
    }

    void setClient(Minecraft client) {
        this.client = client;
    }

    public boolean onUpdateTextureEffect(Sprite effect) {
        ITextureFX ifx = effect instanceof ITextureFX ? (ITextureFX)effect : null;
        if (ifx != null && ifx.getErrored()) {
            return false;
        } else {
            String name = effect.getClass().getSimpleName();
            this.client.profiler.push(name);

            try {
                if (!FMLClientHandler.instance().hasOptifine()) {
                    effect.method_1613();
                }
            } catch (Exception var6) {
                FMLLog.log(
                        "fml.TextureManager",
                        Level.WARNING,
                        "Texture FX %s has failed to animate. Likely caused by a texture pack change that they did not respond correctly to",
                        new Object[]{name}
                );
                if (ifx != null) {
                    ifx.setErrored(true);
                }

                this.client.profiler.pop();
                return false;
            }

            this.client.profiler.pop();
            if (ifx != null) {
                Dimension dim = this.getTextureDimensions(effect);
                int target = (dim.width >> 4) * (dim.height >> 4) << 2;
                if (effect.field_2152.length != target) {
                    FMLLog.log(
                            "fml.TextureManager",
                            Level.WARNING,
                            "Detected a texture FX sizing discrepancy in %s (%d, %d)",
                            new Object[]{name, effect.field_2152.length, target}
                    );
                    ifx.setErrored(true);
                    return false;
                }
            }

            return true;
        }
    }

    public void scaleTextureFXData(byte[] data, ByteBuffer buf, int target, int length) {
        int sWidth = (int)Math.sqrt((double)(data.length / 4));
        int factor = target / sWidth;
        byte[] tmp = new byte[4];
        buf.clear();
        if (factor > 1) {
            for(int y = 0; y < sWidth; ++y) {
                int sRowOff = sWidth * y;
                int tRowOff = target * y * factor;

                for(int x = 0; x < sWidth; ++x) {
                    int sPos = (x + sRowOff) * 4;
                    tmp[0] = data[sPos + 0];
                    tmp[1] = data[sPos + 1];
                    tmp[2] = data[sPos + 2];
                    tmp[3] = data[sPos + 3];
                    int tPosTop = x * factor + tRowOff;

                    for(int y2 = 0; y2 < factor; ++y2) {
                        buf.position((tPosTop + y2 * target) * 4);

                        for(int x2 = 0; x2 < factor; ++x2) {
                            buf.put(tmp);
                        }
                    }
                }
            }
        }

        buf.position(0).limit(length);
    }

    public void onPreRegisterEffect(Sprite effect) {
        Dimension dim = this.getTextureDimensions(effect);
        if (effect instanceof ITextureFX) {
            ((ITextureFX)effect).onTextureDimensionsUpdate(dim.width, dim.height);
        }
    }

    public int getEffectTexture(Sprite effect) {
        Integer id = (Integer)this.effectTextures.get(effect);
        if (id != null) {
            return id;
        } else {
            int old = GL11.glGetInteger(32873);
            effect.bind(this.client.textureManager);
            id = GL11.glGetInteger(32873);
            GL11.glBindTexture(3553, old);
            this.effectTextures.put(effect, id);
            effect.field_2155 = id;
            return id;
        }
    }

    public void onTexturePackChange(TextureManager engine, ITexturePack texturepack, List<Sprite> effects) {
        this.pruneOldTextureFX(texturepack, effects);

        for(Sprite tex : effects) {
            if (tex instanceof ITextureFX) {
                ((ITextureFX)tex).onTexturePackChanged(engine, texturepack, this.getTextureDimensions(tex));
            }
        }

        this.loadTextures(texturepack);
    }

    public void setTextureDimensions(int id, int width, int height, List<Sprite> effects) {
        Dimension dim = new Dimension(width, height);
        this.textureDims.put(id, dim);

        for(Sprite tex : effects) {
            if (this.getEffectTexture(tex) == id && tex instanceof ITextureFX) {
                ((ITextureFX)tex).onTextureDimensionsUpdate(width, height);
            }
        }
    }

    public Dimension getTextureDimensions(Sprite effect) {
        return this.getTextureDimensions(this.getEffectTexture(effect));
    }

    public Dimension getTextureDimensions(int id) {
        return (Dimension)this.textureDims.get(id);
    }

    public void addAnimation(Sprite anim) {
        OverrideInfo info = new OverrideInfo();
        info.index = anim.field_2153;
        info.imageIndex = anim.field_2157;
        info.textureFX = anim;
        if (this.animationSet.contains(info)) {
            this.animationSet.remove(info);
        }

        this.animationSet.add(info);
    }

    public void loadTextures(ITexturePack texturePack) {
        this.registerTextureOverrides(this.client.textureManager);
    }

    public void registerTextureOverrides(TextureManager renderer) {
        for(OverrideInfo animationOverride : this.animationSet) {
            renderer.method_1416(animationOverride.textureFX);
            this.addedTextureFX.add(animationOverride.textureFX);
            FMLLog.log(
                    "fml.TextureManager",
                    Level.FINE,
                    "Registered texture override %d (%d) on %s (%d)",
                    new Object[]{
                            animationOverride.index,
                            animationOverride.textureFX.field_2153,
                            animationOverride.textureFX.getClass().getSimpleName(),
                            animationOverride.textureFX.field_2157
                    }
            );
        }

        for(String fileToOverride : this.overrideInfo.keySet()) {
            for(OverrideInfo override : this.overrideInfo.get(fileToOverride)) {
                try {
                    BufferedImage image = this.loadImageFromTexturePack(renderer, override.override);
                    ModTextureStatic mts = new ModTextureStatic(override.index, 1, override.texture, image);
                    renderer.method_1416(mts);
                    this.addedTextureFX.add(mts);
                    FMLLog.log(
                            "fml.TextureManager",
                            Level.FINE,
                            "Registered texture override %d (%d) on %s (%d)",
                            new Object[]{override.index, mts.field_2153, override.texture, mts.field_2157}
                    );
                } catch (IOException var8) {
                    FMLLog.log(
                            "fml.TextureManager", Level.WARNING, var8, "Exception occurred registering texture override for %s", new Object[]{fileToOverride}
                    );
                }
            }
        }
    }

    protected void registerAnimatedTexturesFor(ModContainer mod) {
    }

    public void onEarlyTexturePackLoad(ITexturePack fallback) {
        if (this.client == null) {
            this.earlyTexturePack = fallback;
        } else {
            this.loadTextures(fallback);
        }
    }

    public void pruneOldTextureFX(ITexturePack var1, List<Sprite> effects) {
        ListIterator<Sprite> li = this.addedTextureFX.listIterator();

        while(li.hasNext()) {
            Sprite tex = (Sprite)li.next();
            if (tex instanceof FMLTextureFX) {
                if (((FMLTextureFX)tex).unregister(this.client.textureManager, effects)) {
                    li.remove();
                }
            } else {
                effects.remove(tex);
                li.remove();
            }
        }
    }

    public void addNewTextureOverride(String textureToOverride, String overridingTexturePath, int location) {
        OverrideInfo info = new OverrideInfo();
        info.index = location;
        info.override = overridingTexturePath;
        info.texture = textureToOverride;
        this.overrideInfo.put(textureToOverride, info);
        FMLLog.log(
                "fml.TextureManager",
                Level.FINE,
                "Overriding %s @ %d with %s. %d slots remaining",
                new Object[]{textureToOverride, location, overridingTexturePath, SpriteHelper.freeSlotCount(textureToOverride)}
        );
    }

    public BufferedImage loadImageFromTexturePack(TextureManager renderEngine, String path) throws IOException {
        InputStream image = this.client.texturePackManager.getCurrentTexturePack().openStream(path);
        if (image == null) {
            throw new RuntimeException(String.format("The requested image path %s is not found", path));
        } else {
            BufferedImage result = ImageIO.read(image);
            if (result == null) {
                throw new RuntimeException(String.format("The requested image path %s appears to be corrupted", path));
            } else {
                return result;
            }
        }
    }

    public static TextureFXManager instance() {
        return INSTANCE;
    }

    public void fixTransparency(BufferedImage loadedImage, String textureName) {
        if (textureName.matches("^/mob/.*_eyes.*.png$")) {
            for(int x = 0; x < loadedImage.getWidth(); ++x) {
                for(int y = 0; y < loadedImage.getHeight(); ++y) {
                    int argb = loadedImage.getRGB(x, y);
                    if ((argb & 0xFF000000) == 0 && argb != 0) {
                        loadedImage.setRGB(x, y, 0);
                    }
                }
            }
        }
    }

    private class TextureProperties {
        private int textureId;
        private Dimension dim;

        private TextureProperties() {
        }
    }
}
