package fr.catcore.fabricatedmodloader.mixin.modloader.common;

import modloader.ModLoader;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FurnaceBlockEntity.class)
public abstract class FurnaceBlockEntityMixin extends BlockEntity implements Inventory {

    @Shadow
    protected abstract boolean method_525();

    @Shadow
    private ItemStack[] stacks;

    @Shadow public int fuelTime;

    @Shadow
    public static int getBurnTime(ItemStack stack) {
        return 0;
    }

    @Shadow public int totalFuelTime;

    @Shadow public abstract boolean isFueled();

    @Shadow public int field_547;

    @Unique
    private Item cachedRecipeReminder = null;

    @Inject(
            method = "method_524",
            at = @At(value = "FIELD", opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/item/ItemStack;count:I", shift = At.Shift.AFTER)
    )
    private void modLoader$fixStack(CallbackInfo ci) {
        if (this.stacks[0].count <= 0) this.cachedRecipeReminder = this.stacks[0].getItem().getRecipeRemainder();
    }

    @Inject(method = "method_524", at = @At("RETURN"))
    private void modLoader$fixStackPart2(CallbackInfo ci) {
        if (this.method_525() && this.cachedRecipeReminder != null && this.stacks[0] == null) {
            this.stacks[0] = new ItemStack(this.cachedRecipeReminder);
            this.cachedRecipeReminder = null;
        }
    }

//    /**
//     * @author
//     */
//    @Overwrite
//    public void method_524() {
//        if (this.method_525()) {
//            ItemStack var1 = SmeltingRecipeRegistry.getInstance().method_3490(this.stacks[0].getItem().id);
//            if (this.stacks[2] == null) {
//                this.stacks[2] = var1.copy();
//            } else if (this.stacks[2].id == var1.id) {
//                ++this.stacks[2].count;
//            }
//
//            --this.stacks[0].count;
//            if (this.stacks[0].count <= 0) {
//                Item item = this.stacks[0].getItem().getRecipeRemainder();
//                this.stacks[0] = item != null ? new ItemStack(item) : null;
//            }
//
//        }
//    }

    @Inject(method = "getBurnTime", at = @At("RETURN"), cancellable = true)
    private static void modLoaderBurnTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        int result = cir.getReturnValue();

        if (result == 0 && stack != null) {
            cir.setReturnValue(ModLoader.addAllFuel(stack.id, stack.getMeta()));
        }
    }

    @Inject(method = "method_545", at = @At(
            value = "FIELD",
            opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/item/ItemStack;count:I",
            ordinal = 0,
            shift = At.Shift.AFTER
    ))
    private void modLoader$fixStackSecond(CallbackInfo ci) {
        if (this.stacks[1].count < 0) {
            this.stacks[1].count = 0;
        }
    }
}
