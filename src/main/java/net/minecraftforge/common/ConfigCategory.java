package net.minecraftforge.common;

import com.google.common.base.Splitter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class ConfigCategory implements Map<String, Property> {
    private String name;
    private String comment;
    private ArrayList<ConfigCategory> children = new ArrayList();
    private Map<String, Property> properties = new TreeMap();
    public final ConfigCategory parent;

    public ConfigCategory(String name) {
        this(name, null);
    }

    public ConfigCategory(String name, ConfigCategory parent) {
        this.name = name;
        this.parent = parent;
        if (parent != null) {
            parent.children.add(this);
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ConfigCategory)) {
            return false;
        } else {
            ConfigCategory cat = (ConfigCategory)obj;
            return this.name.equals(cat.name) && this.children.equals(cat.children);
        }
    }

    public String getQualifiedName() {
        return getQualifiedName(this.name, this.parent);
    }

    public static String getQualifiedName(String name, ConfigCategory parent) {
        return parent == null ? name : parent.getQualifiedName() + "." + name;
    }

    public ConfigCategory getFirstParent() {
        return this.parent == null ? this : this.parent.getFirstParent();
    }

    public boolean isChild() {
        return this.parent != null;
    }

    public Map<String, Property> getValues() {
        return this.properties;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean containsKey(String key) {
        return this.properties.containsKey(key);
    }

    public Property get(String key) {
        return (Property)this.properties.get(key);
    }

    public void set(String key, Property value) {
        this.properties.put(key, value);
    }

    public void write(BufferedWriter out, int indent) throws IOException {
        String pad = this.getIndent(indent);
        out.write(pad + "####################" + Configuration.NEW_LINE);
        out.write(pad + "# " + this.name + Configuration.NEW_LINE);
        if (this.comment != null) {
            out.write(pad + "#===================" + Configuration.NEW_LINE);
            Splitter splitter = Splitter.onPattern("\r?\n");

            for(String line : splitter.split(this.comment)) {
                out.write(pad + "# " + line + Configuration.NEW_LINE);
            }
        }

        out.write(pad + "####################" + Configuration.NEW_LINE + Configuration.NEW_LINE);
        if (!Configuration.allowedProperties.matchesAllOf(this.name)) {
            this.name = '"' + this.name + '"';
        }

        out.write(pad + this.name + " {" + Configuration.NEW_LINE);
        pad = this.getIndent(indent + 1);
        Property[] props = (Property[])this.properties.values().toArray(new Property[this.properties.size()]);

        for(int x = 0; x < props.length; ++x) {
            Property prop = props[x];
            if (prop.comment != null) {
                if (x != 0) {
                    out.newLine();
                }

                Splitter splitter = Splitter.onPattern("\r?\n");

                for(String commentLine : splitter.split(prop.comment)) {
                    out.write(pad + "# " + commentLine + Configuration.NEW_LINE);
                }
            }

            String propName = prop.getName();
            if (!Configuration.allowedProperties.matchesAllOf(propName)) {
                propName = '"' + propName + '"';
            }

            if (!prop.isList()) {
                if (prop.getType() == null) {
                    out.write(String.format(pad + "%s=%s" + Configuration.NEW_LINE, propName, prop.value));
                } else {
                    out.write(String.format(pad + "%s:%s=%s" + Configuration.NEW_LINE, prop.getType().getID(), propName, prop.value));
                }
            } else {
                out.write(String.format(pad + "%s:%s <" + Configuration.NEW_LINE, prop.getType().getID(), propName));
                pad = this.getIndent(indent + 2);

                for(String line : prop.valueList) {
                    out.write(pad + line + Configuration.NEW_LINE);
                }

                out.write(this.getIndent(indent + 1) + " >" + Configuration.NEW_LINE);
            }
        }

        for(ConfigCategory child : this.children) {
            child.write(out, indent + 1);
        }

        out.write(this.getIndent(indent) + "}" + Configuration.NEW_LINE + Configuration.NEW_LINE);
    }

    private String getIndent(int indent) {
        StringBuilder buf = new StringBuilder("");

        for(int x = 0; x < indent; ++x) {
            buf.append("    ");
        }

        return buf.toString();
    }

    public int size() {
        return this.properties.size();
    }

    public boolean isEmpty() {
        return this.properties.isEmpty();
    }

    public boolean containsKey(Object key) {
        return this.properties.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.properties.containsValue(value);
    }

    public Property get(Object key) {
        return (Property)this.properties.get(key);
    }

    public Property put(String key, Property value) {
        return (Property)this.properties.put(key, value);
    }

    public Property remove(Object key) {
        return (Property)this.properties.remove(key);
    }

    public void putAll(Map<? extends String, ? extends Property> m) {
        this.properties.putAll(m);
    }

    public void clear() {
        this.properties.clear();
    }

    public Set<String> keySet() {
        return this.properties.keySet();
    }

    public Collection<Property> values() {
        return this.properties.values();
    }

    public Set<Entry<String, Property>> entrySet() {
        return this.properties.entrySet();
    }
}
