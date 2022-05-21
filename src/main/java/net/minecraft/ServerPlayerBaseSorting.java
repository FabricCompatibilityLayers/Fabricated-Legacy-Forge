//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.minecraft;

public final class ServerPlayerBaseSorting {
    private String[] beforeLocalConstructingSuperiors = null;
    private String[] beforeLocalConstructingInferiors = null;
    private String[] afterLocalConstructingSuperiors = null;
    private String[] afterLocalConstructingInferiors = null;
    private String[] beforeAttackEntityFromSuperiors = null;
    private String[] beforeAttackEntityFromInferiors = null;
    private String[] overrideAttackEntityFromSuperiors = null;
    private String[] overrideAttackEntityFromInferiors = null;
    private String[] afterAttackEntityFromSuperiors = null;
    private String[] afterAttackEntityFromInferiors = null;
    private String[] beforeAttackTargetEntityWithCurrentItemSuperiors = null;
    private String[] beforeAttackTargetEntityWithCurrentItemInferiors = null;
    private String[] overrideAttackTargetEntityWithCurrentItemSuperiors = null;
    private String[] overrideAttackTargetEntityWithCurrentItemInferiors = null;
    private String[] afterAttackTargetEntityWithCurrentItemSuperiors = null;
    private String[] afterAttackTargetEntityWithCurrentItemInferiors = null;
    private String[] beforeCanHarvestBlockSuperiors = null;
    private String[] beforeCanHarvestBlockInferiors = null;
    private String[] overrideCanHarvestBlockSuperiors = null;
    private String[] overrideCanHarvestBlockInferiors = null;
    private String[] afterCanHarvestBlockSuperiors = null;
    private String[] afterCanHarvestBlockInferiors = null;
    private String[] beforeCanPlayerEditSuperiors = null;
    private String[] beforeCanPlayerEditInferiors = null;
    private String[] overrideCanPlayerEditSuperiors = null;
    private String[] overrideCanPlayerEditInferiors = null;
    private String[] afterCanPlayerEditSuperiors = null;
    private String[] afterCanPlayerEditInferiors = null;
    private String[] beforeCanTriggerWalkingSuperiors = null;
    private String[] beforeCanTriggerWalkingInferiors = null;
    private String[] overrideCanTriggerWalkingSuperiors = null;
    private String[] overrideCanTriggerWalkingInferiors = null;
    private String[] afterCanTriggerWalkingSuperiors = null;
    private String[] afterCanTriggerWalkingInferiors = null;
    private String[] beforeDamageEntitySuperiors = null;
    private String[] beforeDamageEntityInferiors = null;
    private String[] overrideDamageEntitySuperiors = null;
    private String[] overrideDamageEntityInferiors = null;
    private String[] afterDamageEntitySuperiors = null;
    private String[] afterDamageEntityInferiors = null;
    private String[] beforeDisplayGUIChestSuperiors = null;
    private String[] beforeDisplayGUIChestInferiors = null;
    private String[] overrideDisplayGUIChestSuperiors = null;
    private String[] overrideDisplayGUIChestInferiors = null;
    private String[] afterDisplayGUIChestSuperiors = null;
    private String[] afterDisplayGUIChestInferiors = null;
    private String[] beforeDisplayGUIDispenserSuperiors = null;
    private String[] beforeDisplayGUIDispenserInferiors = null;
    private String[] overrideDisplayGUIDispenserSuperiors = null;
    private String[] overrideDisplayGUIDispenserInferiors = null;
    private String[] afterDisplayGUIDispenserSuperiors = null;
    private String[] afterDisplayGUIDispenserInferiors = null;
    private String[] beforeDisplayGUIFurnaceSuperiors = null;
    private String[] beforeDisplayGUIFurnaceInferiors = null;
    private String[] overrideDisplayGUIFurnaceSuperiors = null;
    private String[] overrideDisplayGUIFurnaceInferiors = null;
    private String[] afterDisplayGUIFurnaceSuperiors = null;
    private String[] afterDisplayGUIFurnaceInferiors = null;
    private String[] beforeDisplayWorkbenchGUISuperiors = null;
    private String[] beforeDisplayWorkbenchGUIInferiors = null;
    private String[] overrideDisplayWorkbenchGUISuperiors = null;
    private String[] overrideDisplayWorkbenchGUIInferiors = null;
    private String[] afterDisplayWorkbenchGUISuperiors = null;
    private String[] afterDisplayWorkbenchGUIInferiors = null;
    private String[] beforeDropOneItemSuperiors = null;
    private String[] beforeDropOneItemInferiors = null;
    private String[] overrideDropOneItemSuperiors = null;
    private String[] overrideDropOneItemInferiors = null;
    private String[] afterDropOneItemSuperiors = null;
    private String[] afterDropOneItemInferiors = null;
    private String[] beforeDropPlayerItemSuperiors = null;
    private String[] beforeDropPlayerItemInferiors = null;
    private String[] overrideDropPlayerItemSuperiors = null;
    private String[] overrideDropPlayerItemInferiors = null;
    private String[] afterDropPlayerItemSuperiors = null;
    private String[] afterDropPlayerItemInferiors = null;
    private String[] beforeFallSuperiors = null;
    private String[] beforeFallInferiors = null;
    private String[] overrideFallSuperiors = null;
    private String[] overrideFallInferiors = null;
    private String[] afterFallSuperiors = null;
    private String[] afterFallInferiors = null;
    private String[] beforeGetCurrentPlayerStrVsBlockSuperiors = null;
    private String[] beforeGetCurrentPlayerStrVsBlockInferiors = null;
    private String[] overrideGetCurrentPlayerStrVsBlockSuperiors = null;
    private String[] overrideGetCurrentPlayerStrVsBlockInferiors = null;
    private String[] afterGetCurrentPlayerStrVsBlockSuperiors = null;
    private String[] afterGetCurrentPlayerStrVsBlockInferiors = null;
    private String[] beforeGetDistanceSqSuperiors = null;
    private String[] beforeGetDistanceSqInferiors = null;
    private String[] overrideGetDistanceSqSuperiors = null;
    private String[] overrideGetDistanceSqInferiors = null;
    private String[] afterGetDistanceSqSuperiors = null;
    private String[] afterGetDistanceSqInferiors = null;
    private String[] beforeGetBrightnessSuperiors = null;
    private String[] beforeGetBrightnessInferiors = null;
    private String[] overrideGetBrightnessSuperiors = null;
    private String[] overrideGetBrightnessInferiors = null;
    private String[] afterGetBrightnessSuperiors = null;
    private String[] afterGetBrightnessInferiors = null;
    private String[] beforeGetEyeHeightSuperiors = null;
    private String[] beforeGetEyeHeightInferiors = null;
    private String[] overrideGetEyeHeightSuperiors = null;
    private String[] overrideGetEyeHeightInferiors = null;
    private String[] afterGetEyeHeightSuperiors = null;
    private String[] afterGetEyeHeightInferiors = null;
    private String[] beforeGetSpeedModifierSuperiors = null;
    private String[] beforeGetSpeedModifierInferiors = null;
    private String[] overrideGetSpeedModifierSuperiors = null;
    private String[] overrideGetSpeedModifierInferiors = null;
    private String[] afterGetSpeedModifierSuperiors = null;
    private String[] afterGetSpeedModifierInferiors = null;
    private String[] beforeHealSuperiors = null;
    private String[] beforeHealInferiors = null;
    private String[] overrideHealSuperiors = null;
    private String[] overrideHealInferiors = null;
    private String[] afterHealSuperiors = null;
    private String[] afterHealInferiors = null;
    private String[] beforeInteractSuperiors = null;
    private String[] beforeInteractInferiors = null;
    private String[] overrideInteractSuperiors = null;
    private String[] overrideInteractInferiors = null;
    private String[] afterInteractSuperiors = null;
    private String[] afterInteractInferiors = null;
    private String[] beforeIsEntityInsideOpaqueBlockSuperiors = null;
    private String[] beforeIsEntityInsideOpaqueBlockInferiors = null;
    private String[] overrideIsEntityInsideOpaqueBlockSuperiors = null;
    private String[] overrideIsEntityInsideOpaqueBlockInferiors = null;
    private String[] afterIsEntityInsideOpaqueBlockSuperiors = null;
    private String[] afterIsEntityInsideOpaqueBlockInferiors = null;
    private String[] beforeIsInWaterSuperiors = null;
    private String[] beforeIsInWaterInferiors = null;
    private String[] overrideIsInWaterSuperiors = null;
    private String[] overrideIsInWaterInferiors = null;
    private String[] afterIsInWaterSuperiors = null;
    private String[] afterIsInWaterInferiors = null;
    private String[] beforeIsInsideOfMaterialSuperiors = null;
    private String[] beforeIsInsideOfMaterialInferiors = null;
    private String[] overrideIsInsideOfMaterialSuperiors = null;
    private String[] overrideIsInsideOfMaterialInferiors = null;
    private String[] afterIsInsideOfMaterialSuperiors = null;
    private String[] afterIsInsideOfMaterialInferiors = null;
    private String[] beforeIsOnLadderSuperiors = null;
    private String[] beforeIsOnLadderInferiors = null;
    private String[] overrideIsOnLadderSuperiors = null;
    private String[] overrideIsOnLadderInferiors = null;
    private String[] afterIsOnLadderSuperiors = null;
    private String[] afterIsOnLadderInferiors = null;
    private String[] beforeIsPlayerSleepingSuperiors = null;
    private String[] beforeIsPlayerSleepingInferiors = null;
    private String[] overrideIsPlayerSleepingSuperiors = null;
    private String[] overrideIsPlayerSleepingInferiors = null;
    private String[] afterIsPlayerSleepingSuperiors = null;
    private String[] afterIsPlayerSleepingInferiors = null;
    private String[] beforeJumpSuperiors = null;
    private String[] beforeJumpInferiors = null;
    private String[] overrideJumpSuperiors = null;
    private String[] overrideJumpInferiors = null;
    private String[] afterJumpSuperiors = null;
    private String[] afterJumpInferiors = null;
    private String[] beforeMoveEntitySuperiors = null;
    private String[] beforeMoveEntityInferiors = null;
    private String[] overrideMoveEntitySuperiors = null;
    private String[] overrideMoveEntityInferiors = null;
    private String[] afterMoveEntitySuperiors = null;
    private String[] afterMoveEntityInferiors = null;
    private String[] beforeMoveEntityWithHeadingSuperiors = null;
    private String[] beforeMoveEntityWithHeadingInferiors = null;
    private String[] overrideMoveEntityWithHeadingSuperiors = null;
    private String[] overrideMoveEntityWithHeadingInferiors = null;
    private String[] afterMoveEntityWithHeadingSuperiors = null;
    private String[] afterMoveEntityWithHeadingInferiors = null;
    private String[] beforeMoveFlyingSuperiors = null;
    private String[] beforeMoveFlyingInferiors = null;
    private String[] overrideMoveFlyingSuperiors = null;
    private String[] overrideMoveFlyingInferiors = null;
    private String[] afterMoveFlyingSuperiors = null;
    private String[] afterMoveFlyingInferiors = null;
    private String[] beforeOnDeathSuperiors = null;
    private String[] beforeOnDeathInferiors = null;
    private String[] overrideOnDeathSuperiors = null;
    private String[] overrideOnDeathInferiors = null;
    private String[] afterOnDeathSuperiors = null;
    private String[] afterOnDeathInferiors = null;
    private String[] beforeOnLivingUpdateSuperiors = null;
    private String[] beforeOnLivingUpdateInferiors = null;
    private String[] overrideOnLivingUpdateSuperiors = null;
    private String[] overrideOnLivingUpdateInferiors = null;
    private String[] afterOnLivingUpdateSuperiors = null;
    private String[] afterOnLivingUpdateInferiors = null;
    private String[] beforeOnKillEntitySuperiors = null;
    private String[] beforeOnKillEntityInferiors = null;
    private String[] overrideOnKillEntitySuperiors = null;
    private String[] overrideOnKillEntityInferiors = null;
    private String[] afterOnKillEntitySuperiors = null;
    private String[] afterOnKillEntityInferiors = null;
    private String[] beforeOnUpdateSuperiors = null;
    private String[] beforeOnUpdateInferiors = null;
    private String[] overrideOnUpdateSuperiors = null;
    private String[] overrideOnUpdateInferiors = null;
    private String[] afterOnUpdateSuperiors = null;
    private String[] afterOnUpdateInferiors = null;
    private String[] beforeOnUpdateEntitySuperiors = null;
    private String[] beforeOnUpdateEntityInferiors = null;
    private String[] overrideOnUpdateEntitySuperiors = null;
    private String[] overrideOnUpdateEntityInferiors = null;
    private String[] afterOnUpdateEntitySuperiors = null;
    private String[] afterOnUpdateEntityInferiors = null;
    private String[] beforeReadEntityFromNBTSuperiors = null;
    private String[] beforeReadEntityFromNBTInferiors = null;
    private String[] overrideReadEntityFromNBTSuperiors = null;
    private String[] overrideReadEntityFromNBTInferiors = null;
    private String[] afterReadEntityFromNBTSuperiors = null;
    private String[] afterReadEntityFromNBTInferiors = null;
    private String[] beforeSetDeadSuperiors = null;
    private String[] beforeSetDeadInferiors = null;
    private String[] overrideSetDeadSuperiors = null;
    private String[] overrideSetDeadInferiors = null;
    private String[] afterSetDeadSuperiors = null;
    private String[] afterSetDeadInferiors = null;
    private String[] beforeSetPositionSuperiors = null;
    private String[] beforeSetPositionInferiors = null;
    private String[] overrideSetPositionSuperiors = null;
    private String[] overrideSetPositionInferiors = null;
    private String[] afterSetPositionSuperiors = null;
    private String[] afterSetPositionInferiors = null;
    private String[] beforeUpdateEntityActionStateSuperiors = null;
    private String[] beforeUpdateEntityActionStateInferiors = null;
    private String[] overrideUpdateEntityActionStateSuperiors = null;
    private String[] overrideUpdateEntityActionStateInferiors = null;
    private String[] afterUpdateEntityActionStateSuperiors = null;
    private String[] afterUpdateEntityActionStateInferiors = null;
    private String[] beforeWriteEntityToNBTSuperiors = null;
    private String[] beforeWriteEntityToNBTInferiors = null;
    private String[] overrideWriteEntityToNBTSuperiors = null;
    private String[] overrideWriteEntityToNBTInferiors = null;
    private String[] afterWriteEntityToNBTSuperiors = null;
    private String[] afterWriteEntityToNBTInferiors = null;

