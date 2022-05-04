package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BipedEntityRenderer.class)
public interface BipedEntityRendererAccessor {

    @Accessor("field_5195")
    static String[] getArmorTypes() {
        return null;
    }

    @Accessor("field_5195")
    static void setArmorTypes(String[] types) {

    }
}
