package fr.catcore.fabricatedforge.mixin.forgefml.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.RailBlock;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.thrown.EggEntity;
import net.minecraft.entity.thrown.ExperienceBottleEntity;
import net.minecraft.entity.thrown.PotionEntity;
import net.minecraft.entity.thrown.SnowballEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin {

    @Shadow
    private static void method_299(World world, ItemStack itemStack, Random random, int i, int j, int k, double d, double e, double f) {
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    private static int method_298(DispenserBlockEntity par0TileEntityDispenser, World par1World, ItemStack par2ItemStack, Random par3Random, int par4, int par5, int par6, int par7, int par8, double par9, double par11, double par13) {
        float var15 = 1.1F;
        byte var16 = 6;
        int modDispense = GameRegistry.tryDispense(par1World, par4, par5, par6, par7, par8, par2ItemStack, par3Random, par9, par11, par13);
        if (modDispense > -1) {
            return modDispense;
        } else if (par2ItemStack.id == Item.ARROW.id) {
            AbstractArrowEntity var28 = new AbstractArrowEntity(par1World, par9, par11, par13);
            var28.method_3224((double)par7, 0.10000000149011612, (double)par8, var15, (float)var16);
            var28.pickup = 1;
            par1World.spawnEntity(var28);
            par1World.dispatchEvent(1002, par4, par5, par6, 0);
            return 1;
        } else if (par2ItemStack.id == Item.EGG.id) {
            EggEntity var29 = new EggEntity(par1World, par9, par11, par13);
            var29.method_3233((double)par7, 0.10000000149011612, (double)par8, var15, (float)var16);
            par1World.spawnEntity(var29);
            par1World.dispatchEvent(1002, par4, par5, par6, 0);
            return 1;
        } else if (par2ItemStack.id == Item.SNOWBALL.id) {
            SnowballEntity var24 = new SnowballEntity(par1World, par9, par11, par13);
            var24.method_3233((double)par7, 0.10000000149011612, (double)par8, var15, (float)var16);
            par1World.spawnEntity(var24);
            par1World.dispatchEvent(1002, par4, par5, par6, 0);
            return 1;
        } else if (par2ItemStack.id == Item.POTION.id && PotionItem.isThrowable(par2ItemStack.getMeta())) {
            PotionEntity var25 = new PotionEntity(par1World, par9, par11, par13, par2ItemStack.getMeta());
            var25.method_3233((double)par7, 0.10000000149011612, (double)par8, var15 * 1.25F, (float)var16 * 0.5F);
            par1World.spawnEntity(var25);
            par1World.dispatchEvent(1002, par4, par5, par6, 0);
            return 1;
        } else if (par2ItemStack.id == Item.XP_BOTTLE.id) {
            ExperienceBottleEntity var26 = new ExperienceBottleEntity(par1World, par9, par11, par13);
            var26.method_3233((double)par7, 0.10000000149011612, (double)par8, var15 * 1.25F, (float)var16 * 0.5F);
            par1World.spawnEntity(var26);
            par1World.dispatchEvent(1002, par4, par5, par6, 0);
            return 1;
        } else if (par2ItemStack.id == Item.SPAWN_EGG.id) {
            SpawnEggItem.method_3456(par1World, par2ItemStack.getMeta(), par9 + (double)par7 * 0.3, par11 - 0.3, par13 + (double)par8 * 0.3);
            par1World.dispatchEvent(1002, par4, par5, par6, 0);
            return 1;
        } else if (par2ItemStack.id == Item.FIREBALL.id) {
            SmallFireballEntity var27 = new SmallFireballEntity(par1World, par9 + (double)par7 * 0.3, par11, par13 + (double)par8 * 0.3, (double)par7 + par3Random.nextGaussian() * 0.05, par3Random.nextGaussian() * 0.05, (double)par8 + par3Random.nextGaussian() * 0.05);
            par1World.spawnEntity(var27);
            par1World.dispatchEvent(1009, par4, par5, par6, 0);
            return 1;
        } else if (par2ItemStack.id != Item.LAVA_BUCKET.id && par2ItemStack.id != Item.WATER_BUCKET.id) {
            if (par2ItemStack.id == Item.BUCKET.id) {
                int var21 = par4 + par7;
                int var18 = par6 + par8;
                Material var19 = par1World.getMaterial(var21, par5, var18);
                int var20 = par1World.getBlockData(var21, par5, var18);
                if (var19 == Material.WATER && var20 == 0) {
                    par1World.method_3690(var21, par5, var18, 0);
                    if (--par2ItemStack.count == 0) {
                        par2ItemStack.id = Item.WATER_BUCKET.id;
                        par2ItemStack.count = 1;
                    } else if (par0TileEntityDispenser.addToFirstFreeSlot(new ItemStack(Item.WATER_BUCKET)) < 0) {
                        method_299(par1World, new ItemStack(Item.WATER_BUCKET), par3Random, 6, par7, par8, par9, par11, par13);
                    }

                    return 2;
                } else if (var19 == Material.LAVA && var20 == 0) {
                    par1World.method_3690(var21, par5, var18, 0);
                    if (--par2ItemStack.count == 0) {
                        par2ItemStack.id = Item.LAVA_BUCKET.id;
                        par2ItemStack.count = 1;
                    } else if (par0TileEntityDispenser.addToFirstFreeSlot(new ItemStack(Item.LAVA_BUCKET)) < 0) {
                        method_299(par1World, new ItemStack(Item.LAVA_BUCKET), par3Random, 6, par7, par8, par9, par11, par13);
                    }

                    return 2;
                } else {
                    return 0;
                }
            } else if (par2ItemStack.getItem() instanceof MinecartItem) {
                par9 = (double)par4 + (par7 < 0 ? (double)par7 * 0.8 : (double)((float)par7 * 1.8F)) + (double)((float)Math.abs(par8) * 0.5F);
                par13 = (double)par6 + (par8 < 0 ? (double)par8 * 0.8 : (double)((float)par8 * 1.8F)) + (double)((float)Math.abs(par7) * 0.5F);
                if (RailBlock.method_355(par1World, par4 + par7, par5, par6 + par8)) {
                    par11 = (double)((float)par5 + 0.5F);
                } else {
                    if (!par1World.isAir(par4 + par7, par5, par6 + par8) || !RailBlock.method_355(par1World, par4 + par7, par5 - 1, par6 + par8)) {
                        return 0;
                    }

                    par11 = (double)((float)par5 - 0.5F);
                }

                AbstractMinecartEntity var22 = new AbstractMinecartEntity(par1World, par9, par11, par13, ((MinecartItem)par2ItemStack.getItem()).field_4381);
                par1World.spawnEntity(var22);
                par1World.dispatchEvent(1000, par4, par5, par6, 0);
                return 1;
            } else if (par2ItemStack.id == Item.BOAT.id) {
                par9 = (double)par4 + (par7 < 0 ? (double)par7 * 0.8 : (double)((float)par7 * 1.8F)) + (double)((float)Math.abs(par8) * 0.5F);
                par13 = (double)par6 + (par8 < 0 ? (double)par8 * 0.8 : (double)((float)par8 * 1.8F)) + (double)((float)Math.abs(par7) * 0.5F);
                if (par1World.getMaterial(par4 + par7, par5, par6 + par8) == Material.WATER) {
                    par11 = (double)((float)par5 + 1.0F);
                } else {
                    if (!par1World.isAir(par4 + par7, par5, par6 + par8) || par1World.getMaterial(par4 + par7, par5 - 1, par6 + par8) != Material.WATER) {
                        return 0;
                    }

                    par11 = (double)par5;
                }

                BoatEntity var23 = new BoatEntity(par1World, par9, par11, par13);
                par1World.spawnEntity(var23);
                par1World.dispatchEvent(1000, par4, par5, par6, 0);
                return 1;
            } else {
                return 0;
            }
        } else {
            BucketItem var17 = (BucketItem)par2ItemStack.getItem();
            if (var17.method_3312(par1World, (double)par4, (double)par5, (double)par6, par4 + par7, par5, par6 + par8)) {
                par2ItemStack.id = Item.BUCKET.id;
                par2ItemStack.count = 1;
                return 2;
            } else {
                return 0;
            }
        }
    }
}
