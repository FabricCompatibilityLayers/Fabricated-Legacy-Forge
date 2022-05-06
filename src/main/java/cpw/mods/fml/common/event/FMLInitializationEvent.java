package cpw.mods.fml.common.event;

import cpw.mods.fml.common.LoaderState;

public class FMLInitializationEvent extends FMLStateEvent {
    public FMLInitializationEvent(Object... data) {
        super(data);
    }

    public LoaderState.ModState getModState() {
        return LoaderState.ModState.INITIALIZED;
    }
}
