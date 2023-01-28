package net.minecraftforge.event.terraingen;

import net.minecraft.world.gen.carver.Carver;
import net.minecraftforge.event.Event;

public class InitMapGenEvent extends Event {
    public final InitMapGenEvent.EventType type;
    public final Carver originalGen;
    public Carver newGen;

    InitMapGenEvent(InitMapGenEvent.EventType type, Carver original) {
        this.type = type;
        this.originalGen = original;
        this.newGen = original;
    }

    public static enum EventType {
        CAVE,
        MINESHAFT,
        NETHER_BRIDGE,
        NETHER_CAVE,
        RAVINE,
        SCATTERED_FEATURE,
        STRONGHOLD,
        VILLAGE,
        CUSTOM;

        private EventType() {
        }
    }
}
