package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.EntityPlayerExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLiving implements ICommandSender, EntityPlayerExtension {
    @Shadow private ItemStack itemInUse;

    @Shadow private int itemInUseCount;

    @Shadow public abstract void joinEntityItemWithWorld(EntityItem par1EntityItem);

    @Shadow public InventoryPlayer inventory;

    @Shadow public abstract ItemStack getCurrentEquippedItem();

    @Shadow public ChunkCoordinates playerLocation;

    @Shadow public EntityFishHook fishEntity;

    public EntityPlayerMixin(World par1World) {
        super(par1World);
    }

    @Inject(method = "onUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/src/EntityPlayer;itemInUseCount:I", ordinal = 0))
    private void forge$onUsingItemTick(CallbackInfo ci) {
        ((ItemExtension) itemInUse.getItem()).onUsingItemTick(itemInUse, (EntityPlayer) (Object) this, itemInUseCount);
    }

    @Inject(method = "onDeath", at = @At(value = "FIELD", target = "Lnet/minecraft/src/EntityPlayer;username:Ljava/lang/String;"))
    private void forge$captureDrops$setup(DamageSource par1DamageSource, CallbackInfo ci) {
        this.setCaptureDrops(true);
        this.getCapturedDrops().clear();
    }

    @Definition(id = "par1DamageSource", local = @Local(type = DamageSource.class, argsOnly = true))
    @Expression("par1DamageSource != null")
    @Inject(method = "onDeath", at = @At("MIXINEXTRAS:EXPRESSION"))
    private void forge$captureDrops$collect(DamageSource par1DamageSource, CallbackInfo ci) {
        this.setCaptureDrops(false);
        if (!worldObj.isRemote)
        {
            PlayerDropsEvent event = new PlayerDropsEvent((EntityPlayer) (Object) this, par1DamageSource, this.getCapturedDrops(), recentlyHit > 0);
            if (!MinecraftForge.EVENT_BUS.post(event))
            {
                for (EntityItem item : this.getCapturedDrops())
                {
                    joinEntityItemWithWorld(item);
                }
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public EntityItem dropOneItem()
    {
        ItemStack stack = inventory.getCurrentItem();
        if (stack == null)
        {
            return null;
        }
        if (((ItemExtension) stack.getItem()).onDroppedByPlayer(stack, (EntityPlayer) (Object) this))
        {
            return ForgeHooks.onPlayerTossEvent((EntityPlayer) (Object) this, inventory.decrStackSize(inventory.currentItem, 1));
        }
        return null;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public EntityItem dropPlayerItem(ItemStack par1ItemStack)
    {
        return ForgeHooks.onPlayerTossEvent((EntityPlayer) (Object) this, par1ItemStack);
    }

    @WrapWithCondition(method = "joinEntityItemWithWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;spawnEntityInWorld(Lnet/minecraft/src/Entity;)Z"))
    private boolean forge$captureDrops(World instance, Entity par1EntityItem) {
        if (this.isCapturingDrops()) {
            this.getCapturedDrops().add((EntityItem) par1EntityItem);
            return false;
        }

        return true;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public float getCurrentPlayerStrVsBlock(Block par1Block)
    {
        return getCurrentPlayerStrVsBlock(par1Block, 0);
    }

    @Override
    public float getCurrentPlayerStrVsBlock(Block par1Block, int meta) {
        ItemStack stack = inventory.getCurrentItem();
        float var2 = (stack == null ? 1.0F : ((ItemExtension) stack.getItem()).getStrVsBlock(stack, par1Block, meta));
        int var3 = EnchantmentHelper.getEfficiencyModifier(this.inventory);
        if (var3 > 0 && ForgeHooks.canHarvestBlock(par1Block, (EntityPlayer) (Object) this, meta)) {
            var2 += var3 * var3 + 1;
        }

        if (this.isPotionActive(Potion.digSpeed)) {
            var2 *= 1.0F + (this.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F;
        }

        if (this.isPotionActive(Potion.digSlowdown)) {
            var2 *= 1.0F - (this.getActivePotionEffect(Potion.digSlowdown).getAmplifier() + 1) * 0.2F;
        }

        if (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this.inventory)) {
            var2 /= 5.0F;
        }

        if (!this.onGround) {
            var2 /= 5.0F;
        }

        var2 = ForgeEventFactory.getBreakSpeed((EntityPlayer) (Object) this, par1Block, meta, var2);
        return (var2 < 0 ? 0 : var2);
    }

    @ModifyReturnValue(method = "canHarvestBlock", at = @At("RETURN"))
    private boolean forge$doPlayerHarvestCheck(boolean original,
                                               @Local(argsOnly = true) Block par1Block) {
        return ForgeEventFactory.doPlayerHarvestCheck((EntityPlayer) (Object) this, par1Block, original);
    }

    @Inject(method = "damageEntity", at = @At("HEAD"), cancellable = true)
    private void forge$postLivingHurtEvent(DamageSource par1DamageSource, int par2, CallbackInfo ci,
                                           @Local(argsOnly = true) LocalIntRef par2Ref) {
        LivingHurtEvent event = new LivingHurtEvent(this, par1DamageSource, par2);
        if (MinecraftForge.EVENT_BUS.post(event) || event.ammount == 0)
        {
            ci.cancel();
            return;
        }
        par2Ref.set(event.ammount);
    }

    @Redirect(method = "damageEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayer;applyArmorCalculations(Lnet/minecraft/src/DamageSource;I)I"))
    private int forge$ApplyArmor(EntityPlayer instance, DamageSource par1DamageSource, int par2) {
        return ISpecialArmor.ArmorProperties.ApplyArmor(instance, inventory.armorInventory, par1DamageSource, par2);
    }

    @Inject(method = "damageEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayer;applyPotionDamageCalculations(Lnet/minecraft/src/DamageSource;I)I"), cancellable = true)
    private void forge$check(DamageSource par1DamageSource, int par2, CallbackInfo ci) {
        if (par2 <= 0) {
            ci.cancel();
        }
    }

    @Inject(method = "interactWith", at = @At("HEAD"), cancellable = true)
    private void forge$postEntityInteractEvent(Entity par1Entity, CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftForge.EVENT_BUS.post(new EntityInteractEvent((EntityPlayer) (Object) this, par1Entity)))
        {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "destroyCurrentEquippedItem", at = @At("HEAD"))
    private void forge$getCurrentEquippedItem(CallbackInfo ci,
                                              @Share(value = "orig", namespace = "fabricated-forge") LocalRef<ItemStack> origRef) {
        origRef.set(getCurrentEquippedItem());
    }

    @Inject(method = "destroyCurrentEquippedItem", at = @At("RETURN"))
    private void forge$postPlayerDestroyItemEvent(CallbackInfo ci,
                                                  @Share(value = "orig", namespace = "fabricated-forge") LocalRef<ItemStack> origRef) {
        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent((EntityPlayer) (Object) this, origRef.get()));
    }

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At("HEAD"), cancellable = true)
    private void forge$AttackEntityEvent(Entity par1Entity, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new AttackEntityEvent((EntityPlayer) (Object) this, par1Entity)))
        {
            ci.cancel();
            return;
        }
        ItemStack stack = getCurrentEquippedItem();
        if (stack != null && ((ItemExtension) stack.getItem()).onLeftClickEntity(stack, (EntityPlayer) (Object) this, par1Entity))
        {
            ci.cancel();
        }
    }

    @Inject(method = "sleepInBedAt", at = @At("HEAD"), cancellable = true)
    private void forge$postPlayerSleepInBedEvent(int par1, int par2, int par3, CallbackInfoReturnable<EnumStatus> cir) {
        PlayerSleepInBedEvent event = new PlayerSleepInBedEvent((EntityPlayer) (Object) this, par1, par2, par3);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.result != null)
        {
            cir.setReturnValue(event.result);
        }
    }

    @WrapOperation(method = "sleepInBedAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockBed;getDirection(I)I"))
    private int forge$getBedDirection(int i, Operation<Integer> original,
                                      @Local(argsOnly = true, ordinal = 0) int par1,
                                      @Local(argsOnly = true, ordinal = 1) int par2,
                                      @Local(argsOnly = true, ordinal = 2) int par3) {
        int var5 = original.call(i);
        Block block = Block.blocksList[worldObj.getBlockId(par1, par2, par3)];
        if (block != null)
        {
            var5 = ((BlockExtension) block).getBedDirection(worldObj, par1, par2, par3);
        }

        return var5;
    }

    @WrapOperation(method = "wakeUpPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I"))
    private int forge$isBed(World worldObj, int posX, int posY, int posZ, Operation<Integer> original,
                            @Share(value = "block", namespace = "fabricated-forge") LocalRef<Block> blockRef) {
        int blockId = original.call(worldObj, posX, posY, posZ);
        Block block = Block.blocksList[blockId];

        if (block != null && ((BlockExtension) block).isBed(worldObj, posX, posY, posZ, this)) {
            blockRef.set(block);
            return Block.bed.blockID;
        }

        return 0;
    }

    @Redirect(method = "wakeUpPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockBed;setBedOccupied(Lnet/minecraft/src/World;IIIZ)V"))
    private void forge$setBedOccupied(World worldObj, int posX, int posY, int posZ, boolean par4,
                                      @Share(value = "block", namespace = "fabricated-forge") LocalRef<Block> blockRef) {
        ((BlockExtension) blockRef.get()).setBedOccupied(worldObj, posX, posY, posZ, (EntityPlayer) (Object) this, par4);
    }

    @Redirect(method = "wakeUpPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockBed;getNearestEmptyChunkCoordinates(Lnet/minecraft/src/World;IIII)Lnet/minecraft/src/ChunkCoordinates;"))
    private ChunkCoordinates forge$getBedSpawnPosition$wakeUpPlayer(World worldObj, int posX, int posY, int posZ, int par4,
                                                       @Share(value = "block", namespace = "fabricated-forge") LocalRef<Block> blockRef) {
        return ((BlockExtension) blockRef.get()).getBedSpawnPosition(worldObj, posX, posY, posZ, (EntityPlayer) (Object) this);
    }

    @WrapOperation(method = "isInBed", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I"))
    private int forge$isBed(World worldObj, int posX, int posY, int posZ, Operation<Integer> original) {
        Block block = Block.blocksList[original.call(worldObj, posX, posY, posZ)];

        return (block != null && ((BlockExtension) block).isBed(worldObj, posX, posY, posZ, this))
                ? Block.bed.blockID : 0;
    }



    @WrapOperation(method = "verifyRespawnCoordinates", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I"))
    private static int forge$isntBed(World worldObj, int posX, int posY, int posZ, Operation<Integer> original,
                                     @Share(value = "block", namespace = "fabricated-forge") LocalRef<Block> blockRef) {
        Block block = Block.blocksList[original.call(worldObj, posX, posY, posZ)];

        if (block != null) {
            blockRef.set(block);
        }

        return (block == null || !((BlockExtension) block).isBed(worldObj, posX, posY, posZ, null))
                ? 0 : Block.bed.blockID;
    }

    @Redirect(method = "verifyRespawnCoordinates", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockBed;getNearestEmptyChunkCoordinates(Lnet/minecraft/src/World;IIII)Lnet/minecraft/src/ChunkCoordinates;"))
    private static ChunkCoordinates forge$getBedSpawnPosition$verifyRespawnCoordinates(World par0World, int posX, int posY, int posZ, int par4,
                                                              @Share(value = "block", namespace = "fabricated-forge") LocalRef<Block> blockRef) {
        return ((BlockExtension) blockRef.get()).getBedSpawnPosition(par0World, posX, posY, posZ, null);
    }

    @Environment(EnvType.CLIENT)
    @Redirect(method = "getBedOrientationInDegrees", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockBed;getDirection(I)I"))
    private int forge$getBedDirection(int i) {
        int x = playerLocation.posX;
        int y = playerLocation.posY;
        int z = playerLocation.posZ;
        Block block = Block.blocksList[worldObj.getBlockId(x, y, z)];
        return (block == null ? 0 : ((BlockExtension) block).getBedDirection(worldObj, x, y, z));
    }

    @Environment(EnvType.CLIENT)
    @ModifyReturnValue(method = "getItemIcon", at = @At(value = "RETURN", ordinal = 4))
    private int forge$getIconIndex(int original,
                                   @Local(argsOnly = true) ItemStack par1ItemStack,
                                   @Local(argsOnly = true) int par2) {
        if (!(par1ItemStack.itemID == Item.fishingRod.shiftedIndex && this.fishEntity != null)) {
            return ((ItemExtension) par1ItemStack.getItem()).getIconIndex(par1ItemStack, par2, (EntityPlayer) (Object) this, itemInUse, itemInUseCount);
        }

        return original;
    }
}