    public ServerPlayerBaseSorting() {
    }

    public String[] getBeforeLocalConstructingSuperiors() {
        return this.beforeLocalConstructingSuperiors;
    }

    public String[] getBeforeLocalConstructingInferiors() {
        return this.beforeLocalConstructingInferiors;
    }

    public String[] getAfterLocalConstructingSuperiors() {
        return this.afterLocalConstructingSuperiors;
    }

    public String[] getAfterLocalConstructingInferiors() {
        return this.afterLocalConstructingInferiors;
    }

    public void setBeforeLocalConstructingSuperiors(String[] strings) {
        this.beforeLocalConstructingSuperiors = strings;
    }

    public void setBeforeLocalConstructingInferiors(String[] strings) {
        this.beforeLocalConstructingInferiors = strings;
    }

    public void setAfterLocalConstructingSuperiors(String[] strings) {
        this.afterLocalConstructingSuperiors = strings;
    }

    public void setAfterLocalConstructingInferiors(String[] strings) {
        this.afterLocalConstructingInferiors = strings;
    }

    public String[] getBeforeAttackEntityFromSuperiors() {
        return this.beforeAttackEntityFromSuperiors;
    }

    public String[] getBeforeAttackEntityFromInferiors() {
        return this.beforeAttackEntityFromInferiors;
    }

    public String[] getOverrideAttackEntityFromSuperiors() {
        return this.overrideAttackEntityFromSuperiors;
    }

    public String[] getOverrideAttackEntityFromInferiors() {
        return this.overrideAttackEntityFromInferiors;
    }

    public String[] getAfterAttackEntityFromSuperiors() {
        return this.afterAttackEntityFromSuperiors;
    }

    public String[] getAfterAttackEntityFromInferiors() {
        return this.afterAttackEntityFromInferiors;
    }

    public void setBeforeAttackEntityFromSuperiors(String[] strings) {
        this.beforeAttackEntityFromSuperiors = strings;
    }

    public void setBeforeAttackEntityFromInferiors(String[] strings) {
        this.beforeAttackEntityFromInferiors = strings;
    }

    public void setOverrideAttackEntityFromSuperiors(String[] strings) {
        this.overrideAttackEntityFromSuperiors = strings;
    }

    public void setOverrideAttackEntityFromInferiors(String[] strings) {
        this.overrideAttackEntityFromInferiors = strings;
    }

    public void setAfterAttackEntityFromSuperiors(String[] strings) {
        this.afterAttackEntityFromSuperiors = strings;
    }

    public void setAfterAttackEntityFromInferiors(String[] strings) {
        this.afterAttackEntityFromInferiors = strings;
    }

    public String[] getBeforeAttackTargetEntityWithCurrentItemSuperiors() {
        return this.beforeAttackTargetEntityWithCurrentItemSuperiors;
    }

    public String[] getBeforeAttackTargetEntityWithCurrentItemInferiors() {
        return this.beforeAttackTargetEntityWithCurrentItemInferiors;
    }

    public String[] getOverrideAttackTargetEntityWithCurrentItemSuperiors() {
        return this.overrideAttackTargetEntityWithCurrentItemSuperiors;
    }

    public String[] getOverrideAttackTargetEntityWithCurrentItemInferiors() {
        return this.overrideAttackTargetEntityWithCurrentItemInferiors;
    }

    public String[] getAfterAttackTargetEntityWithCurrentItemSuperiors() {
        return this.afterAttackTargetEntityWithCurrentItemSuperiors;
    }

    public String[] getAfterAttackTargetEntityWithCurrentItemInferiors() {
        return this.afterAttackTargetEntityWithCurrentItemInferiors;
    }

    public void setBeforeAttackTargetEntityWithCurrentItemSuperiors(String[] strings) {
        this.beforeAttackTargetEntityWithCurrentItemSuperiors = strings;
    }

    public void setBeforeAttackTargetEntityWithCurrentItemInferiors(String[] strings) {
        this.beforeAttackTargetEntityWithCurrentItemInferiors = strings;
    }

    public void setOverrideAttackTargetEntityWithCurrentItemSuperiors(String[] strings) {
        this.overrideAttackTargetEntityWithCurrentItemSuperiors = strings;
    }

    public void setOverrideAttackTargetEntityWithCurrentItemInferiors(String[] strings) {
        this.overrideAttackTargetEntityWithCurrentItemInferiors = strings;
    }

    public void setAfterAttackTargetEntityWithCurrentItemSuperiors(String[] strings) {
        this.afterAttackTargetEntityWithCurrentItemSuperiors = strings;
    }

    public void setAfterAttackTargetEntityWithCurrentItemInferiors(String[] strings) {
        this.afterAttackTargetEntityWithCurrentItemInferiors = strings;
    }

