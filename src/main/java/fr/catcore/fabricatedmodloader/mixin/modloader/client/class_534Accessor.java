package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import net.minecraft.client.class_534;
import net.minecraft.client.class_584;
import net.minecraft.client.texture.TexturePackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(class_534.class)
public interface class_534Accessor {

    @Accessor("field_1978")
    List<class_584> getAnimations();

    @Accessor("packManager")
    TexturePackManager getTexturePackManager();
}
