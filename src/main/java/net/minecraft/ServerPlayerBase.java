//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.minecraft;

import fr.catcore.fabricatedforge.mixininterface.IPlayerAPIServerPlayerEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.World;

public abstract class ServerPlayerBase {
    protected final ServerPlayerEntity player;
    private final ServerPlayerAPI playerAPI;

    public ServerPlayerBase(ServerPlayerAPI serverPlayerAPI) {
        this.playerAPI = serverPlayerAPI;
        this.player = serverPlayerAPI.player;
    }

    public void beforeLocalConstructing(MinecraftServer minecraftServer, World arg, String string, ServerPlayerInteractionManager arg2) {
    }

    public void afterLocalConstructing(MinecraftServer minecraftServer, World arg, String string, ServerPlayerInteractionManager arg2) {
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public void beforeAttackEntityFrom(DamageSource arg, int i) {
    }

    public boolean attackEntityFrom(DamageSource arg, int i) {
        ServerPlayerBase var3 = this.playerAPI.GetOverwrittenAttackEntityFrom(this);
        boolean var4;
        if (var3 == null) {
            var4 = ((IPlayerAPIServerPlayerEntity)this.player).localAttackEntityFrom(arg, i);
        } else if (var3 != this) {
            var4 = var3.attackEntityFrom(arg, i);
        } else {
            var4 = false;
        }

        return var4;
    }

    public void afterAttackEntityFrom(DamageSource arg, int i) {
    }

    public void beforeAttackTargetEntityWithCurrentItem(Entity arg) {
    }

    public void attackTargetEntityWithCurrentItem(Entity arg) {
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenAttackTargetEntityWithCurrentItem(this);
        if (var2 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localAttackTargetEntityWithCurrentItem(arg);
        } else if (var2 != this) {
            var2.attackTargetEntityWithCurrentItem(arg);
        }

    }

    public void afterAttackTargetEntityWithCurrentItem(Entity arg) {
    }

    public void beforeCanHarvestBlock(Block arg) {
    }

    public boolean canHarvestBlock(Block arg) {
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenCanHarvestBlock(this);
        boolean var3;
        if (var2 == null) {
            var3 = ((IPlayerAPIServerPlayerEntity)this.player).localCanHarvestBlock(arg);
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
        ServerPlayerBase var4 = this.playerAPI.GetOverwrittenCanPlayerEdit(this);
        boolean var5;
        if (var4 == null) {
            var5 = ((IPlayerAPIServerPlayerEntity)this.player).localCanPlayerEdit(i, j, k);
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
        ServerPlayerBase var1 = this.playerAPI.GetOverwrittenCanTriggerWalking(this);
        boolean var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIServerPlayerEntity)this.player).localCanTriggerWalking();
        } else if (var1 != this) {
            var2 = var1.canTriggerWalking();
        } else {
            var2 = false;
        }

        return var2;
    }

    public void afterCanTriggerWalking() {
    }

    public void beforeDamageEntity(DamageSource arg, int i) {
    }

    public void damageEntity(DamageSource arg, int i) {
        ServerPlayerBase var3 = this.playerAPI.GetOverwrittenDamageEntity(this);
        if (var3 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localDamageEntity(arg, i);
        } else if (var3 != this) {
            var3.damageEntity(arg, i);
        }

    }

    public void afterDamageEntity(DamageSource arg, int i) {
    }

    public void beforeDisplayGUIChest(Inventory arg) {
    }

    public void displayGUIChest(Inventory arg) {
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenDisplayGUIChest(this);
        if (var2 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localDisplayGUIChest(arg);
        } else if (var2 != this) {
            var2.displayGUIChest(arg);
        }

    }

    public void afterDisplayGUIChest(Inventory arg) {
    }

    public void beforeDisplayGUIDispenser(DispenserBlockEntity arg) {
    }

    public void displayGUIDispenser(DispenserBlockEntity arg) {
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenDisplayGUIDispenser(this);
        if (var2 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localDisplayGUIDispenser(arg);
        } else if (var2 != this) {
            var2.displayGUIDispenser(arg);
        }

    }