    public String[] getBeforeCanHarvestBlockSuperiors() {
        return this.beforeCanHarvestBlockSuperiors;
    }

    public String[] getBeforeCanHarvestBlockInferiors() {
        return this.beforeCanHarvestBlockInferiors;
    }

    public String[] getOverrideCanHarvestBlockSuperiors() {
        return this.overrideCanHarvestBlockSuperiors;
    }

    public String[] getOverrideCanHarvestBlockInferiors() {
        return this.overrideCanHarvestBlockInferiors;
    }

    public String[] getAfterCanHarvestBlockSuperiors() {
        return this.afterCanHarvestBlockSuperiors;
    }

    public String[] getAfterCanHarvestBlockInferiors() {
        return this.afterCanHarvestBlockInferiors;
    }

    public void setBeforeCanHarvestBlockSuperiors(String[] strings) {
        this.beforeCanHarvestBlockSuperiors = strings;
    }

    public void setBeforeCanHarvestBlockInferiors(String[] strings) {
        this.beforeCanHarvestBlockInferiors = strings;
    }

    public void setOverrideCanHarvestBlockSuperiors(String[] strings) {
        this.overrideCanHarvestBlockSuperiors = strings;
    }

    public void setOverrideCanHarvestBlockInferiors(String[] strings) {
        this.overrideCanHarvestBlockInferiors = strings;
    }

    public void setAfterCanHarvestBlockSuperiors(String[] strings) {
        this.afterCanHarvestBlockSuperiors = strings;
    }

    public void setAfterCanHarvestBlockInferiors(String[] strings) {
        this.afterCanHarvestBlockInferiors = strings;
    }

    public String[] getBeforeCanPlayerEditSuperiors() {
        return this.beforeCanPlayerEditSuperiors;
    }

    public String[] getBeforeCanPlayerEditInferiors() {
        return this.beforeCanPlayerEditInferiors;
    }

    public String[] getOverrideCanPlayerEditSuperiors() {
        return this.overrideCanPlayerEditSuperiors;
    }

    public String[] getOverrideCanPlayerEditInferiors() {
        return this.overrideCanPlayerEditInferiors;
    }

    public String[] getAfterCanPlayerEditSuperiors() {
        return this.afterCanPlayerEditSuperiors;
    }

    public String[] getAfterCanPlayerEditInferiors() {
        return this.afterCanPlayerEditInferiors;
    }

    public void setBeforeCanPlayerEditSuperiors(String[] strings) {
        this.beforeCanPlayerEditSuperiors = strings;
    }

    public void setBeforeCanPlayerEditInferiors(String[] strings) {
        this.beforeCanPlayerEditInferiors = strings;
    }

    public void setOverrideCanPlayerEditSuperiors(String[] strings) {
        this.overrideCanPlayerEditSuperiors = strings;
    }

    public void setOverrideCanPlayerEditInferiors(String[] strings) {
        this.overrideCanPlayerEditInferiors = strings;
    }

    public void setAfterCanPlayerEditSuperiors(String[] strings) {
        this.afterCanPlayerEditSuperiors = strings;
    }

    public void setAfterCanPlayerEditInferiors(String[] strings) {
        this.afterCanPlayerEditInferiors = strings;
    }

    public String[] getBeforeCanTriggerWalkingSuperiors() {
        return this.beforeCanTriggerWalkingSuperiors;
    }

    public String[] getBeforeCanTriggerWalkingInferiors() {
        return this.beforeCanTriggerWalkingInferiors;
    }

    public String[] getOverrideCanTriggerWalkingSuperiors() {
        return this.overrideCanTriggerWalkingSuperiors;
    }

    public String[] getOverrideCanTriggerWalkingInferiors() {
        return this.overrideCanTriggerWalkingInferiors;
    }

    public String[] getAfterCanTriggerWalkingSuperiors() {
        return this.afterCanTriggerWalkingSuperiors;
    }

    public String[] getAfterCanTriggerWalkingInferiors() {
        return this.afterCanTriggerWalkingInferiors;
    }

    public void setBeforeCanTriggerWalkingSuperiors(String[] strings) {
        this.beforeCanTriggerWalkingSuperiors = strings;
    }

    public void setBeforeCanTriggerWalkingInferiors(String[] strings) {
        this.beforeCanTriggerWalkingInferiors = strings;
    }

    public void setOverrideCanTriggerWalkingSuperiors(String[] strings) {
        this.overrideCanTriggerWalkingSuperiors = strings;
    }

    public void setOverrideCanTriggerWalkingInferiors(String[] strings) {
        this.overrideCanTriggerWalkingInferiors = strings;
    }

    public void setAfterCanTriggerWalkingSuperiors(String[] strings) {
        this.afterCanTriggerWalkingSuperiors = strings;
    }

    public void setAfterCanTriggerWalkingInferiors(String[] strings) {
        this.afterCanTriggerWalkingInferiors = strings;
    }

    public String[] getBeforeDamageEntitySuperiors() {
        return this.beforeDamageEntitySuperiors;
    }

    public String[] getBeforeDamageEntityInferiors() {
        return this.beforeDamageEntityInferiors;
    }

    public String[] getOverrideDamageEntitySuperiors() {
        return this.overrideDamageEntitySuperiors;
    }

    public String[] getOverrideDamageEntityInferiors() {
        return this.overrideDamageEntityInferiors;
    }

    public String[] getAfterDamageEntitySuperiors() {
        return this.afterDamageEntitySuperiors;
    }

    public String[] getAfterDamageEntityInferiors() {
        return this.afterDamageEntityInferiors;
    }

    public void setBeforeDamageEntitySuperiors(String[] strings) {
        this.beforeDamageEntitySuperiors = strings;
    }

    public void setBeforeDamageEntityInferiors(String[] strings) {
        this.beforeDamageEntityInferiors = strings;
    }

    public void setOverrideDamageEntitySuperiors(String[] strings) {
        this.overrideDamageEntitySuperiors = strings;
    }

    public void setOverrideDamageEntityInferiors(String[] strings) {
        this.overrideDamageEntityInferiors = strings;
    }

    public void setAfterDamageEntitySuperiors(String[] strings) {
        this.afterDamageEntitySuperiors = strings;
    }

    public void setAfterDamageEntityInferiors(String[] strings) {
        this.afterDamageEntityInferiors = strings;
    }

    public String[] getBeforeDisplayGUIChestSuperiors() {
        return this.beforeDisplayGUIChestSuperiors;
    }

    public String[] getBeforeDisplayGUIChestInferiors() {
        return this.beforeDisplayGUIChestInferiors;
    }

    public String[] getOverrideDisplayGUIChestSuperiors() {
        return this.overrideDisplayGUIChestSuperiors;
    }

    public String[] getOverrideDisplayGUIChestInferiors() {
        return this.overrideDisplayGUIChestInferiors;
    }

    public String[] getAfterDisplayGUIChestSuperiors() {
        return this.afterDisplayGUIChestSuperiors;
    }

    public String[] getAfterDisplayGUIChestInferiors() {
        return this.afterDisplayGUIChestInferiors;
    }

    public void setBeforeDisplayGUIChestSuperiors(String[] strings) {
        this.beforeDisplayGUIChestSuperiors = strings;
    }

    public void setBeforeDisplayGUIChestInferiors(String[] strings) {
        this.beforeDisplayGUIChestInferiors = strings;
    }

    public void setOverrideDisplayGUIChestSuperiors(String[] strings) {
        this.overrideDisplayGUIChestSuperiors = strings;
    }

    public void setOverrideDisplayGUIChestInferiors(String[] strings) {
        this.overrideDisplayGUIChestInferiors = strings;
    }

    public void setAfterDisplayGUIChestSuperiors(String[] strings) {
        this.afterDisplayGUIChestSuperiors = strings;
    }

    public void setAfterDisplayGUIChestInferiors(String[] strings) {
        this.afterDisplayGUIChestInferiors = strings;
    }

    public String[] getBeforeDisplayGUIDispenserSuperiors() {
        return this.beforeDisplayGUIDispenserSuperiors;
    }

    public String[] getBeforeDisplayGUIDispenserInferiors() {
        return this.beforeDisplayGUIDispenserInferiors;
    }

    public String[] getOverrideDisplayGUIDispenserSuperiors() {
        return this.overrideDisplayGUIDispenserSuperiors;
    }

    public String[] getOverrideDisplayGUIDispenserInferiors() {
        return this.overrideDisplayGUIDispenserInferiors;
    }

    public String[] getAfterDisplayGUIDispenserSuperiors() {
        return this.afterDisplayGUIDispenserSuperiors;
    }

    public String[] getAfterDisplayGUIDispenserInferiors() {
        return this.afterDisplayGUIDispenserInferiors;
    }

    public void setBeforeDisplayGUIDispenserSuperiors(String[] strings) {
        this.beforeDisplayGUIDispenserSuperiors = strings;
    }

    public void setBeforeDisplayGUIDispenserInferiors(String[] strings) {
        this.beforeDisplayGUIDispenserInferiors = strings;
    }

    public void setOverrideDisplayGUIDispenserSuperiors(String[] strings) {
        this.overrideDisplayGUIDispenserSuperiors = strings;
    }

    public void setOverrideDisplayGUIDispenserInferiors(String[] strings) {
        this.overrideDisplayGUIDispenserInferiors = strings;
    }

    public void setAfterDisplayGUIDispenserSuperiors(String[] strings) {
        this.afterDisplayGUIDispenserSuperiors = strings;
    }

    public void setAfterDisplayGUIDispenserInferiors(String[] strings) {
        this.afterDisplayGUIDispenserInferiors = strings;
    }

