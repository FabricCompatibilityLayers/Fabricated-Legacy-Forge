/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.common.versioning;

import java.math.BigInteger;
import java.util.*;

public class ComparableVersion implements Comparable<ComparableVersion> {
    private String value;
    private String canonical;
    private ComparableVersion.ListItem items;

    public ComparableVersion(String version) {
        this.parseVersion(version);
    }

    public final void parseVersion(String version) {
        this.value = version;

        items = new ListItem();

        version = version.toLowerCase( Locale.ENGLISH );

        ListItem list = items;

        Stack<Item> stack = new Stack<Item>();
        stack.push( list );

        boolean isDigit = false;

        int startIndex = 0;

        for ( int i = 0; i < version.length(); i++ )
        {
            char c = version.charAt( i );

            if ( c == '.' )
            {
                if ( i == startIndex )
                {
                    list.add( IntegerItem.ZERO );
                }
                else
                {
                    list.add( parseItem( isDigit, version.substring( startIndex, i ) ) );
                }
                startIndex = i + 1;
            }
            else if ( c == '-' )
            {
                if ( i == startIndex )
                {
                    list.add( IntegerItem.ZERO );
                }
                else
                {
                    list.add( parseItem( isDigit, version.substring( startIndex, i ) ) );
                }
                startIndex = i + 1;

                if ( isDigit )
                {
                    list.normalize(); // 1.0-* = 1-*

                    if ( ( i + 1 < version.length() ) && Character.isDigit( version.charAt( i + 1 ) ) )
                    {
                        // new ListItem only if previous were digits and new char is a digit,
                        // ie need to differentiate only 1.1 from 1-1
                        list.add( list = new ListItem() );

                        stack.push( list );
                    }
                }
            }
            else if ( Character.isDigit( c ) )
            {
                if ( !isDigit && i > startIndex )
                {
                    list.add( new StringItem( version.substring( startIndex, i ), true ) );
                    startIndex = i;
                }

                isDigit = true;
            }
            else
            {
                if ( isDigit && i > startIndex )
                {
                    list.add( parseItem( true, version.substring( startIndex, i ) ) );
                    startIndex = i;
                }

                isDigit = false;
            }
        }

        if ( version.length() > startIndex )
        {
            list.add( parseItem( isDigit, version.substring( startIndex ) ) );
        }

        while ( !stack.isEmpty() )
        {
            list = (ListItem) stack.pop();
            list.normalize();
        }

        canonical = items.toString();
    }

    private static ComparableVersion.Item parseItem(boolean isDigit, String buf) {
        return (ComparableVersion.Item)(isDigit ? new ComparableVersion.IntegerItem(buf) : new ComparableVersion.StringItem(buf, false));
    }

    public int compareTo(ComparableVersion o) {
        return this.items.compareTo(o.items);
    }

    public String toString() {
        return this.value;
    }

    public boolean equals(Object o) {
        return o instanceof ComparableVersion && this.canonical.equals(((ComparableVersion)o).canonical);
    }

    public int hashCode() {
        return this.canonical.hashCode();
    }

    private static class ListItem extends ArrayList<ComparableVersion.Item> implements ComparableVersion.Item {
        private ListItem() {
        }

        public int getType() {
            return 2;
        }

        public boolean isNull() {
            return this.size() == 0;
        }

        void normalize() {
            ListIterator<ComparableVersion.Item> iterator = this.listIterator(this.size());

            while(iterator.hasPrevious()) {
                ComparableVersion.Item item = (ComparableVersion.Item)iterator.previous();
                if (!item.isNull()) {
                    break;
                }

                iterator.remove();
            }

        }

        public int compareTo(ComparableVersion.Item item) {
            if (item == null) {
                if (this.size() == 0) {
                    return 0;
                } else {
                    ComparableVersion.Item first = (ComparableVersion.Item)this.get(0);
                    return first.compareTo((ComparableVersion.Item)null);
                }
            } else {
                switch (item.getType()) {
                    case 0:
                        return -1;
                    case 1:
                        return 1;
                    case 2:
                        Iterator<ComparableVersion.Item> left = this.iterator();
                        Iterator<ComparableVersion.Item> right = ((ComparableVersion.ListItem)item).iterator();

                        int result;
                        do {
                            if (!left.hasNext() && !right.hasNext()) {
                                return 0;
                            }

                            ComparableVersion.Item l = left.hasNext() ? (ComparableVersion.Item)left.next() : null;
                            ComparableVersion.Item r = right.hasNext() ? (ComparableVersion.Item)right.next() : null;
                            result = l == null ? -1 * r.compareTo(l) : l.compareTo(r);
                        } while(result == 0);

                        return result;
                    default:
                        throw new RuntimeException("invalid item: " + item.getClass());
                }
            }
        }

