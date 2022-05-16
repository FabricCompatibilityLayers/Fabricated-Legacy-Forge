package fr.catcore.fabricatedforge.mixin.forgefml.entity.ai.goal;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.entity.ai.goal.VillagerMatingGoal;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VillagerMatingGoal.class)
public class VillagerMatingGoalMixin {

    @Shadow private World world;

    @Shadow private VillagerEntity mate;

    @Shadow private VillagerEntity villager;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    private void method_2759() {
        VillagerEntity var1 = new VillagerEntity(this.world);
        this.mate.setAge(6000);
        this.villager.setAge(6000);
        var1.setAge(-24000);
        VillagerRegistry.applyRandomTrade(var1, this.villager.method_2618());
        var1.refreshPositionAndAngles(this.villager.x, this.villager.y, this.villager.z, 0.0F, 0.0F);
        this.world.spawnEntity(var1);
        this.world.sendEntityStatus(var1, (byte)12);
    }
}
