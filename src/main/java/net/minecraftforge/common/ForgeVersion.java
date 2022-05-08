package net.minecraftforge.common;

public class ForgeVersion {
    public static final int majorVersion = 4;
    public static final int minorVersion = 3;
    public static final int revisionVersion = 5;
    public static final int buildVersion = 318;

    public ForgeVersion() {
    }

    public static int getMajorVersion() {
        return 4;
    }

    public static int getMinorVersion() {
        return 3;
    }

    public static int getRevisionVersion() {
        return 5;
    }

    public static int getBuildVersion() {
        return 318;
    }

    public static String getVersion() {
        return String.format("%d.%d.%d.%d", 4, 3, 5, 318);
    }
}
