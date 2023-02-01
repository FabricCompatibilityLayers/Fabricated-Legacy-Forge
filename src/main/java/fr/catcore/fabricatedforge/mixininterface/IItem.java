package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.itemgroup.ItemGroup;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

import java.util.Random;

public interface IItem {
    boolean onDroppedByPlayer(ItemStack item, PlayerEntity player);
    boolean onItemUseFirst(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ);
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
    int getIconIndex(ItemStack stack, int pass);

    WeightedRandomChestContent getChestGenBase(ChestGenHooks chest, Random rnd, WeightedRandomChestContent original);

    boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6);

    // Non Forge APIs
    boolean isDefaultTexture();
    void isDefaultTexture(boolean isDefaultTexture);
}
