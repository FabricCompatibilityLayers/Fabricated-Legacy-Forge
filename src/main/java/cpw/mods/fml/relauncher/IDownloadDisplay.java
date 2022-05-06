package cpw.mods.fml.relauncher;

public interface IDownloadDisplay {
    void resetProgress(int i);

    void setPokeThread(Thread thread);

    void updateProgress(int i);

    boolean shouldStopIt();

    void updateProgressString(String string, Object... objects);

    Object makeDialog();

    void makeHeadless();
}
