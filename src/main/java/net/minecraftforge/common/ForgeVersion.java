/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

public class ForgeVersion {
    public static final int majorVersion = 6;
    public static final int minorVersion = 4;
    public static final int revisionVersion = 2;
    public static final int buildVersion = 448;

    public ForgeVersion() {
    }

    public static int getMajorVersion() {
        return majorVersion;
    }

    public static int getMinorVersion() {
        return minorVersion;
    }

    public static int getRevisionVersion() {
        return revisionVersion;
    }

    public static int getBuildVersion() {
        return buildVersion;
    }

    public static String getVersion() {
        return String.format("%d.%d.%d.%d", majorVersion, minorVersion, revisionVersion, buildVersion);
    }
}
