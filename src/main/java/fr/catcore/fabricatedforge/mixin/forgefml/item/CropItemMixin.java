package fr.catcore.fabricatedforge.mixin.forgefml.item;

import net.minecraft.item.CropItem;
import net.minecraft.item.FoodItem;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CropItem.class)
public abstract class CropItemMixin extends FoodItem implements IPlantable {
    @Shadow private int field_5450;

    public CropItemMixin(int i, int j, float f, boolean bl) {
        super(i, j, f, bl);
    }

    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z) {
        return EnumPlantType.Crop;
    }

    @Override
    public int getPlantID(World world, int x, int y, int z) {
        return this.field_5450;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z) {
        return 0;
    }
}
