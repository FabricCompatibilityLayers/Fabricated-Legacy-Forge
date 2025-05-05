package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import fr.catcore.cursedmixinextensions.annotations.Public;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraftforge.common.*;
import static net.minecraftforge.common.ForgeDirection.*;

import java.util.ArrayList;
import java.util.Random;

@Mixin(Block.class)
public abstract class BlockMixin implements BlockExtension {

    @Shadow @Final public int blockID;
    @Shadow @Final public static Block obsidian;
    @Shadow @Final public static Block whiteStone;
    @Shadow @Final public static Block bedrock;
    @Shadow @Final public static int[] lightOpacity;
    @Shadow @Final public static Block tilledField;
    @Shadow @Final public static Block sand;
    @Shadow @Final public static Block slowSand;
    @Shadow @Final public static BlockGrass grass;
    @Shadow @Final public static Block dirt;
    @Shadow @Final public static Block reed;
    @Shadow @Final public static Block cactus;

    @Shadow public abstract int getDamageValue(World par1World, int par2, int par3, int par4);

    @Shadow public abstract int idPicked(World par1World, int par2, int par3, int par4);

    @Shadow public abstract int getRenderBlockPass();

    @Shadow public abstract float getExplosionResistance(Entity par1Entity);

    @Shadow @Final public static Block stone;

    @Shadow
    public static boolean isNormalCube(int par0) {
        return false;
    }

    @Shadow public abstract boolean isOpaqueCube();

    @Shadow public abstract boolean renderAsNormalBlock();

    @Shadow protected abstract int damageDropped(int par1);

    @Shadow public abstract int idDropped(int par1, Random par2Random, int par3);

    @Shadow public abstract int quantityDroppedWithBonus(int par1, Random par2Random);

    @Shadow protected boolean isBlockContainer;
    @Shadow @Final public Material blockMaterial;
    @Shadow @Final public static int[] lightValue;

    @Shadow protected abstract void dropBlockAsItem_do(World par1World, int par2, int par3, int par4, ItemStack par5ItemStack);

    // Forge Fields
    @Public
    private static int[] blockFireSpreadSpeed = new int[4096];
    @Public
    private static int[] blockFlammability = new int[4096];
    protected String currentTexture = "/terrain.png";
    public boolean isDefaultTexture = true;

