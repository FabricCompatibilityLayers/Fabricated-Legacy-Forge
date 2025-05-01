package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.SaveHandler;
import net.minecraft.src.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SaveHandler.class)
public class SaveHandlerMixin {
    @ModifyReturnValue(method = "func_75757_d", at = @At("RETURN"))
    private WorldInfo fml$handleWorldDataLoad(WorldInfo original, @Local(ordinal = 0) NBTTagCompound var2) {
        if (original != null) {
            FMLCommonHandler.instance().handleWorldDataLoad((SaveHandler) (Object) this, original, var2);
        }

        return original;
    }

    @Inject(method = "func_75755_a", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/src/NBTTagCompound;func_74782_a(Ljava/lang/String;Lnet/minecraft/src/NBTBase;)V"))
    private void fml$handleWorldDataSave(WorldInfo p_75755_1_, NBTTagCompound p_75755_2_, CallbackInfo ci, @Local(ordinal = 2) NBTTagCompound var4) {
        FMLCommonHandler.instance().handleWorldDataSave((SaveHandler) (Object) this, p_75755_1_, var4);
    }

    @Inject(method = "func_75761_a", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/src/NBTTagCompound;func_74782_a(Ljava/lang/String;Lnet/minecraft/src/NBTBase;)V"))
    private void fml$handleWorldDataSave(WorldInfo p_75761_1_, CallbackInfo ci, @Local(ordinal = 1) NBTTagCompound var3) {
        FMLCommonHandler.instance().handleWorldDataSave((SaveHandler) (Object) this, p_75761_1_, var3);
    }
}