    public String[] getBeforeDisplayGUIFurnaceSuperiors() {
        return this.beforeDisplayGUIFurnaceSuperiors;
    }

    public String[] getBeforeDisplayGUIFurnaceInferiors() {
        return this.beforeDisplayGUIFurnaceInferiors;
    }

    public String[] getOverrideDisplayGUIFurnaceSuperiors() {
        return this.overrideDisplayGUIFurnaceSuperiors;
    }

    public String[] getOverrideDisplayGUIFurnaceInferiors() {
        return this.overrideDisplayGUIFurnaceInferiors;
    }

    public String[] getAfterDisplayGUIFurnaceSuperiors() {
        return this.afterDisplayGUIFurnaceSuperiors;
    }

    public String[] getAfterDisplayGUIFurnaceInferiors() {
        return this.afterDisplayGUIFurnaceInferiors;
    }

    public void setBeforeDisplayGUIFurnaceSuperiors(String[] strings) {
        this.beforeDisplayGUIFurnaceSuperiors = strings;
    }

    public void setBeforeDisplayGUIFurnaceInferiors(String[] strings) {
        this.beforeDisplayGUIFurnaceInferiors = strings;
    }

    public void setOverrideDisplayGUIFurnaceSuperiors(String[] strings) {
        this.overrideDisplayGUIFurnaceSuperiors = strings;
    }

    public void setOverrideDisplayGUIFurnaceInferiors(String[] strings) {
        this.overrideDisplayGUIFurnaceInferiors = strings;
    }

    public void setAfterDisplayGUIFurnaceSuperiors(String[] strings) {
        this.afterDisplayGUIFurnaceSuperiors = strings;
    }

    public void setAfterDisplayGUIFurnaceInferiors(String[] strings) {
        this.afterDisplayGUIFurnaceInferiors = strings;
    }

    public String[] getBeforeDisplayWorkbenchGUISuperiors() {
        return this.beforeDisplayWorkbenchGUISuperiors;
    }

    public String[] getBeforeDisplayWorkbenchGUIInferiors() {
        return this.beforeDisplayWorkbenchGUIInferiors;
    }

    public String[] getOverrideDisplayWorkbenchGUISuperiors() {
        return this.overrideDisplayWorkbenchGUISuperiors;
    }

    public String[] getOverrideDisplayWorkbenchGUIInferiors() {
        return this.overrideDisplayWorkbenchGUIInferiors;
    }

    public String[] getAfterDisplayWorkbenchGUISuperiors() {
        return this.afterDisplayWorkbenchGUISuperiors;
    }

    public String[] getAfterDisplayWorkbenchGUIInferiors() {
        return this.afterDisplayWorkbenchGUIInferiors;
    }

    public void setBeforeDisplayWorkbenchGUISuperiors(String[] strings) {
        this.beforeDisplayWorkbenchGUISuperiors = strings;
    }

    public void setBeforeDisplayWorkbenchGUIInferiors(String[] strings) {
        this.beforeDisplayWorkbenchGUIInferiors = strings;
    }

    public void setOverrideDisplayWorkbenchGUISuperiors(String[] strings) {
        this.overrideDisplayWorkbenchGUISuperiors = strings;
    }

    public void setOverrideDisplayWorkbenchGUIInferiors(String[] strings) {
        this.overrideDisplayWorkbenchGUIInferiors = strings;
    }

    public void setAfterDisplayWorkbenchGUISuperiors(String[] strings) {
        this.afterDisplayWorkbenchGUISuperiors = strings;
    }

    public void setAfterDisplayWorkbenchGUIInferiors(String[] strings) {
        this.afterDisplayWorkbenchGUIInferiors = strings;
    }

    public String[] getBeforeDropOneItemSuperiors() {
        return this.beforeDropOneItemSuperiors;
    }

    public String[] getBeforeDropOneItemInferiors() {
        return this.beforeDropOneItemInferiors;
    }

    public String[] getOverrideDropOneItemSuperiors() {
        return this.overrideDropOneItemSuperiors;
    }

    public String[] getOverrideDropOneItemInferiors() {
        return this.overrideDropOneItemInferiors;
    }

    public String[] getAfterDropOneItemSuperiors() {
        return this.afterDropOneItemSuperiors;
    }

    public String[] getAfterDropOneItemInferiors() {
        return this.afterDropOneItemInferiors;
    }

    public void setBeforeDropOneItemSuperiors(String[] strings) {
        this.beforeDropOneItemSuperiors = strings;
    }

    public void setBeforeDropOneItemInferiors(String[] strings) {
        this.beforeDropOneItemInferiors = strings;
    }

    public void setOverrideDropOneItemSuperiors(String[] strings) {
        this.overrideDropOneItemSuperiors = strings;
    }

    public void setOverrideDropOneItemInferiors(String[] strings) {
        this.overrideDropOneItemInferiors = strings;
    }

    public void setAfterDropOneItemSuperiors(String[] strings) {
        this.afterDropOneItemSuperiors = strings;
    }

    public void setAfterDropOneItemInferiors(String[] strings) {
        this.afterDropOneItemInferiors = strings;
    }

    public String[] getBeforeDropPlayerItemSuperiors() {
        return this.beforeDropPlayerItemSuperiors;
    }

    public String[] getBeforeDropPlayerItemInferiors() {
        return this.beforeDropPlayerItemInferiors;
    }

    public String[] getOverrideDropPlayerItemSuperiors() {
        return this.overrideDropPlayerItemSuperiors;
    }

    public String[] getOverrideDropPlayerItemInferiors() {
        return this.overrideDropPlayerItemInferiors;
    }

    public String[] getAfterDropPlayerItemSuperiors() {
        return this.afterDropPlayerItemSuperiors;
    }

    public String[] getAfterDropPlayerItemInferiors() {
        return this.afterDropPlayerItemInferiors;
    }

    public void setBeforeDropPlayerItemSuperiors(String[] strings) {
        this.beforeDropPlayerItemSuperiors = strings;
    }

    public void setBeforeDropPlayerItemInferiors(String[] strings) {
        this.beforeDropPlayerItemInferiors = strings;
    }

    public void setOverrideDropPlayerItemSuperiors(String[] strings) {
        this.overrideDropPlayerItemSuperiors = strings;
    }

    public void setOverrideDropPlayerItemInferiors(String[] strings) {
        this.overrideDropPlayerItemInferiors = strings;
    }

    public void setAfterDropPlayerItemSuperiors(String[] strings) {
        this.afterDropPlayerItemSuperiors = strings;
    }

    public void setAfterDropPlayerItemInferiors(String[] strings) {
        this.afterDropPlayerItemInferiors = strings;
    }

    public String[] getBeforeFallSuperiors() {
        return this.beforeFallSuperiors;
    }

    public String[] getBeforeFallInferiors() {
        return this.beforeFallInferiors;
    }

    public String[] getOverrideFallSuperiors() {
        return this.overrideFallSuperiors;
    }

    public String[] getOverrideFallInferiors() {
        return this.overrideFallInferiors;
    }

    public String[] getAfterFallSuperiors() {
        return this.afterFallSuperiors;
    }

    public String[] getAfterFallInferiors() {
        return this.afterFallInferiors;
    }

    public void setBeforeFallSuperiors(String[] strings) {
        this.beforeFallSuperiors = strings;
    }

    public void setBeforeFallInferiors(String[] strings) {
        this.beforeFallInferiors = strings;
    }

    public void setOverrideFallSuperiors(String[] strings) {
        this.overrideFallSuperiors = strings;
    }

    public void setOverrideFallInferiors(String[] strings) {
        this.overrideFallInferiors = strings;
    }

    public void setAfterFallSuperiors(String[] strings) {
        this.afterFallSuperiors = strings;
    }

    public void setAfterFallInferiors(String[] strings) {
        this.afterFallInferiors = strings;
    }

    public String[] getBeforeGetCurrentPlayerStrVsBlockSuperiors() {
        return this.beforeGetCurrentPlayerStrVsBlockSuperiors;
    }

    public String[] getBeforeGetCurrentPlayerStrVsBlockInferiors() {
        return this.beforeGetCurrentPlayerStrVsBlockInferiors;
    }

    public String[] getOverrideGetCurrentPlayerStrVsBlockSuperiors() {
        return this.overrideGetCurrentPlayerStrVsBlockSuperiors;
    }

    public String[] getOverrideGetCurrentPlayerStrVsBlockInferiors() {
        return this.overrideGetCurrentPlayerStrVsBlockInferiors;
    }

    public String[] getAfterGetCurrentPlayerStrVsBlockSuperiors() {
        return this.afterGetCurrentPlayerStrVsBlockSuperiors;
    }

    public String[] getAfterGetCurrentPlayerStrVsBlockInferiors() {
        return this.afterGetCurrentPlayerStrVsBlockInferiors;
    }

    public void setBeforeGetCurrentPlayerStrVsBlockSuperiors(String[] strings) {
        this.beforeGetCurrentPlayerStrVsBlockSuperiors = strings;
    }

    public void setBeforeGetCurrentPlayerStrVsBlockInferiors(String[] strings) {
        this.beforeGetCurrentPlayerStrVsBlockInferiors = strings;
    }

    public void setOverrideGetCurrentPlayerStrVsBlockSuperiors(String[] strings) {
        this.overrideGetCurrentPlayerStrVsBlockSuperiors = strings;
    }

    public void setOverrideGetCurrentPlayerStrVsBlockInferiors(String[] strings) {
        this.overrideGetCurrentPlayerStrVsBlockInferiors = strings;
    }

    public void setAfterGetCurrentPlayerStrVsBlockSuperiors(String[] strings) {
        this.afterGetCurrentPlayerStrVsBlockSuperiors = strings;
    }

