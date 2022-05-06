package fr.catcore.fabricatedforge.mixin.modloader.client;

import net.minecraft.client.render.entity.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerEntityRenderer.class)
public interface PlayerEntityRendererAccessor {

    @Accessor("field_2136")
    static void setArmor(String[] armor) {

    }


    @Accessor("field_2136")
    static String[] getArmor() {
        return new String[0];
    }
}
