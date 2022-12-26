package cpw.mods.fml.common.event;

import cpw.mods.fml.common.ModContainer;

public class FMLEvent {
    public FMLEvent() {
    }

    public final String getEventType() {
        return this.getClass().getSimpleName();
    }

    public void applyModContainer(ModContainer activeContainer) {
    }
}
