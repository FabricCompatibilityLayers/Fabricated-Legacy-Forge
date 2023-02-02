package fr.catcore.fabricatedforge.mixin.forgefml.world.explosion;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import net.minecraft.block.Block;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Shadow public float power;

    @Shadow private int field_4516;

    @Shadow private World world;

    @Shadow public Entity causingEntity;

    @Shadow public double x;

    @Shadow public double y;

    @Shadow public double z;

    @Shadow public List<Vec3i> affectedBlocks;

    @Shadow private Map<PlayerEntity, Vec3d> affectedPlayers;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void collectBlocksAndDamageEntities() {
        float var1 = this.power;
        HashSet var2 = new HashSet();

        for(int var3 = 0; var3 < this.field_4516; ++var3) {
            for(int var4 = 0; var4 < this.field_4516; ++var4) {
                for(int var5 = 0; var5 < this.field_4516; ++var5) {
                    if (var3 == 0 || var3 == this.field_4516 - 1 || var4 == 0 || var4 == this.field_4516 - 1 || var5 == 0 || var5 == this.field_4516 - 1) {
                        double var6 = (double)((float)var3 / ((float)this.field_4516 - 1.0F) * 2.0F - 1.0F);
                        double var8 = (double)((float)var4 / ((float)this.field_4516 - 1.0F) * 2.0F - 1.0F);
                        double var10 = (double)((float)var5 / ((float)this.field_4516 - 1.0F) * 2.0F - 1.0F);
                        double var12 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
                        var6 /= var12;
                        var8 /= var12;
                        var10 /= var12;
                        float var14 = this.power * (0.7F + this.world.random.nextFloat() * 0.6F);
                        double var15 = this.x;
                        double var17 = this.y;
                        double var19 = this.z;

                        for(float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * 0.75F) {
                            int var22 = MathHelper.floor(var15);
                            int var23 = MathHelper.floor(var17);
                            int var24 = MathHelper.floor(var19);
                            int var25 = this.world.getBlock(var22, var23, var24);
                            if (var25 > 0) {
                                Block var26 = Block.BLOCKS[var25];
                                float var27 = this.causingEntity != null
                                        ? this.causingEntity.method_4444((Explosion)(Object) this, var26, var22, var23, var24)
                                        : var26.getExplosionResistance(this.causingEntity, this.world, var22, var23, var24, this.x, this.y, this.z);
                                var14 -= (var27 + 0.3F) * var21;
                            }

                            if (var14 > 0.0F) {
                                var2.add(new Vec3i(var22, var23, var24));
                            }

                            var15 += var6 * (double)var21;
                            var17 += var8 * (double)var21;
                            var19 += var10 * (double)var21;
                        }
                    }
                }
            }
        }

        this.affectedBlocks.addAll(var2);
        this.power *= 2.0F;
        int var291 = MathHelper.floor(this.x - (double)this.power - 1.0);
        int var4 = MathHelper.floor(this.x + (double)this.power + 1.0);
        int var5 = MathHelper.floor(this.y - (double)this.power - 1.0);
        int var29x = MathHelper.floor(this.y + (double)this.power + 1.0);
        int var7 = MathHelper.floor(this.z - (double)this.power - 1.0);
        int var30 = MathHelper.floor(this.z + (double)this.power + 1.0);
        List var9 = this.world
                .getEntitiesIn(
                        this.causingEntity, Box.getLocalPool().getOrCreate((double)var291, (double)var5, (double)var7, (double)var4, (double)var29x, (double)var30)
                );
        Vec3d var31 = this.world.getVectorPool().getOrCreate(this.x, this.y, this.z);

        for(int var11 = 0; var11 < var9.size(); ++var11) {
            Entity var32 = (Entity)var9.get(var11);
            double var13 = var32.distanceTo(this.x, this.y, this.z) / (double)this.power;
            if (var13 <= 1.0) {
                double var15 = var32.x - this.x;
                double var17 = var32.y + (double)var32.getEyeHeight() - this.y;
                double var19 = var32.z - this.z;
                double var34 = (double)MathHelper.sqrt(var15 * var15 + var17 * var17 + var19 * var19);
                if (var34 != 0.0) {
                    var15 /= var34;
                    var17 /= var34;
                    var19 /= var34;
                    double var33 = (double)this.world.method_3612(var31, var32.boundingBox);
                    double var35 = (1.0 - var13) * var33;
                    var32.damage(DamageSource.field_3138, (int)((var35 * var35 + var35) / 2.0 * 8.0 * (double)this.power + 1.0));
                    double var36 = ProtectionEnchantment.method_4658(var32, var35);
                    var32.velocityX += var15 * var36;
                    var32.velocityY += var17 * var36;
                    var32.velocityZ += var19 * var36;
                    if (var32 instanceof PlayerEntity) {
                        this.affectedPlayers.put((PlayerEntity)var32, this.world.getVectorPool().getOrCreate(var15 * var35, var17 * var35, var19 * var35));
                    }
                }
            }
        }

        this.power = var1;
    }
}
