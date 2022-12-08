package net.minecraftforge.common;

public class ForgeVersion {
    public static final int majorVersion = 6;
    public static final int minorVersion = 0;
    public static final int revisionVersion = 0;
    public static final int buildVersion = 329;

    public ForgeVersion() {
    }

    public static int getMajorVersion() {
        return 6;
    }

    public static int getMinorVersion() {
        return 0;
    }

    public static int getRevisionVersion() {
        return 0;
    }

    public static int getBuildVersion() {
        return 329;
    }

    public static String getVersion() {
        return String.format("%d.%d.%d.%d", 6, 0, 0, 329);
    }
}
