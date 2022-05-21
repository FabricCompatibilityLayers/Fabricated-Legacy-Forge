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

public final class ServerPlayerAPI {
    private static final Class<?>[] Class = new Class[]{ServerPlayerAPI.class};
    private static final Class<?>[] Classes = new Class[]{ServerPlayerAPI.class, String.class};
    private static boolean isCreated;
    private static final Logger logger = Logger.getLogger("ServerPlayerAPI");
    private static final List<String> beforeAttackEntityFromHookTypes = new LinkedList();
    private static final List<String> overrideAttackEntityFromHookTypes = new LinkedList();
    private static final List<String> afterAttackEntityFromHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeAttackEntityFromHooks;
    private final ServerPlayerBase[] overrideAttackEntityFromHooks;
    private final ServerPlayerBase[] afterAttackEntityFromHooks;
    public final boolean isAttackEntityFromModded;
    private static final List<String> beforeAttackTargetEntityWithCurrentItemHookTypes = new LinkedList();
    private static final List<String> overrideAttackTargetEntityWithCurrentItemHookTypes = new LinkedList();
    private static final List<String> afterAttackTargetEntityWithCurrentItemHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeAttackTargetEntityWithCurrentItemHooks;
    private final ServerPlayerBase[] overrideAttackTargetEntityWithCurrentItemHooks;
    private final ServerPlayerBase[] afterAttackTargetEntityWithCurrentItemHooks;
    public final boolean isAttackTargetEntityWithCurrentItemModded;
    private static final List<String> beforeCanHarvestBlockHookTypes = new LinkedList();
    private static final List<String> overrideCanHarvestBlockHookTypes = new LinkedList();
    private static final List<String> afterCanHarvestBlockHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeCanHarvestBlockHooks;
    private final ServerPlayerBase[] overrideCanHarvestBlockHooks;
    private final ServerPlayerBase[] afterCanHarvestBlockHooks;
    public final boolean isCanHarvestBlockModded;
    private static final List<String> beforeCanPlayerEditHookTypes = new LinkedList();
    private static final List<String> overrideCanPlayerEditHookTypes = new LinkedList();
    private static final List<String> afterCanPlayerEditHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeCanPlayerEditHooks;
    private final ServerPlayerBase[] overrideCanPlayerEditHooks;
    private final ServerPlayerBase[] afterCanPlayerEditHooks;
    public final boolean isCanPlayerEditModded;
    private static final List<String> beforeCanTriggerWalkingHookTypes = new LinkedList();
    private static final List<String> overrideCanTriggerWalkingHookTypes = new LinkedList();
    private static final List<String> afterCanTriggerWalkingHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeCanTriggerWalkingHooks;
    private final ServerPlayerBase[] overrideCanTriggerWalkingHooks;
    private final ServerPlayerBase[] afterCanTriggerWalkingHooks;
    public final boolean isCanTriggerWalkingModded;
    private static final List<String> beforeDamageEntityHookTypes = new LinkedList();
    private static final List<String> overrideDamageEntityHookTypes = new LinkedList();
    private static final List<String> afterDamageEntityHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeDamageEntityHooks;
    private final ServerPlayerBase[] overrideDamageEntityHooks;
    private final ServerPlayerBase[] afterDamageEntityHooks;
    public final boolean isDamageEntityModded;
    private static final List<String> beforeDisplayGUIChestHookTypes = new LinkedList();
    private static final List<String> overrideDisplayGUIChestHookTypes = new LinkedList();
    private static final List<String> afterDisplayGUIChestHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeDisplayGUIChestHooks;
    private final ServerPlayerBase[] overrideDisplayGUIChestHooks;
    private final ServerPlayerBase[] afterDisplayGUIChestHooks;
    public final boolean isDisplayGUIChestModded;
    private static final List<String> beforeDisplayGUIDispenserHookTypes = new LinkedList();
    private static final List<String> overrideDisplayGUIDispenserHookTypes = new LinkedList();
    private static final List<String> afterDisplayGUIDispenserHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeDisplayGUIDispenserHooks;
    private final ServerPlayerBase[] overrideDisplayGUIDispenserHooks;
    private final ServerPlayerBase[] afterDisplayGUIDispenserHooks;
    public final boolean isDisplayGUIDispenserModded;
    private static final List<String> beforeDisplayGUIFurnaceHookTypes = new LinkedList();
    private static final List<String> overrideDisplayGUIFurnaceHookTypes = new LinkedList();
    private static final List<String> afterDisplayGUIFurnaceHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeDisplayGUIFurnaceHooks;
    private final ServerPlayerBase[] overrideDisplayGUIFurnaceHooks;
    private final ServerPlayerBase[] afterDisplayGUIFurnaceHooks;
    public final boolean isDisplayGUIFurnaceModded;
    private static final List<String> beforeDisplayWorkbenchGUIHookTypes = new LinkedList();
    private static final List<String> overrideDisplayWorkbenchGUIHookTypes = new LinkedList();
    private static final List<String> afterDisplayWorkbenchGUIHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeDisplayWorkbenchGUIHooks;
    private final ServerPlayerBase[] overrideDisplayWorkbenchGUIHooks;
    private final ServerPlayerBase[] afterDisplayWorkbenchGUIHooks;
    public final boolean isDisplayWorkbenchGUIModded;
    private static final List<String> beforeDropOneItemHookTypes = new LinkedList();
    private static final List<String> overrideDropOneItemHookTypes = new LinkedList();
    private static final List<String> afterDropOneItemHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeDropOneItemHooks;
    private final ServerPlayerBase[] overrideDropOneItemHooks;
    private final ServerPlayerBase[] afterDropOneItemHooks;
    public final boolean isDropOneItemModded;
    private static final List<String> beforeDropPlayerItemHookTypes = new LinkedList();
    private static final List<String> overrideDropPlayerItemHookTypes = new LinkedList();
    private static final List<String> afterDropPlayerItemHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeDropPlayerItemHooks;
    private final ServerPlayerBase[] overrideDropPlayerItemHooks;
    private final ServerPlayerBase[] afterDropPlayerItemHooks;
    public final boolean isDropPlayerItemModded;
    private static final List<String> beforeFallHookTypes = new LinkedList();
    private static final List<String> overrideFallHookTypes = new LinkedList();
    private static final List<String> afterFallHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeFallHooks;
    private final ServerPlayerBase[] overrideFallHooks;
    private final ServerPlayerBase[] afterFallHooks;
    public final boolean isFallModded;
    private static final List<String> beforeGetCurrentPlayerStrVsBlockHookTypes = new LinkedList();
    private static final List<String> overrideGetCurrentPlayerStrVsBlockHookTypes = new LinkedList();
    private static final List<String> afterGetCurrentPlayerStrVsBlockHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeGetCurrentPlayerStrVsBlockHooks;
    private final ServerPlayerBase[] overrideGetCurrentPlayerStrVsBlockHooks;
    private final ServerPlayerBase[] afterGetCurrentPlayerStrVsBlockHooks;
    public final boolean isGetCurrentPlayerStrVsBlockModded;
    private static final List<String> beforeGetDistanceSqHookTypes = new LinkedList();
    private static final List<String> overrideGetDistanceSqHookTypes = new LinkedList();
    private static final List<String> afterGetDistanceSqHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeGetDistanceSqHooks;
    private final ServerPlayerBase[] overrideGetDistanceSqHooks;
    private final ServerPlayerBase[] afterGetDistanceSqHooks;
    public final boolean isGetDistanceSqModded;
    private static final List<String> beforeGetBrightnessHookTypes = new LinkedList();
    private static final List<String> overrideGetBrightnessHookTypes = new LinkedList();
    private static final List<String> afterGetBrightnessHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeGetBrightnessHooks;
    private final ServerPlayerBase[] overrideGetBrightnessHooks;
    private final ServerPlayerBase[] afterGetBrightnessHooks;
    public final boolean isGetBrightnessModded;
    private static final List<String> beforeGetEyeHeightHookTypes = new LinkedList();
    private static final List<String> overrideGetEyeHeightHookTypes = new LinkedList();
    private static final List<String> afterGetEyeHeightHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeGetEyeHeightHooks;
    private final ServerPlayerBase[] overrideGetEyeHeightHooks;
    private final ServerPlayerBase[] afterGetEyeHeightHooks;
    public final boolean isGetEyeHeightModded;
    private static final List<String> beforeGetSpeedModifierHookTypes = new LinkedList();
    private static final List<String> overrideGetSpeedModifierHookTypes = new LinkedList();
    private static final List<String> afterGetSpeedModifierHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeGetSpeedModifierHooks;
    private final ServerPlayerBase[] overrideGetSpeedModifierHooks;
    private final ServerPlayerBase[] afterGetSpeedModifierHooks;
    public final boolean isGetSpeedModifierModded;
    private static final List<String> beforeHealHookTypes = new LinkedList();
    private static final List<String> overrideHealHookTypes = new LinkedList();
    private static final List<String> afterHealHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeHealHooks;
    private final ServerPlayerBase[] overrideHealHooks;
    private final ServerPlayerBase[] afterHealHooks;
    public final boolean isHealModded;
    private static final List<String> beforeInteractHookTypes = new LinkedList();
    private static final List<String> overrideInteractHookTypes = new LinkedList();
    private static final List<String> afterInteractHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeInteractHooks;
    private final ServerPlayerBase[] overrideInteractHooks;
    private final ServerPlayerBase[] afterInteractHooks;
    public final boolean isInteractModded;
    private static final List<String> beforeIsEntityInsideOpaqueBlockHookTypes = new LinkedList();
    private static final List<String> overrideIsEntityInsideOpaqueBlockHookTypes = new LinkedList();
    private static final List<String> afterIsEntityInsideOpaqueBlockHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeIsEntityInsideOpaqueBlockHooks;
    private final ServerPlayerBase[] overrideIsEntityInsideOpaqueBlockHooks;
    private final ServerPlayerBase[] afterIsEntityInsideOpaqueBlockHooks;
    public final boolean isIsEntityInsideOpaqueBlockModded;
    private static final List<String> beforeIsInWaterHookTypes = new LinkedList();
    private static final List<String> overrideIsInWaterHookTypes = new LinkedList();
    private static final List<String> afterIsInWaterHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeIsInWaterHooks;
    private final ServerPlayerBase[] overrideIsInWaterHooks;
    private final ServerPlayerBase[] afterIsInWaterHooks;
    public final boolean isIsInWaterModded;
    private static final List<String> beforeIsInsideOfMaterialHookTypes = new LinkedList();
    private static final List<String> overrideIsInsideOfMaterialHookTypes = new LinkedList();
    private static final List<String> afterIsInsideOfMaterialHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeIsInsideOfMaterialHooks;
    private final ServerPlayerBase[] overrideIsInsideOfMaterialHooks;
    private final ServerPlayerBase[] afterIsInsideOfMaterialHooks;
    public final boolean isIsInsideOfMaterialModded;
    private static final List<String> beforeIsOnLadderHookTypes = new LinkedList();
    private static final List<String> overrideIsOnLadderHookTypes = new LinkedList();
    private static final List<String> afterIsOnLadderHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeIsOnLadderHooks;
    private final ServerPlayerBase[] overrideIsOnLadderHooks;
    private final ServerPlayerBase[] afterIsOnLadderHooks;
    public final boolean isIsOnLadderModded;
    private static final List<String> beforeIsPlayerSleepingHookTypes = new LinkedList();
    private static final List<String> overrideIsPlayerSleepingHookTypes = new LinkedList();
    private static final List<String> afterIsPlayerSleepingHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeIsPlayerSleepingHooks;
    private final ServerPlayerBase[] overrideIsPlayerSleepingHooks;
    private final ServerPlayerBase[] afterIsPlayerSleepingHooks;
    public final boolean isIsPlayerSleepingModded;
    private static final List<String> beforeJumpHookTypes = new LinkedList();
    private static final List<String> overrideJumpHookTypes = new LinkedList();
    private static final List<String> afterJumpHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeJumpHooks;
    private final ServerPlayerBase[] overrideJumpHooks;
    private final ServerPlayerBase[] afterJumpHooks;
    public final boolean isJumpModded;
    private static final List<String> beforeMoveEntityHookTypes = new LinkedList();
    private static final List<String> overrideMoveEntityHookTypes = new LinkedList();
    private static final List<String> afterMoveEntityHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeMoveEntityHooks;
    private final ServerPlayerBase[] overrideMoveEntityHooks;
    private final ServerPlayerBase[] afterMoveEntityHooks;
    public final boolean isMoveEntityModded;
    private static final List<String> beforeMoveEntityWithHeadingHookTypes = new LinkedList();
    private static final List<String> overrideMoveEntityWithHeadingHookTypes = new LinkedList();
    private static final List<String> afterMoveEntityWithHeadingHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeMoveEntityWithHeadingHooks;
    private final ServerPlayerBase[] overrideMoveEntityWithHeadingHooks;
    private final ServerPlayerBase[] afterMoveEntityWithHeadingHooks;
    public final boolean isMoveEntityWithHeadingModded;
    private static final List<String> beforeMoveFlyingHookTypes = new LinkedList();
    private static final List<String> overrideMoveFlyingHookTypes = new LinkedList();
    private static final List<String> afterMoveFlyingHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeMoveFlyingHooks;
    private final ServerPlayerBase[] overrideMoveFlyingHooks;
    private final ServerPlayerBase[] afterMoveFlyingHooks;
    public final boolean isMoveFlyingModded;
    private static final List<String> beforeOnDeathHookTypes = new LinkedList();
    private static final List<String> overrideOnDeathHookTypes = new LinkedList();
    private static final List<String> afterOnDeathHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeOnDeathHooks;
    private final ServerPlayerBase[] overrideOnDeathHooks;
    private final ServerPlayerBase[] afterOnDeathHooks;
    public final boolean isOnDeathModded;
    private static final List<String> beforeOnLivingUpdateHookTypes = new LinkedList();
    private static final List<String> overrideOnLivingUpdateHookTypes = new LinkedList();
    private static final List<String> afterOnLivingUpdateHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeOnLivingUpdateHooks;
    private final ServerPlayerBase[] overrideOnLivingUpdateHooks;
    private final ServerPlayerBase[] afterOnLivingUpdateHooks;
    public final boolean isOnLivingUpdateModded;
    private static final List<String> beforeOnKillEntityHookTypes = new LinkedList();
    private static final List<String> overrideOnKillEntityHookTypes = new LinkedList();
    private static final List<String> afterOnKillEntityHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeOnKillEntityHooks;
    private final ServerPlayerBase[] overrideOnKillEntityHooks;
    private final ServerPlayerBase[] afterOnKillEntityHooks;
    public final boolean isOnKillEntityModded;
    private static final List<String> beforeOnUpdateHookTypes = new LinkedList();
    private static final List<String> overrideOnUpdateHookTypes = new LinkedList();
    private static final List<String> afterOnUpdateHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeOnUpdateHooks;
    private final ServerPlayerBase[] overrideOnUpdateHooks;
    private final ServerPlayerBase[] afterOnUpdateHooks;
    public final boolean isOnUpdateModded;
    private static final List<String> beforeOnUpdateEntityHookTypes = new LinkedList();
    private static final List<String> overrideOnUpdateEntityHookTypes = new LinkedList();
    private static final List<String> afterOnUpdateEntityHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeOnUpdateEntityHooks;
    private final ServerPlayerBase[] overrideOnUpdateEntityHooks;
    private final ServerPlayerBase[] afterOnUpdateEntityHooks;
    public final boolean isOnUpdateEntityModded;
    private static final List<String> beforeReadEntityFromNBTHookTypes = new LinkedList();
    private static final List<String> overrideReadEntityFromNBTHookTypes = new LinkedList();
    private static final List<String> afterReadEntityFromNBTHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeReadEntityFromNBTHooks;
    private final ServerPlayerBase[] overrideReadEntityFromNBTHooks;
    private final ServerPlayerBase[] afterReadEntityFromNBTHooks;
    public final boolean isReadEntityFromNBTModded;
    private static final List<String> beforeSetDeadHookTypes = new LinkedList();
    private static final List<String> overrideSetDeadHookTypes = new LinkedList();
    private static final List<String> afterSetDeadHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeSetDeadHooks;
    private final ServerPlayerBase[] overrideSetDeadHooks;
    private final ServerPlayerBase[] afterSetDeadHooks;
    public final boolean isSetDeadModded;
    private static final List<String> beforeSetPositionHookTypes = new LinkedList();
    private static final List<String> overrideSetPositionHookTypes = new LinkedList();
    private static final List<String> afterSetPositionHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeSetPositionHooks;
    private final ServerPlayerBase[] overrideSetPositionHooks;
    private final ServerPlayerBase[] afterSetPositionHooks;
    public final boolean isSetPositionModded;
    private static final List<String> beforeUpdateEntityActionStateHookTypes = new LinkedList();
    private static final List<String> overrideUpdateEntityActionStateHookTypes = new LinkedList();
    private static final List<String> afterUpdateEntityActionStateHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeUpdateEntityActionStateHooks;
    private final ServerPlayerBase[] overrideUpdateEntityActionStateHooks;
    private final ServerPlayerBase[] afterUpdateEntityActionStateHooks;
    public final boolean isUpdateEntityActionStateModded;
    private static final List<String> beforeWriteEntityToNBTHookTypes = new LinkedList();
    private static final List<String> overrideWriteEntityToNBTHookTypes = new LinkedList();
    private static final List<String> afterWriteEntityToNBTHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeWriteEntityToNBTHooks;
    private final ServerPlayerBase[] overrideWriteEntityToNBTHooks;
    private final ServerPlayerBase[] afterWriteEntityToNBTHooks;
    public final boolean isWriteEntityToNBTModded;
    protected final ServerPlayerEntity player;
    private static final List<String> beforeLocalConstructingHookTypes = new LinkedList();
    private static final List<String> afterLocalConstructingHookTypes = new LinkedList();
    private final ServerPlayerBase[] beforeLocalConstructingHooks;
    private final ServerPlayerBase[] afterLocalConstructingHooks;
    private final Map<String, ServerPlayerBase> allBaseObjects = new Hashtable();
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
        register((String)string, (Class)class_, (ServerPlayerBaseSorting)null);
    }

    public static void register(String string, Class<?> class_, ServerPlayerBaseSorting serverPlayerBaseSorting) {
        try {
            register(class_, string, serverPlayerBaseSorting);
        } catch (RuntimeException var4) {
            if (string != null) {
                log("ServerPlayerAPI: failed to register id '" + string + "'");
            } else {
                log("ServerPlayerAPI: failed to register ServerPlayerBase");
            }

            throw var4;
        }
    }

    private static void register(Class<?> class_, String string, ServerPlayerBaseSorting serverPlayerBaseSorting) {
        if (!isCreated) {
            log("ServerPlayerAPI 1.1 Created");
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
                        throw new IllegalArgumentException("Can not find necessary constructor with one argument of type '" + ServerPlayerAPI.class.getName() + "' and eventually a second argument of type 'String' in the class '" + class_.getName() + "'", var8);
                    }
                }

                allBaseConstructors.put(string, var4);
                if (serverPlayerBaseSorting != null) {
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeLocalConstructingSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeLocalConstructingInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterLocalConstructingSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterLocalConstructingInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeAttackEntityFromSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeAttackEntityFromInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideAttackEntityFromSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideAttackEntityFromInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterAttackEntityFromSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterAttackEntityFromInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeAttackTargetEntityWithCurrentItemSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeAttackTargetEntityWithCurrentItemInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideAttackTargetEntityWithCurrentItemSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideAttackTargetEntityWithCurrentItemInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterAttackTargetEntityWithCurrentItemSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterAttackTargetEntityWithCurrentItemInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeCanHarvestBlockSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeCanHarvestBlockInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideCanHarvestBlockSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideCanHarvestBlockInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterCanHarvestBlockSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterCanHarvestBlockInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeCanPlayerEditSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeCanPlayerEditInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideCanPlayerEditSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideCanPlayerEditInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterCanPlayerEditSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterCanPlayerEditInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeCanTriggerWalkingSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeCanTriggerWalkingInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideCanTriggerWalkingSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideCanTriggerWalkingInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterCanTriggerWalkingSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterCanTriggerWalkingInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeDamageEntitySuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeDamageEntityInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideDamageEntitySuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideDamageEntityInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterDamageEntitySuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterDamageEntityInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeDisplayGUIChestSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeDisplayGUIChestInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideDisplayGUIChestSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideDisplayGUIChestInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterDisplayGUIChestSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterDisplayGUIChestInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeDisplayGUIDispenserSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeDisplayGUIDispenserInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideDisplayGUIDispenserSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideDisplayGUIDispenserInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterDisplayGUIDispenserSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterDisplayGUIDispenserInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeDisplayGUIFurnaceSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeDisplayGUIFurnaceInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideDisplayGUIFurnaceSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideDisplayGUIFurnaceInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterDisplayGUIFurnaceSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterDisplayGUIFurnaceInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeDisplayWorkbenchGUISuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeDisplayWorkbenchGUIInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideDisplayWorkbenchGUISuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideDisplayWorkbenchGUIInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterDisplayWorkbenchGUISuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterDisplayWorkbenchGUIInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeDropOneItemSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeDropOneItemInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideDropOneItemSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideDropOneItemInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterDropOneItemSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterDropOneItemInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeDropPlayerItemSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeDropPlayerItemInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideDropPlayerItemSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideDropPlayerItemInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterDropPlayerItemSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterDropPlayerItemInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeFallSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeFallInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideFallSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideFallInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterFallSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterFallInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeGetCurrentPlayerStrVsBlockSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeGetCurrentPlayerStrVsBlockInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideGetCurrentPlayerStrVsBlockSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideGetCurrentPlayerStrVsBlockInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterGetCurrentPlayerStrVsBlockSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterGetCurrentPlayerStrVsBlockInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeGetDistanceSqSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeGetDistanceSqInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideGetDistanceSqSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideGetDistanceSqInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterGetDistanceSqSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterGetDistanceSqInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeGetBrightnessSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeGetBrightnessInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideGetBrightnessSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideGetBrightnessInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterGetBrightnessSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterGetBrightnessInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeGetEyeHeightSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeGetEyeHeightInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideGetEyeHeightSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideGetEyeHeightInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterGetEyeHeightSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterGetEyeHeightInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeGetSpeedModifierSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeGetSpeedModifierInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideGetSpeedModifierSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideGetSpeedModifierInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterGetSpeedModifierSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterGetSpeedModifierInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeHealSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeHealInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideHealSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideHealInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterHealSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterHealInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeInteractSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeInteractInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideInteractSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideInteractInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterInteractSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterInteractInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeIsEntityInsideOpaqueBlockSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeIsEntityInsideOpaqueBlockInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideIsEntityInsideOpaqueBlockSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideIsEntityInsideOpaqueBlockInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterIsEntityInsideOpaqueBlockSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterIsEntityInsideOpaqueBlockInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeIsInWaterSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeIsInWaterInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideIsInWaterSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideIsInWaterInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterIsInWaterSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterIsInWaterInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeIsInsideOfMaterialSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeIsInsideOfMaterialInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideIsInsideOfMaterialSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideIsInsideOfMaterialInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterIsInsideOfMaterialSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterIsInsideOfMaterialInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeIsOnLadderSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeIsOnLadderInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideIsOnLadderSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideIsOnLadderInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterIsOnLadderSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterIsOnLadderInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeIsPlayerSleepingSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeIsPlayerSleepingInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideIsPlayerSleepingSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideIsPlayerSleepingInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterIsPlayerSleepingSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterIsPlayerSleepingInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeJumpSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeJumpInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideJumpSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideJumpInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterJumpSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterJumpInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeMoveEntitySuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeMoveEntityInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideMoveEntitySuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideMoveEntityInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterMoveEntitySuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterMoveEntityInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeMoveEntityWithHeadingSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeMoveEntityWithHeadingInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideMoveEntityWithHeadingSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideMoveEntityWithHeadingInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterMoveEntityWithHeadingSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterMoveEntityWithHeadingInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeMoveFlyingSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeMoveFlyingInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideMoveFlyingSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideMoveFlyingInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterMoveFlyingSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterMoveFlyingInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeOnDeathSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeOnDeathInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideOnDeathSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideOnDeathInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterOnDeathSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterOnDeathInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeOnLivingUpdateSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeOnLivingUpdateInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideOnLivingUpdateSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideOnLivingUpdateInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterOnLivingUpdateSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterOnLivingUpdateInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeOnKillEntitySuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeOnKillEntityInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideOnKillEntitySuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideOnKillEntityInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterOnKillEntitySuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterOnKillEntityInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeOnUpdateSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeOnUpdateInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideOnUpdateSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideOnUpdateInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterOnUpdateSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterOnUpdateInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeOnUpdateEntitySuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeOnUpdateEntityInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideOnUpdateEntitySuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideOnUpdateEntityInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterOnUpdateEntitySuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterOnUpdateEntityInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeReadEntityFromNBTSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeReadEntityFromNBTInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideReadEntityFromNBTSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideReadEntityFromNBTInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterReadEntityFromNBTSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterReadEntityFromNBTInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeSetDeadSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeSetDeadInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideSetDeadSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideSetDeadInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterSetDeadSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterSetDeadInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeSetPositionSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeSetPositionInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideSetPositionSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideSetPositionInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterSetPositionSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterSetPositionInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeUpdateEntityActionStateSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeUpdateEntityActionStateInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideUpdateEntityActionStateSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideUpdateEntityActionStateInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterUpdateEntityActionStateSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterUpdateEntityActionStateInferiors());
                    addSorting(string, allBaseBeforeSuperiors, serverPlayerBaseSorting.getBeforeWriteEntityToNBTSuperiors());
                    addSorting(string, allBaseBeforeInferiors, serverPlayerBaseSorting.getBeforeWriteEntityToNBTInferiors());
                    addSorting(string, allBaseOverrideSuperiors, serverPlayerBaseSorting.getOverrideWriteEntityToNBTSuperiors());
                    addSorting(string, allBaseOverrideInferiors, serverPlayerBaseSorting.getOverrideWriteEntityToNBTInferiors());
                    addSorting(string, allBaseAfterSuperiors, serverPlayerBaseSorting.getAfterWriteEntityToNBTSuperiors());
                    addSorting(string, allBaseAfterInferiors, serverPlayerBaseSorting.getAfterWriteEntityToNBTInferiors());
                }

                addMethod(string, class_, beforeLocalConstructingHookTypes, "beforeLocalConstructing", MinecraftServer.class, World.class, String.class, ServerPlayerInteractionManager.class);
                addMethod(string, class_, afterLocalConstructingHookTypes, "afterLocalConstructing", MinecraftServer.class, World.class, String.class, ServerPlayerInteractionManager.class);
                addMethod(string, class_, beforeAttackEntityFromHookTypes, "beforeAttackEntityFrom", DamageSource.class, Integer.TYPE);
                addMethod(string, class_, overrideAttackEntityFromHookTypes, "attackEntityFrom", DamageSource.class, Integer.TYPE);
                addMethod(string, class_, afterAttackEntityFromHookTypes, "afterAttackEntityFrom", DamageSource.class, Integer.TYPE);
                addMethod(string, class_, beforeAttackTargetEntityWithCurrentItemHookTypes, "beforeAttackTargetEntityWithCurrentItem", Entity.class);
                addMethod(string, class_, overrideAttackTargetEntityWithCurrentItemHookTypes, "attackTargetEntityWithCurrentItem", Entity.class);
                addMethod(string, class_, afterAttackTargetEntityWithCurrentItemHookTypes, "afterAttackTargetEntityWithCurrentItem", Entity.class);
                addMethod(string, class_, beforeCanHarvestBlockHookTypes, "beforeCanHarvestBlock", Block.class);
                addMethod(string, class_, overrideCanHarvestBlockHookTypes, "canHarvestBlock", Block.class);
                addMethod(string, class_, afterCanHarvestBlockHookTypes, "afterCanHarvestBlock", Block.class);
                addMethod(string, class_, beforeCanPlayerEditHookTypes, "beforeCanPlayerEdit", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, overrideCanPlayerEditHookTypes, "canPlayerEdit", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, afterCanPlayerEditHookTypes, "afterCanPlayerEdit", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                addMethod(string, class_, beforeCanTriggerWalkingHookTypes, "beforeCanTriggerWalking");
                addMethod(string, class_, overrideCanTriggerWalkingHookTypes, "canTriggerWalking");
                addMethod(string, class_, afterCanTriggerWalkingHookTypes, "afterCanTriggerWalking");
                addMethod(string, class_, beforeDamageEntityHookTypes, "beforeDamageEntity", DamageSource.class, Integer.TYPE);
                addMethod(string, class_, overrideDamageEntityHookTypes, "damageEntity", DamageSource.class, Integer.TYPE);
                addMethod(string, class_, afterDamageEntityHookTypes, "afterDamageEntity", DamageSource.class, Integer.TYPE);
                addMethod(string, class_, beforeDisplayGUIChestHookTypes, "beforeDisplayGUIChest", Inventory.class);
                addMethod(string, class_, overrideDisplayGUIChestHookTypes, "displayGUIChest", Inventory.class);
                addMethod(string, class_, afterDisplayGUIChestHookTypes, "afterDisplayGUIChest", Inventory.class);
                addMethod(string, class_, beforeDisplayGUIDispenserHookTypes, "beforeDisplayGUIDispenser", DispenserBlockEntity.class);
                addMethod(string, class_, overrideDisplayGUIDispenserHookTypes, "displayGUIDispenser", DispenserBlockEntity.class);
                addMethod(string, class_, afterDisplayGUIDispenserHookTypes, "afterDisplayGUIDispenser", DispenserBlockEntity.class);
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
                addMethod(string, class_, beforeFallHookTypes, "beforeFall", Float.TYPE);
                addMethod(string, class_, overrideFallHookTypes, "fall", Float.TYPE);
                addMethod(string, class_, afterFallHookTypes, "afterFall", Float.TYPE);
                addMethod(string, class_, beforeGetCurrentPlayerStrVsBlockHookTypes, "beforeGetCurrentPlayerStrVsBlock", Block.class);
                addMethod(string, class_, overrideGetCurrentPlayerStrVsBlockHookTypes, "getCurrentPlayerStrVsBlock", Block.class);
                addMethod(string, class_, afterGetCurrentPlayerStrVsBlockHookTypes, "afterGetCurrentPlayerStrVsBlock", Block.class);
                addMethod(string, class_, beforeGetDistanceSqHookTypes, "beforeGetDistanceSq", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, overrideGetDistanceSqHookTypes, "getDistanceSq", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, afterGetDistanceSqHookTypes, "afterGetDistanceSq", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, beforeGetBrightnessHookTypes, "beforeGetBrightness", Float.TYPE);
                addMethod(string, class_, overrideGetBrightnessHookTypes, "getBrightness", Float.TYPE);
                addMethod(string, class_, afterGetBrightnessHookTypes, "afterGetBrightness", Float.TYPE);
                addMethod(string, class_, beforeGetEyeHeightHookTypes, "beforeGetEyeHeight");
                addMethod(string, class_, overrideGetEyeHeightHookTypes, "getEyeHeight");
                addMethod(string, class_, afterGetEyeHeightHookTypes, "afterGetEyeHeight");
                addMethod(string, class_, beforeGetSpeedModifierHookTypes, "beforeGetSpeedModifier");
                addMethod(string, class_, overrideGetSpeedModifierHookTypes, "getSpeedModifier");
                addMethod(string, class_, afterGetSpeedModifierHookTypes, "afterGetSpeedModifier");
                addMethod(string, class_, beforeHealHookTypes, "beforeHeal", Integer.TYPE);
                addMethod(string, class_, overrideHealHookTypes, "heal", Integer.TYPE);
                addMethod(string, class_, afterHealHookTypes, "afterHeal", Integer.TYPE);
                addMethod(string, class_, beforeInteractHookTypes, "beforeInteract", PlayerEntity.class);
                addMethod(string, class_, overrideInteractHookTypes, "interact", PlayerEntity.class);
                addMethod(string, class_, afterInteractHookTypes, "afterInteract", PlayerEntity.class);
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
                addMethod(string, class_, beforeIsPlayerSleepingHookTypes, "beforeIsPlayerSleeping");
                addMethod(string, class_, overrideIsPlayerSleepingHookTypes, "isPlayerSleeping");
                addMethod(string, class_, afterIsPlayerSleepingHookTypes, "afterIsPlayerSleeping");
                addMethod(string, class_, beforeJumpHookTypes, "beforeJump");
                addMethod(string, class_, overrideJumpHookTypes, "jump");
                addMethod(string, class_, afterJumpHookTypes, "afterJump");
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
                addMethod(string, class_, beforeOnUpdateEntityHookTypes, "beforeOnUpdateEntity");
                addMethod(string, class_, overrideOnUpdateEntityHookTypes, "onUpdateEntity");
                addMethod(string, class_, afterOnUpdateEntityHookTypes, "afterOnUpdateEntity");
                addMethod(string, class_, beforeReadEntityFromNBTHookTypes, "beforeReadEntityFromNBT", NbtCompound.class);
                addMethod(string, class_, overrideReadEntityFromNBTHookTypes, "readEntityFromNBT", NbtCompound.class);
                addMethod(string, class_, afterReadEntityFromNBTHookTypes, "afterReadEntityFromNBT", NbtCompound.class);
                addMethod(string, class_, beforeSetDeadHookTypes, "beforeSetDead");
                addMethod(string, class_, overrideSetDeadHookTypes, "setDead");
                addMethod(string, class_, afterSetDeadHookTypes, "afterSetDead");
                addMethod(string, class_, beforeSetPositionHookTypes, "beforeSetPosition", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, overrideSetPositionHookTypes, "setPosition", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, afterSetPositionHookTypes, "afterSetPosition", Double.TYPE, Double.TYPE, Double.TYPE);
                addMethod(string, class_, beforeUpdateEntityActionStateHookTypes, "beforeUpdateEntityActionState");
                addMethod(string, class_, overrideUpdateEntityActionStateHookTypes, "updateEntityActionState");
                addMethod(string, class_, afterUpdateEntityActionStateHookTypes, "afterUpdateEntityActionState");
                addMethod(string, class_, beforeWriteEntityToNBTHookTypes, "beforeWriteEntityToNBT", NbtCompound.class);
                addMethod(string, class_, overrideWriteEntityToNBTHookTypes, "writeEntityToNBT", NbtCompound.class);
                addMethod(string, class_, afterWriteEntityToNBTHookTypes, "afterWriteEntityToNBT", NbtCompound.class);
                System.out.println("ServerPlayerAPI: registered " + string);
                logger.fine("ServerPlayerAPI: registered class '" + class_.getName() + "' with id '" + string + "'");
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
            beforeAttackEntityFromHookTypes.remove(string);
            overrideAttackEntityFromHookTypes.remove(string);
            afterAttackEntityFromHookTypes.remove(string);
            beforeAttackTargetEntityWithCurrentItemHookTypes.remove(string);
            overrideAttackTargetEntityWithCurrentItemHookTypes.remove(string);
            afterAttackTargetEntityWithCurrentItemHookTypes.remove(string);
            beforeCanHarvestBlockHookTypes.remove(string);
            overrideCanHarvestBlockHookTypes.remove(string);
            afterCanHarvestBlockHookTypes.remove(string);
            beforeCanPlayerEditHookTypes.remove(string);
            overrideCanPlayerEditHookTypes.remove(string);
            afterCanPlayerEditHookTypes.remove(string);
            beforeCanTriggerWalkingHookTypes.remove(string);
            overrideCanTriggerWalkingHookTypes.remove(string);
            afterCanTriggerWalkingHookTypes.remove(string);
            beforeDamageEntityHookTypes.remove(string);
            overrideDamageEntityHookTypes.remove(string);
            afterDamageEntityHookTypes.remove(string);
            beforeDisplayGUIChestHookTypes.remove(string);
            overrideDisplayGUIChestHookTypes.remove(string);
            afterDisplayGUIChestHookTypes.remove(string);
            beforeDisplayGUIDispenserHookTypes.remove(string);
            overrideDisplayGUIDispenserHookTypes.remove(string);
            afterDisplayGUIDispenserHookTypes.remove(string);
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
            beforeFallHookTypes.remove(string);
            overrideFallHookTypes.remove(string);
            afterFallHookTypes.remove(string);
            beforeGetCurrentPlayerStrVsBlockHookTypes.remove(string);
            overrideGetCurrentPlayerStrVsBlockHookTypes.remove(string);
            afterGetCurrentPlayerStrVsBlockHookTypes.remove(string);
            beforeGetDistanceSqHookTypes.remove(string);
            overrideGetDistanceSqHookTypes.remove(string);
            afterGetDistanceSqHookTypes.remove(string);
            beforeGetBrightnessHookTypes.remove(string);
            overrideGetBrightnessHookTypes.remove(string);
            afterGetBrightnessHookTypes.remove(string);
            beforeGetEyeHeightHookTypes.remove(string);
            overrideGetEyeHeightHookTypes.remove(string);
            afterGetEyeHeightHookTypes.remove(string);
            beforeGetSpeedModifierHookTypes.remove(string);
            overrideGetSpeedModifierHookTypes.remove(string);
            afterGetSpeedModifierHookTypes.remove(string);
            beforeHealHookTypes.remove(string);
            overrideHealHookTypes.remove(string);
            afterHealHookTypes.remove(string);
            beforeInteractHookTypes.remove(string);
            overrideInteractHookTypes.remove(string);
            afterInteractHookTypes.remove(string);
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
            beforeIsPlayerSleepingHookTypes.remove(string);
            overrideIsPlayerSleepingHookTypes.remove(string);
            afterIsPlayerSleepingHookTypes.remove(string);
            beforeJumpHookTypes.remove(string);
            overrideJumpHookTypes.remove(string);
            afterJumpHookTypes.remove(string);
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
            beforeOnUpdateEntityHookTypes.remove(string);
            overrideOnUpdateEntityHookTypes.remove(string);
            afterOnUpdateEntityHookTypes.remove(string);
            beforeReadEntityFromNBTHookTypes.remove(string);
            overrideReadEntityFromNBTHookTypes.remove(string);
            afterReadEntityFromNBTHookTypes.remove(string);
            beforeSetDeadHookTypes.remove(string);
            overrideSetDeadHookTypes.remove(string);
            afterSetDeadHookTypes.remove(string);
            beforeSetPositionHookTypes.remove(string);
            overrideSetPositionHookTypes.remove(string);
            afterSetPositionHookTypes.remove(string);
            beforeUpdateEntityActionStateHookTypes.remove(string);
            overrideUpdateEntityActionStateHookTypes.remove(string);
            afterUpdateEntityActionStateHookTypes.remove(string);
            beforeWriteEntityToNBTHookTypes.remove(string);
            overrideWriteEntityToNBTHookTypes.remove(string);
            afterWriteEntityToNBTHookTypes.remove(string);
            log("ServerPlayerAPI: unregistered id '" + string + "'");
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
            boolean var6 = var5.getDeclaringClass() != ServerPlayerBase.class;
            if (var6) {
                list.add(string);
            }

            return var6;
        } catch (Exception var7) {
            throw new RuntimeException("Can not reflect method '" + string2 + "' of class '" + class_.getName() + "'", var7);
        }
    }

    public static ServerPlayerAPI create(ServerPlayerEntity arg) {
        if (allBaseConstructors.size() > 0) {
            if (!initialized) {
                initialize();
            }

            return new ServerPlayerAPI(arg);
        } else {
            return null;
        }
    }

    private static void initialize() {
        sortBases(beforeLocalConstructingHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeLocalConstructing");
        sortBases(afterLocalConstructingHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterLocalConstructing");
        sortBases(beforeAttackEntityFromHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeAttackEntityFrom");
        sortBases(overrideAttackEntityFromHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideAttackEntityFrom");
        sortBases(afterAttackEntityFromHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterAttackEntityFrom");
        sortBases(beforeAttackTargetEntityWithCurrentItemHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeAttackTargetEntityWithCurrentItem");
        sortBases(overrideAttackTargetEntityWithCurrentItemHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideAttackTargetEntityWithCurrentItem");
        sortBases(afterAttackTargetEntityWithCurrentItemHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterAttackTargetEntityWithCurrentItem");
        sortBases(beforeCanHarvestBlockHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeCanHarvestBlock");
        sortBases(overrideCanHarvestBlockHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideCanHarvestBlock");
        sortBases(afterCanHarvestBlockHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterCanHarvestBlock");
        sortBases(beforeCanPlayerEditHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeCanPlayerEdit");
        sortBases(overrideCanPlayerEditHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideCanPlayerEdit");
        sortBases(afterCanPlayerEditHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterCanPlayerEdit");
        sortBases(beforeCanTriggerWalkingHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeCanTriggerWalking");
        sortBases(overrideCanTriggerWalkingHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideCanTriggerWalking");
        sortBases(afterCanTriggerWalkingHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterCanTriggerWalking");
        sortBases(beforeDamageEntityHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeDamageEntity");
        sortBases(overrideDamageEntityHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideDamageEntity");
        sortBases(afterDamageEntityHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterDamageEntity");
        sortBases(beforeDisplayGUIChestHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeDisplayGUIChest");
        sortBases(overrideDisplayGUIChestHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideDisplayGUIChest");
        sortBases(afterDisplayGUIChestHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterDisplayGUIChest");
        sortBases(beforeDisplayGUIDispenserHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeDisplayGUIDispenser");
        sortBases(overrideDisplayGUIDispenserHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideDisplayGUIDispenser");
        sortBases(afterDisplayGUIDispenserHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterDisplayGUIDispenser");
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
        sortBases(beforeFallHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeFall");
        sortBases(overrideFallHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideFall");
        sortBases(afterFallHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterFall");
        sortBases(beforeGetCurrentPlayerStrVsBlockHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeGetCurrentPlayerStrVsBlock");
        sortBases(overrideGetCurrentPlayerStrVsBlockHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideGetCurrentPlayerStrVsBlock");
        sortBases(afterGetCurrentPlayerStrVsBlockHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterGetCurrentPlayerStrVsBlock");
        sortBases(beforeGetDistanceSqHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeGetDistanceSq");
        sortBases(overrideGetDistanceSqHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideGetDistanceSq");
        sortBases(afterGetDistanceSqHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterGetDistanceSq");
        sortBases(beforeGetBrightnessHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeGetBrightness");
        sortBases(overrideGetBrightnessHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideGetBrightness");
        sortBases(afterGetBrightnessHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterGetBrightness");
        sortBases(beforeGetEyeHeightHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeGetEyeHeight");
        sortBases(overrideGetEyeHeightHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideGetEyeHeight");
        sortBases(afterGetEyeHeightHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterGetEyeHeight");
        sortBases(beforeGetSpeedModifierHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeGetSpeedModifier");
        sortBases(overrideGetSpeedModifierHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideGetSpeedModifier");
        sortBases(afterGetSpeedModifierHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterGetSpeedModifier");
        sortBases(beforeHealHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeHeal");
        sortBases(overrideHealHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideHeal");
        sortBases(afterHealHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterHeal");
        sortBases(beforeInteractHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeInteract");
        sortBases(overrideInteractHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideInteract");
        sortBases(afterInteractHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterInteract");
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
        sortBases(beforeIsPlayerSleepingHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeIsPlayerSleeping");
        sortBases(overrideIsPlayerSleepingHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideIsPlayerSleeping");
        sortBases(afterIsPlayerSleepingHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterIsPlayerSleeping");
        sortBases(beforeJumpHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeJump");
        sortBases(overrideJumpHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideJump");
        sortBases(afterJumpHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterJump");
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
        sortBases(beforeOnUpdateEntityHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeOnUpdateEntity");
        sortBases(overrideOnUpdateEntityHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideOnUpdateEntity");
        sortBases(afterOnUpdateEntityHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterOnUpdateEntity");
        sortBases(beforeReadEntityFromNBTHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeReadEntityFromNBT");
        sortBases(overrideReadEntityFromNBTHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideReadEntityFromNBT");
        sortBases(afterReadEntityFromNBTHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterReadEntityFromNBT");
        sortBases(beforeSetDeadHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeSetDead");
        sortBases(overrideSetDeadHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideSetDead");
        sortBases(afterSetDeadHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterSetDead");
        sortBases(beforeSetPositionHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeSetPosition");
        sortBases(overrideSetPositionHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideSetPosition");
        sortBases(afterSetPositionHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterSetPosition");
        sortBases(beforeUpdateEntityActionStateHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeUpdateEntityActionState");
        sortBases(overrideUpdateEntityActionStateHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideUpdateEntityActionState");
        sortBases(afterUpdateEntityActionStateHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterUpdateEntityActionState");
        sortBases(beforeWriteEntityToNBTHookTypes, allBaseBeforeSuperiors, allBaseBeforeInferiors, "beforeWriteEntityToNBT");
        sortBases(overrideWriteEntityToNBTHookTypes, allBaseOverrideSuperiors, allBaseOverrideInferiors, "overrideWriteEntityToNBT");
        sortBases(afterWriteEntityToNBTHookTypes, allBaseAfterSuperiors, allBaseAfterInferiors, "afterWriteEntityToNBT");
        initialized = true;
    }

    public static void beforeLocalConstructing(ServerPlayerEntity arg, MinecraftServer minecraftServer, World arg2, String string, ServerPlayerInteractionManager arg3) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().beforeLocalConstructing(minecraftServer, arg2, string, arg3);
        }

    }

    public static void afterLocalConstructing(ServerPlayerEntity arg, MinecraftServer minecraftServer, World arg2, String string, ServerPlayerInteractionManager arg3) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().afterLocalConstructing(minecraftServer, arg2, string, arg3);
        }

    }

    private static void sortBases(List<String> list, Map<String, String[]> map, Map<String, String[]> map2, String string) {
        (new ServerPlayerBaseSorter(list, map, map2, string)).Sort();
    }

    private ServerPlayerAPI(ServerPlayerEntity arg) {
        this.unmodifiableAllBaseIds = Collections.unmodifiableSet(this.allBaseObjects.keySet());
        this.player = arg;
        Object[] var2 = new Object[]{this};
        Object[] var3 = new Object[]{this, null};

        Map.Entry<String, Constructor<?>> var5;
        ServerPlayerBase var7;
        for(Iterator<Map.Entry<String, Constructor<?>>> var4 = allBaseConstructors.entrySet().iterator(); var4.hasNext(); this.allBaseObjects.put(var5.getKey(), var7)) {
            var5 = var4.next();
            Constructor<?> var6 = var5.getValue();
            var3[1] = var5.getKey();

            try {
                if (var6.getParameterTypes().length == 1) {
                    var7 = (ServerPlayerBase)var6.newInstance(var2);
                } else {
                    var7 = (ServerPlayerBase)var6.newInstance(var3);
                }
            } catch (Exception var9) {
                throw new RuntimeException("Exception while creating a ServerPlayerBase of type '" + var6.getDeclaringClass() + "'", var9);
            }
        }

        this.beforeLocalConstructingHooks = this.create(beforeLocalConstructingHookTypes);
        this.afterLocalConstructingHooks = this.create(afterLocalConstructingHookTypes);
        this.beforeAttackEntityFromHooks = this.create(beforeAttackEntityFromHookTypes);
        this.overrideAttackEntityFromHooks = this.create(overrideAttackEntityFromHookTypes);
        this.afterAttackEntityFromHooks = this.create(afterAttackEntityFromHookTypes);
        this.isAttackEntityFromModded = this.beforeAttackEntityFromHooks != null || this.overrideAttackEntityFromHooks != null || this.afterAttackEntityFromHooks != null;
        this.beforeAttackTargetEntityWithCurrentItemHooks = this.create(beforeAttackTargetEntityWithCurrentItemHookTypes);
        this.overrideAttackTargetEntityWithCurrentItemHooks = this.create(overrideAttackTargetEntityWithCurrentItemHookTypes);
        this.afterAttackTargetEntityWithCurrentItemHooks = this.create(afterAttackTargetEntityWithCurrentItemHookTypes);
        this.isAttackTargetEntityWithCurrentItemModded = this.beforeAttackTargetEntityWithCurrentItemHooks != null || this.overrideAttackTargetEntityWithCurrentItemHooks != null || this.afterAttackTargetEntityWithCurrentItemHooks != null;
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
        this.beforeDamageEntityHooks = this.create(beforeDamageEntityHookTypes);
        this.overrideDamageEntityHooks = this.create(overrideDamageEntityHookTypes);
        this.afterDamageEntityHooks = this.create(afterDamageEntityHookTypes);
        this.isDamageEntityModded = this.beforeDamageEntityHooks != null || this.overrideDamageEntityHooks != null || this.afterDamageEntityHooks != null;
        this.beforeDisplayGUIChestHooks = this.create(beforeDisplayGUIChestHookTypes);
        this.overrideDisplayGUIChestHooks = this.create(overrideDisplayGUIChestHookTypes);
        this.afterDisplayGUIChestHooks = this.create(afterDisplayGUIChestHookTypes);
        this.isDisplayGUIChestModded = this.beforeDisplayGUIChestHooks != null || this.overrideDisplayGUIChestHooks != null || this.afterDisplayGUIChestHooks != null;
        this.beforeDisplayGUIDispenserHooks = this.create(beforeDisplayGUIDispenserHookTypes);
        this.overrideDisplayGUIDispenserHooks = this.create(overrideDisplayGUIDispenserHookTypes);
        this.afterDisplayGUIDispenserHooks = this.create(afterDisplayGUIDispenserHookTypes);
        this.isDisplayGUIDispenserModded = this.beforeDisplayGUIDispenserHooks != null || this.overrideDisplayGUIDispenserHooks != null || this.afterDisplayGUIDispenserHooks != null;
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
        this.beforeFallHooks = this.create(beforeFallHookTypes);
        this.overrideFallHooks = this.create(overrideFallHookTypes);
        this.afterFallHooks = this.create(afterFallHookTypes);
        this.isFallModded = this.beforeFallHooks != null || this.overrideFallHooks != null || this.afterFallHooks != null;
        this.beforeGetCurrentPlayerStrVsBlockHooks = this.create(beforeGetCurrentPlayerStrVsBlockHookTypes);
        this.overrideGetCurrentPlayerStrVsBlockHooks = this.create(overrideGetCurrentPlayerStrVsBlockHookTypes);
        this.afterGetCurrentPlayerStrVsBlockHooks = this.create(afterGetCurrentPlayerStrVsBlockHookTypes);
        this.isGetCurrentPlayerStrVsBlockModded = this.beforeGetCurrentPlayerStrVsBlockHooks != null || this.overrideGetCurrentPlayerStrVsBlockHooks != null || this.afterGetCurrentPlayerStrVsBlockHooks != null;
        this.beforeGetDistanceSqHooks = this.create(beforeGetDistanceSqHookTypes);
        this.overrideGetDistanceSqHooks = this.create(overrideGetDistanceSqHookTypes);
        this.afterGetDistanceSqHooks = this.create(afterGetDistanceSqHookTypes);
        this.isGetDistanceSqModded = this.beforeGetDistanceSqHooks != null || this.overrideGetDistanceSqHooks != null || this.afterGetDistanceSqHooks != null;
        this.beforeGetBrightnessHooks = this.create(beforeGetBrightnessHookTypes);
        this.overrideGetBrightnessHooks = this.create(overrideGetBrightnessHookTypes);
        this.afterGetBrightnessHooks = this.create(afterGetBrightnessHookTypes);
        this.isGetBrightnessModded = this.beforeGetBrightnessHooks != null || this.overrideGetBrightnessHooks != null || this.afterGetBrightnessHooks != null;
        this.beforeGetEyeHeightHooks = this.create(beforeGetEyeHeightHookTypes);
        this.overrideGetEyeHeightHooks = this.create(overrideGetEyeHeightHookTypes);
        this.afterGetEyeHeightHooks = this.create(afterGetEyeHeightHookTypes);
        this.isGetEyeHeightModded = this.beforeGetEyeHeightHooks != null || this.overrideGetEyeHeightHooks != null || this.afterGetEyeHeightHooks != null;
        this.beforeGetSpeedModifierHooks = this.create(beforeGetSpeedModifierHookTypes);
        this.overrideGetSpeedModifierHooks = this.create(overrideGetSpeedModifierHookTypes);
        this.afterGetSpeedModifierHooks = this.create(afterGetSpeedModifierHookTypes);
        this.isGetSpeedModifierModded = this.beforeGetSpeedModifierHooks != null || this.overrideGetSpeedModifierHooks != null || this.afterGetSpeedModifierHooks != null;
        this.beforeHealHooks = this.create(beforeHealHookTypes);
        this.overrideHealHooks = this.create(overrideHealHookTypes);
        this.afterHealHooks = this.create(afterHealHookTypes);
        this.isHealModded = this.beforeHealHooks != null || this.overrideHealHooks != null || this.afterHealHooks != null;
        this.beforeInteractHooks = this.create(beforeInteractHookTypes);
        this.overrideInteractHooks = this.create(overrideInteractHookTypes);
        this.afterInteractHooks = this.create(afterInteractHookTypes);
        this.isInteractModded = this.beforeInteractHooks != null || this.overrideInteractHooks != null || this.afterInteractHooks != null;
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
        this.beforeIsPlayerSleepingHooks = this.create(beforeIsPlayerSleepingHookTypes);
        this.overrideIsPlayerSleepingHooks = this.create(overrideIsPlayerSleepingHookTypes);
        this.afterIsPlayerSleepingHooks = this.create(afterIsPlayerSleepingHookTypes);
        this.isIsPlayerSleepingModded = this.beforeIsPlayerSleepingHooks != null || this.overrideIsPlayerSleepingHooks != null || this.afterIsPlayerSleepingHooks != null;
        this.beforeJumpHooks = this.create(beforeJumpHookTypes);
        this.overrideJumpHooks = this.create(overrideJumpHookTypes);
        this.afterJumpHooks = this.create(afterJumpHookTypes);
        this.isJumpModded = this.beforeJumpHooks != null || this.overrideJumpHooks != null || this.afterJumpHooks != null;
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
        this.beforeOnUpdateEntityHooks = this.create(beforeOnUpdateEntityHookTypes);
        this.overrideOnUpdateEntityHooks = this.create(overrideOnUpdateEntityHookTypes);
        this.afterOnUpdateEntityHooks = this.create(afterOnUpdateEntityHookTypes);
        this.isOnUpdateEntityModded = this.beforeOnUpdateEntityHooks != null || this.overrideOnUpdateEntityHooks != null || this.afterOnUpdateEntityHooks != null;
        this.beforeReadEntityFromNBTHooks = this.create(beforeReadEntityFromNBTHookTypes);
        this.overrideReadEntityFromNBTHooks = this.create(overrideReadEntityFromNBTHookTypes);
        this.afterReadEntityFromNBTHooks = this.create(afterReadEntityFromNBTHookTypes);
        this.isReadEntityFromNBTModded = this.beforeReadEntityFromNBTHooks != null || this.overrideReadEntityFromNBTHooks != null || this.afterReadEntityFromNBTHooks != null;
        this.beforeSetDeadHooks = this.create(beforeSetDeadHookTypes);
        this.overrideSetDeadHooks = this.create(overrideSetDeadHookTypes);
        this.afterSetDeadHooks = this.create(afterSetDeadHookTypes);
        this.isSetDeadModded = this.beforeSetDeadHooks != null || this.overrideSetDeadHooks != null || this.afterSetDeadHooks != null;
        this.beforeSetPositionHooks = this.create(beforeSetPositionHookTypes);
        this.overrideSetPositionHooks = this.create(overrideSetPositionHookTypes);
        this.afterSetPositionHooks = this.create(afterSetPositionHookTypes);
        this.isSetPositionModded = this.beforeSetPositionHooks != null || this.overrideSetPositionHooks != null || this.afterSetPositionHooks != null;
        this.beforeUpdateEntityActionStateHooks = this.create(beforeUpdateEntityActionStateHookTypes);
        this.overrideUpdateEntityActionStateHooks = this.create(overrideUpdateEntityActionStateHookTypes);
        this.afterUpdateEntityActionStateHooks = this.create(afterUpdateEntityActionStateHookTypes);
        this.isUpdateEntityActionStateModded = this.beforeUpdateEntityActionStateHooks != null || this.overrideUpdateEntityActionStateHooks != null || this.afterUpdateEntityActionStateHooks != null;
        this.beforeWriteEntityToNBTHooks = this.create(beforeWriteEntityToNBTHookTypes);
        this.overrideWriteEntityToNBTHooks = this.create(overrideWriteEntityToNBTHookTypes);
        this.afterWriteEntityToNBTHooks = this.create(afterWriteEntityToNBTHookTypes);
        this.isWriteEntityToNBTModded = this.beforeWriteEntityToNBTHooks != null || this.overrideWriteEntityToNBTHooks != null || this.afterWriteEntityToNBTHooks != null;
    }

    private ServerPlayerBase[] create(List<String> list) {
        if (list.isEmpty()) {
            return null;
        } else {
            ServerPlayerBase[] var2 = new ServerPlayerBase[list.size()];

            for(int var3 = 0; var3 < var2.length; ++var3) {
                var2[var3] = this.getServerPlayerBase((String)list.get(var3));
            }

            return var2;
        }
    }

    private void beforeLocalConstructing(MinecraftServer minecraftServer, World arg, String string, ServerPlayerInteractionManager arg2) {
        if (this.beforeLocalConstructingHooks != null) {
            for(int var5 = this.beforeLocalConstructingHooks.length - 1; var5 >= 0; --var5) {
                this.beforeLocalConstructingHooks[var5].beforeLocalConstructing(minecraftServer, arg, string, arg2);
            }
        }

    }

    private void afterLocalConstructing(MinecraftServer minecraftServer, World arg, String string, ServerPlayerInteractionManager arg2) {
        if (this.afterLocalConstructingHooks != null) {
            for(int var5 = 0; var5 < this.afterLocalConstructingHooks.length; ++var5) {
                this.afterLocalConstructingHooks[var5].afterLocalConstructing(minecraftServer, arg, string, arg2);
            }
        }

    }

    public ServerPlayerBase getServerPlayerBase(String string) {
        return (ServerPlayerBase)this.allBaseObjects.get(string);
    }

    public Set<String> getServerPlayerBaseIds() {
        return this.unmodifiableAllBaseIds;
    }

    public static boolean attackEntityFrom(ServerPlayerEntity arg, DamageSource arg2, int i) {
        boolean var3;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var3 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().attackEntityFrom(arg2, i);
        } else {
            var3 = ((IPlayerAPIServerPlayerEntity)arg).localAttackEntityFrom(arg2, i);
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
            var5 = ((IPlayerAPIServerPlayerEntity)this.player).localAttackEntityFrom(arg, i);
        }

        if (this.afterAttackEntityFromHooks != null) {
            for(int var4 = 0; var4 < this.afterAttackEntityFromHooks.length; ++var4) {
                this.afterAttackEntityFromHooks[var4].afterAttackEntityFrom(arg, i);
            }
        }

        return var5;
    }

    protected ServerPlayerBase GetOverwrittenAttackEntityFrom(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideAttackEntityFromHooks.length; ++var2) {
            if (this.overrideAttackEntityFromHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideAttackEntityFromHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void attackTargetEntityWithCurrentItem(ServerPlayerEntity arg, Entity arg2) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().attackTargetEntityWithCurrentItem(arg2);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localAttackTargetEntityWithCurrentItem(arg2);
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
            ((IPlayerAPIServerPlayerEntity)this.player).localAttackTargetEntityWithCurrentItem(arg);
        }

        if (this.afterAttackTargetEntityWithCurrentItemHooks != null) {
            for(var2 = 0; var2 < this.afterAttackTargetEntityWithCurrentItemHooks.length; ++var2) {
                this.afterAttackTargetEntityWithCurrentItemHooks[var2].afterAttackTargetEntityWithCurrentItem(arg);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenAttackTargetEntityWithCurrentItem(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideAttackTargetEntityWithCurrentItemHooks.length; ++var2) {
            if (this.overrideAttackTargetEntityWithCurrentItemHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideAttackTargetEntityWithCurrentItemHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static boolean canHarvestBlock(ServerPlayerEntity arg, Block arg2) {
        boolean var2;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var2 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().canHarvestBlock(arg2);
        } else {
            var2 = ((IPlayerAPIServerPlayerEntity)arg).localCanHarvestBlock(arg2);
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
            var4 = ((IPlayerAPIServerPlayerEntity)this.player).localCanHarvestBlock(arg);
        }

        if (this.afterCanHarvestBlockHooks != null) {
            for(int var3 = 0; var3 < this.afterCanHarvestBlockHooks.length; ++var3) {
                this.afterCanHarvestBlockHooks[var3].afterCanHarvestBlock(arg);
            }
        }

        return var4;
    }

    protected ServerPlayerBase GetOverwrittenCanHarvestBlock(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideCanHarvestBlockHooks.length; ++var2) {
            if (this.overrideCanHarvestBlockHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideCanHarvestBlockHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static boolean canPlayerEdit(ServerPlayerEntity arg, int i, int j, int k) {
        boolean var4;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var4 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().canPlayerEdit(i, j, k);
        } else {
            var4 = ((IPlayerAPIServerPlayerEntity)arg).localCanPlayerEdit(i, j, k);
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
            var6 = ((IPlayerAPIServerPlayerEntity)this.player).localCanPlayerEdit(i, j, k);
        }

        if (this.afterCanPlayerEditHooks != null) {
            for(int var5 = 0; var5 < this.afterCanPlayerEditHooks.length; ++var5) {
                this.afterCanPlayerEditHooks[var5].afterCanPlayerEdit(i, j, k);
            }
        }

        return var6;
    }

    protected ServerPlayerBase GetOverwrittenCanPlayerEdit(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideCanPlayerEditHooks.length; ++var2) {
            if (this.overrideCanPlayerEditHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideCanPlayerEditHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static boolean canTriggerWalking(ServerPlayerEntity arg) {
        boolean var1;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().canTriggerWalking();
        } else {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).localCanTriggerWalking();
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
            var3 = ((IPlayerAPIServerPlayerEntity)this.player).localCanTriggerWalking();
        }

        if (this.afterCanTriggerWalkingHooks != null) {
            for(int var2 = 0; var2 < this.afterCanTriggerWalkingHooks.length; ++var2) {
                this.afterCanTriggerWalkingHooks[var2].afterCanTriggerWalking();
            }
        }

        return var3;
    }

    protected ServerPlayerBase GetOverwrittenCanTriggerWalking(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideCanTriggerWalkingHooks.length; ++var2) {
            if (this.overrideCanTriggerWalkingHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideCanTriggerWalkingHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void damageEntity(ServerPlayerEntity arg, DamageSource arg2, int i) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().damageEntity(arg2, i);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localDamageEntity(arg2, i);
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
            ((IPlayerAPIServerPlayerEntity)this.player).localDamageEntity(arg, i);
        }

        if (this.afterDamageEntityHooks != null) {
            for(var3 = 0; var3 < this.afterDamageEntityHooks.length; ++var3) {
                this.afterDamageEntityHooks[var3].afterDamageEntity(arg, i);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenDamageEntity(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideDamageEntityHooks.length; ++var2) {
            if (this.overrideDamageEntityHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDamageEntityHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void displayGUIChest(ServerPlayerEntity arg, Inventory arg2) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().displayGUIChest(arg2);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localDisplayGUIChest(arg2);
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
            ((IPlayerAPIServerPlayerEntity)this.player).localDisplayGUIChest(arg);
        }

        if (this.afterDisplayGUIChestHooks != null) {
            for(var2 = 0; var2 < this.afterDisplayGUIChestHooks.length; ++var2) {
                this.afterDisplayGUIChestHooks[var2].afterDisplayGUIChest(arg);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenDisplayGUIChest(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideDisplayGUIChestHooks.length; ++var2) {
            if (this.overrideDisplayGUIChestHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDisplayGUIChestHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void displayGUIDispenser(ServerPlayerEntity arg, DispenserBlockEntity arg2) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().displayGUIDispenser(arg2);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localDisplayGUIDispenser(arg2);
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
            ((IPlayerAPIServerPlayerEntity)this.player).localDisplayGUIDispenser(arg);
        }

        if (this.afterDisplayGUIDispenserHooks != null) {
            for(var2 = 0; var2 < this.afterDisplayGUIDispenserHooks.length; ++var2) {
                this.afterDisplayGUIDispenserHooks[var2].afterDisplayGUIDispenser(arg);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenDisplayGUIDispenser(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideDisplayGUIDispenserHooks.length; ++var2) {
            if (this.overrideDisplayGUIDispenserHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDisplayGUIDispenserHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void displayGUIFurnace(ServerPlayerEntity arg, FurnaceBlockEntity arg2) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().displayGUIFurnace(arg2);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localDisplayGUIFurnace(arg2);
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
            ((IPlayerAPIServerPlayerEntity)this.player).localDisplayGUIFurnace(arg);
        }

        if (this.afterDisplayGUIFurnaceHooks != null) {
            for(var2 = 0; var2 < this.afterDisplayGUIFurnaceHooks.length; ++var2) {
                this.afterDisplayGUIFurnaceHooks[var2].afterDisplayGUIFurnace(arg);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenDisplayGUIFurnace(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideDisplayGUIFurnaceHooks.length; ++var2) {
            if (this.overrideDisplayGUIFurnaceHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDisplayGUIFurnaceHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void displayWorkbenchGUI(ServerPlayerEntity arg, int i, int j, int k) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().displayWorkbenchGUI(i, j, k);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localDisplayWorkbenchGUI(i, j, k);
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
            ((IPlayerAPIServerPlayerEntity)this.player).localDisplayWorkbenchGUI(i, j, k);
        }

        if (this.afterDisplayWorkbenchGUIHooks != null) {
            for(var4 = 0; var4 < this.afterDisplayWorkbenchGUIHooks.length; ++var4) {
                this.afterDisplayWorkbenchGUIHooks[var4].afterDisplayWorkbenchGUI(i, j, k);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenDisplayWorkbenchGUI(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideDisplayWorkbenchGUIHooks.length; ++var2) {
            if (this.overrideDisplayWorkbenchGUIHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDisplayWorkbenchGUIHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static ItemEntity dropOneItem(ServerPlayerEntity arg) {
        ItemEntity var1;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().dropOneItem();
        } else {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).localDropOneItem();
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
            var3 = ((IPlayerAPIServerPlayerEntity)this.player).localDropOneItem();
        }

        if (this.afterDropOneItemHooks != null) {
            for(int var2 = 0; var2 < this.afterDropOneItemHooks.length; ++var2) {
                this.afterDropOneItemHooks[var2].afterDropOneItem();
            }
        }

        return var3;
    }

    protected ServerPlayerBase GetOverwrittenDropOneItem(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideDropOneItemHooks.length; ++var2) {
            if (this.overrideDropOneItemHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDropOneItemHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static ItemEntity dropPlayerItem(ServerPlayerEntity arg, ItemStack arg2) {
        ItemEntity var2;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var2 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().dropPlayerItem(arg2);
        } else {
            var2 = ((IPlayerAPIServerPlayerEntity)arg).localDropPlayerItem(arg2);
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
            var4 = ((IPlayerAPIServerPlayerEntity)this.player).localDropPlayerItem(arg);
        }

        if (this.afterDropPlayerItemHooks != null) {
            for(int var3 = 0; var3 < this.afterDropPlayerItemHooks.length; ++var3) {
                this.afterDropPlayerItemHooks[var3].afterDropPlayerItem(arg);
            }
        }

        return var4;
    }

    protected ServerPlayerBase GetOverwrittenDropPlayerItem(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideDropPlayerItemHooks.length; ++var2) {
            if (this.overrideDropPlayerItemHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideDropPlayerItemHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void fall(ServerPlayerEntity arg, float f) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().fall(f);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localFall(f);
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
            ((IPlayerAPIServerPlayerEntity)this.player).localFall(f);
        }

        if (this.afterFallHooks != null) {
            for(var2 = 0; var2 < this.afterFallHooks.length; ++var2) {
                this.afterFallHooks[var2].afterFall(f);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenFall(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideFallHooks.length; ++var2) {
            if (this.overrideFallHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideFallHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static float getCurrentPlayerStrVsBlock(ServerPlayerEntity arg, Block arg2) {
        float var2;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var2 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().getCurrentPlayerStrVsBlock(arg2);
        } else {
            var2 = ((IPlayerAPIServerPlayerEntity)arg).localGetCurrentPlayerStrVsBlock(arg2);
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
            var4 = ((IPlayerAPIServerPlayerEntity)this.player).localGetCurrentPlayerStrVsBlock(arg);
        }

        if (this.afterGetCurrentPlayerStrVsBlockHooks != null) {
            for(int var3 = 0; var3 < this.afterGetCurrentPlayerStrVsBlockHooks.length; ++var3) {
                this.afterGetCurrentPlayerStrVsBlockHooks[var3].afterGetCurrentPlayerStrVsBlock(arg);
            }
        }

        return var4;
    }

    protected ServerPlayerBase GetOverwrittenGetCurrentPlayerStrVsBlock(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideGetCurrentPlayerStrVsBlockHooks.length; ++var2) {
            if (this.overrideGetCurrentPlayerStrVsBlockHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideGetCurrentPlayerStrVsBlockHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static double getDistanceSq(ServerPlayerEntity arg, double d, double e, double f) {
        double var7;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var7 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().getDistanceSq(d, e, f);
        } else {
            var7 = ((IPlayerAPIServerPlayerEntity)arg).localGetDistanceSq(d, e, f);
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
            var10 = ((IPlayerAPIServerPlayerEntity)this.player).localGetDistanceSq(d, e, f);
        }

        if (this.afterGetDistanceSqHooks != null) {
            for(int var9 = 0; var9 < this.afterGetDistanceSqHooks.length; ++var9) {
                this.afterGetDistanceSqHooks[var9].afterGetDistanceSq(d, e, f);
            }
        }

        return var10;
    }

    protected ServerPlayerBase GetOverwrittenGetDistanceSq(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideGetDistanceSqHooks.length; ++var2) {
            if (this.overrideGetDistanceSqHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideGetDistanceSqHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static float getBrightness(ServerPlayerEntity arg, float f) {
        float var2;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var2 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().getBrightness(f);
        } else {
            var2 = ((IPlayerAPIServerPlayerEntity)arg).localGetBrightness(f);
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
            var4 = ((IPlayerAPIServerPlayerEntity)this.player).localGetBrightness(f);
        }

        if (this.afterGetBrightnessHooks != null) {
            for(int var3 = 0; var3 < this.afterGetBrightnessHooks.length; ++var3) {
                this.afterGetBrightnessHooks[var3].afterGetBrightness(f);
            }
        }

        return var4;
    }

    protected ServerPlayerBase GetOverwrittenGetBrightness(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideGetBrightnessHooks.length; ++var2) {
            if (this.overrideGetBrightnessHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideGetBrightnessHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static float getEyeHeight(ServerPlayerEntity arg) {
        float var1;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().getEyeHeight();
        } else {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).localGetEyeHeight();
        }

        return var1;
    }

    protected float getEyeHeight() {
        if (this.beforeGetEyeHeightHooks != null) {
            for(int var1 = this.beforeGetEyeHeightHooks.length - 1; var1 >= 0; --var1) {
                this.beforeGetEyeHeightHooks[var1].beforeGetEyeHeight();
            }
        }

        float var3;
        if (this.overrideGetEyeHeightHooks != null) {
            var3 = this.overrideGetEyeHeightHooks[this.overrideGetEyeHeightHooks.length - 1].getEyeHeight();
        } else {
            var3 = ((IPlayerAPIServerPlayerEntity)this.player).localGetEyeHeight();
        }

        if (this.afterGetEyeHeightHooks != null) {
            for(int var2 = 0; var2 < this.afterGetEyeHeightHooks.length; ++var2) {
                this.afterGetEyeHeightHooks[var2].afterGetEyeHeight();
            }
        }

        return var3;
    }

    protected ServerPlayerBase GetOverwrittenGetEyeHeight(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideGetEyeHeightHooks.length; ++var2) {
            if (this.overrideGetEyeHeightHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideGetEyeHeightHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static float getSpeedModifier(ServerPlayerEntity arg) {
        float var1;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().getSpeedModifier();
        } else {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).localGetSpeedModifier();
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
            var3 = ((IPlayerAPIServerPlayerEntity)this.player).localGetSpeedModifier();
        }

        if (this.afterGetSpeedModifierHooks != null) {
            for(int var2 = 0; var2 < this.afterGetSpeedModifierHooks.length; ++var2) {
                this.afterGetSpeedModifierHooks[var2].afterGetSpeedModifier();
            }
        }

        return var3;
    }

    protected ServerPlayerBase GetOverwrittenGetSpeedModifier(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideGetSpeedModifierHooks.length; ++var2) {
            if (this.overrideGetSpeedModifierHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideGetSpeedModifierHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void heal(ServerPlayerEntity arg, int i) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().heal(i);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localHeal(i);
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
            ((IPlayerAPIServerPlayerEntity)this.player).localHeal(i);
        }

        if (this.afterHealHooks != null) {
            for(var2 = 0; var2 < this.afterHealHooks.length; ++var2) {
                this.afterHealHooks[var2].afterHeal(i);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenHeal(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideHealHooks.length; ++var2) {
            if (this.overrideHealHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideHealHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static boolean interact(ServerPlayerEntity arg, PlayerEntity arg2) {
        boolean var2;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var2 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().interact(arg2);
        } else {
            var2 = ((IPlayerAPIServerPlayerEntity)arg).localInteract(arg2);
        }

        return var2;
    }

    protected boolean interact(PlayerEntity arg) {
        if (this.beforeInteractHooks != null) {
            for(int var2 = this.beforeInteractHooks.length - 1; var2 >= 0; --var2) {
                this.beforeInteractHooks[var2].beforeInteract(arg);
            }
        }

        boolean var4;
        if (this.overrideInteractHooks != null) {
            var4 = this.overrideInteractHooks[this.overrideInteractHooks.length - 1].interact(arg);
        } else {
            var4 = ((IPlayerAPIServerPlayerEntity)this.player).localInteract(arg);
        }

        if (this.afterInteractHooks != null) {
            for(int var3 = 0; var3 < this.afterInteractHooks.length; ++var3) {
                this.afterInteractHooks[var3].afterInteract(arg);
            }
        }

        return var4;
    }

    protected ServerPlayerBase GetOverwrittenInteract(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideInteractHooks.length; ++var2) {
            if (this.overrideInteractHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideInteractHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static boolean isEntityInsideOpaqueBlock(ServerPlayerEntity arg) {
        boolean var1;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().isEntityInsideOpaqueBlock();
        } else {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).localIsEntityInsideOpaqueBlock();
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
            var3 = ((IPlayerAPIServerPlayerEntity)this.player).localIsEntityInsideOpaqueBlock();
        }

        if (this.afterIsEntityInsideOpaqueBlockHooks != null) {
            for(int var2 = 0; var2 < this.afterIsEntityInsideOpaqueBlockHooks.length; ++var2) {
                this.afterIsEntityInsideOpaqueBlockHooks[var2].afterIsEntityInsideOpaqueBlock();
            }
        }

        return var3;
    }

    protected ServerPlayerBase GetOverwrittenIsEntityInsideOpaqueBlock(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideIsEntityInsideOpaqueBlockHooks.length; ++var2) {
            if (this.overrideIsEntityInsideOpaqueBlockHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideIsEntityInsideOpaqueBlockHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static boolean isInWater(ServerPlayerEntity arg) {
        boolean var1;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().isInWater();
        } else {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).localIsInWater();
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
            var3 = ((IPlayerAPIServerPlayerEntity)this.player).localIsInWater();
        }

        if (this.afterIsInWaterHooks != null) {
            for(int var2 = 0; var2 < this.afterIsInWaterHooks.length; ++var2) {
                this.afterIsInWaterHooks[var2].afterIsInWater();
            }
        }

        return var3;
    }

    protected ServerPlayerBase GetOverwrittenIsInWater(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideIsInWaterHooks.length; ++var2) {
            if (this.overrideIsInWaterHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideIsInWaterHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static boolean isInsideOfMaterial(ServerPlayerEntity arg, Material arg2) {
        boolean var2;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var2 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().isInsideOfMaterial(arg2);
        } else {
            var2 = ((IPlayerAPIServerPlayerEntity)arg).localIsInsideOfMaterial(arg2);
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
            var4 = ((IPlayerAPIServerPlayerEntity)this.player).localIsInsideOfMaterial(arg);
        }

        if (this.afterIsInsideOfMaterialHooks != null) {
            for(int var3 = 0; var3 < this.afterIsInsideOfMaterialHooks.length; ++var3) {
                this.afterIsInsideOfMaterialHooks[var3].afterIsInsideOfMaterial(arg);
            }
        }

        return var4;
    }

    protected ServerPlayerBase GetOverwrittenIsInsideOfMaterial(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideIsInsideOfMaterialHooks.length; ++var2) {
            if (this.overrideIsInsideOfMaterialHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideIsInsideOfMaterialHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static boolean isOnLadder(ServerPlayerEntity arg) {
        boolean var1;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().isOnLadder();
        } else {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).localIsOnLadder();
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
            var3 = ((IPlayerAPIServerPlayerEntity)this.player).localIsOnLadder();
        }

        if (this.afterIsOnLadderHooks != null) {
            for(int var2 = 0; var2 < this.afterIsOnLadderHooks.length; ++var2) {
                this.afterIsOnLadderHooks[var2].afterIsOnLadder();
            }
        }

        return var3;
    }

    protected ServerPlayerBase GetOverwrittenIsOnLadder(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideIsOnLadderHooks.length; ++var2) {
            if (this.overrideIsOnLadderHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideIsOnLadderHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static boolean isPlayerSleeping(ServerPlayerEntity arg) {
        boolean var1;
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().isPlayerSleeping();
        } else {
            var1 = ((IPlayerAPIServerPlayerEntity)arg).localIsPlayerSleeping();
        }

        return var1;
    }

    protected boolean isPlayerSleeping() {
        if (this.beforeIsPlayerSleepingHooks != null) {
            for(int var1 = this.beforeIsPlayerSleepingHooks.length - 1; var1 >= 0; --var1) {
                this.beforeIsPlayerSleepingHooks[var1].beforeIsPlayerSleeping();
            }
        }

        boolean var3;
        if (this.overrideIsPlayerSleepingHooks != null) {
            var3 = this.overrideIsPlayerSleepingHooks[this.overrideIsPlayerSleepingHooks.length - 1].isPlayerSleeping();
        } else {
            var3 = ((IPlayerAPIServerPlayerEntity)this.player).localIsPlayerSleeping();
        }

        if (this.afterIsPlayerSleepingHooks != null) {
            for(int var2 = 0; var2 < this.afterIsPlayerSleepingHooks.length; ++var2) {
                this.afterIsPlayerSleepingHooks[var2].afterIsPlayerSleeping();
            }
        }

        return var3;
    }

    protected ServerPlayerBase GetOverwrittenIsPlayerSleeping(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideIsPlayerSleepingHooks.length; ++var2) {
            if (this.overrideIsPlayerSleepingHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideIsPlayerSleepingHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void jump(ServerPlayerEntity arg) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().jump();
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localJump();
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
            ((IPlayerAPIServerPlayerEntity)this.player).localJump();
        }

        if (this.afterJumpHooks != null) {
            for(var1 = 0; var1 < this.afterJumpHooks.length; ++var1) {
                this.afterJumpHooks[var1].afterJump();
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenJump(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideJumpHooks.length; ++var2) {
            if (this.overrideJumpHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideJumpHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void moveEntity(ServerPlayerEntity arg, double d, double e, double f) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().moveEntity(d, e, f);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localMoveEntity(d, e, f);
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
            ((IPlayerAPIServerPlayerEntity)this.player).localMoveEntity(d, e, f);
        }

        if (this.afterMoveEntityHooks != null) {
            for(var7 = 0; var7 < this.afterMoveEntityHooks.length; ++var7) {
                this.afterMoveEntityHooks[var7].afterMoveEntity(d, e, f);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenMoveEntity(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideMoveEntityHooks.length; ++var2) {
            if (this.overrideMoveEntityHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideMoveEntityHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void moveEntityWithHeading(ServerPlayerEntity arg, float f, float g) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().moveEntityWithHeading(f, g);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localMoveEntityWithHeading(f, g);
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
            ((IPlayerAPIServerPlayerEntity)this.player).localMoveEntityWithHeading(f, g);
        }

        if (this.afterMoveEntityWithHeadingHooks != null) {
            for(var3 = 0; var3 < this.afterMoveEntityWithHeadingHooks.length; ++var3) {
                this.afterMoveEntityWithHeadingHooks[var3].afterMoveEntityWithHeading(f, g);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenMoveEntityWithHeading(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideMoveEntityWithHeadingHooks.length; ++var2) {
            if (this.overrideMoveEntityWithHeadingHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideMoveEntityWithHeadingHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void moveFlying(ServerPlayerEntity arg, float f, float g, float h) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().moveFlying(f, g, h);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localMoveFlying(f, g, h);
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
            ((IPlayerAPIServerPlayerEntity)this.player).localMoveFlying(f, g, h);
        }

        if (this.afterMoveFlyingHooks != null) {
            for(var4 = 0; var4 < this.afterMoveFlyingHooks.length; ++var4) {
                this.afterMoveFlyingHooks[var4].afterMoveFlying(f, g, h);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenMoveFlying(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideMoveFlyingHooks.length; ++var2) {
            if (this.overrideMoveFlyingHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideMoveFlyingHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void onDeath(ServerPlayerEntity arg, DamageSource arg2) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().onDeath(arg2);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localOnDeath(arg2);
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
            ((IPlayerAPIServerPlayerEntity)this.player).localOnDeath(arg);
        }

        if (this.afterOnDeathHooks != null) {
            for(var2 = 0; var2 < this.afterOnDeathHooks.length; ++var2) {
                this.afterOnDeathHooks[var2].afterOnDeath(arg);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenOnDeath(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideOnDeathHooks.length; ++var2) {
            if (this.overrideOnDeathHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideOnDeathHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void onLivingUpdate(ServerPlayerEntity arg) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().onLivingUpdate();
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localOnLivingUpdate();
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
            ((IPlayerAPIServerPlayerEntity)this.player).localOnLivingUpdate();
        }

        if (this.afterOnLivingUpdateHooks != null) {
            for(var1 = 0; var1 < this.afterOnLivingUpdateHooks.length; ++var1) {
                this.afterOnLivingUpdateHooks[var1].afterOnLivingUpdate();
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenOnLivingUpdate(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideOnLivingUpdateHooks.length; ++var2) {
            if (this.overrideOnLivingUpdateHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideOnLivingUpdateHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void onKillEntity(ServerPlayerEntity arg, MobEntity arg2) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().onKillEntity(arg2);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localOnKillEntity(arg2);
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
            ((IPlayerAPIServerPlayerEntity)this.player).localOnKillEntity(arg);
        }

        if (this.afterOnKillEntityHooks != null) {
            for(var2 = 0; var2 < this.afterOnKillEntityHooks.length; ++var2) {
                this.afterOnKillEntityHooks[var2].afterOnKillEntity(arg);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenOnKillEntity(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideOnKillEntityHooks.length; ++var2) {
            if (this.overrideOnKillEntityHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideOnKillEntityHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void onUpdate(ServerPlayerEntity arg) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().onUpdate();
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localOnUpdate();
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
            ((IPlayerAPIServerPlayerEntity)this.player).localOnUpdate();
        }

        if (this.afterOnUpdateHooks != null) {
            for(var1 = 0; var1 < this.afterOnUpdateHooks.length; ++var1) {
                this.afterOnUpdateHooks[var1].afterOnUpdate();
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenOnUpdate(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideOnUpdateHooks.length; ++var2) {
            if (this.overrideOnUpdateHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideOnUpdateHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void onUpdateEntity(ServerPlayerEntity arg) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().onUpdateEntity();
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localOnUpdateEntity();
        }

    }

    protected void onUpdateEntity() {
        int var1;
        if (this.beforeOnUpdateEntityHooks != null) {
            for(var1 = this.beforeOnUpdateEntityHooks.length - 1; var1 >= 0; --var1) {
                this.beforeOnUpdateEntityHooks[var1].beforeOnUpdateEntity();
            }
        }

        if (this.overrideOnUpdateEntityHooks != null) {
            this.overrideOnUpdateEntityHooks[this.overrideOnUpdateEntityHooks.length - 1].onUpdateEntity();
        } else {
            ((IPlayerAPIServerPlayerEntity)this.player).localOnUpdateEntity();
        }

        if (this.afterOnUpdateEntityHooks != null) {
            for(var1 = 0; var1 < this.afterOnUpdateEntityHooks.length; ++var1) {
                this.afterOnUpdateEntityHooks[var1].afterOnUpdateEntity();
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenOnUpdateEntity(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideOnUpdateEntityHooks.length; ++var2) {
            if (this.overrideOnUpdateEntityHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideOnUpdateEntityHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void readEntityFromNBT(ServerPlayerEntity arg, NbtCompound arg2) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().readEntityFromNBT(arg2);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localReadEntityFromNBT(arg2);
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
            ((IPlayerAPIServerPlayerEntity)this.player).localReadEntityFromNBT(arg);
        }

        if (this.afterReadEntityFromNBTHooks != null) {
            for(var2 = 0; var2 < this.afterReadEntityFromNBTHooks.length; ++var2) {
                this.afterReadEntityFromNBTHooks[var2].afterReadEntityFromNBT(arg);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenReadEntityFromNBT(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideReadEntityFromNBTHooks.length; ++var2) {
            if (this.overrideReadEntityFromNBTHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideReadEntityFromNBTHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void setDead(ServerPlayerEntity arg) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().setDead();
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localSetDead();
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
            ((IPlayerAPIServerPlayerEntity)this.player).localSetDead();
        }

        if (this.afterSetDeadHooks != null) {
            for(var1 = 0; var1 < this.afterSetDeadHooks.length; ++var1) {
                this.afterSetDeadHooks[var1].afterSetDead();
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenSetDead(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideSetDeadHooks.length; ++var2) {
            if (this.overrideSetDeadHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideSetDeadHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void setPosition(ServerPlayerEntity arg, double d, double e, double f) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().setPosition(d, e, f);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localSetPosition(d, e, f);
        }

    }

    protected void setPosition(double d, double e, double f) {
        int var7;
        if (this.beforeSetPositionHooks != null) {
            for(var7 = this.beforeSetPositionHooks.length - 1; var7 >= 0; --var7) {
                this.beforeSetPositionHooks[var7].beforeSetPosition(d, e, f);
            }
        }

        if (this.overrideSetPositionHooks != null) {
            this.overrideSetPositionHooks[this.overrideSetPositionHooks.length - 1].setPosition(d, e, f);
        } else {
            ((IPlayerAPIServerPlayerEntity)this.player).localSetPosition(d, e, f);
        }

        if (this.afterSetPositionHooks != null) {
            for(var7 = 0; var7 < this.afterSetPositionHooks.length; ++var7) {
                this.afterSetPositionHooks[var7].afterSetPosition(d, e, f);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenSetPosition(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideSetPositionHooks.length; ++var2) {
            if (this.overrideSetPositionHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideSetPositionHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void updateEntityActionState(ServerPlayerEntity arg) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().updateEntityActionState();
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localUpdateEntityActionState();
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
            ((IPlayerAPIServerPlayerEntity)this.player).localUpdateEntityActionState();
        }

        if (this.afterUpdateEntityActionStateHooks != null) {
            for(var1 = 0; var1 < this.afterUpdateEntityActionStateHooks.length; ++var1) {
                this.afterUpdateEntityActionStateHooks[var1].afterUpdateEntityActionState();
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenUpdateEntityActionState(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideUpdateEntityActionStateHooks.length; ++var2) {
            if (this.overrideUpdateEntityActionStateHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideUpdateEntityActionStateHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
    }

    public static void writeEntityToNBT(ServerPlayerEntity arg, NbtCompound arg2) {
        if (((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI() != null) {
            ((IPlayerAPIServerPlayerEntity)arg).getServerPlayerAPI().writeEntityToNBT(arg2);
        } else {
            ((IPlayerAPIServerPlayerEntity)arg).localWriteEntityToNBT(arg2);
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
            ((IPlayerAPIServerPlayerEntity)this.player).localWriteEntityToNBT(arg);
        }

        if (this.afterWriteEntityToNBTHooks != null) {
            for(var2 = 0; var2 < this.afterWriteEntityToNBTHooks.length; ++var2) {
                this.afterWriteEntityToNBTHooks[var2].afterWriteEntityToNBT(arg);
            }
        }

    }

    protected ServerPlayerBase GetOverwrittenWriteEntityToNBT(ServerPlayerBase serverPlayerBase) {
        for(int var2 = 0; var2 < this.overrideWriteEntityToNBTHooks.length; ++var2) {
            if (this.overrideWriteEntityToNBTHooks[var2] == serverPlayerBase) {
                if (var2 == 0) {
                    return null;
                }

                return this.overrideWriteEntityToNBTHooks[var2 - 1];
            }
        }

        return serverPlayerBase;
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
