package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.SaveHandler;
import net.minecraft.src.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SaveHandler.class)
public class SaveHandlerMixin {
    @ModifyReturnValue(method = "func_75757_d", at = {@At(value = "RETURN", ordinal = 0), @At(value = "RETURN", ordinal = 1)})
    private WorldInfo fml$handleWorldDataLoad(WorldInfo original, @Local(ordinal = 0) NBTTagCompound var2) {
        if (original != null) {
            FMLCommonHandler.instance().handleWorldDataLoad((SaveHandler) (Object) this, original, var2);
        }

        return original;
    }

    @WrapOperation(method = "func_75755_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/NBTTagCompound;func_74782_a(Ljava/lang/String;Lnet/minecraft/src/NBTBase;)V"))
    private void fml$handleWorldDataSave1(NBTTagCompound instance, String p_74782_2_, NBTBase nbtBase, Operation<Void> original, @Local(argsOnly = true) WorldInfo p_75755_1_, @Local(ordinal = 2) NBTTagCompound var4) {
        original.call(instance, p_74782_2_, nbtBase);
        FMLCommonHandler.instance().handleWorldDataSave((SaveHandler) (Object) this, p_75755_1_, var4);
    }

    @WrapOperation(method = "func_75761_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/NBTTagCompound;func_74782_a(Ljava/lang/String;Lnet/minecraft/src/NBTBase;)V"))
    private void fml$handleWorldDataSave2(NBTTagCompound instance, String p_74782_2_, NBTBase nbtBase, Operation<Void> original, @Local(argsOnly = true) WorldInfo p_75761_1_, @Local(ordinal = 1) NBTTagCompound var3) {
        original.call(instance, p_74782_2_, nbtBase);
        FMLCommonHandler.instance().handleWorldDataSave((SaveHandler) (Object) this, p_75761_1_, var3);
    }
}
