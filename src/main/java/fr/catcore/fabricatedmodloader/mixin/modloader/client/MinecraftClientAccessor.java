package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {

    @Accessor("resourcePacks")
    List getResourcePacks();
}
