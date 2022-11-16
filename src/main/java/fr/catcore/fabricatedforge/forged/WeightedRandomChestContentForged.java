package fr.catcore.fabricatedforge.forged;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;

public class WeightedRandomChestContentForged extends WeightedRandomChestContent {
    public WeightedRandomChestContentForged(ItemStack stack, int min, int max, int weight) {
        super(0, 0, min, max, weight);
        this.setItemStack(stack);
    }

    public WeightedRandomChestContentForged(int id, int i, int minCount, int maxCount, int weight) {
        super(id, i, minCount, maxCount, weight);
    }
}
