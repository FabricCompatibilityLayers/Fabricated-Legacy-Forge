package cpw.mods.fml.common.event;

import cpw.mods.fml.common.LoaderState;

public class FMLServerStartedEvent extends FMLStateEvent {
    public FMLServerStartedEvent(Object... data) {
        super(data);
    }

    public LoaderState.ModState getModState() {
        return LoaderState.ModState.AVAILABLE;
    }
}
