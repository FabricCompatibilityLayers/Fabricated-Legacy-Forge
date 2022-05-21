//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.minecraft;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

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

public final class PlayerAPI {
    private static final Class<?>[] Class = new Class[]{PlayerAPI.class};
    private static final Class<?>[] Classes = new Class[]{PlayerAPI.class, String.class};
    private static boolean isCreated;
    private static final Logger logger = Logger.getLogger("PlayerAPI");
    private static final List<String> beforeAddExhaustionHookTypes = new LinkedList();
    private static final List<String> overrideAddExhaustionHookTypes = new LinkedList();
    private static final List<String> afterAddExhaustionHookTypes = new LinkedList();
    private final PlayerBase[] beforeAddExhaustionHooks;
    private final PlayerBase[] overrideAddExhaustionHooks;
    private final PlayerBase[] afterAddExhaustionHooks;
    public final boolean isAddExhaustionModded;
    private static final List<String> beforeAddMovementStatHookTypes = new LinkedList();
    private static final List<String> overrideAddMovementStatHookTypes = new LinkedList();
    private static final List<String> afterAddMovementStatHookTypes = new LinkedList();
    private final PlayerBase[] beforeAddMovementStatHooks;
    private final PlayerBase[] overrideAddMovementStatHooks;
    private final PlayerBase[] afterAddMovementStatHooks;
    public final boolean isAddMovementStatModded;
    private static final List<String> beforeAddStatHookTypes = new LinkedList();
    private static final List<String> overrideAddStatHookTypes = new LinkedList();
    private static final List<String> afterAddStatHookTypes = new LinkedList();
    private final PlayerBase[] beforeAddStatHooks;
    private final PlayerBase[] overrideAddStatHooks;
    private final PlayerBase[] afterAddStatHooks;
    public final boolean isAddStatModded;
    private static final List<String> beforeAttackEntityFromHookTypes = new LinkedList();
    private static final List<String> overrideAttackEntityFromHookTypes = new LinkedList();
    private static final List<String> afterAttackEntityFromHookTypes = new LinkedList();
    private final PlayerBase[] beforeAttackEntityFromHooks;
    private final PlayerBase[] overrideAttackEntityFromHooks;
    private final PlayerBase[] afterAttackEntityFromHooks;
    public final boolean isAttackEntityFromModded;
    private static final List<String> beforeAlertWolvesHookTypes = new LinkedList();
    private static final List<String> overrideAlertWolvesHookTypes = new LinkedList();
    private static final List<String> afterAlertWolvesHookTypes = new LinkedList();
    private final PlayerBase[] beforeAlertWolvesHooks;
    private final PlayerBase[] overrideAlertWolvesHooks;
    private final PlayerBase[] afterAlertWolvesHooks;
    public final boolean isAlertWolvesModded;
    private static final List<String> beforeAttackTargetEntityWithCurrentItemHookTypes = new LinkedList();
    private static final List<String> overrideAttackTargetEntityWithCurrentItemHookTypes = new LinkedList();
    private static final List<String> afterAttackTargetEntityWithCurrentItemHookTypes = new LinkedList();
    private final PlayerBase[] beforeAttackTargetEntityWithCurrentItemHooks;
    private final PlayerBase[] overrideAttackTargetEntityWithCurrentItemHooks;
    private final PlayerBase[] afterAttackTargetEntityWithCurrentItemHooks;
    public final boolean isAttackTargetEntityWithCurrentItemModded;
    private static final List<String> beforeCanBreatheUnderwaterHookTypes = new LinkedList();
    private static final List<String> overrideCanBreatheUnderwaterHookTypes = new LinkedList();
    private static final List<String> afterCanBreatheUnderwaterHookTypes = new LinkedList();
    private final PlayerBase[] beforeCanBreatheUnderwaterHooks;
    private final PlayerBase[] overrideCanBreatheUnderwaterHooks;
    private final PlayerBase[] afterCanBreatheUnderwaterHooks;
    public final boolean isCanBreatheUnderwaterModded;
    private static final List<String> beforeCanHarvestBlockHookTypes = new LinkedList();
    private static final List<String> overrideCanHarvestBlockHookTypes = new LinkedList();
    private static final List<String> afterCanHarvestBlockHookTypes = new LinkedList();
    private final PlayerBase[] beforeCanHarvestBlockHooks;
    private final PlayerBase[] overrideCanHarvestBlockHooks;
    private final PlayerBase[] afterCanHarvestBlockHooks;
    public final boolean isCanHarvestBlockModded;
    private static final List<String> beforeCanPlayerEditHookTypes = new LinkedList();
    private static final List<String> overrideCanPlayerEditHookTypes = new LinkedList();
    private static final List<String> afterCanPlayerEditHookTypes = new LinkedList();
    private final PlayerBase[] beforeCanPlayerEditHooks;
    private final PlayerBase[] overrideCanPlayerEditHooks;
    private final PlayerBase[] afterCanPlayerEditHooks;
    public final boolean isCanPlayerEditModded;
    private static final List<String> beforeCanTriggerWalkingHookTypes = new LinkedList();
    private static final List<String> overrideCanTriggerWalkingHookTypes = new LinkedList();
    private static final List<String> afterCanTriggerWalkingHookTypes = new LinkedList();
    private final PlayerBase[] beforeCanTriggerWalkingHooks;
    private final PlayerBase[] overrideCanTriggerWalkingHooks;
    private final PlayerBase[] afterCanTriggerWalkingHooks;
    public final boolean isCanTriggerWalkingModded;
    private static final List<String> beforeCloseScreenHookTypes = new LinkedList();
    private static final List<String> overrideCloseScreenHookTypes = new LinkedList();
    private static final List<String> afterCloseScreenHookTypes = new LinkedList();
    private final PlayerBase[] beforeCloseScreenHooks;
    private final PlayerBase[] overrideCloseScreenHooks;
    private final PlayerBase[] afterCloseScreenHooks;
    public final boolean isCloseScreenModded;
    private static final List<String> beforeDamageEntityHookTypes = new LinkedList();
    private static final List<String> overrideDamageEntityHookTypes = new LinkedList();
    private static final List<String> afterDamageEntityHookTypes = new LinkedList();
    private final PlayerBase[] beforeDamageEntityHooks;
    private final PlayerBase[] overrideDamageEntityHooks;
    private final PlayerBase[] afterDamageEntityHooks;
    public final boolean isDamageEntityModded;
    private static final List<String> beforeDisplayGUIBrewingStandHookTypes = new LinkedList();
    private static final List<String> overrideDisplayGUIBrewingStandHookTypes = new LinkedList();
    private static final List<String> afterDisplayGUIBrewingStandHookTypes = new LinkedList();
    private final PlayerBase[] beforeDisplayGUIBrewingStandHooks;
    private final PlayerBase[] overrideDisplayGUIBrewingStandHooks;
    private final PlayerBase[] afterDisplayGUIBrewingStandHooks;
    public final boolean isDisplayGUIBrewingStandModded;
    private static final List<String> beforeDisplayGUIChestHookTypes = new LinkedList();
    private static final List<String> overrideDisplayGUIChestHookTypes = new LinkedList();
    private static final List<String> afterDisplayGUIChestHookTypes = new LinkedList();
    private final PlayerBase[] beforeDisplayGUIChestHooks;
    private final PlayerBase[] overrideDisplayGUIChestHooks;
    private final PlayerBase[] afterDisplayGUIChestHooks;
    public final boolean isDisplayGUIChestModded;
    private static final List<String> beforeDisplayGUIDispenserHookTypes = new LinkedList();
    private static final List<String> overrideDisplayGUIDispenserHookTypes = new LinkedList();
    private static final List<String> afterDisplayGUIDispenserHookTypes = new LinkedList();
    private final PlayerBase[] beforeDisplayGUIDispenserHooks;
    private final PlayerBase[] overrideDisplayGUIDispenserHooks;
    private final PlayerBase[] afterDisplayGUIDispenserHooks;
    public final boolean isDisplayGUIDispenserModded;
    private static final List<String> beforeDisplayGUIEditSignHookTypes = new LinkedList();
    private static final List<String> overrideDisplayGUIEditSignHookTypes = new LinkedList();
    private static final List<String> afterDisplayGUIEditSignHookTypes = new LinkedList();
    private final PlayerBase[] beforeDisplayGUIEditSignHooks;
    private final PlayerBase[] overrideDisplayGUIEditSignHooks;
    private final PlayerBase[] afterDisplayGUIEditSignHooks;
    public final boolean isDisplayGUIEditSignModded;
    private static final List<String> beforeDisplayGUIEnchantmentHookTypes = new LinkedList();
    private static final List<String> overrideDisplayGUIEnchantmentHookTypes = new LinkedList();
    private static final List<String> afterDisplayGUIEnchantmentHookTypes = new LinkedList();
    private final PlayerBase[] beforeDisplayGUIEnchantmentHooks;
    private final PlayerBase[] overrideDisplayGUIEnchantmentHooks;
    private final PlayerBase[] afterDisplayGUIEnchantmentHooks;
    public final boolean isDisplayGUIEnchantmentModded;
    private static final List<String> beforeDisplayGUIFurnaceHookTypes = new LinkedList();
    private static final List<String> overrideDisplayGUIFurnaceHookTypes = new LinkedList();
    private static final List<String> afterDisplayGUIFurnaceHookTypes = new LinkedList();
    private final PlayerBase[] beforeDisplayGUIFurnaceHooks;
    private final PlayerBase[] overrideDisplayGUIFurnaceHooks;
    private final PlayerBase[] afterDisplayGUIFurnaceHooks;
    public final boolean isDisplayGUIFurnaceModded;
    private static final List<String> beforeDisplayWorkbenchGUIHookTypes = new LinkedList();
    private static final List<String> overrideDisplayWorkbenchGUIHookTypes = new LinkedList();
    private static final List<String> afterDisplayWorkbenchGUIHookTypes = new LinkedList();
    private final PlayerBase[] beforeDisplayWorkbenchGUIHooks;
    private final PlayerBase[] overrideDisplayWorkbenchGUIHooks;
    private final PlayerBase[] afterDisplayWorkbenchGUIHooks;
    public final boolean isDisplayWorkbenchGUIModded;
    private static final List<String> beforeDropOneItemHookTypes = new LinkedList();
    private static final List<String> overrideDropOneItemHookTypes = new LinkedList();
    private static final List<String> afterDropOneItemHookTypes = new LinkedList();
    private final PlayerBase[] beforeDropOneItemHooks;
    private final PlayerBase[] overrideDropOneItemHooks;
    private final PlayerBase[] afterDropOneItemHooks;
    public final boolean isDropOneItemModded;
    private static final List<String> beforeDropPlayerItemHookTypes = new LinkedList();
    private static final List<String> overrideDropPlayerItemHookTypes = new LinkedList();
    private static final List<String> afterDropPlayerItemHookTypes = new LinkedList();
    private final PlayerBase[] beforeDropPlayerItemHooks;
    private final PlayerBase[] overrideDropPlayerItemHooks;
    private final PlayerBase[] afterDropPlayerItemHooks;
    public final boolean isDropPlayerItemModded;
    private static final List<String> beforeDropPlayerItemWithRandomChoiceHookTypes = new LinkedList();
    private static final List<String> overrideDropPlayerItemWithRandomChoiceHookTypes = new LinkedList();
    private static final List<String> afterDropPlayerItemWithRandomChoiceHookTypes = new LinkedList();
    private final PlayerBase[] beforeDropPlayerItemWithRandomChoiceHooks;
    private final PlayerBase[] overrideDropPlayerItemWithRandomChoiceHooks;
    private final PlayerBase[] afterDropPlayerItemWithRandomChoiceHooks;
    public final boolean isDropPlayerItemWithRandomChoiceModded;
    private static final List<String> beforeFallHookTypes = new LinkedList();
    private static final List<String> overrideFallHookTypes = new LinkedList();
    private static final List<String> afterFallHookTypes = new LinkedList();
    private final PlayerBase[] beforeFallHooks;
    private final PlayerBase[] overrideFallHooks;
    private final PlayerBase[] afterFallHooks;
    public final boolean isFallModded;
    private static final List<String> beforeGetBrightnessHookTypes = new LinkedList();
    private static final List<String> overrideGetBrightnessHookTypes = new LinkedList();
    private static final List<String> afterGetBrightnessHookTypes = new LinkedList();
    private final PlayerBase[] beforeGetBrightnessHooks;
    private final PlayerBase[] overrideGetBrightnessHooks;
    private final PlayerBase[] afterGetBrightnessHooks;
    public final boolean isGetBrightnessModded;
    private static final List<String> beforeGetBrightnessForRenderHookTypes = new LinkedList();
    private static final List<String> overrideGetBrightnessForRenderHookTypes = new LinkedList();
    private static final List<String> afterGetBrightnessForRenderHookTypes = new LinkedList();
    private final PlayerBase[] beforeGetBrightnessForRenderHooks;
    private final PlayerBase[] overrideGetBrightnessForRenderHooks;
    private final PlayerBase[] afterGetBrightnessForRenderHooks;
    public final boolean isGetBrightnessForRenderModded;
    private static final List<String> beforeGetCurrentPlayerStrVsBlockHookTypes = new LinkedList();
    private static final List<String> overrideGetCurrentPlayerStrVsBlockHookTypes = new LinkedList();
    private static final List<String> afterGetCurrentPlayerStrVsBlockHookTypes = new LinkedList();
    private final PlayerBase[] beforeGetCurrentPlayerStrVsBlockHooks;
    private final PlayerBase[] overrideGetCurrentPlayerStrVsBlockHooks;
    private final PlayerBase[] afterGetCurrentPlayerStrVsBlockHooks;
    public final boolean isGetCurrentPlayerStrVsBlockModded;
    private static final List<String> beforeGetDistanceSqHookTypes = new LinkedList();
    private static final List<String> overrideGetDistanceSqHookTypes = new LinkedList();
    private static final List<String> afterGetDistanceSqHookTypes = new LinkedList();
    private final PlayerBase[] beforeGetDistanceSqHooks;
    private final PlayerBase[] overrideGetDistanceSqHooks;
    private final PlayerBase[] afterGetDistanceSqHooks;
    public final boolean isGetDistanceSqModded;
    private static final List<String> beforeGetDistanceSqToEntityHookTypes = new LinkedList();
    private static final List<String> overrideGetDistanceSqToEntityHookTypes = new LinkedList();
    private static final List<String> afterGetDistanceSqToEntityHookTypes = new LinkedList();
    private final PlayerBase[] beforeGetDistanceSqToEntityHooks;
    private final PlayerBase[] overrideGetDistanceSqToEntityHooks;
    private final PlayerBase[] afterGetDistanceSqToEntityHooks;
    public final boolean isGetDistanceSqToEntityModded;
    private static final List<String> beforeGetFOVMultiplierHookTypes = new LinkedList();
    private static final List<String> overrideGetFOVMultiplierHookTypes = new LinkedList();
    private static final List<String> afterGetFOVMultiplierHookTypes = new LinkedList();
    private final PlayerBase[] beforeGetFOVMultiplierHooks;
    private final PlayerBase[] overrideGetFOVMultiplierHooks;
    private final PlayerBase[] afterGetFOVMultiplierHooks;
    public final boolean isGetFOVMultiplierModded;
    private static final List<String> beforeGetHurtSoundHookTypes = new LinkedList();
    private static final List<String> overrideGetHurtSoundHookTypes = new LinkedList();
    private static final List<String> afterGetHurtSoundHookTypes = new LinkedList();
    private final PlayerBase[] beforeGetHurtSoundHooks;
    private final PlayerBase[] overrideGetHurtSoundHooks;
    private final PlayerBase[] afterGetHurtSoundHooks;
    public final boolean isGetHurtSoundModded;
    private static final List<String> beforeGetItemIconHookTypes = new LinkedList();
    private static final List<String> overrideGetItemIconHookTypes = new LinkedList();
    private static final List<String> afterGetItemIconHookTypes = new LinkedList();
    private final PlayerBase[] beforeGetItemIconHooks;
    private final PlayerBase[] overrideGetItemIconHooks;
    private final PlayerBase[] afterGetItemIconHooks;
    public final boolean isGetItemIconModded;
    private static final List<String> beforeGetSleepTimerHookTypes = new LinkedList();
    private static final List<String> overrideGetSleepTimerHookTypes = new LinkedList();
    private static final List<String> afterGetSleepTimerHookTypes = new LinkedList();
    private final PlayerBase[] beforeGetSleepTimerHooks;
    private final PlayerBase[] overrideGetSleepTimerHooks;
    private final PlayerBase[] afterGetSleepTimerHooks;
    public final boolean isGetSleepTimerModded;
    private static final List<String> beforeGetSpeedModifierHookTypes = new LinkedList();
    private static final List<String> overrideGetSpeedModifierHookTypes = new LinkedList();
    private static final List<String> afterGetSpeedModifierHookTypes = new LinkedList();
    private final PlayerBase[] beforeGetSpeedModifierHooks;
    private final PlayerBase[] overrideGetSpeedModifierHooks;
    private final PlayerBase[] afterGetSpeedModifierHooks;
    public final boolean isGetSpeedModifierModded;
    private static final List<String> beforeHandleLavaMovementHookTypes = new LinkedList();
    private static final List<String> overrideHandleLavaMovementHookTypes = new LinkedList();
    private static final List<String> afterHandleLavaMovementHookTypes = new LinkedList();
    private final PlayerBase[] beforeHandleLavaMovementHooks;
    private final PlayerBase[] overrideHandleLavaMovementHooks;
    private final PlayerBase[] afterHandleLavaMovementHooks;
    public final boolean isHandleLavaMovementModded;
    private static final List<String> beforeHandleWaterMovementHookTypes = new LinkedList();
    private static final List<String> overrideHandleWaterMovementHookTypes = new LinkedList();
    private static final List<String> afterHandleWaterMovementHookTypes = new LinkedList();
    private final PlayerBase[] beforeHandleWaterMovementHooks;
    private final PlayerBase[] overrideHandleWaterMovementHooks;
    private final PlayerBase[] afterHandleWaterMovementHooks;
    public final boolean isHandleWaterMovementModded;
    private static final List<String> beforeHealHookTypes = new LinkedList();
    private static final List<String> overrideHealHookTypes = new LinkedList();
    private static final List<String> afterHealHookTypes = new LinkedList();
    private final PlayerBase[] beforeHealHooks;
    private final PlayerBase[] overrideHealHooks;
    private final PlayerBase[] afterHealHooks;
    public final boolean isHealModded;
    private static final List<String> beforeIsEntityInsideOpaqueBlockHookTypes = new LinkedList();
    private static final List<String> overrideIsEntityInsideOpaqueBlockHookTypes = new LinkedList();
    private static final List<String> afterIsEntityInsideOpaqueBlockHookTypes = new LinkedList();
    private final PlayerBase[] beforeIsEntityInsideOpaqueBlockHooks;
    private final PlayerBase[] overrideIsEntityInsideOpaqueBlockHooks;
    private final PlayerBase[] afterIsEntityInsideOpaqueBlockHooks;
    public final boolean isIsEntityInsideOpaqueBlockModded;
    private static final List<String> beforeIsInWaterHookTypes = new LinkedList();
    private static final List<String> overrideIsInWaterHookTypes = new LinkedList();
    private static final List<String> afterIsInWaterHookTypes = new LinkedList();
    private final PlayerBase[] beforeIsInWaterHooks;
    private final PlayerBase[] overrideIsInWaterHooks;
    private final PlayerBase[] afterIsInWaterHooks;
    public final boolean isIsInWaterModded;
    private static final List<String> beforeIsInsideOfMaterialHookTypes = new LinkedList();
    private static final List<String> overrideIsInsideOfMaterialHookTypes = new LinkedList();
    private static final List<String> afterIsInsideOfMaterialHookTypes = new LinkedList();
    private final PlayerBase[] beforeIsInsideOfMaterialHooks;
    private final PlayerBase[] overrideIsInsideOfMaterialHooks;
    private final PlayerBase[] afterIsInsideOfMaterialHooks;
    public final boolean isIsInsideOfMaterialModded;
    private static final List<String> beforeIsOnLadderHookTypes = new LinkedList();
    private static final List<String> overrideIsOnLadderHookTypes = new LinkedList();
    private static final List<String> afterIsOnLadderHookTypes = new LinkedList();
    private final PlayerBase[] beforeIsOnLadderHooks;
    private final PlayerBase[] overrideIsOnLadderHooks;
    private final PlayerBase[] afterIsOnLadderHooks;
    public final boolean isIsOnLadderModded;
    private static final List<String> beforeIsSneakingHookTypes = new LinkedList();
    private static final List<String> overrideIsSneakingHookTypes = new LinkedList();
    private static final List<String> afterIsSneakingHookTypes = new LinkedList();
    private final PlayerBase[] beforeIsSneakingHooks;
    private final PlayerBase[] overrideIsSneakingHooks;
    private final PlayerBase[] afterIsSneakingHooks;
    public final boolean isIsSneakingModded;
    private static final List<String> beforeIsSprintingHookTypes = new LinkedList();
    private static final List<String> overrideIsSprintingHookTypes = new LinkedList();
    private static final List<String> afterIsSprintingHookTypes = new LinkedList();
    private final PlayerBase[] beforeIsSprintingHooks;
    private final PlayerBase[] overrideIsSprintingHooks;
    private final PlayerBase[] afterIsSprintingHooks;
    public final boolean isIsSprintingModded;
    private static final List<String> beforeJumpHookTypes = new LinkedList();
    private static final List<String> overrideJumpHookTypes = new LinkedList();
    private static final List<String> afterJumpHookTypes = new LinkedList();
    private final PlayerBase[] beforeJumpHooks;
    private final PlayerBase[] overrideJumpHooks;
    private final PlayerBase[] afterJumpHooks;
    public final boolean isJumpModded;
    private static final List<String> beforeKnockBackHookTypes = new LinkedList();
    private static final List<String> overrideKnockBackHookTypes = new LinkedList();
    private static final List<String> afterKnockBackHookTypes = new LinkedList();
    private final PlayerBase[] beforeKnockBackHooks;
    private final PlayerBase[] overrideKnockBackHooks;
    private final PlayerBase[] afterKnockBackHooks;
    public final boolean isKnockBackModded;
    private static final List<String> beforeMoveEntityHookTypes = new LinkedList();
    private static final List<String> overrideMoveEntityHookTypes = new LinkedList();
    private static final List<String> afterMoveEntityHookTypes = new LinkedList();
    private final PlayerBase[] beforeMoveEntityHooks;
    private final PlayerBase[] overrideMoveEntityHooks;
    private final PlayerBase[] afterMoveEntityHooks;
    public final boolean isMoveEntityModded;
    private static final List<String> beforeMoveEntityWithHeadingHookTypes = new LinkedList();
    private static final List<String> overrideMoveEntityWithHeadingHookTypes = new LinkedList();
    private static final List<String> afterMoveEntityWithHeadingHookTypes = new LinkedList();
    private final PlayerBase[] beforeMoveEntityWithHeadingHooks;
    private final PlayerBase[] overrideMoveEntityWithHeadingHooks;
    private final PlayerBase[] afterMoveEntityWithHeadingHooks;
    public final boolean isMoveEntityWithHeadingModded;
    private static final List<String> beforeMoveFlyingHookTypes = new LinkedList();
    private static final List<String> overrideMoveFlyingHookTypes = new LinkedList();
    private static final List<String> afterMoveFlyingHookTypes = new LinkedList();
    private final PlayerBase[] beforeMoveFlyingHooks;
    private final PlayerBase[] overrideMoveFlyingHooks;
    private final PlayerBase[] afterMoveFlyingHooks;
    public final boolean isMoveFlyingModded;
    private static final List<String> beforeOnDeathHookTypes = new LinkedList();
    private static final List<String> overrideOnDeathHookTypes = new LinkedList();
    private static final List<String> afterOnDeathHookTypes = new LinkedList();
    private final PlayerBase[] beforeOnDeathHooks;
    private final PlayerBase[] overrideOnDeathHooks;
    private final PlayerBase[] afterOnDeathHooks;
    public final boolean isOnDeathModded;
    private static final List<String> beforeOnLivingUpdateHookTypes = new LinkedList();
    private static final List<String> overrideOnLivingUpdateHookTypes = new LinkedList();
    private static final List<String> afterOnLivingUpdateHookTypes = new LinkedList();
    private final PlayerBase[] beforeOnLivingUpdateHooks;
    private final PlayerBase[] overrideOnLivingUpdateHooks;
    private final PlayerBase[] afterOnLivingUpdateHooks;
    public final boolean isOnLivingUpdateModded;
    private static final List<String> beforeOnKillEntityHookTypes = new LinkedList();
    private static final List<String> overrideOnKillEntityHookTypes = new LinkedList();
    private static final List<String> afterOnKillEntityHookTypes = new LinkedList();
    private final PlayerBase[] beforeOnKillEntityHooks;
    private final PlayerBase[] overrideOnKillEntityHooks;
    private final PlayerBase[] afterOnKillEntityHooks;
    public final boolean isOnKillEntityModded;
    private static final List<String> beforeOnUpdateHookTypes = new LinkedList();
    private static final List<String> overrideOnUpdateHookTypes = new LinkedList();
    private static final List<String> afterOnUpdateHookTypes = new LinkedList();
    private final PlayerBase[] beforeOnUpdateHooks;
    private final PlayerBase[] overrideOnUpdateHooks;
    private final PlayerBase[] afterOnUpdateHooks;
    public final boolean isOnUpdateModded;
    private static final List<String> beforePlayStepSoundHookTypes = new LinkedList();
    private static final List<String> overridePlayStepSoundHookTypes = new LinkedList();
    private static final List<String> afterPlayStepSoundHookTypes = new LinkedList();
    private final PlayerBase[] beforePlayStepSoundHooks;
    private final PlayerBase[] overridePlayStepSoundHooks;
    private final PlayerBase[] afterPlayStepSoundHooks;
    public final boolean isPlayStepSoundModded;
    private static final List<String> beforePushOutOfBlocksHookTypes = new LinkedList();
    private static final List<String> overridePushOutOfBlocksHookTypes = new LinkedList();
    private static final List<String> afterPushOutOfBlocksHookTypes = new LinkedList();
    private final PlayerBase[] beforePushOutOfBlocksHooks;
    private final PlayerBase[] overridePushOutOfBlocksHooks;
    private final PlayerBase[] afterPushOutOfBlocksHooks;
    public final boolean isPushOutOfBlocksModded;
    private static final List<String> beforeRayTraceHookTypes = new LinkedList();
    private static final List<String> overrideRayTraceHookTypes = new LinkedList();
    private static final List<String> afterRayTraceHookTypes = new LinkedList();
    private final PlayerBase[] beforeRayTraceHooks;
    private final PlayerBase[] overrideRayTraceHooks;
    private final PlayerBase[] afterRayTraceHooks;
    public final boolean isRayTraceModded;
    private static final List<String> beforeReadEntityFromNBTHookTypes = new LinkedList();
    private static final List<String> overrideReadEntityFromNBTHookTypes = new LinkedList();
    private static final List<String> afterReadEntityFromNBTHookTypes = new LinkedList();
    private final PlayerBase[] beforeReadEntityFromNBTHooks;
    private final PlayerBase[] overrideReadEntityFromNBTHooks;
    private final PlayerBase[] afterReadEntityFromNBTHooks;
    public final boolean isReadEntityFromNBTModded;
    private static final List<String> beforeRespawnPlayerHookTypes = new LinkedList();
    private static final List<String> overrideRespawnPlayerHookTypes = new LinkedList();
    private static final List<String> afterRespawnPlayerHookTypes = new LinkedList();
    private final PlayerBase[] beforeRespawnPlayerHooks;
    private final PlayerBase[] overrideRespawnPlayerHooks;
    private final PlayerBase[] afterRespawnPlayerHooks;
    public final boolean isRespawnPlayerModded;
    private static final List<String> beforeSetDeadHookTypes = new LinkedList();
    private static final List<String> overrideSetDeadHookTypes = new LinkedList();
    private static final List<String> afterSetDeadHookTypes = new LinkedList();
    private final PlayerBase[] beforeSetDeadHooks;
    private final PlayerBase[] overrideSetDeadHooks;
    private final PlayerBase[] afterSetDeadHooks;
    public final boolean isSetDeadModded;
    private static final List<String> beforeSetPositionAndRotationHookTypes = new LinkedList();
    private static final List<String> overrideSetPositionAndRotationHookTypes = new LinkedList();
    private static final List<String> afterSetPositionAndRotationHookTypes = new LinkedList();
    private final PlayerBase[] beforeSetPositionAndRotationHooks;
    private final PlayerBase[] overrideSetPositionAndRotationHooks;
    private final PlayerBase[] afterSetPositionAndRotationHooks;
    public final boolean isSetPositionAndRotationModded;
    private static final List<String> beforeSleepInBedAtHookTypes = new LinkedList();
    private static final List<String> overrideSleepInBedAtHookTypes = new LinkedList();
    private static final List<String> afterSleepInBedAtHookTypes = new LinkedList();
    private final PlayerBase[] beforeSleepInBedAtHooks;
    private final PlayerBase[] overrideSleepInBedAtHooks;
    private final PlayerBase[] afterSleepInBedAtHooks;
    public final boolean isSleepInBedAtModded;
    private static final List<String> beforeSwingItemHookTypes = new LinkedList();
    private static final List<String> overrideSwingItemHookTypes = new LinkedList();
    private static final List<String> afterSwingItemHookTypes = new LinkedList();
    private final PlayerBase[] beforeSwingItemHooks;
    private final PlayerBase[] overrideSwingItemHooks;
    private final PlayerBase[] afterSwingItemHooks;
    public final boolean isSwingItemModded;
    private static final List<String> beforeUpdateEntityActionStateHookTypes = new LinkedList();
    private static final List<String> overrideUpdateEntityActionStateHookTypes = new LinkedList();
    private static final List<String> afterUpdateEntityActionStateHookTypes = new LinkedList();
    private final PlayerBase[] beforeUpdateEntityActionStateHooks;
    private final PlayerBase[] overrideUpdateEntityActionStateHooks;
    private final PlayerBase[] afterUpdateEntityActionStateHooks;
    public final boolean isUpdateEntityActionStateModded;
    private static final List<String> beforeWriteEntityToNBTHookTypes = new LinkedList();
    private static final List<String> overrideWriteEntityToNBTHookTypes = new LinkedList();
    private static final List<String> afterWriteEntityToNBTHookTypes = new LinkedList();
    private final PlayerBase[] beforeWriteEntityToNBTHooks;
    private final PlayerBase[] overrideWriteEntityToNBTHooks;
    private final PlayerBase[] afterWriteEntityToNBTHooks;
    public final boolean isWriteEntityToNBTModded;
    protected final ClientPlayerEntity player;
    private static final List<String> beforeLocalConstructingHookTypes = new LinkedList();
    private static final List<String> afterLocalConstructingHookTypes = new LinkedList();
    private final PlayerBase[] beforeLocalConstructingHooks;
    private final PlayerBase[] afterLocalConstructingHooks;
    private final Map<String, PlayerBase> allBaseObjects = new Hashtable();
    private final Set<String> unmodifiableAllBaseIds;
    private static final Map<String, Constructor<?>> allBaseConstructors = new Hashtable();
    private static final Set<String> unmodifiableAllIds;
    private static final Map<String, String[]> allBaseBeforeSuperiors;
    private static final Map<String, String[]> allBaseBeforeInferiors;
    private static final Map<String, String[]> allBaseOverrideSuperiors;
    private static final Map<String, String[]> allBaseOverrideInferiors;
    private static final Map<String, String[]> allBaseAfterSuperiors;
    private static final Map<String, String[]> allBaseAfterInferiors;
    private static boolean initialized;

