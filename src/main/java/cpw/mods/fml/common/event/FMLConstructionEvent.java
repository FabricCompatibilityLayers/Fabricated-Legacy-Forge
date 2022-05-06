package cpw.mods.fml.common.event;

import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.common.discovery.ASMDataTable;

public class FMLConstructionEvent extends FMLStateEvent {
    private ModClassLoader modClassLoader;
    private ASMDataTable asmData;

    public FMLConstructionEvent(Object... eventData) {
        super(new Object[0]);
        this.modClassLoader = (ModClassLoader)eventData[0];
        this.asmData = (ASMDataTable)eventData[1];
    }

    public ModClassLoader getModClassLoader() {
        return this.modClassLoader;
    }

    public LoaderState.ModState getModState() {
        return LoaderState.ModState.CONSTRUCTED;
    }

    public ASMDataTable getASMHarvestedData() {
        return this.asmData;
    }
}