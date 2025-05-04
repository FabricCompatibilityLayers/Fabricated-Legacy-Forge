package io.github.fabriccompatibilitylayers.fabricatedfml.compat.guava;

import com.google.common.base.Equivalence;

import java.io.Serializable;

public final class Equivalences {
    private Equivalences() {}

    /**
     * Returns an equivalence that delegates to {@link Object#equals} and {@link Object#hashCode}.
     * {@link Equivalence#equivalent} returns {@code true} if both values are null, or if neither
     * value is null and {@link Object#equals} returns {@code true}. {@link Equivalence#hash} returns
     * {@code 0} if passed a null value.
     *
     * @since 8.0 (present null-friendly behavior)
     * @since 4.0 (otherwise)
     */
    public static Equivalence<Object> equals() {
        return Equals.INSTANCE;
    }

    /**
     * Returns an equivalence that uses {@code ==} to compare values and {@link
     * System#identityHashCode(Object)} to compute the hash code.  {@link Equivalence#equivalent}
     * returns {@code true} if {@code a == b}, including in the case that a and b are both null.
     */
    public static Equivalence<Object> identity() {
        return Identity.INSTANCE;
    }

    private static final class Equals extends Equivalence<Object>
            implements Serializable {

        static final Equals INSTANCE = new Equals();

        @Override protected boolean doEquivalent(Object a, Object b) {
            return a.equals(b);
        }
        @Override public int doHash(Object o) {
            return o.hashCode();
        }

        private Object readResolve() {
            return INSTANCE;
        }
        private static final long serialVersionUID = 1;
    }

    private static final class Identity extends Equivalence<Object>
            implements Serializable {

        static final Identity INSTANCE = new Identity();

        @Override protected boolean doEquivalent(Object a, Object b) {
            return false;
        }

        @Override protected int doHash(Object o) {
            return System.identityHashCode(o);
        }

        private Object readResolve() {
            return INSTANCE;
        }
        private static final long serialVersionUID = 1;
    }
}
