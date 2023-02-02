package fr.catcore.fabricatedforge.mixin.forgefml.block.entity;

import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(BlockEntity.class)
public interface BlockEntityAccessor {
    @Accessor
    static Map<String, Class> getStringClassMap() {
        return null;
    }
}
