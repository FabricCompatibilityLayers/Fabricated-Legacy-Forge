package cpw.mods.fml.common;

import java.util.EnumSet;

public enum TickType {
    WORLD,
    RENDER,
    GUI,
    CLIENTGUI,
    WORLDLOAD,
    CLIENT,
    PLAYER,
    SERVER;

    private TickType() {
    }

    public EnumSet<TickType> partnerTicks() {
        if (this == CLIENT) {
            return EnumSet.of(RENDER);
        } else if (this == RENDER) {
            return EnumSet.of(CLIENT);
        } else if (this == GUI) {
            return EnumSet.of(CLIENTGUI);
        } else {
            return this == CLIENTGUI ? EnumSet.of(GUI) : EnumSet.noneOf(TickType.class);
        }
    }
}
