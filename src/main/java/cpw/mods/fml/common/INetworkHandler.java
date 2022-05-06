package cpw.mods.fml.common;

public interface INetworkHandler {
    boolean onChat(Object... objects);

    void onPacket250Packet(Object... objects);

    void onServerLogin(Object object);
}
