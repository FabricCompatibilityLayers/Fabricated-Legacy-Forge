package fr.catcore.fabricatedforge.mixin.forgefml.entity.mob;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin extends MobEntity {
    @Shadow public abstract int getSize();

    public SlimeEntityMixin(World world) {
        super(world);
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public boolean canSpawn() {
        Chunk var1 = this.world.getChunkFromPos(MathHelper.floor(this.x), MathHelper.floor(this.z));
        return this.world.getLevelProperties().getGeneratorType().handleSlimeSpawnReduction(this.random, this.world) ? false : ((this.getSize() == 1 || this.world.field_4556 > 0) && this.random.nextInt(10) == 0 && var1.getRandom(987234911L).nextInt(10) == 0 && this.y < 40.0 ? super.canSpawn() : false);
    }
}
