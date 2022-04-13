package fr.catcore.fabricatedforge.mixin.modloader.common;

import modloader.ModLoader;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FurnaceBlockEntity.class)
public abstract class FurnaceBlockEntityMixin {

    @Shadow protected abstract boolean method_525();

    @Shadow private ItemStack[] stacks;

    /**
     * @author
     */
    @Overwrite
    public void method_524() {
        if (this.method_525()) {
            ItemStack var1 = SmeltingRecipeRegistry.getInstance().method_3490(this.stacks[0].getItem().id);
            if (this.stacks[2] == null) {
                this.stacks[2] = var1.copy();
            } else if (this.stacks[2].id == var1.id) {
                ++this.stacks[2].count;
            }

            --this.stacks[0].count;
            if (this.stacks[0].count <= 0) {
                Item item = this.stacks[0].getItem().getRecipeRemainder();
                this.stacks[0] = item != null ? new ItemStack(item) : null;
            }

        }
    }

    @Inject(method = "getBurnTime", at = @At("RETURN"), cancellable = true)
    private static void modLoaderBurnTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        int result = cir.getReturnValue();

        if (result == 0 && stack != null) {
            cir.setReturnValue(ModLoader.addAllFuel(stack.id, stack.getMeta()));
        }
    }
}
