package cpw.mods.fml.common;

import java.util.EnumSet;

public class SingleIntervalHandler implements IScheduledTickHandler {
    private ITickHandler wrapped;

    public SingleIntervalHandler(ITickHandler handler) {
        this.wrapped = handler;
    }

    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        this.wrapped.tickStart(type, tickData);
    }

    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        this.wrapped.tickEnd(type, tickData);
    }

    public EnumSet<TickType> ticks() {
        return this.wrapped.ticks();
    }

    public String getLabel() {
        return this.wrapped.getLabel();
    }

    public int nextTickSpacing() {
        return 1;
    }
}
