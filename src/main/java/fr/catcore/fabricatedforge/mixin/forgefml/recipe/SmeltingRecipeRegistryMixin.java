package fr.catcore.fabricatedforge.mixin.forgefml.recipe;

import fr.catcore.fabricatedforge.mixininterface.ISmeltingRecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(SmeltingRecipeRegistry.class)
public class SmeltingRecipeRegistryMixin implements ISmeltingRecipeRegistry {

    @Shadow private Map<Integer, ItemStack> ORIGINAL_PRODUCT_MAP;

    private Map<List<Integer>, ItemStack> metaSmeltingList = new HashMap<>();

    @Override
    public void addSmelting(int itemID, int metadata, ItemStack itemstack) {
        this.metaSmeltingList.put(Arrays.asList(itemID, metadata), itemstack);
    }

    @Override
    public ItemStack getSmeltingResult(ItemStack item) {
        if (item == null) {
            return null;
        } else {
            ItemStack ret = this.metaSmeltingList.get(Arrays.asList(item.id, item.getMeta()));
            return ret != null ? ret : this.ORIGINAL_PRODUCT_MAP.get(item.id);
        }
    }
}
