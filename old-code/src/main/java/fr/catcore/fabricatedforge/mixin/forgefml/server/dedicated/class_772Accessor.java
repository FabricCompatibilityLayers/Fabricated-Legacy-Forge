package fr.catcore.fabricatedforge.mixin.forgefml.server.dedicated;

import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.dedicated.class_772;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(class_772.class)
public interface class_772Accessor {

    @Invoker("<init>")
    static class_772 newInstance(MinecraftDedicatedServer minecraftDedicatedServer) {
        return null;
    }
}
