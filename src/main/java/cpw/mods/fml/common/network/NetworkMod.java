/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
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
package cpw.mods.fml.common.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface NetworkMod {
    boolean clientSideRequired() default false;

    boolean serverSideRequired() default false;

    String[] channels() default {};

    String versionBounds() default "";

    Class<? extends IPacketHandler> packetHandler() default NetworkMod.NULL.class;

    Class<? extends ITinyPacketHandler> tinyPacketHandler() default NetworkMod.NULL.class;

    Class<? extends IConnectionHandler> connectionHandler() default NetworkMod.NULL.class;

    NetworkMod.SidedPacketHandler clientPacketHandlerSpec() default @NetworkMod.SidedPacketHandler(
            channels = {},
            packetHandler = NetworkMod.NULL.class
    );

    NetworkMod.SidedPacketHandler serverPacketHandlerSpec() default @NetworkMod.SidedPacketHandler(
            channels = {},
            packetHandler = NetworkMod.NULL.class
    );

    public interface NULL extends IPacketHandler, IConnectionHandler, ITinyPacketHandler {
    }

    public @interface SidedPacketHandler {
        String[] channels();

        Class<? extends IPacketHandler> packetHandler();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public @interface VersionCheckHandler {
    }
}
