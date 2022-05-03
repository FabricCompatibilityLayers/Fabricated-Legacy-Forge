package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import net.minecraft.client.class_534;
import net.minecraft.client.texture.TexturePackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_534.class)
public interface class_534Accessor {

    @Accessor("packManager")
    TexturePackManager getTexturePackManager();
}
