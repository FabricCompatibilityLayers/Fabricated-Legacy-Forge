/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

public class Property {
    private String name;
    public String value;
    public String comment;
    private Property.Type type;

    public Property() {
    }

    public Property(String name, String value, Property.Type type) {
        this.setName(name);
        this.value = value;
        this.type = type;
    }

    public int getInt() {
        return this.getInt(-1);
    }

    public int getInt(int _default) {
        try {
            return Integer.parseInt(this.value);
        } catch (NumberFormatException var3) {
            return _default;
        }
    }

    public boolean isIntValue() {
        try {
            Integer.parseInt(this.value);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    public boolean getBoolean(boolean _default) {
        return this.isBooleanValue() ? Boolean.parseBoolean(this.value) : _default;
    }

    public boolean isBooleanValue() {
        return "true".equals(this.value.toLowerCase()) || "false".equals(this.value.toLowerCase());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static enum Type {
        STRING,
        INTEGER,
        BOOLEAN;

        private Type() {
        }
    }
}
