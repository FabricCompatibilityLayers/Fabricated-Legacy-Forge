package fr.catcore.fabricatedforge.mixin.forgefml.item;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin extends Item {
    protected SpawnEggItemMixin(int id) {
        super(id);
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public static boolean method_3456(World par0World, int par1, double par2, double par4, double par6) {
        if (!EntityType.field_3267.containsKey(par1)) {
            return false;
        } else {
            Entity var8 = EntityType.createInstanceFromRawId(par1, par0World);
            if (var8 != null) {
                var8.refreshPositionAndAngles(par2, par4, par6, par0World.random.nextFloat() * 360.0F, 0.0F);
                if (var8 instanceof VillagerEntity) {
                    VillagerEntity var9 = (VillagerEntity)var8;
                    VillagerRegistry.applyRandomTrade(var9, var9.method_2618());
                    par0World.spawnEntity(var9);
                    return true;
                }

                par0World.spawnEntity(var8);
                ((MobEntity)var8).playAmbientSound();
            }

            return var8 != null;
        }
    }
}
