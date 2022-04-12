package modloader;

public class EntityTrackerNonliving {
    public final BaseMod mod;
    public final Class entityClass;
    public final int id;
    public final int viewDistance;
    public final int updateFrequency;
    public final boolean trackMotion;

    public EntityTrackerNonliving(BaseMod mod, Class entityClass, int id, int viewDistance, int updateFrequency, boolean trackMotion) {
        this.mod = mod;
        this.entityClass = entityClass;
        this.id = id;
        this.viewDistance = viewDistance;
        this.updateFrequency = updateFrequency;
        this.trackMotion = trackMotion;
    }
}