        public String toString() {
            StringBuilder buffer = new StringBuilder("(");
            Iterator<ComparableVersion.Item> iter = this.iterator();

            while(iter.hasNext()) {
                buffer.append(iter.next());
                if (iter.hasNext()) {
                    buffer.append(',');
                }
            }

            buffer.append(')');
            return buffer.toString();
        }
    }

    private static class StringItem implements ComparableVersion.Item {
        private static final String[] QUALIFIERS = new String[]{"alpha", "beta", "milestone", "rc", "snapshot", "", "sp"};
        private static final List<String> _QUALIFIERS;
        private static final Properties ALIASES;
        private static final String RELEASE_VERSION_INDEX;
        private String value;

        public StringItem(String value, boolean followedByDigit) {
            if (followedByDigit && value.length() == 1) {
                switch (value.charAt(0)) {
                    case 'a':
                        value = "alpha";
                        break;
                    case 'b':
                        value = "beta";
                        break;
                    case 'm':
                        value = "milestone";
                }
            }

            this.value = ALIASES.getProperty(value, value);
        }

        public int getType() {
            return 1;
        }

        public boolean isNull() {
            return comparableQualifier(this.value).compareTo(RELEASE_VERSION_INDEX) == 0;
        }

        public static String comparableQualifier(String qualifier) {
            int i = _QUALIFIERS.indexOf(qualifier);
            return i == -1 ? _QUALIFIERS.size() + "-" + qualifier : String.valueOf(i);
        }

        public int compareTo(ComparableVersion.Item item) {
            if (item == null) {
                return comparableQualifier(this.value).compareTo(RELEASE_VERSION_INDEX);
            } else {
                switch (item.getType()) {
                    case 0:
                        return -1;
                    case 1:
                        return comparableQualifier(this.value).compareTo(comparableQualifier(((ComparableVersion.StringItem)item).value));
                    case 2:
                        return -1;
                    default:
                        throw new RuntimeException("invalid item: " + item.getClass());
                }
            }
        }

        public String toString() {
            return this.value;
        }

        static {
            _QUALIFIERS = Arrays.asList(QUALIFIERS);
            ALIASES = new Properties();
            ALIASES.put("ga", "");
            ALIASES.put("final", "");
            ALIASES.put("cr", "rc");
            RELEASE_VERSION_INDEX = String.valueOf(_QUALIFIERS.indexOf(""));
        }
    }

    private static class IntegerItem implements ComparableVersion.Item {
        private static final BigInteger BigInteger_ZERO = new BigInteger("0");
        private final BigInteger value;
        public static final ComparableVersion.IntegerItem ZERO = new ComparableVersion.IntegerItem();

        private IntegerItem() {
            this.value = BigInteger_ZERO;
        }

        public IntegerItem(String str) {
            this.value = new BigInteger(str);
        }

        public int getType() {
            return 0;
        }

        public boolean isNull() {
            return BigInteger_ZERO.equals(this.value);
        }

        public int compareTo(ComparableVersion.Item item) {
            if (item == null) {
                return BigInteger_ZERO.equals(this.value) ? 0 : 1;
            } else {
                switch (item.getType()) {
                    case 0:
                        return this.value.compareTo(((ComparableVersion.IntegerItem)item).value);
                    case 1:
                        return 1;
                    case 2:
                        return 1;
                    default:
                        throw new RuntimeException("invalid item: " + item.getClass());
                }
            }
        }

        public String toString() {
            return this.value.toString();
        }
    }

    private interface Item {
        int INTEGER_ITEM = 0;
        int STRING_ITEM = 1;
        int LIST_ITEM = 2;

        int compareTo(ComparableVersion.Item item);

        int getType();

        boolean isNull();
    }
}