    public void setAfterGetCurrentPlayerStrVsBlockInferiors(String[] strings) {
        this.afterGetCurrentPlayerStrVsBlockInferiors = strings;
    }

    public String[] getBeforeGetDistanceSqSuperiors() {
        return this.beforeGetDistanceSqSuperiors;
    }

    public String[] getBeforeGetDistanceSqInferiors() {
        return this.beforeGetDistanceSqInferiors;
    }

    public String[] getOverrideGetDistanceSqSuperiors() {
        return this.overrideGetDistanceSqSuperiors;
    }

    public String[] getOverrideGetDistanceSqInferiors() {
        return this.overrideGetDistanceSqInferiors;
    }

    public String[] getAfterGetDistanceSqSuperiors() {
        return this.afterGetDistanceSqSuperiors;
    }

    public String[] getAfterGetDistanceSqInferiors() {
        return this.afterGetDistanceSqInferiors;
    }

    public void setBeforeGetDistanceSqSuperiors(String[] strings) {
        this.beforeGetDistanceSqSuperiors = strings;
    }

    public void setBeforeGetDistanceSqInferiors(String[] strings) {
        this.beforeGetDistanceSqInferiors = strings;
    }

    public void setOverrideGetDistanceSqSuperiors(String[] strings) {
        this.overrideGetDistanceSqSuperiors = strings;
    }

    public void setOverrideGetDistanceSqInferiors(String[] strings) {
        this.overrideGetDistanceSqInferiors = strings;
    }

    public void setAfterGetDistanceSqSuperiors(String[] strings) {
        this.afterGetDistanceSqSuperiors = strings;
    }

    public void setAfterGetDistanceSqInferiors(String[] strings) {
        this.afterGetDistanceSqInferiors = strings;
    }

    public String[] getBeforeGetBrightnessSuperiors() {
        return this.beforeGetBrightnessSuperiors;
    }

    public String[] getBeforeGetBrightnessInferiors() {
        return this.beforeGetBrightnessInferiors;
    }

    public String[] getOverrideGetBrightnessSuperiors() {
        return this.overrideGetBrightnessSuperiors;
    }

    public String[] getOverrideGetBrightnessInferiors() {
        return this.overrideGetBrightnessInferiors;
    }

    public String[] getAfterGetBrightnessSuperiors() {
        return this.afterGetBrightnessSuperiors;
    }

    public String[] getAfterGetBrightnessInferiors() {
        return this.afterGetBrightnessInferiors;
    }

    public void setBeforeGetBrightnessSuperiors(String[] strings) {
        this.beforeGetBrightnessSuperiors = strings;
    }

    public void setBeforeGetBrightnessInferiors(String[] strings) {
        this.beforeGetBrightnessInferiors = strings;
    }

    public void setOverrideGetBrightnessSuperiors(String[] strings) {
        this.overrideGetBrightnessSuperiors = strings;
    }

    public void setOverrideGetBrightnessInferiors(String[] strings) {
        this.overrideGetBrightnessInferiors = strings;
    }

    public void setAfterGetBrightnessSuperiors(String[] strings) {
        this.afterGetBrightnessSuperiors = strings;
    }

    public void setAfterGetBrightnessInferiors(String[] strings) {
        this.afterGetBrightnessInferiors = strings;
    }

    public String[] getBeforeGetEyeHeightSuperiors() {
        return this.beforeGetEyeHeightSuperiors;
    }

    public String[] getBeforeGetEyeHeightInferiors() {
        return this.beforeGetEyeHeightInferiors;
    }

    public String[] getOverrideGetEyeHeightSuperiors() {
        return this.overrideGetEyeHeightSuperiors;
    }

    public String[] getOverrideGetEyeHeightInferiors() {
        return this.overrideGetEyeHeightInferiors;
    }

    public String[] getAfterGetEyeHeightSuperiors() {
        return this.afterGetEyeHeightSuperiors;
    }

    public String[] getAfterGetEyeHeightInferiors() {
        return this.afterGetEyeHeightInferiors;
    }

    public void setBeforeGetEyeHeightSuperiors(String[] strings) {
        this.beforeGetEyeHeightSuperiors = strings;
    }

    public void setBeforeGetEyeHeightInferiors(String[] strings) {
        this.beforeGetEyeHeightInferiors = strings;
    }

    public void setOverrideGetEyeHeightSuperiors(String[] strings) {
        this.overrideGetEyeHeightSuperiors = strings;
    }

    public void setOverrideGetEyeHeightInferiors(String[] strings) {
        this.overrideGetEyeHeightInferiors = strings;
    }

    public void setAfterGetEyeHeightSuperiors(String[] strings) {
        this.afterGetEyeHeightSuperiors = strings;
    }

    public void setAfterGetEyeHeightInferiors(String[] strings) {
        this.afterGetEyeHeightInferiors = strings;
    }

    public String[] getBeforeGetSpeedModifierSuperiors() {
        return this.beforeGetSpeedModifierSuperiors;
    }

    public String[] getBeforeGetSpeedModifierInferiors() {
        return this.beforeGetSpeedModifierInferiors;
    }

    public String[] getOverrideGetSpeedModifierSuperiors() {
        return this.overrideGetSpeedModifierSuperiors;
    }

    public String[] getOverrideGetSpeedModifierInferiors() {
        return this.overrideGetSpeedModifierInferiors;
    }

    public String[] getAfterGetSpeedModifierSuperiors() {
        return this.afterGetSpeedModifierSuperiors;
    }

    public String[] getAfterGetSpeedModifierInferiors() {
        return this.afterGetSpeedModifierInferiors;
    }

    public void setBeforeGetSpeedModifierSuperiors(String[] strings) {
        this.beforeGetSpeedModifierSuperiors = strings;
    }

    public void setBeforeGetSpeedModifierInferiors(String[] strings) {
        this.beforeGetSpeedModifierInferiors = strings;
    }

    public void setOverrideGetSpeedModifierSuperiors(String[] strings) {
        this.overrideGetSpeedModifierSuperiors = strings;
    }

    public void setOverrideGetSpeedModifierInferiors(String[] strings) {
        this.overrideGetSpeedModifierInferiors = strings;
    }

    public void setAfterGetSpeedModifierSuperiors(String[] strings) {
        this.afterGetSpeedModifierSuperiors = strings;
    }

    public void setAfterGetSpeedModifierInferiors(String[] strings) {
        this.afterGetSpeedModifierInferiors = strings;
    }

    public String[] getBeforeHealSuperiors() {
        return this.beforeHealSuperiors;
    }

    public String[] getBeforeHealInferiors() {
        return this.beforeHealInferiors;
    }

    public String[] getOverrideHealSuperiors() {
        return this.overrideHealSuperiors;
    }

    public String[] getOverrideHealInferiors() {
        return this.overrideHealInferiors;
    }

    public String[] getAfterHealSuperiors() {
        return this.afterHealSuperiors;
    }

    public String[] getAfterHealInferiors() {
        return this.afterHealInferiors;
    }

    public void setBeforeHealSuperiors(String[] strings) {
        this.beforeHealSuperiors = strings;
    }

    public void setBeforeHealInferiors(String[] strings) {
        this.beforeHealInferiors = strings;
    }

    public void setOverrideHealSuperiors(String[] strings) {
        this.overrideHealSuperiors = strings;
    }

    public void setOverrideHealInferiors(String[] strings) {
        this.overrideHealInferiors = strings;
    }

    public void setAfterHealSuperiors(String[] strings) {
        this.afterHealSuperiors = strings;
    }

    public void setAfterHealInferiors(String[] strings) {
        this.afterHealInferiors = strings;
    }

    public String[] getBeforeInteractSuperiors() {
        return this.beforeInteractSuperiors;
    }

    public String[] getBeforeInteractInferiors() {
        return this.beforeInteractInferiors;
    }

    public String[] getOverrideInteractSuperiors() {
        return this.overrideInteractSuperiors;
    }

    public String[] getOverrideInteractInferiors() {
        return this.overrideInteractInferiors;
    }

    public String[] getAfterInteractSuperiors() {
        return this.afterInteractSuperiors;
    }

    public String[] getAfterInteractInferiors() {
        return this.afterInteractInferiors;
    }

    public void setBeforeInteractSuperiors(String[] strings) {
        this.beforeInteractSuperiors = strings;
    }

    public void setBeforeInteractInferiors(String[] strings) {
        this.beforeInteractInferiors = strings;
    }

    public void setOverrideInteractSuperiors(String[] strings) {
        this.overrideInteractSuperiors = strings;
    }

    public void setOverrideInteractInferiors(String[] strings) {
        this.overrideInteractInferiors = strings;
    }

    public void setAfterInteractSuperiors(String[] strings) {
        this.afterInteractSuperiors = strings;
    }

    public void setAfterInteractInferiors(String[] strings) {
        this.afterInteractInferiors = strings;
    }

    public String[] getBeforeIsEntityInsideOpaqueBlockSuperiors() {
        return this.beforeIsEntityInsideOpaqueBlockSuperiors;
    }

    public String[] getBeforeIsEntityInsideOpaqueBlockInferiors() {
        return this.beforeIsEntityInsideOpaqueBlockInferiors;
    }

    public String[] getOverrideIsEntityInsideOpaqueBlockSuperiors() {
        return this.overrideIsEntityInsideOpaqueBlockSuperiors;
    }

    public String[] getOverrideIsEntityInsideOpaqueBlockInferiors() {
        return this.overrideIsEntityInsideOpaqueBlockInferiors;
    }

    public String[] getAfterIsEntityInsideOpaqueBlockSuperiors() {
        return this.afterIsEntityInsideOpaqueBlockSuperiors;
    }

