package fr.catcore.fabricatedforge.mixin.playerapi;

import fr.catcore.fabricatedforge.mixininterface.IMobEntity;
import fr.catcore.fabricatedforge.mixininterface.IPlayerAPIServerPlayerEntity;
import net.minecraft.ServerPlayerAPI;
import net.minecraft.ServerPlayerBase;
import net.minecraft.advancement.AchievementsAndCriterions;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.JumpControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.Trader;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.MobVisibilityCache;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NetworkSyncedItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.screen.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerPacketListener;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.util.BlockPos;
import net.minecraft.util.CanSleepEnum;
import net.minecraft.util.Language;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ScreenHandlerListener, IPlayerAPIServerPlayerEntity {
    @Shadow private int spawnProtectionTicks;

    @Shadow public MinecraftServer server;

    @Shadow public ServerPacketListener field_2823;

    @Shadow public int screenHandlerSyncId;

    @Shadow public ServerPlayerInteractionManager interactionManager;

    @Shadow private ItemStack[] field_2835;

    @Shadow @Final public List loadedChunks;

    @Shadow @Final public List removedEntities;

    @Shadow private int lastXp;

    @Shadow private int field_2827;

    @Shadow private int lastHungerLevel;

    @Shadow private boolean wasHungry;

    @Shadow private Language language;

    @Shadow private int chatFilterLevel;

    @Shadow public boolean skipPacketSlotUpdates;

    @Shadow public boolean killedEnderdragon;

    @Shadow private int viewDistance;

    @Shadow private boolean chatColors;

    @Shadow public abstract void incrementSyncId();

    @Shadow protected abstract void updateBlockEntity(BlockEntity blockEntity);

    @Shadow public abstract void closeOpenedScreenHandler();

    @Shadow public abstract ItemStack method_2155(int i);

    @Shadow public abstract ServerWorld getServerWorld();

    public ServerPlayerEntityMixin(World world) {
        super(world);
    }

    @Unique
    protected final ServerPlayerAPI serverPlayerAPI = ServerPlayerAPI.create((ServerPlayerEntity)(Object) this);

    @Inject(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;player:Lnet/minecraft/entity/player/ServerPlayerEntity;"))
    private void ctrTop(MinecraftServer minecraftServer, World arg, String string, ServerPlayerInteractionManager arg2, CallbackInfo ci) {
        ServerPlayerAPI.beforeLocalConstructing((ServerPlayerEntity) (Object)this, minecraftServer, arg, string, arg2);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void ctrBottom(MinecraftServer minecraftServer, World arg, String string, ServerPlayerInteractionManager arg2, CallbackInfo ci) {
        ServerPlayerAPI.afterLocalConstructing((ServerPlayerEntity)(Object) this, minecraftServer, arg, string, arg2);
    }

    @Override
    public final ServerPlayerBase getServerPlayerBase(String string) {
        return this.serverPlayerAPI != null ? this.serverPlayerAPI.getServerPlayerBase(string) : null;
    }

    @Override
    public final Set<String> getServerPlayerBaseIds(String string) {
        return this.serverPlayerAPI != null ? this.serverPlayerAPI.getServerPlayerBaseIds() : Collections.emptySet();
    }

    /**
     * @author Player API
     * @reason none
     */
    @Overwrite
    public boolean damage(DamageSource arg, int i) {
        boolean var3;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isAttackEntityFromModded) {
            var3 = ServerPlayerAPI.attackEntityFrom((ServerPlayerEntity)(Object) this, arg, i);
        } else {
            var3 = this.localAttackEntityFrom(arg, i);
        }

        return var3;
    }

    @Override
    public final boolean superAttackEntityFrom(DamageSource arg, int i) {
        return super.damage(arg, i);
    }

    @Override
    public final boolean localAttackEntityFrom(DamageSource arg, int i) {
        if (this.spawnProtectionTicks > 0) {
            return false;
        } else {
            if (!this.server.isPvpEnabled() && arg instanceof EntityDamageSource) {
                Entity var3 = arg.getAttacker();
                if (var3 instanceof PlayerEntity) {
                    return false;
                }

                if (var3 instanceof AbstractArrowEntity) {
                    AbstractArrowEntity var4 = (AbstractArrowEntity)var3;
                    if (var4.owner instanceof PlayerEntity) {
                        return false;
                    }
                }
            }

            return super.damage(arg, i);
        }
    }

    @Override
    public void method_3216(Entity arg) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isAttackTargetEntityWithCurrentItemModded) {
            ServerPlayerAPI.attackTargetEntityWithCurrentItem((ServerPlayerEntity)(Object) this, arg);
        } else {
            super.method_3216(arg);
        }

    }

    @Override
    public final void superAttackTargetEntityWithCurrentItem(Entity arg) {
        super.method_3216(arg);
    }

    @Override
    public final void localAttackTargetEntityWithCurrentItem(Entity arg) {
        super.method_3216(arg);
    }

    @Override
    public boolean isUsingEffectiveTool(Block arg) {
        boolean var2;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isCanHarvestBlockModded) {
            var2 = ServerPlayerAPI.canHarvestBlock((ServerPlayerEntity)(Object) this, arg);
        } else {
            var2 = super.isUsingEffectiveTool(arg);
        }

        return var2;
    }

    @Override
    public final boolean superCanHarvestBlock(Block arg) {
        return super.isUsingEffectiveTool(arg);
    }

    @Override
    public final boolean localCanHarvestBlock(Block arg) {
        return super.isUsingEffectiveTool(arg);
    }

    @Override
    public boolean method_3204(int i, int j, int k) {
        boolean var4;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isCanPlayerEditModded) {
            var4 = ServerPlayerAPI.canPlayerEdit((ServerPlayerEntity)(Object) this, i, j, k);
        } else {
            var4 = super.method_3204(i, j, k);
        }

        return var4;
    }

    @Override
    public final boolean superCanPlayerEdit(int i, int j, int k) {
        return super.method_3204(i, j, k);
    }

    @Override
    public final boolean localCanPlayerEdit(int i, int j, int k) {
        return super.method_3204(i, j, k);
    }

    @Override
    protected boolean canClimb() {
        boolean var1;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isCanTriggerWalkingModded) {
            var1 = ServerPlayerAPI.canTriggerWalking((ServerPlayerEntity)(Object) this);
        } else {
            var1 = super.canClimb();
        }

        return var1;
    }

    @Override
    public final boolean realCanTriggerWalking() {
        return this.canClimb();
    }

    @Override
    public final boolean superCanTriggerWalking() {
        return super.canClimb();
    }

    @Override
    public final boolean localCanTriggerWalking() {
        return super.canClimb();
    }

    @Override
    protected void method_2653(DamageSource arg, int i) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isDamageEntityModded) {
            ServerPlayerAPI.damageEntity((ServerPlayerEntity)(Object) this, arg, i);
        } else {
            super.method_2653(arg, i);
        }

    }

    @Override
    public final void realDamageEntity(DamageSource arg, int i) {
        this.method_2653(arg, i);
    }

    @Override
    public final void superDamageEntity(DamageSource arg, int i) {
        super.method_2653(arg, i);
    }

    @Override
    public final void localDamageEntity(DamageSource arg, int i) {
        super.method_2653(arg, i);
    }

    /**
     * @author Player API
     * @reason none
     */
    @Overwrite
    public void openInventory(Inventory arg) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isDisplayGUIChestModded) {
            ServerPlayerAPI.displayGUIChest((ServerPlayerEntity)(Object) this, arg);
        } else {
            this.localDisplayGUIChest(arg);
        }

    }

    @Override
    public final void superDisplayGUIChest(Inventory arg) {
        super.openInventory(arg);
    }

    @Override
    public final void localDisplayGUIChest(Inventory arg) {
        if (this.openScreenHandler != this.playerScreenHandler) {
            this.closeHandledScreen();
        }

        this.incrementSyncId();
        this.closeOpenedScreenHandler();
        this.field_2823.sendPacket(new OpenScreen_S2CPacket(this.screenHandlerSyncId, 0, arg.method_2385(), arg.getInvSize()));
        this.openScreenHandler = new ChestScreenHandler(this.inventory, arg);
        this.openScreenHandler.syncId = this.screenHandlerSyncId;
        this.openScreenHandler.addListener(this);
    }

    /**
     * @author Player API
     * @reason none
     */
    @Overwrite
    public void method_3155(DispenserBlockEntity arg) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isDisplayGUIDispenserModded) {
            ServerPlayerAPI.displayGUIDispenser((ServerPlayerEntity)(Object) this, arg);
        } else {
            this.localDisplayGUIDispenser(arg);
        }

    }

    @Override
    public final void superDisplayGUIDispenser(DispenserBlockEntity arg) {
        super.method_3155(arg);
    }

    @Override
    public final void localDisplayGUIDispenser(DispenserBlockEntity arg) {
        this.incrementSyncId();
        this.closeOpenedScreenHandler();
        this.field_2823.sendPacket(new OpenScreen_S2CPacket(this.screenHandlerSyncId, 3, arg.method_2385(), arg.getInvSize()));
        this.openScreenHandler = new Generic3x3ScreenHandler(this.inventory, arg);
        this.openScreenHandler.syncId = this.screenHandlerSyncId;
        this.openScreenHandler.addListener(this);
    }

    /**
     * @author Player API
     * @reason none
     */
    @Overwrite
    public void method_3156(FurnaceBlockEntity arg) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isDisplayGUIFurnaceModded) {
            ServerPlayerAPI.displayGUIFurnace((ServerPlayerEntity)(Object) this, arg);
        } else {
            this.localDisplayGUIFurnace(arg);
        }

    }

    @Override
    public final void superDisplayGUIFurnace(FurnaceBlockEntity arg) {
        super.method_3156(arg);
    }

    @Override
    public final void localDisplayGUIFurnace(FurnaceBlockEntity arg) {
        this.incrementSyncId();
        this.closeOpenedScreenHandler();
        this.field_2823.sendPacket(new OpenScreen_S2CPacket(this.screenHandlerSyncId, 2, arg.method_2385(), arg.getInvSize()));
        this.openScreenHandler = new FurnaceScreenHandler(this.inventory, arg);
        this.openScreenHandler.syncId = this.screenHandlerSyncId;
        this.openScreenHandler.addListener(this);
    }

    /**
     * @author Player API
     * @reason none
     */
    @Overwrite
    public void method_3173(int i, int j, int k) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isDisplayWorkbenchGUIModded) {
            ServerPlayerAPI.displayWorkbenchGUI((ServerPlayerEntity)(Object) this, i, j, k);
        } else {
            this.localDisplayWorkbenchGUI(i, j, k);
        }

    }

    @Override
    public final void superDisplayWorkbenchGUI(int i, int j, int k) {
        super.method_3173(i, j, k);
    }

    @Override
    public final void localDisplayWorkbenchGUI(int i, int j, int k) {
        this.incrementSyncId();
        this.closeOpenedScreenHandler();
        this.field_2823.sendPacket(new OpenScreen_S2CPacket(this.screenHandlerSyncId, 1, "Crafting", 9));
        this.openScreenHandler = new CraftingScreenHandler(this.inventory, this.world, i, j, k);
        this.openScreenHandler.syncId = this.screenHandlerSyncId;
        this.openScreenHandler.addListener(this);
    }

    @Override
    public ItemEntity dropSelectedStack() {
        ItemEntity var1;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isDropOneItemModded) {
            var1 = ServerPlayerAPI.dropOneItem((ServerPlayerEntity)(Object) this);
        } else {
            var1 = super.dropSelectedStack();
        }

        return var1;
    }

    @Override
    public final ItemEntity superDropOneItem() {
        return super.dropSelectedStack();
    }

    @Override
    public final ItemEntity localDropOneItem() {
        return super.dropSelectedStack();
    }

    @Override
    public ItemEntity dropStack(ItemStack arg) {
        ItemEntity var2;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isDropPlayerItemModded) {
            var2 = ServerPlayerAPI.dropPlayerItem((ServerPlayerEntity)(Object) this, arg);
        } else {
            var2 = super.dropStack(arg);
        }

        return var2;
    }

    @Override
    public final ItemEntity superDropPlayerItem(ItemStack arg) {
        return super.dropStack(arg);
    }

    @Override
    public final ItemEntity localDropPlayerItem(ItemStack arg) {
        return super.dropStack(arg);
    }

    @Override
    protected void method_2490(float f) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isFallModded) {
            ServerPlayerAPI.fall((ServerPlayerEntity)(Object) this, f);
        } else {
            super.method_2490(f);
        }

    }

    @Override
    public final void realFall(float f) {
        this.method_2490(f);
    }

    @Override
    public final void superFall(float f) {
        super.method_2490(f);
    }

    @Override
    public final void localFall(float f) {
        super.method_2490(f);
    }

    @Override
    public float method_3153(Block arg) {
        float var2;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isGetCurrentPlayerStrVsBlockModded) {
            var2 = ServerPlayerAPI.getCurrentPlayerStrVsBlock((ServerPlayerEntity)(Object) this, arg);
        } else {
            var2 = super.method_3153(arg);
        }

        return var2;
    }

    @Override
    public final float superGetCurrentPlayerStrVsBlock(Block arg) {
        return super.method_3153(arg);
    }

    @Override
    public final float localGetCurrentPlayerStrVsBlock(Block arg) {
        return super.method_3153(arg);
    }

    @Override
    public double squaredDistanceTo(double d, double e, double f) {
        double var7;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isGetDistanceSqModded) {
            var7 = ServerPlayerAPI.getDistanceSq((ServerPlayerEntity)(Object) this, d, e, f);
        } else {
            var7 = super.squaredDistanceTo(d, e, f);
        }

        return var7;
    }

    @Override
    public final double superGetDistanceSq(double d, double e, double f) {
        return super.squaredDistanceTo(d, e, f);
    }

    @Override
    public final double localGetDistanceSq(double d, double e, double f) {
        return super.squaredDistanceTo(d, e, f);
    }

    @Override
    public float getBrightnessAtEyes(float f) {
        float var2;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isGetBrightnessModded) {
            var2 = ServerPlayerAPI.getBrightness((ServerPlayerEntity)(Object) this, f);
        } else {
            var2 = super.getBrightnessAtEyes(f);
        }

        return var2;
    }

    @Override
    public final float superGetBrightness(float f) {
        return super.getBrightnessAtEyes(f);
    }

    @Override
    public final float localGetBrightness(float f) {
        return super.getBrightnessAtEyes(f);
    }

    /**
     * @author Player API
     * @reason none
     */
    @Overwrite
    public float getEyeHeight() {
        float var1;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isGetEyeHeightModded) {
            var1 = ServerPlayerAPI.getEyeHeight((ServerPlayerEntity)(Object) this);
        } else {
            var1 = this.localGetEyeHeight();
        }

        return var1;
    }

    @Override
    public final float superGetEyeHeight() {
        return super.getEyeHeight();
    }

    @Override
    public final float localGetEyeHeight() {
        return 1.62F;
    }

    @Override
    protected float method_2646() {
        float var1;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isGetSpeedModifierModded) {
            var1 = ServerPlayerAPI.getSpeedModifier((ServerPlayerEntity)(Object) this);
        } else {
            var1 = super.method_2646();
        }

        return var1;
    }

    @Override
    public final float realGetSpeedModifier() {
        return this.method_2646();
    }

    @Override
    public final float superGetSpeedModifier() {
        return super.method_2646();
    }

    @Override
    public final float localGetSpeedModifier() {
        return super.method_2646();
    }

    @Override
    public void method_2667(int i) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isHealModded) {
            ServerPlayerAPI.heal((ServerPlayerEntity)(Object) this, i);
        } else {
            super.method_2667(i);
        }

    }

    @Override
    public final void superHeal(int i) {
        super.method_2667(i);
    }

    @Override
    public final void localHeal(int i) {
        super.method_2667(i);
    }

    @Override
    public boolean method_2537(PlayerEntity arg) {
        boolean var2;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isInteractModded) {
            var2 = ServerPlayerAPI.interact((ServerPlayerEntity)(Object) this, arg);
        } else {
            var2 = super.method_2537(arg);
        }

        return var2;
    }

    @Override
    public final boolean superInteract(PlayerEntity arg) {
        return super.method_2537(arg);
    }

    @Override
    public final boolean localInteract(PlayerEntity arg) {
        return super.method_2537(arg);
    }

    @Override
    public boolean isInsideWall() {
        boolean var1;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isIsEntityInsideOpaqueBlockModded) {
            var1 = ServerPlayerAPI.isEntityInsideOpaqueBlock((ServerPlayerEntity)(Object) this);
        } else {
            var1 = super.isInsideWall();
        }

        return var1;
    }

    @Override
    public final boolean superIsEntityInsideOpaqueBlock() {
        return super.isInsideWall();
    }

    @Override
    public final boolean localIsEntityInsideOpaqueBlock() {
        return super.isInsideWall();
    }

    @Override
    public boolean isTouchingWater() {
        boolean var1;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isIsInWaterModded) {
            var1 = ServerPlayerAPI.isInWater((ServerPlayerEntity)(Object) this);
        } else {
            var1 = super.isTouchingWater();
        }

        return var1;
    }

    @Override
    public final boolean superIsInWater() {
        return super.isTouchingWater();
    }

    @Override
    public final boolean localIsInWater() {
        return super.isTouchingWater();
    }

    @Override
    public boolean isSubmergedIn(Material arg) {
        boolean var2;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isIsInsideOfMaterialModded) {
            var2 = ServerPlayerAPI.isInsideOfMaterial((ServerPlayerEntity)(Object) this, arg);
        } else {
            var2 = super.isSubmergedIn(arg);
        }

        return var2;
    }

    @Override
    public final boolean superIsInsideOfMaterial(Material arg) {
        return super.isSubmergedIn(arg);
    }

    @Override
    public final boolean localIsInsideOfMaterial(Material arg) {
        return super.isSubmergedIn(arg);
    }

    @Override
    public boolean method_2660() {
        boolean var1;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isIsOnLadderModded) {
            var1 = ServerPlayerAPI.isOnLadder((ServerPlayerEntity)(Object) this);
        } else {
            var1 = super.method_2660();
        }

        return var1;
    }

    @Override
    public final boolean superIsOnLadder() {
        return super.method_2660();
    }

    @Override
    public final boolean localIsOnLadder() {
        return super.method_2660();
    }

    @Override
    public boolean method_2641() {
        boolean var1;
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isIsPlayerSleepingModded) {
            var1 = ServerPlayerAPI.isPlayerSleeping((ServerPlayerEntity)(Object) this);
        } else {
            var1 = super.method_2641();
        }

        return var1;
    }

    @Override
    public final boolean superIsPlayerSleeping() {
        return super.method_2641();
    }

    @Override
    public final boolean localIsPlayerSleeping() {
        return super.method_2641();
    }

    @Override
    protected void method_2612() {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isJumpModded) {
            ServerPlayerAPI.jump((ServerPlayerEntity)(Object) this);
        } else {
            super.method_2612();
        }

    }

    @Override
    public final void realJump() {
        this.method_2612();
    }

    @Override
    public final void superJump() {
        super.method_2612();
    }

    @Override
    public final void localJump() {
        super.method_2612();
    }

    @Override
    public void move(double d, double e, double f) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isMoveEntityModded) {
            ServerPlayerAPI.moveEntity((ServerPlayerEntity)(Object) this, d, e, f);
        } else {
            super.move(d, e, f);
        }

    }

    @Override
    public final void superMoveEntity(double d, double e, double f) {
        super.move(d, e, f);
    }

    @Override
    public final void localMoveEntity(double d, double e, double f) {
        super.move(d, e, f);
    }

    @Override
    public void method_2657(float f, float g) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isMoveEntityWithHeadingModded) {
            ServerPlayerAPI.moveEntityWithHeading((ServerPlayerEntity)(Object) this, f, g);
        } else {
            super.method_2657(f, g);
        }

    }

    @Override
    public final void superMoveEntityWithHeading(float f, float g) {
        super.method_2657(f, g);
    }

    @Override
    public final void localMoveEntityWithHeading(float f, float g) {
        super.method_2657(f, g);
    }

    @Override
    public void updateVelocity(float f, float g, float h) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isMoveFlyingModded) {
            ServerPlayerAPI.moveFlying((ServerPlayerEntity)(Object) this, f, g, h);
        } else {
            super.updateVelocity(f, g, h);
        }

    }

    @Override
    public final void superMoveFlying(float f, float g, float h) {
        super.updateVelocity(f, g, h);
    }

    @Override
    public final void localMoveFlying(float f, float g, float h) {
        super.updateVelocity(f, g, h);
    }

    /**
     * @author Player API
     * @reason none
     */
    @Overwrite
    public void dropInventory(DamageSource arg) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isOnDeathModded) {
            ServerPlayerAPI.onDeath((ServerPlayerEntity)(Object) this, arg);
        } else {
            this.localOnDeath(arg);
        }

    }

    @Override
    public final void superOnDeath(DamageSource arg) {
        super.dropInventory(arg);
    }

    @Override
    public final void localOnDeath(DamageSource arg) {
        this.server.getPlayerManager().sendToAll(new ChatMessage_S2CPacket(arg.method_2420(this)));
        this.inventory.dropAll();
    }

    @Override
    public void method_2651() {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isOnLivingUpdateModded) {
            ServerPlayerAPI.onLivingUpdate((ServerPlayerEntity)(Object) this);
        } else {
            super.method_2651();
        }

    }

    @Override
    public final void superOnLivingUpdate() {
        super.method_2651();
    }

    @Override
    public final void localOnLivingUpdate() {
        super.method_2651();
    }

    @Override
    public void method_2501(MobEntity arg) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isOnKillEntityModded) {
            ServerPlayerAPI.onKillEntity((ServerPlayerEntity)(Object) this, arg);
        } else {
            super.method_2501(arg);
        }

    }

    @Override
    public final void superOnKillEntity(MobEntity arg) {
        super.method_2501(arg);
    }

    @Override
    public final void localOnKillEntity(MobEntity arg) {
        super.method_2501(arg);
    }

    /**
     * @author Player API
     * @reason none
     */
    @Overwrite
    public void tick() {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isOnUpdateModded) {
            ServerPlayerAPI.onUpdate((ServerPlayerEntity)(Object) this);
        } else {
            this.localOnUpdate();
        }

    }

    @Override
    public final void superOnUpdate() {
        super.tick();
    }

    @Override
    public final void localOnUpdate() {
        this.interactionManager.update();
        --this.spawnProtectionTicks;
        this.openScreenHandler.sendContentUpdates();

        int var1;
        for(var1 = 0; var1 < 5; ++var1) {
            ItemStack var2 = this.method_2155(var1);
            if (var2 != this.field_2835[var1]) {
                this.getServerWorld().getEntityTracker().sendToOtherTrackingEntities(this, new EntityEquipmentUpdate_S2CPacket(this.id, var1, var2));
                this.field_2835[var1] = var2;
            }
        }

        if (!this.loadedChunks.isEmpty()) {
            ArrayList var6 = new ArrayList();
            Iterator var7 = this.loadedChunks.iterator();
            ArrayList var3 = new ArrayList();

            while(var7.hasNext() && var6.size() < 5) {
                ChunkPos var4 = (ChunkPos)var7.next();
                var7.remove();
                if (var4 != null && this.world.isPosLoaded(var4.x << 4, 0, var4.z << 4)) {
                    var6.add(this.world.getChunk(var4.x, var4.z));
                    var3.addAll(((ServerWorld)this.world).method_2134(var4.x * 16, 0, var4.z * 16, var4.x * 16 + 16, 256, var4.z * 16 + 16));
                }
            }

            if (!var6.isEmpty()) {
                this.field_2823.sendPacket(new class_687(var6));
                Iterator var10 = var3.iterator();

                while(var10.hasNext()) {
                    BlockEntity var5 = (BlockEntity)var10.next();
                    this.updateBlockEntity(var5);
                }
            }
        }

        if (!this.removedEntities.isEmpty()) {
            var1 = Math.min(this.removedEntities.size(), 127);
            int[] var8 = new int[var1];
            Iterator var9 = this.removedEntities.iterator();
            int var11 = 0;

            while(var9.hasNext() && var11 < var1) {
                var8[var11++] = (Integer)var9.next();
                var9.remove();
            }

            this.field_2823.sendPacket(new DestroyEntityS2CPacket(var8));
        }

    }

    /**
     * @author Player API
     * @reason none
     */
    @Overwrite
    public void tickPlayer() {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isOnUpdateEntityModded) {
            ServerPlayerAPI.onUpdateEntity((ServerPlayerEntity)(Object) this);
        } else {
            this.localOnUpdateEntity();
        }

    }

    @Override
    public final void localOnUpdateEntity() {
        super.tick();

        for(int var1 = 0; var1 < this.inventory.getInvSize(); ++var1) {
            ItemStack var2 = this.inventory.getInvStack(var1);
            if (var2 != null && Item.ITEMS[var2.id].isNetworkSynced() && this.field_2823.method_2202() <= 2) {
                Packet var3 = ((NetworkSyncedItem)Item.ITEMS[var2.id]).createSyncPacket(var2, this.world, this);
                if (var3 != null) {
                    this.field_2823.sendPacket(var3);
                }
            }
        }

        if (this.field_3996) {
            if (this.server.isNetherAllowed()) {
                if (this.openScreenHandler != this.playerScreenHandler) {
                    this.closeHandledScreen();
                }

                if (this.vehicle != null) {
                    this.startRiding(this.vehicle);
                } else {
                    this.field_3997 += 0.0125F;
                    if (this.field_3997 >= 1.0F) {
                        this.field_3997 = 1.0F;
                        this.field_3995 = 10;
                        this.id = 0;
                        if (this._dimension == -1) {
                            this.id = 0;
                        } else {
                            this.id = -1;
                        }

                        this.server.getPlayerManager().teleportToDimension((ServerPlayerEntity)(Object) this, this.id);
                        this.lastXp = -1;
                        this.field_2827 = -1;
                        this.lastHungerLevel = -1;
                        this.incrementStat(AchievementsAndCriterions.PORTAL);
                    }
                }

                this.field_3996 = false;
            }
        } else {
            if (this.field_3997 > 0.0F) {
                this.field_3997 -= 0.05F;
            }

            if (this.field_3997 < 0.0F) {
                this.field_3997 = 0.0F;
            }
        }

        if (this.field_3995 > 0) {
            --this.field_3995;
        }

        if (this.method_2600() == this.field_2827 && this.lastHungerLevel == this.hungerManager.getFoodLevel()) {
            if (this.hungerManager.getSaturationLevel() == 0.0F == this.wasHungry) {
            }
        } else {
            this.field_2823.sendPacket(new HealthUpdate_S2CPacket(this.method_2600(), this.hungerManager.getFoodLevel(), this.hungerManager.getSaturationLevel()));
            this.field_2827 = this.method_2600();
            this.lastHungerLevel = this.hungerManager.getFoodLevel();
            this.wasHungry = this.hungerManager.getSaturationLevel() == 0.0F;
        }

        if (this.totalExperience != this.lastXp) {
            this.lastXp = this.totalExperience;
            this.field_2823.sendPacket(new class_716(this.experienceProgress, this.totalExperience, this.experienceLevel));
        }

    }

    /**
     * @author Player API
     * @reason none
     */
    @Overwrite
    public void readCustomDataFromNbt(NbtCompound arg) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isReadEntityFromNBTModded) {
            ServerPlayerAPI.readEntityFromNBT((ServerPlayerEntity)(Object) this, arg);
        } else {
            this.localReadEntityFromNBT(arg);
        }

    }

    @Override
    public final void superReadEntityFromNBT(NbtCompound arg) {
        super.readCustomDataFromNbt(arg);
    }

    @Override
    public final void localReadEntityFromNBT(NbtCompound arg) {
        super.readCustomDataFromNbt(arg);
        if (arg.contains("playerGameType")) {
            this.interactionManager.setGamemode(GameMode.setGameModeWithId(arg.getInt("playerGameType")));
        }

    }

    @Override
    public void remove() {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isSetDeadModded) {
            ServerPlayerAPI.setDead((ServerPlayerEntity)(Object) this);
        } else {
            super.remove();
        }

    }

    @Override
    public final void superSetDead() {
        super.remove();
    }

    @Override
    public final void localSetDead() {
        super.remove();
    }

    @Override
    public void updatePosition(double d, double e, double f) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isSetPositionModded) {
            ServerPlayerAPI.setPosition((ServerPlayerEntity)(Object) this, d, e, f);
        } else {
            super.updatePosition(d, e, f);
        }

    }

    @Override
    public final void superSetPosition(double d, double e, double f) {
        super.updatePosition(d, e, f);
    }

    @Override
    public final void localSetPosition(double d, double e, double f) {
        super.updatePosition(d, e, f);
    }

    @Override
    protected void method_2635() {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isUpdateEntityActionStateModded) {
            ServerPlayerAPI.updateEntityActionState((ServerPlayerEntity)(Object) this);
        } else {
            super.method_2635();
        }

    }

    @Override
    public final void realUpdateEntityActionState() {
        this.method_2635();
    }

    @Override
    public final void superUpdateEntityActionState() {
        super.method_2635();
    }

    @Override
    public final void localUpdateEntityActionState() {
        super.method_2635();
    }

    /**
     * @author Player API
     * @reason none
     */
    @Overwrite
    public void writeCustomDataToNbt(NbtCompound arg) {
        if (this.serverPlayerAPI != null && this.serverPlayerAPI.isWriteEntityToNBTModded) {
            ServerPlayerAPI.writeEntityToNBT((ServerPlayerEntity)(Object) this, arg);
        } else {
            this.localWriteEntityToNBT(arg);
        }

    }

    @Override
    public final void superWriteEntityToNBT(NbtCompound arg) {
        super.writeCustomDataToNbt(arg);
    }

    @Override
    public final void localWriteEntityToNBT(NbtCompound arg) {
        super.writeCustomDataToNbt(arg);
        arg.putInt("playerGameType", this.interactionManager.getgamemode().getGameModeId());
    }

    @Override
    public final void superAddChatMessage(String string) {
        super.method_3199(string);
    }

    @Override
    public final boolean superAddEntityID(NbtCompound arg) {
        return super.saveToNbt(arg);
    }

    @Override
    public final void superAddExhaustion(float f) {
        super.addExhaustion(f);
    }

    @Override
    public final void superAddExperience(int i) {
        super.addExperience(i);
    }

    @Override
    public final void superAddMovementStat(double d, double e, double f) {
        super.method_3209(d, e, f);
    }

    @Override
    public final void superAddPotionEffect(StatusEffectInstance arg) {
        super.method_2654(arg);
    }

    @Override
    public final void superAddStat(Stat arg, int i) {
        super.incrementStat(arg, i);
    }

    @Override
    public final void superAddToPlayerScore(Entity arg, int i) {
        super.updateKilledAdvancementCriterion(arg, i);
    }

    @Override
    public final void superAddVelocity(double d, double e, double f) {
        super.addVelocity(d, e, f);
    }

    @Override
    public final void realAlertWolves(MobEntity arg, boolean bl) {
        this.method_3163(arg, bl);
    }

    @Override
    public final void superAlertWolves(MobEntity arg, boolean bl) {
        super.method_3163(arg, bl);
    }

    @Override
    public final int realApplyArmorCalculations(DamageSource arg, int i) {
        return this.method_2626(arg, i);
    }

    @Override
    public final int superApplyArmorCalculations(DamageSource arg, int i) {
        return super.method_2626(arg, i);
    }

    @Override
    public final void superApplyEntityCollision(Entity arg) {
        super.pushAwayFrom(arg);
    }

    @Override
    public final int realApplyPotionDamageCalculations(DamageSource arg, int i) {
        return this.method_2648(arg, i);
    }

    @Override
    public final int superApplyPotionDamageCalculations(DamageSource arg, int i) {
        return super.method_2648(arg, i);
    }

    @Override
    public final boolean superAttackEntityAsMob(Entity arg) {
        return super.method_2671(arg);
    }

    @Override
    public final boolean superCanAttackWithItem() {
        return super.isAttackable();
    }

    @Override
    public final boolean superCanBeCollidedWith() {
        return super.collides();
    }

    @Override
    public final boolean superCanBePushed() {
        return super.isPushable();
    }

    @Override
    public final boolean superCanBreatheUnderwater() {
        return super.method_2607();
    }

    @Override
    public final boolean realCanDespawn() {
        return this.canImmediatelyDespawn();
    }

    @Override
    public final boolean superCanDespawn() {
        return super.canImmediatelyDespawn();
    }

    @Override
    public final boolean superCanEat(boolean bl) {
        return super.canConsume(bl);
    }

    @Override
    public final boolean superCanEntityBeSeen(Entity arg) {
        return super.method_2673(arg);
    }

    @Override
    public final void superClearActivePotions() {
        super.method_2643();
    }

    @Override
    public final void superClearItemInUse() {
        super.resetUseItem();
    }

    @Override
    public final void superClonePlayer(PlayerEntity arg, boolean bl) {
        super.copyFrom(arg, bl);
    }

    @Override
    public final void superCloseScreen() {
        super.closeHandledScreen();
    }

    @Override
    public final void realDamageArmor(int i) {
        this.method_2670(i);
    }

    @Override
    public final void superDamageArmor(int i) {
        super.method_2670(i);
    }

    @Override
    public final void realDealFireDamage(int i) {
        this.burn(i);
    }

    @Override
    public final void superDealFireDamage(int i) {
        super.burn(i);
    }

    @Override
    public final int realDecreaseAirSupply(int i) {
        return this.method_2664(i);
    }

    @Override
    public final int superDecreaseAirSupply(int i) {
        return super.method_2664(i);
    }

    @Override
    public final void realDespawnEntity() {
        this.checkDespawn();
    }

    @Override
    public final void superDespawnEntity() {
        super.checkDespawn();
    }

    @Override
    public final void superDestroyCurrentEquippedItem() {
        super.removeSelectedSlotItem();
    }

    @Override
    public final void superDetachHome() {
        super.method_2592();
    }

    @Override
    public final void superDisplayGUIBook(ItemStack arg) {
        super.openBookEditScreen(arg);
    }

    @Override
    public final void superDisplayGUIBrewingStand(BrewingStandBlockEntity arg) {
        super.method_3154(arg);
    }

    @Override
    public final void superDisplayGUIEditSign(SignBlockEntity arg) {
        super.method_3157(arg);
    }

    @Override
    public final void superDisplayGUIEnchantment(int i, int j, int k) {
        super.method_3198(i, j, k);
    }

    @Override
    public final void superDisplayGUIMerchant(Trader arg) {
        super.openTradingScreen(arg);
    }

    @Override
    public final void realDoBlockCollisions() {
        this.checkBlockCollision();
    }

    @Override
    public final void superDoBlockCollisions() {
        super.checkBlockCollision();
    }

    @Override
    public final void realDropFewItems(boolean bl, int i) {
        this.method_2587(bl, i);
    }

    @Override
    public final void superDropFewItems(boolean bl, int i) {
        super.method_2587(bl, i);
    }

    @Override
    public final ItemEntity superDropItem(int i, int j) {
        return super.method_2526(i, j);
    }

    @Override
    public final ItemEntity superDropItemWithOffset(int i, int j, float f) {
        return super.method_2493(i, j, f);
    }

    @Override
    public final ItemEntity superDropPlayerItemWithRandomChoice(ItemStack arg, boolean bl) {
        return super.dropStack(arg, bl);
    }

    @Override
    public final void realDropRareDrop(int i) {
        this.method_2672(i);
    }

    @Override
    public final void superDropRareDrop(int i) {
        super.method_2672(i);
    }

    @Override
    public final void superEatGrassBonus() {
        super.onEatingGrass();
    }

    @Override
    public final ItemEntity superEntityDropItem(ItemStack arg, float f) {
        return super.dropItem(arg, f);
    }

    @Override
    public final void realEntityInit() {
        this.initDataTracker();
    }

    @Override
    public final void superEntityInit() {
        super.initDataTracker();
    }

    @Override
    public final boolean superEquals(Object object) {
        return super.equals(object);
    }

    @Override
    public final void superExtinguish() {
        super.extinguish();
    }

    @Override
    public final void superFaceEntity(Entity arg, float f, float g) {
        super.lookAtEntity(arg, f, g);
    }

    @Override
    public final void superFunc_70062_b(int i, ItemStack arg) {
        super.setArmorSlot(i, arg);
    }

    @Override
    public final float superFunc_70079_am() {
        return super.getHeadRotation();
    }

    @Override
    public final boolean superFunc_71066_bF() {
        return super.method_3182();
    }

    @Override
    public final float superGetAIMoveSpeed() {
        return super.method_2622();
    }

    @Override
    public final MobEntity superGetAITarget() {
        return super.method_2619();
    }

    @Override
    public final StatusEffectInstance superGetActivePotionEffect(StatusEffect arg) {
        return super.method_2627(arg);
    }

    @Override
    public final Collection superGetActivePotionEffects() {
        return super.method_2644();
    }

    @Override
    public final int superGetAge() {
        return super.method_2621();
    }

    @Override
    public final int superGetAir() {
        return super.getAir();
    }

    @Override
    public final MobEntity superGetAttackTarget() {
        return super.method_2623();
    }

    @Override
    public final float superGetBedOrientationInDegrees() {
        return super.method_3183();
    }

    @Override
    public final Box superGetBoundingBox() {
        return super.getBox();
    }

    @Override
    public final int superGetBrightnessForRender(float f) {
        return super.getLightmapCoordinates(f);
    }

    @Override
    public final boolean superGetCanSpawnHere() {
        return super.canSpawn();
    }

    @Override
    public final float superGetCollisionBorderSize() {
        return super.getTargetingMargin();
    }

    @Override
    public final Box superGetCollisionBox(Entity arg) {
        return super.getHardCollisionBox(arg);
    }

    @Override
    public final String superGetCommandSenderName() {
        return super.getUsername();
    }

    @Override
    public final EntityGroup superGetCreatureAttribute() {
        return super.method_2647();
    }

    @Override
    public final ItemStack superGetCurrentEquippedItem() {
        return super.getMainHandStack();
    }

    @Override
    public final DataTracker superGetDataWatcher() {
        return super.getDataTracker();
    }

    @Override
    public final String realGetDeathSound() {
        return this.method_2605();
    }

    @Override
    public final String superGetDeathSound() {
        return super.method_2605();
    }

    @Override
    public final double superGetDistance(double d, double e, double f) {
        return super.distanceTo(d, e, f);
    }

    @Override
    public final double superGetDistanceSqToEntity(Entity arg) {
        return super.squaredDistanceTo(arg);
    }

    @Override
    public final float superGetDistanceToEntity(Entity arg) {
        return super.distanceTo(arg);
    }

    @Override
    public final int realGetDropItemId() {
        return this.method_2606();
    }

    @Override
    public final int superGetDropItemId() {
        return super.method_2606();
    }

    @Override
    public final String superGetEntityName() {
        return super.getTranslationKey();
    }

    @Override
    public final MobVisibilityCache superGetEntitySenses() {
        return super.getVisibilityCache();
    }

    @Override
    public final int realGetExperiencePoints(PlayerEntity arg) {
        return this.method_2585(arg);
    }

    @Override
    public final int superGetExperiencePoints(PlayerEntity arg) {
        return super.method_2585(arg);
    }

    @Override
    public final boolean realGetFlag(int i) {
        return this.getFlag(i);
    }

    @Override
    public final boolean superGetFlag(int i) {
        return super.getFlag(i);
    }

    @Override
    public final HungerManager superGetFoodStats() {
        return super.getHungerManager();
    }

    @Override
    public final int superGetHealth() {
        return super.method_2600();
    }

    @Override
    public final ItemStack superGetHeldItem() {
        return super.method_2640();
    }

    @Override
    public final BlockPos superGetHomePosition() {
        return super.method_2590();
    }

    @Override
    public final String realGetHurtSound() {
        return this.method_2604();
    }

    @Override
    public final String superGetHurtSound() {
        return super.method_2604();
    }

    @Override
    public final EnderChestInventory superGetInventoryEnderChest() {
        return super.getEnderChestInventory();
    }

    @Override
    public final int superGetItemIcon(ItemStack arg, int i) {
        return super.method_2630(arg, i);
    }

    @Override
    public final ItemStack superGetItemInUse() {
        return super.getUsedItem();
    }

    @Override
    public final int superGetItemInUseCount() {
        return super.getItemUseTicks();
    }

    @Override
    public final int superGetItemInUseDuration() {
        return super.getRemainingUseTime();
    }

    @Override
    public final JumpControl superGetJumpHelper() {
        return super.getJumpControl();
    }

    @Override
    public final ItemStack[] superGetLastActiveItems() {
        return super.getArmorStacks();
    }

    @Override
    public final MobEntity superGetLastAttackingEntity() {
        return super.method_2620();
    }

    @Override
    public final String realGetLivingSound() {
        return this.getAmbientSound();
    }

    @Override
    public final String superGetLivingSound() {
        return super.getAmbientSound();
    }

    @Override
    public final Vec3d superGetLook(float f) {
        return super.method_2666(f);
    }

    @Override
    public final LookControl superGetLookHelper() {
        return super.getLookControl();
    }

    @Override
    public final Vec3d superGetLookVec() {
        return super.getRotation();
    }

    @Override
    public final int superGetMaxHealth() {
        return super.method_2599();
    }

    @Override
    public final int superGetMaxSpawnedInChunk() {
        return super.getLimitPerChunk();
    }

    @Override
    public final float superGetMaximumHomeDistance() {
        return super.method_2591();
    }

    @Override
    public final double superGetMountedYOffset() {
        return super.getMountedHeightOffset();
    }

    @Override
    public final MoveControl superGetMoveHelper() {
        return super.getMotionHelper();
    }

    @Override
    public final EntityNavigation superGetNavigator() {
        return super.getNavigation();
    }

    @Override
    public final Entity[] superGetParts() {
        return super.getParts();
    }

    @Override
    public final Vec3d superGetPosition(float f) {
        return super.method_2663(f);
    }

    @Override
    public final Random superGetRNG() {
        return super.method_2618();
    }

    @Override
    public final float superGetRenderSizeModifier() {
        return super.method_2638();
    }

    @Override
    public final int superGetScore() {
        return super.getScore();
    }

    @Override
    public final float superGetShadowSize() {
        return super.method_2475();
    }

    @Override
    public final int superGetSleepTimer() {
        return super.getSleepTimer();
    }

    @Override
    public final float realGetSoundVolume() {
        return this.method_2602();
    }

    @Override
    public final float superGetSoundVolume() {
        return super.method_2602();
    }

    @Override
    public final BlockPos superGetSpawnChunk() {
        return super.method_3186();
    }

    @Override
    public final float superGetSwingProgress(float f) {
        return super.method_2661(f);
    }

    @Override
    public final int superGetTalkInterval() {
        return super.getMinAmbientSoundDelay();
    }

    @Override
    public final String superGetTexture() {
        return super.method_2473();
    }

    @Override
    public final int superGetTotalArmorValue() {
        return super.method_2601();
    }

    @Override
    public final Language superGetTranslator() {
        return super.getlanguage();
    }

    @Override
    public final int superGetVerticalFaceSpeed() {
        return super.getLookPitchSpeed();
    }

    @Override
    public final double superGetYOffset() {
        return super.getHeightOffset();
    }

    @Override
    public final void superHandleHealthUpdate(byte b) {
        super.handleStatus(b);
    }

    @Override
    public final boolean superHandleLavaMovement() {
        return super.method_2469();
    }

    @Override
    public final boolean superHandleWaterMovement() {
        return super.updateWaterState();
    }

    @Override
    public final boolean superHasHome() {
        return super.method_2593();
    }

    @Override
    public final int superHashCode() {
        return super.hashCode();
    }

    @Override
    public final void realIncrementWindowID() {
        this.incrementSyncId();
    }

    @Override
    public final boolean superInteractWith(Entity arg) {
        return super.method_3215(arg);
    }

    @Override
    public final boolean realIsAIEnabled() {
        return this.method_2608();
    }

    @Override
    public final boolean superIsAIEnabled() {
        return super.method_2608();
    }

    @Override
    public final boolean superIsBlocking() {
        return super.method_2611();
    }

    @Override
    public final boolean superIsBurning() {
        return super.isOnFire();
    }

    @Override
    public final boolean superIsChild() {
        return super.method_2662();
    }

    @Override
    public final boolean realIsClientWorld() {
        return this.method_2609();
    }

    @Override
    public final boolean superIsClientWorld() {
        return super.method_2609();
    }

    @Override
    public final boolean superIsEating() {
        return super.isSwimming();
    }

    @Override
    public final boolean superIsEntityAlive() {
        return super.isAlive();
    }

    @Override
    public final boolean superIsEntityEqual(Entity arg) {
        return super.isPartOf(arg);
    }

    @Override
    public final boolean superIsEntityUndead() {
        return super.method_2645();
    }

    @Override
    public final boolean superIsExplosiveMob(Class<?> class_) {
        return super.canAttackEntity(class_);
    }

    @Override
    public final boolean superIsInRangeToRenderDist(double d) {
        return super.shouldRender(d);
    }

    @Override
    public final boolean superIsInRangeToRenderVec3D(Vec3d arg) {
        return super.method_2497(arg);
    }

    @Override
    public final boolean realIsMovementBlocked() {
        return this.method_2610();
    }

    @Override
    public final boolean superIsMovementBlocked() {
        return super.method_2610();
    }

    @Override
    public final boolean superIsOffsetPositionInLiquid(double d, double e, double f) {
        return super.doesNotCollide(d, e, f);
    }

    @Override
    public final boolean realIsPVPEnabled() {
        return this.method_3206();
    }

    @Override
    public final boolean superIsPVPEnabled() {
        return super.method_3206();
    }

    @Override
    public final boolean realIsPlayer() {
        return this.method_2597();
    }

    @Override
    public final boolean superIsPlayer() {
        return super.method_2597();
    }

    @Override
    public final boolean superIsPlayerFullyAsleep() {
        return super.isSleepingLongEnough();
    }

    @Override
    public final boolean superIsPotionActive(StatusEffect arg) {
        return super.method_2581(arg);
    }

    @Override
    public final boolean superIsPotionApplicable(StatusEffectInstance arg) {
        return super.method_2658(arg);
    }

    @Override
    public final boolean superIsRiding() {
        return super.hasVehicle();
    }

    @Override
    public final boolean superIsSneaking() {
        return super.isSneaking();
    }

    @Override
    public final boolean superIsSprinting() {
        return super.isSprinting();
    }

    @Override
    public final boolean superIsUsingItem() {
        return super.isUsingItem();
    }

    @Override
    public final boolean superIsWet() {
        return super.tickFire();
    }

    @Override
    public final boolean superIsWithinHomeDistance(int i, int j, int k) {
        return super.method_2652(i, j, k);
    }

    @Override
    public final boolean superIsWithinHomeDistanceCurrentPosition() {
        return super.method_2589();
    }

    @Override
    public final void realJoinEntityItemWithWorld(ItemEntity arg) {
        this.spawnItemEntity(arg);
    }

    @Override
    public final void superJoinEntityItemWithWorld(ItemEntity arg) {
        super.spawnItemEntity(arg);
    }

    @Override
    public final void realKill() {
        this.destroy();
    }

    @Override
    public final void superKill() {
        super.destroy();
    }

    @Override
    public final void superKnockBack(Entity arg, int i, double d, double e) {
        super.method_2584(arg, i, d, e);
    }

    @Override
    public final void superMountEntity(Entity arg) {
        super.startRiding(arg);
    }

    @Override
    public final NbtList realNewDoubleNBTList(double... ds) {
        return this.toListNbt(ds);
    }

    @Override
    public final NbtList superNewDoubleNBTList(double... ds) {
        return super.toListNbt(ds);
    }

    @Override
    public final NbtList realNewFloatNBTList(float... fs) {
        return this.toListNbt(fs);
    }

    @Override
    public final NbtList superNewFloatNBTList(float... fs) {
        return super.toListNbt(fs);
    }

    @Override
    public final void realOnChangedPotionEffect(StatusEffectInstance arg) {
        this.method_2628(arg);
    }

    @Override
    public final void superOnChangedPotionEffect(StatusEffectInstance arg) {
        super.method_2628(arg);
    }

    @Override
    public final void superOnCollideWithPlayer(PlayerEntity arg) {
        super.onPlayerCollision(arg);
    }

    @Override
    public final void superOnCriticalHit(Entity arg) {
        super.addCritParticles(arg);
    }

    @Override
    public final void realOnDeathUpdate() {
        this.method_2596();
    }

    @Override
    public final void superOnDeathUpdate() {
        super.method_2596();
    }

    @Override
    public final void superOnEnchantmentCritical(Entity arg) {
        super.addEnchantedHitParticles(arg);
    }

    @Override
    public final void superOnEntityUpdate() {
        super.baseTick();
    }

    @Override
    public final void realOnFinishedPotionEffect(StatusEffectInstance arg) {
        this.method_2649(arg);
    }

    @Override
    public final void superOnFinishedPotionEffect(StatusEffectInstance arg) {
        super.method_2649(arg);
    }

    @Override
    public final void superOnItemPickup(Entity arg, int i) {
        super.method_3162(arg, i);
    }

    @Override
    public final void realOnItemUseFinish() {
        this.useItem();
    }

    @Override
    public final void superOnItemUseFinish() {
        super.useItem();
    }

    @Override
    public final void realOnNewPotionEffect(StatusEffectInstance arg) {
        this.method_2582(arg);
    }

    @Override
    public final void superOnNewPotionEffect(StatusEffectInstance arg) {
        super.method_2582(arg);
    }

    @Override
    public final void superOnStruckByLightning(LightningBoltEntity arg) {
        super.onLightningStrike(arg);
    }

    @Override
    public final void superPerformHurtAnimation() {
        super.animateDamage();
    }

    @Override
    public final void superPlayLivingSound() {
        super.playAmbientSound();
    }

    @Override
    public final void realPlayStepSound(int i, int j, int k, int l) {
        this.method_2494(i, j, k, l);
    }

    @Override
    public final void superPlayStepSound(int i, int j, int k, int l) {
        super.method_2494(i, j, k, l);
    }

    @Override
    public final void superPreparePlayerToSpawn() {
        super.afterSpawn();
    }

    @Override
    public final boolean realPushOutOfBlocks(double d, double e, double f) {
        return this.pushOutOfBlocks(d, e, f);
    }

    @Override
    public final boolean superPushOutOfBlocks(double d, double e, double f) {
        return super.pushOutOfBlocks(d, e, f);
    }

    @Override
    public final HitResult superRayTrace(double d, float f) {
        return super.method_2578(d, f);
    }

    @Override
    public final void superReadFromNBT(NbtCompound arg) {
        super.fromNbt(arg);
    }

    @Override
    public final void superRemoveExperience(int i) {
        super.incrementXp(i);
    }

    @Override
    public final void superRemovePotionEffect(int i) {
        super.method_2674(i);
    }

    @Override
    public final void superRenderBrokenItemStack(ItemStack arg) {
        super.method_2586(arg);
    }

    @Override
    public final void realResetHeight() {
        this.method_3203();
    }

    @Override
    public final void superResetHeight() {
        super.method_3203();
    }

    @Override
    public final void superRespawnPlayer() {
        super.requestRespawn();
    }

    @Override
    public final void superSendGameTypeToPlayer(GameMode arg) {
        super.method_3170(arg);
    }

    @Override
    public final void superSendPlayerAbilities() {
        super.sendAbilitiesUpdate();
    }

    @Override
    public final void realSendTileEntityToPlayer(BlockEntity arg) {
        this.updateBlockEntity(arg);
    }

    @Override
    public final void superSetAIMoveSpeed(float f) {
        super.method_2656(f);
    }

    @Override
    public final void superSetAir(int i) {
        super.setAir(i);
    }

    @Override
    public final void superSetAngles(float f, float g) {
        super.increaseTransforms(f, g);
    }

    @Override
    public final void superSetAttackTarget(MobEntity arg) {
        super.method_2629(arg);
    }

    @Override
    public final void realSetBeenAttacked() {
        this.scheduleVelocityUpdate();
    }

    @Override
    public final void superSetBeenAttacked() {
        super.scheduleVelocityUpdate();
    }

    @Override
    public final void superSetEating(boolean bl) {
        super.setSwimming(bl);
    }

    @Override
    public final void superSetEntityHealth(int i) {
        super.method_2668(i);
    }

    @Override
    public final void superSetFire(int i) {
        super.setOnFireFor(i);
    }

    @Override
    public final void realSetFlag(int i, boolean bl) {
        this.setFlag(i, bl);
    }

    @Override
    public final void superSetFlag(int i, boolean bl) {
        super.setFlag(i, bl);
    }

    @Override
    public final void superSetHeadRotationYaw(float f) {
        super.setHeadYaw(f);
    }

    @Override
    public final void superSetHomeArea(int i, int j, int k, int l) {
        super.method_2625(i, j, k, l);
    }

    @Override
    public final void superSetInPortal() {
        super.enterNetherPortal();
    }

    @Override
    public final void superSetInWeb() {
        super.setInLava();
    }

    @Override
    public final void superSetItemInUse(ItemStack arg, int i) {
        super.setUseItem(arg, i);
    }

    @Override
    public final void superSetJumping(boolean bl) {
        super.method_2655(bl);
    }

    @Override
    public final void superSetLastAttackingEntity(Entity arg) {
        super.method_2669(arg);
    }

    @Override
    public final void superSetLocationAndAngles(double d, double e, double f, float g, float h) {
        super.refreshPositionAndAngles(d, e, f, g, h);
    }

    @Override
    public final void superSetMoveForward(float f) {
        super.setForwardSpeed(f);
    }

    @Override
    public final void realSetOnFireFromLava() {
        this.setOnFireFromLava();
    }

    @Override
    public final void superSetOnFireFromLava() {
        super.setOnFireFromLava();
    }

    @Override
    public final void superSetPositionAndRotation(double d, double e, double f, float g, float h) {
        super.updatePositionAndAngles(d, e, f, g, h);
    }

    @Override
    public final void superSetPositionAndRotation2(double d, double e, double f, float g, float h, int i) {
        super.method_2488(d, e, f, g, h, i);
    }

    @Override
    public final void superSetPositionAndUpdate(double d, double e, double f) {
        super.method_2577(d, e, f);
    }

    @Override
    public final void superSetRevengeTarget(MobEntity arg) {
        super.method_2650(arg);
    }

    @Override
    public final void realSetRotation(float f, float g) {
        this.setRotation(f, g);
    }

    @Override
    public final void superSetRotation(float f, float g) {
        super.setRotation(f, g);
    }

    @Override
    public final void realSetSize(float f, float g) {
        this.setBounds(f, g);
    }

    @Override
    public final void superSetSize(float f, float g) {
        super.setBounds(f, g);
    }

    @Override
    public final void superSetSneaking(boolean bl) {
        super.setSneaking(bl);
    }

    @Override
    public final void superSetSpawnChunk(BlockPos arg) {
        super.method_3161(arg);
    }

    @Override
    public final void superSetSprinting(boolean bl) {
        super.setSprinting(bl);
    }

    @Override
    public final void superSetVelocity(double d, double e, double f) {
        super.setVelocityClient(d, e, f);
    }

    @Override
    public final void superSetWorld(World arg) {
        super.setWorld(arg);
    }

    @Override
    public final boolean superShouldHeal() {
        return super.canFoodHeal();
    }

    @Override
    public final CanSleepEnum superSleepInBedAt(int i, int j, int k) {
        return super.method_3152(i, j, k);
    }

    @Override
    public final void superSpawnExplosionParticle() {
        super.playSpawnEffects();
    }

    @Override
    public final void superStopUsingItem() {
        super.stopUsingItem();
    }

    @Override
    public final void superSwingItem() {
        super.method_3207();
    }

    @Override
    public final String superToString() {
        return super.toString();
    }

    @Override
    public final String superTranslateString(String string, Object... objects) {
        return super.translate(string, objects);
    }

    @Override
    public final void superTravelToTheEnd(int i) {
        super.method_3197(i);
    }

    @Override
    public final void superTriggerAchievement(Stat arg) {
        super.incrementStat(arg);
    }

    @Override
    public final void superUnmountEntity(Entity arg) {
        super.method_2557(arg);
    }

    @Override
    public final void realUpdateAITasks() {
        this.mobTick();
    }

    @Override
    public final void superUpdateAITasks() {
        super.mobTick();
    }

    @Override
    public final void realUpdateAITick() {
        this.method_2634();
    }

    @Override
    public final void superUpdateAITick() {
        super.method_2634();
    }

    @Override
    public final void superUpdateCloak() {
        super.method_2510();
    }

    @Override
    public final void realUpdateFallState(double d, boolean bl) {
        this.method_2489(d, bl);
    }

    @Override
    public final void superUpdateFallState(double d, boolean bl) {
        super.method_2489(d, bl);
    }

    @Override
    public final void realUpdateItemUse(ItemStack arg, int i) {
        this.drink(arg, i);
    }

    @Override
    public final void superUpdateItemUse(ItemStack arg, int i) {
        super.drink(arg, i);
    }

    @Override
    public final void realUpdatePotionEffects() {
        this.method_2642();
    }

    @Override
    public final void superUpdatePotionEffects() {
        super.method_2642();
    }

    @Override
    public final void superUpdateRidden() {
        super.tickRiding();
    }

    @Override
    public final void superUpdateRiderPosition() {
        super.updatePassengerPosition();
    }

    @Override
    public final void superWakeUpPlayer(boolean bl, boolean bl2, boolean bl3) {
        super.awaken(bl, bl2, bl3);
    }

    @Override
    public final void superWriteToNBT(NbtCompound arg) {
        super.writePlayerData(arg);
    }

    @Override
    public final int superXpBarCap() {
        return super.getNextLevelExperience();
    }

    @Override
    public final HashMap getActivePotionsMapField() {
        return this.field_3335;
    }

    @Override
    public final void setActivePotionsMapField(HashMap hashMap) {
        this.field_3335 = hashMap;
    }

    @Override
    public final PlayerEntity getAttackingPlayerField() {
        return this.field_3331;
    }

    @Override
    public final void setAttackingPlayerField(PlayerEntity arg) {
        this.field_3331 = arg;
    }

    @Override
    public final int getCarryoverDamageField() {
        return this.field_3296;
    }

    @Override
    public final void setCarryoverDamageField(int i) {
        this.field_3296 = i;
    }

    @Override
    public final boolean getChatColoursField() {
        return this.chatColors;
    }

    @Override
    public final void setChatColoursField(boolean bl) {
        this.chatColors = bl;
    }

    @Override
    public final int getChatVisibilityField() {
        return this.chatFilterLevel;
    }

    @Override
    public final void setChatVisibilityField(int i) {
        this.chatFilterLevel = i;
    }

    @Override
    public final int getCurrentWindowIdField() {
        return this.screenHandlerSyncId;
    }

    @Override
    public final void setCurrentWindowIdField(int i) {
        this.screenHandlerSyncId = i;
    }

    @Override
    public final DataTracker getDataWatcherField() {
        return this.dataTracker;
    }

    @Override
    public final void setDataWatcherField(DataTracker arg) {
        this.dataTracker = arg;
    }

    @Override
    public final boolean getDeadField() {
        return this.field_3304;
    }

    @Override
    public final void setDeadField(boolean bl) {
        this.field_3304 = bl;
    }

    @Override
    public final float getDefaultPitchField() {
        return this.field_3351;
    }

    @Override
    public final void setDefaultPitchField(float f) {
        this.field_3351 = f;
    }

    @Override
    public final int getEntityAgeField() {
        return this.field_3346;
    }

    @Override
    public final void setEntityAgeField(int i) {
        this.field_3346 = i;
    }

    @Override
    public final String getEntityTypeField() {
        return this.field_3285;
    }

    @Override
    public final void setEntityTypeField(String string) {
        this.field_3285 = string;
    }

    @Override
    public final int getExperienceValueField() {
        return this.experiencePoints;
    }

    @Override
    public final void setExperienceValueField(int i) {
        this.experiencePoints = i;
    }

    @Override
    public final float getField_70706_boField() {
        return ((IMobEntity)this).getField_3344();
    }

    @Override
    public final void setField_70706_boField(float f) {
        ((IMobEntity)this).setField_3344(f);
    }

    @Override
    public final boolean getField_70740_aAField() {
        return this.field_3283;
    }

    @Override
    public final void setField_70740_aAField(boolean bl) {
        this.field_3283 = bl;
    }

    @Override
    public final float getField_70741_aBField() {
        return this.field_3284;
    }

    @Override
    public final void setField_70741_aBField(float f) {
        this.field_3284 = f;
    }

    @Override
    public final float getField_70743_aDField() {
        return this.field_3286;
    }

    @Override
    public final void setField_70743_aDField(float f) {
        this.field_3286 = f;
    }

    @Override
    public final float getField_70745_aFField() {
        return this.field_3288;
    }

    @Override
    public final void setField_70745_aFField(float f) {
        this.field_3288 = f;
    }

    @Override
    public final boolean getField_70753_ayField() {
        return this.field_3321;
    }

    @Override
    public final void setField_70753_ayField(boolean bl) {
        this.field_3321 = bl;
    }

    @Override
    public final float getField_70763_axField() {
        return this.field_3320;
    }

    @Override
    public final void setField_70763_axField(float f) {
        this.field_3320 = f;
    }

    @Override
    public final float getField_70764_awField() {
        return this.field_3319;
    }

    @Override
    public final void setField_70764_awField(float f) {
        this.field_3319 = f;
    }

    @Override
    public final float getField_70766_avField() {
        return this.field_3318;
    }

    @Override
    public final void setField_70766_avField(float f) {
        this.field_3318 = f;
    }

    @Override
    public final float getField_70768_auField() {
        return this.field_3317;
    }

    @Override
    public final void setField_70768_auField(float f) {
        this.field_3317 = f;
    }

    @Override
    public final int getFlyToggleTimerField() {
        return this.abilityResyncCountdown;
    }

    @Override
    public final void setFlyToggleTimerField(int i) {
        this.abilityResyncCountdown = i;
    }

    @Override
    public final HungerManager getFoodStatsField() {
        return this.hungerManager;
    }

    @Override
    public final void setFoodStatsField(HungerManager arg) {
        this.hungerManager = arg;
    }

    @Override
    public final int getHealthField() {
        return this.field_3294;
    }

    @Override
    public final void setHealthField(int i) {
        this.field_3294 = i;
    }

    @Override
    public final boolean getInPortalField() {
        return this.field_3996;
    }

    @Override
    public final void setInPortalField(boolean bl) {
        this.field_3996 = bl;
    }

    @Override
    public final boolean getInWaterField() {
        return this.touchingWater;
    }

    @Override
    public final void setInWaterField(boolean bl) {
        this.touchingWater = bl;
    }

    @Override
    public final int getInitialInvulnerabilityField() {
        return this.spawnProtectionTicks;
    }

    @Override
    public final void setInitialInvulnerabilityField(int i) {
        this.spawnProtectionTicks = i;
    }

    @Override
    public final boolean getIsImmuneToFireField() {
        return this.isFireImmune;
    }

    @Override
    public final void setIsImmuneToFireField(boolean bl) {
        this.isFireImmune = bl;
    }

    @Override
    public final boolean getIsInWebField() {
        return this.inLava;
    }

    @Override
    public final void setIsInWebField(boolean bl) {
        this.inLava = bl;
    }

    @Override
    public final boolean getIsJumpingField() {
        return this.field_3350;
    }

    @Override
    public final void setIsJumpingField(boolean bl) {
        this.field_3350 = bl;
    }

    @Override
    public final ItemStack[] getLastActiveItemsField() {
        return this.field_2835;
    }

    @Override
    public final void setLastActiveItemsField(ItemStack[] args) {
        this.field_2835 = args;
    }

    @Override
    public final int getLastDamageField() {
        return this.field_3345;
    }

    @Override
    public final void setLastDamageField(int i) {
        this.field_3345 = i;
    }

    @Override
    public final int getLastExperienceField() {
        return this.lastXp;
    }

    @Override
    public final void setLastExperienceField(int i) {
        this.lastXp = i;
    }

    @Override
    public final int getLastFoodLevelField() {
        return this.lastHungerLevel;
    }

    @Override
    public final void setLastFoodLevelField(int i) {
        this.lastHungerLevel = i;
    }

    @Override
    public final int getLastHealthField() {
        return this.field_2827;
    }

    @Override
    public final void setLastHealthField(int i) {
        this.field_2827 = i;
    }

    @Override
    public final float getMoveForwardField() {
        return this.field_3348;
    }

    @Override
    public final void setMoveForwardField(float f) {
        this.field_3348 = f;
    }

    @Override
    public final float getMoveSpeedField() {
        return this.field_3352;
    }

    @Override
    public final void setMoveSpeedField(float f) {
        this.field_3352 = f;
    }

    @Override
    public final float getMoveStrafingField() {
        return this.field_3347;
    }

    @Override
    public final void setMoveStrafingField(float f) {
        this.field_3347 = f;
    }

    @Override
    public final int getNewPosRotationIncrementsField() {
        return this.field_3338;
    }

    @Override
    public final void setNewPosRotationIncrementsField(int i) {
        this.field_3338 = i;
    }

    @Override
    public final double getNewPosXField() {
        return this.field_3339;
    }

    @Override
    public final void setNewPosXField(double d) {
        this.field_3339 = d;
    }

    @Override
    public final double getNewPosYField() {
        return this.field_3340;
    }

    @Override
    public final void setNewPosYField(double d) {
        this.field_3340 = d;
    }

    @Override
    public final double getNewPosZField() {
        return this.field_3341;
    }

    @Override
    public final void setNewPosZField(double d) {
        this.field_3341 = d;
    }

    @Override
    public final double getNewRotationPitchField() {
        return this.field_3343;
    }

    @Override
    public final void setNewRotationPitchField(double d) {
        this.field_3343 = d;
    }

    @Override
    public final double getNewRotationYawField() {
        return this.field_3342;
    }

    @Override
    public final void setNewRotationYawField(double d) {
        this.field_3342 = d;
    }

    @Override
    public final int getNumTicksToChaseTargetField() {
        return this.field_3353;
    }

    @Override
    public final void setNumTicksToChaseTargetField(int i) {
        this.field_3353 = i;
    }

    @Override
    public final Random getRandField() {
        return this.random;
    }

    @Override
    public final void setRandField(Random random) {
        this.random = random;
    }

    @Override
    public final float getRandomYawVelocityField() {
        return this.field_3349;
    }

    @Override
    public final void setRandomYawVelocityField(float f) {
        this.field_3349 = f;
    }

    @Override
    public final int getRecentlyHitField() {
        return this.field_3332;
    }

    @Override
    public final void setRecentlyHitField(int i) {
        this.field_3332 = i;
    }

    @Override
    public final int getRenderDistanceField() {
        return this.viewDistance;
    }

    @Override
    public final void setRenderDistanceField(int i) {
        this.viewDistance = i;
    }

    @Override
    public final int getScoreValueField() {
        return this.field_3287;
    }

    @Override
    public final void setScoreValueField(int i) {
        this.field_3287 = i;
    }

    @Override
    public final boolean getSleepingField() {
        return this.inBed;
    }

    @Override
    public final void setSleepingField(boolean bl) {
        this.inBed = bl;
    }

    @Override
    public final float getSpeedInAirField() {
        return this.field_4006;
    }

    @Override
    public final void setSpeedInAirField(float f) {
        this.field_4006 = f;
    }

    @Override
    public final float getSpeedOnGroundField() {
        return this.field_4005;
    }

    @Override
    public final void setSpeedOnGroundField(float f) {
        this.field_4005 = f;
    }

    @Override
    public final GoalSelector getTargetTasksField() {
        return this.attackGoals;
    }

    @Override
    public final GoalSelector getTasksField() {
        return this.goals;
    }

    @Override
    public final String getTextureField() {
        return this.field_3322;
    }

    @Override
    public final void setTextureField(String string) {
        this.field_3322 = string;
    }

    @Override
    public final Language getTranslatorField() {
        return this.language;
    }

    @Override
    public final void setTranslatorField(Language arg) {
        this.language = arg;
    }

    @Override
    public final boolean getWasHungryField() {
        return this.wasHungry;
    }

    @Override
    public final void setWasHungryField(boolean bl) {
        this.wasHungry = bl;
    }

    @Override
    public ServerPlayerAPI getServerPlayerAPI() {
        return this.serverPlayerAPI;
    }
}
