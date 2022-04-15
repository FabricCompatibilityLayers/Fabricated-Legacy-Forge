package fr.catcore.fabricatedforge.remapping;

public enum MapEntryType {
    CLASS("CLASS", 0, 'C'),
    METHOD("METHOD", 2, 'M'),
    FIELD("FIELD", 2, 'F');

    private static final MapEntryType[] VALUES = MapEntryType.values();

    final String name;
    final char firstChar;
    final int offset;

    MapEntryType(String name, int offset, char firstChar) {
        this.name = name;
        this.offset = offset;
        this.firstChar = firstChar;
    }

    public static MapEntryType getType(String name) {
        char c = name.charAt(0);
        for (MapEntryType type: VALUES) {
            if (type.firstChar == c) {
                return type;
            }
        }
        return null;
    }
}
