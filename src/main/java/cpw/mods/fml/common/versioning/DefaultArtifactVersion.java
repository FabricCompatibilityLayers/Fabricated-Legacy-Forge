package cpw.mods.fml.common.versioning;

public class DefaultArtifactVersion implements ArtifactVersion {
    private ComparableVersion comparableVersion;
    private String label;
    private boolean unbounded;
    private VersionRange range;

    public DefaultArtifactVersion(String versionNumber) {
        this.comparableVersion = new ComparableVersion(versionNumber);
        this.range = VersionRange.createFromVersion(versionNumber, this);
    }

    public DefaultArtifactVersion(String label, VersionRange range) {
        this.label = label;
        this.range = range;
    }

    public DefaultArtifactVersion(String label, String version) {
        this(version);
        this.label = label;
    }

    public DefaultArtifactVersion(String string, boolean unbounded) {
        this.label = string;
        this.unbounded = true;
    }

    public boolean equals(Object obj) {
        return ((DefaultArtifactVersion)obj).containsVersion(this);
    }

    public int compareTo(ArtifactVersion o) {
        return this.unbounded ? 0 : this.comparableVersion.compareTo(((DefaultArtifactVersion)o).comparableVersion);
    }

    public String getLabel() {
        return this.label;
    }

    public boolean containsVersion(ArtifactVersion source) {
        if (!source.getLabel().equals(this.getLabel())) {
            return false;
        } else if (this.unbounded) {
            return true;
        } else {
            return this.range != null ? this.range.containsVersion(source) : false;
        }
    }

    public String getVersionString() {
        return this.comparableVersion == null ? "unknown" : this.comparableVersion.toString();
    }

    public String getRangeString() {
        return this.range == null ? "any" : this.range.toString();
    }

    public String toString() {
        return this.label == null ? this.comparableVersion.toString() : this.label + (this.unbounded ? "" : "@" + this.range);
    }
}
