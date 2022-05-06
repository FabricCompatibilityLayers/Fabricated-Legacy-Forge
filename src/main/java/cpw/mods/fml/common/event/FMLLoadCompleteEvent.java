package cpw.mods.fml.common.event;

import cpw.mods.fml.common.LoaderState;

public class FMLLoadCompleteEvent extends FMLStateEvent {
    public FMLLoadCompleteEvent(Object... data) {
        super(data);
    }

    public LoaderState.ModState getModState() {
        return LoaderState.ModState.AVAILABLE;
    }
}