    public String[] getAfterIsEntityInsideOpaqueBlockInferiors() {
        return this.afterIsEntityInsideOpaqueBlockInferiors;
    }

    public void setBeforeIsEntityInsideOpaqueBlockSuperiors(String[] strings) {
        this.beforeIsEntityInsideOpaqueBlockSuperiors = strings;
    }

    public void setBeforeIsEntityInsideOpaqueBlockInferiors(String[] strings) {
        this.beforeIsEntityInsideOpaqueBlockInferiors = strings;
    }

    public void setOverrideIsEntityInsideOpaqueBlockSuperiors(String[] strings) {
        this.overrideIsEntityInsideOpaqueBlockSuperiors = strings;
    }

    public void setOverrideIsEntityInsideOpaqueBlockInferiors(String[] strings) {
        this.overrideIsEntityInsideOpaqueBlockInferiors = strings;
    }

    public void setAfterIsEntityInsideOpaqueBlockSuperiors(String[] strings) {
        this.afterIsEntityInsideOpaqueBlockSuperiors = strings;
    }

    public void setAfterIsEntityInsideOpaqueBlockInferiors(String[] strings) {
        this.afterIsEntityInsideOpaqueBlockInferiors = strings;
    }

    public String[] getBeforeIsInWaterSuperiors() {
        return this.beforeIsInWaterSuperiors;
    }

    public String[] getBeforeIsInWaterInferiors() {
        return this.beforeIsInWaterInferiors;
    }

    public String[] getOverrideIsInWaterSuperiors() {
        return this.overrideIsInWaterSuperiors;
    }

    public String[] getOverrideIsInWaterInferiors() {
        return this.overrideIsInWaterInferiors;
    }

    public String[] getAfterIsInWaterSuperiors() {
        return this.afterIsInWaterSuperiors;
    }

    public String[] getAfterIsInWaterInferiors() {
        return this.afterIsInWaterInferiors;
    }

    public void setBeforeIsInWaterSuperiors(String[] strings) {
        this.beforeIsInWaterSuperiors = strings;
    }

    public void setBeforeIsInWaterInferiors(String[] strings) {
        this.beforeIsInWaterInferiors = strings;
    }

    public void setOverrideIsInWaterSuperiors(String[] strings) {
        this.overrideIsInWaterSuperiors = strings;
    }

    public void setOverrideIsInWaterInferiors(String[] strings) {
        this.overrideIsInWaterInferiors = strings;
    }

    public void setAfterIsInWaterSuperiors(String[] strings) {
        this.afterIsInWaterSuperiors = strings;
    }

    public void setAfterIsInWaterInferiors(String[] strings) {
        this.afterIsInWaterInferiors = strings;
    }

    public String[] getBeforeIsInsideOfMaterialSuperiors() {
        return this.beforeIsInsideOfMaterialSuperiors;
    }

    public String[] getBeforeIsInsideOfMaterialInferiors() {
        return this.beforeIsInsideOfMaterialInferiors;
    }

    public String[] getOverrideIsInsideOfMaterialSuperiors() {
        return this.overrideIsInsideOfMaterialSuperiors;
    }

    public String[] getOverrideIsInsideOfMaterialInferiors() {
        return this.overrideIsInsideOfMaterialInferiors;
    }

    public String[] getAfterIsInsideOfMaterialSuperiors() {
        return this.afterIsInsideOfMaterialSuperiors;
    }

    public String[] getAfterIsInsideOfMaterialInferiors() {
        return this.afterIsInsideOfMaterialInferiors;
    }

    public void setBeforeIsInsideOfMaterialSuperiors(String[] strings) {
        this.beforeIsInsideOfMaterialSuperiors = strings;
    }

    public void setBeforeIsInsideOfMaterialInferiors(String[] strings) {
        this.beforeIsInsideOfMaterialInferiors = strings;
    }

    public void setOverrideIsInsideOfMaterialSuperiors(String[] strings) {
        this.overrideIsInsideOfMaterialSuperiors = strings;
    }

    public void setOverrideIsInsideOfMaterialInferiors(String[] strings) {
        this.overrideIsInsideOfMaterialInferiors = strings;
    }

    public void setAfterIsInsideOfMaterialSuperiors(String[] strings) {
        this.afterIsInsideOfMaterialSuperiors = strings;
    }

    public void setAfterIsInsideOfMaterialInferiors(String[] strings) {
        this.afterIsInsideOfMaterialInferiors = strings;
    }

    public String[] getBeforeIsOnLadderSuperiors() {
        return this.beforeIsOnLadderSuperiors;
    }

    public String[] getBeforeIsOnLadderInferiors() {
        return this.beforeIsOnLadderInferiors;
    }

    public String[] getOverrideIsOnLadderSuperiors() {
        return this.overrideIsOnLadderSuperiors;
    }

    public String[] getOverrideIsOnLadderInferiors() {
        return this.overrideIsOnLadderInferiors;
    }

    public String[] getAfterIsOnLadderSuperiors() {
        return this.afterIsOnLadderSuperiors;
    }

    public String[] getAfterIsOnLadderInferiors() {
        return this.afterIsOnLadderInferiors;
    }

    public void setBeforeIsOnLadderSuperiors(String[] strings) {
        this.beforeIsOnLadderSuperiors = strings;
    }

    public void setBeforeIsOnLadderInferiors(String[] strings) {
        this.beforeIsOnLadderInferiors = strings;
    }

    public void setOverrideIsOnLadderSuperiors(String[] strings) {
        this.overrideIsOnLadderSuperiors = strings;
    }

    public void setOverrideIsOnLadderInferiors(String[] strings) {
        this.overrideIsOnLadderInferiors = strings;
    }

    public void setAfterIsOnLadderSuperiors(String[] strings) {
        this.afterIsOnLadderSuperiors = strings;
    }

    public void setAfterIsOnLadderInferiors(String[] strings) {
        this.afterIsOnLadderInferiors = strings;
    }

    public String[] getBeforeIsPlayerSleepingSuperiors() {
        return this.beforeIsPlayerSleepingSuperiors;
    }

    public String[] getBeforeIsPlayerSleepingInferiors() {
        return this.beforeIsPlayerSleepingInferiors;
    }

    public String[] getOverrideIsPlayerSleepingSuperiors() {
        return this.overrideIsPlayerSleepingSuperiors;
    }

    public String[] getOverrideIsPlayerSleepingInferiors() {
        return this.overrideIsPlayerSleepingInferiors;
    }

    public String[] getAfterIsPlayerSleepingSuperiors() {
        return this.afterIsPlayerSleepingSuperiors;
    }

    public String[] getAfterIsPlayerSleepingInferiors() {
        return this.afterIsPlayerSleepingInferiors;
    }

    public void setBeforeIsPlayerSleepingSuperiors(String[] strings) {
        this.beforeIsPlayerSleepingSuperiors = strings;
    }

    public void setBeforeIsPlayerSleepingInferiors(String[] strings) {
        this.beforeIsPlayerSleepingInferiors = strings;
    }

    public void setOverrideIsPlayerSleepingSuperiors(String[] strings) {
        this.overrideIsPlayerSleepingSuperiors = strings;
    }

    public void setOverrideIsPlayerSleepingInferiors(String[] strings) {
        this.overrideIsPlayerSleepingInferiors = strings;
    }

    public void setAfterIsPlayerSleepingSuperiors(String[] strings) {
        this.afterIsPlayerSleepingSuperiors = strings;
    }

    public void setAfterIsPlayerSleepingInferiors(String[] strings) {
        this.afterIsPlayerSleepingInferiors = strings;
    }

    public String[] getBeforeJumpSuperiors() {
        return this.beforeJumpSuperiors;
    }

    public String[] getBeforeJumpInferiors() {
        return this.beforeJumpInferiors;
    }

    public String[] getOverrideJumpSuperiors() {
        return this.overrideJumpSuperiors;
    }

    public String[] getOverrideJumpInferiors() {
        return this.overrideJumpInferiors;
    }

    public String[] getAfterJumpSuperiors() {
        return this.afterJumpSuperiors;
    }

    public String[] getAfterJumpInferiors() {
        return this.afterJumpInferiors;
    }

    public void setBeforeJumpSuperiors(String[] strings) {
        this.beforeJumpSuperiors = strings;
    }

    public void setBeforeJumpInferiors(String[] strings) {
        this.beforeJumpInferiors = strings;
    }

    public void setOverrideJumpSuperiors(String[] strings) {
        this.overrideJumpSuperiors = strings;
    }

    public void setOverrideJumpInferiors(String[] strings) {
        this.overrideJumpInferiors = strings;
    }

    public void setAfterJumpSuperiors(String[] strings) {
        this.afterJumpSuperiors = strings;
    }

    public void setAfterJumpInferiors(String[] strings) {
        this.afterJumpInferiors = strings;
    }

    public String[] getBeforeMoveEntitySuperiors() {
        return this.beforeMoveEntitySuperiors;
    }

    public String[] getBeforeMoveEntityInferiors() {
        return this.beforeMoveEntityInferiors;
    }

    public String[] getOverrideMoveEntitySuperiors() {
        return this.overrideMoveEntitySuperiors;
    }

    public String[] getOverrideMoveEntityInferiors() {
        return this.overrideMoveEntityInferiors;
    }

    public String[] getAfterMoveEntitySuperiors() {
        return this.afterMoveEntitySuperiors;
    }

    public String[] getAfterMoveEntityInferiors() {
        return this.afterMoveEntityInferiors;
    }

    public void setBeforeMoveEntitySuperiors(String[] strings) {
        this.beforeMoveEntitySuperiors = strings;
    }

