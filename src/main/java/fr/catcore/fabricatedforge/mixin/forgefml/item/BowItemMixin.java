package fr.catcore.fabricatedforge.mixin.forgefml.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BowItem.class)
public class BowItemMixin extends Item {
    protected BowItemMixin(int id) {
        super(id);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void onUseStopped(ItemStack par1ItemStack, World par2World, PlayerEntity par3EntityPlayer, int par4) {
        int var6 = this.getMaxUseTime(par1ItemStack) - par4;
        ArrowLooseEvent event = new ArrowLooseEvent(par3EntityPlayer, par1ItemStack, var6);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            var6 = event.charge;
            boolean var5 = par3EntityPlayer.abilities.creativeMode || EnchantmentHelper.getLevel(Enchantment.INIFINITY.id, par1ItemStack) > 0;
            if (var5 || par3EntityPlayer.inventory.method_3139(Item.ARROW.id)) {
                float var7 = (float)var6 / 20.0F;
                var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;
                if ((double)var7 < 0.1) {
                    return;
                }

                if (var7 > 1.0F) {
                    var7 = 1.0F;
                }

                AbstractArrowEntity var8 = new AbstractArrowEntity(par2World, par3EntityPlayer, var7 * 2.0F);
                if (var7 == 1.0F) {
                    var8.setCritical(true);
                }

                int var9 = EnchantmentHelper.getLevel(Enchantment.POWER.id, par1ItemStack);
                if (var9 > 0) {
                    var8.setDamage(var8.getDamage() + (double)var9 * 0.5 + 0.5);
                }

                int var10 = EnchantmentHelper.getLevel(Enchantment.PUNCH.id, par1ItemStack);
                if (var10 > 0) {
                    var8.setPunch(var10);
                }

                if (EnchantmentHelper.getLevel(Enchantment.FLAME.id, par1ItemStack) > 0) {
                    var8.setOnFireFor(100);
                }

                par1ItemStack.method_3406(1, par3EntityPlayer);
                par2World.playSound(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (RANDOM.nextFloat() * 0.4F + 1.2F) + var7 * 0.5F);
                if (var5) {
                    var8.pickup = 2;
                } else {
                    par3EntityPlayer.inventory.method_3137(Item.ARROW.id);
                }

                if (!par2World.isClient) {
                    par2World.spawnEntity(var8);
                }
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public ItemStack onStartUse(ItemStack par1ItemStack, World par2World, PlayerEntity par3EntityPlayer) {
        ArrowNockEvent event = new ArrowNockEvent(par3EntityPlayer, par1ItemStack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return event.result;
        } else {
            if (par3EntityPlayer.abilities.creativeMode || par3EntityPlayer.inventory.method_3139(Item.ARROW.id)) {
                par3EntityPlayer.setUseItem(par1ItemStack, this.getMaxUseTime(par1ItemStack));
            }

            return par1ItemStack;
        }
    }
}
