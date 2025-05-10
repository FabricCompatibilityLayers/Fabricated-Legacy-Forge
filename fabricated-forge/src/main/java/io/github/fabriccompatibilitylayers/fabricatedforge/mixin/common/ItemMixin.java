package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.PrintStream;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemExtension {
    @Shadow public static Item[] itemsList;

    @Shadow public abstract float getStrVsBlock(ItemStack par1ItemStack, Block par2Block);

    @Shadow public abstract boolean isDamageable();

    @Shadow public abstract int getIconIndex(ItemStack par1ItemStack);

    @Shadow public abstract boolean requiresMultipleRenderPasses();

    @Shadow public abstract boolean hasContainerItem();

    @Shadow public abstract Item getContainerItem();

    /** FORGE: To disable repair recipes. */
    protected boolean canRepair = true;

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V", remap = false))
    private void forge$extraLogging(PrintStream instance, String x, Operation<Void> original, @Local(argsOnly = true) int par1) {
        original.call(instance, x + " item slot already occupied by " + itemsList[256 + par1] + " while adding " + this);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void forge$setDefaultTexture(int par1, CallbackInfo ci) {
        if (!((Object)this instanceof ItemBlock))
        {
            isDefaultTexture = "/gui/items.png".equals(getTextureFile());
        }
    }

    @Inject(method = "getMovingObjectPositionFromPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Vec3;addVector(DDD)Lnet/minecraft/src/Vec3;"))
    private void forge$getBlockReachDistance(World par1World, EntityPlayer par2EntityPlayer, boolean par3, CallbackInfoReturnable<MovingObjectPosition> cir,
                                             @Local(ordinal = 3) LocalDoubleRef var21Ref) {
        if (par2EntityPlayer instanceof EntityPlayerMP)
        {
            var21Ref.set(((EntityPlayerMP)par2EntityPlayer).theItemInWorldManager.getBlockReachDistance());
        }
    }

    /* =========================================================== FORGE START ===============================================================*/
    public boolean isDefaultTexture = true;
    private String currentTexture = "/gui/items.png";

    /**
     * Called when a player drops the item into the world,
     * returning false from this will prevent the item from
     * being removed from the players inventory and spawning
     * in the world
     *
     * @param player The player that dropped the item
     * @param item The item stack, before the item is removed.
     */
    @Override
    public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player)
    {
        return true;
    }

    /**
     * This is called when the item is used, before the block is activated.
     * @param stack The Item Stack
     * @param player The Player that used the item
     * @param world The Current World
     * @param x Target X Position
     * @param y Target Y Position
     * @param z Target Z Position
     * @param side The side of the target hit
     * @return Return true to prevent any further processing.
     */
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return onItemUseFirst(stack, player, world, x, y, z, side);
    }

    /**
     * See onItemUseFirst above, this is deprecated in favor of the more aware version.
     * Only here for compaibility.
     */
    @Deprecated
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side)
    {
        return false;
    }

    /**
     * Metadata-sensitive version of getStrVsBlock
     * @param itemstack The Item Stack
     * @param block The block the item is trying to break
     * @param metadata The items current metadata
     * @return The damage strength
     */
    @Override
    public float getStrVsBlock(ItemStack itemstack, Block block, int metadata)
    {
        return getStrVsBlock(itemstack, block);
    }

    /**
     * Called by CraftingManager to determine if an item is reparable.
     * @return True if reparable
     */
    @Override
    public boolean isRepairable()
    {
        return canRepair && isDamageable();
    }

    /**
     * Call to disable repair recipes.
     * @return The current Item instance
     */
    @Override
    public Item setNoRepair()
    {
        canRepair = false;
        return (Item) (Object) this;
    }

    /**
     * Called before a block is broken.  Return true to prevent default block harvesting.
     *
     * Note: In SMP, this is called on both client and server sides!
     *
     * @param itemstack The current ItemStack
     * @param X The X Position
     * @param Y The X Position
     * @param Z The X Position
     * @param player The Player that is wielding the item
     * @return True to prevent harvesting, false to continue as normal
     */
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player)
    {
        return false;
    }

    /**
     * Called each tick while using an item.
     * @param stack The Item being used
     * @param player The Player using the item
     * @param count The amount of time in tick the item has been used for continuously
     */
    @Override
    public void onUsingItemTick(ItemStack stack, EntityPlayer player, int count)
    {
    }

    /**
     * Called when the player Left Clicks (attacks) an entity.
     * Processed before damage is done, if return value is true further processing is canceled
     * and the entity is not attacked.
     *
     * @param stack The Item being used
     * @param player The player that is attacking
     * @param entity The entity being attacked
     * @return True to cancel the rest of the interaction.
     */
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        return false;
    }

    /**
     * Player, Render pass, and item usage sensitive version of getIconIndex.
     *
     * @param stack The item stack to get the icon for. (Usually this, and usingItem will be the same if usingItem is not null)
     * @param renderPass The pass to get the icon for, 0 is default.
     * @param player The player holding the item
     * @param usingItem The item the player is actively using. Can be null if not using anything.
     * @param useRemaining The ticks remaining for the active item.
     * @return The icon index
     */
    @Override
    public int getIconIndex(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        /*
         * Here is an example usage for Vanilla bows.
        if (usingItem != null && usingItem.getItem().shiftedIndex == Item.bow.shiftedIndex)
        {
            int k = usingItem.getMaxItemUseDuration() - useRemaining;
            if (k >= 18) return 133;
            if (k >  13) return 117;
            if (k >   0) return 101;
        }
         */
        return getIconIndex(stack);
    }

    /**
     * Returns the number of render passes/layers this item has.
     * Usually equates to ItemRenderer.renderItem being called for this many passes.
     * Does not get called unless requiresMultipleRenderPasses() is true;
     *
     * @param metadata The item's metadata
     * @return The number of passes to run.
     */
    @Override
    public int getRenderPasses(int metadata)
    {
        return requiresMultipleRenderPasses() ? 2 : 1;
    }

    /**
     * Grabs the current texture file used for this block
     */
    @Override
    public String getTextureFile()
    {
        if ((Object) this instanceof ItemBlock)
        {
            return ((BlockExtension) Block.blocksList[((ItemBlock) (Object) this).getBlockID()]).getTextureFile();
        }
        return currentTexture;
    }

    /**
     * Sets the current texture file for this item, used when rendering.
     * Default is "/gui/items.png"
     *
     * @param texture The texture file
     */
    @Override
    public void setTextureFile(String texture)
    {
        currentTexture = texture;
        isDefaultTexture = false;
    }

    /**
     * ItemStack sensitive version of getContainerItem.
     * Returns a full ItemStack instance of the result.
     *
     * @param itemStack The current ItemStack
     * @return The resulting ItemStack
     */
    @Override
    public ItemStack getContainerItemStack(ItemStack itemStack)
    {
        if (!hasContainerItem())
        {
            return null;
        }
        return new ItemStack(getContainerItem());
    }

    /**
     * Retrieves the normal 'lifespan' of this item when it is dropped on the ground as a EntityItem.
     * This is in ticks, standard result is 6000, or 5 mins.
     *
     * @param itemStack The current ItemStack
     * @param world The world the entity is in
     * @return The normal lifespan in ticks.
     */
    @Override
    public int getEntityLifespan(ItemStack itemStack, World world)
    {
        return 6000;
    }

    /**
     * Determines if this Item has a special entity for when they are in the world.
     * Is called when a EntityItem is spawned in the world, if true and Item#createCustomEntity
     * returns non null, the EntityItem will be destroyed and the new Entity will be added to the world.
     *
     * @param stack The current item stack
     * @return True of the item has a custom entity, If true, Item#createCustomEntity will be called
     */
    @Override
    public boolean hasCustomEntity(ItemStack stack)
    {
        return false;
    }

    /**
     * This function should return a new entity to replace the dropped item.
     * Returning null here will not kill the EntityItem and will leave it to function normally.
     * Called when the item it placed in a world.
     *
     * @param world The world object
     * @param location The EntityItem object, useful for getting the position of the entity
     * @param itemstack The current item stack
     * @return A new Entity object to spawn or null
     */
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack)
    {
        return null;
    }

    @Override
    public void setIsDefaultTexture(boolean isDefaultTexture) {
        this.isDefaultTexture = isDefaultTexture;
    }

    @Override
    public boolean isDefaultTexture() {
        return isDefaultTexture;
    }
}