    public void setBeforeMoveEntityInferiors(String[] strings) {
        this.beforeMoveEntityInferiors = strings;
    }

    public void setOverrideMoveEntitySuperiors(String[] strings) {
        this.overrideMoveEntitySuperiors = strings;
    }

    public void setOverrideMoveEntityInferiors(String[] strings) {
        this.overrideMoveEntityInferiors = strings;
    }

    public void setAfterMoveEntitySuperiors(String[] strings) {
        this.afterMoveEntitySuperiors = strings;
    }

    public void setAfterMoveEntityInferiors(String[] strings) {
        this.afterMoveEntityInferiors = strings;
    }

    public String[] getBeforeMoveEntityWithHeadingSuperiors() {
        return this.beforeMoveEntityWithHeadingSuperiors;
    }

    public String[] getBeforeMoveEntityWithHeadingInferiors() {
        return this.beforeMoveEntityWithHeadingInferiors;
    }

    public String[] getOverrideMoveEntityWithHeadingSuperiors() {
        return this.overrideMoveEntityWithHeadingSuperiors;
    }

    public String[] getOverrideMoveEntityWithHeadingInferiors() {
        return this.overrideMoveEntityWithHeadingInferiors;
    }

    public String[] getAfterMoveEntityWithHeadingSuperiors() {
        return this.afterMoveEntityWithHeadingSuperiors;
    }

    public String[] getAfterMoveEntityWithHeadingInferiors() {
        return this.afterMoveEntityWithHeadingInferiors;
    }

    public void setBeforeMoveEntityWithHeadingSuperiors(String[] strings) {
        this.beforeMoveEntityWithHeadingSuperiors = strings;
    }

    public void setBeforeMoveEntityWithHeadingInferiors(String[] strings) {
        this.beforeMoveEntityWithHeadingInferiors = strings;
    }

    public void setOverrideMoveEntityWithHeadingSuperiors(String[] strings) {
        this.overrideMoveEntityWithHeadingSuperiors = strings;
    }

    public void setOverrideMoveEntityWithHeadingInferiors(String[] strings) {
        this.overrideMoveEntityWithHeadingInferiors = strings;
    }

    public void setAfterMoveEntityWithHeadingSuperiors(String[] strings) {
        this.afterMoveEntityWithHeadingSuperiors = strings;
    }

    public void setAfterMoveEntityWithHeadingInferiors(String[] strings) {
        this.afterMoveEntityWithHeadingInferiors = strings;
    }

    public String[] getBeforeMoveFlyingSuperiors() {
        return this.beforeMoveFlyingSuperiors;
    }

    public String[] getBeforeMoveFlyingInferiors() {
        return this.beforeMoveFlyingInferiors;
    }

    public String[] getOverrideMoveFlyingSuperiors() {
        return this.overrideMoveFlyingSuperiors;
    }

    public String[] getOverrideMoveFlyingInferiors() {
        return this.overrideMoveFlyingInferiors;
    }

    public String[] getAfterMoveFlyingSuperiors() {
        return this.afterMoveFlyingSuperiors;
    }

    public String[] getAfterMoveFlyingInferiors() {
        return this.afterMoveFlyingInferiors;
    }

    public void setBeforeMoveFlyingSuperiors(String[] strings) {
        this.beforeMoveFlyingSuperiors = strings;
    }

    public void setBeforeMoveFlyingInferiors(String[] strings) {
        this.beforeMoveFlyingInferiors = strings;
    }

    public void setOverrideMoveFlyingSuperiors(String[] strings) {
        this.overrideMoveFlyingSuperiors = strings;
    }

    public void setOverrideMoveFlyingInferiors(String[] strings) {
        this.overrideMoveFlyingInferiors = strings;
    }

    public void setAfterMoveFlyingSuperiors(String[] strings) {
        this.afterMoveFlyingSuperiors = strings;
    }

    public void setAfterMoveFlyingInferiors(String[] strings) {
        this.afterMoveFlyingInferiors = strings;
    }

    public String[] getBeforeOnDeathSuperiors() {
        return this.beforeOnDeathSuperiors;
    }

    public String[] getBeforeOnDeathInferiors() {
        return this.beforeOnDeathInferiors;
    }

    public String[] getOverrideOnDeathSuperiors() {
        return this.overrideOnDeathSuperiors;
    }

    public String[] getOverrideOnDeathInferiors() {
        return this.overrideOnDeathInferiors;
    }

    public String[] getAfterOnDeathSuperiors() {
        return this.afterOnDeathSuperiors;
    }

    public String[] getAfterOnDeathInferiors() {
        return this.afterOnDeathInferiors;
    }

    public void setBeforeOnDeathSuperiors(String[] strings) {
        this.beforeOnDeathSuperiors = strings;
    }

    public void setBeforeOnDeathInferiors(String[] strings) {
        this.beforeOnDeathInferiors = strings;
    }

    public void setOverrideOnDeathSuperiors(String[] strings) {
        this.overrideOnDeathSuperiors = strings;
    }

    public void setOverrideOnDeathInferiors(String[] strings) {
        this.overrideOnDeathInferiors = strings;
    }

    public void setAfterOnDeathSuperiors(String[] strings) {
        this.afterOnDeathSuperiors = strings;
    }

    public void setAfterOnDeathInferiors(String[] strings) {
        this.afterOnDeathInferiors = strings;
    }

    public String[] getBeforeOnLivingUpdateSuperiors() {
        return this.beforeOnLivingUpdateSuperiors;
    }

    public String[] getBeforeOnLivingUpdateInferiors() {
        return this.beforeOnLivingUpdateInferiors;
    }

    public String[] getOverrideOnLivingUpdateSuperiors() {
        return this.overrideOnLivingUpdateSuperiors;
    }

    public String[] getOverrideOnLivingUpdateInferiors() {
        return this.overrideOnLivingUpdateInferiors;
    }

    public String[] getAfterOnLivingUpdateSuperiors() {
        return this.afterOnLivingUpdateSuperiors;
    }

    public String[] getAfterOnLivingUpdateInferiors() {
        return this.afterOnLivingUpdateInferiors;
    }

    public void setBeforeOnLivingUpdateSuperiors(String[] strings) {
        this.beforeOnLivingUpdateSuperiors = strings;
    }

    public void setBeforeOnLivingUpdateInferiors(String[] strings) {
        this.beforeOnLivingUpdateInferiors = strings;
    }

    public void setOverrideOnLivingUpdateSuperiors(String[] strings) {
        this.overrideOnLivingUpdateSuperiors = strings;
    }

    public void setOverrideOnLivingUpdateInferiors(String[] strings) {
        this.overrideOnLivingUpdateInferiors = strings;
    }

    public void setAfterOnLivingUpdateSuperiors(String[] strings) {
        this.afterOnLivingUpdateSuperiors = strings;
    }

    public void setAfterOnLivingUpdateInferiors(String[] strings) {
        this.afterOnLivingUpdateInferiors = strings;
    }

    public String[] getBeforeOnKillEntitySuperiors() {
        return this.beforeOnKillEntitySuperiors;
    }

    public String[] getBeforeOnKillEntityInferiors() {
        return this.beforeOnKillEntityInferiors;
    }

    public String[] getOverrideOnKillEntitySuperiors() {
        return this.overrideOnKillEntitySuperiors;
    }

    public String[] getOverrideOnKillEntityInferiors() {
        return this.overrideOnKillEntityInferiors;
    }

    public String[] getAfterOnKillEntitySuperiors() {
        return this.afterOnKillEntitySuperiors;
    }

    public String[] getAfterOnKillEntityInferiors() {
        return this.afterOnKillEntityInferiors;
    }

    public void setBeforeOnKillEntitySuperiors(String[] strings) {
        this.beforeOnKillEntitySuperiors = strings;
    }

    public void setBeforeOnKillEntityInferiors(String[] strings) {
        this.beforeOnKillEntityInferiors = strings;
    }

    public void setOverrideOnKillEntitySuperiors(String[] strings) {
        this.overrideOnKillEntitySuperiors = strings;
    }

    public void setOverrideOnKillEntityInferiors(String[] strings) {
        this.overrideOnKillEntityInferiors = strings;
    }

    public void setAfterOnKillEntitySuperiors(String[] strings) {
        this.afterOnKillEntitySuperiors = strings;
    }

    public void setAfterOnKillEntityInferiors(String[] strings) {
        this.afterOnKillEntityInferiors = strings;
    }

    public String[] getBeforeOnUpdateSuperiors() {
        return this.beforeOnUpdateSuperiors;
    }

    public String[] getBeforeOnUpdateInferiors() {
        return this.beforeOnUpdateInferiors;
    }

    public String[] getOverrideOnUpdateSuperiors() {
        return this.overrideOnUpdateSuperiors;
    }

    public String[] getOverrideOnUpdateInferiors() {
        return this.overrideOnUpdateInferiors;
    }

    public String[] getAfterOnUpdateSuperiors() {
        return this.afterOnUpdateSuperiors;
    }

    public String[] getAfterOnUpdateInferiors() {
        return this.afterOnUpdateInferiors;
    }

    public void setBeforeOnUpdateSuperiors(String[] strings) {
        this.beforeOnUpdateSuperiors = strings;
    }

    public void setBeforeOnUpdateInferiors(String[] strings) {
        this.beforeOnUpdateInferiors = strings;
    }

    public void setOverrideOnUpdateSuperiors(String[] strings) {
        this.overrideOnUpdateSuperiors = strings;
    }

    public void setOverrideOnUpdateInferiors(String[] strings) {
        this.overrideOnUpdateInferiors = strings;
    }

