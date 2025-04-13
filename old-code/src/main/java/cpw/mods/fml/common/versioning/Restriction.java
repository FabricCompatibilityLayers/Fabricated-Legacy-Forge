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

public class Restriction {
    private final ArtifactVersion lowerBound;
    private final boolean lowerBoundInclusive;
    private final ArtifactVersion upperBound;
    private final boolean upperBoundInclusive;
    public static final Restriction EVERYTHING = new Restriction((ArtifactVersion)null, false, (ArtifactVersion)null, false);

    public Restriction(ArtifactVersion lowerBound, boolean lowerBoundInclusive, ArtifactVersion upperBound, boolean upperBoundInclusive) {
        this.lowerBound = lowerBound;
        this.lowerBoundInclusive = lowerBoundInclusive;
        this.upperBound = upperBound;
        this.upperBoundInclusive = upperBoundInclusive;
    }

    public ArtifactVersion getLowerBound() {
        return this.lowerBound;
    }

    public boolean isLowerBoundInclusive() {
        return this.lowerBoundInclusive;
    }

    public ArtifactVersion getUpperBound() {
        return this.upperBound;
    }

    public boolean isUpperBoundInclusive() {
        return this.upperBoundInclusive;
    }

    public boolean containsVersion(ArtifactVersion version) {
        int comparison;
        if (this.lowerBound != null) {
            comparison = this.lowerBound.compareTo(version);
            if (comparison == 0 && !this.lowerBoundInclusive) {
                return false;
            }

            if (comparison > 0) {
                return false;
            }
        }

        if (this.upperBound != null) {
            comparison = this.upperBound.compareTo(version);
            if (comparison == 0 && !this.upperBoundInclusive) {
                return false;
            }

            if (comparison < 0) {
                return false;
            }
        }

        return true;
    }

    public int hashCode() {
        int result = 13;
        if (this.lowerBound == null) {
            ++result;
        } else {
            result += this.lowerBound.hashCode();
        }

        result *= this.lowerBoundInclusive ? 1 : 2;
        if (this.upperBound == null) {
            result -= 3;
        } else {
            result -= this.upperBound.hashCode();
        }

        result *= this.upperBoundInclusive ? 2 : 3;
        return result;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof Restriction)) {
            return false;
        } else {
            Restriction restriction = (Restriction)other;
            if (this.lowerBound != null) {
                if (!this.lowerBound.equals(restriction.lowerBound)) {
                    return false;
                }
            } else if (restriction.lowerBound != null) {
                return false;
            }

            if (this.lowerBoundInclusive != restriction.lowerBoundInclusive) {
                return false;
            } else {
                if (this.upperBound != null) {
                    if (!this.upperBound.equals(restriction.upperBound)) {
                        return false;
                    }
                } else if (restriction.upperBound != null) {
                    return false;
                }

                return this.upperBoundInclusive == restriction.upperBoundInclusive;
            }
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(this.isLowerBoundInclusive() ? "[" : "(");
        if (this.getLowerBound() != null) {
            buf.append(this.getLowerBound().toString());
        }

        buf.append(",");
        if (this.getUpperBound() != null) {
            buf.append(this.getUpperBound().toString());
        }

        buf.append(this.isUpperBoundInclusive() ? "]" : ")");
        return buf.toString();
    }
}
