package modloader;

import com.google.gson.JsonObject;
import net.minecraft.client.resource.ResourceMetadataProvider;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.ResourcePack;
import net.minecraft.util.Identifier;
import net.minecraft.util.MetadataSerializer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class ModResourcePack implements ResourcePack {
    private final Class modClass;

    public ModResourcePack(Class modClass) {
        this.modClass = modClass;
    }

    @Override
    public InputStream open(Identifier id) {
        return this.modClass.getResourceAsStream("/assets/minecraft/" + id.getPath());
    }

    @Override
    public boolean contains(Identifier id) {
        try {
            return this.open(id) != null;
        } catch (RuntimeException var3) {
            return false;
        }
    }

    @Override
    public Set getNamespaces() {
        return DefaultResourcePack.NAMESPACES;
    }

    @Override
    public ResourceMetadataProvider parseMetadata(MetadataSerializer serializer, String key) {
        return serializer.fromJson(key, new JsonObject());
    }

    @Override
    public BufferedImage getIcon() {
        try {
            return ImageIO.read(DefaultResourcePack.class.getResourceAsStream("/" + (new Identifier("pack.png")).getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return this.modClass.getSimpleName();
    }
}
