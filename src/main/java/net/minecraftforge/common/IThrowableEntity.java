package net.minecraftforge.common;

import net.minecraft.entity.Entity;

public interface IThrowableEntity {
    Entity getThrower();

    void setThrower(Entity arg);
}
