package fr.catcore.fabricatedforge.mixin.forgefml.block.entity;

import cpw.mods.fml.common.FMLLog;
import fr.catcore.fabricatedforge.mixininterface.IBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Connection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.crash.provider.world.BlockEntityNameProvider;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.logging.Level;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements IBlockEntity {
    @Shadow private static Map<String, Class> stringClassMap;

    @Shadow public int x;

    @Shadow public int y;

    @Shadow public int z;

    @Shadow public Block block;

    @Shadow public int dataValue;

    /**
     * @author forge
     * @reason yes
     */
    @Overwrite
    public static BlockEntity createFromNbt(NbtCompound par0NBTTagCompound) {
        BlockEntity var1 = null;
        Class var2 = null;

        try {
            var2 = (Class)stringClassMap.get(par0NBTTagCompound.getString("id"));
            if (var2 != null) {
                var1 = (BlockEntity)var2.newInstance();
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        if (var1 != null) {
            try {
                var1.fromNbt(par0NBTTagCompound);
            } catch (Exception var4) {
                FMLLog.log(
                        Level.SEVERE,
                        var4,
                        "A TileEntity %s(%s) has thrown an exception during loading, its state cannot be restored. Report this to the mod author",
                        new Object[]{par0NBTTagCompound.getString("id"), var2.getName()}
                );
                var1 = null;
            }
        } else {
            System.out.println("Skipping TileEntity with id " + par0NBTTagCompound.getString("id"));
        }

        return var1;
    }

    /**
     * @author forge
     * @reason additional null check
     */
    @Overwrite
    public void populateCrashReport(CrashReportSection par1CrashReportCategory) {
        par1CrashReportCategory.add("Name", new BlockEntityNameProvider((BlockEntity)(Object) this));
        CrashReportSection.addBlock(par1CrashReportCategory, this.x, this.y, this.z, this.block != null ? this.block.id : 0, this.dataValue);
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void onDataPacket(Connection net, BlockEntityUpdateS2CPacket pkt) {
    }

    @Override
    public void onChunkUnload() {
    }

    @Override
    public boolean shouldRefresh(int oldID, int newID, int oldMeta, int newMeta, World world, int x, int y, int z) {
        return true;
    }
}
