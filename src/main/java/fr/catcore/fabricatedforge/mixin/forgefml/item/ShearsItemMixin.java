package fr.catcore.fabricatedforge.mixin.forgefml.item;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;
import java.util.Random;

@Mixin(ShearsItem.class)
public class ShearsItemMixin extends Item {

    protected ShearsItemMixin(int id) {
        super(id);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_3356(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, MobEntity par7EntityLiving) {
        return par3 != Block.LEAVES.id
                && par3 != Block.COBWEB.id
                && par3 != Block.TALLGRASS.id
                && par3 != Block.VINE.id
                && par3 != Block.TRIPWIRE.id
                && !(Block.BLOCKS[par3] instanceof IShearable)
                ? super.method_3356(par1ItemStack, par2World, par3, par4, par5, par6, par7EntityLiving)
                : true;
    }

    @Override
    public boolean method_3353(ItemStack itemstack, MobEntity entity) {
        if (entity.world.isClient) {
            return false;
        } else if (!(entity instanceof IShearable)) {
            return false;
        } else {
            IShearable target = (IShearable)entity;
            if (target.isShearable(itemstack, entity.world, (int)entity.x, (int)entity.y, (int)entity.z)) {
                ArrayList<ItemStack> drops = target.onSheared(
                        itemstack, entity.world, (int)entity.x, (int)entity.y, (int)entity.z, EnchantmentHelper.getLevel(Enchantment.FORTUNE.id, itemstack)
                );
                Random rand = new Random();

                for(ItemStack stack : drops) {
                    ItemEntity ent = entity.dropItem(stack, 1.0F);
                    ent.velocityY += (double)(rand.nextFloat() * 0.05F);
                    ent.velocityX += (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F);
                    ent.velocityZ += (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F);
                }

                itemstack.method_3406(1, entity);
            }

            return true;
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, PlayerEntity player) {
        if (player.world.isClient) {
            return false;
        } else {
            int id = player.world.getBlock(x, y, z);
            if (Block.BLOCKS[id] instanceof IShearable) {
                IShearable target = (IShearable)Block.BLOCKS[id];
                if (target.isShearable(itemstack, player.world, x, y, z)) {
                    ArrayList<ItemStack> drops = target.onSheared(
                            itemstack, player.world, x, y, z, EnchantmentHelper.getLevel(Enchantment.FORTUNE.id, itemstack)
                    );
                    Random rand = new Random();

                    for(ItemStack stack : drops) {
                        float f = 0.7F;
                        double d = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5;
                        double d1 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5;
                        double d2 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5;
                        ItemEntity entityitem = new ItemEntity(player.world, (double)x + d, (double)y + d1, (double)z + d2, stack);
                        entityitem.pickupDelay = 10;
                        player.world.spawnEntity(entityitem);
                    }

                    itemstack.method_3406(1, player);
                    player.incrementStat(Stats.BLOCK_STATS[id], 1);
                }
            }

            return false;
        }
    }
}
