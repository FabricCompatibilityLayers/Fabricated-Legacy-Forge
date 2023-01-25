package fr.catcore.fabricatedforge.mixininterface;

public interface ISlot {
    String getBackgroundIconTexture();

    void setBackgroundIconIndex(int iconIndex);

    void setBackgroundIconTexture(String textureFilename);

    int getSlotIndex();
}
