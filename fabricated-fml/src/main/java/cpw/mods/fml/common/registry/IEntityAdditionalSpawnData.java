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

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

/**
 * A interface for Entities that need extra information to be communicated
 * between the server and client when they are spawned.
 */
public interface IEntityAdditionalSpawnData
{
    /**
     * Called by the server when constructing the spawn packet.
     * Data should be added to the provided stream.
     *
     * @param data The packet data stream
     */
    public void writeSpawnData(ByteArrayDataOutput data);

    /**
     * Called by the client when it receives a Entity spawn packet.
     * Data should be read out of the stream in the same way as it was written.
     *
     * @param data The packet data stream
     */
    public void readSpawnData(ByteArrayDataInput data);
}
