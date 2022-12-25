package fr.catcore.fabricatedforge.mixin.forgefml.item;

import fr.catcore.fabricatedforge.mixininterface.IMapState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EmptyMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EmptyMapItem.class)
public class EmptyMapItemMixin {
    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public ItemStack onStartUse(ItemStack par1ItemStack, World par2World, PlayerEntity par3EntityPlayer) {
        ItemStack var4 = new ItemStack(Item.MAP, 1, par2World.getIntState("map"));
        String var5 = "map_" + var4.getData();
        MapState var6 = new MapState(var5);
        par2World.replaceState(var5, var6);
        var6.scale = 0;
        int var7 = 128 * (1 << var6.scale);
        var6.xCenter = (int)(Math.round(par3EntityPlayer.x / (double)var7) * (long)var7);
        var6.zCenter = (int)(Math.round(par3EntityPlayer.z / (double)var7) * (long)var7);
        ((IMapState)var6).setC((byte)par2World.dimension.dimensionType);
        var6.markDirty();
        --par1ItemStack.count;
        if (par1ItemStack.count <= 0) {
            return var4;
        } else {
            if (!par3EntityPlayer.inventory.insertStack(var4.copy())) {
                par3EntityPlayer.dropStack(var4);
            }

            return par1ItemStack;
        }
    }
}
