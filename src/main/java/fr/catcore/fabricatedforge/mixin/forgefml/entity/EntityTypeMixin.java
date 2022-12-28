package fr.catcore.fabricatedforge.mixin.forgefml.entity;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.logging.Level;

@Mixin(EntityType.class)
public class EntityTypeMixin {
    @Shadow public static Map<String, Class> NAME_CLASS_MAP;

    /**
     * @author forge
     * @reason yes
     */
    @Overwrite
    public static Entity createInstanceFromNbt(NbtCompound par0NBTTagCompound, World par1World) {
        Entity var2 = null;
        Class var3 = null;

        try {
            var3 = (Class)NAME_CLASS_MAP.get(par0NBTTagCompound.getString("id"));
            if (var3 != null) {
                var2 = (Entity)var3.getConstructor(World.class).newInstance(par1World);
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        if (var2 != null) {
            try {
                var2.fromNbt(par0NBTTagCompound);
            } catch (Exception var5) {
                FMLLog.log(
                        Level.SEVERE,
                        var5,
                        "An Entity %s(%s) has thrown an exception during loading, its state cannot be restored. Report this to the mod author",
                        new Object[]{par0NBTTagCompound.getString("id"), var3.getName()}
                );
                var2 = null;
            }
        } else {
            System.out.println("Skipping Entity with id " + par0NBTTagCompound.getString("id"));
        }

        return var2;
    }
}
