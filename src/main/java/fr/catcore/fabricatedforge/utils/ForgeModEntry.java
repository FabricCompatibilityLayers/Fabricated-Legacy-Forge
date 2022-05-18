package fr.catcore.fabricatedforge.utils;

import net.fabricmc.loader.impl.lib.gson.JsonReader;
import net.fabricmc.loader.impl.lib.gson.JsonToken;
import net.fabricmc.loader.impl.metadata.LoaderModMetadata;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ForgeModEntry extends ModEntry {

    public final String modVersion;
    public final String modDescription;
    public final String modUrl;
    public final String modLogoFile;
    public final String modCredits;
    public final String parent;
    public final List<String> modAuthors;
    protected ForgeModEntry(String modName, String modId, File file, File original, String modVersion, String modDescription, String modUrl, String modLogoFile, String modCredits, String parent, List<String> modAuthors) {
        super(modName, modId, file, original);
        this.modVersion = modVersion;
        this.modDescription = modDescription;
        this.modUrl = modUrl;
        this.modLogoFile = modLogoFile;
        this.modCredits = modCredits;
        this.parent = parent;
        this.modAuthors = modAuthors;
    }

    private static int metaVersion = -1;

    protected static List<ForgeModEntry> parseModInfoFile(InputStream inputStream, File file, File original) {
        List<ForgeModEntry> list = new ArrayList<>();

        try (JsonReader reader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            if (reader.peek() == JsonToken.BEGIN_ARRAY) {
                metaVersion = -1;
                list.addAll(parseModInfos(reader, file, original));
            } else if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                reader.beginObject();

                while (reader.hasNext()) {
                    String key = reader.nextName();

                    if (key.equals("modinfoversion")) {
                        if (reader.peek() != JsonToken.NUMBER) {
                            reader.skipValue();
                            continue;
                        }

                        metaVersion = reader.nextInt();
                    } else if (key.equals("modlist")) {
                        if (reader.peek() != JsonToken.BEGIN_ARRAY) {
                            reader.skipValue();
                            continue;
                        }

                        list.addAll(parseModInfos(reader, file, original));
                    } else {
                        reader.skipValue();
                    }
                }

                reader.endObject();
            } else {
                reader.skipValue();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private static List<ForgeModEntry> parseModInfos(JsonReader reader, File file, File original) throws IOException {
        List<ForgeModEntry> list = new ArrayList<>();

        reader.beginArray();

        while (reader.hasNext()) {
            if (reader.peek() != JsonToken.BEGIN_OBJECT) {
                reader.skipValue();
                continue;
            }

            list.add(parseModInfo(reader, list.isEmpty(), file, original));
        }

        reader.endArray();

        return list;
    }

    private static ForgeModEntry parseModInfo(JsonReader reader, boolean first, File file, File original) throws IOException {
        String modName = "Unknown";
        String modId = "unknown";
        String modVersion = "1.0.0";
        String modDescription = "";
        String modUrl = "";
        String modLogoFile = "";
        String modCredits = "";
        String parent = "";
        List<String> modAuthors = new ArrayList<>();

        reader.beginObject();

        while (reader.hasNext()) {
            String key = reader.nextName();
            String valueString = "";

            switch (key) {
                case "name":
                    valueString = reader.nextString();
                    modName = valueString;
                    break;
                case "modid":
                    valueString = reader.nextString();
                    modId = valueString;
                    break;
                case "description":
                    valueString = reader.nextString();
                    modDescription = valueString;
                    break;
                case "url":
                    valueString = reader.nextString();
                    modUrl = valueString;
                    break;
                case "logoFile":
                    valueString = reader.nextString();
                    modLogoFile = valueString;
                    break;
                case "version":
                    valueString = reader.nextString();
                    modVersion = valueString;
                    break;
                case "credits":
                    valueString = reader.nextString();
                    modCredits = valueString;
                    break;
                case "parent":
                    valueString = reader.nextString();
                    parent = valueString;
                    break;
                case "authors":
                    if (reader.peek() != JsonToken.BEGIN_ARRAY) {
                        reader.skipValue();
                    } else {
                        reader.beginArray();

                        while (reader.hasNext()) {
                            if (reader.peek() != JsonToken.STRING) {
                                reader.skipValue();
                            } else {
                                modAuthors.add(reader.nextString());
                            }
                        }

                        reader.endArray();
                    }
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        return new ForgeModEntry(
                modName,
                modId,
                file,
                first ? original : null,
                modVersion,
                modDescription,
                modUrl,
                modLogoFile,
                modCredits,
                parent,
                modAuthors
        );
    }

    @Override
    LoaderModMetadata createModMetadata() {
        return new ForgeModMetadata(modId, modName, modVersion, modDescription, modLogoFile);
    }
}
