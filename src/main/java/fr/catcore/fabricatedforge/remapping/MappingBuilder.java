package fr.catcore.fabricatedforge.remapping;

import java.util.ArrayList;
import java.util.List;

public class MappingBuilder {
    private final String obfucated;
    private final String intermediary;
    private final List<Entry> entries = new ArrayList<>();

    private MappingBuilder(String obfucated, String intermediary) {
        this.obfucated = obfucated;
        this.intermediary = intermediary;
    }

    private static String toString(String... line) {
        StringBuilder builder = new StringBuilder(line[0]);
        for (int j = 1; j < line.length; j++) {
            builder.append('\t');
            builder.append(line[j]);
        }
        return builder.toString();
    }

    public static MappingBuilder create(String obfucated, String intermediary) {
        return new MappingBuilder(obfucated, intermediary);
    }

    public static MappingBuilder create(String name) {
        return new MappingBuilder(name, name);
    }

    public MappingBuilder field(String obfuscated, String intermediary, String description) {
        this.entries.add(new Entry(obfuscated, intermediary, description, Type.FIELD));
        return this;
    }

    public MappingBuilder field(String name, String description) {
        this.entries.add(new Entry(name, name, description, Type.FIELD));
        return this;
    }

    public MappingBuilder method(String obfuscated, String intermediary, String description) {
        this.entries.add(new Entry(obfuscated, intermediary, description, Type.METHOD));
        return this;
    }

    public MappingBuilder method(String name, String description) {
        this.entries.add(new Entry(name, name, description, Type.METHOD));
        return this;
    }

    public List<String> build() {
        List<String> list = new ArrayList<>();
        list.add(toString("CLASS", this.obfucated, this.intermediary, this.intermediary));

        entries.forEach(entry -> list.add(entry.toString(this.obfucated)));

        return list;
    }

    public static class Entry {
        private final String obfuscated;
        private final String intermediary;
        private final String description;
        private final Type type;

        public Entry(String obfuscated, String intermediary, String description, Type type) {
            this.obfuscated = obfuscated;
            this.intermediary = intermediary;
            this.description = description;
            this.type = type;
        }

        public String toString(String className) {
            return MappingBuilder.toString(this.type.name(), className, this.description, this.obfuscated, this.intermediary, this.intermediary);
        }
    }

    public enum Type {
        METHOD, FIELD;
    }
}
