package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.nbt.NbtElement;

import java.util.Map;

public interface ILevelProperties {
    void setAdditionalProperties(Map<String, NbtElement> additionalProperties);

    NbtElement getAdditionalProperty(String additionalProperty);
}
