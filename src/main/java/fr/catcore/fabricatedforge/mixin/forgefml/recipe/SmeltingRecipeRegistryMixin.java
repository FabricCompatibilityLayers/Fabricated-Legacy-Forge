package fr.catcore.fabricatedforge.mixin.forgefml.recipe;

import fr.catcore.fabricatedforge.mixininterface.ISmeltingRecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Mixin(SmeltingRecipeRegistry.class)
public class SmeltingRecipeRegistryMixin implements ISmeltingRecipeRegistry {

    @Shadow private Map ORIGINAL_PRODUCT_MAP;
    @Unique
    private Map metaSmeltingList = new HashMap();

    @Override
    public void addSmelting(int itemID, int metadata, ItemStack itemstack) {
        this.metaSmeltingList.put(Arrays.asList(itemID, metadata), itemstack);
    }

    @Override
    public ItemStack getSmeltingResult(ItemStack item) {
        if (item == null) {
            return null;
        } else {
            ItemStack ret = (ItemStack)this.metaSmeltingList.get(Arrays.asList(item.id, item.getMeta()));
            return ret != null ? ret : (ItemStack)this.ORIGINAL_PRODUCT_MAP.get(item.id);
        }
    }
}
