/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.io.*;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Configuration {
    private static boolean[] configMarkers = new boolean[Item.ITEMS.length];
    private static final int ITEM_SHIFT = 256;
    private static final int MAX_BLOCKS = 4096;
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_BLOCK = "block";
    public static final String CATEGORY_ITEM = "item";
    public static final String ALLOWED_CHARS = "._-";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String CATEGORY_SPLITTER = ".";
    public static final String NEW_LINE = System.getProperty("line.separator");
    private static final Pattern CONFIG_START = Pattern.compile("START: \"([^\\\"]+)\"");
    private static final Pattern CONFIG_END = Pattern.compile("END: \"([^\\\"]+)\"");
    public static final CharMatcher allowedProperties = CharMatcher.JAVA_LETTER_OR_DIGIT.or(CharMatcher.anyOf("._-"));
    private static Configuration PARENT = null;
    File file;
    public Map<String, ConfigCategory> categories = new TreeMap();
    private Map<String, Configuration> children = new TreeMap();
    private boolean caseSensitiveCustomCategories;
    public String defaultEncoding = "UTF-8";
    private String fileName = null;
    public boolean isChild = false;

    public Configuration() {
    }

    public Configuration(File file) {
        this.file = file;
        String basePath = ((File)FMLInjectionData.data()[6]).getAbsolutePath().replace(File.separatorChar, '/').replace("/.", "");
        String path = file.getAbsolutePath().replace(File.separatorChar, '/').replace("/./", "/").replace(basePath, "");
        if (PARENT != null) {
            PARENT.setChild(path, this);
            this.isChild = true;
        } else {
            this.fileName = path;
            this.load();
        }
    }

    public Configuration(File file, boolean caseSensitiveCustomCategories) {
        this(file);
        this.caseSensitiveCustomCategories = caseSensitiveCustomCategories;
    }

    public Property getBlock(String key, int defaultID) {
        return this.getBlock("block", key, defaultID, null);
    }

    public Property getBlock(String key, int defaultID, String comment) {
        return this.getBlock("block", key, defaultID, comment);
    }

    public Property getBlock(String category, String key, int defaultID) {
        return this.getBlockInternal(category, key, defaultID, null, 256, Block.BLOCKS.length);
    }

    public Property getBlock(String category, String key, int defaultID, String comment) {
        return this.getBlockInternal(category, key, defaultID, comment, 256, Block.BLOCKS.length);
    }

    public Property getTerrainBlock(String category, String key, int defaultID, String comment) {
        return this.getBlockInternal(category, key, defaultID, comment, 0, 256);
    }

    private Property getBlockInternal(String category, String key, int defaultID, String comment, int lower, int upper) {
        Property prop = this.get(category, key, -1, comment);
        if (prop.getInt() != -1) {
            configMarkers[prop.getInt()] = true;
            return prop;
        } else {
            if (defaultID < lower) {
                FMLLog.warning(
                        "Mod attempted to get a block ID with a default in the Terrain Generation section, mod authors should make sure there defaults are above 256 unless explicitly needed for terrain generation. Most ores do not need to be below 256.",
                        new Object[0]
                );
                FMLLog.warning("Config \"%s\" Category: \"%s\" Key: \"%s\" Default: %d", new Object[]{this.fileName, category, key, defaultID});
                defaultID = upper - 1;
            }

            if (Block.BLOCKS[defaultID] == null && !configMarkers[defaultID]) {
                prop.value = Integer.toString(defaultID);
                configMarkers[defaultID] = true;
                return prop;
            } else {
                for(int j = upper - 1; j > 0; --j) {
                    if (Block.BLOCKS[j] == null && !configMarkers[j]) {
                        prop.value = Integer.toString(j);
                        configMarkers[j] = true;
                        return prop;
                    }
                }

                throw new RuntimeException("No more block ids available for " + key);
            }
        }
    }

    public Property getItem(String key, int defaultID) {
        return this.getItem("item", key, defaultID, null);
    }

    public Property getItem(String key, int defaultID, String comment) {
        return this.getItem("item", key, defaultID, comment);
    }

    public Property getItem(String category, String key, int defaultID) {
        return this.getItem(category, key, defaultID, null);
    }

    public Property getItem(String category, String key, int defaultID, String comment) {
        Property prop = this.get(category, key, -1, comment);
        int defaultShift = defaultID + 256;
        if (prop.getInt() != -1) {
            configMarkers[prop.getInt() + 256] = true;
            return prop;
        } else {
            if (defaultID < 3840) {
                FMLLog.warning(
                        "Mod attempted to get a item ID with a default value in the block ID section, mod authors should make sure there defaults are above %d unless explicitly needed so that all block ids are free to store blocks.",
                        new Object[]{3840}
                );
                FMLLog.warning("Config \"%s\" Category: \"%s\" Key: \"%s\" Default: %d", new Object[]{this.fileName, category, key, defaultID});
            }

            if (Item.ITEMS[defaultShift] == null && !configMarkers[defaultShift] && defaultShift > Block.BLOCKS.length) {
                prop.value = Integer.toString(defaultID);
                configMarkers[defaultShift] = true;
                return prop;
            } else {
                for(int x = Item.ITEMS.length - 1; x >= 256; --x) {
                    if (Item.ITEMS[x] == null && !configMarkers[x]) {
                        prop.value = Integer.toString(x - 256);
                        configMarkers[x] = true;
                        return prop;
                    }
                }

                throw new RuntimeException("No more item ids available for " + key);
            }
        }
    }

    public Property get(String category, String key, int defaultValue) {
        return this.get(category, key, defaultValue, null);
    }

    public Property get(String category, String key, int defaultValue, String comment) {
        Property prop = this.get(category, key, Integer.toString(defaultValue), comment, Property.Type.INTEGER);
        if (!prop.isIntValue()) {
            prop.value = Integer.toString(defaultValue);
        }

        return prop;
    }

    public Property get(String category, String key, boolean defaultValue) {
        return this.get(category, key, defaultValue, null);
    }

    public Property get(String category, String key, boolean defaultValue, String comment) {
        Property prop = this.get(category, key, Boolean.toString(defaultValue), comment, Property.Type.BOOLEAN);
        if (!prop.isBooleanValue()) {
            prop.value = Boolean.toString(defaultValue);
        }

        return prop;
    }

    public Property get(String category, String key, double defaultValue) {
        return this.get(category, key, defaultValue, null);
    }

    public Property get(String category, String key, double defaultValue, String comment) {
        Property prop = this.get(category, key, Double.toString(defaultValue), comment, Property.Type.DOUBLE);
        if (!prop.isDoubleValue()) {
            prop.value = Double.toString(defaultValue);
        }

        return prop;
    }

    public Property get(String category, String key, String defaultValue) {
        return this.get(category, key, defaultValue, null);
    }

    public Property get(String category, String key, String defaultValue, String comment) {
        return this.get(category, key, defaultValue, comment, Property.Type.STRING);
    }

    public Property get(String category, String key, String[] defaultValue) {
        return this.get(category, key, defaultValue, null);
    }

    public Property get(String category, String key, String[] defaultValue, String comment) {
        return this.get(category, key, defaultValue, comment, Property.Type.STRING);
    }

    public Property get(String category, String key, int[] defaultValue) {
        return this.get(category, key, defaultValue, null);
    }

    public Property get(String category, String key, int[] defaultValue, String comment) {
        String[] values = new String[defaultValue.length];

        for(int i = 0; i < defaultValue.length; ++i) {
            values[i] = Integer.toString(defaultValue[i]);
        }

        Property prop = this.get(category, key, values, comment, Property.Type.INTEGER);
        if (!prop.isIntList()) {
            prop.valueList = values;
        }

        return prop;
    }

    public Property get(String category, String key, double[] defaultValue) {
        return this.get(category, key, defaultValue, null);
    }

    public Property get(String category, String key, double[] defaultValue, String comment) {
        String[] values = new String[defaultValue.length];

        for(int i = 0; i < defaultValue.length; ++i) {
            values[i] = Double.toString(defaultValue[i]);
        }

        Property prop = this.get(category, key, values, comment, Property.Type.DOUBLE);
        if (!prop.isDoubleList()) {
            prop.valueList = values;
        }

        return prop;
    }

    public Property get(String category, String key, boolean[] defaultValue) {
        return this.get(category, key, defaultValue, null);
    }

    public Property get(String category, String key, boolean[] defaultValue, String comment) {
        String[] values = new String[defaultValue.length];

        for(int i = 0; i < defaultValue.length; ++i) {
            values[i] = Boolean.toString(defaultValue[i]);
        }

        Property prop = this.get(category, key, values, comment, Property.Type.BOOLEAN);
        if (!prop.isBooleanList()) {
            prop.valueList = values;
        }

        return prop;
    }

    public Property get(String category, String key, String defaultValue, String comment, Property.Type type) {
        if (!this.caseSensitiveCustomCategories) {
            category = category.toLowerCase(Locale.ENGLISH);
        }

        ConfigCategory cat = this.getCategory(category);
        if (cat.containsKey(key)) {
            Property prop = cat.get(key);
            if (prop.getType() == null) {
                prop = new Property(prop.getName(), prop.value, type);
                cat.set(key, prop);
            }

            prop.comment = comment;
            return prop;
        } else if (defaultValue != null) {
            Property prop = new Property(key, defaultValue, type);
            cat.set(key, prop);
            prop.comment = comment;
            return prop;
        } else {
            return null;
        }
    }

    public Property get(String category, String key, String[] defaultValue, String comment, Property.Type type) {
        if (!this.caseSensitiveCustomCategories) {
            category = category.toLowerCase(Locale.ENGLISH);
        }

        ConfigCategory cat = this.getCategory(category);
        if (cat.containsKey(key)) {
            Property prop = cat.get(key);
            if (prop.getType() == null) {
                prop = new Property(prop.getName(), prop.value, type);
                cat.set(key, prop);
            }

            prop.comment = comment;
            return prop;
        } else if (defaultValue != null) {
            Property prop = new Property(key, defaultValue, type);
            prop.comment = comment;
            cat.set(key, prop);
            return prop;
        } else {
            return null;
        }
    }

    public boolean hasCategory(String category) {
        return this.categories.get(category) != null;
    }

    public boolean hasKey(String category, String key) {
        ConfigCategory cat = (ConfigCategory)this.categories.get(category);
        return cat != null && cat.containsKey(key);
    }

    public void load() {
        if (PARENT == null || PARENT == this) {
            BufferedReader buffer = null;

            try {
                if (this.file.getParentFile() != null) {
                    this.file.getParentFile().mkdirs();
                }

                if (this.file.exists() || this.file.createNewFile()) {
                    if (this.file.canRead()) {
                        Configuration.UnicodeInputStreamReader input = new Configuration.UnicodeInputStreamReader(
                                new FileInputStream(this.file), this.defaultEncoding
                        );
                        this.defaultEncoding = input.getEncoding();
                        buffer = new BufferedReader(input);
                        ConfigCategory currentCat = null;
                        Property.Type type = null;
                        ArrayList<String> tmpList = null;
                        int lineNum = 0;
                        String name = null;

                        while(true) {
                            ++lineNum;
                            String line = buffer.readLine();
                            if (line == null) {
                                return;
                            }

                            Matcher start = CONFIG_START.matcher(line);
                            Matcher end = CONFIG_END.matcher(line);
                            if (start.matches()) {
                                this.fileName = start.group(1);
                                this.categories = new TreeMap();
                            } else if (end.matches()) {
                                this.fileName = end.group(1);
                                Configuration child = new Configuration();
                                child.categories = this.categories;
                                this.children.put(this.fileName, child);
                            } else {
                                int nameStart = -1;
                                int nameEnd = -1;
                                boolean skip = false;
                                boolean quoted = false;

                                for(int i = 0; i < line.length() && !skip; ++i) {
                                    if (Character.isLetterOrDigit(line.charAt(i)) || "._-".indexOf(line.charAt(i)) != -1 || quoted && line.charAt(i) != '"') {
                                        if (nameStart == -1) {
                                            nameStart = i;
                                        }

                                        nameEnd = i;
                                    } else if (!Character.isWhitespace(line.charAt(i))) {
                                        switch(line.charAt(i)) {
                                            case '"':
                                                if (quoted) {
                                                    quoted = false;
                                                }

                                                if (!quoted && nameStart == -1) {
                                                    quoted = true;
                                                }
                                                break;
                                            case '#':
                                                skip = true;
                                                break;
                                            case ':':
                                                type = Property.Type.tryParse(line.substring(nameStart, nameEnd + 1).charAt(0));
                                                nameEnd = -1;
                                                nameStart = -1;
                                                break;
                                            case '<':
                                                if (tmpList != null) {
                                                    throw new RuntimeException(String.format("Malformed list property \"%s:%d\"", this.fileName, lineNum));
                                                }

                                                name = line.substring(nameStart, nameEnd + 1);
                                                if (currentCat == null) {
                                                    throw new RuntimeException(String.format("'%s' has no scope in '%s:%d'", name, this.fileName, lineNum));
                                                }

                                                tmpList = new ArrayList();
                                                skip = true;
                                                break;
                                            case '=':
                                                name = line.substring(nameStart, nameEnd + 1);
                                                if (currentCat == null) {
                                                    throw new RuntimeException(String.format("'%s' has no scope in '%s:%d'", name, this.fileName, lineNum));
                                                }

                                                Property prop = new Property(name, line.substring(i + 1), type, true);
                                                i = line.length();
                                                currentCat.set(name, prop);
                                                break;
                                            case '>':
                                                if (tmpList == null) {
                                                    throw new RuntimeException(String.format("Malformed list property \"%s:%d\"", this.fileName, lineNum));
                                                }

                                                currentCat.set(name, new Property(name, (String[])tmpList.toArray(new String[tmpList.size()]), type));
                                                name = null;
                                                tmpList = null;
                                                type = null;
                                                break;
                                            case '{':
                                                name = line.substring(nameStart, nameEnd + 1);
                                                String qualifiedName = ConfigCategory.getQualifiedName(name, currentCat);
                                                ConfigCategory cat = (ConfigCategory)this.categories.get(qualifiedName);
                                                if (cat == null) {
                                                    currentCat = new ConfigCategory(name, currentCat);
                                                    this.categories.put(qualifiedName, currentCat);
                                                } else {
                                                    currentCat = cat;
                                                }

                                                name = null;
                                                break;
                                            case '}':
                                                if (currentCat == null) {
                                                    throw new RuntimeException(
                                                            String.format(
                                                                    "Config file corrupt, attepted to close to many categories '%s:%d'", this.fileName, lineNum
                                                            )
                                                    );
                                                }

                                                currentCat = currentCat.parent;
                                                break;
                                            default:
                                                throw new RuntimeException(
                                                        String.format("Unknown character '%s' in '%s:%d'", line.charAt(i), this.fileName, lineNum)
                                                );
                                        }
                                    }
                                }

                                if (quoted) {
                                    throw new RuntimeException(String.format("Unmatched quote in '%s:%d'", this.fileName, lineNum));
                                }

                                if (tmpList != null && !skip) {
                                    tmpList.add(line.trim());
                                }
                            }
                        }
                    }

                    return;
                }
            } catch (IOException var28) {
                var28.printStackTrace();
                return;
            } finally {
                if (buffer != null) {
                    try {
                        buffer.close();
                    } catch (IOException var27) {
                    }
                }
            }
        }
    }

    public void save() {
        if (PARENT != null && PARENT != this) {
            PARENT.save();
        } else {
            try {
                if (this.file.getParentFile() != null) {
                    this.file.getParentFile().mkdirs();
                }

                if (!this.file.exists() && !this.file.createNewFile()) {
                    return;
                }

                if (this.file.canWrite()) {
                    FileOutputStream fos = new FileOutputStream(this.file);
                    BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fos, this.defaultEncoding));
                    buffer.write("# Configuration file" + NEW_LINE);
                    buffer.write("# Generated on " + DateFormat.getInstance().format(new Date()) + NEW_LINE + NEW_LINE);
                    if (this.children.isEmpty()) {
                        this.save(buffer);
                    } else {
                        for(Map.Entry<String, Configuration> entry : this.children.entrySet()) {
                            buffer.write("START: \"" + (String)entry.getKey() + "\"" + NEW_LINE);
                            ((Configuration)entry.getValue()).save(buffer);
                            buffer.write("END: \"" + (String)entry.getKey() + "\"" + NEW_LINE + NEW_LINE);
                        }
                    }

                    buffer.close();
                    fos.close();
                }
            } catch (IOException var5) {
                var5.printStackTrace();
            }
        }
    }

    private void save(BufferedWriter out) throws IOException {
        Object[] categoryArray = this.categories.values().toArray();

        for(Object o : categoryArray) {
            if (o instanceof TreeMap) {
                TreeMap treeMap = (TreeMap)o;
                ConfigCategory converted = new ConfigCategory(this.file.getName());
                FMLLog.warning("Forge found a Treemap saved for Configuration file " + this.file.getName() + ", this is deprecated behaviour!", new Object[0]);

                for(Object key : treeMap.keySet()) {
                    FMLLog.warning(
                            "Converting Treemap to ConfigCategory, key: " + key + ", property value: " + ((Property)treeMap.get(key)).value, new Object[0]
                    );
                    converted.set((String)key, (Property)treeMap.get(key));
                }

                this.categories.values().remove(o);
                this.categories.put(this.file.getName(), converted);
            }
        }

        for(ConfigCategory cat : this.categories.values()) {
            if (!cat.isChild()) {
                cat.write(out, 0);
                out.newLine();
            }
        }
    }

    public ConfigCategory getCategory(String category) {
        ConfigCategory ret = (ConfigCategory)this.categories.get(category);
        if (ret == null) {
            if (category.contains(".")) {
                String[] hierarchy = category.split("\\.");
                ConfigCategory parent = (ConfigCategory)this.categories.get(hierarchy[0]);
                if (parent == null) {
                    parent = new ConfigCategory(hierarchy[0]);
                    this.categories.put(parent.getQualifiedName(), parent);
                }

                for(int i = 1; i < hierarchy.length; ++i) {
                    String name = ConfigCategory.getQualifiedName(hierarchy[i], parent);
                    ConfigCategory child = (ConfigCategory)this.categories.get(name);
                    if (child == null) {
                        child = new ConfigCategory(hierarchy[i], parent);
                        this.categories.put(name, child);
                    }

                    ret = child;
                    parent = child;
                }
            } else {
                ret = new ConfigCategory(category);
                this.categories.put(category, ret);
            }
        }

        return ret;
    }

    public void addCustomCategoryComment(String category, String comment) {
        if (!this.caseSensitiveCustomCategories) {
            category = category.toLowerCase(Locale.ENGLISH);
        }

        this.getCategory(category).setComment(comment);
    }

    private void setChild(String name, Configuration child) {
        if (!this.children.containsKey(name)) {
            this.children.put(name, child);
        } else {
            Configuration old = (Configuration)this.children.get(name);
            child.categories = old.categories;
            child.fileName = old.fileName;
        }
    }

    public static void enableGlobalConfig() {
        PARENT = new Configuration(new File(Loader.instance().getConfigDir(), "global.cfg"));
        PARENT.load();
    }

    static {
        Arrays.fill(configMarkers, false);
    }

    public static class UnicodeInputStreamReader extends Reader {
        private final InputStreamReader input;
        private final String defaultEnc;

        public UnicodeInputStreamReader(InputStream source, String encoding) throws IOException {
            this.defaultEnc = encoding;
            String enc = encoding;
            byte[] data = new byte[4];
            PushbackInputStream pbStream = new PushbackInputStream(source, data.length);
            int read = pbStream.read(data, 0, data.length);
            int size = 0;
            int bom16 = (data[0] & 255) << 8 | data[1] & 255;
            int bom24 = bom16 << 8 | data[2] & 255;
            int bom32 = bom24 << 8 | data[3] & 255;
            if (bom24 == 15711167) {
                enc = "UTF-8";
                size = 3;
            } else if (bom16 == 65279) {
                enc = "UTF-16BE";
                size = 2;
            } else if (bom16 == 65534) {
                enc = "UTF-16LE";
                size = 2;
            } else if (bom32 == 65279) {
                enc = "UTF-32BE";
                size = 4;
            } else if (bom32 == -131072) {
                enc = "UTF-32LE";
                size = 4;
            }

            if (size < read) {
                pbStream.unread(data, size, read - size);
            }

            this.input = new InputStreamReader(pbStream, enc);
        }

        public String getEncoding() {
            return this.input.getEncoding();
        }

        public int read(char[] cbuf, int off, int len) throws IOException {
            return this.input.read(cbuf, off, len);
        }

        public void close() throws IOException {
            this.input.close();
        }
    }
}