    @Inject(method = "<init>(ILnet/minecraft/src/Material;)V", at = @At("RETURN"))
    private void forge$setIsDefaultTexture(int par1, Material par2Material, CallbackInfo ci) {
        isDefaultTexture = (getTextureFile() != null && getTextureFile().equalsIgnoreCase("/terrain.png"));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    @Deprecated //Forge: New Metadata sensitive version.
    public boolean hasTileEntity()
    {
        return hasTileEntity(0);
    }

    @Environment(EnvType.CLIENT)
    @Definition(id = "lightValue", field = "Lnet/minecraft/src/Block;lightValue:[I")
    @Definition(id = "par1IBlockAccess", local = @Local(argsOnly = true, type = IBlockAccess.class))
    @Definition(id = "getBlockId", method = "Lnet/minecraft/src/IBlockAccess;getBlockId(III)I")
    @Definition(id = "par2", local = @Local(argsOnly = true, type = int.class, ordinal = 0))
    @Definition(id = "par3", local = @Local(argsOnly = true, type = int.class, ordinal = 1))
    @Definition(id = "par4", local = @Local(argsOnly = true, type = int.class, ordinal = 2))
    @Expression("lightValue[par1IBlockAccess.getBlockId(par2, par3, par4)]")
    @Redirect(method = {"getBlockBrightness", "getMixedBrightnessForBlock"}, at = @At("MIXINEXTRAS:EXPRESSION"))
    private int forge$getLightValue(int[] array, int index,
                                    @Local(argsOnly = true) IBlockAccess par1IBlockAccess,
                                    @Local(argsOnly = true, type = int.class, ordinal = 0) int par2,
                                    @Local(argsOnly = true, type = int.class, ordinal = 1) int par3,
                                    @Local(argsOnly = true, type = int.class, ordinal = 2) int par4) {
        return getLightValue(par1IBlockAccess, par2, par3, par4);
    }

    @Environment(EnvType.CLIENT)
    @Redirect(method = {"getBlockBrightness", "getMixedBrightnessForBlock"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/src/IBlockAccess;getBlockId(III)I"))
    private int forge$redirectGetBlockId(IBlockAccess instance, int i, int j, int k) {
        return 0;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public float getPlayerRelativeBlockHardness(EntityPlayer par1EntityPlayer, World par2World, int par3, int par4, int par5)
    {
        return ForgeHooks.blockStrength((Block)(Object) this, par1EntityPlayer, par2World, par3, par4, par5);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
        if (!par1World.isRemote) {
            ArrayList<ItemStack> items = getBlockDropped(par1World, par2, par3, par4, par5, par7);

            for (ItemStack item : items)
            {
                if (par1World.rand.nextFloat() <= par6)
                {
                    this.dropBlockAsItem_do(par1World, par2, par3, par4, item);
                }
            }
        }
    }

    @Redirect(method = "harvestBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;canSilkHarvest()Z"))
    private boolean forge$canSilkHarvest(Block instance, @Local(argsOnly = true) World par1World, @Local(argsOnly = true) EntityPlayer par2EntityPlayer, @Local(argsOnly = true, ordinal = 0) int par3, @Local(argsOnly = true, ordinal = 1) int par4, @Local(argsOnly = true, ordinal = 2) int par5, @Local(argsOnly = true, ordinal = 3) int par6) {
        return this.canSilkHarvest(par1World, par2EntityPlayer, par3, par4, par5, par6);
    }

    /* =================================================== FORGE START =====================================*/
    /**
     * Get a light value for this block, normal ranges are between 0 and 15
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return The light value
     */
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        return lightValue[blockID];
    }

    /**
     * Checks if a player or entity can use this block to 'climb' like a ladder.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block should act like a ladder
     */
    @Override
    public boolean isLadder(World world, int x, int y, int z)
    {
        return false;
    }

    /**
     * Return true if the block is a normal, solid cube.  This
     * determines indirect power state, entity ejection from blocks, and a few
     * others.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block is a full cube
     */
    @Override
    public boolean isBlockNormalCube(World world, int x, int y, int z)
    {
        return blockMaterial.isOpaque() && renderAsNormalBlock();
    }

    /**
     * Checks if the block is a solid face on the given side, used by placement logic.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @param side The side to check
     * @return True if the block is solid on the specified side.
     */
    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        int meta = world.getBlockMetadata(x, y, z);
        if ((Object) this instanceof BlockHalfSlab)
        {
            return (((meta & 8) == 8 && (side == UP)) || isOpaqueCube());
        }
        else if ((Object) this instanceof BlockFarmland)
        {
            return (side != DOWN && side != UP);
        }
        else if ((Object) this instanceof BlockStairs)
        {
            boolean flipped = ((meta & 4) != 0);
            return ((meta & 3) + side.ordinal() == 5) || (side == UP && flipped);
        }
        return isBlockNormalCube(world, x, y, z);
    }

    /**
     * Determines if a new block can be replace the space occupied by this one,
     * Used in the player's placement code to make the block act like water, and lava.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block is replaceable by another block
     */
    @Override
    public boolean isBlockReplaceable(World world, int x, int y, int z)
    {
        return false;
    }

    /**
     * Determines if this block should set fire and deal fire damage
     * to entities coming into contact with it.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block should deal damage
     */
    @Override
    public boolean isBlockBurning(World world, int x, int y, int z)
    {
        return false;
    }

    /**
     * Determines this block should be treated as an air block
     * by the rest of the code. This method is primarily
     * useful for creating pure logic-blocks that will be invisible
     * to the player and otherwise interact as air would.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block considered air
     */
    @Override
    public boolean isAirBlock(World world, int x, int y, int z)
    {
        return false;
    }

    /**
     * Determines if the player can harvest this block, obtaining it's drops when the block is destroyed.
     *
     * @param player The player damaging the block, may be null
     * @param meta The block's current metadata
     * @return True to spawn the drops
     */
    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta)
    {
        return ForgeHooks.canHarvestBlock((Block) (Object) this, player, meta);
    }

