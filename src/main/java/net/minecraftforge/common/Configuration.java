package net.minecraftforge.common;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
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
    private static boolean[] configBlocks = new boolean[Block.BLOCKS.length];
    private static boolean[] configItems = new boolean[Item.ITEMS.length];
    private static final int ITEM_SHIFT = 256;
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_BLOCK = "block";
    public static final String CATEGORY_ITEM = "item";
    public static final String ALLOWED_CHARS = "._-";
    public static final String DEFAULT_ENCODING = "UTF-8";
    private static final Pattern CONFIG_START = Pattern.compile("START: \"([^\\\"]+)\"");
    private static final Pattern CONFIG_END = Pattern.compile("END: \"([^\\\"]+)\"");
    private static final CharMatcher allowedProperties = CharMatcher.JAVA_LETTER_OR_DIGIT.or(CharMatcher.anyOf("._-"));
    private static Configuration PARENT = null;
    File file;
    public Map<String, Map<String, Property>> categories = new TreeMap();
    private Map<String, Configuration> children = new TreeMap();
    private Map<String, String> customCategoryComments = Maps.newHashMap();
    private boolean caseSensitiveCustomCategories;
    public String defaultEncoding = "UTF-8";
    private String fileName = null;
    public boolean isChild = false;

    public Configuration() {
    }

    public Configuration(File file) {
        this.file = file;
        String basePath = ((File) FMLInjectionData.data()[6]).getAbsolutePath().replace(File.separatorChar, '/').replace("/.", "");
        String path = file.getAbsolutePath().replace(File.separatorChar, '/').replace("/./", "/").replace(basePath, "");
        if (PARENT != null) {
            PARENT.setChild(path, this);
            this.isChild = true;
        } else {
            this.load();
        }
    }

    public Configuration(File file, boolean caseSensitiveCustomCategories) {
        this(file);
        this.caseSensitiveCustomCategories = caseSensitiveCustomCategories;
    }

    public Property getBlock(String key, int defaultID) {
        return this.getBlock("block", key, defaultID);
    }

    public Property getBlock(String category, String key, int defaultID) {
        Property prop = this.get(category, key, -1);
        if (prop.getInt() != -1) {
            configBlocks[prop.getInt()] = true;
            return prop;
        } else if (Block.BLOCKS[defaultID] == null && !configBlocks[defaultID]) {
            prop.value = Integer.toString(defaultID);
            configBlocks[defaultID] = true;
            return prop;
        } else {
            for(int j = configBlocks.length - 1; j > 0; --j) {
                if (Block.BLOCKS[j] == null && !configBlocks[j]) {
                    prop.value = Integer.toString(j);
                    configBlocks[j] = true;
                    return prop;
                }
            }

            throw new RuntimeException("No more block ids available for " + key);
        }
    }

    public Property getItem(String key, int defaultID) {
        return this.getItem("item", key, defaultID);
    }

    public Property getItem(String category, String key, int defaultID) {
        Property prop = this.get(category, key, -1);
        int defaultShift = defaultID + 256;
        if (prop.getInt() != -1) {
            configItems[prop.getInt() + 256] = true;
            return prop;
        } else if (Item.ITEMS[defaultShift] == null && !configItems[defaultShift] && defaultShift > Block.BLOCKS.length) {
            prop.value = Integer.toString(defaultID);
            configItems[defaultShift] = true;
            return prop;
        } else {
            for(int x = configItems.length - 1; x >= 256; --x) {
                if (Item.ITEMS[x] == null && !configItems[x]) {
                    prop.value = Integer.toString(x - 256);
                    configItems[x] = true;
                    return prop;
                }
            }

            throw new RuntimeException("No more item ids available for " + key);
        }
    }

    public Property get(String category, String key, int defaultValue) {
        Property prop = this.get(category, key, Integer.toString(defaultValue), Property.Type.INTEGER);
        if (!prop.isIntValue()) {
            prop.value = Integer.toString(defaultValue);
        }

        return prop;
    }

    public Property get(String category, String key, boolean defaultValue) {
        Property prop = this.get(category, key, Boolean.toString(defaultValue), Property.Type.BOOLEAN);
        if (!prop.isBooleanValue()) {
            prop.value = Boolean.toString(defaultValue);
        }

        return prop;
    }

    public Property get(String category, String key, String defaultValue) {
        return this.get(category, key, defaultValue, Property.Type.STRING);
    }

    public Property get(String category, String key, String defaultValue, Property.Type type) {
        if (!this.caseSensitiveCustomCategories) {
            category = category.toLowerCase(Locale.ENGLISH);
        }

        Map<String, Property> source = (Map)this.categories.get(category);
        if (source == null) {
            source = new TreeMap();
            this.categories.put(category, source);
        }

        if (source.containsKey(key)) {
            return (Property)source.get(key);
        } else if (defaultValue != null) {
            Property prop = new Property(key, defaultValue, type);
            source.put(key, prop);
            return prop;
        } else {
            return null;
        }
    }

    public boolean hasCategory(String category) {
        return this.categories.get(category) != null;
    }

    public boolean hasKey(String category, String key) {
        Map<String, Property> cat = (Map)this.categories.get(category);
        return cat != null && cat.get(key) != null;
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
                        Map<String, Property> currentMap = null;

                        while(true) {
                            String line = buffer.readLine();
                            if (line == null) {
                                return;
                            }

                            Matcher start = CONFIG_START.matcher(line);
                            Matcher end = CONFIG_END.matcher(line);
                            if (start.matches()) {
                                this.fileName = start.group(1);
                                this.categories = new TreeMap();
                                this.customCategoryComments = Maps.newHashMap();
                            } else if (end.matches()) {
                                this.fileName = end.group(1);
                                Configuration child = new Configuration();
                                child.categories = this.categories;
                                child.customCategoryComments = this.customCategoryComments;
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
                                            case '=':
                                                String propertyName = line.substring(nameStart, nameEnd + 1);
                                                if (currentMap == null) {
                                                    throw new RuntimeException("property " + propertyName + " has no scope");
                                                }

                                                Property prop = new Property();
                                                prop.setName(propertyName);
                                                prop.value = line.substring(i + 1);
                                                i = line.length();
                                                currentMap.put(propertyName, prop);
                                                break;
                                            case '{':
                                                String scopeName = line.substring(nameStart, nameEnd + 1);
                                                currentMap = (Map)this.categories.get(scopeName);
                                                if (currentMap == null) {
                                                    currentMap = new TreeMap();
                                                    this.categories.put(scopeName, currentMap);
                                                }
                                                break;
                                            case '}':
                                                currentMap = null;
                                                break;
                                            default:
                                                throw new RuntimeException("unknown character " + line.charAt(i));
                                        }
                                    }
                                }

                                if (quoted) {
                                    throw new RuntimeException("unmatched quote");
                                }
                            }
                        }
                    }

                    return;
                }
            } catch (IOException var24) {
                var24.printStackTrace();
                return;
            } finally {
                if (buffer != null) {
                    try {
                        buffer.close();
                    } catch (IOException var23) {
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
                    buffer.write("# Configuration file\r\n");
                    buffer.write("# Generated on " + DateFormat.getInstance().format(new Date()) + "\r\n");
                    buffer.write("\r\n");
                    if (this.children.isEmpty()) {
                        this.save(buffer);
                    } else {
                        for(Map.Entry<String, Configuration> entry : this.children.entrySet()) {
                            buffer.write("START: \"" + (String)entry.getKey() + "\"\r\n");
                            ((Configuration)entry.getValue()).save(buffer);
                            buffer.write("END: \"" + (String)entry.getKey() + "\"\r\n\r\n");
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
        for(Map.Entry<String, Map<String, Property>> category : this.categories.entrySet()) {
            out.write("####################\r\n");
            out.write("# " + (String)category.getKey() + " \r\n");
            if (this.customCategoryComments.containsKey(category.getKey())) {
                out.write("#===================\r\n");
                String comment = (String)this.customCategoryComments.get(category.getKey());
                Splitter splitter = Splitter.onPattern("\r?\n");

                for(String commentLine : splitter.split(comment)) {
                    out.write("# ");
                    out.write(commentLine + "\r\n");
                }
            }

            out.write("####################\r\n\r\n");
            String catKey = (String)category.getKey();
            if (!allowedProperties.matchesAllOf(catKey)) {
                catKey = '"' + catKey + '"';
            }

            out.write(catKey + " {\r\n");
            this.writeProperties(out, ((Map)category.getValue()).values());
            out.write("}\r\n\r\n");
        }
    }

    public void addCustomCategoryComment(String category, String comment) {
        if (!this.caseSensitiveCustomCategories) {
            category = category.toLowerCase(Locale.ENGLISH);
        }

        this.customCategoryComments.put(category, comment);
    }

    private void writeProperties(BufferedWriter buffer, Collection<Property> props) throws IOException {
        for(Property property : props) {
            if (property.comment != null) {
                Splitter splitter = Splitter.onPattern("\r?\n");

                for(String commentLine : splitter.split(property.comment)) {
                    buffer.write("   # " + commentLine + "\r\n");
                }
            }

            String propName = property.getName();
            if (!allowedProperties.matchesAllOf(propName)) {
                propName = '"' + propName + '"';
            }

            buffer.write("   " + propName + "=" + property.value);
            buffer.write("\r\n");
        }
    }

    private void setChild(String name, Configuration child) {
        if (!this.children.containsKey(name)) {
            this.children.put(name, child);
        } else {
            Configuration old = (Configuration)this.children.get(name);
            child.categories = old.categories;
            child.customCategoryComments = old.customCategoryComments;
            child.fileName = old.fileName;
        }
    }

    public static void enableGlobalConfig() {
        PARENT = new Configuration(new File(Loader.instance().getConfigDir(), "global.cfg"));
        PARENT.load();
    }

    static {
        Arrays.fill(configBlocks, false);
        Arrays.fill(configItems, false);
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
