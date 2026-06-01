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
package cpw.mods.fml.common.registry;

import net.minecraft.src.Entity;

/**
 * This interface should be implemented by an Entity that can be 'thrown', like snowballs.
 * This was created to mimic ModLoaderMP's 'owner' functionality.
 */
public interface IThrowableEntity
{
    /**
     * Gets the entity that threw/created this entity.
     * @return The owner instance, Null if none.
     */
    public Entity getThrower();

    /**
     * Sets the entity that threw/created this entity.
     * @param entity The new thrower/creator.
     */
    public void setThrower(Entity entity);
}
