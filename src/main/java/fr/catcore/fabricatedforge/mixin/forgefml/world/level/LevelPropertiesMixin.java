package fr.catcore.fabricatedforge.mixin.forgefml.world.level;

import fr.catcore.fabricatedforge.mixininterface.ILevelProperties;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Map;

@Mixin(LevelProperties.class)
public class LevelPropertiesMixin implements ILevelProperties {

    private Map<String, NbtElement> additionalProperties;

    @Override
    public void setAdditionalProperties(Map<String, NbtElement> additionalProperties) {
        if (this.additionalProperties == null) {
            this.additionalProperties = additionalProperties;
        }
    }

    @Override
    public NbtElement getAdditionalProperty(String additionalProperty) {
        return this.additionalProperties != null ? this.additionalProperties.get(additionalProperty) : null;
    }
}
