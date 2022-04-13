package fr.catcore.fabricatedforge.mixin.modloader.common;

import modloader.ModLoader;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MinecartItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {

    @Shadow private Random random;

    @Inject(method = "method_298", at = @At("RETURN"), cancellable = true)
    private static void modLoaderDispenserBehavior(DispenserBlockEntity dispenserBlockEntity, World world, ItemStack itemStack, Random random, int i, int j, int k, int l, int m, double d, double e, double f, CallbackInfoReturnable<Integer> cir) {
        int result = cir.getReturnValue();

        if (result == 0
                && (itemStack.id != Item.LAVA_BUCKET.id && itemStack.id != Item.WATER_BUCKET.id)

                && !(itemStack.id == Item.BUCKET.id)
                && !(itemStack.getItem() instanceof MinecartItem)
                && !(itemStack.id == Item.BOAT.id)
        ) {
            cir.setReturnValue(ModLoader.dispenseEntity(world, itemStack, random, i, j, k, l, m, d, e, f));
        }
    }
}
