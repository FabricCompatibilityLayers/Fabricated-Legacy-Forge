package cpw.mods.fml.common;

public enum Side {
    CLIENT,
    SERVER,
    BUKKIT;

    private Side() {
    }

    public boolean isServer() {
        return !this.isClient();
    }

    public boolean isClient() {
        return this == CLIENT;
    }
}
