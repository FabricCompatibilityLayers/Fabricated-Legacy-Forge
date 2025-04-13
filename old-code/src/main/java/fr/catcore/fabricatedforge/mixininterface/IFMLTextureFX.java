package fr.catcore.fabricatedforge.mixininterface;

import cpw.mods.fml.client.FMLTextureFX;

public interface IFMLTextureFX {
    default void setup() {}

    default void callFMLSetup() {

    }

    default FMLTextureFX getThis() {
        return (FMLTextureFX) (Object) this;
    }
}
