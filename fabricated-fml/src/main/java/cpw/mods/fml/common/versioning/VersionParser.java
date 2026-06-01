/*
 * The FML Forge Mod Loader suite. Copyright (C) 2012 cpw
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

import java.util.List;
import java.util.logging.Level;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoaderException;

/**
 * Parses version strings according to the specification here:
 * http://docs.codehaus.org/display/MAVEN/Versioning
 * and allows for comparison of versions based on that document.
 * Bounded version specifications are defined as
 * http://maven.apache.org/plugins/maven-enforcer-plugin/rules/versionRanges.html
 *
 * Borrows heavily from maven version range management code
 *
 * @author cpw
 *
 */
public class VersionParser
{
    private static final Splitter SEPARATOR = Splitter.on('@').omitEmptyStrings().trimResults();
    public static ArtifactVersion parseVersionReference(String labelledRef)
    {
        if (Strings.isNullOrEmpty(labelledRef))
        {
            throw new RuntimeException(String.format("Empty reference %s", labelledRef));
        }
        List<String> parts = Lists.newArrayList(SEPARATOR.split(labelledRef));
        if (parts.size()>2)
        {
            throw new RuntimeException(String.format("Invalid versioned reference %s", labelledRef));
        }
        if (parts.size()==1)
        {
            return new DefaultArtifactVersion(parts.get(0), true);
        }
        return new DefaultArtifactVersion(parts.get(0),parseRange(parts.get(1)));
    }

    public static boolean satisfies(ArtifactVersion target, ArtifactVersion source)
    {
        return target.containsVersion(source);
    }

    public static VersionRange parseRange(String range)
    {
        try
        {
            return VersionRange.createFromVersionSpec(range);
        }
        catch (InvalidVersionSpecificationException e)
        {
            FMLLog.log(Level.SEVERE, e, "Unable to parse a version range specification successfully %s", range);
            throw new LoaderException(e);
        }
    }
}