    public void afterDisplayGUIDispenser(DispenserBlockEntity arg) {
    }

    public void beforeDisplayGUIFurnace(FurnaceBlockEntity arg) {
    }

    public void displayGUIFurnace(FurnaceBlockEntity arg) {
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenDisplayGUIFurnace(this);
        if (var2 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localDisplayGUIFurnace(arg);
        } else if (var2 != this) {
            var2.displayGUIFurnace(arg);
        }

    }

    public void afterDisplayGUIFurnace(FurnaceBlockEntity arg) {
    }

    public void beforeDisplayWorkbenchGUI(int i, int j, int k) {
    }

    public void displayWorkbenchGUI(int i, int j, int k) {
        ServerPlayerBase var4 = this.playerAPI.GetOverwrittenDisplayWorkbenchGUI(this);
        if (var4 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localDisplayWorkbenchGUI(i, j, k);
        } else if (var4 != this) {
            var4.displayWorkbenchGUI(i, j, k);
        }

    }

    public void afterDisplayWorkbenchGUI(int i, int j, int k) {
    }

    public void beforeDropOneItem() {
    }

    public ItemEntity dropOneItem() {
        ServerPlayerBase var1 = this.playerAPI.GetOverwrittenDropOneItem(this);
        ItemEntity var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIServerPlayerEntity)this.player).localDropOneItem();
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
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenDropPlayerItem(this);
        ItemEntity var3;
        if (var2 == null) {
            var3 = ((IPlayerAPIServerPlayerEntity)this.player).localDropPlayerItem(arg);
        } else if (var2 != this) {
            var3 = var2.dropPlayerItem(arg);
        } else {
            var3 = null;
        }

        return var3;
    }

    public void afterDropPlayerItem(ItemStack arg) {
    }

    public void beforeFall(float f) {
    }

    public void fall(float f) {
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenFall(this);
        if (var2 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localFall(f);
        } else if (var2 != this) {
            var2.fall(f);
        }

    }

    public void afterFall(float f) {
    }

    public void beforeGetCurrentPlayerStrVsBlock(Block arg) {
    }

    public float getCurrentPlayerStrVsBlock(Block arg) {
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenGetCurrentPlayerStrVsBlock(this);
        float var3;
        if (var2 == null) {
            var3 = ((IPlayerAPIServerPlayerEntity)this.player).localGetCurrentPlayerStrVsBlock(arg);
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
        ServerPlayerBase var7 = this.playerAPI.GetOverwrittenGetDistanceSq(this);
        double var8;
        if (var7 == null) {
            var8 = ((IPlayerAPIServerPlayerEntity)this.player).localGetDistanceSq(d, e, f);
        } else if (var7 != this) {
            var8 = var7.getDistanceSq(d, e, f);
        } else {
            var8 = 0.0;
        }

        return var8;
    }

    public void afterGetDistanceSq(double d, double e, double f) {
    }

    public void beforeGetBrightness(float f) {
    }

    public float getBrightness(float f) {
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenGetBrightness(this);
        float var3;
        if (var2 == null) {
            var3 = ((IPlayerAPIServerPlayerEntity)this.player).localGetBrightness(f);
        } else if (var2 != this) {
            var3 = var2.getBrightness(f);
        } else {
            var3 = 0.0F;
        }

        return var3;
    }

    public void afterGetBrightness(float f) {
    }

    public void beforeGetEyeHeight() {
    }

    public float getEyeHeight() {
        ServerPlayerBase var1 = this.playerAPI.GetOverwrittenGetEyeHeight(this);
        float var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIServerPlayerEntity)this.player).localGetEyeHeight();
        } else if (var1 != this) {
            var2 = var1.getEyeHeight();
        } else {
            var2 = 0.0F;
        }

        return var2;
    }

    public void afterGetEyeHeight() {
    }

    public void beforeGetSpeedModifier() {
    }

    public float getSpeedModifier() {
        ServerPlayerBase var1 = this.playerAPI.GetOverwrittenGetSpeedModifier(this);
        float var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIServerPlayerEntity)this.player).localGetSpeedModifier();
        } else if (var1 != this) {
            var2 = var1.getSpeedModifier();
        } else {
            var2 = 0.0F;
        }

        return var2;
    }

    public void afterGetSpeedModifier() {
    }

    public void beforeHeal(int i) {
    }

    public void heal(int i) {
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenHeal(this);
        if (var2 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localHeal(i);
        } else if (var2 != this) {
            var2.heal(i);
        }

    }

    public void afterHeal(int i) {
    }

    public void beforeInteract(PlayerEntity arg) {
    }

    public boolean interact(PlayerEntity arg) {
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenInteract(this);
        boolean var3;
        if (var2 == null) {
            var3 = ((IPlayerAPIServerPlayerEntity)this.player).localInteract(arg);
        } else if (var2 != this) {
            var3 = var2.interact(arg);
        } else {
            var3 = false;
        }

        return var3;
    }

    public void afterInteract(PlayerEntity arg) {
    }

    public void beforeIsEntityInsideOpaqueBlock() {
    }

    public boolean isEntityInsideOpaqueBlock() {
        ServerPlayerBase var1 = this.playerAPI.GetOverwrittenIsEntityInsideOpaqueBlock(this);
        boolean var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIServerPlayerEntity)this.player).localIsEntityInsideOpaqueBlock();
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
        ServerPlayerBase var1 = this.playerAPI.GetOverwrittenIsInWater(this);
        boolean var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIServerPlayerEntity)this.player).localIsInWater();
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
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenIsInsideOfMaterial(this);
        boolean var3;
        if (var2 == null) {
            var3 = ((IPlayerAPIServerPlayerEntity)this.player).localIsInsideOfMaterial(arg);
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
        ServerPlayerBase var1 = this.playerAPI.GetOverwrittenIsOnLadder(this);
        boolean var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIServerPlayerEntity)this.player).localIsOnLadder();
        } else if (var1 != this) {
            var2 = var1.isOnLadder();
        } else {
            var2 = false;
        }

        return var2;
    }

    public void afterIsOnLadder() {
    }

    public void beforeIsPlayerSleeping() {
    }

    public boolean isPlayerSleeping() {
        ServerPlayerBase var1 = this.playerAPI.GetOverwrittenIsPlayerSleeping(this);
        boolean var2;
        if (var1 == null) {
            var2 = ((IPlayerAPIServerPlayerEntity)this.player).localIsPlayerSleeping();
        } else if (var1 != this) {
            var2 = var1.isPlayerSleeping();
        } else {
            var2 = false;
        }

        return var2;
    }

    public void afterIsPlayerSleeping() {
    }

    public void beforeJump() {
    }

    public void jump() {
        ServerPlayerBase var1 = this.playerAPI.GetOverwrittenJump(this);
        if (var1 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localJump();
        } else if (var1 != this) {
            var1.jump();
        }

    }

    public void afterJump() {
    }

    public void beforeMoveEntity(double d, double e, double f) {
    }

    public void moveEntity(double d, double e, double f) {
        ServerPlayerBase var7 = this.playerAPI.GetOverwrittenMoveEntity(this);
        if (var7 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localMoveEntity(d, e, f);
        } else if (var7 != this) {
            var7.moveEntity(d, e, f);
        }

    }

    public void afterMoveEntity(double d, double e, double f) {
    }

    public void beforeMoveEntityWithHeading(float f, float g) {
    }

    public void moveEntityWithHeading(float f, float g) {
        ServerPlayerBase var3 = this.playerAPI.GetOverwrittenMoveEntityWithHeading(this);
        if (var3 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localMoveEntityWithHeading(f, g);
        } else if (var3 != this) {
            var3.moveEntityWithHeading(f, g);
        }

    }

    public void afterMoveEntityWithHeading(float f, float g) {
    }

    public void beforeMoveFlying(float f, float g, float h) {
    }

    public void moveFlying(float f, float g, float h) {
        ServerPlayerBase var4 = this.playerAPI.GetOverwrittenMoveFlying(this);
        if (var4 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localMoveFlying(f, g, h);
        } else if (var4 != this) {
            var4.moveFlying(f, g, h);
        }

    }

    public void afterMoveFlying(float f, float g, float h) {
    }

    public void beforeOnDeath(DamageSource arg) {
    }

    public void onDeath(DamageSource arg) {
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenOnDeath(this);
        if (var2 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localOnDeath(arg);
        } else if (var2 != this) {
            var2.onDeath(arg);
        }

    }

    public void afterOnDeath(DamageSource arg) {
    }

    public void beforeOnLivingUpdate() {
    }

    public void onLivingUpdate() {
        ServerPlayerBase var1 = this.playerAPI.GetOverwrittenOnLivingUpdate(this);
        if (var1 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localOnLivingUpdate();
        } else if (var1 != this) {
            var1.onLivingUpdate();
        }

    }

    public void afterOnLivingUpdate() {
    }

    public void beforeOnKillEntity(MobEntity arg) {
    }

    public void onKillEntity(MobEntity arg) {
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenOnKillEntity(this);
        if (var2 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localOnKillEntity(arg);
        } else if (var2 != this) {
            var2.onKillEntity(arg);
        }

    }

    public void afterOnKillEntity(MobEntity arg) {
    }

    public void beforeOnUpdate() {
    }

    public void onUpdate() {
        ServerPlayerBase var1 = this.playerAPI.GetOverwrittenOnUpdate(this);
        if (var1 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localOnUpdate();
        } else if (var1 != this) {
            var1.onUpdate();
        }

    }

    public void afterOnUpdate() {
    }

    public void beforeOnUpdateEntity() {
    }

    public void onUpdateEntity() {
        ServerPlayerBase var1 = this.playerAPI.GetOverwrittenOnUpdateEntity(this);
        if (var1 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localOnUpdateEntity();
        } else if (var1 != this) {
            var1.onUpdateEntity();
        }

    }

    public void afterOnUpdateEntity() {
    }

    public void beforeReadEntityFromNBT(NbtCompound arg) {
    }

    public void readEntityFromNBT(NbtCompound arg) {
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenReadEntityFromNBT(this);
        if (var2 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localReadEntityFromNBT(arg);
        } else if (var2 != this) {
            var2.readEntityFromNBT(arg);
        }

    }

    public void afterReadEntityFromNBT(NbtCompound arg) {
    }

    public void beforeSetDead() {
    }

    public void setDead() {
        ServerPlayerBase var1 = this.playerAPI.GetOverwrittenSetDead(this);
        if (var1 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localSetDead();
        } else if (var1 != this) {
            var1.setDead();
        }

    }

    public void afterSetDead() {
    }

    public void beforeSetPosition(double d, double e, double f) {
    }

    public void setPosition(double d, double e, double f) {
        ServerPlayerBase var7 = this.playerAPI.GetOverwrittenSetPosition(this);
        if (var7 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localSetPosition(d, e, f);
        } else if (var7 != this) {
            var7.setPosition(d, e, f);
        }

    }

    public void afterSetPosition(double d, double e, double f) {
    }

    public void beforeUpdateEntityActionState() {
    }

    public void updateEntityActionState() {
        ServerPlayerBase var1 = this.playerAPI.GetOverwrittenUpdateEntityActionState(this);
        if (var1 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localUpdateEntityActionState();
        } else if (var1 != this) {
            var1.updateEntityActionState();
        }

    }

    public void afterUpdateEntityActionState() {
    }

    public void beforeWriteEntityToNBT(NbtCompound arg) {
    }

    public void writeEntityToNBT(NbtCompound arg) {
        ServerPlayerBase var2 = this.playerAPI.GetOverwrittenWriteEntityToNBT(this);
        if (var2 == null) {
            ((IPlayerAPIServerPlayerEntity)this.player).localWriteEntityToNBT(arg);
        } else if (var2 != this) {
            var2.writeEntityToNBT(arg);
        }

    }

    public void afterWriteEntityToNBT(NbtCompound arg) {
    }
}
