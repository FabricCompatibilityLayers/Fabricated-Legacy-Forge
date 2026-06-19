/**
 * Copyright (C) 2025-2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityAgeable;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.MerchantRecipeList;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityVillager.class)
public abstract class EntityVillagerMixin extends EntityAgeable {
    public EntityVillagerMixin(World p_i3436_1_) {
        super(p_i3436_1_);
    }

    @Shadow public abstract int func_70946_n();

    @Environment(EnvType.CLIENT)
    @ModifyExpressionValue(method = "func_70073_O", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityAgeable;func_70073_O()Ljava/lang/String;"))
    private String fml$getVillagerSkin(String original) {
        return VillagerRegistry.getVillagerSkin(this.func_70946_n(), original);
    }

    @Definition(id = "MerchantRecipeList", type = MerchantRecipeList.class)
    @Expression("new MerchantRecipeList()")
    @WrapOperation(method = "func_70950_c", at = @At("MIXINEXTRAS:EXPRESSION"))
    private MerchantRecipeList fml$manageVillagerTrades(Operation<MerchantRecipeList> original) {
        MerchantRecipeList var2 = original.call();
        VillagerRegistry.manageVillagerTrades(var2, (EntityVillager)(Object) this, this.func_70946_n(), this.field_70146_Z);
        return var2;
    }

    /**
     * @author CatCore
     * @reason The original logic is fully replaced
     */
    @Overwrite
    public void func_82163_bD() {
        VillagerRegistry.applyRandomTrade((EntityVillager) (Object) this, field_70170_p.field_73012_v);
    }
}
