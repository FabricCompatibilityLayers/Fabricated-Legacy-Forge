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
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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

    @Shadow public Map icons;

    public MapStateMixin(String id) {
        super(id);
    }

    public int c;

    /**
     * @author Minecraft Forge
     * @reason none
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
     * @reason none
     */
    @Overwrite
    public void toNbt(NbtCompound par1NBTTagCompound) {
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
     * @reason none
     */
    @Overwrite
    public void update(PlayerEntity par1EntityPlayer, ItemStack par2ItemStack) {
        if (!this.updateTrackersByPlayer.containsKey(par1EntityPlayer)) {
            class_90 var3 = new class_90((MapState)(Object) this, par1EntityPlayer);
            this.updateTrackersByPlayer.put(par1EntityPlayer, var3);
            this.updateTrackers.add(var3);
        }

        if (!par1EntityPlayer.inventory.contains(par2ItemStack)) {
            this.icons.remove(par1EntityPlayer.getUsername());
        }

        for(int var5 = 0; var5 < this.updateTrackers.size(); ++var5) {
            class_90 var4 = (class_90)this.updateTrackers.get(var5);
            if (!var4.playerEntity.removed && (var4.playerEntity.inventory.contains(par2ItemStack) || par2ItemStack.isInItemFrame())) {
                if (!par2ItemStack.isInItemFrame() && var4.playerEntity.dimension == this.c) {
                    this.method_4126(
                            0, var4.playerEntity.world, var4.playerEntity.getUsername(), var4.playerEntity.x, var4.playerEntity.z, (double)var4.playerEntity.yaw
                    );
                }
            } else {
                this.updateTrackersByPlayer.remove(var4.playerEntity);
                this.updateTrackers.remove(var4);
            }
        }

        if (par2ItemStack.isInItemFrame()) {
            this.method_4126(
                    1,
                    par1EntityPlayer.world,
                    "frame-" + par2ItemStack.getItemFrame().id,
                    (double)par2ItemStack.getItemFrame().field_5329,
                    (double)par2ItemStack.getItemFrame().field_5331,
                    (double)(par2ItemStack.getItemFrame().field_5328 * 90)
            );
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_4126(int par1, World par2World, String par3Str, double par4, double par6, double par8) {
        int var10 = 1 << this.scale;
        float var11 = (float)(par4 - (double)this.xCenter) / (float)var10;
        float var12 = (float)(par6 - (double)this.zCenter) / (float)var10;
        byte var13 = (byte)((int)((double)(var11 * 2.0F) + 0.5));
        byte var14 = (byte)((int)((double)(var12 * 2.0F) + 0.5));
        byte var16 = 63;
        byte var15;
        if (var11 >= (float)(-var16) && var12 >= (float)(-var16) && var11 <= (float)var16 && var12 <= (float)var16) {
            par8 += par8 < 0.0 ? -8.0 : 8.0;
            var15 = (byte)((int)(par8 * 16.0 / 360.0));
            if (par2World.dimension.shouldMapSpin(par3Str, par4, par6, par8)) {
                int var17 = (int)(par2World.getLevelProperties().getTimeOfDay() / 10L);
                var15 = (byte)(var17 * var17 * 34187121 + var17 * 121 >> 15 & 15);
            }
        } else {
            if (Math.abs(var11) >= 320.0F || Math.abs(var12) >= 320.0F) {
                this.icons.remove(par3Str);
                return;
            }

            par1 = 6;
            var15 = 0;
            if (var11 <= (float)(-var16)) {
                var13 = (byte)((int)((double)(var16 * 2) + 2.5));
            }

            if (var12 <= (float)(-var16)) {
                var14 = (byte)((int)((double)(var16 * 2) + 2.5));
            }

            if (var11 >= (float)var16) {
                var13 = (byte)(var16 * 2 + 1);
            }

            if (var12 >= (float)var16) {
                var14 = (byte)(var16 * 2 + 1);
            }
        }

        this.icons.put(par3Str, new class_91((MapState)(Object) this, (byte)par1, var13, var14, var15));
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
