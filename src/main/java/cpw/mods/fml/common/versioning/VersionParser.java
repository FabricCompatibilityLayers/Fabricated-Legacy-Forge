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

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoaderException;

import java.util.List;
import java.util.logging.Level;

public class VersionParser {
    private static final Splitter SEPARATOR = Splitter.on('@').omitEmptyStrings().trimResults();

    public VersionParser() {
    }

    public static ArtifactVersion parseVersionReference(String labelledRef) {
        if (Strings.isNullOrEmpty(labelledRef)) {
            throw new RuntimeException(String.format("Empty reference %s", labelledRef));
        } else {
            List<String> parts = Lists.newArrayList(SEPARATOR.split(labelledRef));
            if (parts.size() > 2) {
                throw new RuntimeException(String.format("Invalid versioned reference %s", labelledRef));
            } else {
                return parts.size() == 1
                        ? new DefaultArtifactVersion((String)parts.get(0), true)
                        : new DefaultArtifactVersion((String)parts.get(0), parseRange((String)parts.get(1)));
            }
        }
    }

    public static boolean satisfies(ArtifactVersion target, ArtifactVersion source) {
        return target.containsVersion(source);
    }

    public static VersionRange parseRange(String range) {
        try {
            return VersionRange.createFromVersionSpec(range);
        } catch (InvalidVersionSpecificationException var2) {
            FMLLog.log(Level.SEVERE, var2, "Unable to parse a version range specification successfully %s", new Object[]{range});
            throw new LoaderException(var2);
        }
    }
}
