package fr.catcore.fabricatedforge.mixin.forgefml.item;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemProxy;
import fr.catcore.fabricatedforge.mixininterface.IBlock;
import fr.catcore.fabricatedforge.mixininterface.IItem;
import fr.catcore.fabricatedforge.mixininterface.IServerPlayerInteractionManager;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.itemgroup.ItemGroup;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemProxy, IItem {

    @Shadow @Final public int id;
    @Shadow public static Item[] ITEMS;


    @Shadow public abstract float getMiningSpeedMultiplier(ItemStack stack, Block block);

    @Shadow public abstract boolean isDamageable();

    @Shadow public abstract int method_3378(ItemStack itemStack);

    @Shadow public abstract boolean method_3397();

    @Shadow public abstract boolean isFood();

    @Shadow public abstract Item getRecipeRemainder();

    @Shadow public abstract ItemGroup getItemGroup();

    @Shadow public abstract int method_3369(int i, int j);

    protected boolean canRepair = true;
    public boolean isDefaultTexture = true;
    private String currentTexture = "/gui/items.png";

    @Inject(method = "<init>", at = @At("RETURN"))
    private void fmlCtr(int par1, CallbackInfo ci) {
        GameData.newItemAdded((Item)(Object) this);
        if (!((Object)this instanceof BlockItem)) {
            this.isDefaultTexture = "/gui/items.png".equals(this.getTextureFile());
        }
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"), index = 0)
    private String fmlCtr(String x) {
        return "CONFLICT @ " + this.id + " item slot already occupied by " + ITEMS[this.id] + " while adding " + this;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    protected BlockHitResult onHit(World par1World, PlayerEntity par2EntityPlayer, boolean par3) {
        float var4 = 1.0F;
        float var5 = par2EntityPlayer.prevPitch + (par2EntityPlayer.pitch - par2EntityPlayer.prevPitch) * var4;
        float var6 = par2EntityPlayer.prevYaw + (par2EntityPlayer.yaw - par2EntityPlayer.prevYaw) * var4;
        double var7 = par2EntityPlayer.prevX + (par2EntityPlayer.x - par2EntityPlayer.prevX) * (double)var4;
        double var9 = par2EntityPlayer.prevY + (par2EntityPlayer.y - par2EntityPlayer.prevY) * (double)var4 + 1.62 - (double)par2EntityPlayer.heightOffset;
        double var11 = par2EntityPlayer.prevZ + (par2EntityPlayer.z - par2EntityPlayer.prevZ) * (double)var4;
        Vec3d var13 = par1World.getVectorPool().getOrCreate(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float var15 = MathHelper.sin(-var6 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float var16 = -MathHelper.cos(-var5 * (float) (Math.PI / 180.0));
        float var17 = MathHelper.sin(-var5 * (float) (Math.PI / 180.0));
        float var18 = var15 * var16;
        float var20 = var14 * var16;
        double var21 = 5.0;
        if (par2EntityPlayer instanceof ServerPlayerEntity) {
            var21 = ((IServerPlayerInteractionManager)((ServerPlayerEntity)par2EntityPlayer).interactionManager).getBlockReachDistance();
        }

        Vec3d var23 = var13.method_613((double)var18 * var21, (double)var17 * var21, (double)var20 * var21);
        return par1World.rayTrace(var13, var23, par3, !par3);
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, PlayerEntity player) {
        return true;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public float getStrVsBlock(ItemStack itemstack, Block block, int metadata) {
        return this.getMiningSpeedMultiplier(itemstack, block);
    }

    @Override
    public boolean isRepairable() {
        return this.canRepair && this.isDamageable();
    }

    @Override
    public Item setNoRepair() {
        this.canRepair = false;
        return (Item)(Object) this;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, PlayerEntity player) {
        return false;
    }

    @Override
    public void onUsingItemTick(ItemStack stack, PlayerEntity player, int count) {
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        return false;
    }

    @Override
    public int getIconIndex(ItemStack stack, int renderPass, PlayerEntity player, ItemStack usingItem, int useRemaining) {
        return this.method_3378(stack);
    }

    @Override
    public int getRenderPasses(int metadata) {
        return this.method_3397() ? 2 : 1;
    }

    @Override
    public String getTextureFile() {
        return (Object) this instanceof BlockItem ? ((IBlock)Block.BLOCKS[((BlockItem)(Object)this).method_3464()]).getTextureFile() : this.currentTexture;
    }

    @Override
    public Item setTextureFile(String texture) {
        this.currentTexture = texture;
        this.isDefaultTexture = false;
        return (Item)(Object) this;
    }

    @Override
    public ItemStack getContainerItemStack(ItemStack itemStack) {
        return !this.isFood() ? null : new ItemStack(this.getRecipeRemainder());
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return 6000;
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return false;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return null;
    }

    @Override
    public ItemGroup[] getCreativeTabs() {
        return new ItemGroup[]{this.getItemGroup()};
    }

    @Override
    public float getSmeltingExperience(ItemStack item) {
        return -1.0F;
    }

    @Override
    public int getIconIndex(ItemStack stack, int pass) {
        return this.method_3369(stack.getData(), pass);
    }

    @Override
    public WeightedRandomChestContent getChestGenBase(ChestGenHooks chest, Random rnd, WeightedRandomChestContent original) {
        return (Object)this instanceof EnchantedBookItem ? ((EnchantedBookItem)(Object)this).method_4606(rnd, original.min, original.max, original.weight) : original;
    }

    @Override
    public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6) {
        return false;
    }

    @Override
    public boolean isDefaultTexture() {
        return this.isDefaultTexture;
    }

    @Override
    public void isDefaultTexture(boolean isDefaultTexture) {
        this.isDefaultTexture = isDefaultTexture;
    }
}
