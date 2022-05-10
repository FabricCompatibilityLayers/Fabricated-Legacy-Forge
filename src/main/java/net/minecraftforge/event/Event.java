package net.minecraftforge.event;

public class Event {
    private boolean isCanceled = false;
    private final boolean isCancelable;
    private static ListenerList listeners = new ListenerList();

    public Event() {
        this.setup();
        Class cls = this.getClass();

        boolean found;
        for(found = false; cls != Event.class; cls = cls.getSuperclass()) {
            if (cls.isAnnotationPresent(Cancelable.class)) {
                found = true;
                break;
            }
        }

        this.isCancelable = found;
    }

    public boolean isCancelable() {
        return this.isCancelable;
    }

    public boolean isCanceled() {
        return this.isCanceled;
    }

    public void setCanceled(boolean cancel) {
        if (!this.isCancelable()) {
            throw new IllegalArgumentException("Attempted to cancel a uncancelable event");
        } else {
            this.isCanceled = cancel;
        }
    }

    protected void setup() {
    }

    public ListenerList getListenerList() {
        return listeners;
    }

    public static enum Result {
        DENY,
        DEFAULT,
        ALLOW;

        private Result() {
        }
    }
}