    private static void log(String string) {
        System.out.println(string);
        logger.fine(string);
    }

    public static void register(String string, Class<?> class_) {
        register((String)string, (Class)class_, (PlayerBaseSorting)null);
    }

    public static void register(String string, Class<?> class_, PlayerBaseSorting playerBaseSorting) {
        try {
            register(class_, string, playerBaseSorting);
        } catch (RuntimeException var4) {
            if (string != null) {
                log("PlayerAPI: failed to register id '" + string + "'");
            } else {
                log("PlayerAPI: failed to register PlayerBase");
            }

            throw var4;
        }
    }

    private static void register(Class<?> class_, String string, PlayerBaseSorting playerBaseSorting) {
        if (!isCreated) {
            log("PlayerAPI 1.1 Created");
            isCreated = true;
        }

        if (string == null) {
            throw new NullPointerException("Argument 'id' can not be null");
        } else if (class_ == null) {
            throw new NullPointerException("Argument 'baseClass' can not be null");
        } else {
            Constructor var3 = (Constructor)allBaseConstructors.get(string);
            if (var3 != null) {
                throw new IllegalArgumentException("The class '" + class_.getName() + "' can not be registered with the id '" + string + "' because the class '" + var3.getDeclaringClass().getName() + "' has allready been registered with the same id");
            } else {
                Constructor var4;
                try {
                    var4 = class_.getDeclaredConstructor(Classes);
                } catch (Exception var8) {
                    try {
                        var4 = class_.getDeclaredConstructor(Class);
                    } catch (Exception var7) {
                        throw new IllegalArgumentException("Can not find necessary constructor with one argument of type '" + PlayerAPI.class.getName() + "' and eventually a second argument of type 'String' in the class '" + class_.getName() + "'", var8);
                    }
                }

                allBaseConstructors.put(string, var4);
                if (playerBaseSorting != null) {
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeLocalConstructingSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeLocalConstructingInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterLocalConstructingSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterLocalConstructingInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeAddExhaustionSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeAddExhaustionInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideAddExhaustionSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideAddExhaustionInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterAddExhaustionSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterAddExhaustionInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeAddMovementStatSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeAddMovementStatInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideAddMovementStatSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideAddMovementStatInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterAddMovementStatSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterAddMovementStatInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeAddStatSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeAddStatInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideAddStatSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideAddStatInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterAddStatSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterAddStatInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeAttackEntityFromSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeAttackEntityFromInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideAttackEntityFromSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideAttackEntityFromInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterAttackEntityFromSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterAttackEntityFromInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeAlertWolvesSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeAlertWolvesInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideAlertWolvesSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideAlertWolvesInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterAlertWolvesSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterAlertWolvesInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeAttackTargetEntityWithCurrentItemSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeAttackTargetEntityWithCurrentItemInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideAttackTargetEntityWithCurrentItemSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideAttackTargetEntityWithCurrentItemInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterAttackTargetEntityWithCurrentItemSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterAttackTargetEntityWithCurrentItemInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeCanBreatheUnderwaterSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeCanBreatheUnderwaterInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideCanBreatheUnderwaterSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideCanBreatheUnderwaterInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterCanBreatheUnderwaterSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterCanBreatheUnderwaterInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeCanHarvestBlockSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeCanHarvestBlockInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideCanHarvestBlockSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideCanHarvestBlockInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterCanHarvestBlockSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterCanHarvestBlockInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeCanPlayerEditSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeCanPlayerEditInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideCanPlayerEditSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideCanPlayerEditInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterCanPlayerEditSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterCanPlayerEditInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeCanTriggerWalkingSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeCanTriggerWalkingInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideCanTriggerWalkingSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideCanTriggerWalkingInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterCanTriggerWalkingSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterCanTriggerWalkingInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeCloseScreenSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeCloseScreenInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideCloseScreenSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideCloseScreenInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterCloseScreenSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterCloseScreenInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeDamageEntitySuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeDamageEntityInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideDamageEntitySuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideDamageEntityInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterDamageEntitySuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterDamageEntityInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeDisplayGUIBrewingStandSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeDisplayGUIBrewingStandInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideDisplayGUIBrewingStandSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideDisplayGUIBrewingStandInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterDisplayGUIBrewingStandSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterDisplayGUIBrewingStandInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeDisplayGUIChestSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeDisplayGUIChestInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideDisplayGUIChestSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideDisplayGUIChestInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterDisplayGUIChestSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterDisplayGUIChestInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeDisplayGUIDispenserSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeDisplayGUIDispenserInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideDisplayGUIDispenserSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideDisplayGUIDispenserInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterDisplayGUIDispenserSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterDisplayGUIDispenserInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeDisplayGUIEditSignSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeDisplayGUIEditSignInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideDisplayGUIEditSignSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideDisplayGUIEditSignInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterDisplayGUIEditSignSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterDisplayGUIEditSignInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeDisplayGUIEnchantmentSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeDisplayGUIEnchantmentInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideDisplayGUIEnchantmentSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideDisplayGUIEnchantmentInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterDisplayGUIEnchantmentSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterDisplayGUIEnchantmentInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeDisplayGUIFurnaceSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeDisplayGUIFurnaceInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideDisplayGUIFurnaceSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideDisplayGUIFurnaceInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterDisplayGUIFurnaceSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterDisplayGUIFurnaceInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeDisplayWorkbenchGUISuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeDisplayWorkbenchGUIInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideDisplayWorkbenchGUISuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideDisplayWorkbenchGUIInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterDisplayWorkbenchGUISuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterDisplayWorkbenchGUIInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeDropOneItemSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeDropOneItemInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideDropOneItemSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideDropOneItemInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterDropOneItemSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterDropOneItemInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeDropPlayerItemSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeDropPlayerItemInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideDropPlayerItemSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideDropPlayerItemInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterDropPlayerItemSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterDropPlayerItemInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeDropPlayerItemWithRandomChoiceSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeDropPlayerItemWithRandomChoiceInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideDropPlayerItemWithRandomChoiceSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideDropPlayerItemWithRandomChoiceInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterDropPlayerItemWithRandomChoiceSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterDropPlayerItemWithRandomChoiceInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeFallSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeFallInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideFallSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideFallInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterFallSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterFallInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeGetBrightnessSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeGetBrightnessInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideGetBrightnessSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideGetBrightnessInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterGetBrightnessSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterGetBrightnessInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeGetBrightnessForRenderSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeGetBrightnessForRenderInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideGetBrightnessForRenderSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideGetBrightnessForRenderInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterGetBrightnessForRenderSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterGetBrightnessForRenderInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeGetCurrentPlayerStrVsBlockSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeGetCurrentPlayerStrVsBlockInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideGetCurrentPlayerStrVsBlockSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideGetCurrentPlayerStrVsBlockInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterGetCurrentPlayerStrVsBlockSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterGetCurrentPlayerStrVsBlockInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeGetDistanceSqSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeGetDistanceSqInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideGetDistanceSqSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideGetDistanceSqInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterGetDistanceSqSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterGetDistanceSqInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeGetDistanceSqToEntitySuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeGetDistanceSqToEntityInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideGetDistanceSqToEntitySuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideGetDistanceSqToEntityInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterGetDistanceSqToEntitySuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterGetDistanceSqToEntityInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeGetFOVMultiplierSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeGetFOVMultiplierInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideGetFOVMultiplierSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideGetFOVMultiplierInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterGetFOVMultiplierSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterGetFOVMultiplierInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeGetHurtSoundSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeGetHurtSoundInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideGetHurtSoundSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideGetHurtSoundInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterGetHurtSoundSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterGetHurtSoundInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeGetItemIconSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeGetItemIconInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideGetItemIconSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideGetItemIconInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterGetItemIconSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterGetItemIconInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeGetSleepTimerSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeGetSleepTimerInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideGetSleepTimerSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideGetSleepTimerInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterGetSleepTimerSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterGetSleepTimerInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeGetSpeedModifierSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeGetSpeedModifierInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideGetSpeedModifierSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideGetSpeedModifierInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterGetSpeedModifierSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterGetSpeedModifierInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeHandleLavaMovementSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeHandleLavaMovementInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideHandleLavaMovementSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideHandleLavaMovementInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterHandleLavaMovementSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterHandleLavaMovementInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeHandleWaterMovementSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeHandleWaterMovementInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideHandleWaterMovementSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideHandleWaterMovementInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterHandleWaterMovementSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterHandleWaterMovementInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeHealSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeHealInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideHealSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideHealInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterHealSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterHealInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeIsEntityInsideOpaqueBlockSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeIsEntityInsideOpaqueBlockInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideIsEntityInsideOpaqueBlockSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideIsEntityInsideOpaqueBlockInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterIsEntityInsideOpaqueBlockSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterIsEntityInsideOpaqueBlockInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeIsInWaterSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeIsInWaterInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideIsInWaterSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideIsInWaterInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterIsInWaterSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterIsInWaterInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeIsInsideOfMaterialSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeIsInsideOfMaterialInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideIsInsideOfMaterialSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideIsInsideOfMaterialInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterIsInsideOfMaterialSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterIsInsideOfMaterialInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeIsOnLadderSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeIsOnLadderInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideIsOnLadderSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideIsOnLadderInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterIsOnLadderSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterIsOnLadderInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeIsSneakingSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeIsSneakingInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideIsSneakingSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideIsSneakingInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterIsSneakingSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterIsSneakingInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeIsSprintingSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeIsSprintingInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideIsSprintingSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideIsSprintingInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterIsSprintingSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterIsSprintingInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeJumpSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeJumpInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideJumpSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideJumpInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterJumpSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterJumpInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeKnockBackSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeKnockBackInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideKnockBackSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideKnockBackInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterKnockBackSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterKnockBackInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeMoveEntitySuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeMoveEntityInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideMoveEntitySuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideMoveEntityInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterMoveEntitySuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterMoveEntityInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeMoveEntityWithHeadingSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeMoveEntityWithHeadingInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideMoveEntityWithHeadingSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideMoveEntityWithHeadingInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterMoveEntityWithHeadingSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterMoveEntityWithHeadingInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeMoveFlyingSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeMoveFlyingInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideMoveFlyingSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideMoveFlyingInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterMoveFlyingSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterMoveFlyingInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeOnDeathSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeOnDeathInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideOnDeathSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideOnDeathInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterOnDeathSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterOnDeathInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeOnLivingUpdateSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeOnLivingUpdateInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideOnLivingUpdateSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideOnLivingUpdateInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterOnLivingUpdateSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterOnLivingUpdateInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeOnKillEntitySuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeOnKillEntityInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideOnKillEntitySuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideOnKillEntityInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterOnKillEntitySuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterOnKillEntityInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeOnUpdateSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeOnUpdateInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideOnUpdateSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideOnUpdateInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterOnUpdateSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterOnUpdateInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforePlayStepSoundSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforePlayStepSoundInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverridePlayStepSoundSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverridePlayStepSoundInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterPlayStepSoundSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterPlayStepSoundInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforePushOutOfBlocksSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforePushOutOfBlocksInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverridePushOutOfBlocksSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverridePushOutOfBlocksInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterPushOutOfBlocksSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterPushOutOfBlocksInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeRayTraceSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeRayTraceInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideRayTraceSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideRayTraceInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterRayTraceSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterRayTraceInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeReadEntityFromNBTSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeReadEntityFromNBTInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideReadEntityFromNBTSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideReadEntityFromNBTInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterReadEntityFromNBTSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterReadEntityFromNBTInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeRespawnPlayerSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeRespawnPlayerInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideRespawnPlayerSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideRespawnPlayerInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterRespawnPlayerSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterRespawnPlayerInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeSetDeadSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeSetDeadInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideSetDeadSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideSetDeadInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterSetDeadSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterSetDeadInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeSetPositionAndRotationSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeSetPositionAndRotationInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideSetPositionAndRotationSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideSetPositionAndRotationInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterSetPositionAndRotationSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterSetPositionAndRotationInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeSleepInBedAtSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeSleepInBedAtInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideSleepInBedAtSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideSleepInBedAtInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterSleepInBedAtSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterSleepInBedAtInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeSwingItemSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeSwingItemInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideSwingItemSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideSwingItemInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterSwingItemSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterSwingItemInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeUpdateEntityActionStateSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeUpdateEntityActionStateInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideUpdateEntityActionStateSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideUpdateEntityActionStateInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterUpdateEntityActionStateSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterUpdateEntityActionStateInferiors());
                    addSorting(string, allBaseBeforeSuperiors, playerBaseSorting.getBeforeWriteEntityToNBTSuperiors());
                    addSorting(string, allBaseBeforeInferiors, playerBaseSorting.getBeforeWriteEntityToNBTInferiors());
                    addSorting(string, allBaseOverrideSuperiors, playerBaseSorting.getOverrideWriteEntityToNBTSuperiors());
                    addSorting(string, allBaseOverrideInferiors, playerBaseSorting.getOverrideWriteEntityToNBTInferiors());
                    addSorting(string, allBaseAfterSuperiors, playerBaseSorting.getAfterWriteEntityToNBTSuperiors());
                    addSorting(string, allBaseAfterInferiors, playerBaseSorting.getAfterWriteEntityToNBTInferiors());
                }

                addMethod(string, class_, beforeLocalConstructingHookTypes, "beforeLocalConstructing", Minecraft.class, World.class, Session.class, Integer.TYPE);
                addMethod(string, class_, afterLocalConstructingHookTypes, "afterLocalConstructing", Minecraft.class, World.class, Session.class, Integer.TYPE);
                addMethod(string, class_, beforeAddExhaustionHookTypes, "beforeAddExhaustion", Float.TYPE);
                addMethod(string, class_, overrideAddExhaustionHookTypes, "addExhaustion", Float.TYPE);
                addMethod(string, class_, afterAddExhaustionHookTypes, "afterAddExhaustion", Float.TYPE);
                addMethod(string, class_, beforeAddMovementStatHookTypes, "beforeAddMovementStat", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, overrideAddMovementStatHookTypes, "addMovementStat", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, afterAddMovementStatHookTypes, "afterAddMovementStat", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, beforeAddStatHookTypes, "beforeAddStat", Stat.class, Integer.TYPE);
                addMethod(string, class_, overrideAddStatHookTypes, "addStat", Stat.class, Integer.TYPE);
                addMethod(string, class_, afterAddStatHookTypes, "afterAddStat", Stat.class, Integer.TYPE);
                addMethod(string, class_, beforeAttackEntityFromHookTypes, "beforeAttackEntityFrom", DamageSource.class, Integer.TYPE);
                addMethod(string, class_, overrideAttackEntityFromHookTypes, "attackEntityFrom", DamageSource.class, Integer.TYPE);
                addMethod(string, class_, afterAttackEntityFromHookTypes, "afterAttackEntityFrom", DamageSource.class, Integer.TYPE);
                addMethod(string, class_, beforeAlertWolvesHookTypes, "beforeAlertWolves", MobEntity.class, Boolean.TYPE);
                addMethod(string, class_, overrideAlertWolvesHookTypes, "alertWolves", MobEntity.class, Boolean.TYPE);
                addMethod(string, class_, afterAlertWolvesHookTypes, "afterAlertWolves", MobEntity.class, Boolean.TYPE);
                addMethod(string, class_, beforeAttackTargetEntityWithCurrentItemHookTypes, "beforeAttackTargetEntityWithCurrentItem", Entity.class);
                addMethod(string, class_, overrideAttackTargetEntityWithCurrentItemHookTypes, "attackTargetEntityWithCurrentItem", Entity.class);
                addMethod(string, class_, afterAttackTargetEntityWithCurrentItemHookTypes, "afterAttackTargetEntityWithCurrentItem", Entity.class);
                addMethod(string, class_, beforeCanBreatheUnderwaterHookTypes, "beforeCanBreatheUnderwater");
                addMethod(string, class_, overrideCanBreatheUnderwaterHookTypes, "canBreatheUnderwater");
                addMethod(string, class_, afterCanBreatheUnderwaterHookTypes, "afterCanBreatheUnderwater");
                addMethod(string, class_, beforeCanHarvestBlockHookTypes, "beforeCanHarvestBlock", Block.class);
                addMethod(string, class_, overrideCanHarvestBlockHookTypes, "canHarvestBlock", Block.class);
                addMethod(string, class_, afterCanHarvestBlockHookTypes, "afterCanHarvestBlock", Block.class);
                addMethod(string, class_, beforeCanPlayerEditHookTypes, "beforeCanPlayerEdit", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, overrideCanPlayerEditHookTypes, "canPlayerEdit", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, afterCanPlayerEditHookTypes, "afterCanPlayerEdit", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, beforeCanTriggerWalkingHookTypes, "beforeCanTriggerWalking");
                addMethod(string, class_, overrideCanTriggerWalkingHookTypes, "canTriggerWalking");
                addMethod(string, class_, afterCanTriggerWalkingHookTypes, "afterCanTriggerWalking");
                addMethod(string, class_, beforeCloseScreenHookTypes, "beforeCloseScreen");
                addMethod(string, class_, overrideCloseScreenHookTypes, "closeScreen");
                addMethod(string, class_, afterCloseScreenHookTypes, "afterCloseScreen");
                addMethod(string, class_, beforeDamageEntityHookTypes, "beforeDamageEntity", DamageSource.class, Integer.TYPE);
                addMethod(string, class_, overrideDamageEntityHookTypes, "damageEntity", DamageSource.class, Integer.TYPE);
                addMethod(string, class_, afterDamageEntityHookTypes, "afterDamageEntity", DamageSource.class, Integer.TYPE);
                addMethod(string, class_, beforeDisplayGUIBrewingStandHookTypes, "beforeDisplayGUIBrewingStand", BrewingStandBlockEntity.class);
                addMethod(string, class_, overrideDisplayGUIBrewingStandHookTypes, "displayGUIBrewingStand", BrewingStandBlockEntity.class);
                addMethod(string, class_, afterDisplayGUIBrewingStandHookTypes, "afterDisplayGUIBrewingStand", BrewingStandBlockEntity.class);
                addMethod(string, class_, beforeDisplayGUIChestHookTypes, "beforeDisplayGUIChest", Inventory.class);
                addMethod(string, class_, overrideDisplayGUIChestHookTypes, "displayGUIChest", Inventory.class);
                addMethod(string, class_, afterDisplayGUIChestHookTypes, "afterDisplayGUIChest", Inventory.class);
                addMethod(string, class_, beforeDisplayGUIDispenserHookTypes, "beforeDisplayGUIDispenser", DispenserBlockEntity.class);
                addMethod(string, class_, overrideDisplayGUIDispenserHookTypes, "displayGUIDispenser", DispenserBlockEntity.class);
                addMethod(string, class_, afterDisplayGUIDispenserHookTypes, "afterDisplayGUIDispenser", DispenserBlockEntity.class);
                addMethod(string, class_, beforeDisplayGUIEditSignHookTypes, "beforeDisplayGUIEditSign", SignBlockEntity.class);
                addMethod(string, class_, overrideDisplayGUIEditSignHookTypes, "displayGUIEditSign", SignBlockEntity.class);
                addMethod(string, class_, afterDisplayGUIEditSignHookTypes, "afterDisplayGUIEditSign", SignBlockEntity.class);
                addMethod(string, class_, beforeDisplayGUIEnchantmentHookTypes, "beforeDisplayGUIEnchantment", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, overrideDisplayGUIEnchantmentHookTypes, "displayGUIEnchantment", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, afterDisplayGUIEnchantmentHookTypes, "afterDisplayGUIEnchantment", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, beforeDisplayGUIFurnaceHookTypes, "beforeDisplayGUIFurnace", FurnaceBlockEntity.class);
                addMethod(string, class_, overrideDisplayGUIFurnaceHookTypes, "displayGUIFurnace", FurnaceBlockEntity.class);
                addMethod(string, class_, afterDisplayGUIFurnaceHookTypes, "afterDisplayGUIFurnace", FurnaceBlockEntity.class);
                addMethod(string, class_, beforeDisplayWorkbenchGUIHookTypes, "beforeDisplayWorkbenchGUI", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, overrideDisplayWorkbenchGUIHookTypes, "displayWorkbenchGUI", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, afterDisplayWorkbenchGUIHookTypes, "afterDisplayWorkbenchGUI", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, beforeDropOneItemHookTypes, "beforeDropOneItem");
                addMethod(string, class_, overrideDropOneItemHookTypes, "dropOneItem");
                addMethod(string, class_, afterDropOneItemHookTypes, "afterDropOneItem");
                addMethod(string, class_, beforeDropPlayerItemHookTypes, "beforeDropPlayerItem", ItemStack.class);
                addMethod(string, class_, overrideDropPlayerItemHookTypes, "dropPlayerItem", ItemStack.class);
                addMethod(string, class_, afterDropPlayerItemHookTypes, "afterDropPlayerItem", ItemStack.class);
                addMethod(string, class_, beforeDropPlayerItemWithRandomChoiceHookTypes, "beforeDropPlayerItemWithRandomChoice", ItemStack.class, Boolean.TYPE);
                addMethod(string, class_, overrideDropPlayerItemWithRandomChoiceHookTypes, "dropPlayerItemWithRandomChoice", ItemStack.class, Boolean.TYPE);
                addMethod(string, class_, afterDropPlayerItemWithRandomChoiceHookTypes, "afterDropPlayerItemWithRandomChoice", ItemStack.class, Boolean.TYPE);
                addMethod(string, class_, beforeFallHookTypes, "beforeFall", Float.TYPE);
                addMethod(string, class_, overrideFallHookTypes, "fall", Float.TYPE);
                addMethod(string, class_, afterFallHookTypes, "afterFall", Float.TYPE);
                addMethod(string, class_, beforeGetBrightnessHookTypes, "beforeGetBrightness", Float.TYPE);
                addMethod(string, class_, overrideGetBrightnessHookTypes, "getBrightness", Float.TYPE);
                addMethod(string, class_, afterGetBrightnessHookTypes, "afterGetBrightness", Float.TYPE);
                addMethod(string, class_, beforeGetBrightnessForRenderHookTypes, "beforeGetBrightnessForRender", Float.TYPE);
                addMethod(string, class_, overrideGetBrightnessForRenderHookTypes, "getBrightnessForRender", Float.TYPE);
                addMethod(string, class_, afterGetBrightnessForRenderHookTypes, "afterGetBrightnessForRender", Float.TYPE);
                addMethod(string, class_, beforeGetCurrentPlayerStrVsBlockHookTypes, "beforeGetCurrentPlayerStrVsBlock", Block.class);
                addMethod(string, class_, overrideGetCurrentPlayerStrVsBlockHookTypes, "getCurrentPlayerStrVsBlock", Block.class);
                addMethod(string, class_, afterGetCurrentPlayerStrVsBlockHookTypes, "afterGetCurrentPlayerStrVsBlock", Block.class);
                addMethod(string, class_, beforeGetDistanceSqHookTypes, "beforeGetDistanceSq", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, overrideGetDistanceSqHookTypes, "getDistanceSq", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, afterGetDistanceSqHookTypes, "afterGetDistanceSq", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, beforeGetDistanceSqToEntityHookTypes, "beforeGetDistanceSqToEntity", Entity.class);
                addMethod(string, class_, overrideGetDistanceSqToEntityHookTypes, "getDistanceSqToEntity", Entity.class);
                addMethod(string, class_, afterGetDistanceSqToEntityHookTypes, "afterGetDistanceSqToEntity", Entity.class);
                addMethod(string, class_, beforeGetFOVMultiplierHookTypes, "beforeGetFOVMultiplier");
                addMethod(string, class_, overrideGetFOVMultiplierHookTypes, "getFOVMultiplier");
                addMethod(string, class_, afterGetFOVMultiplierHookTypes, "afterGetFOVMultiplier");
                addMethod(string, class_, beforeGetHurtSoundHookTypes, "beforeGetHurtSound");
                addMethod(string, class_, overrideGetHurtSoundHookTypes, "getHurtSound");
                addMethod(string, class_, afterGetHurtSoundHookTypes, "afterGetHurtSound");
                addMethod(string, class_, beforeGetItemIconHookTypes, "beforeGetItemIcon", ItemStack.class, Integer.TYPE);
                addMethod(string, class_, overrideGetItemIconHookTypes, "getItemIcon", ItemStack.class, Integer.TYPE);
                addMethod(string, class_, afterGetItemIconHookTypes, "afterGetItemIcon", ItemStack.class, Integer.TYPE);
                addMethod(string, class_, beforeGetSleepTimerHookTypes, "beforeGetSleepTimer");
                addMethod(string, class_, overrideGetSleepTimerHookTypes, "getSleepTimer");
                addMethod(string, class_, afterGetSleepTimerHookTypes, "afterGetSleepTimer");
                addMethod(string, class_, beforeGetSpeedModifierHookTypes, "beforeGetSpeedModifier");
                addMethod(string, class_, overrideGetSpeedModifierHookTypes, "getSpeedModifier");
                addMethod(string, class_, afterGetSpeedModifierHookTypes, "afterGetSpeedModifier");
                addMethod(string, class_, beforeHandleLavaMovementHookTypes, "beforeHandleLavaMovement");
                addMethod(string, class_, overrideHandleLavaMovementHookTypes, "handleLavaMovement");
                addMethod(string, class_, afterHandleLavaMovementHookTypes, "afterHandleLavaMovement");
                addMethod(string, class_, beforeHandleWaterMovementHookTypes, "beforeHandleWaterMovement");
                addMethod(string, class_, overrideHandleWaterMovementHookTypes, "handleWaterMovement");
                addMethod(string, class_, afterHandleWaterMovementHookTypes, "afterHandleWaterMovement");
                addMethod(string, class_, beforeHealHookTypes, "beforeHeal", Integer.TYPE);
                addMethod(string, class_, overrideHealHookTypes, "heal", Integer.TYPE);
                addMethod(string, class_, afterHealHookTypes, "afterHeal", Integer.TYPE);
                addMethod(string, class_, beforeIsEntityInsideOpaqueBlockHookTypes, "beforeIsEntityInsideOpaqueBlock");
                addMethod(string, class_, overrideIsEntityInsideOpaqueBlockHookTypes, "isEntityInsideOpaqueBlock");
                addMethod(string, class_, afterIsEntityInsideOpaqueBlockHookTypes, "afterIsEntityInsideOpaqueBlock");
                addMethod(string, class_, beforeIsInWaterHookTypes, "beforeIsInWater");
                addMethod(string, class_, overrideIsInWaterHookTypes, "isInWater");
                addMethod(string, class_, afterIsInWaterHookTypes, "afterIsInWater");
                addMethod(string, class_, beforeIsInsideOfMaterialHookTypes, "beforeIsInsideOfMaterial", Material.class);
                addMethod(string, class_, overrideIsInsideOfMaterialHookTypes, "isInsideOfMaterial", Material.class);
                addMethod(string, class_, afterIsInsideOfMaterialHookTypes, "afterIsInsideOfMaterial", Material.class);
                addMethod(string, class_, beforeIsOnLadderHookTypes, "beforeIsOnLadder");
                addMethod(string, class_, overrideIsOnLadderHookTypes, "isOnLadder");
                addMethod(string, class_, afterIsOnLadderHookTypes, "afterIsOnLadder");
                addMethod(string, class_, beforeIsSneakingHookTypes, "beforeIsSneaking");
                addMethod(string, class_, overrideIsSneakingHookTypes, "isSneaking");
                addMethod(string, class_, afterIsSneakingHookTypes, "afterIsSneaking");
                addMethod(string, class_, beforeIsSprintingHookTypes, "beforeIsSprinting");
                addMethod(string, class_, overrideIsSprintingHookTypes, "isSprinting");
                addMethod(string, class_, afterIsSprintingHookTypes, "afterIsSprinting");
                addMethod(string, class_, beforeJumpHookTypes, "beforeJump");
                addMethod(string, class_, overrideJumpHookTypes, "jump");
                addMethod(string, class_, afterJumpHookTypes, "afterJump");
                addMethod(string, class_, beforeKnockBackHookTypes, "beforeKnockBack", Entity.class, Integer.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, overrideKnockBackHookTypes, "knockBack", Entity.class, Integer.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, afterKnockBackHookTypes, "afterKnockBack", Entity.class, Integer.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, beforeMoveEntityHookTypes, "beforeMoveEntity", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, overrideMoveEntityHookTypes, "moveEntity", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, afterMoveEntityHookTypes, "afterMoveEntity", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, beforeMoveEntityWithHeadingHookTypes, "beforeMoveEntityWithHeading", Float.TYPE, Float.TYPE);
                addMethod(string, class_, overrideMoveEntityWithHeadingHookTypes, "moveEntityWithHeading", Float.TYPE, Float.TYPE);
                addMethod(string, class_, afterMoveEntityWithHeadingHookTypes, "afterMoveEntityWithHeading", Float.TYPE, Float.TYPE);
                addMethod(string, class_, beforeMoveFlyingHookTypes, "beforeMoveFlying", Float.TYPE, Float.TYPE, Float.TYPE);
                addMethod(string, class_, overrideMoveFlyingHookTypes, "moveFlying", Float.TYPE, Float.TYPE, Float.TYPE);
                addMethod(string, class_, afterMoveFlyingHookTypes, "afterMoveFlying", Float.TYPE, Float.TYPE, Float.TYPE);
                addMethod(string, class_, beforeOnDeathHookTypes, "beforeOnDeath", DamageSource.class);
                addMethod(string, class_, overrideOnDeathHookTypes, "onDeath", DamageSource.class);
                addMethod(string, class_, afterOnDeathHookTypes, "afterOnDeath", DamageSource.class);
                addMethod(string, class_, beforeOnLivingUpdateHookTypes, "beforeOnLivingUpdate");
                addMethod(string, class_, overrideOnLivingUpdateHookTypes, "onLivingUpdate");
                addMethod(string, class_, afterOnLivingUpdateHookTypes, "afterOnLivingUpdate");
                addMethod(string, class_, beforeOnKillEntityHookTypes, "beforeOnKillEntity", MobEntity.class);
                addMethod(string, class_, overrideOnKillEntityHookTypes, "onKillEntity", MobEntity.class);
                addMethod(string, class_, afterOnKillEntityHookTypes, "afterOnKillEntity", MobEntity.class);
                addMethod(string, class_, beforeOnUpdateHookTypes, "beforeOnUpdate");
                addMethod(string, class_, overrideOnUpdateHookTypes, "onUpdate");
                addMethod(string, class_, afterOnUpdateHookTypes, "afterOnUpdate");
                addMethod(string, class_, beforePlayStepSoundHookTypes, "beforePlayStepSound", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, overridePlayStepSoundHookTypes, "playStepSound", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, afterPlayStepSoundHookTypes, "afterPlayStepSound", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, beforePushOutOfBlocksHookTypes, "beforePushOutOfBlocks", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, overridePushOutOfBlocksHookTypes, "pushOutOfBlocks", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, afterPushOutOfBlocksHookTypes, "afterPushOutOfBlocks", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, beforeRayTraceHookTypes, "beforeRayTrace", Double.TYPE, Float.TYPE);
                addMethod(string, class_, overrideRayTraceHookTypes, "rayTrace", Double.TYPE, Float.TYPE);
                addMethod(string, class_, afterRayTraceHookTypes, "afterRayTrace", Double.TYPE, Float.TYPE);
                addMethod(string, class_, beforeReadEntityFromNBTHookTypes, "beforeReadEntityFromNBT", NbtCompound.class);
                addMethod(string, class_, overrideReadEntityFromNBTHookTypes, "readEntityFromNBT", NbtCompound.class);
                addMethod(string, class_, afterReadEntityFromNBTHookTypes, "afterReadEntityFromNBT", NbtCompound.class);
                addMethod(string, class_, beforeRespawnPlayerHookTypes, "beforeRespawnPlayer");
                addMethod(string, class_, overrideRespawnPlayerHookTypes, "respawnPlayer");
                addMethod(string, class_, afterRespawnPlayerHookTypes, "afterRespawnPlayer");
                addMethod(string, class_, beforeSetDeadHookTypes, "beforeSetDead");
                addMethod(string, class_, overrideSetDeadHookTypes, "setDead");
                addMethod(string, class_, afterSetDeadHookTypes, "afterSetDead");
                addMethod(string, class_, beforeSetPositionAndRotationHookTypes, "beforeSetPositionAndRotation", Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE);
                addMethod(string, class_, overrideSetPositionAndRotationHookTypes, "setPositionAndRotation", Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE);
                addMethod(string, class_, afterSetPositionAndRotationHookTypes, "afterSetPositionAndRotation", Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE);
                addMethod(string, class_, beforeSleepInBedAtHookTypes, "beforeSleepInBedAt", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, overrideSleepInBedAtHookTypes, "sleepInBedAt", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, afterSleepInBedAtHookTypes, "afterSleepInBedAt", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, beforeSwingItemHookTypes, "beforeSwingItem");
                addMethod(string, class_, overrideSwingItemHookTypes, "swingItem");
                addMethod(string, class_, afterSwingItemHookTypes, "afterSwingItem");
                addMethod(string, class_, beforeUpdateEntityActionStateHookTypes, "beforeUpdateEntityActionState");
                addMethod(string, class_, overrideUpdateEntityActionStateHookTypes, "updateEntityActionState");
                addMethod(string, class_, afterUpdateEntityActionStateHookTypes, "afterUpdateEntityActionState");
                addMethod(string, class_, beforeWriteEntityToNBTHookTypes, "beforeWriteEntityToNBT", NbtCompound.class);
                addMethod(string, class_, overrideWriteEntityToNBTHookTypes, "writeEntityToNBT", NbtCompound.class);
                addMethod(string, class_, afterWriteEntityToNBTHookTypes, "afterWriteEntityToNBT", NbtCompound.class);
                System.out.println("PlayerAPI: registered " + string);
                logger.fine("PlayerAPI: registered class '" + class_.getName() + "' with id '" + string + "'");
                initialized = false;
            }
        }
    }

    public static boolean unregister(String string) {
        if (string != null && allBaseConstructors.remove(string) != null) {
            allBaseBeforeSuperiors.remove(string);
            allBaseBeforeInferiors.remove(string);
            allBaseOverrideSuperiors.remove(string);
            allBaseOverrideInferiors.remove(string);
            allBaseAfterSuperiors.remove(string);
            allBaseAfterInferiors.remove(string);
            beforeLocalConstructingHookTypes.remove(string);
            afterLocalConstructingHookTypes.remove(string);
            beforeAddExhaustionHookTypes.remove(string);
            overrideAddExhaustionHookTypes.remove(string);
            afterAddExhaustionHookTypes.remove(string);
            beforeAddMovementStatHookTypes.remove(string);
            overrideAddMovementStatHookTypes.remove(string);
            afterAddMovementStatHookTypes.remove(string);
            beforeAddStatHookTypes.remove(string);
            overrideAddStatHookTypes.remove(string);
            afterAddStatHookTypes.remove(string);
            beforeAttackEntityFromHookTypes.remove(string);
            overrideAttackEntityFromHookTypes.remove(string);
            afterAttackEntityFromHookTypes.remove(string);
            beforeAlertWolvesHookTypes.remove(string);
            overrideAlertWolvesHookTypes.remove(string);
            afterAlertWolvesHookTypes.remove(string);
            beforeAttackTargetEntityWithCurrentItemHookTypes.remove(string);
            overrideAttackTargetEntityWithCurrentItemHookTypes.remove(string);
            afterAttackTargetEntityWithCurrentItemHookTypes.remove(string);
            beforeCanBreatheUnderwaterHookTypes.remove(string);
            overrideCanBreatheUnderwaterHookTypes.remove(string);
            afterCanBreatheUnderwaterHookTypes.remove(string);
            beforeCanHarvestBlockHookTypes.remove(string);
            overrideCanHarvestBlockHookTypes.remove(string);
            afterCanHarvestBlockHookTypes.remove(string);
            beforeCanPlayerEditHookTypes.remove(string);
            overrideCanPlayerEditHookTypes.remove(string);
            afterCanPlayerEditHookTypes.remove(string);
            beforeCanTriggerWalkingHookTypes.remove(string);
            overrideCanTriggerWalkingHookTypes.remove(string);
            afterCanTriggerWalkingHookTypes.remove(string);
            beforeCloseScreenHookTypes.remove(string);
            overrideCloseScreenHookTypes.remove(string);
            afterCloseScreenHookTypes.remove(string);
            beforeDamageEntityHookTypes.remove(string);
            overrideDamageEntityHookTypes.remove(string);
            afterDamageEntityHookTypes.remove(string);
            beforeDisplayGUIBrewingStandHookTypes.remove(string);
            overrideDisplayGUIBrewingStandHookTypes.remove(string);
            afterDisplayGUIBrewingStandHookTypes.remove(string);
            beforeDisplayGUIChestHookTypes.remove(string);
            overrideDisplayGUIChestHookTypes.remove(string);
            afterDisplayGUIChestHookTypes.remove(string);
            beforeDisplayGUIDispenserHookTypes.remove(string);
            overrideDisplayGUIDispenserHookTypes.remove(string);
            afterDisplayGUIDispenserHookTypes.remove(string);
            beforeDisplayGUIEditSignHookTypes.remove(string);
            overrideDisplayGUIEditSignHookTypes.remove(string);
            afterDisplayGUIEditSignHookTypes.remove(string);
            beforeDisplayGUIEnchantmentHookTypes.remove(string);
            overrideDisplayGUIEnchantmentHookTypes.remove(string);
            afterDisplayGUIEnchantmentHookTypes.remove(string);
            beforeDisplayGUIFurnaceHookTypes.remove(string);
            overrideDisplayGUIFurnaceHookTypes.remove(string);
            afterDisplayGUIFurnaceHookTypes.remove(string);
            beforeDisplayWorkbenchGUIHookTypes.remove(string);
            overrideDisplayWorkbenchGUIHookTypes.remove(string);
            afterDisplayWorkbenchGUIHookTypes.remove(string);
            beforeDropOneItemHookTypes.remove(string);
            overrideDropOneItemHookTypes.remove(string);
            afterDropOneItemHookTypes.remove(string);
            beforeDropPlayerItemHookTypes.remove(string);
            overrideDropPlayerItemHookTypes.remove(string);
            afterDropPlayerItemHookTypes.remove(string);
            beforeDropPlayerItemWithRandomChoiceHookTypes.remove(string);
            overrideDropPlayerItemWithRandomChoiceHookTypes.remove(string);
            afterDropPlayerItemWithRandomChoiceHookTypes.remove(string);
            beforeFallHookTypes.remove(string);
            overrideFallHookTypes.remove(string);
            afterFallHookTypes.remove(string);
            beforeGetBrightnessHookTypes.remove(string);
            overrideGetBrightnessHookTypes.remove(string);
            afterGetBrightnessHookTypes.remove(string);
            beforeGetBrightnessForRenderHookTypes.remove(string);
            overrideGetBrightnessForRenderHookTypes.remove(string);
            afterGetBrightnessForRenderHookTypes.remove(string);
            beforeGetCurrentPlayerStrVsBlockHookTypes.remove(string);
            overrideGetCurrentPlayerStrVsBlockHookTypes.remove(string);
            afterGetCurrentPlayerStrVsBlockHookTypes.remove(string);
            beforeGetDistanceSqHookTypes.remove(string);
            overrideGetDistanceSqHookTypes.remove(string);
            afterGetDistanceSqHookTypes.remove(string);
            beforeGetDistanceSqToEntityHookTypes.remove(string);
            overrideGetDistanceSqToEntityHookTypes.remove(string);
            afterGetDistanceSqToEntityHookTypes.remove(string);
            beforeGetFOVMultiplierHookTypes.remove(string);
            overrideGetFOVMultiplierHookTypes.remove(string);
            afterGetFOVMultiplierHookTypes.remove(string);
            beforeGetHurtSoundHookTypes.remove(string);
            overrideGetHurtSoundHookTypes.remove(string);
            afterGetHurtSoundHookTypes.remove(string);
            beforeGetItemIconHookTypes.remove(string);
            overrideGetItemIconHookTypes.remove(string);
            afterGetItemIconHookTypes.remove(string);
            beforeGetSleepTimerHookTypes.remove(string);
            overrideGetSleepTimerHookTypes.remove(string);
            afterGetSleepTimerHookTypes.remove(string);
            beforeGetSpeedModifierHookTypes.remove(string);
            overrideGetSpeedModifierHookTypes.remove(string);
            afterGetSpeedModifierHookTypes.remove(string);
            beforeHandleLavaMovementHookTypes.remove(string);
            overrideHandleLavaMovementHookTypes.remove(string);
            afterHandleLavaMovementHookTypes.remove(string);
            beforeHandleWaterMovementHookTypes.remove(string);
            overrideHandleWaterMovementHookTypes.remove(string);
            afterHandleWaterMovementHookTypes.remove(string);
            beforeHealHookTypes.remove(string);
            overrideHealHookTypes.remove(string);
            afterHealHookTypes.remove(string);
            beforeIsEntityInsideOpaqueBlockHookTypes.remove(string);
            overrideIsEntityInsideOpaqueBlockHookTypes.remove(string);
            afterIsEntityInsideOpaqueBlockHookTypes.remove(string);
            beforeIsInWaterHookTypes.remove(string);
            overrideIsInWaterHookTypes.remove(string);
            afterIsInWaterHookTypes.remove(string);
            beforeIsInsideOfMaterialHookTypes.remove(string);
            overrideIsInsideOfMaterialHookTypes.remove(string);
            afterIsInsideOfMaterialHookTypes.remove(string);
            beforeIsOnLadderHookTypes.remove(string);
            overrideIsOnLadderHookTypes.remove(string);
            afterIsOnLadderHookTypes.remove(string);
            beforeIsSneakingHookTypes.remove(string);
            overrideIsSneakingHookTypes.remove(string);
            afterIsSneakingHookTypes.remove(string);
            beforeIsSprintingHookTypes.remove(string);
            overrideIsSprintingHookTypes.remove(string);
            afterIsSprintingHookTypes.remove(string);
            beforeJumpHookTypes.remove(string);
            overrideJumpHookTypes.remove(string);
            afterJumpHookTypes.remove(string);
            beforeKnockBackHookTypes.remove(string);
            overrideKnockBackHookTypes.remove(string);
            afterKnockBackHookTypes.remove(string);
            beforeMoveEntityHookTypes.remove(string);
            overrideMoveEntityHookTypes.remove(string);
            afterMoveEntityHookTypes.remove(string);
            beforeMoveEntityWithHeadingHookTypes.remove(string);
            overrideMoveEntityWithHeadingHookTypes.remove(string);
            afterMoveEntityWithHeadingHookTypes.remove(string);
            beforeMoveFlyingHookTypes.remove(string);
            overrideMoveFlyingHookTypes.remove(string);
            afterMoveFlyingHookTypes.remove(string);
            beforeOnDeathHookTypes.remove(string);
            overrideOnDeathHookTypes.remove(string);
            afterOnDeathHookTypes.remove(string);
            beforeOnLivingUpdateHookTypes.remove(string);
            overrideOnLivingUpdateHookTypes.remove(string);
            afterOnLivingUpdateHookTypes.remove(string);
            beforeOnKillEntityHookTypes.remove(string);
            overrideOnKillEntityHookTypes.remove(string);
            afterOnKillEntityHookTypes.remove(string);
            beforeOnUpdateHookTypes.remove(string);
            overrideOnUpdateHookTypes.remove(string);
            afterOnUpdateHookTypes.remove(string);
            beforePlayStepSoundHookTypes.remove(string);
            overridePlayStepSoundHookTypes.remove(string);
            afterPlayStepSoundHookTypes.remove(string);
            beforePushOutOfBlocksHookTypes.remove(string);
            overridePushOutOfBlocksHookTypes.remove(string);
            afterPushOutOfBlocksHookTypes.remove(string);
            beforeRayTraceHookTypes.remove(string);
            overrideRayTraceHookTypes.remove(string);
            afterRayTraceHookTypes.remove(string);
            beforeReadEntityFromNBTHookTypes.remove(string);
            overrideReadEntityFromNBTHookTypes.remove(string);
            afterReadEntityFromNBTHookTypes.remove(string);
            beforeRespawnPlayerHookTypes.remove(string);
            overrideRespawnPlayerHookTypes.remove(string);
            afterRespawnPlayerHookTypes.remove(string);
            beforeSetDeadHookTypes.remove(string);
            overrideSetDeadHookTypes.remove(string);
            afterSetDeadHookTypes.remove(string);
            beforeSetPositionAndRotationHookTypes.remove(string);
            overrideSetPositionAndRotationHookTypes.remove(string);
            afterSetPositionAndRotationHookTypes.remove(string);
            beforeSleepInBedAtHookTypes.remove(string);
            overrideSleepInBedAtHookTypes.remove(string);
            afterSleepInBedAtHookTypes.remove(string);
            beforeSwingItemHookTypes.remove(string);
            overrideSwingItemHookTypes.remove(string);
            afterSwingItemHookTypes.remove(string);
            beforeUpdateEntityActionStateHookTypes.remove(string);
            overrideUpdateEntityActionStateHookTypes.remove(string);
            afterUpdateEntityActionStateHookTypes.remove(string);
            beforeWriteEntityToNBTHookTypes.remove(string);
            overrideWriteEntityToNBTHookTypes.remove(string);
            afterWriteEntityToNBTHookTypes.remove(string);
            log("PlayerAPI: unregistered id '" + string + "'");
            return true;
        } else {
            return false;
        }
    }

    public static Set<String> getRegisteredIds() {
        return unmodifiableAllIds;
    }

    private static void addSorting(String string, Map<String, String[]> map, String[] strings) {
        if (strings != null && strings.length > 0) {
            map.put(string, strings);
        }

    }

    private static boolean addMethod(String string, Class<?> class_, List<String> list, String string2, Class<?>... classs) {
        try {
            Method var5 = class_.getMethod(string2, classs);
            boolean var6 = var5.getDeclaringClass() != PlayerBase.class;
            if (var6) {
                list.add(string);
            }

            return var6;
        } catch (Exception var7) {
            throw new RuntimeException("Can not reflect method '" + string2 + "' of class '" + class_.getName() + "'", var7);
        }
    }

    public static PlayerAPI create(ClientPlayerEntity arg) {
        if (allBaseConstructors.size() > 0) {
            if (!initialized) {
                initialize();
            }

            return new PlayerAPI(arg);
        } else {
            return null;
        }
    }

    private static void initialize() {
        sortBases(beforeLocalConstructingHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeLocalConstructing");
        sortBases(afterLocalConstructingHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterLocalConstructing");
        sortBases(beforeAddExhaustionHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeAddExhaustion");
        sortBases(overrideAddExhaustionHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideAddExhaustion");
        sortBases(afterAddExhaustionHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterAddExhaustion");
        sortBases(beforeAddMovementStatHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeAddMovementStat");
        sortBases(overrideAddMovementStatHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideAddMovementStat");
        sortBases(afterAddMovementStatHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterAddMovementStat");
        sortBases(beforeAddStatHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeAddStat");
        sortBases(overrideAddStatHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideAddStat");
        sortBases(afterAddStatHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterAddStat");
        sortBases(beforeAttackEntityFromHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeAttackEntityFrom");
        sortBases(overrideAttackEntityFromHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideAttackEntityFrom");
        sortBases(afterAttackEntityFromHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterAttackEntityFrom");
        sortBases(beforeAlertWolvesHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeAlertWolves");
        sortBases(overrideAlertWolvesHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideAlertWolves");
        sortBases(afterAlertWolvesHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterAlertWolves");
        sortBases(beforeAttackTargetEntityWithCurrentItemHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeAttackTargetEntityWithCurrentItem");
        sortBases(overrideAttackTargetEntityWithCurrentItemHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideAttackTargetEntityWithCurrentItem");
        sortBases(afterAttackTargetEntityWithCurrentItemHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterAttackTargetEntityWithCurrentItem");
        sortBases(beforeCanBreatheUnderwaterHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeCanBreatheUnderwater");
        sortBases(overrideCanBreatheUnderwaterHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideCanBreatheUnderwater");
        sortBases(afterCanBreatheUnderwaterHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterCanBreatheUnderwater");
        sortBases(beforeCanHarvestBlockHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeCanHarvestBlock");
        sortBases(overrideCanHarvestBlockHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideCanHarvestBlock");
        sortBases(afterCanHarvestBlockHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterCanHarvestBlock");
        sortBases(beforeCanPlayerEditHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeCanPlayerEdit");
        sortBases(overrideCanPlayerEditHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideCanPlayerEdit");
        sortBases(afterCanPlayerEditHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterCanPlayerEdit");
        sortBases(beforeCanTriggerWalkingHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeCanTriggerWalking");
        sortBases(overrideCanTriggerWalkingHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideCanTriggerWalking");
        sortBases(afterCanTriggerWalkingHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterCanTriggerWalking");
        sortBases(beforeCloseScreenHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeCloseScreen");
        sortBases(overrideCloseScreenHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideCloseScreen");
        sortBases(afterCloseScreenHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterCloseScreen");
        sortBases(beforeDamageEntityHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeDamageEntity");
        sortBases(overrideDamageEntityHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideDamageEntity");
        sortBases(afterDamageEntityHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterDamageEntity");
        sortBases(beforeDisplayGUIBrewingStandHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeDisplayGUIBrewingStand");
        sortBases(overrideDisplayGUIBrewingStandHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideDisplayGUIBrewingStand");
        sortBases(afterDisplayGUIBrewingStandHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterDisplayGUIBrewingStand");
        sortBases(beforeDisplayGUIChestHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeDisplayGUIChest");
        sortBases(overrideDisplayGUIChestHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideDisplayGUIChest");
        sortBases(afterDisplayGUIChestHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterDisplayGUIChest");
        sortBases(beforeDisplayGUIDispenserHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeDisplayGUIDispenser");
        sortBases(overrideDisplayGUIDispenserHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideDisplayGUIDispenser");
        sortBases(afterDisplayGUIDispenserHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterDisplayGUIDispenser");
        sortBases(beforeDisplayGUIEditSignHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeDisplayGUIEditSign");
        sortBases(overrideDisplayGUIEditSignHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideDisplayGUIEditSign");
        sortBases(afterDisplayGUIEditSignHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterDisplayGUIEditSign");
        sortBases(beforeDisplayGUIEnchantmentHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeDisplayGUIEnchantment");
        sortBases(overrideDisplayGUIEnchantmentHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideDisplayGUIEnchantment");
        sortBases(afterDisplayGUIEnchantmentHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterDisplayGUIEnchantment");
        sortBases(beforeDisplayGUIFurnaceHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeDisplayGUIFurnace");
        sortBases(overrideDisplayGUIFurnaceHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideDisplayGUIFurnace");
        sortBases(afterDisplayGUIFurnaceHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterDisplayGUIFurnace");
        sortBases(beforeDisplayWorkbenchGUIHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeDisplayWorkbenchGUI");
        sortBases(overrideDisplayWorkbenchGUIHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideDisplayWorkbenchGUI");
        sortBases(afterDisplayWorkbenchGUIHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterDisplayWorkbenchGUI");
        sortBases(beforeDropOneItemHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeDropOneItem");
        sortBases(overrideDropOneItemHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideDropOneItem");
        sortBases(afterDropOneItemHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterDropOneItem");
        sortBases(beforeDropPlayerItemHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeDropPlayerItem");
        sortBases(overrideDropPlayerItemHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideDropPlayerItem");
        sortBases(afterDropPlayerItemHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterDropPlayerItem");
        sortBases(beforeDropPlayerItemWithRandomChoiceHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeDropPlayerItemWithRandomChoice");
        sortBases(overrideDropPlayerItemWithRandomChoiceHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideDropPlayerItemWithRandomChoice");
        sortBases(afterDropPlayerItemWithRandomChoiceHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterDropPlayerItemWithRandomChoice");
        sortBases(beforeFallHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeFall");
        sortBases(overrideFallHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideFall");
        sortBases(afterFallHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterFall");
        sortBases(beforeGetBrightnessHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeGetBrightness");
        sortBases(overrideGetBrightnessHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideGetBrightness");
        sortBases(afterGetBrightnessHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterGetBrightness");
        sortBases(beforeGetBrightnessForRenderHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeGetBrightnessForRender");
        sortBases(overrideGetBrightnessForRenderHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideGetBrightnessForRender");
        sortBases(afterGetBrightnessForRenderHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterGetBrightnessForRender");
        sortBases(beforeGetCurrentPlayerStrVsBlockHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeGetCurrentPlayerStrVsBlock");
        sortBases(overrideGetCurrentPlayerStrVsBlockHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideGetCurrentPlayerStrVsBlock");
        sortBases(afterGetCurrentPlayerStrVsBlockHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterGetCurrentPlayerStrVsBlock");
        sortBases(beforeGetDistanceSqHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeGetDistanceSq");
        sortBases(overrideGetDistanceSqHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideGetDistanceSq");
        sortBases(afterGetDistanceSqHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterGetDistanceSq");
        sortBases(beforeGetDistanceSqToEntityHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeGetDistanceSqToEntity");
        sortBases(overrideGetDistanceSqToEntityHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideGetDistanceSqToEntity");
        sortBases(afterGetDistanceSqToEntityHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterGetDistanceSqToEntity");
        sortBases(beforeGetFOVMultiplierHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeGetFOVMultiplier");
        sortBases(overrideGetFOVMultiplierHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideGetFOVMultiplier");
        sortBases(afterGetFOVMultiplierHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterGetFOVMultiplier");
        sortBases(beforeGetHurtSoundHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeGetHurtSound");
        sortBases(overrideGetHurtSoundHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideGetHurtSound");
        sortBases(afterGetHurtSoundHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterGetHurtSound");
        sortBases(beforeGetItemIconHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeGetItemIcon");
        sortBases(overrideGetItemIconHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideGetItemIcon");
        sortBases(afterGetItemIconHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterGetItemIcon");
        sortBases(beforeGetSleepTimerHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeGetSleepTimer");
        sortBases(overrideGetSleepTimerHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideGetSleepTimer");
        sortBases(afterGetSleepTimerHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterGetSleepTimer");
        sortBases(beforeGetSpeedModifierHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeGetSpeedModifier");
        sortBases(overrideGetSpeedModifierHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideGetSpeedModifier");
        sortBases(afterGetSpeedModifierHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterGetSpeedModifier");
        sortBases(beforeHandleLavaMovementHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeHandleLavaMovement");
        sortBases(overrideHandleLavaMovementHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideHandleLavaMovement");
        sortBases(afterHandleLavaMovementHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterHandleLavaMovement");
        sortBases(beforeHandleWaterMovementHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeHandleWaterMovement");
        sortBases(overrideHandleWaterMovementHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideHandleWaterMovement");
        sortBases(afterHandleWaterMovementHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterHandleWaterMovement");
        sortBases(beforeHealHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeHeal");
        sortBases(overrideHealHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideHeal");
        sortBases(afterHealHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterHeal");
        sortBases(beforeIsEntityInsideOpaqueBlockHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeIsEntityInsideOpaqueBlock");
        sortBases(overrideIsEntityInsideOpaqueBlockHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideIsEntityInsideOpaqueBlock");
        sortBases(afterIsEntityInsideOpaqueBlockHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterIsEntityInsideOpaqueBlock");
        sortBases(beforeIsInWaterHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeIsInWater");
        sortBases(overrideIsInWaterHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideIsInWater");
        sortBases(afterIsInWaterHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterIsInWater");
        sortBases(beforeIsInsideOfMaterialHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeIsInsideOfMaterial");
        sortBases(overrideIsInsideOfMaterialHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideIsInsideOfMaterial");
        sortBases(afterIsInsideOfMaterialHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterIsInsideOfMaterial");
        sortBases(beforeIsOnLadderHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeIsOnLadder");
        sortBases(overrideIsOnLadderHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideIsOnLadder");
        sortBases(afterIsOnLadderHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterIsOnLadder");
        sortBases(beforeIsSneakingHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeIsSneaking");
        sortBases(overrideIsSneakingHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideIsSneaking");
        sortBases(afterIsSneakingHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterIsSneaking");
        sortBases(beforeIsSprintingHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeIsSprinting");
        sortBases(overrideIsSprintingHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideIsSprinting");
        sortBases(afterIsSprintingHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterIsSprinting");
        sortBases(beforeJumpHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeJump");
        sortBases(overrideJumpHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideJump");
        sortBases(afterJumpHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterJump");
        sortBases(beforeKnockBackHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeKnockBack");
        sortBases(overrideKnockBackHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideKnockBack");
        sortBases(afterKnockBackHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterKnockBack");
        sortBases(beforeMoveEntityHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeMoveEntity");
        sortBases(overrideMoveEntityHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideMoveEntity");
        sortBases(afterMoveEntityHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterMoveEntity");
        sortBases(beforeMoveEntityWithHeadingHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeMoveEntityWithHeading");
        sortBases(overrideMoveEntityWithHeadingHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideMoveEntityWithHeading");
        sortBases(afterMoveEntityWithHeadingHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterMoveEntityWithHeading");
        sortBases(beforeMoveFlyingHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeMoveFlying");
        sortBases(overrideMoveFlyingHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideMoveFlying");
        sortBases(afterMoveFlyingHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterMoveFlying");
        sortBases(beforeOnDeathHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeOnDeath");
        sortBases(overrideOnDeathHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideOnDeath");
        sortBases(afterOnDeathHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterOnDeath");
        sortBases(beforeOnLivingUpdateHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeOnLivingUpdate");
        sortBases(overrideOnLivingUpdateHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideOnLivingUpdate");
        sortBases(afterOnLivingUpdateHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterOnLivingUpdate");
        sortBases(beforeOnKillEntityHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeOnKillEntity");
        sortBases(overrideOnKillEntityHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideOnKillEntity");
        sortBases(afterOnKillEntityHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterOnKillEntity");
        sortBases(beforeOnUpdateHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeOnUpdate");
        sortBases(overrideOnUpdateHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideOnUpdate");
        sortBases(afterOnUpdateHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterOnUpdate");
        sortBases(beforePlayStepSoundHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforePlayStepSound");
        sortBases(overridePlayStepSoundHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overridePlayStepSound");
        sortBases(afterPlayStepSoundHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterPlayStepSound");
        sortBases(beforePushOutOfBlocksHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforePushOutOfBlocks");
        sortBases(overridePushOutOfBlocksHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overridePushOutOfBlocks");
        sortBases(afterPushOutOfBlocksHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterPushOutOfBlocks");
        sortBases(beforeRayTraceHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeRayTrace");
        sortBases(overrideRayTraceHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideRayTrace");
        sortBases(afterRayTraceHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterRayTrace");
        sortBases(beforeReadEntityFromNBTHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeReadEntityFromNBT");
        sortBases(overrideReadEntityFromNBTHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideReadEntityFromNBT");
        sortBases(afterReadEntityFromNBTHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterReadEntityFromNBT");
        sortBases(beforeRespawnPlayerHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeRespawnPlayer");
        sortBases(overrideRespawnPlayerHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideRespawnPlayer");
        sortBases(afterRespawnPlayerHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterRespawnPlayer");
        sortBases(beforeSetDeadHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeSetDead");
        sortBases(overrideSetDeadHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideSetDead");
        sortBases(afterSetDeadHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterSetDead");
        sortBases(beforeSetPositionAndRotationHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeSetPositionAndRotation");
        sortBases(overrideSetPositionAndRotationHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideSetPositionAndRotation");
        sortBases(afterSetPositionAndRotationHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterSetPositionAndRotation");
        sortBases(beforeSleepInBedAtHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeSleepInBedAt");
        sortBases(overrideSleepInBedAtHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideSleepInBedAt");
        sortBases(afterSleepInBedAtHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterSleepInBedAt");
        sortBases(beforeSwingItemHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeSwingItem");
        sortBases(overrideSwingItemHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideSwingItem");
        sortBases(afterSwingItemHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterSwingItem");
        sortBases(beforeUpdateEntityActionStateHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeUpdateEntityActionState");
        sortBases(overrideUpdateEntityActionStateHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideUpdateEntityActionState");
        sortBases(afterUpdateEntityActionStateHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterUpdateEntityActionState");
        sortBases(beforeWriteEntityToNBTHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeWriteEntityToNBT");
        sortBases(overrideWriteEntityToNBTHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideWriteEntityToNBT");
        sortBases(afterWriteEntityToNBTHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterWriteEntityToNBT");
        initialized = true;
    }

    public static void beforeLocalConstructing(ClientPlayerEntity arg, Minecraft minecraft, World arg2, Session arg3, int i) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().beforeLocalConstructing(minecraft, arg2, arg3, i);
        }

    }

    public static void afterLocalConstructing(ClientPlayerEntity arg, Minecraft minecraft, World arg2, Session arg3, int i) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().afterLocalConstructing(minecraft, arg2, arg3, i);
        }

    }

    private static void sortBases(List<String> list, Map<String, String[]> map, Map<String, String[]> map2, String string) {
        (new PlayerBaseSorter(list, map, map2, string)).Sort();
    }

    private PlayerAPI(ClientPlayerEntity arg) {
        this.unmodifiableAllBaseIds = Collections.unmodifiableSet(this.allBaseObjects.keySet());
        this.player = arg;
        Object[] var2 = new Object[]{this};
        Object[] var3 = new Object[]{this, null};

        Map.Entry<String, Constructor> var5;
        PlayerBase var7;
        for(Iterator var4 = allBaseConstructors.entrySet().iterator(); var4.hasNext(); this.allBaseObjects.put(var5.getKey(), var7)) {
            var5 = (Map.Entry)var4.next();
            Constructor var6 = (Constructor)var5.getValue();
            var3[1] = var5.getKey();

            try {
                if (var6.getParameterTypes().length == 1) {
                    var7 = (PlayerBase)var6.newInstance(var2);
                } else {
                    var7 = (PlayerBase)var6.newInstance(var3);
                }
            } catch (Exception var9) {
                throw new RuntimeException("Exception while creating a PlayerBase of type '" + var6.getDeclaringClass() + "'", var9);
            }
        }

        this.beforeLocalConstructingHooks = this.create(beforeLocalConstructingHookTypes);
        this.afterLocalConstructingHooks = this.create(afterLocalConstructingHookTypes);
        this.beforeAddExhaustionHooks = this.create(beforeAddExhaustionHookTypes);
        this.overrideAddExhaustionHooks = this.create(overrideAddExhaustionHookTypes);
        this.afterAddExhaustionHooks = this.create(afterAddExhaustionHookTypes);
        this.isAddExhaustionModded = this.beforeAddExhaustionHooks != null || this.overrideAddExhaustionHooks != null || this.afterAddExhaustionHooks != null;
        this.beforeAddMovementStatHooks = this.create(beforeAddMovementStatHookTypes);
        this.overrideAddMovementStatHooks = this.create(overrideAddMovementStatHookTypes);
        this.afterAddMovementStatHooks = this.create(afterAddMovementStatHookTypes);
        this.isAddMovementStatModded = this.beforeAddMovementStatHooks != null || this.overrideAddMovementStatHooks != null || this.afterAddMovementStatHooks != null;
        this.beforeAddStatHooks = this.create(beforeAddStatHookTypes);
        this.overrideAddStatHooks = this.create(overrideAddStatHookTypes);
        this.afterAddStatHooks = this.create(afterAddStatHookTypes);
        this.isAddStatModded = this.beforeAddStatHooks != null || this.overrideAddStatHooks != null || this.afterAddStatHooks != null;
        this.beforeAttackEntityFromHooks = this.create(beforeAttackEntityFromHookTypes);
        this.overrideAttackEntityFromHooks = this.create(overrideAttackEntityFromHookTypes);
        this.afterAttackEntityFromHooks = this.create(afterAttackEntityFromHookTypes);
        this.isAttackEntityFromModded = this.beforeAttackEntityFromHooks != null || this.overrideAttackEntityFromHooks != null || this.afterAttackEntityFromHooks != null;
        this.beforeAlertWolvesHooks = this.create(beforeAlertWolvesHookTypes);
        this.overrideAlertWolvesHooks = this.create(overrideAlertWolvesHookTypes);
        this.afterAlertWolvesHooks = this.create(afterAlertWolvesHookTypes);
        this.isAlertWolvesModded = this.beforeAlertWolvesHooks != null || this.overrideAlertWolvesHooks != null || this.afterAlertWolvesHooks != null;
        this.beforeAttackTargetEntityWithCurrentItemHooks = this.create(beforeAttackTargetEntityWithCurrentItemHookTypes);
        this.overrideAttackTargetEntityWithCurrentItemHooks = this.create(overrideAttackTargetEntityWithCurrentItemHookTypes);
        this.afterAttackTargetEntityWithCurrentItemHooks = this.create(afterAttackTargetEntityWithCurrentItemHookTypes);
        this.isAttackTargetEntityWithCurrentItemModded = this.beforeAttackTargetEntityWithCurrentItemHooks != null || this.overrideAttackTargetEntityWithCurrentItemHooks != null || this.afterAttackTargetEntityWithCurrentItemHooks != null;
        this.beforeCanBreatheUnderwaterHooks = this.create(beforeCanBreatheUnderwaterHookTypes);
        this.overrideCanBreatheUnderwaterHooks = this.create(overrideCanBreatheUnderwaterHookTypes);
        this.afterCanBreatheUnderwaterHooks = this.create(afterCanBreatheUnderwaterHookTypes);
        this.isCanBreatheUnderwaterModded = this.beforeCanBreatheUnderwaterHooks != null || this.overrideCanBreatheUnderwaterHooks != null || this.afterCanBreatheUnderwaterHooks != null;
        this.beforeCanHarvestBlockHooks = this.create(beforeCanHarvestBlockHookTypes);
        this.overrideCanHarvestBlockHooks = this.create(overrideCanHarvestBlockHookTypes);
        this.afterCanHarvestBlockHooks = this.create(afterCanHarvestBlockHookTypes);
        this.isCanHarvestBlockModded = this.beforeCanHarvestBlockHooks != null || this.overrideCanHarvestBlockHooks != null || this.afterCanHarvestBlockHooks != null;
        this.beforeCanPlayerEditHooks = this.create(beforeCanPlayerEditHookTypes);
        this.overrideCanPlayerEditHooks = this.create(overrideCanPlayerEditHookTypes);
        this.afterCanPlayerEditHooks = this.create(afterCanPlayerEditHookTypes);
        this.isCanPlayerEditModded = this.beforeCanPlayerEditHooks != null || this.overrideCanPlayerEditHooks != null || this.afterCanPlayerEditHooks != null;
        this.beforeCanTriggerWalkingHooks = this.create(beforeCanTriggerWalkingHookTypes);
        this.overrideCanTriggerWalkingHooks = this.create(overrideCanTriggerWalkingHookTypes);
        this.afterCanTriggerWalkingHooks = this.create(afterCanTriggerWalkingHookTypes);
        this.isCanTriggerWalkingModded = this.beforeCanTriggerWalkingHooks != null || this.overrideCanTriggerWalkingHooks != null || this.afterCanTriggerWalkingHooks != null;
        this.beforeCloseScreenHooks = this.create(beforeCloseScreenHookTypes);
        this.overrideCloseScreenHooks = this.create(overrideCloseScreenHookTypes);
        this.afterCloseScreenHooks = this.create(afterCloseScreenHookTypes);
        this.isCloseScreenModded = this.beforeCloseScreenHooks != null || this.overrideCloseScreenHooks != null || this.afterCloseScreenHooks != null;
        this.beforeDamageEntityHooks = this.create(beforeDamageEntityHookTypes);
        this.overrideDamageEntityHooks = this.create(overrideDamageEntityHookTypes);
        this.afterDamageEntityHooks = this.create(afterDamageEntityHookTypes);
        this.isDamageEntityModded = this.beforeDamageEntityHooks != null || this.overrideDamageEntityHooks != null || this.afterDamageEntityHooks != null;
        this.beforeDisplayGUIBrewingStandHooks = this.create(beforeDisplayGUIBrewingStandHookTypes);
        this.overrideDisplayGUIBrewingStandHooks = this.create(overrideDisplayGUIBrewingStandHookTypes);
        this.afterDisplayGUIBrewingStandHooks = this.create(afterDisplayGUIBrewingStandHookTypes);
        this.isDisplayGUIBrewingStandModded = this.beforeDisplayGUIBrewingStandHooks != null || this.overrideDisplayGUIBrewingStandHooks != null || this.afterDisplayGUIBrewingStandHooks != null;
        this.beforeDisplayGUIChestHooks = this.create(beforeDisplayGUIChestHookTypes);
        this.overrideDisplayGUIChestHooks = this.create(overrideDisplayGUIChestHookTypes);
        this.afterDisplayGUIChestHooks = this.create(afterDisplayGUIChestHookTypes);
        this.isDisplayGUIChestModded = this.beforeDisplayGUIChestHooks != null || this.overrideDisplayGUIChestHooks != null || this.afterDisplayGUIChestHooks != null;
        this.beforeDisplayGUIDispenserHooks = this.create(beforeDisplayGUIDispenserHookTypes);
        this.overrideDisplayGUIDispenserHooks = this.create(overrideDisplayGUIDispenserHookTypes);
        this.afterDisplayGUIDispenserHooks = this.create(afterDisplayGUIDispenserHookTypes);
        this.isDisplayGUIDispenserModded = this.beforeDisplayGUIDispenserHooks != null || this.overrideDisplayGUIDispenserHooks != null || this.afterDisplayGUIDispenserHooks != null;
        this.beforeDisplayGUIEditSignHooks = this.create(beforeDisplayGUIEditSignHookTypes);
        this.overrideDisplayGUIEditSignHooks = this.create(overrideDisplayGUIEditSignHookTypes);
        this.afterDisplayGUIEditSignHooks = this.create(afterDisplayGUIEditSignHookTypes);
        this.isDisplayGUIEditSignModded = this.beforeDisplayGUIEditSignHooks != null || this.overrideDisplayGUIEditSignHooks != null || this.afterDisplayGUIEditSignHooks != null;
        this.beforeDisplayGUIEnchantmentHooks = this.create(beforeDisplayGUIEnchantmentHookTypes);
        this.overrideDisplayGUIEnchantmentHooks = this.create(overrideDisplayGUIEnchantmentHookTypes);
        this.afterDisplayGUIEnchantmentHooks = this.create(afterDisplayGUIEnchantmentHookTypes);
        this.isDisplayGUIEnchantmentModded = this.beforeDisplayGUIEnchantmentHooks != null || this.overrideDisplayGUIEnchantmentHooks != null || this.afterDisplayGUIEnchantmentHooks != null;
        this.beforeDisplayGUIFurnaceHooks = this.create(beforeDisplayGUIFurnaceHookTypes);
        this.overrideDisplayGUIFurnaceHooks = this.create(overrideDisplayGUIFurnaceHookTypes);
        this.afterDisplayGUIFurnaceHooks = this.create(afterDisplayGUIFurnaceHookTypes);
        this.isDisplayGUIFurnaceModded = this.beforeDisplayGUIFurnaceHooks != null || this.overrideDisplayGUIFurnaceHooks != null || this.afterDisplayGUIFurnaceHooks != null;
        this.beforeDisplayWorkbenchGUIHooks = this.create(beforeDisplayWorkbenchGUIHookTypes);
        this.overrideDisplayWorkbenchGUIHooks = this.create(overrideDisplayWorkbenchGUIHookTypes);
        this.afterDisplayWorkbenchGUIHooks = this.create(afterDisplayWorkbenchGUIHookTypes);
        this.isDisplayWorkbenchGUIModded = this.beforeDisplayWorkbenchGUIHooks != null || this.overrideDisplayWorkbenchGUIHooks != null || this.afterDisplayWorkbenchGUIHooks != null;
        this.beforeDropOneItemHooks = this.create(beforeDropOneItemHookTypes);
        this.overrideDropOneItemHooks = this.create(overrideDropOneItemHookTypes);
        this.afterDropOneItemHooks = this.create(afterDropOneItemHookTypes);
        this.isDropOneItemModded = this.beforeDropOneItemHooks != null || this.overrideDropOneItemHooks != null || this.afterDropOneItemHooks != null;
        this.beforeDropPlayerItemHooks = this.create(beforeDropPlayerItemHookTypes);
        this.overrideDropPlayerItemHooks = this.create(overrideDropPlayerItemHookTypes);
        this.afterDropPlayerItemHooks = this.create(afterDropPlayerItemHookTypes);
        this.isDropPlayerItemModded = this.beforeDropPlayerItemHooks != null || this.overrideDropPlayerItemHooks != null || this.afterDropPlayerItemHooks != null;
        this.beforeDropPlayerItemWithRandomChoiceHooks = this.create(beforeDropPlayerItemWithRandomChoiceHookTypes);
        this.overrideDropPlayerItemWithRandomChoiceHooks = this.create(overrideDropPlayerItemWithRandomChoiceHookTypes);
        this.afterDropPlayerItemWithRandomChoiceHooks = this.create(afterDropPlayerItemWithRandomChoiceHookTypes);
        this.isDropPlayerItemWithRandomChoiceModded = this.beforeDropPlayerItemWithRandomChoiceHooks != null || this.overrideDropPlayerItemWithRandomChoiceHooks != null || this.afterDropPlayerItemWithRandomChoiceHooks != null;
        this.beforeFallHooks = this.create(beforeFallHookTypes);
        this.overrideFallHooks = this.create(overrideFallHookTypes);
        this.afterFallHooks = this.create(afterFallHookTypes);
        this.isFallModded = this.beforeFallHooks != null || this.overrideFallHooks != null || this.afterFallHooks != null;
        this.beforeGetBrightnessHooks = this.create(beforeGetBrightnessHookTypes);
        this.overrideGetBrightnessHooks = this.create(overrideGetBrightnessHookTypes);
        this.afterGetBrightnessHooks = this.create(afterGetBrightnessHookTypes);
        this.isGetBrightnessModded = this.beforeGetBrightnessHooks != null || this.overrideGetBrightnessHooks != null || this.afterGetBrightnessHooks != null;
        this.beforeGetBrightnessForRenderHooks = this.create(beforeGetBrightnessForRenderHookTypes);
        this.overrideGetBrightnessForRenderHooks = this.create(overrideGetBrightnessForRenderHookTypes);
        this.afterGetBrightnessForRenderHooks = this.create(afterGetBrightnessForRenderHookTypes);
        this.isGetBrightnessForRenderModded = this.beforeGetBrightnessForRenderHooks != null || this.overrideGetBrightnessForRenderHooks != null || this.afterGetBrightnessForRenderHooks != null;
        this.beforeGetCurrentPlayerStrVsBlockHooks = this.create(beforeGetCurrentPlayerStrVsBlockHookTypes);
        this.overrideGetCurrentPlayerStrVsBlockHooks = this.create(overrideGetCurrentPlayerStrVsBlockHookTypes);
        this.afterGetCurrentPlayerStrVsBlockHooks = this.create(afterGetCurrentPlayerStrVsBlockHookTypes);
        this.isGetCurrentPlayerStrVsBlockModded = this.beforeGetCurrentPlayerStrVsBlockHooks != null || this.overrideGetCurrentPlayerStrVsBlockHooks != null || this.afterGetCurrentPlayerStrVsBlockHooks != null;
        this.beforeGetDistanceSqHooks = this.create(beforeGetDistanceSqHookTypes);
        this.overrideGetDistanceSqHooks = this.create(overrideGetDistanceSqHookTypes);
        this.afterGetDistanceSqHooks = this.create(afterGetDistanceSqHookTypes);
        this.isGetDistanceSqModded = this.beforeGetDistanceSqHooks != null || this.overrideGetDistanceSqHooks != null || this.afterGetDistanceSqHooks != null;
        this.beforeGetDistanceSqToEntityHooks = this.create(beforeGetDistanceSqToEntityHookTypes);
        this.overrideGetDistanceSqToEntityHooks = this.create(overrideGetDistanceSqToEntityHookTypes);
        this.afterGetDistanceSqToEntityHooks = this.create(afterGetDistanceSqToEntityHookTypes);
        this.isGetDistanceSqToEntityModded = this.beforeGetDistanceSqToEntityHooks != null || this.overrideGetDistanceSqToEntityHooks != null || this.afterGetDistanceSqToEntityHooks != null;
        this.beforeGetFOVMultiplierHooks = this.create(beforeGetFOVMultiplierHookTypes);
        this.overrideGetFOVMultiplierHooks = this.create(overrideGetFOVMultiplierHookTypes);
        this.afterGetFOVMultiplierHooks = this.create(afterGetFOVMultiplierHookTypes);
        this.isGetFOVMultiplierModded = this.beforeGetFOVMultiplierHooks != null || this.overrideGetFOVMultiplierHooks != null || this.afterGetFOVMultiplierHooks != null;
        this.beforeGetHurtSoundHooks = this.create(beforeGetHurtSoundHookTypes);
        this.overrideGetHurtSoundHooks = this.create(overrideGetHurtSoundHookTypes);
        this.afterGetHurtSoundHooks = this.create(afterGetHurtSoundHookTypes);
        this.isGetHurtSoundModded = this.beforeGetHurtSoundHooks != null || this.overrideGetHurtSoundHooks != null || this.afterGetHurtSoundHooks != null;
        this.beforeGetItemIconHooks = this.create(beforeGetItemIconHookTypes);
        this.overrideGetItemIconHooks = this.create(overrideGetItemIconHookTypes);
        this.afterGetItemIconHooks = this.create(afterGetItemIconHookTypes);
        this.isGetItemIconModded = this.beforeGetItemIconHooks != null || this.overrideGetItemIconHooks != null || this.afterGetItemIconHooks != null;
        this.beforeGetSleepTimerHooks = this.create(beforeGetSleepTimerHookTypes);
        this.overrideGetSleepTimerHooks = this.create(overrideGetSleepTimerHookTypes);
        this.afterGetSleepTimerHooks = this.create(afterGetSleepTimerHookTypes);
        this.isGetSleepTimerModded = this.beforeGetSleepTimerHooks != null || this.overrideGetSleepTimerHooks != null || this.afterGetSleepTimerHooks != null;
        this.beforeGetSpeedModifierHooks = this.create(beforeGetSpeedModifierHookTypes);
        this.overrideGetSpeedModifierHooks = this.create(overrideGetSpeedModifierHookTypes);
        this.afterGetSpeedModifierHooks = this.create(afterGetSpeedModifierHookTypes);
        this.isGetSpeedModifierModded = this.beforeGetSpeedModifierHooks != null || this.overrideGetSpeedModifierHooks != null || this.afterGetSpeedModifierHooks != null;
        this.beforeHandleLavaMovementHooks = this.create(beforeHandleLavaMovementHookTypes);
        this.overrideHandleLavaMovementHooks = this.create(overrideHandleLavaMovementHookTypes);
        this.afterHandleLavaMovementHooks = this.create(afterHandleLavaMovementHookTypes);
        this.isHandleLavaMovementModded = this.beforeHandleLavaMovementHooks != null || this.overrideHandleLavaMovementHooks != null || this.afterHandleLavaMovementHooks != null;
        this.beforeHandleWaterMovementHooks = this.create(beforeHandleWaterMovementHookTypes);
        this.overrideHandleWaterMovementHooks = this.create(overrideHandleWaterMovementHookTypes);
        this.afterHandleWaterMovementHooks = this.create(afterHandleWaterMovementHookTypes);
        this.isHandleWaterMovementModded = this.beforeHandleWaterMovementHooks != null || this.overrideHandleWaterMovementHooks != null || this.afterHandleWaterMovementHooks != null;
        this.beforeHealHooks = this.create(beforeHealHookTypes);
        this.overrideHealHooks = this.create(overrideHealHookTypes);
        this.afterHealHooks = this.create(afterHealHookTypes);
        this.isHealModded = this.beforeHealHooks != null || this.overrideHealHooks != null || this.afterHealHooks != null;
        this.beforeIsEntityInsideOpaqueBlockHooks = this.create(beforeIsEntityInsideOpaqueBlockHookTypes);
        this.overrideIsEntityInsideOpaqueBlockHooks = this.create(overrideIsEntityInsideOpaqueBlockHookTypes);
        this.afterIsEntityInsideOpaqueBlockHooks = this.create(afterIsEntityInsideOpaqueBlockHookTypes);
        this.isIsEntityInsideOpaqueBlockModded = this.beforeIsEntityInsideOpaqueBlockHooks != null || this.overrideIsEntityInsideOpaqueBlockHooks != null || this.afterIsEntityInsideOpaqueBlockHooks != null;
        this.beforeIsInWaterHooks = this.create(beforeIsInWaterHookTypes);
        this.overrideIsInWaterHooks = this.create(overrideIsInWaterHookTypes);
        this.afterIsInWaterHooks = this.create(afterIsInWaterHookTypes);
        this.isIsInWaterModded = this.beforeIsInWaterHooks != null || this.overrideIsInWaterHooks != null || this.afterIsInWaterHooks != null;
        this.beforeIsInsideOfMaterialHooks = this.create(beforeIsInsideOfMaterialHookTypes);
        this.overrideIsInsideOfMaterialHooks = this.create(overrideIsInsideOfMaterialHookTypes);
        this.afterIsInsideOfMaterialHooks = this.create(afterIsInsideOfMaterialHookTypes);
        this.isIsInsideOfMaterialModded = this.beforeIsInsideOfMaterialHooks != null || this.overrideIsInsideOfMaterialHooks != null || this.afterIsInsideOfMaterialHooks != null;
        this.beforeIsOnLadderHooks = this.create(beforeIsOnLadderHookTypes);
        this.overrideIsOnLadderHooks = this.create(overrideIsOnLadderHookTypes);
        this.afterIsOnLadderHooks = this.create(afterIsOnLadderHookTypes);
        this.isIsOnLadderModded = this.beforeIsOnLadderHooks != null || this.overrideIsOnLadderHooks != null || this.afterIsOnLadderHooks != null;
        this.beforeIsSneakingHooks = this.create(beforeIsSneakingHookTypes);
        this.overrideIsSneakingHooks = this.create(overrideIsSneakingHookTypes);
        this.afterIsSneakingHooks = this.create(afterIsSneakingHookTypes);
        this.isIsSneakingModded = this.beforeIsSneakingHooks != null || this.overrideIsSneakingHooks != null || this.afterIsSneakingHooks != null;
        this.beforeIsSprintingHooks = this.create(beforeIsSprintingHookTypes);
        this.overrideIsSprintingHooks = this.create(overrideIsSprintingHookTypes);
        this.afterIsSprintingHooks = this.create(afterIsSprintingHookTypes);
        this.isIsSprintingModded = this.beforeIsSprintingHooks != null || this.overrideIsSprintingHooks != null || this.afterIsSprintingHooks != null;
        this.beforeJumpHooks = this.create(beforeJumpHookTypes);
        this.overrideJumpHooks = this.create(overrideJumpHookTypes);
        this.afterJumpHooks = this.create(afterJumpHookTypes);
        this.isJumpModded = this.beforeJumpHooks != null || this.overrideJumpHooks != null || this.afterJumpHooks != null;
        this.beforeKnockBackHooks = this.create(beforeKnockBackHookTypes);
        this.overrideKnockBackHooks = this.create(overrideKnockBackHookTypes);
        this.afterKnockBackHooks = this.create(afterKnockBackHookTypes);
        this.isKnockBackModded = this.beforeKnockBackHooks != null || this.overrideKnockBackHooks != null || this.afterKnockBackHooks != null;
        this.beforeMoveEntityHooks = this.create(beforeMoveEntityHookTypes);
        this.overrideMoveEntityHooks = this.create(overrideMoveEntityHookTypes);
        this.afterMoveEntityHooks = this.create(afterMoveEntityHookTypes);
        this.isMoveEntityModded = this.beforeMoveEntityHooks != null || this.overrideMoveEntityHooks != null || this.afterMoveEntityHooks != null;
        this.beforeMoveEntityWithHeadingHooks = this.create(beforeMoveEntityWithHeadingHookTypes);
        this.overrideMoveEntityWithHeadingHooks = this.create(overrideMoveEntityWithHeadingHookTypes);
        this.afterMoveEntityWithHeadingHooks = this.create(afterMoveEntityWithHeadingHookTypes);
        this.isMoveEntityWithHeadingModded = this.beforeMoveEntityWithHeadingHooks != null || this.overrideMoveEntityWithHeadingHooks != null || this.afterMoveEntityWithHeadingHooks != null;
        this.beforeMoveFlyingHooks = this.create(beforeMoveFlyingHookTypes);
        this.overrideMoveFlyingHooks = this.create(overrideMoveFlyingHookTypes);
        this.afterMoveFlyingHooks = this.create(afterMoveFlyingHookTypes);
        this.isMoveFlyingModded = this.beforeMoveFlyingHooks != null || this.overrideMoveFlyingHooks != null || this.afterMoveFlyingHooks != null;
        this.beforeOnDeathHooks = this.create(beforeOnDeathHookTypes);
        this.overrideOnDeathHooks = this.create(overrideOnDeathHookTypes);
        this.afterOnDeathHooks = this.create(afterOnDeathHookTypes);
        this.isOnDeathModded = this.beforeOnDeathHooks != null || this.overrideOnDeathHooks != null || this.afterOnDeathHooks != null;
        this.beforeOnLivingUpdateHooks = this.create(beforeOnLivingUpdateHookTypes);
        this.overrideOnLivingUpdateHooks = this.create(overrideOnLivingUpdateHookTypes);
        this.afterOnLivingUpdateHooks = this.create(afterOnLivingUpdateHookTypes);
        this.isOnLivingUpdateModded = this.beforeOnLivingUpdateHooks != null || this.overrideOnLivingUpdateHooks != null || this.afterOnLivingUpdateHooks != null;
        this.beforeOnKillEntityHooks = this.create(beforeOnKillEntityHookTypes);
        this.overrideOnKillEntityHooks = this.create(overrideOnKillEntityHookTypes);
        this.afterOnKillEntityHooks = this.create(afterOnKillEntityHookTypes);
        this.isOnKillEntityModded = this.beforeOnKillEntityHooks != null || this.overrideOnKillEntityHooks != null || this.afterOnKillEntityHooks != null;
        this.beforeOnUpdateHooks = this.create(beforeOnUpdateHookTypes);
        this.overrideOnUpdateHooks = this.create(overrideOnUpdateHookTypes);
        this.afterOnUpdateHooks = this.create(afterOnUpdateHookTypes);
        this.isOnUpdateModded = this.beforeOnUpdateHooks != null || this.overrideOnUpdateHooks != null || this.afterOnUpdateHooks != null;
        this.beforePlayStepSoundHooks = this.create(beforePlayStepSoundHookTypes);
        this.overridePlayStepSoundHooks = this.create(overridePlayStepSoundHookTypes);
        this.afterPlayStepSoundHooks = this.create(afterPlayStepSoundHookTypes);
        this.isPlayStepSoundModded = this.beforePlayStepSoundHooks != null || this.overridePlayStepSoundHooks != null || this.afterPlayStepSoundHooks != null;
        this.beforePushOutOfBlocksHooks = this.create(beforePushOutOfBlocksHookTypes);
        this.overridePushOutOfBlocksHooks = this.create(overridePushOutOfBlocksHookTypes);
        this.afterPushOutOfBlocksHooks = this.create(afterPushOutOfBlocksHookTypes);
        this.isPushOutOfBlocksModded = this.beforePushOutOfBlocksHooks != null || this.overridePushOutOfBlocksHooks != null || this.afterPushOutOfBlocksHooks != null;
        this.beforeRayTraceHooks = this.create(beforeRayTraceHookTypes);
        this.overrideRayTraceHooks = this.create(overrideRayTraceHookTypes);
        this.afterRayTraceHooks = this.create(afterRayTraceHookTypes);
        this.isRayTraceModded = this.beforeRayTraceHooks != null || this.overrideRayTraceHooks != null || this.afterRayTraceHooks != null;
        this.beforeReadEntityFromNBTHooks = this.create(beforeReadEntityFromNBTHookTypes);
        this.overrideReadEntityFromNBTHooks = this.create(overrideReadEntityFromNBTHookTypes);
        this.afterReadEntityFromNBTHooks = this.create(afterReadEntityFromNBTHookTypes);
        this.isReadEntityFromNBTModded = this.beforeReadEntityFromNBTHooks != null || this.overrideReadEntityFromNBTHooks != null || this.afterReadEntityFromNBTHooks != null;
        this.beforeRespawnPlayerHooks = this.create(beforeRespawnPlayerHookTypes);
        this.overrideRespawnPlayerHooks = this.create(overrideRespawnPlayerHookTypes);
        this.afterRespawnPlayerHooks = this.create(afterRespawnPlayerHookTypes);
        this.isRespawnPlayerModded = this.beforeRespawnPlayerHooks != null || this.overrideRespawnPlayerHooks != null || this.afterRespawnPlayerHooks != null;
        this.beforeSetDeadHooks = this.create(beforeSetDeadHookTypes);
        this.overrideSetDeadHooks = this.create(overrideSetDeadHookTypes);
        this.afterSetDeadHooks = this.create(afterSetDeadHookTypes);
        this.isSetDeadModded = this.beforeSetDeadHooks != null || this.overrideSetDeadHooks != null || this.afterSetDeadHooks != null;
        this.beforeSetPositionAndRotationHooks = this.create(beforeSetPositionAndRotationHookTypes);
        this.overrideSetPositionAndRotationHooks = this.create(overrideSetPositionAndRotationHookTypes);
        this.afterSetPositionAndRotationHooks = this.create(afterSetPositionAndRotationHookTypes);
        this.isSetPositionAndRotationModded = this.beforeSetPositionAndRotationHooks != null || this.overrideSetPositionAndRotationHooks != null || this.afterSetPositionAndRotationHooks != null;
        this.beforeSleepInBedAtHooks = this.create(beforeSleepInBedAtHookTypes);
        this.overrideSleepInBedAtHooks = this.create(overrideSleepInBedAtHookTypes);
        this.afterSleepInBedAtHooks = this.create(afterSleepInBedAtHookTypes);
        this.isSleepInBedAtModded = this.beforeSleepInBedAtHooks != null || this.overrideSleepInBedAtHooks != null || this.afterSleepInBedAtHooks != null;
        this.beforeSwingItemHooks = this.create(beforeSwingItemHookTypes);
        this.overrideSwingItemHooks = this.create(overrideSwingItemHookTypes);
        this.afterSwingItemHooks = this.create(afterSwingItemHookTypes);
        this.isSwingItemModded = this.beforeSwingItemHooks != null || this.overrideSwingItemHooks != null || this.afterSwingItemHooks != null;
        this.beforeUpdateEntityActionStateHooks = this.create(beforeUpdateEntityActionStateHookTypes);
        this.overrideUpdateEntityActionStateHooks = this.create(overrideUpdateEntityActionStateHookTypes);
        this.afterUpdateEntityActionStateHooks = this.create(afterUpdateEntityActionStateHookTypes);
        this.isUpdateEntityActionStateModded = this.beforeUpdateEntityActionStateHooks != null || this.overrideUpdateEntityActionStateHooks != null || this.afterUpdateEntityActionStateHooks != null;
        this.beforeWriteEntityToNBTHooks = this.create(beforeWriteEntityToNBTHookTypes);
        this.overrideWriteEntityToNBTHooks = this.create(overrideWriteEntityToNBTHookTypes);
        this.afterWriteEntityToNBTHooks = this.create(afterWriteEntityToNBTHookTypes);
        this.isWriteEntityToNBTModded = this.beforeWriteEntityToNBTHooks != null || this.overrideWriteEntityToNBTHooks != null || this.afterWriteEntityToNBTHooks != null;
    }

    private PlayerBase[] create(List<String> list) {
        if (list.isEmpty()) {
            return null;
        } else {
            PlayerBase[] var2 = new PlayerBase[list.size()];

            for(int var3 = 0; var3 < var2.length; ++var3) {
                var2[var3] = this.getPlayerBase((String)list.get(var3));
            }

            return var2;
        }
    }

    private void beforeLocalConstructing(Minecraft minecraft, World arg, Session arg2, int i) {
        if (this.beforeLocalConstructingHooks != null) {
            for(int var5 = this.beforeLocalConstructingHooks.length - 1; var5 >= 0; --var5) {
                this.beforeLocalConstructingHooks[var5].beforeLocalConstructing(minecraft, arg, arg2, i);
            }
        }

    }

    private void afterLocalConstructing(Minecraft minecraft, World arg, Session arg2, int i) {
        if (this.afterLocalConstructingHooks != null) {
            for(int var5 = 0; var5 < this.afterLocalConstructingHooks.length; ++var5) {
                this.afterLocalConstructingHooks[var5].afterLocalConstructing(minecraft, arg, arg2, i);
            }
        }

    }

    public PlayerBase getPlayerBase(String string) {
        return (PlayerBase)this.allBaseObjects.get(string);
    }

    public Set<String> getPlayerBaseIds() {
        return this.unmodifiableAllBaseIds;
    }

    public static void addExhaustion(ClientPlayerEntity arg, float f) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().addExhaustion(f);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localAddExhaustion(f);
        }

    }

    protected void addExhaustion(float f) {
        int var2;
        if (this.beforeAddExhaustionHooks != null) {
            for(var2 = this.beforeAddExhaustionHooks.length - 1; var2 >= 0; --var2) {
                this.beforeAddExhaustionHooks[var2].beforeAddExhaustion(f);
            }
        }

        if (this.overrideAddExhaustionHooks != null) {
            this.overrideAddExhaustionHooks[this.overrideAddExhaustionHooks.length - 1].addExhaustion(f);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localAddExhaustion(f);
        }

        if (this.afterAddExhaustionHooks != null) {
            for(var2 = 0; var2 < this.afterAddExhaustionHooks.length; ++var2) {
                this.afterAddExhaustionHooks[var2].afterAddExhaustion(f);
            }
        }

    }

    protected PlayerBase GetOverwrittenAddExhaustion(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideAddExhaustionHooks.length; ++var2) {
            if (this.overrideAddExhaustionHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideAddExhaustionHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void addMovementStat(ClientPlayerEntity arg, double d, double e, double f) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().addMovementStat(d, e, f);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localAddMovementStat(d, e, f);
        }

    }

    protected void addMovementStat(double d, double e, double f) {
        int var7;
        if (this.beforeAddMovementStatHooks != null) {
            for(var7 = this.beforeAddMovementStatHooks.length - 1; var7 >= 0; --var7) {
                this.beforeAddMovementStatHooks[var7].beforeAddMovementStat(d, e, f);
            }
        }

        if (this.overrideAddMovementStatHooks != null) {
            this.overrideAddMovementStatHooks[this.overrideAddMovementStatHooks.length - 1].addMovementStat(d, e, f);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localAddMovementStat(d, e, f);
        }

        if (this.afterAddMovementStatHooks != null) {
            for(var7 = 0; var7 < this.afterAddMovementStatHooks.length; ++var7) {
                this.afterAddMovementStatHooks[var7].afterAddMovementStat(d, e, f);
            }
        }

    }

    protected PlayerBase GetOverwrittenAddMovementStat(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideAddMovementStatHooks.length; ++var2) {
            if (this.overrideAddMovementStatHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideAddMovementStatHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void addStat(ClientPlayerEntity arg, Stat arg2, int i) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().addStat(arg2, i);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localAddStat(arg2, i);
        }

    }

    protected void addStat(Stat arg, int i) {
        int var3;
        if (this.beforeAddStatHooks != null) {
            for(var3 = this.beforeAddStatHooks.length - 1; var3 >= 0; --var3) {
                this.beforeAddStatHooks[var3].beforeAddStat(arg, i);
            }
        }

        if (this.overrideAddStatHooks != null) {
            this.overrideAddStatHooks[this.overrideAddStatHooks.length - 1].addStat(arg, i);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localAddStat(arg, i);
        }

        if (this.afterAddStatHooks != null) {
            for(var3 = 0; var3 < this.afterAddStatHooks.length; ++var3) {
                this.afterAddStatHooks[var3].afterAddStat(arg, i);
            }
        }

    }

    protected PlayerBase GetOverwrittenAddStat(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideAddStatHooks.length; ++var2) {
            if (this.overrideAddStatHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideAddStatHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static boolean attackEntityFrom(ClientPlayerEntity arg, DamageSource arg2, int i) {
        boolean var3;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var3 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().attackEntityFrom(arg2, i);
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)arg).localAttackEntityFrom(arg2, i);
        }

        return var3;
    }

    protected boolean attackEntityFrom(DamageSource arg, int i) {
        if (this.beforeAttackEntityFromHooks != null) {
            for(int var3 = this.beforeAttackEntityFromHooks.length - 1; var3 >= 0; --var3) {
                this.beforeAttackEntityFromHooks[var3].beforeAttackEntityFrom(arg, i);
            }
        }

        boolean var5;
        if (this.overrideAttackEntityFromHooks != null) {
            var5 = this.overrideAttackEntityFromHooks[this.overrideAttackEntityFromHooks.length - 1].attackEntityFrom(arg, i);
        } else {
            var5 = ((IPlayerAPIClientPlayerEntity)this.player).localAttackEntityFrom(arg, i);
        }

        if (this.afterAttackEntityFromHooks != null) {
            for(int var4 = 0; var4 < this.afterAttackEntityFromHooks.length; ++var4) {
                this.afterAttackEntityFromHooks[var4].afterAttackEntityFrom(arg, i);
            }
        }

        return var5;
    }

    protected PlayerBase GetOverwrittenAttackEntityFrom(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideAttackEntityFromHooks.length; ++var2) {
            if (this.overrideAttackEntityFromHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideAttackEntityFromHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void alertWolves(ClientPlayerEntity arg, MobEntity arg2, boolean bl) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().alertWolves(arg2, bl);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localAlertWolves(arg2, bl);
        }

    }

    protected void alertWolves(MobEntity arg, boolean bl) {
        int var3;
        if (this.beforeAlertWolvesHooks != null) {
            for(var3 = this.beforeAlertWolvesHooks.length - 1; var3 >= 0; --var3) {
                this.beforeAlertWolvesHooks[var3].beforeAlertWolves(arg, bl);
            }
        }

        if (this.overrideAlertWolvesHooks != null) {
            this.overrideAlertWolvesHooks[this.overrideAlertWolvesHooks.length - 1].alertWolves(arg, bl);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localAlertWolves(arg, bl);
        }

        if (this.afterAlertWolvesHooks != null) {
            for(var3 = 0; var3 < this.afterAlertWolvesHooks.length; ++var3) {
                this.afterAlertWolvesHooks[var3].afterAlertWolves(arg, bl);
            }
        }

    }

    protected PlayerBase GetOverwrittenAlertWolves(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideAlertWolvesHooks.length; ++var2) {
            if (this.overrideAlertWolvesHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideAlertWolvesHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void attackTargetEntityWithCurrentItem(ClientPlayerEntity arg, Entity arg2) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().attackTargetEntityWithCurrentItem(arg2);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localAttackTargetEntityWithCurrentItem(arg2);
        }

    }

    protected void attackTargetEntityWithCurrentItem(Entity arg) {
        int var2;
        if (this.beforeAttackTargetEntityWithCurrentItemHooks != null) {
            for(var2 = this.beforeAttackTargetEntityWithCurrentItemHooks.length - 1; var2 >= 0; --var2) {
                this.beforeAttackTargetEntityWithCurrentItemHooks[var2].beforeAttackTargetEntityWithCurrentItem(arg);
            }
        }

        if (this.overrideAttackTargetEntityWithCurrentItemHooks != null) {
            this.overrideAttackTargetEntityWithCurrentItemHooks[this.overrideAttackTargetEntityWithCurrentItemHooks.length - 1].attackTargetEntityWithCurrentItem(arg);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localAttackTargetEntityWithCurrentItem(arg);
        }

        if (this.afterAttackTargetEntityWithCurrentItemHooks != null) {
            for(var2 = 0; var2 < this.afterAttackTargetEntityWithCurrentItemHooks.length; ++var2) {
                this.afterAttackTargetEntityWithCurrentItemHooks[var2].afterAttackTargetEntityWithCurrentItem(arg);
            }
        }

    }

    protected PlayerBase GetOverwrittenAttackTargetEntityWithCurrentItem(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideAttackTargetEntityWithCurrentItemHooks.length; ++var2) {
            if (this.overrideAttackTargetEntityWithCurrentItemHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideAttackTargetEntityWithCurrentItemHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static boolean canBreatheUnderwater(ClientPlayerEntity arg) {
        boolean var1;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().canBreatheUnderwater();
        } else {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).localCanBreatheUnderwater();
        }

        return var1;
    }

    protected boolean canBreatheUnderwater() {
        if (this.beforeCanBreatheUnderwaterHooks != null) {
            for(int var1 = this.beforeCanBreatheUnderwaterHooks.length - 1; var1 >= 0; --var1) {
                this.beforeCanBreatheUnderwaterHooks[var1].beforeCanBreatheUnderwater();
            }
        }

        boolean var3;
        if (this.overrideCanBreatheUnderwaterHooks != null) {
            var3 = this.overrideCanBreatheUnderwaterHooks[this.overrideCanBreatheUnderwaterHooks.length - 1].canBreatheUnderwater();
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localCanBreatheUnderwater();
        }

        if (this.afterCanBreatheUnderwaterHooks != null) {
            for(int var2 = 0; var2 < this.afterCanBreatheUnderwaterHooks.length; ++var2) {
                this.afterCanBreatheUnderwaterHooks[var2].afterCanBreatheUnderwater();
            }
        }

        return var3;
    }

    protected PlayerBase GetOverwrittenCanBreatheUnderwater(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideCanBreatheUnderwaterHooks.length; ++var2) {
            if (this.overrideCanBreatheUnderwaterHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideCanBreatheUnderwaterHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static boolean canHarvestBlock(ClientPlayerEntity arg, Block arg2) {
        boolean var2;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var2 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().canHarvestBlock(arg2);
        } else {
            var2 = ((IPlayerAPIClientPlayerEntity)arg).localCanHarvestBlock(arg2);
        }

        return var2;
    }

    protected boolean canHarvestBlock(Block arg) {
        if (this.beforeCanHarvestBlockHooks != null) {
            for(int var2 = this.beforeCanHarvestBlockHooks.length - 1; var2 >= 0; --var2) {
                this.beforeCanHarvestBlockHooks[var2].beforeCanHarvestBlock(arg);
            }
        }

        boolean var4;
        if (this.overrideCanHarvestBlockHooks != null) {
            var4 = this.overrideCanHarvestBlockHooks[this.overrideCanHarvestBlockHooks.length - 1].canHarvestBlock(arg);
        } else {
            var4 = ((IPlayerAPIClientPlayerEntity)this.player).localCanHarvestBlock(arg);
        }

        if (this.afterCanHarvestBlockHooks != null) {
            for(int var3 = 0; var3 < this.afterCanHarvestBlockHooks.length; ++var3) {
                this.afterCanHarvestBlockHooks[var3].afterCanHarvestBlock(arg);
            }
        }

        return var4;
    }

    protected PlayerBase GetOverwrittenCanHarvestBlock(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideCanHarvestBlockHooks.length; ++var2) {
            if (this.overrideCanHarvestBlockHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideCanHarvestBlockHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static boolean canPlayerEdit(ClientPlayerEntity arg, int i, int j, int k) {
        boolean var4;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var4 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().canPlayerEdit(i, j, k);
        } else {
            var4 = ((IPlayerAPIClientPlayerEntity)arg).localCanPlayerEdit(i, j, k);
        }

        return var4;
    }

    protected boolean canPlayerEdit(int i, int j, int k) {
        if (this.beforeCanPlayerEditHooks != null) {
            for(int var4 = this.beforeCanPlayerEditHooks.length - 1; var4 >= 0; --var4) {
                this.beforeCanPlayerEditHooks[var4].beforeCanPlayerEdit(i, j, k);
            }
        }

        boolean var6;
        if (this.overrideCanPlayerEditHooks != null) {
            var6 = this.overrideCanPlayerEditHooks[this.overrideCanPlayerEditHooks.length - 1].canPlayerEdit(i, j, k);
        } else {
            var6 = ((IPlayerAPIClientPlayerEntity)this.player).localCanPlayerEdit(i, j, k);
        }

        if (this.afterCanPlayerEditHooks != null) {
            for(int var5 = 0; var5 < this.afterCanPlayerEditHooks.length; ++var5) {
                this.afterCanPlayerEditHooks[var5].afterCanPlayerEdit(i, j, k);
            }
        }

        return var6;
    }

    protected PlayerBase GetOverwrittenCanPlayerEdit(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideCanPlayerEditHooks.length; ++var2) {
            if (this.overrideCanPlayerEditHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideCanPlayerEditHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static boolean canTriggerWalking(ClientPlayerEntity arg) {
        boolean var1;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().canTriggerWalking();
        } else {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).localCanTriggerWalking();
        }

        return var1;
    }

    protected boolean canTriggerWalking() {
        if (this.beforeCanTriggerWalkingHooks != null) {
            for(int var1 = this.beforeCanTriggerWalkingHooks.length - 1; var1 >= 0; --var1) {
                this.beforeCanTriggerWalkingHooks[var1].beforeCanTriggerWalking();
            }
        }

        boolean var3;
        if (this.overrideCanTriggerWalkingHooks != null) {
            var3 = this.overrideCanTriggerWalkingHooks[this.overrideCanTriggerWalkingHooks.length - 1].canTriggerWalking();
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localCanTriggerWalking();
        }

        if (this.afterCanTriggerWalkingHooks != null) {
            for(int var2 = 0; var2 < this.afterCanTriggerWalkingHooks.length; ++var2) {
                this.afterCanTriggerWalkingHooks[var2].afterCanTriggerWalking();
            }
        }

        return var3;
    }

    protected PlayerBase GetOverwrittenCanTriggerWalking(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideCanTriggerWalkingHooks.length; ++var2) {
            if (this.overrideCanTriggerWalkingHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideCanTriggerWalkingHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void closeScreen(ClientPlayerEntity arg) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().closeScreen();
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localCloseScreen();
        }

    }

    protected void closeScreen() {
        int var1;
        if (this.beforeCloseScreenHooks != null) {
            for(var1 = this.beforeCloseScreenHooks.length - 1; var1 >= 0; --var1) {
                this.beforeCloseScreenHooks[var1].beforeCloseScreen();
            }
        }

        if (this.overrideCloseScreenHooks != null) {
            this.overrideCloseScreenHooks[this.overrideCloseScreenHooks.length - 1].closeScreen();
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localCloseScreen();
        }

        if (this.afterCloseScreenHooks != null) {
            for(var1 = 0; var1 < this.afterCloseScreenHooks.length; ++var1) {
                this.afterCloseScreenHooks[var1].afterCloseScreen();
            }
        }

    }

    protected PlayerBase GetOverwrittenCloseScreen(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideCloseScreenHooks.length; ++var2) {
            if (this.overrideCloseScreenHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideCloseScreenHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void damageEntity(ClientPlayerEntity arg, DamageSource arg2, int i) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().damageEntity(arg2, i);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localDamageEntity(arg2, i);
        }

    }

    protected void damageEntity(DamageSource arg, int i) {
        int var3;
        if (this.beforeDamageEntityHooks != null) {
            for(var3 = this.beforeDamageEntityHooks.length - 1; var3 >= 0; --var3) {
                this.beforeDamageEntityHooks[var3].beforeDamageEntity(arg, i);
            }
        }

        if (this.overrideDamageEntityHooks != null) {
            this.overrideDamageEntityHooks[this.overrideDamageEntityHooks.length - 1].damageEntity(arg, i);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localDamageEntity(arg, i);
        }

        if (this.afterDamageEntityHooks != null) {
            for(var3 = 0; var3 < this.afterDamageEntityHooks.length; ++var3) {
                this.afterDamageEntityHooks[var3].afterDamageEntity(arg, i);
            }
        }

    }

    protected PlayerBase GetOverwrittenDamageEntity(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideDamageEntityHooks.length; ++var2) {
            if (this.overrideDamageEntityHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDamageEntityHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void displayGUIBrewingStand(ClientPlayerEntity arg, BrewingStandBlockEntity arg2) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().displayGUIBrewingStand(arg2);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localDisplayGUIBrewingStand(arg2);
        }

    }

    protected void displayGUIBrewingStand(BrewingStandBlockEntity arg) {
        int var2;
        if (this.beforeDisplayGUIBrewingStandHooks != null) {
            for(var2 = this.beforeDisplayGUIBrewingStandHooks.length - 1; var2 >= 0; --var2) {
                this.beforeDisplayGUIBrewingStandHooks[var2].beforeDisplayGUIBrewingStand(arg);
            }
        }

        if (this.overrideDisplayGUIBrewingStandHooks != null) {
            this.overrideDisplayGUIBrewingStandHooks[this.overrideDisplayGUIBrewingStandHooks.length - 1].displayGUIBrewingStand(arg);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localDisplayGUIBrewingStand(arg);
        }

        if (this.afterDisplayGUIBrewingStandHooks != null) {
            for(var2 = 0; var2 < this.afterDisplayGUIBrewingStandHooks.length; ++var2) {
                this.afterDisplayGUIBrewingStandHooks[var2].afterDisplayGUIBrewingStand(arg);
            }
        }

    }

    protected PlayerBase GetOverwrittenDisplayGUIBrewingStand(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideDisplayGUIBrewingStandHooks.length; ++var2) {
            if (this.overrideDisplayGUIBrewingStandHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDisplayGUIBrewingStandHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void displayGUIChest(ClientPlayerEntity arg, Inventory arg2) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().displayGUIChest(arg2);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localDisplayGUIChest(arg2);
        }

    }

    protected void displayGUIChest(Inventory arg) {
        int var2;
        if (this.beforeDisplayGUIChestHooks != null) {
            for(var2 = this.beforeDisplayGUIChestHooks.length - 1; var2 >= 0; --var2) {
                this.beforeDisplayGUIChestHooks[var2].beforeDisplayGUIChest(arg);
            }
        }

        if (this.overrideDisplayGUIChestHooks != null) {
            this.overrideDisplayGUIChestHooks[this.overrideDisplayGUIChestHooks.length - 1].displayGUIChest(arg);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localDisplayGUIChest(arg);
        }

        if (this.afterDisplayGUIChestHooks != null) {
            for(var2 = 0; var2 < this.afterDisplayGUIChestHooks.length; ++var2) {
                this.afterDisplayGUIChestHooks[var2].afterDisplayGUIChest(arg);
            }
        }

    }

    protected PlayerBase GetOverwrittenDisplayGUIChest(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideDisplayGUIChestHooks.length; ++var2) {
            if (this.overrideDisplayGUIChestHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDisplayGUIChestHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void displayGUIDispenser(ClientPlayerEntity arg, DispenserBlockEntity arg2) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().displayGUIDispenser(arg2);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localDisplayGUIDispenser(arg2);
        }

    }

    protected void displayGUIDispenser(DispenserBlockEntity arg) {
        int var2;
        if (this.beforeDisplayGUIDispenserHooks != null) {
            for(var2 = this.beforeDisplayGUIDispenserHooks.length - 1; var2 >= 0; --var2) {
                this.beforeDisplayGUIDispenserHooks[var2].beforeDisplayGUIDispenser(arg);
            }
        }

        if (this.overrideDisplayGUIDispenserHooks != null) {
            this.overrideDisplayGUIDispenserHooks[this.overrideDisplayGUIDispenserHooks.length - 1].displayGUIDispenser(arg);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localDisplayGUIDispenser(arg);
        }

        if (this.afterDisplayGUIDispenserHooks != null) {
            for(var2 = 0; var2 < this.afterDisplayGUIDispenserHooks.length; ++var2) {
                this.afterDisplayGUIDispenserHooks[var2].afterDisplayGUIDispenser(arg);
            }
        }

    }

    protected PlayerBase GetOverwrittenDisplayGUIDispenser(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideDisplayGUIDispenserHooks.length; ++var2) {
            if (this.overrideDisplayGUIDispenserHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDisplayGUIDispenserHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void displayGUIEditSign(ClientPlayerEntity arg, SignBlockEntity arg2) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().displayGUIEditSign(arg2);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localDisplayGUIEditSign(arg2);
        }

    }

    protected void displayGUIEditSign(SignBlockEntity arg) {
        int var2;
        if (this.beforeDisplayGUIEditSignHooks != null) {
            for(var2 = this.beforeDisplayGUIEditSignHooks.length - 1; var2 >= 0; --var2) {
                this.beforeDisplayGUIEditSignHooks[var2].beforeDisplayGUIEditSign(arg);
            }
        }

        if (this.overrideDisplayGUIEditSignHooks != null) {
            this.overrideDisplayGUIEditSignHooks[this.overrideDisplayGUIEditSignHooks.length - 1].displayGUIEditSign(arg);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localDisplayGUIEditSign(arg);
        }

        if (this.afterDisplayGUIEditSignHooks != null) {
            for(var2 = 0; var2 < this.afterDisplayGUIEditSignHooks.length; ++var2) {
                this.afterDisplayGUIEditSignHooks[var2].afterDisplayGUIEditSign(arg);
            }
        }

    }

    protected PlayerBase GetOverwrittenDisplayGUIEditSign(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideDisplayGUIEditSignHooks.length; ++var2) {
            if (this.overrideDisplayGUIEditSignHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDisplayGUIEditSignHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void displayGUIEnchantment(ClientPlayerEntity arg, int i, int j, int k) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().displayGUIEnchantment(i, j, k);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localDisplayGUIEnchantment(i, j, k);
        }

    }

    protected void displayGUIEnchantment(int i, int j, int k) {
        int var4;
        if (this.beforeDisplayGUIEnchantmentHooks != null) {
            for(var4 = this.beforeDisplayGUIEnchantmentHooks.length - 1; var4 >= 0; --var4) {
                this.beforeDisplayGUIEnchantmentHooks[var4].beforeDisplayGUIEnchantment(i, j, k);
            }
        }

        if (this.overrideDisplayGUIEnchantmentHooks != null) {
            this.overrideDisplayGUIEnchantmentHooks[this.overrideDisplayGUIEnchantmentHooks.length - 1].displayGUIEnchantment(i, j, k);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localDisplayGUIEnchantment(i, j, k);
        }

        if (this.afterDisplayGUIEnchantmentHooks != null) {
            for(var4 = 0; var4 < this.afterDisplayGUIEnchantmentHooks.length; ++var4) {
                this.afterDisplayGUIEnchantmentHooks[var4].afterDisplayGUIEnchantment(i, j, k);
            }
        }

    }

    protected PlayerBase GetOverwrittenDisplayGUIEnchantment(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideDisplayGUIEnchantmentHooks.length; ++var2) {
            if (this.overrideDisplayGUIEnchantmentHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDisplayGUIEnchantmentHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void displayGUIFurnace(ClientPlayerEntity arg, FurnaceBlockEntity arg2) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().displayGUIFurnace(arg2);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localDisplayGUIFurnace(arg2);
        }

    }

    protected void displayGUIFurnace(FurnaceBlockEntity arg) {
        int var2;
        if (this.beforeDisplayGUIFurnaceHooks != null) {
            for(var2 = this.beforeDisplayGUIFurnaceHooks.length - 1; var2 >= 0; --var2) {
                this.beforeDisplayGUIFurnaceHooks[var2].beforeDisplayGUIFurnace(arg);
            }
        }

        if (this.overrideDisplayGUIFurnaceHooks != null) {
            this.overrideDisplayGUIFurnaceHooks[this.overrideDisplayGUIFurnaceHooks.length - 1].displayGUIFurnace(arg);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localDisplayGUIFurnace(arg);
        }

        if (this.afterDisplayGUIFurnaceHooks != null) {
            for(var2 = 0; var2 < this.afterDisplayGUIFurnaceHooks.length; ++var2) {
                this.afterDisplayGUIFurnaceHooks[var2].afterDisplayGUIFurnace(arg);
            }
        }

    }

    protected PlayerBase GetOverwrittenDisplayGUIFurnace(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideDisplayGUIFurnaceHooks.length; ++var2) {
            if (this.overrideDisplayGUIFurnaceHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDisplayGUIFurnaceHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void displayWorkbenchGUI(ClientPlayerEntity arg, int i, int j, int k) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().displayWorkbenchGUI(i, j, k);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localDisplayWorkbenchGUI(i, j, k);
        }

    }

    protected void displayWorkbenchGUI(int i, int j, int k) {
        int var4;
        if (this.beforeDisplayWorkbenchGUIHooks != null) {
            for(var4 = this.beforeDisplayWorkbenchGUIHooks.length - 1; var4 >= 0; --var4) {
                this.beforeDisplayWorkbenchGUIHooks[var4].beforeDisplayWorkbenchGUI(i, j, k);
            }
        }

        if (this.overrideDisplayWorkbenchGUIHooks != null) {
            this.overrideDisplayWorkbenchGUIHooks[this.overrideDisplayWorkbenchGUIHooks.length - 1].displayWorkbenchGUI(i, j, k);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localDisplayWorkbenchGUI(i, j, k);
        }

        if (this.afterDisplayWorkbenchGUIHooks != null) {
            for(var4 = 0; var4 < this.afterDisplayWorkbenchGUIHooks.length; ++var4) {
                this.afterDisplayWorkbenchGUIHooks[var4].afterDisplayWorkbenchGUI(i, j, k);
            }
        }

    }

    protected PlayerBase GetOverwrittenDisplayWorkbenchGUI(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideDisplayWorkbenchGUIHooks.length; ++var2) {
            if (this.overrideDisplayWorkbenchGUIHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDisplayWorkbenchGUIHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static ItemEntity dropOneItem(ClientPlayerEntity arg) {
        ItemEntity var1;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().dropOneItem();
        } else {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).localDropOneItem();
        }

        return var1;
    }

    protected ItemEntity dropOneItem() {
        if (this.beforeDropOneItemHooks != null) {
            for(int var1 = this.beforeDropOneItemHooks.length - 1; var1 >= 0; --var1) {
                this.beforeDropOneItemHooks[var1].beforeDropOneItem();
            }
        }

        ItemEntity var3;
        if (this.overrideDropOneItemHooks != null) {
            var3 = this.overrideDropOneItemHooks[this.overrideDropOneItemHooks.length - 1].dropOneItem();
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localDropOneItem();
        }

        if (this.afterDropOneItemHooks != null) {
            for(int var2 = 0; var2 < this.afterDropOneItemHooks.length; ++var2) {
                this.afterDropOneItemHooks[var2].afterDropOneItem();
            }
        }

        return var3;
    }

    protected PlayerBase GetOverwrittenDropOneItem(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideDropOneItemHooks.length; ++var2) {
            if (this.overrideDropOneItemHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDropOneItemHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static ItemEntity dropPlayerItem(ClientPlayerEntity arg, ItemStack arg2) {
        ItemEntity var2;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var2 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().dropPlayerItem(arg2);
        } else {
            var2 = ((IPlayerAPIClientPlayerEntity)arg).localDropPlayerItem(arg2);
        }

        return var2;
    }

    protected ItemEntity dropPlayerItem(ItemStack arg) {
        if (this.beforeDropPlayerItemHooks != null) {
            for(int var2 = this.beforeDropPlayerItemHooks.length - 1; var2 >= 0; --var2) {
                this.beforeDropPlayerItemHooks[var2].beforeDropPlayerItem(arg);
            }
        }

        ItemEntity var4;
        if (this.overrideDropPlayerItemHooks != null) {
            var4 = this.overrideDropPlayerItemHooks[this.overrideDropPlayerItemHooks.length - 1].dropPlayerItem(arg);
        } else {
            var4 = ((IPlayerAPIClientPlayerEntity)this.player).localDropPlayerItem(arg);
        }

        if (this.afterDropPlayerItemHooks != null) {
            for(int var3 = 0; var3 < this.afterDropPlayerItemHooks.length; ++var3) {
                this.afterDropPlayerItemHooks[var3].afterDropPlayerItem(arg);
            }
        }

        return var4;
    }

    protected PlayerBase GetOverwrittenDropPlayerItem(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideDropPlayerItemHooks.length; ++var2) {
            if (this.overrideDropPlayerItemHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDropPlayerItemHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static ItemEntity dropPlayerItemWithRandomChoice(ClientPlayerEntity arg, ItemStack arg2, boolean bl) {
        ItemEntity var3;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var3 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().dropPlayerItemWithRandomChoice(arg2, bl);
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)arg).localDropPlayerItemWithRandomChoice(arg2, bl);
        }

        return var3;
    }

    protected ItemEntity dropPlayerItemWithRandomChoice(ItemStack arg, boolean bl) {
        if (this.beforeDropPlayerItemWithRandomChoiceHooks != null) {
            for(int var3 = this.beforeDropPlayerItemWithRandomChoiceHooks.length - 1; var3 >= 0; --var3) {
                this.beforeDropPlayerItemWithRandomChoiceHooks[var3].beforeDropPlayerItemWithRandomChoice(arg, bl);
            }
        }

        ItemEntity var5;
        if (this.overrideDropPlayerItemWithRandomChoiceHooks != null) {
            var5 = this.overrideDropPlayerItemWithRandomChoiceHooks[this.overrideDropPlayerItemWithRandomChoiceHooks.length - 1].dropPlayerItemWithRandomChoice(arg, bl);
        } else {
            var5 = ((IPlayerAPIClientPlayerEntity)this.player).localDropPlayerItemWithRandomChoice(arg, bl);
        }

        if (this.afterDropPlayerItemWithRandomChoiceHooks != null) {
            for(int var4 = 0; var4 < this.afterDropPlayerItemWithRandomChoiceHooks.length; ++var4) {
                this.afterDropPlayerItemWithRandomChoiceHooks[var4].afterDropPlayerItemWithRandomChoice(arg, bl);
            }
        }

        return var5;
    }

    protected PlayerBase GetOverwrittenDropPlayerItemWithRandomChoice(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideDropPlayerItemWithRandomChoiceHooks.length; ++var2) {
            if (this.overrideDropPlayerItemWithRandomChoiceHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDropPlayerItemWithRandomChoiceHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void fall(ClientPlayerEntity arg, float f) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().fall(f);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localFall(f);
        }

    }

    protected void fall(float f) {
        int var2;
        if (this.beforeFallHooks != null) {
            for(var2 = this.beforeFallHooks.length - 1; var2 >= 0; --var2) {
                this.beforeFallHooks[var2].beforeFall(f);
            }
        }

        if (this.overrideFallHooks != null) {
            this.overrideFallHooks[this.overrideFallHooks.length - 1].fall(f);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localFall(f);
        }

        if (this.afterFallHooks != null) {
            for(var2 = 0; var2 < this.afterFallHooks.length; ++var2) {
                this.afterFallHooks[var2].afterFall(f);
            }
        }

    }

    protected PlayerBase GetOverwrittenFall(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideFallHooks.length; ++var2) {
            if (this.overrideFallHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideFallHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static float getBrightness(ClientPlayerEntity arg, float f) {
        float var2;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var2 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().getBrightness(f);
        } else {
            var2 = ((IPlayerAPIClientPlayerEntity)arg).localGetBrightness(f);
        }

        return var2;
    }

    protected float getBrightness(float f) {
        if (this.beforeGetBrightnessHooks != null) {
            for(int var2 = this.beforeGetBrightnessHooks.length - 1; var2 >= 0; --var2) {
                this.beforeGetBrightnessHooks[var2].beforeGetBrightness(f);
            }
        }

        float var4;
        if (this.overrideGetBrightnessHooks != null) {
            var4 = this.overrideGetBrightnessHooks[this.overrideGetBrightnessHooks.length - 1].getBrightness(f);
        } else {
            var4 = ((IPlayerAPIClientPlayerEntity)this.player).localGetBrightness(f);
        }

        if (this.afterGetBrightnessHooks != null) {
            for(int var3 = 0; var3 < this.afterGetBrightnessHooks.length; ++var3) {
                this.afterGetBrightnessHooks[var3].afterGetBrightness(f);
            }
        }

        return var4;
    }

    protected PlayerBase GetOverwrittenGetBrightness(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideGetBrightnessHooks.length; ++var2) {
            if (this.overrideGetBrightnessHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideGetBrightnessHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static int getBrightnessForRender(ClientPlayerEntity arg, float f) {
        int var2;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var2 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().getBrightnessForRender(f);
        } else {
            var2 = ((IPlayerAPIClientPlayerEntity)arg).localGetBrightnessForRender(f);
        }

        return var2;
    }

    protected int getBrightnessForRender(float f) {
        int var2;
        if (this.beforeGetBrightnessForRenderHooks != null) {
            for(var2 = this.beforeGetBrightnessForRenderHooks.length - 1; var2 >= 0; --var2) {
                this.beforeGetBrightnessForRenderHooks[var2].beforeGetBrightnessForRender(f);
            }
        }

        if (this.overrideGetBrightnessForRenderHooks != null) {
            var2 = this.overrideGetBrightnessForRenderHooks[this.overrideGetBrightnessForRenderHooks.length - 1].getBrightnessForRender(f);
        } else {
            var2 = ((IPlayerAPIClientPlayerEntity)this.player).localGetBrightnessForRender(f);
        }

        if (this.afterGetBrightnessForRenderHooks != null) {
            for(int var3 = 0; var3 < this.afterGetBrightnessForRenderHooks.length; ++var3) {
                this.afterGetBrightnessForRenderHooks[var3].afterGetBrightnessForRender(f);
            }
        }

        return var2;
    }

    protected PlayerBase GetOverwrittenGetBrightnessForRender(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideGetBrightnessForRenderHooks.length; ++var2) {
            if (this.overrideGetBrightnessForRenderHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideGetBrightnessForRenderHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static float getCurrentPlayerStrVsBlock(ClientPlayerEntity arg, Block arg2) {
        float var2;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var2 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().getCurrentPlayerStrVsBlock(arg2);
        } else {
            var2 = ((IPlayerAPIClientPlayerEntity)arg).localGetCurrentPlayerStrVsBlock(arg2);
        }

        return var2;
    }

    protected float getCurrentPlayerStrVsBlock(Block arg) {
        if (this.beforeGetCurrentPlayerStrVsBlockHooks != null) {
            for(int var2 = this.beforeGetCurrentPlayerStrVsBlockHooks.length - 1; var2 >= 0; --var2) {
                this.beforeGetCurrentPlayerStrVsBlockHooks[var2].beforeGetCurrentPlayerStrVsBlock(arg);
            }
        }

        float var4;
        if (this.overrideGetCurrentPlayerStrVsBlockHooks != null) {
            var4 = this.overrideGetCurrentPlayerStrVsBlockHooks[this.overrideGetCurrentPlayerStrVsBlockHooks.length - 1].getCurrentPlayerStrVsBlock(arg);
        } else {
            var4 = ((IPlayerAPIClientPlayerEntity)this.player).localGetCurrentPlayerStrVsBlock(arg);
        }

        if (this.afterGetCurrentPlayerStrVsBlockHooks != null) {
            for(int var3 = 0; var3 < this.afterGetCurrentPlayerStrVsBlockHooks.length; ++var3) {
                this.afterGetCurrentPlayerStrVsBlockHooks[var3].afterGetCurrentPlayerStrVsBlock(arg);
            }
        }

        return var4;
    }

    protected PlayerBase GetOverwrittenGetCurrentPlayerStrVsBlock(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideGetCurrentPlayerStrVsBlockHooks.length; ++var2) {
            if (this.overrideGetCurrentPlayerStrVsBlockHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideGetCurrentPlayerStrVsBlockHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static double getDistanceSq(ClientPlayerEntity arg, double d, double e, double f) {
        double var7;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var7 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().getDistanceSq(d, e, f);
        } else {
            var7 = ((IPlayerAPIClientPlayerEntity)arg).localGetDistanceSq(d, e, f);
        }

        return var7;
    }

    protected double getDistanceSq(double d, double e, double f) {
        if (this.beforeGetDistanceSqHooks != null) {
            for(int var7 = this.beforeGetDistanceSqHooks.length - 1; var7 >= 0; --var7) {
                this.beforeGetDistanceSqHooks[var7].beforeGetDistanceSq(d, e, f);
            }
        }

        double var10;
        if (this.overrideGetDistanceSqHooks != null) {
            var10 = this.overrideGetDistanceSqHooks[this.overrideGetDistanceSqHooks.length - 1].getDistanceSq(d, e, f);
        } else {
            var10 = ((IPlayerAPIClientPlayerEntity)this.player).localGetDistanceSq(d, e, f);
        }

        if (this.afterGetDistanceSqHooks != null) {
            for(int var9 = 0; var9 < this.afterGetDistanceSqHooks.length; ++var9) {
                this.afterGetDistanceSqHooks[var9].afterGetDistanceSq(d, e, f);
            }
        }

        return var10;
    }

    protected PlayerBase GetOverwrittenGetDistanceSq(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideGetDistanceSqHooks.length; ++var2) {
            if (this.overrideGetDistanceSqHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideGetDistanceSqHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static double getDistanceSqToEntity(ClientPlayerEntity arg, Entity arg2) {
        double var2;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var2 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().getDistanceSqToEntity(arg2);
        } else {
            var2 = ((IPlayerAPIClientPlayerEntity)arg).localGetDistanceSqToEntity(arg2);
        }

        return var2;
    }

    protected double getDistanceSqToEntity(Entity arg) {
        if (this.beforeGetDistanceSqToEntityHooks != null) {
            for(int var2 = this.beforeGetDistanceSqToEntityHooks.length - 1; var2 >= 0; --var2) {
                this.beforeGetDistanceSqToEntityHooks[var2].beforeGetDistanceSqToEntity(arg);
            }
        }

        double var5;
        if (this.overrideGetDistanceSqToEntityHooks != null) {
            var5 = this.overrideGetDistanceSqToEntityHooks[this.overrideGetDistanceSqToEntityHooks.length - 1].getDistanceSqToEntity(arg);
        } else {
            var5 = ((IPlayerAPIClientPlayerEntity)this.player).localGetDistanceSqToEntity(arg);
        }

        if (this.afterGetDistanceSqToEntityHooks != null) {
            for(int var4 = 0; var4 < this.afterGetDistanceSqToEntityHooks.length; ++var4) {
                this.afterGetDistanceSqToEntityHooks[var4].afterGetDistanceSqToEntity(arg);
            }
        }

        return var5;
    }

    protected PlayerBase GetOverwrittenGetDistanceSqToEntity(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideGetDistanceSqToEntityHooks.length; ++var2) {
            if (this.overrideGetDistanceSqToEntityHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideGetDistanceSqToEntityHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static float getFOVMultiplier(ClientPlayerEntity arg) {
        float var1;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().getFOVMultiplier();
        } else {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).localGetFOVMultiplier();
        }

        return var1;
    }

    protected float getFOVMultiplier() {
        if (this.beforeGetFOVMultiplierHooks != null) {
            for(int var1 = this.beforeGetFOVMultiplierHooks.length - 1; var1 >= 0; --var1) {
                this.beforeGetFOVMultiplierHooks[var1].beforeGetFOVMultiplier();
            }
        }

        float var3;
        if (this.overrideGetFOVMultiplierHooks != null) {
            var3 = this.overrideGetFOVMultiplierHooks[this.overrideGetFOVMultiplierHooks.length - 1].getFOVMultiplier();
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localGetFOVMultiplier();
        }

        if (this.afterGetFOVMultiplierHooks != null) {
            for(int var2 = 0; var2 < this.afterGetFOVMultiplierHooks.length; ++var2) {
                this.afterGetFOVMultiplierHooks[var2].afterGetFOVMultiplier();
            }
        }

        return var3;
    }

    protected PlayerBase GetOverwrittenGetFOVMultiplier(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideGetFOVMultiplierHooks.length; ++var2) {
            if (this.overrideGetFOVMultiplierHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideGetFOVMultiplierHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static String getHurtSound(ClientPlayerEntity arg) {
        String var1;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().getHurtSound();
        } else {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).localGetHurtSound();
        }

        return var1;
    }

    protected String getHurtSound() {
        if (this.beforeGetHurtSoundHooks != null) {
            for(int var1 = this.beforeGetHurtSoundHooks.length - 1; var1 >= 0; --var1) {
                this.beforeGetHurtSoundHooks[var1].beforeGetHurtSound();
            }
        }

        String var3;
        if (this.overrideGetHurtSoundHooks != null) {
            var3 = this.overrideGetHurtSoundHooks[this.overrideGetHurtSoundHooks.length - 1].getHurtSound();
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localGetHurtSound();
        }

        if (this.afterGetHurtSoundHooks != null) {
            for(int var2 = 0; var2 < this.afterGetHurtSoundHooks.length; ++var2) {
                this.afterGetHurtSoundHooks[var2].afterGetHurtSound();
            }
        }

        return var3;
    }

    protected PlayerBase GetOverwrittenGetHurtSound(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideGetHurtSoundHooks.length; ++var2) {
            if (this.overrideGetHurtSoundHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideGetHurtSoundHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static int getItemIcon(ClientPlayerEntity arg, ItemStack arg2, int i) {
        int var3;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var3 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().getItemIcon(arg2, i);
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)arg).localGetItemIcon(arg2, i);
        }

        return var3;
    }

    protected int getItemIcon(ItemStack arg, int i) {
        int var3;
        if (this.beforeGetItemIconHooks != null) {
            for(var3 = this.beforeGetItemIconHooks.length - 1; var3 >= 0; --var3) {
                this.beforeGetItemIconHooks[var3].beforeGetItemIcon(arg, i);
            }
        }

        if (this.overrideGetItemIconHooks != null) {
            var3 = this.overrideGetItemIconHooks[this.overrideGetItemIconHooks.length - 1].getItemIcon(arg, i);
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localGetItemIcon(arg, i);
        }

        if (this.afterGetItemIconHooks != null) {
            for(int var4 = 0; var4 < this.afterGetItemIconHooks.length; ++var4) {
                this.afterGetItemIconHooks[var4].afterGetItemIcon(arg, i);
            }
        }

        return var3;
    }

    protected PlayerBase GetOverwrittenGetItemIcon(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideGetItemIconHooks.length; ++var2) {
            if (this.overrideGetItemIconHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideGetItemIconHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static int getSleepTimer(ClientPlayerEntity arg) {
        int var1;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().getSleepTimer();
        } else {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).localGetSleepTimer();
        }

        return var1;
    }

    protected int getSleepTimer() {
        int var1;
        if (this.beforeGetSleepTimerHooks != null) {
            for(var1 = this.beforeGetSleepTimerHooks.length - 1; var1 >= 0; --var1) {
                this.beforeGetSleepTimerHooks[var1].beforeGetSleepTimer();
            }
        }

        if (this.overrideGetSleepTimerHooks != null) {
            var1 = this.overrideGetSleepTimerHooks[this.overrideGetSleepTimerHooks.length - 1].getSleepTimer();
        } else {
            var1 = ((IPlayerAPIClientPlayerEntity)this.player).localGetSleepTimer();
        }

        if (this.afterGetSleepTimerHooks != null) {
            for(int var2 = 0; var2 < this.afterGetSleepTimerHooks.length; ++var2) {
                this.afterGetSleepTimerHooks[var2].afterGetSleepTimer();
            }
        }

        return var1;
    }

    protected PlayerBase GetOverwrittenGetSleepTimer(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideGetSleepTimerHooks.length; ++var2) {
            if (this.overrideGetSleepTimerHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideGetSleepTimerHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static float getSpeedModifier(ClientPlayerEntity arg) {
        float var1;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().getSpeedModifier();
        } else {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).localGetSpeedModifier();
        }

        return var1;
    }

    protected float getSpeedModifier() {
        if (this.beforeGetSpeedModifierHooks != null) {
            for(int var1 = this.beforeGetSpeedModifierHooks.length - 1; var1 >= 0; --var1) {
                this.beforeGetSpeedModifierHooks[var1].beforeGetSpeedModifier();
            }
        }

        float var3;
        if (this.overrideGetSpeedModifierHooks != null) {
            var3 = this.overrideGetSpeedModifierHooks[this.overrideGetSpeedModifierHooks.length - 1].getSpeedModifier();
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localGetSpeedModifier();
        }

        if (this.afterGetSpeedModifierHooks != null) {
            for(int var2 = 0; var2 < this.afterGetSpeedModifierHooks.length; ++var2) {
                this.afterGetSpeedModifierHooks[var2].afterGetSpeedModifier();
            }
        }

        return var3;
    }

    protected PlayerBase GetOverwrittenGetSpeedModifier(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideGetSpeedModifierHooks.length; ++var2) {
            if (this.overrideGetSpeedModifierHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideGetSpeedModifierHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static boolean handleLavaMovement(ClientPlayerEntity arg) {
        boolean var1;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().handleLavaMovement();
        } else {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).localHandleLavaMovement();
        }

        return var1;
    }

    protected boolean handleLavaMovement() {
        if (this.beforeHandleLavaMovementHooks != null) {
            for(int var1 = this.beforeHandleLavaMovementHooks.length - 1; var1 >= 0; --var1) {
                this.beforeHandleLavaMovementHooks[var1].beforeHandleLavaMovement();
            }
        }

        boolean var3;
        if (this.overrideHandleLavaMovementHooks != null) {
            var3 = this.overrideHandleLavaMovementHooks[this.overrideHandleLavaMovementHooks.length - 1].handleLavaMovement();
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localHandleLavaMovement();
        }

        if (this.afterHandleLavaMovementHooks != null) {
            for(int var2 = 0; var2 < this.afterHandleLavaMovementHooks.length; ++var2) {
                this.afterHandleLavaMovementHooks[var2].afterHandleLavaMovement();
            }
        }

        return var3;
    }

    protected PlayerBase GetOverwrittenHandleLavaMovement(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideHandleLavaMovementHooks.length; ++var2) {
            if (this.overrideHandleLavaMovementHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideHandleLavaMovementHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static boolean handleWaterMovement(ClientPlayerEntity arg) {
        boolean var1;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().handleWaterMovement();
        } else {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).localHandleWaterMovement();
        }

        return var1;
    }

    protected boolean handleWaterMovement() {
        if (this.beforeHandleWaterMovementHooks != null) {
            for(int var1 = this.beforeHandleWaterMovementHooks.length - 1; var1 >= 0; --var1) {
                this.beforeHandleWaterMovementHooks[var1].beforeHandleWaterMovement();
            }
        }

        boolean var3;
        if (this.overrideHandleWaterMovementHooks != null) {
            var3 = this.overrideHandleWaterMovementHooks[this.overrideHandleWaterMovementHooks.length - 1].handleWaterMovement();
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localHandleWaterMovement();
        }

        if (this.afterHandleWaterMovementHooks != null) {
            for(int var2 = 0; var2 < this.afterHandleWaterMovementHooks.length; ++var2) {
                this.afterHandleWaterMovementHooks[var2].afterHandleWaterMovement();
            }
        }

        return var3;
    }

    protected PlayerBase GetOverwrittenHandleWaterMovement(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideHandleWaterMovementHooks.length; ++var2) {
            if (this.overrideHandleWaterMovementHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideHandleWaterMovementHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void heal(ClientPlayerEntity arg, int i) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().heal(i);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localHeal(i);
        }

    }

    protected void heal(int i) {
        int var2;
        if (this.beforeHealHooks != null) {
            for(var2 = this.beforeHealHooks.length - 1; var2 >= 0; --var2) {
                this.beforeHealHooks[var2].beforeHeal(i);
            }
        }

        if (this.overrideHealHooks != null) {
            this.overrideHealHooks[this.overrideHealHooks.length - 1].heal(i);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localHeal(i);
        }

        if (this.afterHealHooks != null) {
            for(var2 = 0; var2 < this.afterHealHooks.length; ++var2) {
                this.afterHealHooks[var2].afterHeal(i);
            }
        }

    }

    protected PlayerBase GetOverwrittenHeal(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideHealHooks.length; ++var2) {
            if (this.overrideHealHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideHealHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static boolean isEntityInsideOpaqueBlock(ClientPlayerEntity arg) {
        boolean var1;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().isEntityInsideOpaqueBlock();
        } else {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).localIsEntityInsideOpaqueBlock();
        }

        return var1;
    }

    protected boolean isEntityInsideOpaqueBlock() {
        if (this.beforeIsEntityInsideOpaqueBlockHooks != null) {
            for(int var1 = this.beforeIsEntityInsideOpaqueBlockHooks.length - 1; var1 >= 0; --var1) {
                this.beforeIsEntityInsideOpaqueBlockHooks[var1].beforeIsEntityInsideOpaqueBlock();
            }
        }

        boolean var3;
        if (this.overrideIsEntityInsideOpaqueBlockHooks != null) {
            var3 = this.overrideIsEntityInsideOpaqueBlockHooks[this.overrideIsEntityInsideOpaqueBlockHooks.length - 1].isEntityInsideOpaqueBlock();
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localIsEntityInsideOpaqueBlock();
        }

        if (this.afterIsEntityInsideOpaqueBlockHooks != null) {
            for(int var2 = 0; var2 < this.afterIsEntityInsideOpaqueBlockHooks.length; ++var2) {
                this.afterIsEntityInsideOpaqueBlockHooks[var2].afterIsEntityInsideOpaqueBlock();
            }
        }

        return var3;
    }

    protected PlayerBase GetOverwrittenIsEntityInsideOpaqueBlock(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideIsEntityInsideOpaqueBlockHooks.length; ++var2) {
            if (this.overrideIsEntityInsideOpaqueBlockHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideIsEntityInsideOpaqueBlockHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static boolean isInWater(ClientPlayerEntity arg) {
        boolean var1;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().isInWater();
        } else {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).localIsInWater();
        }

        return var1;
    }

    protected boolean isInWater() {
        if (this.beforeIsInWaterHooks != null) {
            for(int var1 = this.beforeIsInWaterHooks.length - 1; var1 >= 0; --var1) {
                this.beforeIsInWaterHooks[var1].beforeIsInWater();
            }
        }

        boolean var3;
        if (this.overrideIsInWaterHooks != null) {
            var3 = this.overrideIsInWaterHooks[this.overrideIsInWaterHooks.length - 1].isInWater();
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localIsInWater();
        }

        if (this.afterIsInWaterHooks != null) {
            for(int var2 = 0; var2 < this.afterIsInWaterHooks.length; ++var2) {
                this.afterIsInWaterHooks[var2].afterIsInWater();
            }
        }

        return var3;
    }

    protected PlayerBase GetOverwrittenIsInWater(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideIsInWaterHooks.length; ++var2) {
            if (this.overrideIsInWaterHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideIsInWaterHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static boolean isInsideOfMaterial(ClientPlayerEntity arg, Material arg2) {
        boolean var2;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var2 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().isInsideOfMaterial(arg2);
        } else {
            var2 = ((IPlayerAPIClientPlayerEntity)arg).localIsInsideOfMaterial(arg2);
        }

        return var2;
    }

    protected boolean isInsideOfMaterial(Material arg) {
        if (this.beforeIsInsideOfMaterialHooks != null) {
            for(int var2 = this.beforeIsInsideOfMaterialHooks.length - 1; var2 >= 0; --var2) {
                this.beforeIsInsideOfMaterialHooks[var2].beforeIsInsideOfMaterial(arg);
            }
        }

        boolean var4;
        if (this.overrideIsInsideOfMaterialHooks != null) {
            var4 = this.overrideIsInsideOfMaterialHooks[this.overrideIsInsideOfMaterialHooks.length - 1].isInsideOfMaterial(arg);
        } else {
            var4 = ((IPlayerAPIClientPlayerEntity)this.player).localIsInsideOfMaterial(arg);
        }

        if (this.afterIsInsideOfMaterialHooks != null) {
            for(int var3 = 0; var3 < this.afterIsInsideOfMaterialHooks.length; ++var3) {
                this.afterIsInsideOfMaterialHooks[var3].afterIsInsideOfMaterial(arg);
            }
        }

        return var4;
    }

    protected PlayerBase GetOverwrittenIsInsideOfMaterial(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideIsInsideOfMaterialHooks.length; ++var2) {
            if (this.overrideIsInsideOfMaterialHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideIsInsideOfMaterialHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static boolean isOnLadder(ClientPlayerEntity arg) {
        boolean var1;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().isOnLadder();
        } else {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).localIsOnLadder();
        }

        return var1;
    }

    protected boolean isOnLadder() {
        if (this.beforeIsOnLadderHooks != null) {
            for(int var1 = this.beforeIsOnLadderHooks.length - 1; var1 >= 0; --var1) {
                this.beforeIsOnLadderHooks[var1].beforeIsOnLadder();
            }
        }

        boolean var3;
        if (this.overrideIsOnLadderHooks != null) {
            var3 = this.overrideIsOnLadderHooks[this.overrideIsOnLadderHooks.length - 1].isOnLadder();
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localIsOnLadder();
        }

        if (this.afterIsOnLadderHooks != null) {
            for(int var2 = 0; var2 < this.afterIsOnLadderHooks.length; ++var2) {
                this.afterIsOnLadderHooks[var2].afterIsOnLadder();
            }
        }

        return var3;
    }

    protected PlayerBase GetOverwrittenIsOnLadder(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideIsOnLadderHooks.length; ++var2) {
            if (this.overrideIsOnLadderHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideIsOnLadderHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static boolean isSneaking(ClientPlayerEntity arg) {
        boolean var1;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().isSneaking();
        } else {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).localIsSneaking();
        }

        return var1;
    }

    protected boolean isSneaking() {
        if (this.beforeIsSneakingHooks != null) {
            for(int var1 = this.beforeIsSneakingHooks.length - 1; var1 >= 0; --var1) {
                this.beforeIsSneakingHooks[var1].beforeIsSneaking();
            }
        }

        boolean var3;
        if (this.overrideIsSneakingHooks != null) {
            var3 = this.overrideIsSneakingHooks[this.overrideIsSneakingHooks.length - 1].isSneaking();
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localIsSneaking();
        }

        if (this.afterIsSneakingHooks != null) {
            for(int var2 = 0; var2 < this.afterIsSneakingHooks.length; ++var2) {
                this.afterIsSneakingHooks[var2].afterIsSneaking();
            }
        }

        return var3;
    }

    protected PlayerBase GetOverwrittenIsSneaking(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideIsSneakingHooks.length; ++var2) {
            if (this.overrideIsSneakingHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideIsSneakingHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static boolean isSprinting(ClientPlayerEntity arg) {
        boolean var1;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().isSprinting();
        } else {
            var1 = ((IPlayerAPIClientPlayerEntity)arg).localIsSprinting();
        }

        return var1;
    }

    protected boolean isSprinting() {
        if (this.beforeIsSprintingHooks != null) {
            for(int var1 = this.beforeIsSprintingHooks.length - 1; var1 >= 0; --var1) {
                this.beforeIsSprintingHooks[var1].beforeIsSprinting();
            }
        }

        boolean var3;
        if (this.overrideIsSprintingHooks != null) {
            var3 = this.overrideIsSprintingHooks[this.overrideIsSprintingHooks.length - 1].isSprinting();
        } else {
            var3 = ((IPlayerAPIClientPlayerEntity)this.player).localIsSprinting();
        }

        if (this.afterIsSprintingHooks != null) {
            for(int var2 = 0; var2 < this.afterIsSprintingHooks.length; ++var2) {
                this.afterIsSprintingHooks[var2].afterIsSprinting();
            }
        }

        return var3;
    }

    protected PlayerBase GetOverwrittenIsSprinting(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideIsSprintingHooks.length; ++var2) {
            if (this.overrideIsSprintingHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideIsSprintingHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void jump(ClientPlayerEntity arg) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().jump();
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localJump();
        }

    }

    protected void jump() {
        int var1;
        if (this.beforeJumpHooks != null) {
            for(var1 = this.beforeJumpHooks.length - 1; var1 >= 0; --var1) {
                this.beforeJumpHooks[var1].beforeJump();
            }
        }

        if (this.overrideJumpHooks != null) {
            this.overrideJumpHooks[this.overrideJumpHooks.length - 1].jump();
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localJump();
        }

        if (this.afterJumpHooks != null) {
            for(var1 = 0; var1 < this.afterJumpHooks.length; ++var1) {
                this.afterJumpHooks[var1].afterJump();
            }
        }

    }

    protected PlayerBase GetOverwrittenJump(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideJumpHooks.length; ++var2) {
            if (this.overrideJumpHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideJumpHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void knockBack(ClientPlayerEntity arg, Entity arg2, int i, double d, double e) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().knockBack(arg2, i, d, e);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localKnockBack(arg2, i, d, e);
        }

    }

    protected void knockBack(Entity arg, int i, double d, double e) {
        int var7;
        if (this.beforeKnockBackHooks != null) {
            for(var7 = this.beforeKnockBackHooks.length - 1; var7 >= 0; --var7) {
                this.beforeKnockBackHooks[var7].beforeKnockBack(arg, i, d, e);
            }
        }

        if (this.overrideKnockBackHooks != null) {
            this.overrideKnockBackHooks[this.overrideKnockBackHooks.length - 1].knockBack(arg, i, d, e);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localKnockBack(arg, i, d, e);
        }

        if (this.afterKnockBackHooks != null) {
            for(var7 = 0; var7 < this.afterKnockBackHooks.length; ++var7) {
                this.afterKnockBackHooks[var7].afterKnockBack(arg, i, d, e);
            }
        }

    }

    protected PlayerBase GetOverwrittenKnockBack(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideKnockBackHooks.length; ++var2) {
            if (this.overrideKnockBackHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideKnockBackHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void moveEntity(ClientPlayerEntity arg, double d, double e, double f) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().moveEntity(d, e, f);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localMoveEntity(d, e, f);
        }

    }

    protected void moveEntity(double d, double e, double f) {
        int var7;
        if (this.beforeMoveEntityHooks != null) {
            for(var7 = this.beforeMoveEntityHooks.length - 1; var7 >= 0; --var7) {
                this.beforeMoveEntityHooks[var7].beforeMoveEntity(d, e, f);
            }
        }

        if (this.overrideMoveEntityHooks != null) {
            this.overrideMoveEntityHooks[this.overrideMoveEntityHooks.length - 1].moveEntity(d, e, f);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localMoveEntity(d, e, f);
        }

        if (this.afterMoveEntityHooks != null) {
            for(var7 = 0; var7 < this.afterMoveEntityHooks.length; ++var7) {
                this.afterMoveEntityHooks[var7].afterMoveEntity(d, e, f);
            }
        }

    }

    protected PlayerBase GetOverwrittenMoveEntity(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideMoveEntityHooks.length; ++var2) {
            if (this.overrideMoveEntityHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideMoveEntityHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void moveEntityWithHeading(ClientPlayerEntity arg, float f, float g) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().moveEntityWithHeading(f, g);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localMoveEntityWithHeading(f, g);
        }

    }

    protected void moveEntityWithHeading(float f, float g) {
        int var3;
        if (this.beforeMoveEntityWithHeadingHooks != null) {
            for(var3 = this.beforeMoveEntityWithHeadingHooks.length - 1; var3 >= 0; --var3) {
                this.beforeMoveEntityWithHeadingHooks[var3].beforeMoveEntityWithHeading(f, g);
            }
        }

        if (this.overrideMoveEntityWithHeadingHooks != null) {
            this.overrideMoveEntityWithHeadingHooks[this.overrideMoveEntityWithHeadingHooks.length - 1].moveEntityWithHeading(f, g);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localMoveEntityWithHeading(f, g);
        }

        if (this.afterMoveEntityWithHeadingHooks != null) {
            for(var3 = 0; var3 < this.afterMoveEntityWithHeadingHooks.length; ++var3) {
                this.afterMoveEntityWithHeadingHooks[var3].afterMoveEntityWithHeading(f, g);
            }
        }

    }

    protected PlayerBase GetOverwrittenMoveEntityWithHeading(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideMoveEntityWithHeadingHooks.length; ++var2) {
            if (this.overrideMoveEntityWithHeadingHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideMoveEntityWithHeadingHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void moveFlying(ClientPlayerEntity arg, float f, float g, float h) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().moveFlying(f, g, h);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localMoveFlying(f, g, h);
        }

    }

    protected void moveFlying(float f, float g, float h) {
        int var4;
        if (this.beforeMoveFlyingHooks != null) {
            for(var4 = this.beforeMoveFlyingHooks.length - 1; var4 >= 0; --var4) {
                this.beforeMoveFlyingHooks[var4].beforeMoveFlying(f, g, h);
            }
        }

        if (this.overrideMoveFlyingHooks != null) {
            this.overrideMoveFlyingHooks[this.overrideMoveFlyingHooks.length - 1].moveFlying(f, g, h);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localMoveFlying(f, g, h);
        }

        if (this.afterMoveFlyingHooks != null) {
            for(var4 = 0; var4 < this.afterMoveFlyingHooks.length; ++var4) {
                this.afterMoveFlyingHooks[var4].afterMoveFlying(f, g, h);
            }
        }

    }

    protected PlayerBase GetOverwrittenMoveFlying(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideMoveFlyingHooks.length; ++var2) {
            if (this.overrideMoveFlyingHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideMoveFlyingHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void onDeath(ClientPlayerEntity arg, DamageSource arg2) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().onDeath(arg2);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localOnDeath(arg2);
        }

    }

    protected void onDeath(DamageSource arg) {
        int var2;
        if (this.beforeOnDeathHooks != null) {
            for(var2 = this.beforeOnDeathHooks.length - 1; var2 >= 0; --var2) {
                this.beforeOnDeathHooks[var2].beforeOnDeath(arg);
            }
        }

        if (this.overrideOnDeathHooks != null) {
            this.overrideOnDeathHooks[this.overrideOnDeathHooks.length - 1].onDeath(arg);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localOnDeath(arg);
        }

        if (this.afterOnDeathHooks != null) {
            for(var2 = 0; var2 < this.afterOnDeathHooks.length; ++var2) {
                this.afterOnDeathHooks[var2].afterOnDeath(arg);
            }
        }

    }

    protected PlayerBase GetOverwrittenOnDeath(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideOnDeathHooks.length; ++var2) {
            if (this.overrideOnDeathHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideOnDeathHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void onLivingUpdate(ClientPlayerEntity arg) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().onLivingUpdate();
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localOnLivingUpdate();
        }

    }

    protected void onLivingUpdate() {
        int var1;
        if (this.beforeOnLivingUpdateHooks != null) {
            for(var1 = this.beforeOnLivingUpdateHooks.length - 1; var1 >= 0; --var1) {
                this.beforeOnLivingUpdateHooks[var1].beforeOnLivingUpdate();
            }
        }

        if (this.overrideOnLivingUpdateHooks != null) {
            this.overrideOnLivingUpdateHooks[this.overrideOnLivingUpdateHooks.length - 1].onLivingUpdate();
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localOnLivingUpdate();
        }

        if (this.afterOnLivingUpdateHooks != null) {
            for(var1 = 0; var1 < this.afterOnLivingUpdateHooks.length; ++var1) {
                this.afterOnLivingUpdateHooks[var1].afterOnLivingUpdate();
            }
        }

    }

    protected PlayerBase GetOverwrittenOnLivingUpdate(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideOnLivingUpdateHooks.length; ++var2) {
            if (this.overrideOnLivingUpdateHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideOnLivingUpdateHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void onKillEntity(ClientPlayerEntity arg, MobEntity arg2) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().onKillEntity(arg2);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localOnKillEntity(arg2);
        }

    }

    protected void onKillEntity(MobEntity arg) {
        int var2;
        if (this.beforeOnKillEntityHooks != null) {
            for(var2 = this.beforeOnKillEntityHooks.length - 1; var2 >= 0; --var2) {
                this.beforeOnKillEntityHooks[var2].beforeOnKillEntity(arg);
            }
        }

        if (this.overrideOnKillEntityHooks != null) {
            this.overrideOnKillEntityHooks[this.overrideOnKillEntityHooks.length - 1].onKillEntity(arg);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localOnKillEntity(arg);
        }

        if (this.afterOnKillEntityHooks != null) {
            for(var2 = 0; var2 < this.afterOnKillEntityHooks.length; ++var2) {
                this.afterOnKillEntityHooks[var2].afterOnKillEntity(arg);
            }
        }

    }

    protected PlayerBase GetOverwrittenOnKillEntity(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideOnKillEntityHooks.length; ++var2) {
            if (this.overrideOnKillEntityHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideOnKillEntityHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void onUpdate(ClientPlayerEntity arg) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().onUpdate();
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localOnUpdate();
        }

    }

    protected void onUpdate() {
        int var1;
        if (this.beforeOnUpdateHooks != null) {
            for(var1 = this.beforeOnUpdateHooks.length - 1; var1 >= 0; --var1) {
                this.beforeOnUpdateHooks[var1].beforeOnUpdate();
            }
        }

        if (this.overrideOnUpdateHooks != null) {
            this.overrideOnUpdateHooks[this.overrideOnUpdateHooks.length - 1].onUpdate();
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localOnUpdate();
        }

        if (this.afterOnUpdateHooks != null) {
            for(var1 = 0; var1 < this.afterOnUpdateHooks.length; ++var1) {
                this.afterOnUpdateHooks[var1].afterOnUpdate();
            }
        }

    }

    protected PlayerBase GetOverwrittenOnUpdate(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideOnUpdateHooks.length; ++var2) {
            if (this.overrideOnUpdateHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideOnUpdateHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void playStepSound(ClientPlayerEntity arg, int i, int j, int k, int l) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().playStepSound(i, j, k, l);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localPlayStepSound(i, j, k, l);
        }

    }

    protected void playStepSound(int i, int j, int k, int l) {
        int var5;
        if (this.beforePlayStepSoundHooks != null) {
            for(var5 = this.beforePlayStepSoundHooks.length - 1; var5 >= 0; --var5) {
                this.beforePlayStepSoundHooks[var5].beforePlayStepSound(i, j, k, l);
            }
        }

        if (this.overridePlayStepSoundHooks != null) {
            this.overridePlayStepSoundHooks[this.overridePlayStepSoundHooks.length - 1].playStepSound(i, j, k, l);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localPlayStepSound(i, j, k, l);
        }

        if (this.afterPlayStepSoundHooks != null) {
            for(var5 = 0; var5 < this.afterPlayStepSoundHooks.length; ++var5) {
                this.afterPlayStepSoundHooks[var5].afterPlayStepSound(i, j, k, l);
            }
        }

    }

    protected PlayerBase GetOverwrittenPlayStepSound(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overridePlayStepSoundHooks.length; ++var2) {
            if (this.overridePlayStepSoundHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overridePlayStepSoundHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static boolean pushOutOfBlocks(ClientPlayerEntity arg, double d, double e, double f) {
        boolean var7;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var7 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().pushOutOfBlocks(d, e, f);
        } else {
            var7 = ((IPlayerAPIClientPlayerEntity)arg).localPushOutOfBlocks(d, e, f);
        }

        return var7;
    }

    protected boolean pushOutOfBlocks(double d, double e, double f) {
        if (this.beforePushOutOfBlocksHooks != null) {
            for(int var7 = this.beforePushOutOfBlocksHooks.length - 1; var7 >= 0; --var7) {
                this.beforePushOutOfBlocksHooks[var7].beforePushOutOfBlocks(d, e, f);
            }
        }

        boolean var9;
        if (this.overridePushOutOfBlocksHooks != null) {
            var9 = this.overridePushOutOfBlocksHooks[this.overridePushOutOfBlocksHooks.length - 1].pushOutOfBlocks(d, e, f);
        } else {
            var9 = ((IPlayerAPIClientPlayerEntity)this.player).localPushOutOfBlocks(d, e, f);
        }

        if (this.afterPushOutOfBlocksHooks != null) {
            for(int var8 = 0; var8 < this.afterPushOutOfBlocksHooks.length; ++var8) {
                this.afterPushOutOfBlocksHooks[var8].afterPushOutOfBlocks(d, e, f);
            }
        }

        return var9;
    }

    protected PlayerBase GetOverwrittenPushOutOfBlocks(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overridePushOutOfBlocksHooks.length; ++var2) {
            if (this.overridePushOutOfBlocksHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overridePushOutOfBlocksHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static HitResult rayTrace(ClientPlayerEntity arg, double d, float f) {
        HitResult var4;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var4 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().rayTrace(d, f);
        } else {
            var4 = ((IPlayerAPIClientPlayerEntity)arg).localRayTrace(d, f);
        }

        return var4;
    }

    protected HitResult rayTrace(double d, float f) {
        if (this.beforeRayTraceHooks != null) {
            for(int var4 = this.beforeRayTraceHooks.length - 1; var4 >= 0; --var4) {
                this.beforeRayTraceHooks[var4].beforeRayTrace(d, f);
            }
        }

        HitResult var6;
        if (this.overrideRayTraceHooks != null) {
            var6 = this.overrideRayTraceHooks[this.overrideRayTraceHooks.length - 1].rayTrace(d, f);
        } else {
            var6 = ((IPlayerAPIClientPlayerEntity)this.player).localRayTrace(d, f);
        }

        if (this.afterRayTraceHooks != null) {
            for(int var5 = 0; var5 < this.afterRayTraceHooks.length; ++var5) {
                this.afterRayTraceHooks[var5].afterRayTrace(d, f);
            }
        }

        return var6;
    }

    protected PlayerBase GetOverwrittenRayTrace(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideRayTraceHooks.length; ++var2) {
            if (this.overrideRayTraceHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideRayTraceHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void readEntityFromNBT(ClientPlayerEntity arg, NbtCompound arg2) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().readEntityFromNBT(arg2);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localReadEntityFromNBT(arg2);
        }

    }

    protected void readEntityFromNBT(NbtCompound arg) {
        int var2;
        if (this.beforeReadEntityFromNBTHooks != null) {
            for(var2 = this.beforeReadEntityFromNBTHooks.length - 1; var2 >= 0; --var2) {
                this.beforeReadEntityFromNBTHooks[var2].beforeReadEntityFromNBT(arg);
            }
        }

        if (this.overrideReadEntityFromNBTHooks != null) {
            this.overrideReadEntityFromNBTHooks[this.overrideReadEntityFromNBTHooks.length - 1].readEntityFromNBT(arg);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localReadEntityFromNBT(arg);
        }

        if (this.afterReadEntityFromNBTHooks != null) {
            for(var2 = 0; var2 < this.afterReadEntityFromNBTHooks.length; ++var2) {
                this.afterReadEntityFromNBTHooks[var2].afterReadEntityFromNBT(arg);
            }
        }

    }

    protected PlayerBase GetOverwrittenReadEntityFromNBT(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideReadEntityFromNBTHooks.length; ++var2) {
            if (this.overrideReadEntityFromNBTHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideReadEntityFromNBTHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void respawnPlayer(ClientPlayerEntity arg) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().respawnPlayer();
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localRespawnPlayer();
        }

    }

    protected void respawnPlayer() {
        int var1;
        if (this.beforeRespawnPlayerHooks != null) {
            for(var1 = this.beforeRespawnPlayerHooks.length - 1; var1 >= 0; --var1) {
                this.beforeRespawnPlayerHooks[var1].beforeRespawnPlayer();
            }
        }

        if (this.overrideRespawnPlayerHooks != null) {
            this.overrideRespawnPlayerHooks[this.overrideRespawnPlayerHooks.length - 1].respawnPlayer();
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localRespawnPlayer();
        }

        if (this.afterRespawnPlayerHooks != null) {
            for(var1 = 0; var1 < this.afterRespawnPlayerHooks.length; ++var1) {
                this.afterRespawnPlayerHooks[var1].afterRespawnPlayer();
            }
        }

    }

    protected PlayerBase GetOverwrittenRespawnPlayer(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideRespawnPlayerHooks.length; ++var2) {
            if (this.overrideRespawnPlayerHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideRespawnPlayerHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void setDead(ClientPlayerEntity arg) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().setDead();
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localSetDead();
        }

    }

    protected void setDead() {
        int var1;
        if (this.beforeSetDeadHooks != null) {
            for(var1 = this.beforeSetDeadHooks.length - 1; var1 >= 0; --var1) {
                this.beforeSetDeadHooks[var1].beforeSetDead();
            }
        }

        if (this.overrideSetDeadHooks != null) {
            this.overrideSetDeadHooks[this.overrideSetDeadHooks.length - 1].setDead();
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localSetDead();
        }

        if (this.afterSetDeadHooks != null) {
            for(var1 = 0; var1 < this.afterSetDeadHooks.length; ++var1) {
                this.afterSetDeadHooks[var1].afterSetDead();
            }
        }

    }

    protected PlayerBase GetOverwrittenSetDead(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideSetDeadHooks.length; ++var2) {
            if (this.overrideSetDeadHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideSetDeadHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void setPositionAndRotation(ClientPlayerEntity arg, double d, double e, double f, float g, float h) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().setPositionAndRotation(d, e, f, g, h);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localSetPositionAndRotation(d, e, f, g, h);
        }

    }

    protected void setPositionAndRotation(double d, double e, double f, float g, float h) {
        int var9;
        if (this.beforeSetPositionAndRotationHooks != null) {
            for(var9 = this.beforeSetPositionAndRotationHooks.length - 1; var9 >= 0; --var9) {
                this.beforeSetPositionAndRotationHooks[var9].beforeSetPositionAndRotation(d, e, f, g, h);
            }
        }

        if (this.overrideSetPositionAndRotationHooks != null) {
            this.overrideSetPositionAndRotationHooks[this.overrideSetPositionAndRotationHooks.length - 1].setPositionAndRotation(d, e, f, g, h);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localSetPositionAndRotation(d, e, f, g, h);
        }

        if (this.afterSetPositionAndRotationHooks != null) {
            for(var9 = 0; var9 < this.afterSetPositionAndRotationHooks.length; ++var9) {
                this.afterSetPositionAndRotationHooks[var9].afterSetPositionAndRotation(d, e, f, g, h);
            }
        }

    }

    protected PlayerBase GetOverwrittenSetPositionAndRotation(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideSetPositionAndRotationHooks.length; ++var2) {
            if (this.overrideSetPositionAndRotationHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideSetPositionAndRotationHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static CanSleepEnum sleepInBedAt(ClientPlayerEntity arg, int i, int j, int k) {
        CanSleepEnum var4;
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            var4 = ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().sleepInBedAt(i, j, k);
        } else {
            var4 = ((IPlayerAPIClientPlayerEntity)arg).localSleepInBedAt(i, j, k);
        }

        return var4;
    }

    protected CanSleepEnum sleepInBedAt(int i, int j, int k) {
        if (this.beforeSleepInBedAtHooks != null) {
            for(int var4 = this.beforeSleepInBedAtHooks.length - 1; var4 >= 0; --var4) {
                this.beforeSleepInBedAtHooks[var4].beforeSleepInBedAt(i, j, k);
            }
        }

        CanSleepEnum var6;
        if (this.overrideSleepInBedAtHooks != null) {
            var6 = this.overrideSleepInBedAtHooks[this.overrideSleepInBedAtHooks.length - 1].sleepInBedAt(i, j, k);
        } else {
            var6 = ((IPlayerAPIClientPlayerEntity)this.player).localSleepInBedAt(i, j, k);
        }

        if (this.afterSleepInBedAtHooks != null) {
            for(int var5 = 0; var5 < this.afterSleepInBedAtHooks.length; ++var5) {
                this.afterSleepInBedAtHooks[var5].afterSleepInBedAt(i, j, k);
            }
        }

        return var6;
    }

    protected PlayerBase GetOverwrittenSleepInBedAt(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideSleepInBedAtHooks.length; ++var2) {
            if (this.overrideSleepInBedAtHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideSleepInBedAtHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void swingItem(ClientPlayerEntity arg) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().swingItem();
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localSwingItem();
        }

    }

    protected void swingItem() {
        int var1;
        if (this.beforeSwingItemHooks != null) {
            for(var1 = this.beforeSwingItemHooks.length - 1; var1 >= 0; --var1) {
                this.beforeSwingItemHooks[var1].beforeSwingItem();
            }
        }

        if (this.overrideSwingItemHooks != null) {
            this.overrideSwingItemHooks[this.overrideSwingItemHooks.length - 1].swingItem();
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localSwingItem();
        }

        if (this.afterSwingItemHooks != null) {
            for(var1 = 0; var1 < this.afterSwingItemHooks.length; ++var1) {
                this.afterSwingItemHooks[var1].afterSwingItem();
            }
        }

    }

    protected PlayerBase GetOverwrittenSwingItem(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideSwingItemHooks.length; ++var2) {
            if (this.overrideSwingItemHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideSwingItemHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void updateEntityActionState(ClientPlayerEntity arg) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().updateEntityActionState();
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localUpdateEntityActionState();
        }

    }

    protected void updateEntityActionState() {
        int var1;
        if (this.beforeUpdateEntityActionStateHooks != null) {
            for(var1 = this.beforeUpdateEntityActionStateHooks.length - 1; var1 >= 0; --var1) {
                this.beforeUpdateEntityActionStateHooks[var1].beforeUpdateEntityActionState();
            }
        }

        if (this.overrideUpdateEntityActionStateHooks != null) {
            this.overrideUpdateEntityActionStateHooks[this.overrideUpdateEntityActionStateHooks.length - 1].updateEntityActionState();
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localUpdateEntityActionState();
        }

        if (this.afterUpdateEntityActionStateHooks != null) {
            for(var1 = 0; var1 < this.afterUpdateEntityActionStateHooks.length; ++var1) {
                this.afterUpdateEntityActionStateHooks[var1].afterUpdateEntityActionState();
            }
        }

    }

    protected PlayerBase GetOverwrittenUpdateEntityActionState(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideUpdateEntityActionStateHooks.length; ++var2) {
            if (this.overrideUpdateEntityActionStateHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideUpdateEntityActionStateHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    public static void writeEntityToNBT(ClientPlayerEntity arg, NbtCompound arg2) {
        if (((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI() != null) {
            ((IPlayerAPIClientPlayerEntity)arg).getPlayerAPI().writeEntityToNBT(arg2);
        } else {
            ((IPlayerAPIClientPlayerEntity)arg).localWriteEntityToNBT(arg2);
        }

    }

    protected void writeEntityToNBT(NbtCompound arg) {
        int var2;
        if (this.beforeWriteEntityToNBTHooks != null) {
            for(var2 = this.beforeWriteEntityToNBTHooks.length - 1; var2 >= 0; --var2) {
                this.beforeWriteEntityToNBTHooks[var2].beforeWriteEntityToNBT(arg);
            }
        }

        if (this.overrideWriteEntityToNBTHooks != null) {
            this.overrideWriteEntityToNBTHooks[this.overrideWriteEntityToNBTHooks.length - 1].writeEntityToNBT(arg);
        } else {
            ((IPlayerAPIClientPlayerEntity)this.player).localWriteEntityToNBT(arg);
        }

        if (this.afterWriteEntityToNBTHooks != null) {
            for(var2 = 0; var2 < this.afterWriteEntityToNBTHooks.length; ++var2) {
                this.afterWriteEntityToNBTHooks[var2].afterWriteEntityToNBT(arg);
            }
        }

    }

    protected PlayerBase GetOverwrittenWriteEntityToNBT(PlayerBase playerBase) {
        for(int var2 = 0; var2 < this.overrideWriteEntityToNBTHooks.length; ++var2) {
            if (this.overrideWriteEntityToNBTHooks[var2] == playerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideWriteEntityToNBTHooks[var2 - 1];
            }
        }

        return playerBase;
    }

    static {
        unmodifiableAllIds = Collections.unmodifiableSet(allBaseConstructors.keySet());
        allBaseBeforeSuperiors = new Hashtable();
        allBaseBeforeInferiors = new Hashtable();
        allBaseOverrideSuperiors = new Hashtable();
        allBaseOverrideInferiors = new Hashtable();
        allBaseAfterSuperiors = new Hashtable();
        allBaseAfterInferiors = new Hashtable();
        initialized = false;
    }
}
