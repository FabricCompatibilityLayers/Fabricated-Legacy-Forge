package fr.catcore.fabricatedforge.mixin.forgefml.structure;

import net.minecraft.structure.StrongholdPieces;
import net.minecraft.structure.class_25;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StrongholdPieces.class)
public interface StrongholdPiecesAccessor {

    @Accessor("field_25")
    static class_25 getField_25() {
        return null;
    }
}
