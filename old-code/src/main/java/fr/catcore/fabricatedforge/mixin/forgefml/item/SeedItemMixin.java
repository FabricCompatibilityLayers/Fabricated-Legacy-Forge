package fr.catcore.fabricatedforge.mixin.forgefml.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.SeedItem;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SeedItem.class)
public abstract class SeedItemMixin extends Item implements IPlantable {
    @Shadow private int plantId;

    protected SeedItemMixin(int id) {
        super(id);
    }

    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z) {
        return this.plantId == Block.NETHER_WART.id ? EnumPlantType.Nether : EnumPlantType.Crop;
    }

    @Override
    public int getPlantID(World world, int x, int y, int z) {
        return this.plantId;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z) {
        return 0;
    }
}
