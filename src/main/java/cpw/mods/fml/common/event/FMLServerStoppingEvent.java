package cpw.mods.fml.common.event;

import cpw.mods.fml.common.LoaderState;

public class FMLServerStoppingEvent extends FMLStateEvent {
    public FMLServerStoppingEvent(Object... data) {
        super(data);
    }

    public LoaderState.ModState getModState() {
        return LoaderState.ModState.AVAILABLE;
    }
}
