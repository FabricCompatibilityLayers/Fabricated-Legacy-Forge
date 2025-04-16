package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.src.EntityAgeable;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.MerchantRecipeList;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityVillager.class)
public abstract class EntityVillagerMixin extends EntityAgeable {
    public EntityVillagerMixin(World p_i3436_1_) {
        super(p_i3436_1_);
    }

    @Shadow public abstract int func_70946_n();

    @ModifyExpressionValue(method = "func_70073_O", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityAgeable;func_70073_O()Ljava/lang/String;"))
    private String fml$getVillagerSkin(String original) {
        return VillagerRegistry.getVillagerSkin(this.func_70946_n(), original);
    }

    @Inject(method = "func_70950_c", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/MerchantRecipeList;isEmpty()Z"))
    private void fml$manageVillagerTrades(int p_70950_1_, CallbackInfo ci, @Local MerchantRecipeList var2) {
        VillagerRegistry.manageVillagerTrades(var2, (EntityVillager)(Object) this, this.func_70946_n(), this.field_70146_Z);
    }
}
