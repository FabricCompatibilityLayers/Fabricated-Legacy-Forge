package fr.catcore.fabricatedforge.mixininterface;

import net.minecraft.ServerPlayerAPI;
import net.minecraft.ServerPlayerBase;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningBoltEntity;
import net.minecraft.entity.ai.control.JumpControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.Trader;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.MobVisibilityCache;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.stat.Stat;
import net.minecraft.util.BlockPos;
import net.minecraft.util.CanSleepEnum;
import net.minecraft.util.Language;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public interface IPlayerAPIServerPlayerEntity {
    ServerPlayerBase getServerPlayerBase(String string);

    Set<String> getServerPlayerBaseIds(String string);

    boolean superAttackEntityFrom(DamageSource arg, int i);

    boolean localAttackEntityFrom(DamageSource arg, int i);

    void superAttackTargetEntityWithCurrentItem(Entity arg);

    void localAttackTargetEntityWithCurrentItem(Entity arg);

    boolean superCanHarvestBlock(Block arg);

    boolean localCanHarvestBlock(Block arg);

    boolean superCanPlayerEdit(int i, int j, int k);

    boolean localCanPlayerEdit(int i, int j, int k);

    boolean realCanTriggerWalking();

    boolean superCanTriggerWalking();

    boolean localCanTriggerWalking();

    void realDamageEntity(DamageSource arg, int i);

    void superDamageEntity(DamageSource arg, int i);

    void localDamageEntity(DamageSource arg, int i);

    void superDisplayGUIChest(Inventory arg);

    void localDisplayGUIChest(Inventory arg);

    void superDisplayGUIDispenser(DispenserBlockEntity arg);

    void localDisplayGUIDispenser(DispenserBlockEntity arg);

    void superDisplayGUIFurnace(FurnaceBlockEntity arg);

    void localDisplayGUIFurnace(FurnaceBlockEntity arg);

    void superDisplayWorkbenchGUI(int i, int j, int k);

    void localDisplayWorkbenchGUI(int i, int j, int k);

    ItemEntity superDropOneItem();

    ItemEntity localDropOneItem();

    ItemEntity superDropPlayerItem(ItemStack arg);

    ItemEntity localDropPlayerItem(ItemStack arg);

    void realFall(float f);

    void superFall(float f);

    void localFall(float f);

    float superGetCurrentPlayerStrVsBlock(Block arg);

    float localGetCurrentPlayerStrVsBlock(Block arg);

    double superGetDistanceSq(double d, double e, double f);

    double localGetDistanceSq(double d, double e, double f);

    float superGetBrightness(float f);

    float localGetBrightness(float f);

    float superGetEyeHeight();

    float localGetEyeHeight();

    float realGetSpeedModifier();

    float superGetSpeedModifier();

    float localGetSpeedModifier();

    void superHeal(int i);

    void localHeal(int i);

    boolean superInteract(PlayerEntity arg);

    boolean localInteract(PlayerEntity arg);

    boolean superIsEntityInsideOpaqueBlock();

    boolean localIsEntityInsideOpaqueBlock();

    boolean superIsInWater();

    boolean localIsInWater();

    boolean superIsInsideOfMaterial(Material arg);

    boolean localIsInsideOfMaterial(Material arg);

    boolean superIsOnLadder();

    boolean localIsOnLadder();

    boolean superIsPlayerSleeping();

    boolean localIsPlayerSleeping();

    void realJump();

    void superJump();

    void localJump();

    void superMoveEntity(double d, double e, double f);

    void localMoveEntity(double d, double e, double f);

    void superMoveEntityWithHeading(float f, float g);

    void localMoveEntityWithHeading(float f, float g);

    void superMoveFlying(float f, float g, float h);

    void localMoveFlying(float f, float g, float h);

    void superOnDeath(DamageSource arg);

    void localOnDeath(DamageSource arg);

    void superOnLivingUpdate();

    void localOnLivingUpdate();

    void superOnKillEntity(MobEntity arg);

    void localOnKillEntity(MobEntity arg);

    void superOnUpdate();

    void localOnUpdate();

    void localOnUpdateEntity();

    void superReadEntityFromNBT(NbtCompound arg);

    void localReadEntityFromNBT(NbtCompound arg);

    void superSetDead();

    void localSetDead();

    void superSetPosition(double d, double e, double f);

    void localSetPosition(double d, double e, double f);

    void realUpdateEntityActionState();

    void superUpdateEntityActionState();

    void localUpdateEntityActionState();

    void superWriteEntityToNBT(NbtCompound arg);

    void localWriteEntityToNBT(NbtCompound arg);

    void superAddChatMessage(String string);

    boolean superAddEntityID(NbtCompound arg);

    void superAddExhaustion(float f);

    void superAddExperience(int i);

    void superAddMovementStat(double d, double e, double f);

    void superAddPotionEffect(StatusEffectInstance arg);

    void superAddStat(Stat arg, int i);

    void superAddToPlayerScore(Entity arg, int i);

    void superAddVelocity(double d, double e, double f);

    void realAlertWolves(MobEntity arg, boolean bl);

    void superAlertWolves(MobEntity arg, boolean bl);

    int realApplyArmorCalculations(DamageSource arg, int i);

    int superApplyArmorCalculations(DamageSource arg, int i);

    void superApplyEntityCollision(Entity arg);

    int realApplyPotionDamageCalculations(DamageSource arg, int i);

    int superApplyPotionDamageCalculations(DamageSource arg, int i);

    boolean superAttackEntityAsMob(Entity arg);

    boolean superCanAttackWithItem();

    boolean superCanBeCollidedWith();

    boolean superCanBePushed();

    boolean superCanBreatheUnderwater();

    boolean realCanDespawn();

    boolean superCanDespawn();

    boolean superCanEat(boolean bl);

    boolean superCanEntityBeSeen(Entity arg);

    void superClearActivePotions();

    void superClearItemInUse();

    void superClonePlayer(PlayerEntity arg, boolean bl);

    void superCloseScreen();

    void realDamageArmor(int i);

    void superDamageArmor(int i);

    void realDealFireDamage(int i);

    void superDealFireDamage(int i);

    int realDecreaseAirSupply(int i);

    int superDecreaseAirSupply(int i);

    void realDespawnEntity();

    void superDespawnEntity();

    void superDestroyCurrentEquippedItem();

    void superDetachHome();

    void superDisplayGUIBook(ItemStack arg);

    void superDisplayGUIBrewingStand(BrewingStandBlockEntity arg);

    void superDisplayGUIEditSign(SignBlockEntity arg);

    void superDisplayGUIEnchantment(int i, int j, int k);

    void superDisplayGUIMerchant(Trader arg);

    void realDoBlockCollisions();

    void superDoBlockCollisions();

    void realDropFewItems(boolean bl, int i);

    void superDropFewItems(boolean bl, int i);

    ItemEntity superDropItem(int i, int j);

    ItemEntity superDropItemWithOffset(int i, int j, float f);

    ItemEntity superDropPlayerItemWithRandomChoice(ItemStack arg, boolean bl);

    void realDropRareDrop(int i);

    void superDropRareDrop(int i);

    void superEatGrassBonus();

    ItemEntity superEntityDropItem(ItemStack arg, float f);

    void realEntityInit();

    void superEntityInit();

    boolean superEquals(Object object);

    void superExtinguish();

    void superFaceEntity(Entity arg, float f, float g);

    void superFunc_70062_b(int i, ItemStack arg);

    float superFunc_70079_am();

    boolean superFunc_71066_bF();

    float superGetAIMoveSpeed();

    MobEntity superGetAITarget();

    StatusEffectInstance superGetActivePotionEffect(StatusEffect arg);

    Collection superGetActivePotionEffects();

    int superGetAge();

    int superGetAir();

    MobEntity superGetAttackTarget();

    float superGetBedOrientationInDegrees();

    Box superGetBoundingBox();

    int superGetBrightnessForRender(float f);

    boolean superGetCanSpawnHere();

    float superGetCollisionBorderSize();

    Box superGetCollisionBox(Entity arg);

    String superGetCommandSenderName();

    EntityGroup superGetCreatureAttribute();

    ItemStack superGetCurrentEquippedItem();

    DataTracker superGetDataWatcher();

    String realGetDeathSound();

    String superGetDeathSound();

    double superGetDistance(double d, double e, double f);

    double superGetDistanceSqToEntity(Entity arg);

    float superGetDistanceToEntity(Entity arg);

    int realGetDropItemId();

    int superGetDropItemId();

    String superGetEntityName();

    MobVisibilityCache superGetEntitySenses();

    int realGetExperiencePoints(PlayerEntity arg);

    int superGetExperiencePoints(PlayerEntity arg);

    boolean realGetFlag(int i);

    boolean superGetFlag(int i);

    HungerManager superGetFoodStats();

    int superGetHealth();

    ItemStack superGetHeldItem();

    BlockPos superGetHomePosition();

    String realGetHurtSound();

    String superGetHurtSound();

    EnderChestInventory superGetInventoryEnderChest();

    int superGetItemIcon(ItemStack arg, int i);

    ItemStack superGetItemInUse();

    int superGetItemInUseCount();

    int superGetItemInUseDuration();

    JumpControl superGetJumpHelper();

    ItemStack[] superGetLastActiveItems();

    MobEntity superGetLastAttackingEntity();

    String realGetLivingSound();

    String superGetLivingSound();

    Vec3d superGetLook(float f);

    LookControl superGetLookHelper();

    Vec3d superGetLookVec();

    int superGetMaxHealth();

    int superGetMaxSpawnedInChunk();

    float superGetMaximumHomeDistance();

    double superGetMountedYOffset();

    MoveControl superGetMoveHelper();

    EntityNavigation superGetNavigator();

    Entity[] superGetParts();

    Vec3d superGetPosition(float f);

    Random superGetRNG();

    float superGetRenderSizeModifier();

    int superGetScore();

    float superGetShadowSize();

    int superGetSleepTimer();

    float realGetSoundVolume();

    float superGetSoundVolume();

    BlockPos superGetSpawnChunk();

    float superGetSwingProgress(float f);

    int superGetTalkInterval();

    String superGetTexture();

    int superGetTotalArmorValue();

    Language superGetTranslator();

    int superGetVerticalFaceSpeed();

    double superGetYOffset();

    void superHandleHealthUpdate(byte b);

    boolean superHandleLavaMovement();

    boolean superHandleWaterMovement();

    boolean superHasHome();

    int superHashCode();

    void realIncrementWindowID();

    boolean superInteractWith(Entity arg);

    boolean realIsAIEnabled();

    boolean superIsAIEnabled();

    boolean superIsBlocking();

    boolean superIsBurning();

    boolean superIsChild();

    boolean realIsClientWorld();

    boolean superIsClientWorld();

    boolean superIsEating();

    boolean superIsEntityAlive();

    boolean superIsEntityEqual(Entity arg);

    boolean superIsEntityUndead();

    boolean superIsExplosiveMob(Class<?> class_);

    boolean superIsInRangeToRenderDist(double d);

    boolean superIsInRangeToRenderVec3D(Vec3d arg);

    boolean realIsMovementBlocked();

    boolean superIsMovementBlocked();

    boolean superIsOffsetPositionInLiquid(double d, double e, double f);

    boolean realIsPVPEnabled();

    boolean superIsPVPEnabled();

    boolean realIsPlayer();

    boolean superIsPlayer();

    boolean superIsPlayerFullyAsleep();

    boolean superIsPotionActive(StatusEffect arg);

    boolean superIsPotionApplicable(StatusEffectInstance arg);

    boolean superIsRiding();

    boolean superIsSneaking();

    boolean superIsSprinting();

    boolean superIsUsingItem();

    boolean superIsWet();

    boolean superIsWithinHomeDistance(int i, int j, int k);

    boolean superIsWithinHomeDistanceCurrentPosition();

    void realJoinEntityItemWithWorld(ItemEntity arg);

    void superJoinEntityItemWithWorld(ItemEntity arg);

    void realKill();

    void superKill();

    void superKnockBack(Entity arg, int i, double d, double e);

    void superMountEntity(Entity arg);

    NbtList realNewDoubleNBTList(double... ds);

    NbtList superNewDoubleNBTList(double... ds);

    NbtList realNewFloatNBTList(float... fs);

    NbtList superNewFloatNBTList(float... fs);

    void realOnChangedPotionEffect(StatusEffectInstance arg);

    void superOnChangedPotionEffect(StatusEffectInstance arg);

    void superOnCollideWithPlayer(PlayerEntity arg);

    void superOnCriticalHit(Entity arg);

    void realOnDeathUpdate();

    void superOnDeathUpdate();

    void superOnEnchantmentCritical(Entity arg);

    void superOnEntityUpdate();

    void realOnFinishedPotionEffect(StatusEffectInstance arg);

    void superOnFinishedPotionEffect(StatusEffectInstance arg);

    void superOnItemPickup(Entity arg, int i);

    void realOnItemUseFinish();

    void superOnItemUseFinish();

    void realOnNewPotionEffect(StatusEffectInstance arg);

    void superOnNewPotionEffect(StatusEffectInstance arg);

    void superOnStruckByLightning(LightningBoltEntity arg);

    void superPerformHurtAnimation();

    void superPlayLivingSound();

    void realPlayStepSound(int i, int j, int k, int l);

    void superPlayStepSound(int i, int j, int k, int l);

    void superPreparePlayerToSpawn();

    boolean realPushOutOfBlocks(double d, double e, double f);

    boolean superPushOutOfBlocks(double d, double e, double f);

    HitResult superRayTrace(double d, float f);

    void superReadFromNBT(NbtCompound arg);

    void superRemoveExperience(int i);

    void superRemovePotionEffect(int i);

    void superRenderBrokenItemStack(ItemStack arg);

    void realResetHeight();

    void superResetHeight();

    void superRespawnPlayer();

    void superSendGameTypeToPlayer(GameMode arg);

    void superSendPlayerAbilities();

    void realSendTileEntityToPlayer(BlockEntity arg);

    void superSetAIMoveSpeed(float f);

    void superSetAir(int i);

    void superSetAngles(float f, float g);

    void superSetAttackTarget(MobEntity arg);

    void realSetBeenAttacked();

    void superSetBeenAttacked();

    void superSetEating(boolean bl);

    void superSetEntityHealth(int i);

    void superSetFire(int i);

    void realSetFlag(int i, boolean bl);

    void superSetFlag(int i, boolean bl);

    void superSetHeadRotationYaw(float f);

    void superSetHomeArea(int i, int j, int k, int l);

    void superSetInPortal();

    void superSetInWeb();

    void superSetItemInUse(ItemStack arg, int i);

    void superSetJumping(boolean bl);

    void superSetLastAttackingEntity(Entity arg);

    void superSetLocationAndAngles(double d, double e, double f, float g, float h);

    void superSetMoveForward(float f);

    void realSetOnFireFromLava();

    void superSetOnFireFromLava();

    void superSetPositionAndRotation(double d, double e, double f, float g, float h);

    void superSetPositionAndRotation2(double d, double e, double f, float g, float h, int i);

    void superSetPositionAndUpdate(double d, double e, double f);

    void superSetRevengeTarget(MobEntity arg);

    void realSetRotation(float f, float g);

    void superSetRotation(float f, float g);

    void realSetSize(float f, float g);

    void superSetSize(float f, float g);

    void superSetSneaking(boolean bl);

    void superSetSpawnChunk(BlockPos arg);

    void superSetSprinting(boolean bl);

    void superSetVelocity(double d, double e, double f);

    void superSetWorld(World arg);

    boolean superShouldHeal();

    CanSleepEnum superSleepInBedAt(int i, int j, int k);

    void superSpawnExplosionParticle();

    void superStopUsingItem();

    void superSwingItem();

    String superToString();

    String superTranslateString(String string, Object... objects);

    void superTravelToTheEnd(int i);

    void superTriggerAchievement(Stat arg);

    void superUnmountEntity(Entity arg);

    void realUpdateAITasks();

    void superUpdateAITasks();

    void realUpdateAITick();

    void superUpdateAITick();

    void superUpdateCloak();

    void realUpdateFallState(double d, boolean bl);

    void superUpdateFallState(double d, boolean bl);

    void realUpdateItemUse(ItemStack arg, int i);

    void superUpdateItemUse(ItemStack arg, int i);

    void realUpdatePotionEffects();

    void superUpdatePotionEffects();

    void superUpdateRidden();

    void superUpdateRiderPosition();

    void superWakeUpPlayer(boolean bl, boolean bl2, boolean bl3);

    void superWriteToNBT(NbtCompound arg);

    int superXpBarCap();

    HashMap getActivePotionsMapField();

    void setActivePotionsMapField(HashMap hashMap);

    PlayerEntity getAttackingPlayerField();

    void setAttackingPlayerField(PlayerEntity arg);

    int getCarryoverDamageField();

    void setCarryoverDamageField(int i);

    boolean getChatColoursField();

    void setChatColoursField(boolean bl);

    int getChatVisibilityField();

    void setChatVisibilityField(int i);

    int getCurrentWindowIdField();

    void setCurrentWindowIdField(int i);

    DataTracker getDataWatcherField();

    void setDataWatcherField(DataTracker arg);

    boolean getDeadField();

    void setDeadField(boolean bl);

    float getDefaultPitchField();

    void setDefaultPitchField(float f);

    int getEntityAgeField();

    void setEntityAgeField(int i);

    String getEntityTypeField();

    void setEntityTypeField(String string);

    int getExperienceValueField();

    void setExperienceValueField(int i);

    float getField_70706_boField();

    void setField_70706_boField(float f);

    boolean getField_70740_aAField();

    void setField_70740_aAField(boolean bl);

    float getField_70741_aBField();

    void setField_70741_aBField(float f);

    float getField_70743_aDField();

    void setField_70743_aDField(float f);

    float getField_70745_aFField();

    void setField_70745_aFField(float f);

    boolean getField_70753_ayField();

    void setField_70753_ayField(boolean bl);

    float getField_70763_axField();

    void setField_70763_axField(float f);

    float getField_70764_awField();

    void setField_70764_awField(float f);

    float getField_70766_avField();

    void setField_70766_avField(float f);

    float getField_70768_auField();

    void setField_70768_auField(float f);

    int getFlyToggleTimerField();

    void setFlyToggleTimerField(int i);

    HungerManager getFoodStatsField();

    void setFoodStatsField(HungerManager arg);

    int getHealthField();

    void setHealthField(int i);

    boolean getInPortalField();

    void setInPortalField(boolean bl);

    boolean getInWaterField();

    void setInWaterField(boolean bl);

    int getInitialInvulnerabilityField();

    void setInitialInvulnerabilityField(int i);

    boolean getIsImmuneToFireField();

    void setIsImmuneToFireField(boolean bl);

    boolean getIsInWebField();

    void setIsInWebField(boolean bl);

    boolean getIsJumpingField();

    void setIsJumpingField(boolean bl);

    ItemStack[] getLastActiveItemsField();

    void setLastActiveItemsField(ItemStack[] args);

    int getLastDamageField();

    void setLastDamageField(int i);

    int getLastExperienceField();

    void setLastExperienceField(int i);

    int getLastFoodLevelField();

    void setLastFoodLevelField(int i);

    int getLastHealthField();

    void setLastHealthField(int i);

    float getMoveForwardField();

    void setMoveForwardField(float f);

    float getMoveSpeedField();

    void setMoveSpeedField(float f);

    float getMoveStrafingField();

    void setMoveStrafingField(float f);

    int getNewPosRotationIncrementsField();

    void setNewPosRotationIncrementsField(int i);

    double getNewPosXField();

    void setNewPosXField(double d);

    double getNewPosYField();

    void setNewPosYField(double d);

    double getNewPosZField();

    void setNewPosZField(double d);

    double getNewRotationPitchField();

    void setNewRotationPitchField(double d);

    double getNewRotationYawField();

    void setNewRotationYawField(double d);

    int getNumTicksToChaseTargetField();

    void setNumTicksToChaseTargetField(int i);

    Random getRandField();

    void setRandField(Random random);

    float getRandomYawVelocityField();

    void setRandomYawVelocityField(float f);

    int getRecentlyHitField();

    void setRecentlyHitField(int i);

    int getRenderDistanceField();

    void setRenderDistanceField(int i);

    int getScoreValueField();

    void setScoreValueField(int i);

    boolean getSleepingField();

    void setSleepingField(boolean bl);

    float getSpeedInAirField();

    void setSpeedInAirField(float f);

    float getSpeedOnGroundField();

    void setSpeedOnGroundField(float f);

    GoalSelector getTargetTasksField();

    GoalSelector getTasksField();

    String getTextureField();

    void setTextureField(String string);

    Language getTranslatorField();

    void setTranslatorField(Language arg);

    boolean getWasHungryField();

    void setWasHungryField(boolean bl);

    ServerPlayerAPI getServerPlayerAPI();
}
