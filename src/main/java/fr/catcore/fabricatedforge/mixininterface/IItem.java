package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.itemgroup.ItemGroup;
import net.minecraft.world.World;

public interface IItem {
    boolean onDroppedByPlayer(ItemStack item, PlayerEntity player);
    boolean onItemUseFirst(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ);
    @Deprecated
    boolean onItemUseFirst(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, int side);
    float getStrVsBlock(ItemStack itemstack, Block block, int metadata);
    boolean isRepairable();
    Item setNoRepair();
    boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, PlayerEntity player);
    void onUsingItemTick(ItemStack stack, PlayerEntity player, int count);
    boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity);
    int getIconIndex(ItemStack stack, int renderPass, PlayerEntity player, ItemStack usingItem, int useRemaining);
    int getRenderPasses(int metadata);
    String getTextureFile();
    Item setTextureFile(String texture);
    ItemStack getContainerItemStack(ItemStack itemStack);
    int getEntityLifespan(ItemStack itemStack, World world);
    boolean hasCustomEntity(ItemStack stack);
    Entity createEntity(World world, Entity location, ItemStack itemstack);
    ItemGroup[] getCreativeTabs();
    float getSmeltingExperience(ItemStack item);

    // Non Forge APIs
    boolean isDefaultTexture();
    void isDefaultTexture(boolean isDefaultTexture);
}
