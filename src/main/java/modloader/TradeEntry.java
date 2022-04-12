package modloader;

public class TradeEntry {
    public final int id;
    public float chance;
    public boolean buying;
    public int min;
    public int max;

    public TradeEntry(int id, float chance, boolean buying, int min, int max) {
        this.min = 0;
        this.max = 0;
        this.id = id;
        this.chance = chance;
        this.buying = buying;
        this.min = min;
        this.max = max;
    }

    public TradeEntry(int id, float chance, boolean buying) {
        this(id, chance, buying, 0, 0);
    }
}
