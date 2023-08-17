package fr.catcore.fabricatedforge.compat.mixin.nei;

import codechicken.nei.recipe.FurnaceRecipeHandler;
import fr.catcore.modremapperapi.remapping.RemapUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.reflect.Method;

@Mixin(FurnaceRecipeHandler.class)
public class FurnaceRecipeHandlerMixin {
    @Redirect(method = "findFuels", remap = false, at = @At(value = "INVOKE", target = "Ljava/lang/Class;getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;"))
    private static Method fixFindFuels(Class<?> instance, String name, Class<?>[] parameterTypes) throws NoSuchMethodException {
        name = RemapUtil.getRemappedMethodName(instance, name, parameterTypes);

        return instance.getDeclaredMethod(name, parameterTypes);
    }
}
