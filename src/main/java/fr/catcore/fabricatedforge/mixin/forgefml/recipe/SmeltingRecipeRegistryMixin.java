package fr.catcore.fabricatedforge.mixin.forgefml.recipe;

import fr.catcore.fabricatedforge.mixininterface.IItem;
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

    @Shadow private Map<Integer, Float> PRODUCT_XP_MAP;
    private HashMap<List<Integer>, ItemStack> metaSmeltingList = new HashMap<>();
    private HashMap<List<Integer>, Float> metaExperience = new HashMap<>();

    @Override
    public void addSmelting(int itemID, int metadata, ItemStack itemstack, float experience) {
        this.metaSmeltingList.put(Arrays.asList(itemID, metadata), itemstack);
        this.metaExperience.put(Arrays.asList(itemID, metadata), experience);
    }

    @Override
    public ItemStack getSmeltingResult(ItemStack item) {
        if (item == null) {
            return null;
        } else {
            ItemStack ret = (ItemStack)this.metaSmeltingList.get(Arrays.asList(item.id, item.getData()));
            return ret != null ? ret : (ItemStack)this.ORIGINAL_PRODUCT_MAP.get(item.id);
        }
    }

    @Override
    public float getExperience(ItemStack item) {
        if (item != null && item.getItem() != null) {
            float ret = ((IItem)item.getItem()).getSmeltingExperience(item);
            if (ret < 0.0F && this.metaExperience.containsKey(Arrays.asList(item.id, item.getData()))) {
                ret = this.metaExperience.get(Arrays.asList(item.id, item.getData()));
            }

            if (ret < 0.0F && this.PRODUCT_XP_MAP.containsKey(item.id)) {
                ret = this.PRODUCT_XP_MAP.get(item.id);
            }

            return ret < 0.0F ? 0.0F : ret;
        } else {
            return 0.0F;
        }
    }

    @Override
    public Map<List<Integer>, ItemStack> getMetaSmeltingList() {
        return this.metaSmeltingList;
    }
}
