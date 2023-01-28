/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

import java.util.ArrayList;

public class Property {
    private String name;
    public String value;
    public String comment;
    public String[] valueList;
    private final boolean wasRead;
    private final boolean isList;
    private final Property.Type type;

    public Property() {
        this.wasRead = false;
        this.type = null;
        this.isList = false;
    }

    public Property(String name, String value, Property.Type type) {
        this(name, value, type, false);
    }

    Property(String name, String value, Property.Type type, boolean read) {
        this.setName(name);
        this.value = value;
        this.type = type;
        this.wasRead = read;
        this.isList = false;
    }

    public Property(String name, String[] values, Property.Type type) {
        this(name, values, type, false);
    }

    Property(String name, String[] values, Property.Type type, boolean read) {
        this.setName(name);
        this.type = type;
        this.valueList = values;
        this.wasRead = read;
        this.isList = true;
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

    public boolean isDoubleValue() {
        try {
            Double.parseDouble(this.value);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    public double getDouble(double _default) {
        try {
            return Double.parseDouble(this.value);
        } catch (NumberFormatException var4) {
            return _default;
        }
    }

    public int[] getIntList() {
        ArrayList<Integer> nums = new ArrayList();

        for(String value : this.valueList) {
            try {
                nums.add(Integer.parseInt(value));
            } catch (NumberFormatException var7) {
            }
        }

        int[] primitives = new int[nums.size()];

        for(int i = 0; i < nums.size(); ++i) {
            primitives[i] = nums.get(i);
        }

        return primitives;
    }

    public boolean isIntList() {
        for(String value : this.valueList) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException var6) {
                return false;
            }
        }

        return true;
    }

    public boolean[] getBooleanList() {
        ArrayList<Boolean> values = new ArrayList();

        for(String value : this.valueList) {
            try {
                values.add(Boolean.parseBoolean(value));
            } catch (NumberFormatException var7) {
            }
        }

        boolean[] primitives = new boolean[values.size()];

        for(int i = 0; i < values.size(); ++i) {
            primitives[i] = values.get(i);
        }

        return primitives;
    }

    public boolean isBooleanList() {
        for(String value : this.valueList) {
            if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
                return false;
            }
        }

        return true;
    }

    public double[] getDoubleList() {
        ArrayList<Double> values = new ArrayList();

        for(String value : this.valueList) {
            try {
                values.add(Double.parseDouble(value));
            } catch (NumberFormatException var7) {
            }
        }

        double[] primitives = new double[values.size()];

        for(int i = 0; i < values.size(); ++i) {
            primitives[i] = values.get(i);
        }

        return primitives;
    }

    public boolean isDoubleList() {
        for(String value : this.valueList) {
            try {
                Double.parseDouble(value);
            } catch (NumberFormatException var6) {
                return false;
            }
        }

        return true;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean wasRead() {
        return this.wasRead;
    }

    public Property.Type getType() {
        return this.type;
    }

    public boolean isList() {
        return this.isList;
    }

    public static enum Type {
        STRING,
        INTEGER,
        BOOLEAN,
        DOUBLE;

        private static Property.Type[] values = new Property.Type[]{STRING, INTEGER, BOOLEAN, DOUBLE};

        private Type() {
        }

        public static Property.Type tryParse(char id) {
            for(int x = 0; x < values.length; ++x) {
                if (values[x].getID() == id) {
                    return values[x];
                }
            }

            return STRING;
        }

        public char getID() {
            return this.name().charAt(0);
        }
    }
}