    /**
     * Called when a player removes a block.  This is responsible for
     * actually destroying the block, and the block is intact at time of call.
     * This is called regardless of whether the player can harvest the block or
     * not.
     *
     * Return true if the block is actually destroyed.
     *
     * Note: When used in multiplayer, this is called on both client and
     * server sides!
     *
     * @param world The current world
     * @param player The player damaging the block, may be null
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block is actually destroyed.
     */
    @Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        return world.setBlockWithNotify(x, y, z, 0);
    }

    /**
     * Called when a new CreativeContainer is opened, populate the list
     * with all of the items for this block you want a player in creative mode
     * to have access to.
     *
     * @param itemList The list of items to display on the creative inventory.
     */
    @Override
    public void addCreativeItems(ArrayList itemList)
    {
    }

    /**
     * Chance that fire will spread and consume this block.
     * 300 being a 100% chance, 0, being a 0% chance.
     *
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param metadata The blocks current metadata
     * @param face The face that the fire is coming from
     * @return A number ranging from 0 to 300 relating used to determine if the block will be consumed by fire
     */
    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
    {
        return blockFlammability[blockID];
    }

    /**
     * Called when fire is updating, checks if a block face can catch fire.
     *
     *
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param metadata The blocks current metadata
     * @param face The face that the fire is coming from
     * @return True if the face can be on fire, false otherwise.
     */
    @Override
    public boolean isFlammable(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
    {
        return getFlammability(world, x, y, z, metadata, face) > 0;
    }

    /**
     * Called when fire is updating on a neighbor block.
     * The higher the number returned, the faster fire will spread around this block.
     *
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param metadata The blocks current metadata
     * @param face The face that the fire is coming from
     * @return A number that is used to determine the speed of fire growth around the block
     */
    @Override
    public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face)
    {
        return blockFireSpreadSpeed[blockID];
    }

    /**
     * Currently only called by fire when it is on top of this block.
     * Returning true will prevent the fire from naturally dying during updating.
     * Also prevents firing from dying from rain.
     *
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param metadata The blocks current metadata
     * @param side The face that the fire is coming from
     * @return True if this block sustains fire, meaning it will never go out.
     */
    @Override
    public boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection side)
    {
        if (blockID == Block.netherrack.blockID && side == UP)
        {
            return true;
        }
        if ((world.provider instanceof WorldProviderEnd) && blockID == Block.bedrock.blockID && side == UP)
        {
            return true;
        }
        return false;
    }

    /**
     * Called by BlockFire to setup the burn values of vanilla blocks.
     * @param id The block id
     * @param encouragement How much the block encourages fire to spread
     * @param flammability how easy a block is to catch fire
     */
    @Public
    private static void setBurnProperties(int id, int encouragement, int flammability)
    {
        blockFireSpreadSpeed[id] = encouragement;
        blockFlammability[id] = flammability;
    }

    /**
     * Called throughout the code as a replacement for block instanceof BlockContainer
     * Moving this to the Block base class allows for mods that wish to extend vinella
     * blocks, and also want to have a tile entity on that block, may.
     *
     * Return true from this function to specify this block has a tile entity.
     *
     * @param metadata Metadata of the current block
     * @return True if block has a tile entity, false otherwise
     */
    @Override
    public boolean hasTileEntity(int metadata)
    {
        return isBlockContainer;
    }

    /**
     * Called throughout the code as a replacement for BlockContainer.getBlockEntity
     * Return the same thing you would from that function.
     * This will fall back to BlockContainer.getBlockEntity if this block is a BlockContainer.
     *
     * @param metadata The Metadata of the current block
     * @return A instance of a class extending TileEntity
     */
    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        if ((Object) this instanceof BlockContainer)
        {
            return ((BlockContainer) (Object) this).createNewTileEntity(world, metadata);
        }
        return null;
    }

    /**
     * Metadata and fortune sensitive version, this replaces the old (int meta, Random rand)
     * version in 1.1.
     *
     * @param meta Blocks Metadata
     * @param fortune Current item fortune level
     * @param random Random number generator
     * @return The number of items to drop
     */
    @Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
        return quantityDroppedWithBonus(fortune, random);
    }

    /**
     * This returns a complete list of items dropped from this block.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param metadata Current metadata
     * @param fortune Breakers fortune level
     * @return A ArrayList containing all items this block drops
     */
    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<>();

        int count = quantityDropped(metadata, fortune, world.rand);
        for(int i = 0; i < count; i++)
        {
            int id = idDropped(metadata, world.rand, 0);
            if (id > 0)
            {
                ret.add(new ItemStack(id, 1, damageDropped(metadata)));
            }
        }
        return ret;
    }

    /**
     * Return true from this function if the player with silk touch can harvest this block directly, and not it's normal drops.
     *
     * @param world The world
     * @param player The player doing the harvesting
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param metadata The metadata
     * @return True if the block can be directly harvested using silk touch
     */
    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
    {
        if ((Object) this instanceof BlockGlass || (Object) this instanceof BlockEnderChest)
        {
            return true;
        }
        return renderAsNormalBlock() && !hasTileEntity(metadata);
    }

    /**
     * Determines if a specified mob type can spawn on this block, returning false will
     * prevent any mob from spawning on the block.
     *
     * @param type The Mob Category Type
     * @param world The current world
     * @param x The X Position
     * @param y The Y Position
     * @param z The Z Position
     * @return True to allow a mob of the specified category to spawn, false to prevent it.
     */
    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        if ((Object) this instanceof BlockStep)
        {
            if (MinecraftForge.SPAWNER_ALLOW_ON_INVERTED)
            {
                return (((meta & 8) == 8) || isOpaqueCube());
            }
            else
            {
                return isNormalCube(this.blockID);
            }
        }
        else if ((Object) this instanceof BlockStairs)
        {
            if (MinecraftForge.SPAWNER_ALLOW_ON_INVERTED)
            {
                return ((meta & 4) != 0);
            }
            else
            {
                return isNormalCube(this.blockID);
            }
        }
        return isBlockSolidOnSide(world, x, y, z, UP);
    }

    /**
     * Determines if this block is classified as a Bed, Allowing
     * players to sleep in it, though the block has to specifically
     * perform the sleeping functionality in it's activated event.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param player The player or camera entity, null in some cases.
     * @return True to treat this as a bed
     */
    @Override
    public boolean isBed(World world, int x, int y, int z, EntityLiving player)
    {
        return blockID == Block.bed.blockID;
    }

    /**
     * Returns the position that the player is moved to upon
     * waking up, or respawning at the bed.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param player The player or camera entity, null in some cases.
     * @return The spawn position
     */
    @Override
    public ChunkCoordinates getBedSpawnPosition(World world, int x, int y, int z, EntityPlayer player)
    {
        return BlockBed.getNearestEmptyChunkCoordinates(world, x, y, z, 0);
    }

    /**
     * Called when a user either starts or stops sleeping in the bed.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param player The player or camera entity, null in some cases.
     * @param occupied True if we are occupying the bed, or false if they are stopping use of the bed
     */
    @Override
    public void setBedOccupied(World world, int x, int y, int z, EntityPlayer player, boolean occupied)
    {
        BlockBed.setBedOccupied(world,  x, y, z, occupied);
    }

    /**
     * Returns the direction of the block. Same values that
     * are returned by BlockDirectional
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return Bed direction
     */
    @Override
    public int getBedDirection(IBlockAccess world, int x, int y, int z)
    {
        return BlockBed.getDirection(world.getBlockMetadata(x,  y, z));
    }

    /**
     * Determines if the current block is the foot half of the bed.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return True if the current block is the foot side of a bed.
     */
    @Override
    public boolean isBedFoot(IBlockAccess world, int x, int y, int z)
    {
        return BlockBed.isBlockHeadOfBed(world.getBlockMetadata(x,  y, z));
    }

    /**
     * Called when a leaf should start its decay process.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     */
    @Override
    public void beginLeavesDecay(World world, int x, int y, int z){}

    /**
     * Determines if this block can prevent leaves connected to it from decaying.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return true if the presence this block can prevent leaves from decaying.
     */
    @Override
    public boolean canSustainLeaves(World world, int x, int y, int z)
    {
        return false;
    }

    /**
     * Determines if this block is considered a leaf block, used to apply the leaf decay and generation system.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return true if this block is considered leaves.
     */
    @Override
    public boolean isLeaves(World world, int x, int y, int z)
    {
        return false;
    }

    /**
     * Used during tree growth to determine if newly generated leaves can replace this block.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return true if this block can be replaced by growing leaves.
     */
    @Override
    public boolean canBeReplacedByLeaves(World world, int x, int y, int z)
    {
        return !Block.opaqueCubeLookup[this.blockID];
    }

    /**
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return  true if the block is wood (logs)
     */
    @Override
    public boolean isWood(World world, int x, int y, int z)
    {
        return false;
    }

    /**
     * Determines if the current block is replaceable by Ore veins during world generation.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return True to allow this block to be replaced by a ore
     */
    @Override
    public boolean isGenMineableReplaceable(World world, int x, int y, int z)
    {
        return blockID == stone.blockID;
    }

    /**
     * Grabs the current texture file used for this block
     */
    @Override
    public String getTextureFile()
    {
        return currentTexture;
    }

    /**
     * Sets the current texture file for this block, used when rendering.
     * Default is "/terrain.png"
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
     * Location sensitive version of getExplosionRestance
     *
     * @param par1Entity The entity that caused the explosion
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param explosionX Explosion source X Position
     * @param explosionY Explosion source X Position
     * @param explosionZ Explosion source X Position
     * @return The amount of the explosion absorbed.
     */
    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
        return getExplosionResistance(par1Entity);
    }

    /**
     * Determine if this block can make a redstone connection on the side provided,
     * Useful to control which sides are inputs and outputs for redstone wires.
     *
     * Side:
     *  -1: UP
     *   0: NORTH
     *   1: EAST
     *   2: SOUTH
     *   3: WEST
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param side The side that is trying to make the connection
     * @return True to make the connection
     */
    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        return Block.blocksList[blockID].canProvidePower() && side != -1;
    }

    /**
     * Determines if a torch can be placed on the top surface of this block.
     * Useful for creating your own block that torches can be on, such as fences.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return True to allow the torch to be placed
     */
    @Override
    public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
    {
        if (world.doesBlockHaveSolidTopSurface(x, y, z))
        {
            return true;
        }
        else
        {
            int id = world.getBlockId(x, y, z);
            return id == Block.fence.blockID || id == Block.netherFence.blockID || id == Block.glass.blockID;
        }
    }


    /**
     * Determines if this block should render in this pass.
     *
     * @param pass The pass in question
     * @return True to render
     */
    @Override
    public boolean canRenderInPass(int pass)
    {
        return pass == getRenderBlockPass();
    }

    /**
     * Called when a user uses the creative pick block button on this block
     *
     * @param target The full target the player is looking at
     * @return A ItemStack to add to the player's inventory, Null if nothing should be added.
     */
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        int id = idPicked(world, x, y, z);

        if (id == 0)
        {
            return null;
        }

        Item item = Item.itemsList[id];
        if (item == null)
        {
            return null;
        }

        return new ItemStack(id, 1, getDamageValue(world, x, y, z));
    }

    /**
     * Used by getTopSolidOrLiquidBlock while placing biome decorations, villages, etc
     * Also used to determine if the player can spawn on this block.
     *
     * @return False to disallow spawning
     */
    @Override
    public boolean isBlockFoliage(World world, int x, int y, int z)
    {
        return false;
    }

    /**
     * Spawn a digging particle effect in the world, this is a wrapper
     * around EffectRenderer.addBlockHitEffects to allow the block more
     * control over the particles. Useful when you have entirely different
     * texture sheets for different sides/locations in the world.
     *
     * @param world The current world
     * @param target The target the player is looking at {x/y/z/side/sub}
     * @param effectRenderer A reference to the current effect renderer.
     * @return True to prevent vanilla digging particles form spawning.
     */
    @Environment(EnvType.CLIENT)
    @Override
    public boolean addBlockHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer)
    {
        return false;
    }

    /**
     * Spawn particles for when the block is destroyed. Due to the nature
     * of how this is invoked, the x/y/z locations are not always guaranteed
     * to host your block. So be sure to do proper sanity checks before assuming
     * that the location is this block.
     *
     * @param world The current world
     * @param x X position to spawn the particle
     * @param y Y position to spawn the particle
     * @param z Z position to spawn the particle
     * @param meta The metadata for the block before it was destroyed.
     * @param effectRenderer A reference to the current effect renderer.
     * @return True to prevent vanilla break particles from spawning.
     */
    @Environment(EnvType.CLIENT)
    @Override
    public boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
    {
        return false;
    }

    /**
     * Determines if this block can support the passed in plant, allowing it to be planted and grow.
     * Some examples:
     *   Reeds check if its a reed, or if its sand/dirt/grass and adjacent to water
     *   Cacti checks if its a cacti, or if its sand
     *   Nether types check for soul sand
     *   Crops check for tilled soil
     *   Caves check if it's a colid surface
     *   Plains check if its grass or dirt
     *   Water check if its still water
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z position
     * @param direction The direction relative to the given position the plant wants to be, typically its UP
     * @param plant The plant that wants to check
     * @return True to allow the plant to be planted/stay.
     */
    @Override
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant)
    {
        int plantID = plant.getPlantID(world, x, y + 1, z);
        EnumPlantType plantType = plant.getPlantType(world, x, y + 1, z);

        if (plantID == cactus.blockID && blockID == cactus.blockID)
        {
            return true;
        }

        if (plantID == reed.blockID && blockID == reed.blockID)
        {
            return true;
        }

        if (plant instanceof BlockFlower && ((BlockFlower)plant).canThisPlantGrowOnThisBlockID(blockID))
        {
            return true;
        }

        switch (plantType)
        {
            case Desert: return blockID == sand.blockID;
            case Nether: return blockID == slowSand.blockID;
            case Crop:   return blockID == tilledField.blockID;
            case Cave:   return isBlockSolidOnSide(world, x, y, z, UP);
            case Plains: return blockID == grass.blockID || blockID == dirt.blockID;
            case Water:  return world.getBlockMaterial(x, y, z) == Material.water && world.getBlockMetadata(x, y, z) == 0;
            case Beach:
                boolean isBeach = (blockID == Block.grass.blockID || blockID == Block.dirt.blockID || blockID == Block.sand.blockID);
                boolean hasWater = (world.getBlockMaterial(x - 1, y - 1, z    ) == Material.water ||
                        world.getBlockMaterial(x + 1, y - 1, z    ) == Material.water ||
                        world.getBlockMaterial(x,     y - 1, z - 1) == Material.water ||
                        world.getBlockMaterial(x,     y - 1, z + 1) == Material.water);
                return isBeach && hasWater;
        }

        return false;
    }

    /**
     * Checks if this soil is fertile, typically this means that growth rates
     * of plants on this soil will be slightly sped up.
     * Only vanilla case is tilledField when it is within range of water.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z position
     * @return True if the soil should be considered fertile.
     */
    @Override
    public boolean isFertile(World world, int x, int y, int z)
    {
        if (blockID == tilledField.blockID)
        {
            return world.getBlockMetadata(x, y, z) > 0;
        }

        return false;
    }

    /**
     * Location aware and overrideable version of the lightOpacity array,
     * return the number to subtract from the light value when it passes through this block.
     *
     * This is not guaranteed to have the tile entity in place before this is called, so it is
     * Recommended that you have your tile entity call relight after being placed if you
     * rely on it for light info.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z position
     * @return The amount of light to block, 0 for air, 255 for fully opaque.
     */
    @Override
    public int getLightOpacity(World world, int x, int y, int z)
    {
        return lightOpacity[blockID];
    }

    /**
     * Determines if this block is destroyed when a ender dragon tries to fly through it.
     * The block will be set to 0, nothing will drop.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z position
     * @return True to allow the ender dragon to destroy this block
     */
    @Override
    public boolean canDragonDestroy(World world, int x, int y, int z)
    {
        return blockID != obsidian.blockID && blockID != whiteStone.blockID && blockID != bedrock.blockID;
    }
}
