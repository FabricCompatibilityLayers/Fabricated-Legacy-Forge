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

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.common.registry.IThrowableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.Connection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.util.math.MathHelper;

import java.io.*;
import java.util.List;
import java.util.logging.Level;

public class EntitySpawnPacket extends FMLPacket {
    public int networkId;
    public int modEntityId;
    public int entityId;
    public double scaledX;
    public double scaledY;
    public double scaledZ;
    public float scaledYaw;
    public float scaledPitch;
    public float scaledHeadYaw;
    public List metadata;
    public int throwerId;
    public double speedScaledX;
    public double speedScaledY;
    public double speedScaledZ;
    public ByteArrayDataInput dataStream;
    public int rawX;
    public int rawY;
    public int rawZ;

    public EntitySpawnPacket() {
        super(Type.ENTITYSPAWN);
    }

    public byte[] generatePacket(Object... data) {
        EntityRegistry.EntityRegistration er = (EntityRegistry.EntityRegistration)data[0];
        Entity ent = (Entity)data[1];
        NetworkModHandler handler = (NetworkModHandler)data[2];
        ByteArrayDataOutput dat = ByteStreams.newDataOutput();
        dat.writeInt(handler.getNetworkId());
        dat.writeInt(er.getModEntityId());
        dat.writeInt(ent.id);
        dat.writeInt(MathHelper.floor(ent.x * 32.0));
        dat.writeInt(MathHelper.floor(ent.y * 32.0));
        dat.writeInt(MathHelper.floor(ent.z * 32.0));
        dat.writeByte((byte)((int)(ent.yaw * 256.0F / 360.0F)));
        dat.writeByte((byte)((int)(ent.pitch * 256.0F / 360.0F)));
        if (ent instanceof MobEntity) {
            dat.writeByte((byte)((int)(((MobEntity)ent).field_3315 * 256.0F / 360.0F)));
        } else {
            dat.writeByte(0);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            ent.getDataTracker().method_2696(dos);
        } catch (Exception var17) {
        }

        dat.write(bos.toByteArray());
        if (ent instanceof IThrowableEntity) {
            Entity owner = ((IThrowableEntity)ent).getThrower();
            dat.writeInt(owner == null ? ent.id : owner.id);
            double maxVel = 3.9;
            double mX = ent.velocityX;
            double mY = ent.velocityY;
            double mZ = ent.velocityZ;
            if (mX < -maxVel) {
                mX = -maxVel;
            }

            if (mY < -maxVel) {
                mY = -maxVel;
            }

            if (mZ < -maxVel) {
                mZ = -maxVel;
            }

            if (mX > maxVel) {
                mX = maxVel;
            }

            if (mY > maxVel) {
                mY = maxVel;
            }

            if (mZ > maxVel) {
                mZ = maxVel;
            }

            dat.writeInt((int)(mX * 8000.0));
            dat.writeInt((int)(mY * 8000.0));
            dat.writeInt((int)(mZ * 8000.0));
        } else {
            dat.writeInt(0);
        }

        if (ent instanceof IEntityAdditionalSpawnData) {
            ((IEntityAdditionalSpawnData)ent).writeSpawnData(dat);
        }

        return dat.toByteArray();
    }

    public FMLPacket consumePacket(byte[] data) {
        ByteArrayDataInput dat = ByteStreams.newDataInput(data);
        this.networkId = dat.readInt();
        this.modEntityId = dat.readInt();
        this.entityId = dat.readInt();
        this.rawX = dat.readInt();
        this.rawY = dat.readInt();
        this.rawZ = dat.readInt();
        this.scaledX = (double)this.rawX / 32.0;
        this.scaledY = (double)this.rawY / 32.0;
        this.scaledZ = (double)this.rawZ / 32.0;
        this.scaledYaw = (float)dat.readByte() * 360.0F / 256.0F;
        this.scaledPitch = (float)dat.readByte() * 360.0F / 256.0F;
        this.scaledHeadYaw = (float)dat.readByte() * 360.0F / 256.0F;
        ByteArrayInputStream bis = new ByteArrayInputStream(data, 27, data.length - 27);
        DataInputStream dis = new DataInputStream(bis);

        try {
            this.metadata = DataTracker.method_2695(dis);
        } catch (Exception var6) {
        }

        dat.skipBytes(data.length - bis.available() - 27);
        this.throwerId = dat.readInt();
        if (this.throwerId != 0) {
            this.speedScaledX = (double)dat.readInt() / 8000.0;
            this.speedScaledY = (double)dat.readInt() / 8000.0;
            this.speedScaledZ = (double)dat.readInt() / 8000.0;
        }

        this.dataStream = dat;
        return this;
    }

    public void execute(Connection network, FMLNetworkHandler handler, PacketListener netHandler, String userName) {
        NetworkModHandler nmh = handler.findNetworkModHandler(this.networkId);
        ModContainer mc = nmh.getContainer();
        EntityRegistry.EntityRegistration registration = EntityRegistry.instance().lookupModSpawn(mc, this.modEntityId);
        Class<? extends Entity> cls = registration.getEntityClass();
        if (cls == null) {
            FMLLog.log(Level.WARNING, "Missing mod entity information for %s : %d", mc.getModId(), this.modEntityId);
        } else {
            FMLCommonHandler.instance().spawnEntityIntoClientWorld(registration, this);
        }
    }
}