    public void setAfterOnUpdateSuperiors(String[] strings) {
        this.afterOnUpdateSuperiors = strings;
    }

    public void setAfterOnUpdateInferiors(String[] strings) {
        this.afterOnUpdateInferiors = strings;
    }

    public String[] getBeforeOnUpdateEntitySuperiors() {
        return this.beforeOnUpdateEntitySuperiors;
    }

    public String[] getBeforeOnUpdateEntityInferiors() {
        return this.beforeOnUpdateEntityInferiors;
    }

    public String[] getOverrideOnUpdateEntitySuperiors() {
        return this.overrideOnUpdateEntitySuperiors;
    }

    public String[] getOverrideOnUpdateEntityInferiors() {
        return this.overrideOnUpdateEntityInferiors;
    }

    public String[] getAfterOnUpdateEntitySuperiors() {
        return this.afterOnUpdateEntitySuperiors;
    }

    public String[] getAfterOnUpdateEntityInferiors() {
        return this.afterOnUpdateEntityInferiors;
    }

    public void setBeforeOnUpdateEntitySuperiors(String[] strings) {
        this.beforeOnUpdateEntitySuperiors = strings;
    }

    public void setBeforeOnUpdateEntityInferiors(String[] strings) {
        this.beforeOnUpdateEntityInferiors = strings;
    }

    public void setOverrideOnUpdateEntitySuperiors(String[] strings) {
        this.overrideOnUpdateEntitySuperiors = strings;
    }

    public void setOverrideOnUpdateEntityInferiors(String[] strings) {
        this.overrideOnUpdateEntityInferiors = strings;
    }

    public void setAfterOnUpdateEntitySuperiors(String[] strings) {
        this.afterOnUpdateEntitySuperiors = strings;
    }

    public void setAfterOnUpdateEntityInferiors(String[] strings) {
        this.afterOnUpdateEntityInferiors = strings;
    }

    public String[] getBeforeReadEntityFromNBTSuperiors() {
        return this.beforeReadEntityFromNBTSuperiors;
    }

    public String[] getBeforeReadEntityFromNBTInferiors() {
        return this.beforeReadEntityFromNBTInferiors;
    }

    public String[] getOverrideReadEntityFromNBTSuperiors() {
        return this.overrideReadEntityFromNBTSuperiors;
    }

    public String[] getOverrideReadEntityFromNBTInferiors() {
        return this.overrideReadEntityFromNBTInferiors;
    }

    public String[] getAfterReadEntityFromNBTSuperiors() {
        return this.afterReadEntityFromNBTSuperiors;
    }

    public String[] getAfterReadEntityFromNBTInferiors() {
        return this.afterReadEntityFromNBTInferiors;
    }

    public void setBeforeReadEntityFromNBTSuperiors(String[] strings) {
        this.beforeReadEntityFromNBTSuperiors = strings;
    }

    public void setBeforeReadEntityFromNBTInferiors(String[] strings) {
        this.beforeReadEntityFromNBTInferiors = strings;
    }

    public void setOverrideReadEntityFromNBTSuperiors(String[] strings) {
        this.overrideReadEntityFromNBTSuperiors = strings;
    }

    public void setOverrideReadEntityFromNBTInferiors(String[] strings) {
        this.overrideReadEntityFromNBTInferiors = strings;
    }

    public void setAfterReadEntityFromNBTSuperiors(String[] strings) {
        this.afterReadEntityFromNBTSuperiors = strings;
    }

    public void setAfterReadEntityFromNBTInferiors(String[] strings) {
        this.afterReadEntityFromNBTInferiors = strings;
    }

    public String[] getBeforeSetDeadSuperiors() {
        return this.beforeSetDeadSuperiors;
    }

    public String[] getBeforeSetDeadInferiors() {
        return this.beforeSetDeadInferiors;
    }

    public String[] getOverrideSetDeadSuperiors() {
        return this.overrideSetDeadSuperiors;
    }

    public String[] getOverrideSetDeadInferiors() {
        return this.overrideSetDeadInferiors;
    }

    public String[] getAfterSetDeadSuperiors() {
        return this.afterSetDeadSuperiors;
    }

    public String[] getAfterSetDeadInferiors() {
        return this.afterSetDeadInferiors;
    }

    public void setBeforeSetDeadSuperiors(String[] strings) {
        this.beforeSetDeadSuperiors = strings;
    }

    public void setBeforeSetDeadInferiors(String[] strings) {
        this.beforeSetDeadInferiors = strings;
    }

    public void setOverrideSetDeadSuperiors(String[] strings) {
        this.overrideSetDeadSuperiors = strings;
    }

    public void setOverrideSetDeadInferiors(String[] strings) {
        this.overrideSetDeadInferiors = strings;
    }

    public void setAfterSetDeadSuperiors(String[] strings) {
        this.afterSetDeadSuperiors = strings;
    }

    public void setAfterSetDeadInferiors(String[] strings) {
        this.afterSetDeadInferiors = strings;
    }

    public String[] getBeforeSetPositionSuperiors() {
        return this.beforeSetPositionSuperiors;
    }

    public String[] getBeforeSetPositionInferiors() {
        return this.beforeSetPositionInferiors;
    }

    public String[] getOverrideSetPositionSuperiors() {
        return this.overrideSetPositionSuperiors;
    }

    public String[] getOverrideSetPositionInferiors() {
        return this.overrideSetPositionInferiors;
    }

    public String[] getAfterSetPositionSuperiors() {
        return this.afterSetPositionSuperiors;
    }

    public String[] getAfterSetPositionInferiors() {
        return this.afterSetPositionInferiors;
    }

    public void setBeforeSetPositionSuperiors(String[] strings) {
        this.beforeSetPositionSuperiors = strings;
    }

    public void setBeforeSetPositionInferiors(String[] strings) {
        this.beforeSetPositionInferiors = strings;
    }

    public void setOverrideSetPositionSuperiors(String[] strings) {
        this.overrideSetPositionSuperiors = strings;
    }

    public void setOverrideSetPositionInferiors(String[] strings) {
        this.overrideSetPositionInferiors = strings;
    }

    public void setAfterSetPositionSuperiors(String[] strings) {
        this.afterSetPositionSuperiors = strings;
    }

    public void setAfterSetPositionInferiors(String[] strings) {
        this.afterSetPositionInferiors = strings;
    }

    public String[] getBeforeUpdateEntityActionStateSuperiors() {
        return this.beforeUpdateEntityActionStateSuperiors;
    }

    public String[] getBeforeUpdateEntityActionStateInferiors() {
        return this.beforeUpdateEntityActionStateInferiors;
    }

    public String[] getOverrideUpdateEntityActionStateSuperiors() {
        return this.overrideUpdateEntityActionStateSuperiors;
    }

    public String[] getOverrideUpdateEntityActionStateInferiors() {
        return this.overrideUpdateEntityActionStateInferiors;
    }

    public String[] getAfterUpdateEntityActionStateSuperiors() {
        return this.afterUpdateEntityActionStateSuperiors;
    }

    public String[] getAfterUpdateEntityActionStateInferiors() {
        return this.afterUpdateEntityActionStateInferiors;
    }

    public void setBeforeUpdateEntityActionStateSuperiors(String[] strings) {
        this.beforeUpdateEntityActionStateSuperiors = strings;
    }

    public void setBeforeUpdateEntityActionStateInferiors(String[] strings) {
        this.beforeUpdateEntityActionStateInferiors = strings;
    }

    public void setOverrideUpdateEntityActionStateSuperiors(String[] strings) {
        this.overrideUpdateEntityActionStateSuperiors = strings;
    }

    public void setOverrideUpdateEntityActionStateInferiors(String[] strings) {
        this.overrideUpdateEntityActionStateInferiors = strings;
    }

    public void setAfterUpdateEntityActionStateSuperiors(String[] strings) {
        this.afterUpdateEntityActionStateSuperiors = strings;
    }

    public void setAfterUpdateEntityActionStateInferiors(String[] strings) {
        this.afterUpdateEntityActionStateInferiors = strings;
    }

    public String[] getBeforeWriteEntityToNBTSuperiors() {
        return this.beforeWriteEntityToNBTSuperiors;
    }

    public String[] getBeforeWriteEntityToNBTInferiors() {
        return this.beforeWriteEntityToNBTInferiors;
    }

    public String[] getOverrideWriteEntityToNBTSuperiors() {
        return this.overrideWriteEntityToNBTSuperiors;
    }

    public String[] getOverrideWriteEntityToNBTInferiors() {
        return this.overrideWriteEntityToNBTInferiors;
    }

    public String[] getAfterWriteEntityToNBTSuperiors() {
        return this.afterWriteEntityToNBTSuperiors;
    }

    public String[] getAfterWriteEntityToNBTInferiors() {
        return this.afterWriteEntityToNBTInferiors;
    }

    public void setBeforeWriteEntityToNBTSuperiors(String[] strings) {
        this.beforeWriteEntityToNBTSuperiors = strings;
    }

    public void setBeforeWriteEntityToNBTInferiors(String[] strings) {
        this.beforeWriteEntityToNBTInferiors = strings;
    }

    public void setOverrideWriteEntityToNBTSuperiors(String[] strings) {
        this.overrideWriteEntityToNBTSuperiors = strings;
    }

    public void setOverrideWriteEntityToNBTInferiors(String[] strings) {
        this.overrideWriteEntityToNBTInferiors = strings;
    }

    public void setAfterWriteEntityToNBTSuperiors(String[] strings) {
        this.afterWriteEntityToNBTSuperiors = strings;
    }

    public void setAfterWriteEntityToNBTInferiors(String[] strings) {
        this.afterWriteEntityToNBTInferiors = strings;
    }
}
