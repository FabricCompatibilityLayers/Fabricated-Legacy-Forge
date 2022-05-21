//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.minecraft;

import fr.catcore.fabricatedforge.mixininterface.IPlayerAPIClientPlayerEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.Session;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.stat.Stat;
import net.minecraft.util.CanSleepEnum;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public abstract class PlayerBase {
    protected final ClientPlayerEntity player;
    private final PlayerAPI playerAPI;

    public PlayerBase(PlayerAPI playerAPI) {
        this.playerAPI = playerAPI;
        this.player = playerAPI.player;
    }

    public void beforeLocalConstructing(Minecraft minecraft, World arg, Session arg2, int i) {
    }

    public void afterLocalConstructing(Minecraft minecraft, World arg, Session arg2, int i) {
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public void beforeAddExhaustion(float f) {
    }

    public void addExhaustion(float f) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenAddExhaustion(this);
        if (var2 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localAddExhaustion(f);
        } else if (var2 != this) {
            var2.addExhaustion(f);
        }

    }

    public void afterAddExhaustion(float f) {
    }

    public void beforeAddMovementStat(double d, double e, double f) {
    }

    public void addMovementStat(double d, double e, double f) {
        PlayerBase var7 = this.playerAPI.GetOverwrittenAddMovementStat(this);
        if (var7 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localAddMovementStat(d, e, f);
        } else if (var7 != this) {
            var7.addMovementStat(d, e, f);
        }

    }

    public void afterAddMovementStat(double d, double e, double f) {
    }

    public void beforeAddStat(Stat arg, int i) {
    }

    public void addStat(Stat arg, int i) {
        PlayerBase var3 = this.playerAPI.GetOverwrittenAddStat(this);
        if (var3 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localAddStat(arg, i);
        } else if (var3 != this) {
            var3.addStat(arg, i);
        }

    }

    public void afterAddStat(Stat arg, int i) {
    }

    public void beforeAttackEntityFrom(DamageSource arg, int i) {
    }

    public boolean attackEntityFrom(DamageSource arg, int i) {
        PlayerBase var3 = this.playerAPI.GetOverwrittenAttackEntityFrom(this);
        boolean var4;
        if (var3 == null) {
            var4 = ((IPlayerAPIClientPlayerEntity)this.player).localAttackEntityFrom(arg, i);
        } else if (var3 != this) {
            var4 = var3.attackEntityFrom(arg, i);
        } else {
            var4 = false;
        }

        return var4;
    }

    public void afterAttackEntityFrom(DamageSource arg, int i) {
    }

    public void beforeAlertWolves(MobEntity arg, boolean bl) {
    }

    public void alertWolves(MobEntity arg, boolean bl) {
        PlayerBase var3 = this.playerAPI.GetOverwrittenAlertWolves(this);
        if (var3 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localAlertWolves(arg, bl);
        } else if (var3 != this) {
            var3.alertWolves(arg, bl);
        }

    }

    public void afterAlertWolves(MobEntity arg, boolean bl) {
    }

    public void beforeAttackTargetEntityWithCurrentItem(Entity arg) {
    }

    public void attackTargetEntityWithCurrentItem(Entity arg) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenAttackTargetEntityWithCurrentItem(this);
        if (var2 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localAttackTargetEntityWithCurrentItem(arg);
        } else if (var2 != this) {
            var2.attackTargetEntityWithCurrentItem(arg);
        }

    }

    public void afterAttackTargetEntityWithCurrentItem(Entity arg) {
    }

    public void beforeCanBreatheUnderwater() {
    }

    public boolean canBreatheUnderwater() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenCanBreatheUnderwater(this);
        boolean var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIClientPlayerEntity)this.player).localCanBreatheUnderwater();
        } else if (var1 != this) {
            var2 = var1.canBreatheUnderwater();
        } else {
            var2 = false;
        }

        return var2;
    }

    public void afterCanBreatheUnderwater() {
    }

    public void beforeCanHarvestBlock(Block arg) {
    }

    public boolean canHarvestBlock(Block arg) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenCanHarvestBlock(this);
        boolean var3;
        if (var2 == null) {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localCanHarvestBlock(arg);
        } else if (var2 != this) {
            var3 = var2.canHarvestBlock(arg);
        } else {
            var3 = false;
        }

        return var3;
    }

    public void afterCanHarvestBlock(Block arg) {
    }

    public void beforeCanPlayerEdit(int i, int j, int k) {
    }

    public boolean canPlayerEdit(int i, int j, int k) {
        PlayerBase var4 = this.playerAPI.GetOverwrittenCanPlayerEdit(this);
        boolean var5;
        if (var4 == null) {
            var5 = ((IPlayerAPIClientPlayerEntity)this.player).localCanPlayerEdit(i, j, k);
        } else if (var4 != this) {
            var5 = var4.canPlayerEdit(i, j, k);
        } else {
            var5 = false;
        }

        return var5;
    }

    public void afterCanPlayerEdit(int i, int j, int k) {
    }

    public void beforeCanTriggerWalking() {
    }

    public boolean canTriggerWalking() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenCanTriggerWalking(this);
        boolean var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIClientPlayerEntity)this.player).localCanTriggerWalking();
        } else if (var1 != this) {
            var2 = var1.canTriggerWalking();
        } else {
            var2 = false;
        }

        return var2;
    }

    public void afterCanTriggerWalking() {
    }

    public void beforeCloseScreen() {
    }

    public void closeScreen() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenCloseScreen(this);
        if (var1 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localCloseScreen();
        } else if (var1 != this) {
            var1.closeScreen();
        }

    }

    public void afterCloseScreen() {
    }

    public void beforeDamageEntity(DamageSource arg, int i) {
    }

    public void damageEntity(DamageSource arg, int i) {
        PlayerBase var3 = this.playerAPI.GetOverwrittenDamageEntity(this);
        if (var3 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localDamageEntity(arg, i);
        } else if (var3 != this) {
            var3.damageEntity(arg, i);
        }

    }

    public void afterDamageEntity(DamageSource arg, int i) {
    }

    public void beforeDisplayGUIBrewingStand(BrewingStandBlockEntity arg) {
    }

    public void displayGUIBrewingStand(BrewingStandBlockEntity arg) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenDisplayGUIBrewingStand(this);
        if (var2 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localDisplayGUIBrewingStand(arg);
        } else if (var2 != this) {
            var2.displayGUIBrewingStand(arg);
        }

    }

    public void afterDisplayGUIBrewingStand(BrewingStandBlockEntity arg) {
    }

    public void beforeDisplayGUIChest(Inventory arg) {
    }

    public void displayGUIChest(Inventory arg) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenDisplayGUIChest(this);
        if (var2 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localDisplayGUIChest(arg);
        } else if (var2 != this) {
            var2.displayGUIChest(arg);
        }

    }

    public void afterDisplayGUIChest(Inventory arg) {
    }

    public void beforeDisplayGUIDispenser(DispenserBlockEntity arg) {
    }

    public void displayGUIDispenser(DispenserBlockEntity arg) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenDisplayGUIDispenser(this);
        if (var2 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localDisplayGUIDispenser(arg);
        } else if (var2 != this) {
            var2.displayGUIDispenser(arg);
        }

    }

    public void afterDisplayGUIDispenser(DispenserBlockEntity arg) {
    }

    public void beforeDisplayGUIEditSign(SignBlockEntity arg) {
    }

    public void displayGUIEditSign(SignBlockEntity arg) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenDisplayGUIEditSign(this);
        if (var2 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localDisplayGUIEditSign(arg);
        } else if (var2 != this) {
            var2.displayGUIEditSign(arg);
        }

    }

    public void afterDisplayGUIEditSign(SignBlockEntity arg) {
    }

    public void beforeDisplayGUIEnchantment(int i, int j, int k) {
    }

    public void displayGUIEnchantment(int i, int j, int k) {
        PlayerBase var4 = this.playerAPI.GetOverwrittenDisplayGUIEnchantment(this);
        if (var4 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localDisplayGUIEnchantment(i, j, k);
        } else if (var4 != this) {
            var4.displayGUIEnchantment(i, j, k);
        }

    }

    public void afterDisplayGUIEnchantment(int i, int j, int k) {
    }

    public void beforeDisplayGUIFurnace(FurnaceBlockEntity arg) {
    }

    public void displayGUIFurnace(FurnaceBlockEntity arg) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenDisplayGUIFurnace(this);
        if (var2 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localDisplayGUIFurnace(arg);
        } else if (var2 != this) {
            var2.displayGUIFurnace(arg);
        }

    }

    public void afterDisplayGUIFurnace(FurnaceBlockEntity arg) {
    }

    public void beforeDisplayWorkbenchGUI(int i, int j, int k) {
    }

    public void displayWorkbenchGUI(int i, int j, int k) {
        PlayerBase var4 = this.playerAPI.GetOverwrittenDisplayWorkbenchGUI(this);
        if (var4 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localDisplayWorkbenchGUI(i, j, k);
        } else if (var4 != this) {
            var4.displayWorkbenchGUI(i, j, k);
        }

    }

    public void afterDisplayWorkbenchGUI(int i, int j, int k) {
    }

    public void beforeDropOneItem() {
    }

    public ItemEntity dropOneItem() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenDropOneItem(this);
        ItemEntity var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIClientPlayerEntity)this.player).localDropOneItem();
        } else if (var1 != this) {
            var2 = var1.dropOneItem();
        } else {
            var2 = null;
        }

        return var2;
    }

    public void afterDropOneItem() {
    }

    public void beforeDropPlayerItem(ItemStack arg) {
    }

    public ItemEntity dropPlayerItem(ItemStack arg) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenDropPlayerItem(this);
        ItemEntity var3;
        if (var2 == null) {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localDropPlayerItem(arg);
        } else if (var2 != this) {
            var3 = var2.dropPlayerItem(arg);
        } else {
            var3 = null;
        }

        return var3;
    }

    public void afterDropPlayerItem(ItemStack arg) {
    }

    public void beforeDropPlayerItemWithRandomChoice(ItemStack arg, boolean bl) {
    }

    public ItemEntity dropPlayerItemWithRandomChoice(ItemStack arg, boolean bl) {
        PlayerBase var3 = this.playerAPI.GetOverwrittenDropPlayerItemWithRandomChoice(this);
        ItemEntity var4;
        if (var3 == null) {
            var4 = ((IPlayerAPIClientPlayerEntity)this.player).localDropPlayerItemWithRandomChoice(arg, bl);
        } else if (var3 != this) {
            var4 = var3.dropPlayerItemWithRandomChoice(arg, bl);
        } else {
            var4 = null;
        }

        return var4;
    }

    public void afterDropPlayerItemWithRandomChoice(ItemStack arg, boolean bl) {
    }

    public void beforeFall(float f) {
    }

    public void fall(float f) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenFall(this);
        if (var2 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localFall(f);
        } else if (var2 != this) {
            var2.fall(f);
        }

    }

    public void afterFall(float f) {
    }

    public void beforeGetBrightness(float f) {
    }

    public float getBrightness(float f) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenGetBrightness(this);
        float var3;
        if (var2 == null) {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localGetBrightness(f);
        } else if (var2 != this) {
            var3 = var2.getBrightness(f);
        } else {
            var3 = 0.0F;
        }

        return var3;
    }

    public void afterGetBrightness(float f) {
    }

    public void beforeGetBrightnessForRender(float f) {
    }

    public int getBrightnessForRender(float f) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenGetBrightnessForRender(this);
        int var3;
        if (var2 == null) {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localGetBrightnessForRender(f);
        } else if (var2 != this) {
            var3 = var2.getBrightnessForRender(f);
        } else {
            var3 = 0;
        }

        return var3;
    }

    public void afterGetBrightnessForRender(float f) {
    }

    public void beforeGetCurrentPlayerStrVsBlock(Block arg) {
    }

    public float getCurrentPlayerStrVsBlock(Block arg) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenGetCurrentPlayerStrVsBlock(this);
        float var3;
        if (var2 == null) {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localGetCurrentPlayerStrVsBlock(arg);
        } else if (var2 != this) {
            var3 = var2.getCurrentPlayerStrVsBlock(arg);
        } else {
            var3 = 0.0F;
        }

        return var3;
    }

    public void afterGetCurrentPlayerStrVsBlock(Block arg) {
    }

    public void beforeGetDistanceSq(double d, double e, double f) {
    }

    public double getDistanceSq(double d, double e, double f) {
        PlayerBase var7 = this.playerAPI.GetOverwrittenGetDistanceSq(this);
        double var8;
        if (var7 == null) {
            var8 = ((IPlayerAPIClientPlayerEntity)this.player).localGetDistanceSq(d, e, f);
        } else if (var7 != this) {
            var8 = var7.getDistanceSq(d, e, f);
        } else {
            var8 = 0.0;
        }

        return var8;
    }

    public void afterGetDistanceSq(double d, double e, double f) {
    }

    public void beforeGetDistanceSqToEntity(Entity arg) {
    }

    public double getDistanceSqToEntity(Entity arg) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenGetDistanceSqToEntity(this);
        double var3;
        if (var2 == null) {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localGetDistanceSqToEntity(arg);
        } else if (var2 != this) {
            var3 = var2.getDistanceSqToEntity(arg);
        } else {
            var3 = 0.0;
        }

        return var3;
    }

    public void afterGetDistanceSqToEntity(Entity arg) {
    }

    public void beforeGetFOVMultiplier() {
    }

    public float getFOVMultiplier() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenGetFOVMultiplier(this);
        float var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIClientPlayerEntity)this.player).localGetFOVMultiplier();
        } else if (var1 != this) {
            var2 = var1.getFOVMultiplier();
        } else {
            var2 = 0.0F;
        }

        return var2;
    }

    public void afterGetFOVMultiplier() {
    }

    public void beforeGetHurtSound() {
    }

    public String getHurtSound() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenGetHurtSound(this);
        String var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIClientPlayerEntity)this.player).localGetHurtSound();
        } else if (var1 != this) {
            var2 = var1.getHurtSound();
        } else {
            var2 = null;
        }

        return var2;
    }

    public void afterGetHurtSound() {
    }

    public void beforeGetItemIcon(ItemStack arg, int i) {
    }

    public int getItemIcon(ItemStack arg, int i) {
        PlayerBase var3 = this.playerAPI.GetOverwrittenGetItemIcon(this);
        int var4;
        if (var3 == null) {
            var4 = ((IPlayerAPIClientPlayerEntity)this.player).localGetItemIcon(arg, i);
        } else if (var3 != this) {
            var4 = var3.getItemIcon(arg, i);
        } else {
            var4 = 0;
        }

        return var4;
    }

    public void afterGetItemIcon(ItemStack arg, int i) {
    }

    public void beforeGetSleepTimer() {
    }

    public int getSleepTimer() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenGetSleepTimer(this);
        int var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIClientPlayerEntity)this.player).localGetSleepTimer();
        } else if (var1 != this) {
            var2 = var1.getSleepTimer();
        } else {
            var2 = 0;
        }

        return var2;
    }

    public void afterGetSleepTimer() {
    }

    public void beforeGetSpeedModifier() {
    }

    public float getSpeedModifier() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenGetSpeedModifier(this);
        float var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIClientPlayerEntity)this.player).localGetSpeedModifier();
        } else if (var1 != this) {
            var2 = var1.getSpeedModifier();
        } else {
            var2 = 0.0F;
        }

        return var2;
    }

    public void afterGetSpeedModifier() {
    }

    public void beforeHandleLavaMovement() {
    }

    public boolean handleLavaMovement() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenHandleLavaMovement(this);
        boolean var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIClientPlayerEntity)this.player).localHandleLavaMovement();
        } else if (var1 != this) {
            var2 = var1.handleLavaMovement();
        } else {
            var2 = false;
        }

        return var2;
    }

    public void afterHandleLavaMovement() {
    }

    public void beforeHandleWaterMovement() {
    }

    public boolean handleWaterMovement() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenHandleWaterMovement(this);
        boolean var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIClientPlayerEntity)this.player).localHandleWaterMovement();
        } else if (var1 != this) {
            var2 = var1.handleWaterMovement();
        } else {
            var2 = false;
        }

        return var2;
    }

    public void afterHandleWaterMovement() {
    }

    public void beforeHeal(int i) {
    }

    public void heal(int i) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenHeal(this);
        if (var2 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localHeal(i);
        } else if (var2 != this) {
            var2.heal(i);
        }

    }

    public void afterHeal(int i) {
    }

    public void beforeIsEntityInsideOpaqueBlock() {
    }

    public boolean isEntityInsideOpaqueBlock() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenIsEntityInsideOpaqueBlock(this);
        boolean var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIClientPlayerEntity)this.player).localIsEntityInsideOpaqueBlock();
        } else if (var1 != this) {
            var2 = var1.isEntityInsideOpaqueBlock();
        } else {
            var2 = false;
        }

        return var2;
    }

    public void afterIsEntityInsideOpaqueBlock() {
    }

    public void beforeIsInWater() {
    }

    public boolean isInWater() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenIsInWater(this);
        boolean var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIClientPlayerEntity)this.player).localIsInWater();
        } else if (var1 != this) {
            var2 = var1.isInWater();
        } else {
            var2 = false;
        }

        return var2;
    }

    public void afterIsInWater() {
    }

    public void beforeIsInsideOfMaterial(Material arg) {
    }

    public boolean isInsideOfMaterial(Material arg) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenIsInsideOfMaterial(this);
        boolean var3;
        if (var2 == null) {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localIsInsideOfMaterial(arg);
        } else if (var2 != this) {
            var3 = var2.isInsideOfMaterial(arg);
        } else {
            var3 = false;
        }

        return var3;
    }

    public void afterIsInsideOfMaterial(Material arg) {
    }

    public void beforeIsOnLadder() {
    }

    public boolean isOnLadder() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenIsOnLadder(this);
        boolean var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIClientPlayerEntity)this.player).localIsOnLadder();
        } else if (var1 != this) {
            var2 = var1.isOnLadder();
        } else {
            var2 = false;
        }

        return var2;
    }

    public void afterIsOnLadder() {
    }

    public void beforeIsSneaking() {
    }

    public boolean isSneaking() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenIsSneaking(this);
        boolean var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIClientPlayerEntity)this.player).localIsSneaking();
        } else if (var1 != this) {
            var2 = var1.isSneaking();
        } else {
            var2 = false;
        }

        return var2;
    }

    public void afterIsSneaking() {
    }

    public void beforeIsSprinting() {
    }

    public boolean isSprinting() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenIsSprinting(this);
        boolean var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIClientPlayerEntity)this.player).localIsSprinting();
        } else if (var1 != this) {
            var2 = var1.isSprinting();
        } else {
            var2 = false;
        }

        return var2;
    }

    public void afterIsSprinting() {
    }

    public void beforeJump() {
    }

    public void jump() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenJump(this);
        if (var1 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localJump();
        } else if (var1 != this) {
            var1.jump();
        }

    }

    public void afterJump() {
    }

    public void beforeKnockBack(Entity arg, int i, double d, double e) {
    }

    public void knockBack(Entity arg, int i, double d, double e) {
        PlayerBase var7 = this.playerAPI.GetOverwrittenKnockBack(this);
        if (var7 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localKnockBack(arg, i, d, e);
        } else if (var7 != this) {
            var7.knockBack(arg, i, d, e);
        }

    }

    public void afterKnockBack(Entity arg, int i, double d, double e) {
    }

    public void beforeMoveEntity(double d, double e, double f) {
    }

    public void moveEntity(double d, double e, double f) {
        PlayerBase var7 = this.playerAPI.GetOverwrittenMoveEntity(this);
        if (var7 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localMoveEntity(d, e, f);
        } else if (var7 != this) {
            var7.moveEntity(d, e, f);
        }

    }

    public void afterMoveEntity(double d, double e, double f) {
    }

    public void beforeMoveEntityWithHeading(float f, float g) {
    }

    public void moveEntityWithHeading(float f, float g) {
        PlayerBase var3 = this.playerAPI.GetOverwrittenMoveEntityWithHeading(this);
        if (var3 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localMoveEntityWithHeading(f, g);
        } else if (var3 != this) {
            var3.moveEntityWithHeading(f, g);
        }

    }

    public void afterMoveEntityWithHeading(float f, float g) {
    }

    public void beforeMoveFlying(float f, float g, float h) {
    }

    public void moveFlying(float f, float g, float h) {
        PlayerBase var4 = this.playerAPI.GetOverwrittenMoveFlying(this);
        if (var4 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localMoveFlying(f, g, h);
        } else if (var4 != this) {
            var4.moveFlying(f, g, h);
        }

    }

    public void afterMoveFlying(float f, float g, float h) {
    }

    public void beforeOnDeath(DamageSource arg) {
    }

    public void onDeath(DamageSource arg) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenOnDeath(this);
        if (var2 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localOnDeath(arg);
        } else if (var2 != this) {
            var2.onDeath(arg);
        }

    }

    public void afterOnDeath(DamageSource arg) {
    }

    public void beforeOnLivingUpdate() {
    }

    public void onLivingUpdate() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenOnLivingUpdate(this);
        if (var1 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localOnLivingUpdate();
        } else if (var1 != this) {
            var1.onLivingUpdate();
        }

    }

    public void afterOnLivingUpdate() {
    }

    public void beforeOnKillEntity(MobEntity arg) {
    }

    public void onKillEntity(MobEntity arg) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenOnKillEntity(this);
        if (var2 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localOnKillEntity(arg);
        } else if (var2 != this) {
            var2.onKillEntity(arg);
        }

    }

    public void afterOnKillEntity(MobEntity arg) {
    }

    public void beforeOnUpdate() {
    }

    public void onUpdate() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenOnUpdate(this);
        if (var1 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localOnUpdate();
        } else if (var1 != this) {
            var1.onUpdate();
        }

    }

    public void afterOnUpdate() {
    }

    public void beforePlayStepSound(int i, int j, int k, int l) {
    }

    public void playStepSound(int i, int j, int k, int l) {
        PlayerBase var5 = this.playerAPI.GetOverwrittenPlayStepSound(this);
        if (var5 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localPlayStepSound(i, j, k, l);
        } else if (var5 != this) {
            var5.playStepSound(i, j, k, l);
        }

    }

    public void afterPlayStepSound(int i, int j, int k, int l) {
    }

    public void beforePushOutOfBlocks(double d, double e, double f) {
    }

    public boolean pushOutOfBlocks(double d, double e, double f) {
        PlayerBase var7 = this.playerAPI.GetOverwrittenPushOutOfBlocks(this);
        boolean var8;
        if (var7 == null) {
            var8 = ((IPlayerAPIClientPlayerEntity)this.player).localPushOutOfBlocks(d, e, f);
        } else if (var7 != this) {
            var8 = var7.pushOutOfBlocks(d, e, f);
        } else {
            var8 = false;
        }

        return var8;
    }

    public void afterPushOutOfBlocks(double d, double e, double f) {
    }

    public void beforeRayTrace(double d, float f) {
    }

    public HitResult rayTrace(double d, float f) {
        PlayerBase var4 = this.playerAPI.GetOverwrittenRayTrace(this);
        HitResult var5;
        if (var4 == null) {
            var5 = ((IPlayerAPIClientPlayerEntity)this.player).localRayTrace(d, f);
        } else if (var4 != this) {
            var5 = var4.rayTrace(d, f);
        } else {
            var5 = null;
        }

        return var5;
    }

    public void afterRayTrace(double d, float f) {
    }

    public void beforeReadEntityFromNBT(NbtCompound arg) {
    }

    public void readEntityFromNBT(NbtCompound arg) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenReadEntityFromNBT(this);
        if (var2 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localReadEntityFromNBT(arg);
        } else if (var2 != this) {
            var2.readEntityFromNBT(arg);
        }

    }

    public void afterReadEntityFromNBT(NbtCompound arg) {
    }

    public void beforeRespawnPlayer() {
    }

    public void respawnPlayer() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenRespawnPlayer(this);
        if (var1 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localRespawnPlayer();
        } else if (var1 != this) {
            var1.respawnPlayer();
        }

    }

    public void afterRespawnPlayer() {
    }

    public void beforeSetDead() {
    }

    public void setDead() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenSetDead(this);
        if (var1 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localSetDead();
        } else if (var1 != this) {
            var1.setDead();
        }

    }

    public void afterSetDead() {
    }

    public void beforeSetPositionAndRotation(double d, double e, double f, float g, float h) {
    }

    public void setPositionAndRotation(double d, double e, double f, float g, float h) {
        PlayerBase var9 = this.playerAPI.GetOverwrittenSetPositionAndRotation(this);
        if (var9 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localSetPositionAndRotation(d, e, f, g, h);
        } else if (var9 != this) {
            var9.setPositionAndRotation(d, e, f, g, h);
        }

    }

    public void afterSetPositionAndRotation(double d, double e, double f, float g, float h) {
    }

    public void beforeSleepInBedAt(int i, int j, int k) {
    }

    public CanSleepEnum sleepInBedAt(int i, int j, int k) {
        PlayerBase var4 = this.playerAPI.GetOverwrittenSleepInBedAt(this);
        CanSleepEnum var5;
        if (var4 == null) {
            var5 = ((IPlayerAPIClientPlayerEntity)this.player).localSleepInBedAt(i, j, k);
        } else if (var4 != this) {
            var5 = var4.sleepInBedAt(i, j, k);
        } else {
            var5 = null;
        }

        return var5;
    }

    public void afterSleepInBedAt(int i, int j, int k) {
    }

    public void beforeSwingItem() {
    }

    public void swingItem() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenSwingItem(this);
        if (var1 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localSwingItem();
        } else if (var1 != this) {
            var1.swingItem();
        }

    }

    public void afterSwingItem() {
    }

    public void beforeUpdateEntityActionState() {
    }

    public void updateEntityActionState() {
        PlayerBase var1 = this.playerAPI.GetOverwrittenUpdateEntityActionState(this);
        if (var1 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localUpdateEntityActionState();
        } else if (var1 != this) {
            var1.updateEntityActionState();
        }

    }

    public void afterUpdateEntityActionState() {
    }

    public void beforeWriteEntityToNBT(NbtCompound arg) {
    }

    public void writeEntityToNBT(NbtCompound arg) {
        PlayerBase var2 = this.playerAPI.GetOverwrittenWriteEntityToNBT(this);
        if (var2 == null) {
            ((IPlayerAPIClientPlayerEntity)this.player).localWriteEntityToNBT(arg);
        } else if (var2 != this) {
            var2.writeEntityToNBT(arg);
        }

    }

    public void afterWriteEntityToNBT(NbtCompound arg) {
    }
}
