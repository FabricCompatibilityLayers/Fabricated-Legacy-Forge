package cpw.mods.fml.common.event;

public class FMLEvent {
    public FMLEvent() {
    }

    public final String getEventType() {
        return this.getClass().getSimpleName();
    }
}
