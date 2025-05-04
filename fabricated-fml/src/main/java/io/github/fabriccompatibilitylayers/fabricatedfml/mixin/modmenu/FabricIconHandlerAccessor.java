package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.modmenu;

import com.terraformersmc.modmenu.util.mod.fabric.FabricIconHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(value = FabricIconHandler.class, remap = false)
public interface FabricIconHandlerAccessor {
    @Accessor(value = "modIconCache", remap = false)
    Map<Path, BufferedImage> getCache();
}
