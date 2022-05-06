package cpw.mods.fml.relauncher;

public class DummyDownloader implements IDownloadDisplay {
    public DummyDownloader() {
    }

    public void resetProgress(int sizeGuess) {
    }

    public void setPokeThread(Thread currentThread) {
    }

    public void updateProgress(int fullLength) {
    }

    public boolean shouldStopIt() {
        return false;
    }

    public void updateProgressString(String string, Object... data) {
    }

    public Object makeDialog() {
        return null;
    }

    public void makeHeadless() {
    }
}
