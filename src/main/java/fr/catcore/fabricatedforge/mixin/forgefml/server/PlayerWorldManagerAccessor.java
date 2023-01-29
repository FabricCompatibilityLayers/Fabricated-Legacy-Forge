package fr.catcore.fabricatedforge.mixin.forgefml.server;

import net.minecraft.server.PlayerWorldManager;
import net.minecraft.server.class_793;
import net.minecraft.util.collection.LongObjectStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PlayerWorldManager.class)
public interface PlayerWorldManagerAccessor {
    @Accessor
    LongObjectStorage getPlayerInstancesById();

    @Accessor
    List<class_793> getField_2792();
}
