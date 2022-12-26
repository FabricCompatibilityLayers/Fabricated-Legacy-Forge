package cpw.mods.fml.common.event;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModContainer;

public class FMLInterModComms {
    private static ArrayListMultimap<String, IMCMessage> modMessages = ArrayListMultimap.create();

    public FMLInterModComms() {
    }

    public static boolean sendMessage(String modId, String key, String value) {
        if (Loader.instance().activeModContainer() == null) {
            return false;
        } else {
            modMessages.put(modId, new FMLInterModComms.IMCMessage(Loader.instance().activeModContainer().getModId(), key, value));
            return Loader.isModLoaded(modId) && !Loader.instance().hasReachedState(LoaderState.POSTINITIALIZATION);
        }
    }

    public static class IMCEvent extends FMLEvent {
        private ImmutableList<IMCMessage> currentList;

        public IMCEvent() {
        }

        public void applyModContainer(ModContainer activeContainer) {
            this.currentList = ImmutableList.copyOf(FMLInterModComms.modMessages.get(activeContainer.getModId()));
        }

        public ImmutableList<FMLInterModComms.IMCMessage> getMessages() {
            return this.currentList;
        }
    }

    public static final class IMCMessage {
        public final String sender;
        public final String key;
        public final String value;

        private IMCMessage(String sender, String key, String value) {
            this.key = key;
            this.value = value;
            this.sender = sender;
        }

        public String toString() {
            return this.sender;
        }
    }
}
