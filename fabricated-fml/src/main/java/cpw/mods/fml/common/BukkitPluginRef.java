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
package cpw.mods.fml.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declare a variable to be populated by a Bukkit Plugin proxy instance if the bukkit coremod
 * is available. It can only be applied to field typed as {@link BukkitProxy}
 * Generally it should be used in conjunction with {@link Mod#bukkitPlugin()} specifying the
 * plugin to load.
 *
 * @author cpw
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BukkitPluginRef
{
    /**
     * A reference (possibly version specific) to a Bukkit Plugin by name, using the name@versionbound
     * specification. If this is a bukkit enabled environment the field annotated by this
     * will be populated with a {@link BukkitProxy} instance if possible. This proxy will be gotten by
     * reflectively calling the "getModProxy" method on the bukkit plugin instance.
     * @return
     */
    String value();
}
