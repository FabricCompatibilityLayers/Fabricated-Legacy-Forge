package fr.catcore.fabricatedforge.mixin.forgefml.item.map;

import fr.catcore.fabricatedforge.mixininterface.IMapState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.item.map.class_90;
import net.minecraft.item.map.class_91;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.world.PersistentState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Map;

@Mixin(MapState.class)
public abstract class MapStateMixin extends PersistentState implements IMapState {
    @Shadow public int xCenter;

    @Shadow public int zCenter;

    @Shadow public byte scale;

    @Shadow public byte[] colors;

    @Shadow private Map updateTrackersByPlayer;

    @Shadow public List updateTrackers;

    @Shadow public List field_208;

    @Shadow public int field_206;

    public MapStateMixin(String id) {
        super(id);
    }

    @Unique
    public int c;

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void fromNbt(NbtCompound par1NBTTagCompound) {
        NbtElement dimension = par1NBTTagCompound.get("dimension");
        if (dimension instanceof NbtByte) {
            this.c = ((NbtByte)dimension).value;
        } else {
            this.c = ((NbtInt)dimension).value;
        }

        this.xCenter = par1NBTTagCompound.getInt("xCenter");
        this.zCenter = par1NBTTagCompound.getInt("zCenter");
        this.scale = par1NBTTagCompound.getByte("scale");
        if (this.scale < 0) {
            this.scale = 0;
        }

        if (this.scale > 4) {
            this.scale = 4;
        }

        short var2 = par1NBTTagCompound.getShort("width");
        short var3 = par1NBTTagCompound.getShort("height");
        if (var2 == 128 && var3 == 128) {
            this.colors = par1NBTTagCompound.getByteArray("colors");
        } else {
            byte[] var4 = par1NBTTagCompound.getByteArray("colors");
            this.colors = new byte[16384];
            int var5 = (128 - var2) / 2;
            int var6 = (128 - var3) / 2;

            for(int var7 = 0; var7 < var3; ++var7) {
                int var8 = var7 + var6;
                if (var8 >= 0 || var8 < 128) {
                    for(int var9 = 0; var9 < var2; ++var9) {
                        int var10 = var9 + var5;
                        if (var10 >= 0 || var10 < 128) {
                            this.colors[var10 + var8 * 128] = var4[var9 + var7 * var2];
                        }
                    }
                }
            }
        }

    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void method_191(NbtCompound par1NBTTagCompound) {
        par1NBTTagCompound.putInt("dimension", this.c);
        par1NBTTagCompound.putInt("xCenter", this.xCenter);
        par1NBTTagCompound.putInt("zCenter", this.zCenter);
        par1NBTTagCompound.putByte("scale", this.scale);
        par1NBTTagCompound.putShort("width", (short)128);
        par1NBTTagCompound.putShort("height", (short)128);
        par1NBTTagCompound.putByteArray("colors", this.colors);
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void update(PlayerEntity par1EntityPlayer, ItemStack par2ItemStack) {
        if (!this.updateTrackersByPlayer.containsKey(par1EntityPlayer)) {
            class_90 var3 = new class_90((MapState)(Object) this, par1EntityPlayer);
            this.updateTrackersByPlayer.put(par1EntityPlayer, var3);
            this.updateTrackers.add(var3);
        }

        this.field_208.clear();

        for(int var14 = 0; var14 < this.updateTrackers.size(); ++var14) {
            class_90 var4 = (class_90)this.updateTrackers.get(var14);
            if (!var4.playerEntity.removed && var4.playerEntity.inventory.contains(par2ItemStack)) {
                float var5 = (float)(var4.playerEntity.x - (double)this.xCenter) / (float)(1 << this.scale);
                float var6 = (float)(var4.playerEntity.z - (double)this.zCenter) / (float)(1 << this.scale);
                byte var7 = 64;
                byte var8 = 64;
                if (var5 >= (float)(-var7) && var6 >= (float)(-var8) && var5 <= (float)var7 && var6 <= (float)var8) {
                    byte var9 = 0;
                    byte var10 = (byte)((int)((double)(var5 * 2.0F) + 0.5));
                    byte var11 = (byte)((int)((double)(var6 * 2.0F) + 0.5));
                    byte var12 = (byte)((int)((double)var4.playerEntity.yaw * 16.0 / 360.0));
                    if (this.c < 0) {
                        int var13 = this.field_206 / 10;
                        var12 = (byte)(var13 * var13 * 34187121 + var13 * 121 >> 15 & 15);
                    }

                    if (var4.playerEntity._dimension == this.c) {
                        this.field_208.add(new class_91((MapState)(Object) this, var9, var10, var11, var12));
                    }
                }
            } else {
                this.updateTrackersByPlayer.remove(var4.playerEntity);
                this.updateTrackers.remove(var4);
            }
        }

    }

    @Override
    public int getC() {
        return this.c;
    }

    @Override
    public void setC(int c) {
        this.c = c;
    }
}
