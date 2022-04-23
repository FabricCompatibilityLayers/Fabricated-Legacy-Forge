package fr.catcore.fabricatedmodloader.mixin.modloader.common;

import modloader.ModLoader;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
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

    /**
     * @author
     */
    @Overwrite
    public void method_545() {
        boolean var1 = this.fuelTime > 0;
        boolean var2 = false;
        if (this.fuelTime > 0) {
            --this.fuelTime;
        }

        if (!this.world.isClient) {
            if (this.fuelTime == 0 && this.method_525()) {
                this.totalFuelTime = this.fuelTime = getBurnTime(this.stacks[1]);
                if (this.fuelTime > 0) {
                    var2 = true;
                    if (this.stacks[1] != null) {
                        --this.stacks[1].count;
                        if (this.stacks[1].count <= 0) {
                            Item var3 = this.stacks[1].getItem().getRecipeRemainder();
                            this.stacks[1] = var3 != null ? new ItemStack(var3) : null;
                        }
                    }
                }
            }

            if (this.isFueled() && this.method_525()) {
                ++this.field_547;
                if (this.field_547 == 200) {
                    this.field_547 = 0;
                    this.method_524();
                    var2 = true;
                }
            } else {
                this.field_547 = 0;
            }

            if (var1 != this.fuelTime > 0) {
                var2 = true;
                FurnaceBlock.method_321(this.fuelTime > 0, this.world, this.x, this.y, this.z);
            }
        }

        if (var2) {
            this.markDirty();
        }

    }
}
