package fr.catcore.fabricatedforge.mixin.forgefml.inventory.slot;

import fr.catcore.fabricatedforge.mixininterface.ISlot;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Slot.class)
public class SlotMixin implements ISlot {
    @Shadow @Final private int invSlot;
    protected int backgroundIconIndex = -1;
    protected String texture = "/gui/items.png";

    /**
     * @author forge
     * @reason replace default icon index
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public int method_3297() {
        return this.backgroundIconIndex;
    }

    @Override
    public String getBackgroundIconTexture() {
        return this.texture == null ? "/gui/items.png" : this.texture;
    }

    @Override
    public void setBackgroundIconIndex(int iconIndex) {
        this.backgroundIconIndex = iconIndex;
    }

    @Override
    public void setBackgroundIconTexture(String textureFilename) {
        this.texture = textureFilename;
    }

    @Override
    public int getSlotIndex() {
        return this.invSlot;
    }
}
