package cpw.mods.fml.common.registry;

import net.minecraft.entity.Entity;

public interface IThrowableEntity {
    Entity getThrower();

    void setThrower(Entity arg);
}
